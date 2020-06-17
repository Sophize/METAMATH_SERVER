package org.sophize.metamath.formachines;

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

public class ProofCompressor {
  private final MachineProof original;

  private final BiMap<ResourcePointer, ResourcePointer> conclusionPtrToArgumentPtr;

  public ProofCompressor(MachineProof proof) {
    this.original = proof;
    this.conclusionPtrToArgumentPtr =
        proof.arguments.entrySet().stream()
            .collect(
                ImmutableBiMap.toImmutableBiMap(
                    Map.Entry::getKey, entry -> entry.getValue().getArgumentPtr()));
  }

  public MachineProof compress(MetamathProposition proposition) {
    Map<ResourcePointer, Integer> compressableArguments =
        getCompressableArgumentsInOrder(proposition.getResourcePtr());
    MetamathArgument compressed = compressArguments(proposition, compressableArguments);
    List<MetamathProposition> uncompressablePropositions =
        getPropositonsWithUncompressableArguments(proposition.getResourcePtr()).stream()
            .map(original.propositions::get)
            .collect(Collectors.toList());
    List<MachineProof> subProofs =
        uncompressablePropositions.stream().map(this::compress).collect(Collectors.toList());

    List<MetamathProposition> props =
        Stream.concat(
                uncompressablePropositions.stream(),
                subProofs.stream().flatMap(proof -> proof.propositions.values().stream()))
            .collect(Collectors.toList());
    Map<ResourcePointer, MetamathArgument> args =
        Stream.concat(
                Stream.of(compressed),
                subProofs.stream().flatMap(proof -> proof.arguments.values().stream()))
            .collect(
                Collectors.toMap(arg -> arg.getConclusion().getResourcePtr(), Function.identity()));

    return new MachineProof(props, args, this.original.fromMachineArguments);
  }

  private MetamathArgument compressArguments(
      MetamathProposition proposition, Map<ResourcePointer, Integer> compresableArguments) {
    MetamathArgument[] orderedArguments = new MetamathArgument[compresableArguments.size()];
    for (var entry : compresableArguments.entrySet()) {
      ResourcePointer conclusionPtr = conclusionPtrToArgumentPtr.inverse().get(entry.getKey());
      Preconditions.checkArgument(original.arguments.containsKey(conclusionPtr));
      orderedArguments[entry.getValue()] = original.arguments.get(conclusionPtr);
    }
    List<ArgumentStep> newArgumentSteps = new ArrayList<>();
    for (var argument : orderedArguments) {
      Map<Integer, Integer> stepMapping = new HashMap<>();

      for (int index = 0; index < argument.getSteps().size(); index++) {
        var step = argument.getSteps().get(index);
        Preconditions.checkArgument(Strings.isNullOrEmpty(step.hypothesis), "Not implemented");
        Integer existingIndex = getExistingStepIndex(newArgumentSteps, step.expression);
        if (existingIndex != null) {
          stepMapping.put(index, existingIndex);
          continue;
        }
        Preconditions.checkArgument(step.reference.getPointerType() != EPHEMERAL);
        var indices = step.hypIndices.stream().map(stepMapping::get).collect(Collectors.toList());
        Preconditions.checkArgument(indices.stream().allMatch(Objects::nonNull));
        ArgumentStep newStep = step.withHypIndices(indices);
        newArgumentSteps.add(newStep);
        stepMapping.put(index, newArgumentSteps.size() - 1);
      }
    }
    ResourcePointer compressedArgPtr =
        ResourcePointer.ephemeral(ARGUMENT, proposition.getAssrt().getLabel());
    return new MetamathArgument(compressedArgPtr, proposition, newArgumentSteps);
  }

  private Integer getExistingStepIndex(List<ArgumentStep> steps, String expression) {
    for (int index = 0; index < steps.size(); index++) {
      if (steps.get(index).expression.equals(expression)) return index;
    }
    return null;
  }

  // Propositions with hypothesis obviously cant be compressed, they need a separate argument.
  private List<ResourcePointer> getPropositonsWithUncompressableArguments(
      ResourcePointer propositionPtr) {
    ArrayList<ResourcePointer> uncompressable = new ArrayList<>();
    getPropositionsWithUncompressableArgumentsInternal(propositionPtr, uncompressable);
    return uncompressable;
  }

  private void getPropositionsWithUncompressableArgumentsInternal(
      ResourcePointer conclusionPtr, ArrayList<ResourcePointer> uncompressable) {
    var argument = original.arguments.get(conclusionPtr);
    if (uncompressable.contains(argument.getArgumentPtr())) return;
    argument.getSteps().stream()
        // References of steps with hypothesis are uncompressable. They will be handled separately.
        .filter(step -> !step.hypIndices.isEmpty())
        .map(step -> step.reference)
        .filter(Objects::nonNull)
        .filter(ptr -> ptr.getPointerType() == EPHEMERAL)
        .forEach(uncompressable::add);
  }

  private Map<ResourcePointer, Integer> getCompressableArgumentsInOrder(
      ResourcePointer propositionPtr) {
    Map<ResourcePointer, Integer> argumentOrder = new HashMap<>();
    assignDagOrderToCompressableArguments(propositionPtr, argumentOrder);
    return argumentOrder;
  }

  private void assignDagOrderToCompressableArguments(
      ResourcePointer conclusionPtr, Map<ResourcePointer, Integer> argumentOrder) {
    var argument = original.arguments.get(conclusionPtr);
    if (argumentOrder.containsKey(argument.getArgumentPtr())) return;
    argument.getSteps().stream()
        // References of steps with hypothesis are uncompressable. They will be handled separately.
        .filter(step -> step.hypIndices.isEmpty())
        .map(step -> step.reference)
        .filter(Objects::nonNull)
        .filter(ptr -> ptr.getPointerType() == EPHEMERAL)
        .forEach(ptr -> assignDagOrderToCompressableArguments(ptr, argumentOrder));
    int numNodesProcessed = argumentOrder.size();
    argumentOrder.put(argument.getArgumentPtr(), numNodesProcessed);
  }
}
