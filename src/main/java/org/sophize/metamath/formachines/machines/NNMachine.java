package org.sophize.metamath.formachines.machines;

import com.google.common.base.Preconditions;
import mmj.lang.Stmt;
import org.sophize.datamodel.Language;
import org.sophize.datamodel.Proposition;
import org.sophize.datamodel.ResourcePointer;
import org.sophize.metamath.formachines.*;
import org.sophize.metamath.formachines.propositions.NNStatement;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.sophize.datamodel.ResourceType.ARGUMENT;
import static org.sophize.metamath.formachines.MachineUtils.getDigitsLenient;
import static org.sophize.metamath.formachines.PropositionFactory.NNProposition;

public class NNMachine extends MetamathMachine {
  private static NNMachine instance = new NNMachine();

  public static NNMachine getInstance() {
    return instance;
  }

  private final Stmt CLOSURE1 = Databases.getSetMMStmt("decnncl");
  private final Stmt CLOSURE2 = Databases.getSetMMStmt("decnncl2");

  private final List<Stmt> PREMISE_PROPOSITIONS =
      Stream.concat(
              Stream.of(CLOSURE1, CLOSURE2),
              IntStream.range(1, 11).mapToObj(NNMachine::primitiveStatementPtr))
          .collect(Collectors.toList());

  public String getDescription() {
    return "To get proofs for statements like |- ; 2 1 e. NN";
  }

  public boolean isIndexable() {
    return true;
  }

  public Language getDefaultLanguage() {
    return Language.METAMATH_SET_MM;
  }

  public String getDefaultStrictStatement() {
    return "|- ; 2 1 e. NN";
  }

  public String getDefaultLenientStatement() {
    return "21";
  }

  public List<Stmt> getPremisePropositions() {
    return PREMISE_PROPOSITIONS;
  }

  public List<ResourcePointer> getPremiseMachines() {
    return List.of();
  }

  public MetamathProposition parseStrict(@Nonnull Proposition proposition) {
    return NN0Machine.parseNumBelongsToSetStatement(proposition, "NN");
  }

  public MetamathProposition parseLenient(@Nonnull Proposition proposition) {
    try {
      List<Integer> digits = getDigitsLenient(proposition.getStatement());
      if (digits == null) return null;
      return NNProposition(NumberRepresentation.fromDigits(digits));
    } catch (Exception e) {
      return null;
    }
  }

  public String getNotProvableReason(MetamathProposition proposition) {
    if (!(proposition.getAssrt() instanceof NNStatement))
      return "This machine doesn't prove such statements";
    var numeral = ((NNStatement) proposition.getAssrt()).getNumeral();
    if (numeral.number <= 0) return "Provided number is less than 1";
    if (numeral.leadingZeros != 0) return "Machine doesn't handle leading zeros yet.";
    return null;
  }

  public MachineProof getProof(MetamathProposition proposition) {
    if (getNotProvableReason(proposition) != null) return null;
    var nnStatement = (NNStatement) proposition.getAssrt();
    var numeral = nnStatement.getNumeral();

    if (numeral.number <= 10 && !numeral.decimalFormat) {
      return new MachineProof(safeUse(primitiveStatementPtr(numeral.number)));
    }
    List<Long> digits = numeral.getDigits();
    Set<Long> uniqueDigits = new TreeSet<>(digits);
    var numberToStepIndex = new HashMap<Long, Integer>();
    var steps = new ArrayList<ArgumentStep>();
    for (Long digit : uniqueDigits) {
      if (digit == 0) continue;
      steps.add(ArgumentStep.fromSetMM(safeUse(primitiveStatementPtr(digit))));
      numberToStepIndex.put(digit, steps.size() - 1);
    }
    Long currentNumber = digits.get(0);
    for (int i = 1; i < digits.size(); i++) {
      var prevNumberRep = new NumberRepresentation(currentNumber, currentNumber >= 10);
      long currDigit = digits.get(i);
      currentNumber = currentNumber * 10 + currDigit;

      int previousNumberIndex =
          (i == 1) ? numberToStepIndex.get(digits.get(0)) : (steps.size() - 1);

      var substitutions = Map.of("A", prevNumberRep.toString(), "B", Long.toString(currDigit));
      ArgumentStep step;
      if (currDigit == 0) {
        step =
            ArgumentStep.fromSetMM(safeUse(CLOSURE2), List.of(previousNumberIndex), substitutions);
      } else {
        var hypIndices = List.of(previousNumberIndex, numberToStepIndex.get(currDigit));
        step = ArgumentStep.fromSetMM(safeUse(CLOSURE1), hypIndices, substitutions);
      }
      steps.add(step);
    }
    var argPtr = ResourcePointer.ephemeral(ARGUMENT, proposition.getAssrt().getLabel());
    var arg = new MetamathArgument(argPtr, proposition, steps);
    return new MachineProof(List.of(), Map.of(proposition.getResourcePtr(), arg), Map.of());
  }

  private static Stmt primitiveStatementPtr(long number) {
    Preconditions.checkArgument(number > 0 && number <= 10);
    return Databases.getSetMMStmt(number + "nn");
  }
}
