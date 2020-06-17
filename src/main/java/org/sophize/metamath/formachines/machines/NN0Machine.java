package org.sophize.metamath.formachines.machines;

import com.google.common.base.Preconditions;
import mmj.lang.Stmt;
import org.sophize.datamodel.Language;
import org.sophize.datamodel.Proposition;
import org.sophize.datamodel.ResourcePointer;
import org.sophize.metamath.formachines.*;
import org.sophize.metamath.formachines.MetamathProposition;
import org.sophize.metamath.formachines.propositions.NN0Statement;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.sophize.datamodel.ResourceType.ARGUMENT;
import static org.sophize.metamath.formachines.MachineUtils.getDigitsLenient;
import static org.sophize.metamath.formachines.PropositionFactory.NN0Proposition;

public class NN0Machine extends MetamathMachine {
  private static NN0Machine instance = new NN0Machine();

  public static NN0Machine getInstance() {
    return instance;
  }

  private static Stmt CLOSURE = Databases.getSetMMStmt("deccl");

  private static List<Stmt> PREMISE_PROPOSITIONS =
      Stream.concat(
              Stream.of(CLOSURE),
              IntStream.range(0, 11).mapToObj(NN0Machine::primitiveStatementPtr))
          .collect(Collectors.toList());

  public String getDescription() {
    return "To get proofs for statements like |- ; 2 0 e. NN0";
  }

  public boolean isIndexable() {
    return true;
  }

  public ResourcePointer getPermanentPtr() {
    return MachineId.NN0.getPermanentPtr();
  }

  public List<Stmt> getPremisePropositions() {
    return PREMISE_PROPOSITIONS;
  }

  public List<ResourcePointer> getPremiseMachines() {
    return List.of();
  }

  public Language getDefaultLanguage() {
    return Language.METAMATH_SET_MM;
  }

  public String getDefaultStrictStatement() {
    return "|- ; 2 0 e. NN0";
  }

  public String getDefaultLenientStatement() {
    return "20";
  }

  public MetamathProposition parseStrict(@Nonnull Proposition proposition) {
    return parseNumBelongsToSetStatement(proposition, "NN0");
  }

  public MetamathProposition parseLenient(@Nonnull Proposition proposition) {
    try {
      List<Integer> digits = getDigitsLenient(proposition.getStatement());
      if (digits == null) return null;
      return NN0Proposition(NumberRepresentation.fromDigits(digits));
    } catch (Exception e) {
      return null;
    }
  }

  public String getNotProvableReason(MetamathProposition proposition) {
    if (!(proposition.getAssrt() instanceof NN0Statement))
      return "This machine doesn't prove such statements";
    var numeral = ((NN0Statement) proposition.getAssrt()).getNumeral();
    if (numeral.number < 0) return "Provided number is negative";
    if (numeral.leadingZeros != 0) return "Machine doesn't handle leading zeros yet.";
    return null;
  }

  public MachineProof getProof(MetamathProposition proposition) {
    if (getNotProvableReason(proposition) != null) return null;
    var nn0Statement = (NN0Statement) proposition.getAssrt();
    var numeral = nn0Statement.getNumeral();

    if (numeral.number <= 10 && !numeral.decimalFormat) {
      return new MachineProof(safeUse(primitiveStatementPtr(numeral.number)));
    }
    List<Long> digits = numeral.getDigits();
    Set<Long> uniqueDigits = new TreeSet<>(digits);
    var numberToStepIndex = new HashMap<Long, Integer>();
    var steps = new ArrayList<ArgumentStep>();
    for (Long digit : uniqueDigits) {
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
      var hypIndices = List.of(previousNumberIndex, numberToStepIndex.get(currDigit));
      ArgumentStep step = ArgumentStep.fromSetMM(safeUse(CLOSURE), hypIndices, substitutions);
      steps.add(step);
    }
    var argPtr = ResourcePointer.ephemeral(ARGUMENT, proposition.getAssrt().getLabel());
    var arg = new MetamathArgument(argPtr, proposition, steps);
    return new MachineProof(List.of(), Map.of(proposition.getResourcePtr(), arg), Map.of());
  }

  static MetamathProposition parseNumBelongsToSetStatement(
      @Nonnull Proposition proposition, String setName) {
    if (proposition.getLanguage() != Language.METAMATH_SET_MM) return null;
    String statement = proposition.getStatement();
    String[] tokens = MachineUtils.tokenizeString(statement);
    tokens = MachineUtils.stripMarker(tokens);
    if (tokens == null || tokens.length < 3) return null;
    if (!tokens[tokens.length - 1].equals(setName) || tokens[tokens.length - 2].equals("e.")) {
      return null;
    }
    var number = NumberRepresentation.fromTokens(Arrays.copyOf(tokens, tokens.length - 1));

    if (setName.equals("NN0")) return NN0Proposition(number);

    throw new IllegalStateException("Shouldn't happen");
  }

  private static Stmt primitiveStatementPtr(long number) {
    Preconditions.checkArgument(number >= 0 && number <= 10);
    return Databases.getSetMMStmt(number + "nn0");
  }

  private NN0Machine() {}
}
