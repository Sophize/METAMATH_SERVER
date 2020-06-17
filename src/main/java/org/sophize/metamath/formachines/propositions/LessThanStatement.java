package org.sophize.metamath.formachines.propositions;

import org.sophize.metamath.formachines.NumberRepresentation;

import java.text.MessageFormat;

// Example Statements:
// |- 1 < 2
// |- 10 < ; 1 9
// |- ; 2 3 < ; 27
public class LessThanStatement extends MetamathStatement {
  private static final String LABEL_TEMPLATE = "{0}.lt.{1}";
  private static final String EXPRESSION_TEMPLATE = "|- {0} < {1}";
  private final NumberRepresentation operand1;
  private final NumberRepresentation operand2;

  public LessThanStatement(NumberRepresentation operand1, NumberRepresentation operand2) {
    this.operand1 = operand1;
    this.operand2 = operand2;
  }

  public NumberRepresentation getOperand1() {
    return operand1;
  }

  public NumberRepresentation getOperand2() {
    return operand2;
  }

  public String getLabel() {
    return MessageFormat.format(LABEL_TEMPLATE, operand1.nameForId(), operand2.nameForId());
  }

  public String getExpression() {
    return getExpression(operand1, operand2);
  }

  private static String getExpression(
      NumberRepresentation operand1, NumberRepresentation operand2) {
    return MessageFormat.format(EXPRESSION_TEMPLATE, operand1.toString(), operand2.toString());
  }
}
