package org.sophize.metamath.formachines;

import org.sophize.datamodel.ResourcePointer;
import org.sophize.datamodel.ResourceType;

import static org.sophize.datamodel.ResourcePointer.PointerType.PERMANENT;

public enum MachineId {
  NN0(new ResourcePointer("metamath", ResourceType.MACHINE, PERMANENT, "WpL")),
  NN(new ResourcePointer("metamath", ResourceType.MACHINE, PERMANENT, "WqL")),
  LESS_THAN(new ResourcePointer("metamath", ResourceType.MACHINE, PERMANENT, "DA1"));

  private final ResourcePointer value;

  MachineId(ResourcePointer value) {
    this.value = value;
  }

  public ResourcePointer getValue() {
    return value;
  }

  public static MachineId fromValue(String value) {
    for (var machineId : MachineId.values())
      if (machineId.value.toString().equals(value)) return machineId;
    return null;
  }
}
