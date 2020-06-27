package org.sophize.metamath.formachines;

import org.sophize.metamath.formachines.machines.*;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum MachineId {
  NN0(NN0ClosureMachine.getInstance()),
  NN(NNClosureMachine.getInstance()),
  CC(CCClosureMachine.getInstance()),
  NN_LESS_THAN(NNLessThanMachine.getInstance()),
  NN_SUM(NNSumMachine.getInstance()),
  NN_SUM_PRODUCT_EXPRESSION(NNSumProductExpressionMachine.getInstance());

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
