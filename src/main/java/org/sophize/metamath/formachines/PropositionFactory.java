package org.sophize.metamath.formachines;

import org.sophize.metamath.formachines.propositions.NN0Statement;
import org.sophize.metamath.formachines.propositions.NNStatement;

import java.util.List;

public class PropositionFactory {
  public static MetamathProposition NN0Proposition(NumberRepresentation numeral) {
    return new MetamathProposition(new NN0Statement(numeral), List.of(), List.of());
  }

  public static MetamathProposition NNProposition(NumberRepresentation numeral) {
    return new MetamathProposition(new NNStatement(numeral), List.of(), List.of());
  }

  private PropositionFactory() {}
}
