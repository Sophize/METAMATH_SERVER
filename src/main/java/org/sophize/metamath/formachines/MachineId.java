package org.sophize.metamath.formachines;

import org.sophize.metamath.formachines.machines.LessThanMachine;
import org.sophize.metamath.formachines.machines.MetamathMachine;
import org.sophize.metamath.formachines.machines.NN0ClosureMachine;
import org.sophize.metamath.formachines.machines.NNClosureMachine;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum MachineId {
  NN0(NN0ClosureMachine.getInstance()),
  NN(NNClosureMachine.getInstance()),
  LESS_THAN(LessThanMachine.getInstance());

  private final MetamathMachine machine;
  private static final Map<String, MachineId> machineMap;

  MachineId(MetamathMachine machine) {
    this.machine = machine;
  }

  public MetamathMachine getMachine() {
    return machine;
  }

  public static MachineId fromValue(String value) {
    return machineMap.get(value);
  }

  static {
    machineMap =
        Arrays.stream(MachineId.values())
            .collect(
                Collectors.toUnmodifiableMap(
                    machineId -> machineId.machine.getAssignablePtr().toString(),
                    Function.identity()));
  }
}
