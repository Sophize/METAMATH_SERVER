package org.sophize.metamath.formachines.machines;

import com.google.common.base.Preconditions;
import mmj.lang.Assrt;
import mmj.lang.ParseNode;
import mmj.lang.Stmt;
import org.sophize.datamodel.Proposition;
import org.sophize.datamodel.ResourcePointer;
import org.sophize.metamath.Utils;
import org.sophize.metamath.formachines.*;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import static org.sophize.datamodel.ResourceType.ARGUMENT;
import static org.sophize.metamath.formachines.MachineUtils.getProofForSetMMAssrt;
import static org.sophize.metamath.formachines.MachineUtils.parseSetMMStatement;
import static org.sophize.metamath.formachines.ParseNodeHelpers.*;

public class NNSumMachine extends MetamathMachine {
  private static NNSumMachine instance = new NNSumMachine();

  private NNSumMachine() {}

  public static NNSumMachine getInstance() {
    return instance;
  }

  private final Assrt DECADDI = (Assrt) Databases.getSetMMStmt("decaddi");
  private final Assrt DECADDCI = (Assrt) Databases.getSetMMStmt("decaddci");
  private final Assrt DECADD = (Assrt) Databases.getSetMMStmt("decadd");
  private final Assrt DECADDC = (Assrt) Databases.getSetMMStmt("decaddc");
  private final Assrt EQID = (Assrt) Databases.getSetMMStmt("eqid");
  private final Assrt EQTRI = (Assrt) Databases.getSetMMStmt("eqtri");
  private final Assrt OVEQ1I = (Assrt) Databases.getSetMMStmt("oveq1i");
  private final Assrt ADDID1I = (Assrt) Databases.getSetMMStmt("addid1i");
  private final Assrt ADDCOMLI = (Assrt) Databases.getSetMMStmt("addcomli");

  private final List<Stmt> PREMISE_PROPOSITIONS =
      Stream.concat(
              Stream.of(DECADDI, DECADDCI, DECADD, DECADDC, EQID, EQTRI, OVEQ1I, ADDID1I, ADDCOMLI),
              LongStream.rangeClosed(1, 9)
                  .mapToObj(
                      op1 ->
                          LongStream.rangeClosed(1, op1).mapToObj(op2 -> singleDigitSums(op1, op2)))
                  .flatMap(Function.identity()))
          .collect(Collectors.toList());

  @Override
  public String getDescription() {
    return "To get proofs for statements like |- ( ; 1 3 + 8 ) = ; 2 1\n\n"
        + "Also supports statements of type |- ( ( A + B ) + 1 ) = C";
  }

  @Override
  public String getDefaultStrictStatement() {
    return "|- ( ; 1 6 + ; 1 9 ) < ; 3 5";
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
        CCClosureMachine.getInstance());
  }

  @Override
  public MetamathProposition parseLenient(@Nonnull Proposition proposition) {
    return null; // TODO: implement
  }

  @Override
  public String getNotProvableReason(MetamathProposition proposition) {
    ParseNode assrt = proposition.getAssrt();
    if (!isSumExpressionStatement(assrt)) return DOESNT_HANDLE_REASON;
    var leftChild = assrt.child[0];
    var rightChild = assrt.child[1];

    if (!isNumberExpression(rightChild)) return DOESNT_HANDLE_REASON;
    if (!isSumOrNumberExpression(leftChild)) return DOESNT_HANDLE_REASON;
    var leftValue = getValue(leftChild);
    var rightValue = getValue(rightChild);
    if (isNumberExpression(leftChild)) {
      if (leftValue != null && leftValue.equals(rightValue)) return null; // |- A = A
      return "Not supported yet."; // Eg. |- 10 = ; 1 0
    }
    if (leftValue.number != rightValue.number) return "The two sides have different values.";

    var operand1 = leftChild.child[0];
    var operand2 = leftChild.child[1];
    if (!isNumberExpression(operand2)) return DOESNT_HANDLE_REASON;

    if (!isSumOrNumberExpression(operand1)) return DOESNT_HANDLE_REASON;

    if (isNumberExpression(operand1)) return null; // |- ( A + B ) = C

    // Special case needed for carryover.
    // |- ( A + B ) + 1 = C
    if (!operand2.stmt.getLabel().equals("c1")) return DOESNT_HANDLE_REASON;
    var operand1Part1 = operand1.child[0];
    var operand2Part2 = operand1.child[1];
    if (!isNumberExpression(operand1Part1) || !isNumberExpression(operand2Part2))
      return DOESNT_HANDLE_REASON;
    return null;
  }

  @Override
  public MachineProof getProof(MetamathProposition proposition) {
    if (!isProvable(proposition)) return null;
    ParseNode assrt = proposition.getAssrt();

    var leftSide = assrt.child[0];

    if (isNumberExpression(leftSide)) { // |- A = A
      return getSameNumberProof(proposition, leftSide);
    }
    var operand1 = leftSide.child[0];
    var operand2 = leftSide.child[1];

    if (!isNumberExpression(operand1)) { // |- ( ( A + B ) + 1 ) = C
      Preconditions.checkArgument(operand2.stmt.getLabel().equals("c1"));
      return getSpecialCaseProof(proposition);
    }

    // Operand1, operand2 and resultNode are all numbers (not expressions).
    // TODO: handle non standard representations - PRIMITIVE10 and leadingZeros.
    var op1 = getValue(operand1);
    var op2 = getValue(operand2);
    if (op1.number >= 10 && op2.number >= 10) {
      return getDecimalAdditionProof(proposition, operand1, operand2);
    }
    if (op1.number < op2.number) {
      return getProofUsingCommutationOfOperands(proposition, op1, op2);
    }
    if (op1.number >= 10) {
      return getAddDigitToDecimalProof(proposition, operand1, operand2);
    }
    if (op2.number == 0) {
      return getAddToZeroProof(proposition, operand1);
    }

    // Things that can shorten proof: addid2i(skip swap), 00id, 0p1e1
    return new MachineProof(safeUse(singleDigitSums(op1.number, op2.number)));
  }

  private MachineProof getSameNumberProof(MetamathProposition proposition, ParseNode numberNode) {
    var number = NumberRepresentation.fromParseNode(numberNode);
    var substitutions = Map.of("A", number.toString());
    var factory = StepFactory.forArgumentWithGeneratedPremises(EQID, substitutions, null);
    return getProofForSetMMAssrt(proposition, factory, (Assrt) safeUse(EQID), substitutions);
  }

  private MachineProof getSpecialCaseProof(MetamathProposition prop) {
    // |- ( A + B ) = R
    // |- ( ( A + B ) + 1 ) = R + 1   (oveq1i)
    // |- ( R + 1 ) = C
    // |- ( ( A + B ) + 1 ) = C       (eqtri)
    var assrt = prop.getAssrt();
    var leftSide = assrt.child[0];
    var operand1 = leftSide.child[0];
    var a = NumberRepresentation.fromParseNode(operand1.child[0]);
    var b = NumberRepresentation.fromParseNode(operand1.child[1]);

    var t = new NumberRepresentation(a.number + b.number);
    var c = new NumberRepresentation(t.number + 1);

    var oveq1iSubstitutions =
        Map.ofEntries(
            Map.entry("A", asExpression(operand1)),
            Map.entry("B", t.toString()),
            Map.entry("C", "1"),
            Map.entry("F", "+"));

    var eqtriSubstitutions =
        Map.ofEntries(
            Map.entry("A", asExpression(leftSide)),
            Map.entry("B", "( " + t.toString() + " + 1 )"),
            Map.entry("C", c.toString()));

    var oveq1iHyp =
        Utils.getStatementWithSubstitutions(OVEQ1I.getLogHypArray()[0], oveq1iSubstitutions);

    var eqtriHyp =
        Utils.getStatementWithSubstitutions(EQTRI.getLogHypArray()[1], eqtriSubstitutions);

    var oveq1iHypProp = new MetamathProposition(parseSetMMStatement(oveq1iHyp));
    var eqtriHypProp = new MetamathProposition(parseSetMMStatement(eqtriHyp));

    var steps =
        List.of(
            ArgumentStep.fromSetMMEphemeralReference(oveq1iHypProp, NNSumMachine.getInstance()),
            ArgumentStep.fromSetMM(OVEQ1I, List.of(0), oveq1iSubstitutions),
            ArgumentStep.fromSetMMEphemeralReference(eqtriHypProp, NNSumMachine.getInstance()),
            ArgumentStep.fromSetMM(EQTRI, List.of(1, 2), eqtriSubstitutions));
    var newArgumentPtr = ResourcePointer.ephemeral(ARGUMENT, prop.getResourcePtr().getId());
    var newArg = new MetamathArgument(newArgumentPtr, prop, steps);
    return new MachineProof(
        newArg.getGeneratedPremises(),
        Map.of(prop.getResourcePtr(), newArg),
        newArg.getMachineArguments());
  }

  private MachineProof getDecimalAdditionProof(
      MetamathProposition prop, ParseNode operand1, ParseNode operand2) {
    var op1Value = getValue(operand1);
    var op2Value = getValue(operand2);
    long a = op1Value.number / 10;
    long b = op1Value.number % 10;
    long c = op2Value.number / 10;
    long d = op2Value.number % 10;
    long e, f;
    Assrt assrt;
    if (b + d >= 10) {
      e = a + c + 1;
      f = (b + d) % 10;
      assrt = DECADDC;
    } else {
      e = a + c;
      f = b + d;
      assrt = DECADD;
    }
    var substitutions =
        Map.ofEntries(
            Map.entry("A", new NumberRepresentation(a).toString()),
            Map.entry("B", new NumberRepresentation(b).toString()),
            Map.entry("C", new NumberRepresentation(c).toString()),
            Map.entry("D", new NumberRepresentation(d).toString()),
            Map.entry("E", new NumberRepresentation(e).toString()),
            Map.entry("F", new NumberRepresentation(f).toString()),
            Map.entry("M", asExpression(operand1)),
            Map.entry("N", asExpression(operand2)));
    return getProofOfAssrt(prop, assrt, substitutions);
  }

  private MachineProof getProofUsingCommutationOfOperands(
      MetamathProposition prop, NumberRepresentation operand1, NumberRepresentation operand2) {
    NumberRepresentation resultNode = new NumberRepresentation(operand1.number + operand2.number);
    var substitutions =
        Map.of("A", operand2.toString(), "B", operand1.toString(), "C", resultNode.toString());
    return getProofOfAssrt(prop, ADDCOMLI, substitutions);
  }

  private MachineProof getAddDigitToDecimalProof(
      MetamathProposition prop, ParseNode operand1, ParseNode operand2) {
    var substitutions = new HashMap<String, String>();
    var op1Value = getValue(operand1);
    var op2Value = getValue(operand2);
    var a = op1Value.number / 10;
    var b = op1Value.number % 10;
    var n = op2Value.number;

    substitutions.put("A", new NumberRepresentation(a).toString());
    substitutions.put("B", new NumberRepresentation(b).toString());
    substitutions.put("M", asExpression(operand1));
    substitutions.put("N", asExpression(operand2));

    if (b + n >= 10) {
      substitutions.put("C", new NumberRepresentation((b + n) % 10).toString());
      substitutions.put("D", new NumberRepresentation(a + 1).toString());
      return getProofOfAssrt(prop, DECADDCI, substitutions);
    } else {
      substitutions.put("C", new NumberRepresentation(b + n).toString());
      return getProofOfAssrt(prop, DECADDI, substitutions);
    }
  }

  private MachineProof getAddToZeroProof(MetamathProposition prop, ParseNode operand1) {
    var substitutions = Map.of("A", asExpression(operand1));
    return getProofOfAssrt(prop, ADDID1I, substitutions);
  }

  private static Stmt singleDigitSums(long op1, long op2) {
    Preconditions.checkArgument(op1 >= op2 && op1 >= 1 && op1 <= 9 && op2 >= 1 && op2 <= 9);
    return Databases.getSetMMStmt(op1 + "p" + op2 + "e" + (op1 + op2));
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
    if (node.stmt.getLabel().equals("wceq")) return safeUse(NNSumMachine.getInstance());
    return null;
  }
}
