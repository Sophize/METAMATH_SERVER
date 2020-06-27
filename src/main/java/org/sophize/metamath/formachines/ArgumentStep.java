package org.sophize.metamath.formachines;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import mmj.lang.Assrt;
import mmj.lang.Stmt;
import org.sophize.datamodel.ResourcePointer;
import org.sophize.metamath.Utils;
import org.sophize.metamath.formachines.machines.MetamathMachine;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.sophize.datamodel.ResourceType.PROPOSITION;
import static org.sophize.metamath.Utils.getLookupTermsForParseNode;
import static org.sophize.metamath.formachines.Databases.SET_DB;

public class ArgumentStep {

  final List<Integer> hypIndices;
  final ResourcePointer reference;
  final String hypothesis;
  final String expression;
  final List<String> lookupTerms;
  final MetamathMachine machineForProof;

  private ArgumentStep(
      List<Integer> hypIndices,
      ResourcePointer reference,
      String hypothesis,
      String expression,
      List<String> lookupTerms,
      MetamathMachine machineForProof) {
    if ((reference == null) == (Strings.isNullOrEmpty(hypothesis)))
      throw new IllegalStateException("One of hypothesis or reference must be provided");
    this.hypIndices = hypIndices;
    this.reference = reference;
    this.hypothesis = hypothesis;
    this.expression = expression;
    this.lookupTerms = lookupTerms;
    this.machineForProof = machineForProof;
  }

  public static ArgumentStep fromEphemeralReference(
      MetamathProposition proposition,
      MetamathMachine machineForProof,
      String dbName,
      List<Integer> hypIndices,
      Map<String, String> substitutions) {
    // TODO: input actual hypothesis and verify/compute substitutions.
    String expression = ParseNodeHelpers.asStatement(proposition.getAssrt());
    List<String> lookupTerms;
    if (!substitutions.isEmpty()) {
      expression = Utils.getStatementWithSubstitutions(expression, substitutions);
      lookupTerms = MachineUtils.getLookupTerms(expression, dbName);
    } else {
      lookupTerms = getLookupTermsForParseNode(proposition.getAssrt());
    }

    return new ArgumentStep(
        hypIndices, proposition.getResourcePtr(), "", expression, lookupTerms, machineForProof);
  }

  public static ArgumentStep fromEphemeralReference(
      MetamathProposition proposition, MetamathMachine machineForProof) {
    return fromEphemeralReference(proposition, machineForProof, SET_DB, List.of(), Map.of());
  }

  public static ArgumentStep fromDbReference(
      Stmt stmt, String dbName, List<Integer> hypIndices, Map<String, String> substitutions) {
    // TODO: input actual hypothesis and verify/compute substitutions.
    Preconditions.checkArgument(((Assrt) stmt).getLogHypArrayLength() == hypIndices.size());
    ResourcePointer ptr = ResourcePointer.assignable(PROPOSITION, stmt.getLabel()); // can be wrong!

    String expression = Utils.getStatementWithSubstitutions(stmt, substitutions);
    return new ArgumentStep(
        hypIndices, ptr, "", expression, MachineUtils.getLookupTerms(expression, dbName), null);
  }

  public static ArgumentStep fromSetMM(
      Stmt stmt, List<Integer> hypIndices, Map<String, String> substitutions) {
    return fromDbReference(stmt, SET_DB, hypIndices, substitutions);
  }

  public static ArgumentStep fromSetMM(Stmt stmt) {
    return fromDbReference(stmt, SET_DB, List.of(), Map.of());
  }

  public static ArgumentStep fromSetMM(Stmt stmt, List<Integer> hypIndices) {
    return fromDbReference(stmt, SET_DB, hypIndices, Map.of());
  }

  public static ArgumentStep fromSetMMHypothesis(String expression, String label) {
    Preconditions.checkArgument(!Strings.isNullOrEmpty(expression));
    var lookupTerms = MachineUtils.getLookupTerms(expression, MachineUtils.SET_DB);
    return new ArgumentStep(List.of(), null, label, expression, lookupTerms, null);
  }

  ArgumentStep withHypIndices(List<Integer> hypIndices) {
    return new ArgumentStep(
        hypIndices, reference, hypothesis, expression, lookupTerms, machineForProof);
  }

  ArgumentStep replace(ResourcePointer original, ResourcePointer updated) {
    if (!original.equals(reference)) return this;
    return new ArgumentStep(
        hypIndices, updated, hypothesis, expression, lookupTerms, machineForProof);
  }

  String getHypString() {
    return hypIndices.stream()
        .map(index -> index + 1)
        .map(Object::toString)
        .collect(Collectors.joining(", " + ""));
  }

  String referenceForArgument() {
    if (reference != null) return reference.toString();
    return hypothesis;
  }

  @Override
  public String toString() {
    String indices = hypIndices.stream().map(Object::toString).collect(Collectors.joining(", "));
    return MessageFormat.format(
        "{0} [{1}] {2}", hypothesis == null ? reference : hypothesis, indices, expression);
  }
}
