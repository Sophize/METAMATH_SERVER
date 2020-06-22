package org.sophize.metamath.formachines;

import mmj.lang.ParseNode;
import mmj.lang.Var;
import org.sophize.datamodel.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.sophize.metamath.Utils.getLookupTermsForParseNode;
import static org.sophize.metamath.Utils.varToLookupTerm;

public class MetamathProposition {
  private static final String LOG_HYP_START_TAG = "$e";
  private static final String ASSRT_START_TAG = "$p";

  private final ParseNode assrt;
  private final List<ParseNode> logHypArray;
  private final List<List<Var>> distinctVars;

  public MetamathProposition(
      ParseNode assrt, List<ParseNode> logHypArray, List<List<Var>> distinctVars) {
    this.assrt = assrt;
    this.logHypArray = logHypArray;
    this.distinctVars = distinctVars;
  }

  public List<ParseNode> getLogHypArray() {
    return this.logHypArray;
  }

  public List<List<Var>> getDistinctVars() {
    return this.distinctVars;
  }

  public ParseNode getAssrt() {
    return this.assrt;
  }

  public final ResourcePointer getResourcePtr() {
    return ResourcePointer.ephemeral(ResourceType.PROPOSITION, ParseNodeHelpers.getLabel(getAssrt()));
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
    for (ParseNode hyp : getLogHypArray()) {
      stmtStrings.add(ParseNodeHelpers.asString(hyp, LOG_HYP_START_TAG));
    }
    stmtStrings.add(ParseNodeHelpers.asString(getAssrt(), ASSRT_START_TAG));
    stmtStrings.add(distinctVarsStatement());
    return stmtStrings.stream().filter(s -> !s.isEmpty()).collect(Collectors.joining("\n"));
  }

  private List<String> getLookupTerms() {
    List<String> lookupTerms = new ArrayList<>();
    for (var hyp : getLogHypArray()) {
      lookupTerms.addAll(getLookupTermsForParseNode(hyp));
    }
    lookupTerms.addAll(getLookupTermsForParseNode(getAssrt()));

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
