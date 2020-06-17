package org.sophize.metamath.formachines.propositions;

import org.sophize.metamath.formachines.MachineUtils;

import java.util.List;

public abstract class MetamathStatement {
  public abstract String getExpression();

  public abstract String getLabel();

  public String asString(String startTag) {
    return String.join(" ", getLabel(), startTag, getExpression(), "$.");
  }

  public List<String> getLookupTerms() {
    return MachineUtils.getLookupTerms(getExpression(), MachineUtils.SET_DB);
  }
}
