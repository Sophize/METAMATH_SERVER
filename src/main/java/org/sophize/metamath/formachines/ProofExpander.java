package org.sophize.metamath.formachines;

import com.google.common.base.Preconditions;
import org.sophize.datamodel.ResourcePointer;
import org.sophize.metamath.formachines.machines.MetamathMachine;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.stream.Collectors;

class ProofExpander {

  private static final int MAX_EXPANSIONS = 10000;

  static MachineProof expand(MachineProof machineProof, MetamathProposition primaryProposition) {
    if (machineProof.existingProposition != null) return machineProof;

    var queue = new ExpansionQueue(machineProof);

    Map.Entry<ResourcePointer, MetamathMachine> head = queue.next();
    int numExpansions = 0;
    for (; head != null && numExpansions < MAX_EXPANSIONS; head = queue.next(), ++numExpansions) {
      var toExpandPropPtr = head.getKey();
      var expandingMachine = head.getValue();
      var toExpandProp = queue.getProposition(toExpandPropPtr, primaryProposition);
      MachineProof subProof = expandingMachine.getProof(toExpandProp);
      Preconditions.checkArgument(
          subProof != null, "Expander: Didn't get sub-proof: " + toExpandProp.getResourcePtr());
      if ((toExpandProp == primaryProposition) && subProof.existingProposition != null) {
        return new MachineProof(subProof.existingProposition);
      }
      queue.updateWithSubProof(subProof, toExpandPropPtr);
    }

    var remainingMachineArguments =
        queue.pending.stream()
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (v1, v2) -> v1));
    return new MachineProof(queue.generated, queue.expanded, remainingMachineArguments);
  }

  private static class ExpansionQueue {
    Queue<Map.Entry<ResourcePointer, MetamathMachine>> pending;
    Map<ResourcePointer, MetamathArgument> expanded;
    Map<ResourcePointer, MetamathProposition> generated;

    ExpansionQueue(MachineProof proof) {
      this.pending = new ArrayDeque<>(proof.fromMachineArguments.entrySet());
      this.expanded = new HashMap<>(proof.arguments);
      this.generated = new HashMap<>(proof.propositions);
    }

    private Map.Entry<ResourcePointer, MetamathMachine> next() {
      var head = this.pending.poll();
      while (head != null && expanded.containsKey(head.getKey())) {
        head = this.pending.poll();
      }
      return head;
    }

    private void updateWithSubProof(MachineProof subProof, ResourcePointer toExpandPropPtr) {
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
        generated.remove(toExpandPropPtr);
        expanded.remove(toExpandPropPtr);
        for (var expandedEntry : expanded.entrySet()) {
          var metamathArg = expandedEntry.getValue();
          var updated = metamathArg.replace(toExpandPropPtr, subProof.getExistingPropositionPtr());
          expanded.put(expandedEntry.getKey(), updated);
        }
      }
    }

    private MetamathProposition getProposition(
        ResourcePointer propPtr, MetamathProposition primaryProposition) {
      var generatedProp = generated.get(propPtr);
      if (generatedProp != null) return generatedProp;
      Preconditions.checkArgument(primaryProposition.getResourcePtr().equals(propPtr));
      return primaryProposition;
    }
  }

  private ProofExpander() {}
}
