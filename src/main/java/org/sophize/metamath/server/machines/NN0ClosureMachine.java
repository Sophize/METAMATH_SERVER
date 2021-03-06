package org.sophize.metamath.server.machines;

import com.google.common.base.Preconditions;
import mmj.lang.ParseNode;
import mmj.lang.Stmt;
import org.sophize.datamodel.Language;
import org.sophize.datamodel.Proposition;
import org.sophize.datamodel.ResourcePointer;
import org.sophize.metamath.server.*;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.sophize.datamodel.ResourceType.ARGUMENT;
import static org.sophize.metamath.server.MachineUtils.getDigitsLenient;

public class NN0ClosureMachine extends MetamathMachine {
  private static NN0ClosureMachine instance = new NN0ClosureMachine();

  public static NN0ClosureMachine getInstance() {
    return instance;
  }

  private static Stmt CLOSURE = Databases.getSetMMStmt("deccl");

  private static List<Stmt> PREMISE_PROPOSITIONS =
      Stream.concat(
              Stream.of(CLOSURE),
              IntStream.range(0, 11).mapToObj(NN0ClosureMachine::primitiveStatementPtr))
          .collect(Collectors.toList());

  @Override
  public String getDescription() {
    return "This machine generates proofs of membership in the set of natural numbers (NN0). "
        + "Eg. |- ; 2 0 e. NN0";
  }

  @Override
  public List<Stmt> getPremisePropositions() {
    return PREMISE_PROPOSITIONS;
  }

  @Override
  public List<MetamathMachine> getPremiseMachines() {
    return List.of();
  }

  @Override
  public String getDefaultStrictStatement() {
    return "|- ; 2 0 e. NN0";
  }

  @Override
  public String getDefaultLenientStatement() {
    return "20";
  }

  @Override
  public MetamathProposition parseLenient(@Nonnull Proposition proposition) {
    var number = NumberRepresentation.fromDigits(getDigitsLenient(proposition.getStatement()));
    if (number == null) return null;
    proposition.setLanguage(Language.METAMATH_SET_MM);
    proposition.setStatement("|- " + number + " e. NN0");
    return parseStrict(proposition);
  }

  @Override
  public String getNotProvableReason(MetamathProposition proposition) {
    ParseNode assrt = proposition.getAssrt();
    if (!assrt.stmt.getLabel().equals("wcel")) return DOESNT_HANDLE_REASON;
    if (assrt.child.length != 2) return DOESNT_HANDLE_REASON;

    if (!assrt.child[1].stmt.getLabel().equals("cn0")) return DOESNT_HANDLE_REASON;
    var numeral = NumberRepresentation.fromParseNode(assrt.child[0]);
    if (numeral == null) return DOESNT_HANDLE_REASON;

    if (numeral.number < 0) return "Provided number is negative";
    if (numeral.leadingZeros != 0) return "Machine doesn't handle leading zeros yet.";
    return null;
  }

  @Override
  public MachineProof getProof(MetamathProposition proposition) {
    if (!isProvable(proposition)) return null;
    ParseNode assrt = proposition.getAssrt();
    var numeral = NumberRepresentation.fromParseNode(assrt.child[0]);

    if (numeral.number <= 10 && !numeral.decimalFormat) {
      return new MachineProof(safeUse(primitiveStatementPtr(numeral.number)));
    }

    // This code directly generates all steps required for the proof - without needing the
    // expander and collapser. It is more inconvenient to create but we leave it here to be used
    // as a reference.

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
    var argPtr = ResourcePointer.ephemeral(ARGUMENT, proposition.getResourcePtr().getId());
    var arg = new MetamathArgument(argPtr, proposition, steps);
    return new MachineProof(List.of(), Map.of(proposition.getResourcePtr(), arg), Map.of());
  }

  private static Stmt primitiveStatementPtr(long number) {
    Preconditions.checkArgument(number >= 0 && number <= 10);
    return Databases.getSetMMStmt(number + "nn0");
  }

  private NN0ClosureMachine() {}
}
