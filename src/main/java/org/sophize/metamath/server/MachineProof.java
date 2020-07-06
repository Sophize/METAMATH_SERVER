package org.sophize.metamath.server;

import mmj.lang.Stmt;
import org.sophize.datamodel.Argument;
import org.sophize.datamodel.Proposition;
import org.sophize.datamodel.ResourcePointer;
import org.sophize.datamodel.ResourceType;
import org.sophize.metamath.server.machines.MetamathMachine;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.sophize.datamodel.ResourceType.ARGUMENT;

public class MachineProof {
  // Resource Pointer to proposition.
  final Map<ResourcePointer, MetamathProposition> propositions;
  // Conclusion Pointer to argument.
  final Map<ResourcePointer, MetamathArgument> arguments;
  // Conclusion Pointer to machine that can be used to get the rest of the proof.
  final Map<ResourcePointer, MetamathMachine> fromMachineArguments;
  final Stmt existingProposition;

  public MachineProof(
      Map<ResourcePointer, MetamathProposition> propositions,
      Map<ResourcePointer, MetamathArgument> arguments,
      Map<ResourcePointer, MetamathMachine> fromMachineArguments) {
    this.propositions = Collections.unmodifiableMap(propositions);
    this.arguments = Collections.unmodifiableMap(arguments);
    this.fromMachineArguments = Collections.unmodifiableMap(fromMachineArguments);
    this.existingProposition = null;
  }

  public MachineProof(
      List<MetamathProposition> propositions,
      Map<ResourcePointer, MetamathArgument> arguments,
      Map<ResourcePointer, MetamathMachine> fromMachineArguments) {
    this.propositions =
        propositions.stream()
            .collect(
                Collectors.toUnmodifiableMap(
                    MetamathProposition::getResourcePtr, Function.identity(), (v1, v2) -> v1));
    this.arguments = Collections.unmodifiableMap(arguments);
    this.fromMachineArguments = Collections.unmodifiableMap(fromMachineArguments);
    this.existingProposition = null;
  }

  public MachineProof(Stmt stmt) {
    this.propositions = Collections.emptyMap();
    this.arguments = Collections.emptyMap();
    this.fromMachineArguments = Collections.emptyMap();
    this.existingProposition = stmt;
  }

  Proposition[] getPropositions() {
    return propositions.values().stream()
        .map(MetamathProposition::toProposition)
        .toArray(Proposition[]::new);
  }

  Argument[] getArguments() {
    return Stream.concat(
            arguments.values().stream().map(MetamathArgument::toArgument),
            fromMachineArguments.entrySet().stream()
                .map(entry -> toPremiseMachineArgument(entry.getKey(), entry.getValue())))
        .toArray(Argument[]::new);
  }

  ResourcePointer getExistingPropositionPtr() {
    return existingProposition == null
        ? null
        : ResourcePointer.assignable(ResourceType.PROPOSITION, existingProposition.getLabel());
  }

  private static Argument toPremiseMachineArgument(ResourcePointer ptr, MetamathMachine machine) {
    Argument argument = new Argument();
    argument.setPremiseMachine(machine.getAssignablePtr().toString());
    argument.setConclusion(ptr.toString());
    argument.setEphemeralPtr(ResourcePointer.ephemeral(ARGUMENT, ptr.getId()).toString());
    return argument;
  }
}
