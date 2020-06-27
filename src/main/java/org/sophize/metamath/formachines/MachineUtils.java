package org.sophize.metamath.formachines;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import mmj.lang.*;
import mmj.verify.Grammar;
import org.sophize.datamodel.MachineResponse;
import org.sophize.datamodel.ResourcePointer;
import org.sophize.datamodel.ResourceType;
import org.sophize.datamodel.TruthValue;
import org.sophize.metamath.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static org.sophize.datamodel.ResourceType.ARGUMENT;
import static org.sophize.metamath.Utils.getLookupTermsForParseNode;

public class MachineUtils {
  public static final String SET_DB = "set.mm";
  public static final String ISET_DB = "iset.mm";
  public static final String NF_DB = "nf.mm";

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
    var parsed = parseStatement(statement, dbName);
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
    return parseSetMMStatement(statement, new VarHyp[] {(VarHyp) Databases.getSetMMStmt("cN")});
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

  static MachineResponse responseWithMessage(TruthValue value, String message) {
    MachineResponse response = new MachineResponse();
    response.setTruthValue(value);
    response.setMessage(message);
    return response;
  }

  public static MachineProof getProofForAssrt(
      MetamathProposition prop,
      StepFactory stepFactory,
      Assrt assrt,
      Map<String, String> substitutions) {
    var stepNodes = getNodesForHypAndAssert(assrt, substitutions);
    Preconditions.checkArgument(stepNodes.stream().allMatch(Objects::nonNull));
    var newArg = argumentFromStepParseNodes(stepNodes, stepFactory, prop);
    return new MachineProof(
        newArg.getGeneratedPremises(),
        Map.of(prop.getResourcePtr(), newArg),
        newArg.getMachineArguments());
  }

  private static List<ParseNode> getNodesForHypAndAssert(
      Assrt assrt, Map<String, String> substitutions) {
    return Stream.concat(Arrays.stream(assrt.getLogHypArray()), Stream.of(assrt))
        .map(
            stmt ->
                MachineUtils.parseStatement(
                    Utils.getStatementWithSubstitutions(stmt, substitutions), SET_DB))
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
