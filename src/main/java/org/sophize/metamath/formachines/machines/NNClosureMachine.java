package org.sophize.metamath.formachines.machines;

import com.google.common.base.Preconditions;
import mmj.lang.Assrt;
import mmj.lang.ParseNode;
import mmj.lang.Stmt;
import org.sophize.datamodel.Language;
import org.sophize.datamodel.Proposition;
import org.sophize.metamath.formachines.*;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.sophize.metamath.formachines.MachineUtils.getDigitsLenient;
import static org.sophize.metamath.formachines.MachineUtils.getProofForSetMMAssrt;

public class NNClosureMachine extends MetamathMachine {
  private static NNClosureMachine instance = new NNClosureMachine();

  public static NNClosureMachine getInstance() {
    return instance;
  }

  private final Assrt CLOSURE1 = Databases.getSetMMAssrt("decnncl");
  private final Assrt CLOSURE2 = Databases.getSetMMAssrt("decnncl2");

  private final List<Stmt> PREMISE_PROPOSITIONS =
      Stream.concat(
              Stream.of(CLOSURE1, CLOSURE2),
              IntStream.range(1, 11).mapToObj(NNClosureMachine::primitiveStatementPtr))
          .collect(Collectors.toList());

  @Override
  public String getDescription() {
    return "This machine generates proofs of membership in the set of natural numbers (NN).\n"
        + "Eg. |- ; 2 1 e. NN";
  }

  @Override
  public String getDefaultStrictStatement() {
    return "|- ; 2 1 e. NN";
  }

  @Override
  public String getDefaultLenientStatement() {
    return "21";
  }

  @Override
  public List<Stmt> getPremisePropositions() {
    return PREMISE_PROPOSITIONS;
  }

  @Override
  public List<MetamathMachine> getPremiseMachines() {
    return List.of(NN0ClosureMachine.getInstance());
  }

  @Override
  public boolean isIndexable() {
    return true;
  }

  @Override
  public MetamathProposition parseLenient(@Nonnull Proposition proposition) {
    var number = NumberRepresentation.fromDigits(getDigitsLenient(proposition.getStatement()));
    if (number == null) return null;
    proposition.setLanguage(Language.METAMATH_SET_MM);
    proposition.setStatement("|- " + number + " e. NN");
    return parseStrict(proposition);
  }

  @Override
  public String getNotProvableReason(MetamathProposition proposition) {
    ParseNode assrt = proposition.getAssrt();
    if (!assrt.stmt.getLabel().equals("wcel")) return DOESNT_HANDLE_REASON;
    if (assrt.child.length != 2) return DOESNT_HANDLE_REASON;

    if (!assrt.child[1].stmt.getLabel().equals("cn")) return DOESNT_HANDLE_REASON;
    var numeral = NumberRepresentation.fromParseNode(assrt.child[0]);
    if (numeral == null) return DOESNT_HANDLE_REASON;

    if (numeral.number <= 0) return "Provided number is not a natural number";
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
    Map<String, String> substitutions;
    var higherPart = new NumberRepresentation(numeral.number / 10);
    Assrt dbAssrt;
    if (numeral.number % 10 == 0) {
      substitutions = Map.of("A", higherPart.toString());
      dbAssrt = CLOSURE2;
    } else {
      var lowDigit = new NumberRepresentation(numeral.number % 10);
      substitutions = Map.of("A", higherPart.toString(), "B", lowDigit.toString());
      dbAssrt = CLOSURE1;
    }

    var stepFactory =
        StepFactory.forArgumentWithGeneratedPremises(
            dbAssrt, substitutions, this::machineDeterminer);
    return getProofForSetMMAssrt(proposition, stepFactory, (Assrt) safeUse(dbAssrt), substitutions);
  }

  private MetamathMachine machineDeterminer(ParseNode node) {
    if (node.stmt.getLabel().equals("wcel")) {
      var setLabel = node.child[1].stmt.getLabel();
      if (setLabel.equals("cn0")) return safeUse(NN0ClosureMachine.getInstance());
      if (setLabel.equals("cn")) return safeUse(NNClosureMachine.getInstance());
    }
    return null;
  }

  private static Stmt primitiveStatementPtr(long number) {
    Preconditions.checkArgument(number > 0 && number <= 10);
    return Databases.getSetMMStmt(number + "nn");
  }
}
