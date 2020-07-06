package org.sophize.metamath.server.machines;

import com.google.common.base.Preconditions;
import com.google.common.collect.Streams;
import mmj.lang.Assrt;
import mmj.lang.ParseNode;
import mmj.lang.Stmt;
import org.sophize.datamodel.Proposition;
import org.sophize.metamath.server.*;

import javax.annotation.Nonnull;
import java.text.MessageFormat;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import static org.sophize.metamath.server.MachineUtils.getProofForSetMMAssrt;
import static org.sophize.metamath.server.NumberRepresentation.DECIMAL_10;
import static org.sophize.metamath.server.NumberRepresentation.PRIMITIVE_10;
import static org.sophize.metamath.server.ParseNodeHelpers.*;

public class NNSumProductEquationMachine extends MetamathMachine {
  private static NNSumProductEquationMachine instance = new NNSumProductEquationMachine();

  private NNSumProductEquationMachine() {}

  public static NNSumProductEquationMachine getInstance() {
    return instance;
  }

  private final Assrt EQID = (Assrt) Databases.getSetMMStmt("eqid");
  private final Assrt DECMUL1C = (Assrt) Databases.getSetMMStmt("decmul1c");
  private final Assrt DECMUL2C = (Assrt) Databases.getSetMMStmt("decmul2c");
  private final Assrt DEC10 = (Assrt) Databases.getSetMMStmt("dec10");
  private final Assrt DEC0H = (Assrt) Databases.getSetMMStmt("dec0h");
  private final Assrt MULCOMLI = (Assrt) Databases.getSetMMStmt("mulcomli");
  private final Assrt MULID1I = (Assrt) Databases.getSetMMStmt("mulid1i");
  private final Assrt MULID2I = (Assrt) Databases.getSetMMStmt("mulid2i");
  private final Assrt MUL01I = (Assrt) Databases.getSetMMStmt("mul01i");
  private final Assrt MUL02I = (Assrt) Databases.getSetMMStmt("mul02i");
  private final Assrt EQCOMI = (Assrt) Databases.getSetMMStmt("eqcomi");
  private final Assrt EQTRI = (Assrt) Databases.getSetMMStmt("eqtri");
  private final Assrt EQTR4I = (Assrt) Databases.getSetMMStmt("eqtr4i");
  private final Assrt OVEQ1I = (Assrt) Databases.getSetMMStmt("oveq1i");
  private final Assrt OVEQ2I = (Assrt) Databases.getSetMMStmt("oveq2i");
  private final Assrt OVEQ12I = (Assrt) Databases.getSetMMStmt("oveq12i");

  private final List<Stmt> PREMISE_PROPOSITIONS =
      Streams.concat(
              Stream.of(
                  EQID, DECMUL1C, DECMUL2C, DEC10, DEC0H, MULCOMLI, MULID1I, MULID2I, MUL01I,
                  MUL02I, EQCOMI, EQTRI, EQTR4I, OVEQ1I, OVEQ2I, OVEQ12I),
              LongStream.rangeClosed(2, 9).mapToObj(op -> numberDefinition(op)),
              LongStream.rangeClosed(1, 9)
                  .mapToObj(
                      op1 ->
                          LongStream.rangeClosed(2, op1)
                              .mapToObj(op2 -> singleDigitProduct(op1, op2)))
                  .flatMap(Function.identity()))
          .collect(Collectors.toList());

  @Override
  public String getDescription() {
    return "This machine generates proofs of equations involving sum and product of natural numbers.";
  }

  @Override
  public boolean isIndexable() {
    return true;
  }

  @Override
  public String getDefaultStrictStatement() {
    return "|- ( ( ; 1 6 x. ; 1 9 ) + ; 1 6 ) = ( 2 x. ; ; 1 6 0 )";
  }

  @Override
  public String getDefaultLenientStatement() {
    return "Not supported";
  }

  @Override
  public List<Stmt> getPremisePropositions() {
    return PREMISE_PROPOSITIONS;
  }

  @Override
  public List<MetamathMachine> getPremiseMachines() {
    return List.of(
        NN0ClosureMachine.getInstance(),
        NNClosureMachine.getInstance(),
        CCClosureMachine.getInstance(),
        NNSumMachine.getInstance());
  }

  @Override
  public MetamathProposition parseLenient(@Nonnull Proposition proposition) {
    return null; // TODO: implement
  }

  @Override
  public String getNotProvableReason(MetamathProposition proposition) {
    ParseNode assrt = proposition.getAssrt();
    if (!isSumOrProductExpressionStatement(assrt)) return DOESNT_HANDLE_REASON;
    var leftValue = getValue(assrt.child[0]);
    var rightValue = getValue(assrt.child[1]);
    if (leftValue == null || rightValue == null)
      return "Machine couldn't compute values in the statement.";
    if (leftValue.number != rightValue.number) return "The two expressions have different values";
    return null;
  }

  @Override
  public MachineProof getProof(MetamathProposition proposition) {
    if (!isProvable(proposition)) return null;
    ParseNode assrt = proposition.getAssrt();

    var leftSide = assrt.child[0];
    var rightSide = assrt.child[1];

    boolean leftIsNumber = isNumberExpression(leftSide);
    boolean rightIsNumber = isNumberExpression(rightSide);
    if (rightIsNumber) {
      if (leftIsNumber) return getProofWhenBothSidesAreNumbers(proposition, leftSide, rightSide);
      return getProofWhenOnlyRightSideIsANumber(proposition, leftSide, rightSide);
    }
    if (leftIsNumber) return getProofWhenOnlyLeftSideIsANumber(proposition, leftSide, rightSide);
    return getProofWhenBothSidesAreExpressions(proposition, leftSide, rightSide);
  }

  private MachineProof getProofWhenBothSidesAreExpressions(
      MetamathProposition prop, ParseNode leftNode, ParseNode rightNode) {
    if (leftNode.child[2].stmt == rightNode.child[2].stmt // Same operator
        && isNumberExpression(rightNode.child[0])
        && isNumberExpression(rightNode.child[1])) {
      if (getValue(leftNode.child[0]).number == getValue(rightNode.child[0]).number) {
        // child[1] of the two nodes have to be equal too
        return proofCase1Special(
            prop,
            leftNode.child[0],
            leftNode.child[1],
            rightNode.child[0],
            rightNode.child[1],
            getOperator(leftNode.child[2]));
      }
    }
    NumberRepresentation value = getValue(leftNode);
    Preconditions.checkArgument(value != null, "This shouldn't happen");

    var substitutions =
        Map.of("A", asExpression(leftNode), "B", value.toString(), "C", asExpression(rightNode));

    return getProofOfAssrt(prop, EQTR4I, substitutions);
  }

  private MachineProof proofCase1Special(
      MetamathProposition prop,
      ParseNode leftChild1,
      ParseNode leftChild2,
      ParseNode rightChild1,
      ParseNode rightChild2,
      String operator) {
    // Leftchild(1 and 2) equal rightchild(1 and 2) respectively.
    // RightChild(1 and 2) are numbers.
    var side1Equal = leftChild1.toString().equals(rightChild1.toString());
    var side2Equal = leftChild2.toString().equals(rightChild2.toString());
    if (!side2Equal) {
      if (side1Equal)
        return proofCase1Special1(prop, leftChild1, leftChild2, rightChild2, operator);
      else
        return proofCase1Special2(prop, leftChild1, leftChild2, rightChild1, rightChild2, operator);
    }
    if (!side1Equal) return proofCase1Special3(prop, leftChild1, leftChild2, rightChild1, operator);

    var factory = new StepFactory(Map.of(), null);
    return getProofForSetMMAssrt(
        prop, factory, (Assrt) safeUse(EQID), Map.of("A", asExpression(prop.getAssrt().child[0])));
  }

  private MachineProof proofCase1Special2(
      MetamathProposition prop,
      ParseNode leftChild1,
      ParseNode leftChild2,
      ParseNode rightChild1,
      ParseNode rightChild2,
      String operator) {
    // Leftchild (1 and 2) equal rightchild (1 and 2) in value.
    // RightChild (1 and 2) are numbers.
    var substitutions =
        Map.ofEntries(
            Map.entry("A", asExpression(leftChild1)),
            Map.entry("B", asExpression(rightChild1)),
            Map.entry("C", asExpression(leftChild2)),
            Map.entry("D", asExpression(rightChild2)),
            Map.entry("F", operator));

    return getProofOfAssrt(prop, OVEQ12I, substitutions);
  }

  private MachineProof proofCase1Special1(
      MetamathProposition prop,
      ParseNode leftChild1,
      ParseNode leftChild2,
      ParseNode rightChild2,
      String operator) {
    // Leftchild(1 and 2) equal rightchild(1 and 2) in value respectively.
    // RightChild(1 and 2) are numbers.
    var substitutions =
        Map.ofEntries(
            Map.entry("A", asExpression(leftChild2)),
            Map.entry("B", asExpression(rightChild2)),
            Map.entry("C", asExpression(leftChild1)),
            Map.entry("F", operator));

    return getProofOfAssrt(prop, OVEQ2I, substitutions);
  }

  private MachineProof proofCase1Special3(
      MetamathProposition prop,
      ParseNode leftChild1,
      ParseNode leftChild2,
      ParseNode rightChild1,
      String operator) {
    // Leftchild(1 and 2) equal rightchild(1 and 2) in value respectively.
    // RightChild(1 and 2) are numbers.
    var substitutions =
        Map.ofEntries(
            Map.entry("A", asExpression(leftChild1)),
            Map.entry("B", asExpression(rightChild1)),
            Map.entry("C", asExpression(leftChild2)),
            Map.entry("F", operator));

    return getProofOfAssrt(prop, OVEQ1I, substitutions);
  }

  private MachineProof getProofWhenOnlyLeftSideIsANumber(
      MetamathProposition prop, ParseNode leftNode, ParseNode rightNode) {
    // Left is number, right is expression.
    if (getOperator(rightNode.child[2]).equals("+") && isNumberExpression(rightNode.child[0])) {
      var op1 = getValue(rightNode.child[0]);
      boolean op2Is1 = rightNode.child[1].stmt.getLabel().equals("c1");
      if (op1.leadingZeros == 0 && op1.number >= 2 && op1.number <= 9 && op2Is1) {
        return new MachineProof(numberDefinition(op1.number + 1));
      }
    }
    var substitutions = Map.of("A", asExpression(rightNode), "B", asExpression(leftNode));
    return getProofOfAssrt(prop, EQCOMI, substitutions);
  }

  private MachineProof getProofWhenBothSidesAreNumbers(
      MetamathProposition prop, ParseNode leftNode, ParseNode rightNode) {
    // Left and right are both numbers
    var leftNum = NumberRepresentation.fromParseNode(leftNode);
    var rightNum = NumberRepresentation.fromParseNode(rightNode);
    Preconditions.checkArgument(leftNum.number == rightNum.number);
    if (leftNum.equals(rightNum)) {
      return getProofOfAssrt(prop, EQID, Map.of("A", leftNum.toString()));
    }
    if (leftNum == PRIMITIVE_10 && rightNum == DECIMAL_10) {
      return new MachineProof(safeUse(DEC10));
    }
    if ((leftNum == DECIMAL_10 && rightNum == PRIMITIVE_10)
        || (leftNum.leadingZeros > rightNum.leadingZeros)) {
      return getProofOfAssrt(
          prop, EQCOMI, Map.of("A", rightNum.toString(), "B", leftNum.toString()));
    }
    if (leftNum.leadingZeros == 0
        && rightNum.leadingZeros == 1
        && leftNum.number >= 0
        && leftNum.number <= 10) {
      return getProofOfAssrt(prop, DEC0H, Map.of("A", leftNum.toString()));
    }

    throw new UnsupportedOperationException(
        MessageFormat.format(
            "Dont know how to prove: {0} = {1}", leftNum.toString(), rightNum.toString()));
  }

  private MachineProof getProofWhenOnlyRightSideIsANumber(
      MetamathProposition prop, ParseNode leftNode, ParseNode rightNode) {
    var rightNum = getValue(rightNode);
    if (rightNum == PRIMITIVE_10 || rightNum.leadingZeros > 0) {
      var substitutions =
          Map.ofEntries(
              Map.entry("A", asExpression(leftNode)),
              Map.entry("B", new NumberRepresentation(rightNum.number).toString()),
              Map.entry("C", rightNum.toString()));
      return getProofOfAssrt(prop, EQTRI, substitutions);
    }

    // Right is a number, left is expression
    // If right is primitive10, this will fail.
    var operand1Node = leftNode.child[0];
    var operand2Node = leftNode.child[1];
    var operator = getOperator(leftNode.child[2]);
    var operand1IsNumber = isNumberExpression(operand1Node);
    var operand2IsNumber = isNumberExpression(operand2Node);
    if (!operand1IsNumber || !operand2IsNumber) {
      return getProofWhenLeftNodeIsAComplexExpression(prop, leftNode, rightNode);
    }

    if (operator.equals("+")) {
      return new MachineProof(
          List.of(), Map.of(), Map.of(prop.getResourcePtr(), safeUse(NNSumMachine.getInstance())));
    } else {
      return productProof(prop, operand1Node, operand2Node);
    }
  }

  private MachineProof getProofWhenLeftNodeIsAComplexExpression(
      MetamathProposition prop, ParseNode leftNode, ParseNode resultNode) {
    var operator = getOperator(leftNode.child[2]);
    // Both operands of leftNode are expressions.
    var forTransitive =
        MessageFormat.format(
            "( {0} {1} {2} )",
            getValue(leftNode.child[0]).toString(),
            operator,
            getValue(leftNode.child[1]).toString());
    var substitutions =
        Map.ofEntries(
            Map.entry("A", asExpression(leftNode)),
            Map.entry("B", forTransitive),
            Map.entry("C", asExpression(resultNode)));

    return getProofOfAssrt(prop, EQTRI, substitutions);
  }

  private MachineProof productProof(
      MetamathProposition proposition, ParseNode operand1, ParseNode operand2) {
    var op1 = getValue(operand1);
    var op2 = getValue(operand2);
    if (op1.number >= 10) {
      return getDecimalProductProof(proposition, operand1, operand2, true); // decmul1c
    }
    if (op2.number >= 10) {
      return getDecimalProductProof(proposition, operand1, operand2, false); // decmul2c
    }

    if (op2.number == 0) {
      return getProofOfAssrt(proposition, MUL01I, Map.of("A", asExpression(operand1)));
    }
    if (op1.number == 0) {
      return getProofOfAssrt(proposition, MUL02I, Map.of("A", asExpression(operand2)));
    }
    if (op2.number == 1) {
      return getProofOfAssrt(proposition, MULID1I, Map.of("A", asExpression(operand1)));
    }
    if (op1.number == 1) {
      return getProofOfAssrt(proposition, MULID2I, Map.of("A", asExpression(operand2)));
    }
    if (op2.number > op1.number) {
      return productProofSwapOperands(proposition, op1, op2);
    }

    return new MachineProof(safeUse(singleDigitProduct(op1.number, op2.number)));
  }

  private MachineProof productProofSwapOperands(
      MetamathProposition prop, NumberRepresentation operand1, NumberRepresentation operand2) {
    NumberRepresentation resultNode = new NumberRepresentation(operand1.number * operand2.number);
    var substitutions =
        Map.of("A", operand2.toString(), "B", operand1.toString(), "C", resultNode.toString());
    return getProofOfAssrt(prop, MULCOMLI, substitutions);
  }

  private MachineProof getDecimalProductProof(
      MetamathProposition prop, ParseNode operand1, ParseNode operand2, boolean operand1IsDecimal) {
    NumberRepresentation n, p;
    if (operand1IsDecimal) {
      n = getValue(operand1);
      p = getValue(operand2);
    } else {
      n = getValue(operand2);
      p = getValue(operand1);
    }

    var a = new NumberRepresentation(n.number / 10);
    var b = new NumberRepresentation(n.number % 10);
    var d = new NumberRepresentation((b.number * p.number) % 10);
    var e = new NumberRepresentation((b.number * p.number) / 10);
    var c = new NumberRepresentation(a.number * p.number + e.number);
    var substitutions =
        Map.ofEntries(
            Map.entry("A", a.toString()),
            Map.entry("B", b.toString()),
            Map.entry("C", c.toString()),
            Map.entry("D", d.toString()),
            Map.entry("E", e.toString()),
            Map.entry("N", n.toString()),
            Map.entry("P", p.toString()));
    return getProofOfAssrt(prop, operand1IsDecimal ? DECMUL1C : DECMUL2C, substitutions);
  }

  private String getOperator(ParseNode node) {
    if (node.stmt.getLabel().equals("caddc")) return "+";
    if (node.stmt.getLabel().equals("cmul")) return "x.";
    throw new IllegalStateException("No operator for " + node.stmt.getLabel());
  }

  private static Stmt singleDigitProduct(long op1, long op2) {
    Preconditions.checkArgument(op1 >= op2 && op1 >= 1 && op1 <= 9 && op2 >= 1 && op2 <= 9);
    return Databases.getSetMMStmt(op1 + "t" + op2 + "e" + (op1 * op2));
  }

  private static Stmt numberDefinition(long number) {
    Preconditions.checkArgument(number >= 2 && number <= 10);
    return Databases.getSetMMStmt("df-" + number);
  }

  private MachineProof getProofOfAssrt(
      MetamathProposition proposition, Assrt assrt, Map<String, String> substitutions) {
    var stepFactory =
        StepFactory.forArgumentWithGeneratedPremises(assrt, substitutions, this::machineDeterminer);
    return getProofForSetMMAssrt(proposition, stepFactory, (Assrt) safeUse(assrt), substitutions);
  }

  private MetamathMachine machineDeterminer(ParseNode node) {
    if (node.stmt.getLabel().equals("wcel")) {
      String setLabel = node.child[1].stmt.getLabel();
      if (setLabel.equals("cn0")) return safeUse(NN0ClosureMachine.getInstance());
      if (setLabel.equals("cn")) return safeUse(NNClosureMachine.getInstance());
      if (setLabel.equals("cc")) return safeUse(CCClosureMachine.getInstance());
    }
    if (node.stmt.getLabel().equals("wceq")) {
      return safeUse(NNSumProductEquationMachine.getInstance());
    }
    return null;
  }
}
