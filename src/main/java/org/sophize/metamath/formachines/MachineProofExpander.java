package org.sophize.metamath.formachines;

import com.google.common.base.Preconditions;
import org.sophize.datamodel.ResourcePointer;
import org.sophize.metamath.formachines.machines.MetamathMachine;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.stream.Collectors;

class MachineProofExpander {

  private static final int MAX_EXPANSIONS = 100;

  static MachineProof expand(MachineProof machineProof, MetamathProposition resolvedProposition) {
    if (machineProof.existingProposition != null) return machineProof;

    Queue<Map.Entry<ResourcePointer, MetamathMachine>> pending =
        new ArrayDeque<>(machineProof.fromMachineArguments.entrySet());
    Map<ResourcePointer, MetamathArgument> expanded = new HashMap<>(machineProof.arguments);
    Map<ResourcePointer, MetamathProposition> generated = new HashMap<>(machineProof.propositions);

    Map.Entry<ResourcePointer, MetamathMachine> head;
    int numExpansions = 0;
    while ((head = pending.poll()) != null) {
      var conclusionPtr = head.getKey();
      if (expanded.containsKey(conclusionPtr)) continue;
      if (++numExpansions > MAX_EXPANSIONS) break;

      var machineForProof = head.getValue();
      MachineProof subProof = machineForProof.getProof(generated.get(conclusionPtr));

      for (var propPair : subProof.propositions.entrySet()) {
        generated.putIfAbsent(propPair.getKey(), propPair.getValue());
      }
      for (var argPair : subProof.arguments.entrySet()) {
        expanded.putIfAbsent(argPair.getKey(), argPair.getValue());
      }
      for (var machineArgPair : subProof.fromMachineArguments.entrySet()) {
        if (!expanded.containsKey(machineArgPair.getKey()) && !pending.contains(machineArgPair)) {
          pending.add(machineArgPair);
        }
      }
      if (subProof.existingProposition != null) {
        generated.remove(conclusionPtr);
        expanded.remove(conclusionPtr);
        for (var expandedEntry : expanded.entrySet()) {
          var metamathArg = expandedEntry.getValue();
          var updated = metamathArg.replace(conclusionPtr, subProof.getExistingPropositionPtr());
          expanded.put(expandedEntry.getKey(), updated);
        }
      }
    }

    var remainingMachineArguments =
        pending.stream().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    return new MachineProof(generated, expanded, remainingMachineArguments);
  }

  private MachineProofExpander() {}
}
