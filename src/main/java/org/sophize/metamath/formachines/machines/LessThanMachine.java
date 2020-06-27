package org.sophize.metamath.formachines.machines;

import com.google.common.base.Preconditions;
import mmj.lang.Assrt;
import mmj.lang.ParseNode;
import mmj.lang.Stmt;
import org.sophize.datamodel.Proposition;
import org.sophize.datamodel.ResourcePointer;
import org.sophize.metamath.formachines.*;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static org.sophize.metamath.formachines.MachineUtils.getDigitsLenient;
import static org.sophize.metamath.formachines.MachineUtils.getProofForAssrt;
import static org.sophize.metamath.formachines.NumberRepresentation.DECIMAL_10;
import static org.sophize.metamath.formachines.NumberRepresentation.PRIMITIVE_10;

public class LessThanMachine extends MetamathMachine {
  private static LessThanMachine instance = new LessThanMachine();

  private LessThanMachine() {}

  public static LessThanMachine getInstance() {
    return instance;
  }

  private final Assrt DECLTI = (Assrt) Databases.getSetMMStmt("declti");
  private final Assrt DECLT = (Assrt) Databases.getSetMMStmt("declt");
  private final Assrt DECLTC = (Assrt) Databases.getSetMMStmt("decltc");
  private final Assrt EQBRTRI = (Assrt) Databases.getSetMMStmt("eqbrtri");
  private final Assrt DEC10 = (Assrt) Databases.getSetMMStmt("dec10");
  private final List<Stmt> PREMISE_PROPOSITIONS =
      Stream.concat(
              Stream.of(DECLTI, DECLT, DECLTC, EQBRTRI, DEC10),
              LongStream.range(1, 11)
                  .mapToObj(
                      op2 ->
                          LongStream.range(0, op2)
                              .mapToObj(
                                  op1 ->
                                      nonDecimalComparisionStatement(
                                          new NumberRepresentation(op1, false),
                                          new NumberRepresentation(op2, false))))
                  .flatMap(Function.identity()))
          .collect(toList());
  private static String LESS_THAN_SYMBOL = "<";

  public String getDescription() {
    return "To get proofs for statements like |- ; 1 5 < ; 2 7";
  }

  public boolean isIndexable() {
    return true;
  }

  public String getDefaultStrictStatement() {
    return "|- ; 1 5 < ; 2 7";
  }

  public String getDefaultLenientStatement() {
    return "15 < 27";
  }

  public List<Stmt> getPremisePropositions() {
    return PREMISE_PROPOSITIONS;
  }

  public List<ResourcePointer> getPremiseMachines() {
    return List.of(
        NNMachine.getInstance().getAssignablePtr(), NN0Machine.getInstance().getAssignablePtr());
  }

  public MetamathProposition parseLenient(@Nonnull Proposition proposition) {
    try {
      String stmt = proposition.getStatement();
      int lessThanLoc = stmt.indexOf(LESS_THAN_SYMBOL);
      if (lessThanLoc <= 0) return null;
      List<Integer> operand1Digits = getDigitsLenient(stmt.substring(0, lessThanLoc));
      List<Integer> operand2Digits = getDigitsLenient(stmt.substring(lessThanLoc + 1));
      if (operand1Digits == null || operand2Digits == null) return null;

      var operand1 = NumberRepresentation.fromDigits(operand1Digits);
      var operand2 = NumberRepresentation.fromDigits(operand2Digits);

      proposition.setStatement("|- " + operand1 + " < " + operand2);
      return parseStrict(proposition);
    } catch (Exception e) {
      return null;
    }
  }

  public String getNotProvableReason(MetamathProposition proposition) {
    ParseNode assrt = proposition.getAssrt();
    String doesntHandleReason = "This machine doesn't handle such statements";
    if (!assrt.stmt.getLabel().equals("wbr")) return doesntHandleReason;
    if (assrt.child.length != 3) return doesntHandleReason;
    if (!assrt.child[2].stmt.getLabel().equals("clt")) return doesntHandleReason;
    var operand1 = NumberRepresentation.fromParseNode(assrt.child[0]);
    var operand2 = NumberRepresentation.fromParseNode(assrt.child[1]);

    if (operand1.number >= operand2.number) return "The first operand is greater than the second";

    if (operand1.number < 0) return "This machine doesn't handle negative numbers";

    if (operand1.leadingZeros != 0 || operand2.leadingZeros != 0) {
      return "Machine doesn't handle leading zeros yet.";
    }
    return null;
  }

  public MachineProof getProof(MetamathProposition proposition) {
    if (getNotProvableReason(proposition) != null) return null;
    ParseNode assrt = proposition.getAssrt();
    var operand1 = NumberRepresentation.fromParseNode(assrt.child[0]);
    var operand2 = NumberRepresentation.fromParseNode(assrt.child[1]);

    if (!operand2.decimalFormat) {
      Preconditions.checkArgument(operand2.number <= 10);
      return new MachineProof(safeUse(nonDecimalComparisionStatement(operand1, operand2)));
    }

    // operand2 > 10 and is in decimal format
    if (operand1.number < 10) {
      return getCompareWithDigitProof(proposition, operand1, operand2);
    } else if (operand1.number == 10 && !operand1.decimalFormat) {
      return getCompareWithPrimitive10Proof(proposition, operand1, operand2);
    } else if (operand1.number / 10 != operand2.number / 10) {
      return getCompareWithUnequalHigherPlacesProof(proposition, operand1, operand2);
    } else {
      return getCompareWithEqualHigherPlacesProof(proposition, operand1, operand2);
    }
  }

  private MachineProof getCompareWithDigitProof(
      MetamathProposition prop, NumberRepresentation c, NumberRepresentation operand2) {
    Preconditions.checkArgument(c.number < 10);
    Preconditions.checkArgument(operand2.decimalFormat);

    var a = new NumberRepresentation(operand2.number / 10);
    var b = new NumberRepresentation(operand2.number % 10);

    var decltiSubstitutions = Map.of("A", a.toString(), "B", b.toString(), "C", c.toString());

    var hints =
        StepFactory.forArgumentWithGeneratedPremises(
            DECLTI, decltiSubstitutions, LessThanMachine::machineDeterminer);
    return getProofForAssrt(prop, hints, (Assrt) safeUse(DECLTI), decltiSubstitutions);
  }

  private MachineProof getCompareWithPrimitive10Proof(
      MetamathProposition prop, NumberRepresentation operand1, NumberRepresentation operand2) {
    Preconditions.checkArgument(operand1.number == 10 && !operand1.decimalFormat);
    Preconditions.checkArgument(operand2.decimalFormat);

    var eqbrtriSubstitutions =
        Map.of(
            "A",
            PRIMITIVE_10.toString(),
            "B",
            DECIMAL_10.toString(),
            "C",
            operand2.toString(),
            "R",
            LESS_THAN_SYMBOL);

    StepFactory hints =
        new StepFactory(
            Map.of(
                0,
                ArgumentStep.fromSetMM(safeUse(DEC10)),
                EQBRTRI.getLogHypArrayLength(),
                ArgumentStep.fromSetMM(EQBRTRI, List.of(0, 1), eqbrtriSubstitutions)),
            LessThanMachine::machineDeterminer);

    return getProofForAssrt(prop, hints, (Assrt) safeUse(EQBRTRI), eqbrtriSubstitutions);
  }

  private MachineProof getCompareWithUnequalHigherPlacesProof(
      MetamathProposition prop, NumberRepresentation operand1, NumberRepresentation operand2) {
    Preconditions.checkArgument(operand1.decimalFormat && operand2.decimalFormat);
    Preconditions.checkArgument(operand1.number / 10 != operand2.number / 10);

    var a = new NumberRepresentation(operand1.number / 10);
    var c = new NumberRepresentation(operand1.number % 10);
    var b = new NumberRepresentation(operand2.number / 10);
    var d = new NumberRepresentation(operand2.number % 10);

    var decltcSubstitutions =
        Map.of("A", a.toString(), "B", b.toString(), "C", c.toString(), "D", d.toString());

    var hints =
        StepFactory.forArgumentWithGeneratedPremises(
            DECLTC, decltcSubstitutions, LessThanMachine::machineDeterminer);

    return getProofForAssrt(prop, hints, (Assrt) safeUse(DECLTC), decltcSubstitutions);
  }

  private MachineProof getCompareWithEqualHigherPlacesProof(
      MetamathProposition prop, NumberRepresentation operand1, NumberRepresentation operand2) {
    Preconditions.checkArgument(operand1.decimalFormat && operand2.decimalFormat);
    Preconditions.checkArgument(operand1.number / 10 == operand2.number / 10);

    var a = new NumberRepresentation(operand1.number / 10); // same as operand2.number/10
    var b = new NumberRepresentation(operand1.number % 10);
    var c = new NumberRepresentation(operand2.number % 10);

    var decltSubstitutions = Map.of("A", a.toString(), "B", b.toString(), "C", c.toString());

    var hints =
        StepFactory.forArgumentWithGeneratedPremises(
            DECLT, decltSubstitutions, LessThanMachine::machineDeterminer);
    return getProofForAssrt(prop, hints, (Assrt) safeUse(DECLT), decltSubstitutions);
  }

  private static Stmt nonDecimalComparisionStatement(
      NumberRepresentation operand1, NumberRepresentation operand2) {
    Preconditions.checkArgument(operand1.number >= 0 && operand1.number < 10);
    Preconditions.checkArgument(operand2.number <= 10 && !operand2.decimalFormat);
    Preconditions.checkArgument(operand1.number < operand2.number);
    if (operand1.number == 0 && operand2.number > 1) {
      return Databases.getSetMMStmt(operand2.number + "pos");
    }
    return Databases.getSetMMStmt(operand1.number + "lt" + operand2.number);
  }

  private static MetamathMachine machineDeterminer(ParseNode node) {
    if (node.stmt.getLabel().equals("wbr")) {
      if (node.child[2].stmt.getLabel().equals("clt")) return LessThanMachine.getInstance();
    }
    if (node.stmt.getLabel().equals("wcel")) {
      if (node.child[1].stmt.getLabel().equals("cn0")) return NN0Machine.getInstance();
      if (node.child[1].stmt.getLabel().equals("cn")) return NNMachine.getInstance();
    }
    return null;
  }
}
