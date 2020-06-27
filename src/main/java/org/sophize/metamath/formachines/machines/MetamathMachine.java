package org.sophize.metamath.formachines.machines;

import com.google.common.base.Preconditions;
import mmj.lang.Stmt;
import mmj.lang.VarHyp;
import org.sophize.datamodel.Language;
import org.sophize.datamodel.Machine;
import org.sophize.datamodel.Proposition;
import org.sophize.datamodel.ResourcePointer;
import org.sophize.metamath.formachines.MachineProof;
import org.sophize.metamath.formachines.MachineUtils;
import org.sophize.metamath.formachines.MetamathProposition;

import java.util.List;

import static java.lang.Character.isLowerCase;
import static java.lang.Character.isUpperCase;
import static org.sophize.datamodel.ResourcePointer.PointerType.ASSIGNABLE;
import static org.sophize.datamodel.ResourceType.MACHINE;

public abstract class MetamathMachine {

  private static final String DEFAULT_MATERIALIZE_DATASET = "metamath_save";

  private static final String METAMATH_SERVER_NAME = "METAMATH_SERVER";
  static final String DOESNT_HANDLE_REASON = "This machine doesn't handle such statements";

  public abstract String getDescription();

  public abstract String getDefaultStrictStatement();

  public abstract String getDefaultLenientStatement();

  public abstract List<Stmt> getPremisePropositions();

  public abstract List<ResourcePointer> getPremiseMachines();

  public abstract MetamathProposition parseLenient(Proposition proposition);

  public abstract String getNotProvableReason(MetamathProposition proposition);

  public abstract MachineProof getProof(MetamathProposition proposition);

  public Language getDefaultLanguage() {
    return Language.METAMATH_SET_MM;
  }

  public boolean isIndexable() {
    return false;
  }

  boolean isProvable(MetamathProposition proposition) {
    return getNotProvableReason(proposition) == null;
  }

  public MetamathProposition parseStrict(Proposition proposition) {
    if (proposition.getLanguage() != Language.METAMATH_SET_MM) return null;
    String statement = MachineUtils.stripPropositionMarker(proposition.getStatement());
    var parsedStmt = MachineUtils.parseStatement(statement, MachineUtils.SET_DB, new VarHyp[0]);
    if (parsedStmt == null) return null;
    return new MetamathProposition(parsedStmt);
  }

  // Override this function when the assignablePtr of a machine needs to be changed.
  public ResourcePointer getPermanentPtr() {
    return null;
  }

  public ResourcePointer getAssignablePtr() {
    String id = getClass().getSimpleName();
    if (id.endsWith("Machine")) id = id.substring(0, id.length() - "Machine".length());
    return new ResourcePointer("metamath", MACHINE, ASSIGNABLE, toSnakeCase(id));
  }

  public String[] getNames() {
    return new String[] {toSentenceCase(getClass().getSimpleName())};
  }

  public final Machine toDatamodel() {
    Machine machine = new Machine();
    if (getPermanentPtr() != null) machine.setPermanentPtr(getAssignablePtr().toString());
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
    machine.setNames(getNames());
    return machine;
  }

  final Stmt safeUse(Stmt stmt) {
    Preconditions.checkArgument(getPremisePropositions().contains(stmt));
    return stmt;
  }

  private static String toSentenceCase(String s) {
    var formatted = new StringBuilder();
    for (int i = 0; i < s.length(); i++) {
      char current = s.charAt(i);
      if (i == s.length() - 1) {
        formatted.append(current);
        continue;
      }
      char next = s.charAt(i + 1);
      if (isUpperCase(current) && isLowerCase(next)) formatted.append(" ");
      formatted.append(current);
    }
    return formatted.toString();
  }

  private static String toSnakeCase(String s) {
    return toSentenceCase(s).toLowerCase().replaceAll(" ", "_");
  }
}
