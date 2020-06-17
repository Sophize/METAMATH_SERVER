package org.sophize.metamath.formachines.propositions;

import com.google.common.base.Preconditions;
import org.sophize.metamath.formachines.NumberRepresentation;

import java.text.MessageFormat;

// Example Statements:
// |- 10 e. NN
// |- ; 2 3 e. NN
public class NNStatement extends MetamathStatement {
  private static final String LABEL_TEMPLATE = "NN.{0}";
  private static final String EXPRESSION_TEMPLATE = "|- {0} e. NN";

  private final NumberRepresentation numeral;

  public NNStatement(NumberRepresentation numeral) {
    Preconditions.checkArgument(numeral != null);
    this.numeral = numeral;
  }

  public NumberRepresentation getNumeral() {
    return numeral;
  }

  public String getLabel() {
    return MessageFormat.format(LABEL_TEMPLATE, numeral.nameForId());
  }

  public String getExpression() {
    return getExpression(numeral);
  }

  private static String getExpression(NumberRepresentation numeral) {
    return MessageFormat.format(EXPRESSION_TEMPLATE, numeral.toString());
  }
}
