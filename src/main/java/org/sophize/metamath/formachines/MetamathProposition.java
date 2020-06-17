package org.sophize.metamath.formachines;

import mmj.lang.Var;
import org.sophize.datamodel.*;
import org.sophize.metamath.formachines.propositions.MetamathStatement;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.sophize.metamath.Utils.varToLookupTerm;

public class MetamathProposition {
  private static final String LOG_HYP_START_TAG = "$e";
  private static final String ASSRT_START_TAG = "$p";

  private final MetamathStatement assrt;
  private final List<MetamathStatement> logHypArray;
  private final List<List<Var>> distinctVars;

  public MetamathProposition(
      MetamathStatement assrt, List<MetamathStatement> logHypArray, List<List<Var>> distinctVars) {
    this.assrt = assrt;
    this.logHypArray = logHypArray;
    this.distinctVars = distinctVars;
  }

  public List<MetamathStatement> getLogHypArray() {
    return this.logHypArray;
  }

  public List<List<Var>> getDistinctVars() {
    return this.distinctVars;
  }

  public MetamathStatement getAssrt() {
    return this.assrt;
  }

  public final ResourcePointer getResourcePtr() {
    return ResourcePointer.ephemeral(ResourceType.PROPOSITION, getAssrt().getLabel());
  }

  final Proposition toProposition() {
    Proposition proposition = new Proposition();
    proposition.setAssignablePtr(getResourcePtr().toString());
    proposition.setLanguage(Language.METAMATH_SET_MM);
    proposition.setMetaLanguage(MetaLanguage.METAMATH);
    proposition.setStatement(getStatement());
    proposition.setEphemeralPtr(getResourcePtr().toString());
    proposition.setLookupTerms(getLookupTerms().toArray(new String[0]));
    return proposition;
  }

  private String getStatement() {
    List<String> stmtStrings = new ArrayList<>();
    for (MetamathStatement hyp : getLogHypArray()) {
      stmtStrings.add(hyp.asString(LOG_HYP_START_TAG));
    }
    stmtStrings.add(getAssrt().asString(ASSRT_START_TAG));
    stmtStrings.add(distinctVarsStatement());
    return stmtStrings.stream().filter(s -> !s.isEmpty()).collect(Collectors.joining("\n"));
  }

  private List<String> getLookupTerms() {
    List<String> lookupTerms = new ArrayList<>();
    for (var hyp : getLogHypArray()) {
      lookupTerms.addAll(hyp.getLookupTerms());
    }
    lookupTerms.addAll(getAssrt().getLookupTerms());

    lookupTerms.addAll(
        getDistinctVars().stream()
            .flatMap(List::stream)
            .map(var -> varToLookupTerm(var.getActiveVarHyp()))
            .collect(Collectors.toList()));
    return lookupTerms;
  }

  private String distinctVarsStatement() {
    return getDistinctVars().stream()
        .map(
            group ->
                group.stream().map(Var::getId).collect(Collectors.joining(" ", "$d ", "" + " $.")))
        .collect(Collectors.joining("\n"));
  }
}
