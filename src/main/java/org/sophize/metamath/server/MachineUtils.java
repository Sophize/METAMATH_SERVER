package org.sophize.metamath.server;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import mmj.lang.*;
import mmj.verify.Grammar;
import org.sophize.datamodel.ProofResponse;
import org.sophize.datamodel.ResourcePointer;
import org.sophize.datamodel.ResourceType;
import org.sophize.datamodel.TruthValue;
import org.sophize.metamath.Utils;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static org.sophize.datamodel.ResourceType.ARGUMENT;
import static org.sophize.metamath.Utils.getLookupTermsForParseNode;
import static org.sophize.metamath.server.Databases.SET_DB;

public class MachineUtils {
  // So far we have needed only these two variable hypothesis for parsing the statement needed by
  // the machines in this server.
  // TODO: Determine hyps needed automatically. Don't hard code values here.
  private static final VarHyp[] HYPS_FOR_PARSING =
      new VarHyp[] {(VarHyp) Databases.getSetMMStmt("vx"), (VarHyp) Databases.getSetMMStmt("cN")};

  public static String[] toStringArray(List<ResourcePointer> ptrs) {
    return ptrs.stream().map(ResourcePointer::toString).toArray(String[]::new);
  }

  public static String[] toStringRefArray(List<Stmt> ptrs) {
    return ptrs.stream()
        .map(stmt -> ResourcePointer.assignable(ResourceType.PROPOSITION, stmt.getLabel()))
        .map(ResourcePointer::toString)
        .toArray(String[]::new);
  }

  public static List<String> getLookupTerms(String statement, String dbName) {
    ParseNode parsed;
    if (dbName.equals(SET_DB)) parsed = parseSetMMStatement(statement);
    else parsed = parseStatement(statement, dbName, new VarHyp[0]);
    return parsed == null ? null : getLookupTermsForParseNode(parsed);
  }

  public static ParseNode parseStatement(String statement, String dbName, VarHyp[] hyps) {
    Grammar grammar = Databases.getGrammar(dbName);

    String[] tokens = statement.split(" ");
    List<Sym> symList = new ArrayList<>();
    for (String token : tokens) {
      token = token.trim();
      if (Strings.isNullOrEmpty(token)) continue;
      if (token.equals("$p") || token.equals("$.")) continue;
      symList.add(grammar.symTbl.get(token));
    }

    Formula formula = new Formula(symList);
    ParseTree tree = grammar.parseFormulaWithoutSafetyNet(formula, hyps, 400000000);
    if (tree == null) throw new IllegalStateException("Couldn't parse: " + statement);
    return tree.getRoot();
  }

  public static ParseNode parseSetMMStatement(String statement) {
    // TODO: Remove the default VarHyp array used here.
    return parseSetMMStatement(statement, HYPS_FOR_PARSING);
  }

  public static ParseNode parseSetMMStatement(String statement, VarHyp[] hyps) {
    return parseStatement(statement, SET_DB, hyps);
  }

  public static String stripPropositionMarker(String statement) {
    if (statement == null) return null;
    statement = statement.trim();
    if (statement.startsWith("$p") && statement.endsWith("$."))
      return statement.substring(2, statement.length() - 4).trim();
    return statement;
  }

  public static List<Integer> getDigitsLenient(String s) {
    if (!s.matches("^[\\s0-9;]+$")) return null;
    int codePoint0 = "0".codePointAt(0);
    return s.codePoints()
        .map(d -> d - codePoint0)
        .filter(d -> d >= 0 && d <= 9)
        .boxed()
        .collect(Collectors.toList());
  }

  static ProofResponse responseWithMessage(TruthValue value, String message) {
    ProofResponse response = new ProofResponse();
    response.setTruthValue(value);
    response.setMessage(message);
    return response;
  }

  public static MachineProof getProofForSetMMAssrt(
      MetamathProposition prop,
      StepFactory stepFactory,
      Assrt assrt,
      Map<String, String> substitutions) {
    var stepNodes = getNodesForSetMMHypAndAssert(assrt, substitutions);
    Preconditions.checkArgument(stepNodes.stream().allMatch(Objects::nonNull));
    var newArg = argumentFromStepParseNodes(stepNodes, stepFactory, prop);
    return new MachineProof(
        newArg.getGeneratedPremises(),
        Map.of(prop.getResourcePtr(), newArg),
        newArg.getMachineArguments());
  }

  public static MetamathArgument getArgumentForSetMMGeneratedLemma(
      MetamathProposition prop,
      StepFactory stepFactory,
      MetamathProposition lemma,
      Map<String, String> substitutions) {
    var stepNodes = getNodesForSetMMHypAndAssert(lemma, substitutions);
    Preconditions.checkArgument(stepNodes.stream().allMatch(Objects::nonNull));
    return argumentFromStepParseNodes(stepNodes, stepFactory, prop);
  }

  private static List<ParseNode> getNodesForSetMMHypAndAssert(
      Assrt assrt, Map<String, String> substitutions) {
    return Stream.concat(Arrays.stream(assrt.getLogHypArray()), Stream.of(assrt))
        .map(
            stmt -> {
              String statement = Utils.getStatementWithSubstitutions(stmt, substitutions);
              return MachineUtils.parseStatement(statement, SET_DB, new VarHyp[0]);
            })
        .collect(toList());
  }

  private static List<ParseNode> getNodesForSetMMHypAndAssert(
      MetamathProposition proposition, Map<String, String> substitutions) {
    return Stream.concat(proposition.getLogHypArray().stream(), Stream.of(proposition.getAssrt()))
        .map(
            node -> {
              String unsubstituted = ParseNodeHelpers.asStatement(node);
              String statement = Utils.getStatementWithSubstitutions(unsubstituted, substitutions);
              return parseStatement(statement, SET_DB, new VarHyp[0]);
            })
        .collect(toList());
  }

  private static MetamathArgument argumentFromStepParseNodes(
      List<ParseNode> stepNodes, StepFactory stepFactory, MetamathProposition prop) {
    var steps = stepFactory.getSteps(stepNodes);
    var newArgumentPtr = ResourcePointer.ephemeral(ARGUMENT, prop.getResourcePtr().getId());
    return new MetamathArgument(newArgumentPtr, prop, steps);
  }

  private MachineUtils() {}
}
