package org.sophize.metamath.resourcewriter;

import mmj.gmff.GMFFManager;
import mmj.lang.*;
import mmj.verify.*;
import org.sophize.datamodel.Beliefset;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;
import static org.sophize.metamath.Utils.*;
import static org.sophize.metamath.resourcewriter.Constants.MINIMAL_AXIOMS;
import static org.sophize.metamath.resourcewriter.Helpers.areSameProps;

class ResourceStore {
  static Map<String, String> latexdefMap;
  private static final List<String> TERM_DEFINING_AXIOM_TYPECODES =
      Arrays.asList("class", "wff", "setvar");
  private static Cnst isProvable = null;
  private int seqLimit = Integer.MAX_VALUE; // 924021000; // 421000

  // Statement label (used to create the resource) to resources.
  final Map<String, TempTerm> termData = new HashMap<>();
  final Map<String, TempProposition> propositionData = new TreeMap<>();
  final Map<String, TempArgument> argumentData = new HashMap<>();

  // Statement(argument) label to dummy variables used in the argument.
  private final Map<String, List<Var>> dummyVariablesMap = new HashMap<>();

  // All axioms (with typecode |-) in the Grammar.
  Beliefset defaultBeliefset = null;
  Beliefset minimalBeliefset = null;

  private final Map<String, String> alternateToOriginalStmt = new HashMap<>();
  private final String databaseName;

  ResourceStore(String databaseName) {
    latexdefMap = GMFFManager.LATEXDEF_MAP;
    this.databaseName = databaseName;
  }

  String getDedupPostfix() {
    return DEDUP_POSTFIX + this.databaseName.charAt(0);
  }

  void createResources(Grammar grammar) {
    isProvable = grammar.getProvableLogicStmtTypArray()[0];
    List<Stmt> orderedStmts =
        grammar.stmtTbl.values().stream()
            .sorted(Comparator.comparing(Stmt::getSeq))
            .collect(toList());

    addTerms(orderedStmts);
    addPropositions(orderedStmts, grammar);
    defaultBeliefset = getDefaultBeliefset(orderedStmts, databaseName, propositionData, false);
    if (databaseName.equals("set.mm")) { // TODO: add minimal for iset.mm too.
      minimalBeliefset = getDefaultBeliefset(orderedStmts, databaseName, propositionData, true);
    }
    addArguments(orderedStmts, grammar);
  }

  private void addTerms(List<Stmt> orderedStmts) {
    for (int i = 0; i < orderedStmts.size(); i++) {
      Stmt stmt = orderedStmts.get(i);
      if (stmt.getSeq() > seqLimit) continue;
      addTermsFromStmt(stmt, orderedStmts, i);
    }
  }

  private void addTermsFromStmt(Stmt stmt, List<Stmt> orderedStmts, int stmtIndex) {
    if (!isTermDefiningStatement(stmt)) return;
    if (stmt instanceof VarHyp) {
      TempTerm term = new TempTerm(stmt, null, null, stmt.getLabel());
      String assignableId = term.getAssignableId();
      myAssert(!termData.containsKey(assignableId));
      termData.put(assignableId, term);
      return;
    }
    myAssert(stmt instanceof Axiom || stmt instanceof Theorem);
    long numConstChars = getNumConstChars(stmt);
    int constIndex = 0;
    Sym[] formula = stmt.getFormula().getSym();
    for (int i = 1; i < formula.length; i++) { // skip typecode
      Sym sym = formula[i];
      if (sym instanceof Var) continue;
      if (sym.getId().equals("(") || sym.getId().equals(")")) continue;
      Stmt defn = findDefinition(orderedStmts, stmtIndex + 1, sym);
      String assignableId =
          getAssignableIdForTermInStmt(stmt.getLabel(), constIndex, numConstChars);

      TempTerm term = new TempTerm(stmt, defn, sym, assignableId);

      myAssert(!termData.containsKey(assignableId));
      termData.put(assignableId, term);
      constIndex++;
    }
    myAssert(constIndex == numConstChars);
  }

  private void addPropositions(List<Stmt> orderedStmts, Grammar grammar) {
    for (Stmt stmt : orderedStmts) {
      Assrt assrt = getPropositionAssrt(stmt);
      if (assrt == null) continue;
      if (stmt.getSeq() % 1000000 == 0) System.out.println("prop seq: " + stmt.getSeq());
      List<List<Var>> distinctVars = getDistinctAndSaveDummyVariables(assrt, grammar.symTbl);
      TempProposition prop = new TempProposition(assrt, distinctVars);
      propositionData.put(assrt.getLabel(), prop);
    }
    Map<Integer, List<Assrt>> stmtHashes = new HashMap<>();
    for (Stmt stmt : orderedStmts) {
      Assrt assrt = getPropositionAssrt(stmt);
      if (assrt == null) continue;
      int hash = stmt.getFormula().hashCode();
      if (stmtHashes.containsKey(hash)) {
        stmtHashes.get(hash).add(assrt);
      } else {
        stmtHashes.put(hash, new ArrayList<>(List.of(assrt)));
      }
    }
    for (var hashVals : stmtHashes.values()) {
      List<Assrt> candidates = new ArrayList<>(hashVals);
      while (candidates.size() > 1) candidates = addDuplicatesToMap(candidates);
    }
    for (String alternate : alternateToOriginalStmt.keySet()) {
      propositionData.remove(alternate);
    }
  }

  private Assrt getPropositionAssrt(Stmt stmt) {
    if (!(stmt instanceof Assrt)) return null;
    if (!stmt.getFormula().getSym()[0].getId().equals("|-")) return null;
    if (stmt.getSeq() > seqLimit) return null;
    return (Assrt) stmt;
  }

  private List<Assrt> addDuplicatesToMap(List<Assrt> candidates) {
    Set<Assrt> duplicates = new HashSet<>();
    List<Assrt> remaining = new ArrayList<>();
    Assrt first = candidates.get(0);
    duplicates.add(first);
    for (int i = 1; i < candidates.size(); i++) {
      var candidate = candidates.get(i);
      boolean isIdentical =
          areSameProps(
              propositionData.get(first.getLabel()), propositionData.get(candidate.getLabel()));

      if (isIdentical) duplicates.add(candidate);
      else remaining.add(candidate);
    }
    if (duplicates.size() <= 1) return remaining;
    Assrt original = findOriginal(duplicates, MINIMAL_AXIOMS.get(databaseName));
    for (Assrt duplicate : duplicates) {
      if (duplicate == original) continue;
      putIfNotDifferent(alternateToOriginalStmt, duplicate.getLabel(), original.getLabel());
      for (int i = 0; i < duplicate.getLogHypArray().length; i++) {
        // assume that the order of hypothesis is the same in original and alternate.
        String alternateHypId = duplicate.getLogHypArray()[i].getLabel();
        String originalHypId = original.getLogHypArray()[i].getLabel();
        if (alternateHypId.equals(originalHypId)) continue;
        // putIfNotDifferent(alternateToOriginalStmt, alternateHypId, originalHypId);
        // In rare cases, their may be two original to a single alternate hypothesis
        // Eg. for rexbiiOLD.1 the originals are rexbii.1 and ralbii.1 (which are the same thing)
        alternateToOriginalStmt.put(alternateHypId, originalHypId);
      }
    }
    return remaining;
  }

  private static Assrt findOriginal(Set<Assrt> duplicates, List<String> minimalAxioms) {
    List<Assrt> sortedDuplicates =
        duplicates.stream().sorted(Comparator.comparing(Stmt::getSeq)).collect(toList());
    var axioms =
        sortedDuplicates.stream().filter(assrt -> assrt instanceof Axiom).collect(toList());
    if (axioms.size() > 0) {
      myAssert(axioms.size() == 1);
      if (sortedDuplicates.get(0) instanceof Axiom) return sortedDuplicates.get(0);

      myAssert(!minimalAxioms.contains(axioms.get(0).getLabel()));
      sortedDuplicates.removeIf(assrt -> assrt instanceof Axiom);
    }
    Assrt smallestLabel = null;
    for (var assrt : sortedDuplicates) {
      if (smallestLabel == null || smallestLabel.getLabel().length() > assrt.getLabel().length()) {
        smallestLabel = assrt;
      }
    }
    return smallestLabel;
  }

  private Beliefset getDefaultBeliefset(
      List<Stmt> orderedStmts,
      String datasetName,
      Map<String, TempProposition> propositionData,
      boolean minimalOnly) {
    List<String> axioms = new ArrayList<>();
    for (Stmt stmt : orderedStmts) {
      if (stmt.getSeq() > seqLimit) continue;
      if (!(stmt instanceof Axiom)) continue;
      if (!stmt.getTyp().getId().equals("|-")) continue;

      String label = stmt.getLabel();
      label = alternateToOriginalStmt.getOrDefault(label, label);
      myAssert(propositionData.containsKey(label));

      boolean isMinimalAxiom = MINIMAL_AXIOMS.get(datasetName).contains(label);
      boolean isDefinition = label.startsWith("df-");
      if (!minimalOnly || isMinimalAxiom || isDefinition) {
        axioms.add("#P_" + label);
      }
    }
    // Get all the axioms listed before definitions
    axioms.sort(Comparator.comparing(s -> s.substring(0, "#P_ax".length())));
    String name = datasetName;
    if (name.endsWith(".mm")) name = name.substring(0, name.length() - ".mm".length());
    Beliefset beliefset = new Beliefset();
    beliefset.setNames(new String[] {name + (minimalOnly ? "_minimal" : "")});
    beliefset.setIndexable(true);
    beliefset.setDescription("Belief set with all axioms and definitions from " + datasetName);
    beliefset.setUnsupportedPropositionPtrs(axioms.toArray(String[]::new));
    return beliefset;
  }

  private static boolean isTermDefiningStatement(Stmt stmt) {
    Sym[] formula = stmt.getFormula().getSym();
    if (!TERM_DEFINING_AXIOM_TYPECODES.contains(formula[0].getId())) return false;
    myAssert(stmt instanceof VarHyp || stmt instanceof Axiom || stmt instanceof Theorem);
    return true;
  }

  private List<List<Var>> getDistinctAndSaveDummyVariables(Assrt assrt, Map<String, Sym> symTbl) {
    Set<String> idsInProp = new HashSet<>();
    Set<String> dummyVariableIds = new HashSet<>();

    final DjVars[] comboDvArray =
        DjVars.sortAndCombineDvArrays(
            assrt.getMandFrame().djVarsArray,
            (assrt instanceof Theorem) ? ((Theorem) assrt).getOptFrame().djVarsArray : null);

    addFormulaVarsToSet(assrt.getFormula(), idsInProp);
    Arrays.stream(assrt.getLogHypArray())
        .forEach(hyp -> addFormulaVarsToSet(hyp.getFormula(), idsInProp));

    if (assrt instanceof Theorem) {
      List<ProofDerivationStepEntry> proofSteps;
      try {
        proofSteps =
            new VerifyProofs()
                .getProofDerivationSteps((Theorem) assrt, true, HypsOrder.Correct, isProvable);
      } catch (VerifyException | ArrayIndexOutOfBoundsException e) {
        proofSteps = new ArrayList<>();
      }

      proofSteps.forEach(proofStep -> addFormulaVarsToSet(proofStep.formula, dummyVariableIds));
    }
    dummyVariableIds.removeAll(idsInProp);
    dummyVariablesMap.put(
        assrt.getLabel(),
        dummyVariableIds.stream()
            .map(id -> (Var) symTbl.get(id))
            .sorted(Comparator.comparing(Var::getId))
            .collect(toList()));

    var inPropDvs =
        Arrays.stream(comboDvArray)
            .filter(djvars -> idsInProp.contains(djvars.getVarLo().getId()))
            .filter(djvars -> idsInProp.contains(djvars.getVarHi().getId()))
            .toArray(DjVars[]::new);
    final List<List<Var>> comboDvGroups = ScopeFrame.consolidateDvGroups(inPropDvs);
    return comboDvGroups.stream()
        .map(
            dvGroup ->
                dvGroup.stream()
                    .filter(var -> idsInProp.contains(var.getId()))
                    .sorted(Comparator.comparing(Var::getId))
                    .collect(toList()))
        .filter(dvGroup -> dvGroup.size() > 1)
        .sorted(
            Comparator.comparing(
                varList -> varList.stream().map(Var::getId).collect(Collectors.joining(" "))))
        .collect(Collectors.toList());
  }

  private void addArguments(List<Stmt> orderedStmts, Grammar grammar) {
    // Below maybe wrong! Hyps should be based on the context of the formula, not all hyps.
    Hyp[] hyps = orderedStmts.stream().filter(s -> s instanceof Hyp).toArray(Hyp[]::new);

    for (Stmt stmt : orderedStmts) {
      if (stmt.getSeq() > seqLimit) break;
      if (stmt.getSeq() % 1000000 == 0) System.out.println("arg seq: " + stmt.getSeq());
      TempArgument argument = getArgument(stmt, hyps, grammar);
      if (argument != null) argumentData.put(stmt.getLabel(), argument);
    }
  }

  private TempArgument getArgument(Stmt stmt, Hyp[] hyps, Grammar grammar) {
    // mmj.verify.VerifyException: E-PR-0020 Theorem bj-0: Step 5: Proof Worksheet generation
    // halted because theorem has invalid proof? No proof steps created for Proof Worksheet!
    if (stmt.getLabel().equals("bj-0")) return null;
    if (!(stmt instanceof Theorem)) return null;
    try {
      List<ProofDerivationStepEntry> proofSteps =
          new VerifyProofs()
              .getProofDerivationSteps((Theorem) stmt, true, HypsOrder.Correct, isProvable);

      List<ParseTree> parsedSteps =
          proofSteps.stream()
              .map(
                  step -> {
                    if (step.formulaParseTree != null) return step.formulaParseTree;
                    return grammar.parseFormulaWithoutSafetyNet(step.formula, hyps, stmt.getSeq());
                  })
              .collect(toList());
      return new TempArgument(
          stmt,
          proofSteps,
          dummyVariablesMap.get(stmt.getLabel()),
          parsedSteps,
          termData,
          alternateToOriginalStmt);
    } catch (VerifyException | ArrayIndexOutOfBoundsException e) {
      System.out.println(e.toString());
      return null;
    }
  }

  private static Stmt findDefinition(List<Stmt> orderedStmts, int startingIndex, Sym symId) {
    for (int i = startingIndex; i < orderedStmts.size(); i++) {
      Stmt stmt = orderedStmts.get(i);
      if (!(stmt instanceof Axiom)) {
        continue;
      }
      Sym[] formula = stmt.getFormula().getSym();
      if (!formula[0].getId().equals("|-")) continue;
      for (Sym formulaSym : formula) {
        if (formulaSym.getId().equals(symId.getId())) {
          return stmt;
        }
      }
    }
    return null;
  }

  private static void addFormulaVarsToSet(Formula formula, Set<String> varIdSet) {
    Arrays.stream(formula.getSym())
        .filter(sym -> sym instanceof Var)
        .forEach(sym -> varIdSet.add(sym.getId()));
  }
}
