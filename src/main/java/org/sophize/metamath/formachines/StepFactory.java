package org.sophize.metamath.formachines;

import com.google.common.base.Preconditions;
import mmj.lang.Assrt;
import mmj.lang.ParseNode;
import org.sophize.metamath.formachines.machines.MetamathMachine;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;

public class StepFactory {

  private final Map<Integer, ArgumentStep> exactData;

  private final Function<ParseNode, MetamathMachine> machineDeterminer;

  public StepFactory(
      Map<Integer, ArgumentStep> exactData,
      Function<ParseNode, MetamathMachine> machineDeterminer) {
    this.exactData = exactData;
    this.machineDeterminer = machineDeterminer;
  }

  // For an argument which is application of just one proposition from the set_mm database.
  //
  // Eg. when the assert is EQBRTRI, this factory will return 3 steps.
  // First and second step would be the premises of EQBRTRI. These will have 'machineForProof'
  // set from the value returned by 'machineDeterminer'. The third (and final) step will be the
  // application of EQBRTRI on the first two steps.
  public static StepFactory forArgumentWithGeneratedPremises(
      Assrt assrt,
      Map<String, String> substitutions,
      Function<ParseNode, MetamathMachine> machineDeterminer) {
    var hypIndices = IntStream.range(0, assrt.getLogHypArrayLength()).boxed().collect(toList());
    var totalSteps = assrt.getLogHypArrayLength() + 1;
    var finalStep = ArgumentStep.fromSetMM(assrt, hypIndices, substitutions);
    return new StepFactory(Map.of(totalSteps - 1, finalStep), machineDeterminer);
  }

  // Similar to the above function, this returns steps for an argument that is application of just
  // one proposition. But, instead of a proposition from set.mm, a generated proposition is used.
  public static StepFactory forArgumentWithGeneratedPremises(
      MetamathProposition p,
      String dbForParsing,
      Map<String, String> substitutions,
      Function<ParseNode, MetamathMachine> machineDeterminer) {
    var hypIndices = IntStream.range(0, p.getLogHypArray().size()).boxed().collect(toList());
    var finalStep =
        ArgumentStep.fromEphemeralReference(p, null, dbForParsing, hypIndices, substitutions);
    var totalSteps = p.getLogHypArray().size() + 1;
    return new StepFactory(Map.of(totalSteps - 1, finalStep), machineDeterminer);
  }

  List<ArgumentStep> getSteps(List<ParseNode> argumentStepNodes) {
    return IntStream.range(0, argumentStepNodes.size())
        .mapToObj(stepIndex -> getArgumentStep(stepIndex, argumentStepNodes.get(stepIndex)))
        .collect(Collectors.toList());
  }

  private ArgumentStep getArgumentStep(int stepIndex, ParseNode node) {
    var step = exactData.get(stepIndex);
    if (step != null) return step;
    var metamathMachine = machineDeterminer.apply(node);
    Preconditions.checkArgument(
        metamathMachine != null, "couldn't find machine for: " + node.toString());
    return ArgumentStep.fromSetMMEphemeralReference(new MetamathProposition(node), metamathMachine);
  }
}
