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

public class StepCreationHints {

  private final Map<Integer, ArgumentStep> exactData;

  private final Function<ParseNode, MetamathMachine> machineDeterminer;

  public StepCreationHints(
      Map<Integer, ArgumentStep> exactData,
      Function<ParseNode, MetamathMachine> machineDeterminer) {
    this.exactData = exactData;
    this.machineDeterminer = machineDeterminer;
  }

  public static StepCreationHints forAllGeneratedPremises(
      Assrt assrt,
      Map<String, String> substitutions,
      Function<ParseNode, MetamathMachine> machineDeterminer) {
    return new StepCreationHints(
        Map.of(
            assrt.getLogHypArrayLength(), ArgumentStep.finalStepForArgument(assrt, substitutions)),
        machineDeterminer);
  }

  public static StepCreationHints forAllGeneratedPremises(
      MetamathProposition proposition,
      Map<String, String> substitutions,
      Function<ParseNode, MetamathMachine> machineDeterminer) {
    return new StepCreationHints(
        Map.of(
            proposition.getLogHypArray().size(),
            ArgumentStep.finalStepForArgument(proposition, substitutions)),
        machineDeterminer);
  }

  public List<ArgumentStep> getSteps(List<ParseNode> argumentStepNodes) {
    return IntStream.range(0, argumentStepNodes.size())
        .mapToObj(stepIndex -> getArgumentStep(stepIndex, argumentStepNodes.get(stepIndex)))
        .collect(Collectors.toList());
  }

  public ArgumentStep getArgumentStep(int stepIndex, ParseNode node) {
    var step = exactData.get(stepIndex);
    if (step != null) return step;
    var metamathMachine = machineDeterminer.apply(node);
    Preconditions.checkArgument(
        metamathMachine != null, "couldn't find machine for: " + node.toString());
    return ArgumentStep.fromEphemeralReference(
        new MetamathProposition(node), metamathMachine);
  }
}
