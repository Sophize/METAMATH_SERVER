package org.sophize.metamath.formachines.propositions;

import com.google.common.base.Preconditions;
import org.sophize.metamath.formachines.NumberRepresentation;

import java.text.MessageFormat;

// Example Statement:
// |- 0 e. NN0
// |- 10 e. NN0
// |- ; 2 3 e. NN0
public class NN0Statement extends MetamathStatement {
  private static final String LABEL_TEMPLATE = "NN0.{0}";
  private static final String EXPRESSION_TEMPLATE = "|- {0} e. NN0";

  private final NumberRepresentation numeral;

  public NN0Statement(NumberRepresentation numeral) {
    Preconditions.checkArgument(numeral != null);
    this.numeral = numeral;
  }

  public NumberRepresentation getNumeral() {
    return this.numeral;
  }

  @Override
  public String getLabel() {
    return MessageFormat.format(LABEL_TEMPLATE, numeral.nameForId());
  }

  @Override
  public String getExpression() {
    return MessageFormat.format(EXPRESSION_TEMPLATE, numeral.toString());
  }
}
