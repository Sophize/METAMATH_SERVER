package org.sophize.metamath.formachines;

import org.sophize.datamodel.ResourcePointer;
import org.sophize.datamodel.ResourceType;
import org.sophize.metamath.formachines.machines.MetamathMachine;
import org.sophize.metamath.formachines.machines.NN0Machine;

import static org.sophize.datamodel.ResourcePointer.PointerType.PERMANENT;

public enum MachineId {
  NN0(getPermanentPtr("WpL"), NN0Machine.getInstance());

  private final ResourcePointer permanentPtr;
  private final MetamathMachine machine;

  MachineId(ResourcePointer permanentPtr, MetamathMachine machine) {
    this.permanentPtr = permanentPtr;
    this.machine = machine;
  }

  public ResourcePointer getPermanentPtr() {
    return permanentPtr;
  }

  public MetamathMachine getMachine() {
    return machine;
  }

  public static MachineId fromValue(String value) {
    for (var machineId : MachineId.values())
      if (machineId.permanentPtr.toString().equals(value)) return machineId;
    return null;
  }

  private static ResourcePointer getPermanentPtr(String id) {
    return new ResourcePointer("metamath", ResourceType.MACHINE, PERMANENT, id);
  }
}
