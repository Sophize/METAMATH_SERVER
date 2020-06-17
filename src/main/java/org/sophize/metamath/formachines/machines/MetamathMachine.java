package org.sophize.metamath.formachines.machines;

import com.google.common.base.Preconditions;
import mmj.lang.Stmt;
import org.sophize.datamodel.*;
import org.sophize.metamath.formachines.MachineProof;
import org.sophize.metamath.formachines.MachineUtils;
import org.sophize.metamath.formachines.MetamathProposition;

import java.util.List;

public abstract class MetamathMachine {

  private static final String DEFAULT_MATERIALIZE_DATASET = "metamath_save";

  private static final String METAMATH_SERVER_NAME = "METAMATH_SERVER";

  public abstract String getDescription();

  public abstract boolean isIndexable();

  public abstract ResourcePointer getPermanentPtr();

  public abstract Language getDefaultLanguage();

  public abstract String getDefaultStrictStatement();

  public abstract String getDefaultLenientStatement();

  public abstract List<Stmt> getPremisePropositions();

  public abstract List<ResourcePointer> getPremiseMachines();

  public abstract MetamathProposition parseStrict(Proposition proposition);

  public abstract MetamathProposition parseLenient(Proposition proposition);

  public abstract String getNotProvableReason(MetamathProposition proposition);

  public abstract MachineProof getProof(MetamathProposition proposition);

  public ResourcePointer getAssignablePtr() {
    return ResourcePointer.assignable(ResourceType.MACHINE, getClass().getSimpleName());
  }

  public Machine toDatamodel() {
    Machine machine = new Machine();
    // machine.setPermanentPtr();  // DONT SET PERMANENT PTR.
    machine.setAssignablePtr(getAssignablePtr().toString());
    machine.setDefaultMaterializeDataset(DEFAULT_MATERIALIZE_DATASET);
    machine.setDescription(getDescription());
    machine.setDefaultLanguage(getDefaultLanguage());
    machine.setDefaultStrictStatement(getDefaultStrictStatement());
    machine.setDefaultLenientStatement(getDefaultLenientStatement());
    machine.setPremiseMachines(MachineUtils.toStringArray(getPremiseMachines()));
    machine.setPremisePropositions(MachineUtils.toStringRefArray(getPremisePropositions()));
    machine.setServerName(METAMATH_SERVER_NAME);
    machine.setIndexable(isIndexable());
    machine.setNames(new String[] {this.getClass().getSimpleName()});
    return machine;
  }

  Stmt safeUse(Stmt stmt) {
    Preconditions.checkArgument(getPremisePropositions().contains(stmt));
    return stmt;
  }
}
