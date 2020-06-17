package org.sophize.metamath.formachines.machines;

import com.google.common.base.Preconditions;
import mmj.lang.Stmt;
import org.sophize.datamodel.Language;
import org.sophize.datamodel.Proposition;
import org.sophize.datamodel.ResourcePointer;
import org.sophize.metamath.formachines.*;
import org.sophize.metamath.formachines.propositions.*;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import static org.sophize.datamodel.ResourceType.ARGUMENT;
import static org.sophize.metamath.formachines.MachineUtils.getDigitsLenient;
import static org.sophize.metamath.formachines.NumberRepresentation.DECIMAL_10;
import static org.sophize.metamath.formachines.NumberRepresentation.PRIMITIVE_10;
import static org.sophize.metamath.formachines.PropositionFactory.*;

public class LessThanMachine extends MetamathMachine {
  private static LessThanMachine instance = new LessThanMachine();

  private LessThanMachine() {}

  public static LessThanMachine getInstance() {
    return instance;
  }

  private final Stmt DECLTI = Databases.getSetMMStmt("declti");
  private final Stmt DECLT = Databases.getSetMMStmt("declt");
  private final Stmt DECLTC = Databases.getSetMMStmt("decltc");
  private final Stmt EQBRTRI = Databases.getSetMMStmt("eqbrtri");
  private final Stmt DEC10 = Databases.getSetMMStmt("dec10");
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
                                          new NumberRepresentation(op1),
                                          new NumberRepresentation(op2))))
                  .flatMap(Function.identity()))
          .collect(Collectors.toList());
  private static String LESS_THAN_SYMBOL = "<";

  public String getDescription() {
    return "To get proofs for statements like |- ; 1 5 < ; 2 7";
  }

  public boolean isIndexable() {
    return true;
  }

  public ResourcePointer getPermanentPtr() {
    return MachineId.NN.getPermanentPtr();
  }

  public Language getDefaultLanguage() {
    return Language.METAMATH_SET_MM;
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

  public MetamathProposition parseStrict(@Nonnull Proposition proposition) {
    if (proposition.getLanguage() != Language.METAMATH_SET_MM) return null;

    String[] tokens = MachineUtils.tokenizeString(proposition.getStatement());
    tokens = MachineUtils.stripMarker(tokens);
    if (tokens.length < 4) return null;
    if (!tokens[0].equals("|-")) return null;
    int operand1EndLoc = 1;
    for (; operand1EndLoc < tokens.length; operand1EndLoc++) {
      if (!NumberRepresentation.isNumberSymbol(tokens[operand1EndLoc])) break;
    }
    if (operand1EndLoc == 1 || operand1EndLoc == tokens.length) return null;
    if (!tokens[operand1EndLoc].equals(LESS_THAN_SYMBOL)) return null;

    var operand1 = NumberRepresentation.fromTokens(Arrays.copyOfRange(tokens, 1, operand1EndLoc));
    var operand2 =
        NumberRepresentation.fromTokens(
            Arrays.copyOfRange(tokens, operand1EndLoc + 1, tokens.length));
    if (operand1 == null || operand2 == null) return null;
    Preconditions.checkArgument(operand1.number != 12, "Boom bak");
    return LessThanProposition(operand1, operand2);
  }

  public MetamathProposition parseLenient(@Nonnull Proposition proposition) {
    try {
      String stmt = proposition.getStatement();
      int lessThanLoc = stmt.indexOf(LESS_THAN_SYMBOL);
      if (lessThanLoc <= 0) return null;
      List<Integer> operand1Digits = getDigitsLenient(stmt.substring(0, lessThanLoc));
      List<Integer> operand2Digits = getDigitsLenient(stmt.substring(lessThanLoc + 1));
      if (operand1Digits == null || operand2Digits == null) return null;

      return LessThanProposition(
          NumberRepresentation.fromDigits(operand1Digits),
          NumberRepresentation.fromDigits(operand2Digits));
    } catch (Exception e) {
      return null;
    }
  }

  public String getNotProvableReason(MetamathProposition proposition) {
    if (!(proposition.getAssrt() instanceof LessThanStatement)) {
      return "This machine doesn't prove such statements";
    }

    LessThanStatement stmt = (LessThanStatement) proposition.getAssrt();
    var operand1 = stmt.getOperand1();
    var operand2 = stmt.getOperand2();
    if (operand1.number >= operand2.number) {
      return "The first operand is greater than the second";
    }
    if (operand1.number < 0) {
      return "This machine doesn't handle negative numbers";
    }
    if (operand1.leadingZeros != 0 || operand2.leadingZeros != 0) {
      return "Machine doesn't handle leading zeros yet.";
    }
    return null;
  }

  public MachineProof getProof(MetamathProposition proposition) {
    if (getNotProvableReason(proposition) != null) return null;
    var lessThanStatement = (LessThanStatement) proposition.getAssrt();
    var operand1 = lessThanStatement.getOperand1();
    var operand2 = lessThanStatement.getOperand2();

    if (!operand2.decimalFormat) {
      Preconditions.checkArgument(operand2.number <= 10);
      return new MachineProof(safeUse(nonDecimalComparisionStatement(operand1, operand2)));
    }

    // operand2 > 10 and is in decimal format
    if (operand1.number < 10) {
      return getCompareWithDigitProof(proposition);
    } else if (operand1.number == 10 && !operand1.decimalFormat) {
      return getCompareWithPrimitive10Proof(proposition);
    } else if (operand1.number / 10 != operand2.number / 10) {
      return getCompareWithUnequalHigherPlacesProof(proposition);
    } else {
      return getCompareWithEqualHigherPlacesProof(proposition);
    }
  }

  private MachineProof getCompareWithDigitProof(MetamathProposition prop) {
    LessThanStatement statement = (LessThanStatement) prop.getAssrt();
    var c = statement.getOperand1();
    var operand2 = statement.getOperand2();
    Preconditions.checkArgument(c.number < 10);
    Preconditions.checkArgument(operand2.decimalFormat);

    var a = new NumberRepresentation(operand2.number / 10);
    var b = new NumberRepresentation(operand2.number % 10);

    var aNN = NNProposition(a);
    var bNN0 = NN0Proposition(b);
    var cNN0 = NN0Proposition(c);
    var cLessThanPrimitive10 = LessThanProposition(c, PRIMITIVE_10);

    var newGeneratedPropositions = List.of(aNN, bNN0, cNN0, cLessThanPrimitive10);
    Map<ResourcePointer, MetamathMachine> fromMachineArguments =
        createFromMachineArgumentsMap(newGeneratedPropositions);

    var decltiSubstitutions = Map.of("A", a.toString(), "B", b.toString(), "C", c.toString());
    var steps =
        List.of(
            ArgumentStep.fromEphemeralReference(aNN),
            ArgumentStep.fromEphemeralReference(bNN0),
            ArgumentStep.fromEphemeralReference(cNN0),
            ArgumentStep.fromEphemeralReference(cLessThanPrimitive10),
            ArgumentStep.fromSetMM(safeUse(DECLTI), List.of(0, 1, 2, 3), decltiSubstitutions));
    var newArgumentPtr = ResourcePointer.ephemeral(ARGUMENT, statement.getLabel());
    var newArg = new MetamathArgument(newArgumentPtr, LessThanProposition(statement), steps);
    return new MachineProof(
        newGeneratedPropositions, Map.of(prop.getResourcePtr(), newArg), fromMachineArguments);
  }

  private MachineProof getCompareWithPrimitive10Proof(MetamathProposition prop) {
    LessThanStatement statement = (LessThanStatement) prop.getAssrt();
    var operand1 = statement.getOperand1();
    var operand2 = statement.getOperand2();
    Preconditions.checkArgument(operand1.number == 10 && !operand1.decimalFormat);
    Preconditions.checkArgument(operand2.decimalFormat);

    var decimal10LessThanOperand2 = LessThanProposition(NumberRepresentation.DECIMAL_10, operand2);
    var newGeneratedPropositions = List.of(decimal10LessThanOperand2);
    Map<ResourcePointer, MetamathMachine> fromMachineArguments =
        createFromMachineArgumentsMap(newGeneratedPropositions);

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

    var steps =
        List.of(
            ArgumentStep.fromSetMM(safeUse(DEC10)),
            ArgumentStep.fromEphemeralReference(LessThanProposition(DECIMAL_10, operand2)),
            ArgumentStep.fromSetMM(safeUse(EQBRTRI), List.of(0, 1), eqbrtriSubstitutions));

    var newArgumentPtr = ResourcePointer.ephemeral(ARGUMENT, statement.getLabel());
    var newArg = new MetamathArgument(newArgumentPtr, LessThanProposition(statement), steps);
    return new MachineProof(
        newGeneratedPropositions, Map.of(prop.getResourcePtr(), newArg), fromMachineArguments);
  }

  private MachineProof getCompareWithUnequalHigherPlacesProof(MetamathProposition prop) {
    LessThanStatement statement = (LessThanStatement) prop.getAssrt();
    var operand1 = statement.getOperand1();
    var operand2 = statement.getOperand2();
    Preconditions.checkArgument(operand1.decimalFormat && operand2.decimalFormat);
    Preconditions.checkArgument(operand1.number / 10 != operand2.number / 10);

    var a = new NumberRepresentation(operand1.number / 10);
    var c = new NumberRepresentation(operand1.number % 10);
    var b = new NumberRepresentation(operand2.number / 10);
    var d = new NumberRepresentation(operand2.number % 10);
    var aNN0 = NN0Proposition(a);
    var bNN0 = NN0Proposition(b);
    var cNN0 = NN0Proposition(c);
    var dNN0 = NN0Proposition(d);
    var cLessThanPrimitive10 = LessThanProposition(c, PRIMITIVE_10);
    var aLessThanb = LessThanProposition(a, b);

    var newGeneratedPropositions =
        List.of(aNN0, bNN0, cNN0, dNN0, cLessThanPrimitive10, aLessThanb);
    Map<ResourcePointer, MetamathMachine> fromMachineArguments =
        createFromMachineArgumentsMap(newGeneratedPropositions);

    var decltcSubstitutions =
        Map.of("A", a.toString(), "B", b.toString(), "C", c.toString(), "D", d.toString());
    var steps =
        List.of(
            ArgumentStep.fromEphemeralReference(aNN0),
            ArgumentStep.fromEphemeralReference(bNN0),
            ArgumentStep.fromEphemeralReference(cNN0),
            ArgumentStep.fromEphemeralReference(dNN0),
            ArgumentStep.fromEphemeralReference(cLessThanPrimitive10),
            ArgumentStep.fromEphemeralReference(aLessThanb),
            ArgumentStep.fromSetMM(
                safeUse(DECLTC), List.of(0, 1, 2, 3, 4, 5), decltcSubstitutions));
    var newArgumentPtr = ResourcePointer.ephemeral(ARGUMENT, statement.getLabel());
    var newArg = new MetamathArgument(newArgumentPtr, LessThanProposition(statement), steps);
    return new MachineProof(
        newGeneratedPropositions, Map.of(prop.getResourcePtr(), newArg), fromMachineArguments);
  }

  private MachineProof getCompareWithEqualHigherPlacesProof(MetamathProposition prop) {
    LessThanStatement statement = (LessThanStatement) prop.getAssrt();
    var operand1 = statement.getOperand1();
    var operand2 = statement.getOperand2();
    Preconditions.checkArgument(operand1.decimalFormat && operand2.decimalFormat);
    Preconditions.checkArgument(operand1.number / 10 == operand2.number / 10);

    var a = new NumberRepresentation(operand1.number / 10); // same as operand2.number/10
    var b = new NumberRepresentation(operand1.number % 10);
    var c = new NumberRepresentation(operand2.number % 10);

    var aNN0 = NN0Proposition(a);
    var bNN0 = NN0Proposition(b);
    var cNN = NNProposition(c);
    var bLessThanc = LessThanProposition(b, c);

    var newGeneratedPropositions = List.of(aNN0, bNN0, cNN, bLessThanc);
    Map<ResourcePointer, MetamathMachine> fromMachineArguments =
        createFromMachineArgumentsMap(newGeneratedPropositions);

    var decltSubstitutions = Map.of("A", a.toString(), "B", b.toString(), "C", c.toString());
    var steps =
        List.of(
            ArgumentStep.fromEphemeralReference(aNN0),
            ArgumentStep.fromEphemeralReference(bNN0),
            ArgumentStep.fromEphemeralReference(cNN),
            ArgumentStep.fromEphemeralReference(bLessThanc),
            ArgumentStep.fromSetMM(safeUse(DECLT), List.of(0, 1, 2, 3), decltSubstitutions));
    var newArgumentPtr = ResourcePointer.ephemeral(ARGUMENT, statement.getLabel());
    var newArg = new MetamathArgument(newArgumentPtr, LessThanProposition(statement), steps);
    return new MachineProof(
        newGeneratedPropositions, Map.of(prop.getResourcePtr(), newArg), fromMachineArguments);
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

  private static Map<ResourcePointer, MetamathMachine> createFromMachineArgumentsMap(
      List<MetamathProposition> needsFurtherMachineProof) {
    Map<ResourcePointer, MetamathMachine> map = new HashMap<>();
    for (var proposition : needsFurtherMachineProof) {
      MetamathMachine machine;
      MetamathStatement assrt = proposition.getAssrt();
      if (assrt instanceof LessThanStatement) machine = LessThanMachine.getInstance();
      else if (assrt instanceof NN0Statement) machine = NN0Machine.getInstance();
      else if (assrt instanceof NNStatement) machine = NNMachine.getInstance();
      else throw new IllegalStateException("Don't know which machine to use.");
      map.put(proposition.getResourcePtr(), machine);
    }
    return map;
  }
}
