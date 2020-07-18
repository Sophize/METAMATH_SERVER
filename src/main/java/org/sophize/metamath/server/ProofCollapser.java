package org.sophize.metamath.server;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import org.sophize.datamodel.ResourcePointer;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.sophize.datamodel.ResourcePointer.PointerType.EPHEMERAL;
import static org.sophize.datamodel.ResourceType.ARGUMENT;

class ProofCollapser {
  private final MachineProof original;

  private final BiMap<ResourcePointer, ResourcePointer> conclusionPtrToArgumentPtr;

  ProofCollapser(MachineProof proof) {
    this.original = proof;
    this.conclusionPtrToArgumentPtr =
        proof.arguments.entrySet().stream()
            .collect(
                ImmutableBiMap.toImmutableBiMap(
                    Map.Entry::getKey, entry -> entry.getValue().getArgumentPtr()));
  }

  MachineProof collapse(MetamathProposition proposition) {
    if (this.original.existingProposition != null) return this.original;
    Map<ResourcePointer, Integer> collapsibleArguments =
        getCollapsibleArgumentsInOrder(proposition.getResourcePtr());
    MetamathArgument collapsed = collapseArguments(proposition, collapsibleArguments);
    List<MetamathProposition> uncollapsiblePropositions =
        getPropositionsWithUncollapsibleArguments(proposition.getResourcePtr()).stream()
            .map(original.propositions::get)
            .collect(Collectors.toList());
    List<MachineProof> subProofs =
        uncollapsiblePropositions.stream().map(this::collapse).collect(Collectors.toList());

    List<MetamathProposition> props =
        Stream.concat(
                uncollapsiblePropositions.stream(),
                subProofs.stream().flatMap(proof -> proof.propositions.values().stream()))
            .collect(Collectors.toList());
    Map<ResourcePointer, MetamathArgument> args =
        Stream.concat(
                Stream.of(collapsed),
                subProofs.stream().flatMap(proof -> proof.arguments.values().stream()))
            .collect(
                Collectors.toMap(arg -> arg.getConclusion().getResourcePtr(), Function.identity()));

    return new MachineProof(props, args, this.original.fromMachineArguments);
  }

  private MetamathArgument collapseArguments(
      MetamathProposition proposition, Map<ResourcePointer, Integer> collapsibleArguments) {
    MetamathArgument[] orderedArguments = new MetamathArgument[collapsibleArguments.size()];
    for (var entry : collapsibleArguments.entrySet()) {
      ResourcePointer conclusionPtr = conclusionPtrToArgumentPtr.inverse().get(entry.getKey());
      Preconditions.checkArgument(original.arguments.containsKey(conclusionPtr));
      orderedArguments[entry.getValue()] = original.arguments.get(conclusionPtr);
    }
    List<ArgumentStep> newArgumentSteps = new ArrayList<>();
    for (var argument : orderedArguments) {
      Map<Integer, Integer> stepMapping = new HashMap<>();

      for (int index = 0; index < argument.getSteps().size(); index++) {
        var step = argument.getSteps().get(index);
        if (!Strings.isNullOrEmpty(step.hypothesis)) {
          newArgumentSteps.add(step);
          stepMapping.put(index, newArgumentSteps.size() - 1);
          continue;
        }
        Integer existingIndex = getExistingStepIndex(newArgumentSteps, step.expression);
        if (existingIndex != null) {
          stepMapping.put(index, existingIndex);
          continue;
        }
        // TODO: Below is useful for many cases. Revisit.
        // Preconditions.checkArgument(step.reference.getPointerType() != EPHEMERAL);
        var indices = step.hypIndices.stream().map(stepMapping::get).collect(Collectors.toList());
        Preconditions.checkArgument(indices.stream().allMatch(Objects::nonNull));
        ArgumentStep newStep = step.withHypIndices(indices);
        newArgumentSteps.add(newStep);
        stepMapping.put(index, newArgumentSteps.size() - 1);
      }
    }
    ResourcePointer collapsedArgPtr =
        ResourcePointer.ephemeral(ARGUMENT, proposition.getResourcePtr().getId());
    return new MetamathArgument(collapsedArgPtr, proposition, newArgumentSteps);
  }

  private Integer getExistingStepIndex(List<ArgumentStep> steps, String expression) {
    for (int index = 0; index < steps.size(); index++) {
      if (steps.get(index).expression.equals(expression)) return index;
    }
    return null;
  }

  // Propositions with hypothesis obviously cant be collapsed, they need a separate argument.
  private List<ResourcePointer> getPropositionsWithUncollapsibleArguments(
      ResourcePointer propositionPtr) {
    ArrayList<ResourcePointer> uncollapsible = new ArrayList<>();
    getPropositionsWithUncollapsibleArgumentsInternal(propositionPtr, uncollapsible);
    return uncollapsible;
  }

  private void getPropositionsWithUncollapsibleArgumentsInternal(
      ResourcePointer conclusionPtr, ArrayList<ResourcePointer> uncollapsible) {
    var argument = original.arguments.get(conclusionPtr);
    if (uncollapsible.contains(argument.getArgumentPtr())) return;
    argument.getSteps().stream()
        // References of steps with hypothesis are uncollapsible. They will be handled separately.
        .filter(step -> !step.hypIndices.isEmpty())
        .map(step -> step.reference)
        .filter(Objects::nonNull)
        .filter(ptr -> ptr.getPointerType() == EPHEMERAL)
        .forEach(uncollapsible::add);
  }

  private Map<ResourcePointer, Integer> getCollapsibleArgumentsInOrder(
      ResourcePointer propositionPtr) {
    Map<ResourcePointer, Integer> argumentOrder = new HashMap<>();
    assignDagOrderToCollapsibleArguments(propositionPtr, argumentOrder);
    return argumentOrder;
  }

  private void assignDagOrderToCollapsibleArguments(
      ResourcePointer conclusionPtr, Map<ResourcePointer, Integer> argumentOrder) {
    var argument = original.arguments.get(conclusionPtr);
    Preconditions.checkArgument(
        argument != null, "Argument not found for: " + conclusionPtr.toString());
    if (argumentOrder.containsKey(argument.getArgumentPtr())) return;
    argument.getSteps().stream()
        // References of steps with hypothesis are uncollapsible. They will be handled separately.
        .filter(step -> step.hypIndices.isEmpty())
        .map(step -> step.reference)
        .filter(Objects::nonNull)
        .filter(ptr -> ptr.getPointerType() == EPHEMERAL)
        .forEach(ptr -> assignDagOrderToCollapsibleArguments(ptr, argumentOrder));
    int numNodesProcessed = argumentOrder.size();
    argumentOrder.put(argument.getArgumentPtr(), numNodesProcessed);
  }
}
