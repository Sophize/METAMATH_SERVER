package org.sophize.metamath.formachines;

import mmj.lang.Var;
import org.sophize.datamodel.Argument;
import org.sophize.datamodel.Language;
import org.sophize.datamodel.MetaLanguage;
import org.sophize.datamodel.ResourcePointer;
import org.sophize.metamath.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class MetamathArgument {
  private final List<Var> dummyVariables;
  private final List<ArgumentStep> steps;
  private final ResourcePointer argumentPtr;
  private final MetamathProposition conclusion;

  public MetamathArgument(
      ResourcePointer ptr,
      MetamathProposition conclusion,
      List<ArgumentStep> steps,
      List<Var> dummyVariables) {
    this.argumentPtr = ptr;
    this.conclusion = conclusion;
    this.steps = steps;
    this.dummyVariables = dummyVariables;
  }

  public MetamathArgument(
      ResourcePointer ptr, MetamathProposition conclusion, List<ArgumentStep> steps) {
    this(ptr, conclusion, steps, List.of());
  }

  List<Var> getDummyVariables() {
    return dummyVariables;
  }

  List<ArgumentStep> getSteps() {
    return steps;
  }

  ResourcePointer getArgumentPtr() {
    return argumentPtr;
  }

  MetamathProposition getConclusion() {
    return conclusion;
  }

  Argument toArgument() {
    Argument argument = new Argument();
    argument.setMetaLanguage(MetaLanguage.METAMATH);
    argument.setLanguage(Language.METAMATH_SET_MM);
    argument.setPremises(getPremises().toArray(new String[0]));
    argument.setConclusion(conclusion.getResourcePtr().toString());
    argument.setLookupTerms(getLookupTerms().toArray(new String[0]));
    argument.setEphemeralPtr(argumentPtr.toString());

    String argumentText = String.join("\n", getStatementLines());
    argument.setArgumentText(argumentText);

    return argument;
  }

  MetamathArgument replace(ResourcePointer original, ResourcePointer updated) {
    var updatedSteps =
        steps.stream().map(step -> step.replace(original, updated)).collect(Collectors.toList());
    return new MetamathArgument(argumentPtr, conclusion, updatedSteps, dummyVariables);
  }

  private List<String> getLookupTerms() {
    return Stream.concat(
            dummyVariables.stream().map(var -> Utils.varToLookupTerm(var.getActiveVarHyp())),
            steps.stream().flatMap(step -> step.lookupTerms.stream()))
        .collect(toList());
  }

  private List<String> getStatementLines() {
    List<String> statementLines = new ArrayList<>(Utils.getArgumentHeaders(dummyVariables));
    for (int i = 0; i < steps.size(); i++) {
      ArgumentStep step = steps.get(i);
      String[] stepParts = {
        // TODO: handle the case when one of the steps has a hypothesis as reference.
        Integer.toString(i + 1), step.getHypString(), step.referenceForArgument(), step.expression
      };
      statementLines.add("| " + String.join(" | ", stepParts) + " |");
    }
    return statementLines;
  }

  private List<String> getPremises() {
    return steps.stream()
        .map(step -> step.reference)
        .filter(Objects::nonNull)
        .map(ResourcePointer::toString)
        .collect(toList());
  }
}
