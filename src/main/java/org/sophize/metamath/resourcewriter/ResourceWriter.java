package org.sophize.metamath.resourcewriter;

import com.fasterxml.jackson.databind.ObjectMapper;
import mmj.gmff.GMFFManager;
import mmj.lang.*;
import mmj.pa.ProofAsst;
import mmj.pa.ProofAsstPreferences;
import mmj.tl.TheoremLoader;
import mmj.tl.TlPreferences;
import mmj.util.BatchMMJ2;
import mmj.util.OutputBoss;
import mmj.verify.Grammar;
import mmj.verify.VerifyProofs;
import org.sophize.datamodel.*;
import org.sophize.metamath.server.Databases;
import org.sophize.metamath.server.MachineId;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

import static org.sophize.datamodel.ResourceUtils.toJsonString;
import static org.sophize.metamath.Utils.myAssert;
import static org.sophize.metamath.resourcewriter.TempTerm.createPrimitiveMetamathTerm;

public class ResourceWriter {
  private static final String OUTPUT_DIRECTORY = "_data";
  private static final String MANUAL_DIRECTORY = "manual";
  private static final String RUN_PARMS_FILE = "writer_resources/RunParmsResourceWriter.txt";
  private static List<ResourceStore> STORES = new ArrayList<>();

  private static final Map<String, String> TYPE_TO_COLOR_LATEX =
      Map.of(
          "wff", "\\color{blue}",
          "class", "\\color{#C3C}",
          "setvar", "\\color{red}");

  public static void main(String[] args) throws IOException {
    resetOutputDirectory();
    BatchMMJ2 batchMMJ2 = new BatchMMJ2();
    batchMMJ2.generateSvcCallback(new String[] {RUN_PARMS_FILE}, ResourceWriter::svcCallback);
  }

  private static void resetOutputDirectory() throws IOException {
    Path directory = Paths.get(OUTPUT_DIRECTORY);
    // read java doc, Files.walk need close the resources.
    // try-with-resources to ensure that the stream's open directories are closed
    try (Stream<Path> walk = Files.walk(directory)) {
      walk.sorted(Comparator.reverseOrder())
          .forEach(
              path -> {
                try {
                  Files.delete(path);
                } catch (IOException e) {
                  System.exit(-1);
                }
              });
    }catch (Exception e){

    }

    File file = new File(OUTPUT_DIRECTORY);
    file.mkdir();

    Path manualDirectory = Paths.get(MANUAL_DIRECTORY);
    try (Stream<Path> walk = Files.list(manualDirectory)) {
      walk.forEach(
          src -> {
            Path dest = Paths.get(OUTPUT_DIRECTORY, src.getFileName().toString());
            try {
              Files.copy(src, dest);
            } catch (Exception e) {
              System.exit(-1);
            }
          });
    }
  }

  private static void svcCallback(
      Messages messages,
      OutputBoss outputBoss,
      LogicalSystem logicalSystem,
      VerifyProofs verifyProofs,
      Grammar grammar,
      WorkVarManager workVarManager,
      ProofAsstPreferences proofAsstPreferences,
      ProofAsst proofAsst,
      TlPreferences tlPreferences,
      TheoremLoader theoremLoader,
      File svcFolder,
      Map<String, String> svcArgs) {
    Databases.addGrammar(
        null, null, null, null, grammar, null, null, null, null, null, null, svcArgs);
    processGrammar(grammar, svcArgs.get("databaseName"));
  }

  private static void processGrammar(Grammar grammar, String databaseName) {
    try {
      ResourceStore resourceStore = new ResourceStore(databaseName);
      resourceStore.createResources(grammar);
      STORES.add(resourceStore);
      if (STORES.size() == 2) ResourceWriter.combineAndWriteStores(STORES);
    } catch (Exception e) {
      e.printStackTrace();
      System.out.println(e.getMessage());
    }
  }

  private static void combineAndWriteStores(List<ResourceStore> stores) throws Exception {
    ResourceStore firstStore = stores.get(0);
    Map<String, TempTerm> combinedTerms = firstStore.termData;
    for (int i = 1; i < stores.size(); i++) {
      combinedTerms = combineTerms(combinedTerms, stores.get(i).termData);
    }

    fixAndWriteLatexMap(combinedTerms);

    Map<String, TempProposition> combinedPropositions = firstStore.propositionData;
    Map<String, TempArgument> combinedArguments = firstStore.argumentData;

    Map<String, Beliefset> beliefsets = new HashMap<>();
    fixAxioms(firstStore.defaultBeliefset, new HashMap<>());
    firstStore.defaultBeliefset.setPermanentPtr("#metamath/B.default");
    beliefsets.put(firstStore.defaultBeliefset.getNames()[0], firstStore.defaultBeliefset);
    beliefsets.put(firstStore.minimalBeliefset.getNames()[0], firstStore.minimalBeliefset);

    for (int i = 1; i < stores.size(); i++) {
      ResourceStore store = stores.get(i);
      Map<String, String> dedupedPropsIdMap = new TreeMap<>();
      String dedupPostfix = store.getDedupPostfix();
      combinedPropositions =
          combinePropositions(
              combinedPropositions, store.propositionData, dedupPostfix, dedupedPropsIdMap);
      combinedArguments =
          combineArguments(
              combinedArguments,
              store.argumentData,
              dedupPostfix,
              dedupedPropsIdMap,
              combinedPropositions);
      fixAxioms(store.defaultBeliefset, dedupedPropsIdMap);
      beliefsets.put(store.defaultBeliefset.getNames()[0], store.defaultBeliefset);
    }
    writeResources(combinedTerms, combinedPropositions, combinedArguments, beliefsets);
  }

  private static void fixAndWriteLatexMap(Map<String, TempTerm> combinedTerms) throws Exception {
    for (TempTerm tempTerm : combinedTerms.values()) {
      if (tempTerm.syntaxes.get(0) instanceof VarHyp) {
        VarHyp hyp = (VarHyp) tempTerm.syntaxes.get(0);
        Cnst type = hyp.getTyp();
        Var var = (Var) hyp.getFormula().getSym()[1];

        String existingMapping = GMFFManager.LATEXDEF_MAP.get(var.getId());
        myAssert(existingMapping != null);
        String colorInfo = TYPE_TO_COLOR_LATEX.get(type.getId());
        if (!existingMapping.startsWith(colorInfo)) {
          GMFFManager.LATEXDEF_MAP.put(
              var.getId(), TYPE_TO_COLOR_LATEX.get(type.getId()) + existingMapping);
        }
      }
    }
    GMFFManager.LATEXDEF_MAP.put("|-", "\\scriptsize\\color{#999} \\vdash");
    GMFFManager.LATEXDEF_MAP.put("wff", "\\scriptsize\\color{#999} {\\rm wff}");
    GMFFManager.LATEXDEF_MAP.put("class", "\\scriptsize\\color{#999} {\\rm class}");
    GMFFManager.LATEXDEF_MAP.put("|", "\\vert");
    GMFFManager.LATEXDEF_MAP.put("#", "\\#");

    Files.createDirectories(Paths.get(OUTPUT_DIRECTORY));
    try (PrintStream out =
        new PrintStream(
            new FileOutputStream(
                Paths.get(OUTPUT_DIRECTORY, "metamath_set_mm_latex_map").toString()))) {
      out.print(new ObjectMapper().writeValueAsString(GMFFManager.LATEXDEF_MAP));
    }
  }

  private static void fixAxioms(Beliefset beliefset, Map<String, String> dedupedPropsIdMap) {
    if (beliefset == null || beliefset.getUnsupportedPropositionPtrs() == null) return;
    for (int i = 0; i < beliefset.getUnsupportedPropositionPtrs().length; i++) {
      String label = beliefset.getUnsupportedPropositionPtrs()[i].substring(3); // Remove the "#P_"

      String reference = "#P_" + dedupedPropsIdMap.getOrDefault(label, label);
      beliefset.getUnsupportedPropositionPtrs()[i] = reference;
    }
  }

  private static void writeResources(
      Map<String, TempTerm> termData,
      Map<String, TempProposition> propositionData,
      Map<String, TempArgument> argumentData,
      Map<String, Beliefset> beliefsets)
      throws IOException {
    String directory = getDirectory(Term.class);
    for (Map.Entry<String, Term> entry : getHardcodedTerms().entrySet()) {
      ResourceUtils.writeJson(directory, entry.getKey(), entry.getValue());
    }
    for (Map.Entry<String, TempTerm> entry : termData.entrySet()) {
      ResourceUtils.writeJson(directory, entry.getKey(), entry.getValue().getTerm());
    }

    directory = getDirectory(Proposition.class);
    for (Map.Entry<String, TempProposition> entry : propositionData.entrySet()) {
      ResourceUtils.writeJson(directory, entry.getKey(), entry.getValue().getProposition());
    }

    directory = getDirectory(Argument.class);
    for (Map.Entry<String, TempArgument> entry : argumentData.entrySet()) {
      ResourceUtils.writeJson(
          directory, entry.getKey(), entry.getValue().getArgument(propositionData));
    }

    directory = getDirectory(Beliefset.class);
    for (Map.Entry<String, Beliefset> entry : beliefsets.entrySet()) {
      ResourceUtils.writeJson(directory, entry.getKey(), entry.getValue());
    }

    directory = getDirectory(Machine.class);
    for (var machineId : MachineId.values()) {
      var machine = machineId.getMachine();
      ResourceUtils.writeJson(directory, machine.getAssignablePtr().getId(), machine.toDatamodel());
    }
  }

  private static Map<String, Term> getHardcodedTerms() {
    Term wffTerm = createPrimitiveMetamathTerm("wff", "A well formed formula");
    Term classTerm =
        createPrimitiveMetamathTerm(
            "class",
            "An expression that is a syntactically valid class expression. All valid set "
                + "expressions are also valid class expression, so expressions of sets normally "
                + "have the class typecode. Use the class typecode, not the setvar typecode, for "
                + "the type of set expressions unless you are specifically identifying a single "
                + "set variable.");
    Term setvarTerm =
        createPrimitiveMetamathTerm(
            "setvar",
            "Individual set variable type. Note that this is not the type of an arbitrary "
                + "set expression, instead, it is used to ensure that there is only a single "
                + "symbol used after quantifiers like for-all (∀) and there-exists (∃).");

    return Map.ofEntries(
        Map.entry("wff", wffTerm),
        Map.entry("class", classTerm),
        Map.entry("setvar", setvarTerm),
        Map.entry("distinct_variable", getDistinctVariableTerm()));
  }

  private static String getDirectory(Class tClass) throws IOException {
    Path dirPath = Paths.get(OUTPUT_DIRECTORY, tClass.getSimpleName().toLowerCase());
    Files.createDirectories(dirPath);
    return dirPath.toString();
  }

  private static Map<String, TempTerm> combineTerms(
      Map<String, TempTerm> terms1, Map<String, TempTerm> terms2) {
    Map<String, TempTerm> combined = new HashMap<>();
    for (Map.Entry<String, TempTerm> entry : terms1.entrySet()) {
      String id = entry.getKey();
      TempTerm term1 = entry.getValue();
      TempTerm term2 = terms2.get(id);
      if (term2 == null) {
        combined.put(id, term1);
      } else {
        combined.put(id, mergeTerms(term1, term2));
      }
    }
    for (Map.Entry<String, TempTerm> entry : terms2.entrySet()) {
      String id = entry.getKey();
      TempTerm term2 = entry.getValue();
      if (!terms1.containsKey(id)) {
        combined.put(id, term2);
      }
    }
    return combined;
  }

  private static Map<String, TempProposition> combinePropositions(
      Map<String, TempProposition> props1,
      Map<String, TempProposition> props2,
      String dedupPostfix,
      Map<String, String> dedupedPropsIdMap) {
    Map<String, TempProposition> combined = new HashMap<>();
    for (Map.Entry<String, TempProposition> entry : props1.entrySet()) {
      String id = entry.getKey();
      TempProposition prop1 = entry.getValue();
      TempProposition prop2 = props2.get(id);
      if (prop2 == null) {
        combined.put(id, prop1);
      } else {
        TempProposition merged = mergePropositions(prop1, prop2);
        if (merged != null) {
          combined.put(id, merged);
          for (int i = 0; i < merged.assrts.get(0).getLogHypArray().length; i++) {
            String newHypId = prop2.assrts.get(0).getLogHypArray()[i].getLabel();
            String mergedHypId = merged.assrts.get(0).getLogHypArray()[i].getLabel();
            if (newHypId.equals(mergedHypId)) continue;
            // Props are merged only when the order of hypothesis is same in original and alternate.
            dedupedPropsIdMap.put(newHypId, mergedHypId);
          }
        } else {
          combined.put(id, prop1);
          combined.put(id + dedupPostfix, prop2);
          dedupedPropsIdMap.put(id, id + dedupPostfix);
        }
      }
    }
    for (Map.Entry<String, TempProposition> entry : props2.entrySet()) {
      String id = entry.getKey();
      if (!props1.containsKey(id)) combined.put(id, entry.getValue());
    }
    return combined;
  }

  private static Map<String, TempArgument> combineArguments(
      Map<String, TempArgument> args1,
      Map<String, TempArgument> args2,
      String dedupPostfix,
      Map<String, String> dedupedPropsIdMap,
      Map<String, TempProposition> propositionData) {
    Map<String, TempArgument> combined = new HashMap<>();
    for (Map.Entry<String, TempArgument> entry : args1.entrySet()) {
      String id = entry.getKey();
      TempArgument arg1 = entry.getValue();
      TempArgument arg2 = args2.get(id);
      if (arg2 == null) {
        combined.put(id, arg1);
      } else {
        // TODO: check if bug: what if conclusion or premise has same id but has been deduped?
        arg2.setDedupedPropsIdMap(dedupedPropsIdMap);
        TempArgument merged = mergeArguments(arg1, arg2, propositionData);
        if (merged != null) combined.put(id, merged);
        else {
          combined.put(id, arg1);
          combined.put(id + dedupPostfix, arg2);
        }
      }
    }

    for (Map.Entry<String, TempArgument> entry : args2.entrySet()) {
      String id = entry.getKey();
      if (!args1.containsKey(id)) {
        TempArgument arg = entry.getValue();
        arg.setDedupedPropsIdMap(dedupedPropsIdMap);
        combined.put(id, entry.getValue());
      }
    }
    return combined;
  }

  private static TempTerm mergeTerms(TempTerm term1, TempTerm term2) {
    if (!term1.getPhrase().equals(term2.getPhrase())
        || !term1.getDefinition().equals(term2.getDefinition())
        || !term1.getAssignableId().equals(term2.getAssignableId())
        || !String.join("..", term1.getLookupTerms())
            .equals(String.join("..", term2.getLookupTerms()))) {
      System.out.println("Languages are different, merge not possible.");
      throw new IllegalStateException("Languages are different, merge not possible.");
    }
    List<Stmt> syntaxes = new ArrayList<>(term1.syntaxes);
    syntaxes.addAll(term2.syntaxes);
    List<Stmt> defns = new ArrayList<>(term1.defns);
    defns.addAll(term2.defns);
    return new TempTerm(syntaxes, defns, term1.sym, term1.getAssignableId());
  }

  private static TempProposition mergePropositions(TempProposition prop1, TempProposition prop2) {
    if (!Helpers.areSameProps(prop1, prop2) || !prop1.distinctVars.equals(prop2.distinctVars))
      return null;
    List<Assrt> assrts = new ArrayList<>(prop1.assrts);
    assrts.addAll(prop2.assrts);
    return new TempProposition(assrts, prop1.distinctVars);
  }

  private static TempArgument mergeArguments(
      TempArgument arg1, TempArgument arg2, Map<String, TempProposition> propositionData) {
    try {
      String arg1Str = toJsonString(arg1.getArgument(propositionData));
      String arg2Str = toJsonString(arg2.getArgument(propositionData));
      return arg1Str.equals(arg2Str) ? arg1 : null;
    } catch (Exception e) {
      return null;
    }
  }

  private static Term getDistinctVariableTerm() {
    Term distinctVariableTerm = new Term();
    distinctVariableTerm.setLanguage(Language.INFORMAL);
    distinctVariableTerm.setMetaLanguage(MetaLanguage.INFORMAL);
    distinctVariableTerm.setPhrase("distinct variable");
    distinctVariableTerm.setDefinition(
        "If a set of variables are declared distinct in an assertion, the assertion is claimed to "
            + "be true only if the variables are not substituted with each other. Secondly, "
            + "whenever these variables are substituted with expressions - these expression "
            + "must have no variables in common. Finally, each possible pair of variables, one "
            + "from each (substituted) expression, must have been declared distinct as well.");
    Citation bookCitation = new Citation();
    bookCitation.setTextCitation("Section 4.2.4 of [metamath book](http://us.metamath.org/#book)");
    distinctVariableTerm.setCitations(new Citation[] {bookCitation});
    return distinctVariableTerm;
  }
}
