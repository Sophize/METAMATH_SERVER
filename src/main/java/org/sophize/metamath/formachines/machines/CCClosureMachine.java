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
import java.util.stream.LongStream;
import java.util.stream.Stream;

import static org.sophize.metamath.formachines.MachineUtils.getDigitsLenient;
import static org.sophize.metamath.formachines.MachineUtils.getProofForSetMMAssrt;

public class CCClosureMachine extends MetamathMachine {
  private static CCClosureMachine instance = new CCClosureMachine();

  private CCClosureMachine() {}

  public static CCClosureMachine getInstance() {
    return instance;
  }

  private static Assrt NN0CNI = Databases.getSetMMAssrt("nn0cni");

  private static List<Stmt> PREMISE_PROPOSITIONS =
      Stream.concat(
              Stream.of(NN0CNI),
              LongStream.rangeClosed(0, 9).mapToObj(CCClosureMachine::primitiveStatementPtr))
          .collect(Collectors.toList());

  @Override
  public String getDescription() {
    return "This machine generates proofs of membership in the set of complex numbers. "
        + "Eg. |- ; 2 1 e. NN\n\n"
        + "*Currently, this machine only works positive integers*";
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
  public String getDefaultStrictStatement() {
    return "|- ; 2 0 e. CC";
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
    proposition.setStatement("|- " + number + " e. CC");
    return parseStrict(proposition);
  }

  @Override
  public String getNotProvableReason(MetamathProposition proposition) {
    ParseNode assrt = proposition.getAssrt();
    if (!assrt.stmt.getLabel().equals("wcel")) return DOESNT_HANDLE_REASON;
    if (assrt.child.length != 2) return DOESNT_HANDLE_REASON;

    if (!assrt.child[1].stmt.getLabel().equals("cc")) return DOESNT_HANDLE_REASON;
    var numeral = NumberRepresentation.fromParseNode(assrt.child[0]);
    if (numeral == null) return DOESNT_HANDLE_REASON;

    if (numeral.number < 0) return "This machine only works for natural numbers";
    return null;
  }

  @Override
  public MachineProof getProof(MetamathProposition prop) {
    if (!isProvable(prop)) return null;
    var assrt = prop.getAssrt();
    var numeral = ParseNodeHelpers.getValue(assrt.child[0]);

    if (numeral.number <= 9 && numeral.number >= 0)
      return new MachineProof(safeUse(primitiveStatementPtr(numeral.number)));

    var substitutions = Map.of("A", numeral.toString());
    var stepFactory =
        StepFactory.forArgumentWithGeneratedPremises(
            NN0CNI, substitutions, unused -> safeUse(NN0ClosureMachine.getInstance()));
    return getProofForSetMMAssrt(prop, stepFactory, NN0CNI, substitutions);
  }

  private static Assrt primitiveStatementPtr(long number) {
    Preconditions.checkArgument(number >= 0 && number < 10);
    if (number == 1) return Databases.getSetMMAssrt("ax-1cn");
    return Databases.getSetMMAssrt(number + "cn");
  }
}
