package org.sophize.metamath.formachines.machines;

import com.google.common.base.Preconditions;
import com.google.common.collect.Streams;
import mmj.lang.Assrt;
import mmj.lang.ParseNode;
import mmj.lang.Stmt;
import org.sophize.datamodel.Language;
import org.sophize.datamodel.Proposition;
import org.sophize.datamodel.ResourcePointer;
import org.sophize.metamath.formachines.*;

import javax.annotation.Nonnull;
import java.text.MessageFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static org.sophize.datamodel.ResourceType.ARGUMENT;
import static org.sophize.metamath.formachines.ArgumentStep.fromSetMM;
import static org.sophize.metamath.formachines.MachineUtils.*;

public class PrimeNumberMachine extends MetamathMachine {
  private static PrimeNumberMachine instance = new PrimeNumberMachine();

  private PrimeNumberMachine() {}

  public static PrimeNumberMachine getInstance() {
    return instance;
  }

  private static final long MAX_PRIME = 100000;
  private static final Set<Long> primes = getSortedPrimes();

  // For |- -. N || M
  private static final Assrt NDVDSI = Databases.getSetMMAssrt("ndvdsi");
  private static final Assrt DEC2DVDS = Databases.getSetMMAssrt("dec2dvds");
  private static final Assrt DEC5DVDS = Databases.getSetMMAssrt("dec5dvds");
  private static final Assrt DEC5DVDS2 = Databases.getSetMMAssrt("dec5dvds2");
  // For |- -. N e. Prime
  private static final Assrt NPRMI = Databases.getSetMMAssrt("nprmi");
  private static final Assrt DEC2NPRM = Databases.getSetMMAssrt("dec2nprm");
  private static final Assrt DEC5NPRM = Databases.getSetMMAssrt("dec5nprm");
  // For |- N e. Prime
  private static final Assrt PRMLEM0 = Databases.getSetMMAssrt("prmlem0");
  private static final Assrt PRMLEM1 = Databases.getSetMMAssrt("prmlem1");
  private static final Assrt PRMLEM1a = Databases.getSetMMAssrt("prmlem1a");

  // For Prime lemma.
  private static final Assrt ELUZELRE = Databases.getSetMMAssrt("eluzelre");
  private static final Assrt RESQCLD = Databases.getSetMMAssrt("resqcld");
  private static final Assrt ELUZLE = Databases.getSetMMAssrt("eluzle");
  private static final Assrt NN0REI = Databases.getSetMMAssrt("nn0rei");
  private static final Assrt NN0GE0I = Databases.getSetMMAssrt("nn0ge0i");
  private static final Assrt LE2SQ2 = Databases.getSetMMAssrt("le2sq2");
  private static final Assrt MPANL12 = Databases.getSetMMAssrt("mpanl12");
  private static final Assrt SYL2ANC = Databases.getSetMMAssrt("syl2anc");
  private static final Assrt NNREI = Databases.getSetMMAssrt("nnrei");
  private static final Assrt RESQCLI = Databases.getSetMMAssrt("resqcli");
  private static final Assrt NN0CNI = Databases.getSetMMAssrt("nn0cni");
  private static final Assrt SQVALI = Databases.getSetMMAssrt("sqvali");
  private static final Assrt EQTRI = Databases.getSetMMAssrt("eqtri");
  private static final Assrt BREQTRRI = Databases.getSetMMAssrt("breqtrri");
  private static final Assrt LTLETR = Databases.getSetMMAssrt("ltletr");
  private static final Assrt MPANI = Databases.getSetMMAssrt("mpani");
  private static final Assrt MP3AN12 = Databases.getSetMMAssrt("mp3an12");
  private static final Assrt SYLC = Databases.getSetMMAssrt("sylc");
  private static final Assrt LTNLE = Databases.getSetMMAssrt("ltnle");
  private static final Assrt SYLANCR = Databases.getSetMMAssrt("sylancr");
  private static final Assrt MPBID = Databases.getSetMMAssrt("mpbid");
  private static final Assrt PM2_21D = Databases.getSetMMAssrt("pm2.21d");
  private static final Assrt PM2_21I = Databases.getSetMMAssrt("pm2.21i");
  private static final Assrt A1I = Databases.getSetMMAssrt("a1i");
  private static final Assrt ADANTLD = Databases.getSetMMAssrt("adantld");
  private static final Assrt ADANTL = Databases.getSetMMAssrt("adantl");

  private static final List<Stmt> PREMISE_PROPOSITIONS =
      Stream.concat(
              Stream.of(
                  NDVDSI, DEC2DVDS, DEC5DVDS, DEC5DVDS2, DEC2NPRM, DEC5NPRM, PRMLEM0, NPRMI,
                  PRMLEM1, PRMLEM1a, ELUZELRE, RESQCLD, ELUZLE, NN0REI, NN0GE0I, LE2SQ2, MPANL12,
                  SYL2ANC, NNREI, RESQCLI, NN0CNI, SQVALI, EQTRI, BREQTRRI, LTLETR, MPANI, MP3AN12,
                  SYLC, LTNLE, SYLANCR, MPBID, PM2_21D, PM2_21I, A1I, ADANTLD, ADANTL),
              Stream.of(2, 3, 5, 7, 11, 13, 17, 19, 23).map(PrimeNumberMachine::getSmallPrimeStmt))
          .collect(toList());

  @Override
  public String getDescription() {
    return "This machine generates proofs of membership of elements in the set of prime numbers.\n"
        + "Eg. |- ; 2 3 e. Prime";
  }

  @Override
  public boolean isIndexable() {
    return true;
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
        NNLessThanMachine.getInstance(),
        NNSumProductExpressionMachine.getInstance());
  }

  @Override
  public String getDefaultStrictStatement() {
    return "|- ; 4 3 e. Prime";
  }

  @Override
  public String getDefaultLenientStatement() {
    return "20";
  }

  @Override
  public MetamathProposition parseLenient(@Nonnull Proposition proposition) {
    var number = NumberRepresentation.fromDigits(getDigitsLenient(proposition.getStatement()));
    if (number == null) return null;
    boolean isPrime = isPrime(number.number);
    String statement =
        MessageFormat.format("|-{0} {1} e. Prime", isPrime ? "" : " .-", number.toString());
    proposition.setLanguage(Language.METAMATH_SET_MM);
    proposition.setStatement(statement);
    return parseStrict(proposition);
  }

  @Override
  public String getNotProvableReason(MetamathProposition proposition) {
    ParseNode assrt = proposition.getAssrt();
    boolean notExpression = assrt.stmt.getLabel().equals("wn");
    if (notExpression) {
      assrt = assrt.child[0];
    }
    if (assrt.stmt.getLabel().equals("wbr")) {
      if (!notExpression) return DOESNT_HANDLE_REASON;
      if (!assrt.child[2].stmt.getLabel().equals("cdvds")) return DOESNT_HANDLE_REASON;
      var num1 = NumberRepresentation.fromParseNode(assrt.child[0]);
      var num2 = NumberRepresentation.fromParseNode(assrt.child[1]);
      if (num1 == null || num2 == null) return DOESNT_HANDLE_REASON;
      if (num2.number % num1.number == 0) return num1.number + " divides " + num2.number;
      return null;
    }
    if (!assrt.stmt.getLabel().equals("wcel")) return DOESNT_HANDLE_REASON;
    if (!assrt.child[1].stmt.getLabel().equals("cprime")) return DOESNT_HANDLE_REASON;

    var num = NumberRepresentation.fromParseNode(assrt.child[0]);
    if (num == null) return "This machine only tests for numbers";
    if (num.number > MAX_PRIME) return "This machine supports checking primes upto " + MAX_PRIME;
    var isPrime = isPrime(num.number);
    if (isPrime == notExpression) return "Provide number is " + (isPrime ? "" : "not ") + "prime";
    return null;
  }

  @Override
  public MachineProof getProof(MetamathProposition proposition) {
    if (!isProvable(proposition)) return null;
    ParseNode assrt = proposition.getAssrt();
    boolean notExpression = assrt.stmt.getLabel().equals("wn");
    if (notExpression) assrt = assrt.child[0];

    if (assrt.stmt.getLabel().equals("wbr")) {
      Preconditions.checkArgument(notExpression && assrt.child[2].stmt.getLabel().equals("cdvds"));
      var num1 = NumberRepresentation.fromParseNode(assrt.child[0]);
      var num2 = NumberRepresentation.fromParseNode(assrt.child[1]);
      return getNotDivisibleProof(proposition, num1, num2);
    }
    Preconditions.checkArgument(assrt.stmt.getLabel().equals("wcel"));
    Preconditions.checkArgument(assrt.child[1].stmt.getLabel().equals("cprime"));

    var num = NumberRepresentation.fromParseNode(assrt.child[0]);
    return notExpression ? getNotPrimeProof(proposition, num) : getPrimeProof(proposition, num);
  }

  private MachineProof getNotDivisibleProof(
      MetamathProposition proposition, NumberRepresentation num1, NumberRepresentation num2) {
    if (num2.number > 10) {
      var decHigh = new NumberRepresentation(num2.number / 10);
      var decLow = new NumberRepresentation(num2.number % 10);
      if (num1.number == 2) {
        var substitutions =
            Map.ofEntries(
                Map.entry("A", decHigh.toString()),
                Map.entry("B", new NumberRepresentation(decLow.number / 2).toString()),
                Map.entry("C", new NumberRepresentation(decLow.number - 1).toString()),
                Map.entry("D", decLow.toString()));

        return getProofForAssert(proposition, DEC2DVDS, substitutions);
      }
      if (num1.number == 5) {
        if (decLow.number < 5) {
          var substitutions = Map.of("A", decHigh.toString(), "B", decLow.toString());
          return getProofForAssert(proposition, DEC5DVDS, substitutions);
        } else {
          var b = new NumberRepresentation(decLow.number - 5);
          var substitutions =
              Map.of("A", decHigh.toString(), "B", b.toString(), "C", decLow.toString());
          return getProofForAssert(proposition, DEC5DVDS2, substitutions);
        }
      }
    }
    var substitutions =
        Map.ofEntries(
            Map.entry("A", num1.toString()),
            Map.entry("B", num2.toString()),
            Map.entry("Q", new NumberRepresentation(num2.number / num1.number).toString()),
            Map.entry("R", new NumberRepresentation(num2.number % num1.number).toString()));
    return getProofForAssert(proposition, NDVDSI, substitutions);
  }

  private MachineProof getNotPrimeProof(MetamathProposition proposition, NumberRepresentation num) {
    if ((num.number % 2 == 0 || num.number % 10 == 5) && num.number > 10) {
      var a = new NumberRepresentation(num.number / 10);
      if (num.number % 2 == 0) {
        var c = new NumberRepresentation(num.number % 10);
        var b = new NumberRepresentation((num.number % 10) / 2);
        return getProofForAssert(
            proposition, DEC2NPRM, Map.of("A", a.toString(), "B", b.toString(), "C", c.toString()));
      }
      return getProofForAssert(proposition, DEC5NPRM, Map.of("A", a.toString()));
    }

    NumberRepresentation a = null;
    NumberRepresentation b = null;
    for (long prime : primes) {
      if (num.number % prime == 0) {
        a = new NumberRepresentation(prime);
        b = new NumberRepresentation(num.number / prime);
        break;
      }
    }
    Preconditions.checkArgument(a != null);
    var substitutions = Map.of("A", a.toString(), "B", b.toString(), "N", num.toString());
    return getProofForAssert(proposition, NPRMI, substitutions);
  }

  private MachineProof getPrimeProof(MetamathProposition proposition, NumberRepresentation num) {
    if (num.number < 29) return new MachineProof(safeUse(getSmallPrimeStmt(num.number)));

    var primeNotRequiredToTest = nextPrime((long) Math.sqrt(num.number));
    var primeLemmaAndArgument = getPrimeLemmaAndArg(primeNotRequiredToTest);
    MetamathProposition primeLemma = primeLemmaAndArgument.getKey();
    MetamathArgument lemmaArgument = primeLemmaAndArgument.getValue();

    MetamathArgument mainArgument =
        getProofForLemma(proposition, primeLemma, Map.of("N", num.toString()));

    var machineArgs =
        Stream.concat(
                mainArgument.getMachineArguments().entrySet().stream(),
                lemmaArgument.getMachineArguments().entrySet().stream())
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (v1, v2) -> v1));
    var generatedProps =
        Streams.concat(
                Stream.of(primeLemma),
                mainArgument.getGeneratedPremises().stream(),
                lemmaArgument.getGeneratedPremises().stream())
            .collect(toList());
    var arguments =
        Map.of(
            proposition.getResourcePtr(), mainArgument, primeLemma.getResourcePtr(), lemmaArgument);
    return new MachineProof(generatedProps, arguments, machineArgs);
  }

  private Map.Entry<MetamathProposition, MetamathArgument> getPrimeLemmaAndArg(
      NumberRepresentation num) {
    String lemmaLabel = "prmlem." + (num.number * num.number);
    var numInNN0Prop = new MetamathProposition(parseSetMMStatement("|- " + num + " e. NN0"));

    var squareNum = new NumberRepresentation(num.number * num.number);
    var squareValStmt = MessageFormat.format("|- ( {0} x. {0} ) = {1}", num, squareNum);
    var squareValProp = new MetamathProposition(parseSetMMStatement(squareValStmt));

    var numStr = num.toString();
    var num2 = MessageFormat.format("( {0} ^ 2 )", num);
    var x2 = MessageFormat.format("( {0} ^ 2 )", "x");
    var x2InRR = MessageFormat.format("{0} e. RR", x2);
    var zNum = MessageFormat.format("( ZZ>= ` {0} )", num);
    var num2Ltx2 = num2 + " <_ " + x2;
    var xInZNum = "x e. " + zNum;

    var num2LeX2 = num2 + " <_ " + x2;
    var NltXPow2 = "N < " + x2;

    var x2LeN = MessageFormat.format("{0} <_ N", x2);
    var notX2LeN = "-. " + x2LeN;
    var Not2DvdN = "-. 2 || " + num;

    var a = MessageFormat.format("( x e. RR /\\ {0} <_ x )", num);
    var b = MessageFormat.format("( N e. RR /\\ {0} e. RR /\\ {1} e. RR )", num2, x2);
    var c = MessageFormat.format("( {0} <_ {1} -> N < {1} )", num2, x2);
    var d = MessageFormat.format("( N < {0} <-> -. {0} <_ N )", x2);
    var e = "x e. ( Prime \\ { 2 } )";
    var g = "-. x || N";
    var f = MessageFormat.format("( ( {0} /\\ {1} ) -> {2} )", e, x2LeN, g);

    var preSteps =
        new ArgumentStep[] {
          fromSetMM(safeUse(ELUZELRE), List.of(), Map.of("N", "x", "M", numStr)), // 0
          fromSetMM(safeUse(RESQCLD), List.of(0), Map.of("ph", xInZNum, "A", "x")), // 1
          fromSetMM(safeUse(ELUZLE), List.of(), Map.of("N", "x", "M", numStr)), // 2
          ArgumentStep.fromEphemeralReference(numInNN0Prop, NN0ClosureMachine.getInstance()), // 3
          fromSetMM(safeUse(NN0REI), List.of(3), Map.of("A", numStr)), // 4
          fromSetMM(safeUse(NN0GE0I), List.of(3), Map.of("N", numStr)), // 5
          fromSetMM(safeUse(LE2SQ2), List.of(), Map.of("A", numStr, "B", "x")), // 6
          fromSetMM(safeUse(MPANL12), List.of(4, 5, 6), Map.of("ch", a, "th", num2Ltx2)), // 7
          fromSetMM(safeUse(SYL2ANC), List.of(0, 2, 7), Map.of("ph", xInZNum, "th", num2Ltx2)), // 8
          ArgumentStep.fromSetMMHypothesis("|- N e. NN", lemmaLabel + ".n"), // 9
          fromSetMM(safeUse(NNREI), List.of(9), Map.of("A", "N")), // 10
          fromSetMM(safeUse(RESQCLI), List.of(4), Map.of("A", numStr)), // 11
          fromSetMM(safeUse(NN0CNI), List.of(3), Map.of("A", numStr)), // 12
          fromSetMM(safeUse(SQVALI), List.of(12), Map.of("A", numStr)), // 13
          ArgumentStep.fromEphemeralReference(
              squareValProp, NNSumProductExpressionMachine.getInstance()), // 14
          fromSetMM( // 15
              safeUse(EQTRI), List.of(13, 14), Map.of("A", num2, "C", squareNum.toString())),
          ArgumentStep.fromSetMMHypothesis( // 16
              "|- N < " + squareNum.toString(), lemmaLabel + ".lt"),
          fromSetMM( // 17
              safeUse(BREQTRRI), List.of(15, 16), Map.of("A", "N", "R", "<", "C", num2)),
          fromSetMM(safeUse(LTLETR), List.of(), Map.of("A", "N", "B", numStr, "C", x2)), // 18
          fromSetMM( // 19
              safeUse(MPANI), List.of(17, 18), Map.of("ph", b, "ch", num2LeX2, "th", NltXPow2)),
          fromSetMM(safeUse(MP3AN12), List.of(10, 11, 19), Map.of("ch", x2InRR, "th", c)), // 20
          fromSetMM(safeUse(SYLC), List.of(1, 8, 20), Map.of("ph", xInZNum, "th", NltXPow2)), // 21
          fromSetMM(safeUse(LTNLE), List.of(), Map.of("A", "N", "B", x2)), // 22
          fromSetMM(safeUse(SYLANCR), List.of(10, 1, 22), Map.of("ph", xInZNum, "th", d)), // 23
          fromSetMM(safeUse(MPBID), List.of(21, 23), Map.of("ph", xInZNum, "ch", notX2LeN)), // 24
          fromSetMM( // 25
              safeUse(PM2_21D), List.of(24), Map.of("ph", xInZNum, "ps", x2LeN, "ch", g)),
          fromSetMM( // 26
              safeUse(ADANTLD), List.of(25), Map.of("ph", xInZNum, "ps", x2LeN, "ch", g, "th", e)),
          fromSetMM(safeUse(ADANTL), List.of(26), Map.of("ph", xInZNum, "ps", f, "ch", Not2DvdN))
        };

    List<ArgumentStep> steps = new ArrayList<>(Arrays.asList(preSteps));
    for (long k = num.number - 2; k >= 5; k -= 2) {
      steps.addAll(prmlem0RecursiveSteps(k, steps.size(), lemmaLabel));
    }
    steps.add(ArgumentStep.fromSetMMHypothesis("|- 1 < N", lemmaLabel + ".gt"));
    steps.add(ArgumentStep.fromSetMMHypothesis("|- -. 2 || N", lemmaLabel + ".2"));
    steps.add(ArgumentStep.fromSetMMHypothesis("|- -. 3 || N", lemmaLabel + ".3"));
    var hypIndices =
        List.of(9, steps.size() - 3, steps.size() - 2, steps.size() - 1, steps.size() - 4);
    steps.add(fromSetMM(PRMLEM1a, hypIndices));

    MetamathProposition proposition = getLemmaAsMetamathProposition(num, lemmaLabel);
    var newArgumentPtr = ResourcePointer.ephemeral(ARGUMENT, proposition.getResourcePtr().getId());
    var dummyVariables = List.of(Databases.getSetMMVar("x"));
    var newArg = new MetamathArgument(newArgumentPtr, proposition, steps, dummyVariables);
    return Map.entry(proposition, newArg);
  }

  private static MetamathProposition getLemmaAsMetamathProposition(
      NumberRepresentation num, String lemmaLabel) {
    ParseNode assrt = MachineUtils.parseSetMMStatement("|- N e. Prime");
    var squareNum = new NumberRepresentation(num.number * num.number);
    var primeCheckHypStream =
        primes.stream()
            .takeWhile(number -> number < num.number)
            .map(prime -> MessageFormat.format("|- -. {0} || N", new NumberRepresentation(prime)));
    var otherHypStream = Stream.of("|- N e. NN", "|- N < " + squareNum, "|- 1 < N");
    List<ParseNode> hypList =
        Stream.concat(otherHypStream, primeCheckHypStream)
            .map(MachineUtils::parseSetMMStatement)
            .collect(toList());

    var primeCheckLabels =
        primes.stream().takeWhile(n -> n < num.number).map(n -> lemmaLabel + "." + n);
    var otherLabels = Stream.of(lemmaLabel + ".n", lemmaLabel + ".lt", lemmaLabel + ".gt");
    List<String> hypLabels = Stream.concat(otherLabels, primeCheckLabels).collect(toList());
    return new MetamathProposition(assrt, hypList, lemmaLabel, hypLabels, List.of());
  }

  private List<ArgumentStep> prmlem0RecursiveSteps(long k, int stepOffset, String lemmaLabel) {
    var M = new NumberRepresentation(k + 2);
    var K = new NumberRepresentation(k);
    String kInPrime = K + " e. Prime";
    String notKDvdsN = "-. " + K + " || N";

    var notKInPrime = new MetamathProposition(parseSetMMStatement("|- -. " + kInPrime));

    var KP2EqM = new MetamathProposition(parseSetMMStatement("|- ( " + K + " + 2 ) = " + M));
    var KP2EqMStep = ArgumentStep.fromEphemeralReference(KP2EqM, NNSumMachine.getInstance());

    var finalStepHypIndices = List.of(stepOffset - 1, stepOffset + 1, stepOffset + 2);
    var finalStep =
        fromSetMM(PRMLEM0, finalStepHypIndices, Map.of("M", M.toString(), "K", K.toString()));

    ArgumentStep step1, step2;
    if (isPrime(k)) {
      step1 = ArgumentStep.fromSetMMHypothesis("|- " + notKDvdsN, lemmaLabel + "." + k);
      step2 = fromSetMM(A1I, List.of(stepOffset), Map.of("ph", notKDvdsN, "ps", kInPrime));
    } else {
      step1 = ArgumentStep.fromEphemeralReference(notKInPrime, PrimeNumberMachine.getInstance());
      step2 = fromSetMM(PM2_21I, List.of(stepOffset), Map.of("ph", kInPrime, "ps", notKDvdsN));
    }
    return List.of(step1, step2, KP2EqMStep, finalStep);
  }

  private MachineProof getProofForAssert(
      MetamathProposition proposition, Assrt assrt, Map<String, String> substitutions) {
    var stepFactory =
        StepFactory.forArgumentWithGeneratedPremises(assrt, substitutions, this::machineDeterminer);
    return getProofForAssrt(proposition, stepFactory, (Assrt) safeUse(assrt), substitutions);
  }

  private MetamathArgument getProofForLemma(
      MetamathProposition proposition,
      MetamathProposition lemma,
      Map<String, String> substitutions) {
    var stepFactory =
        StepFactory.forArgumentWithGeneratedPremises(lemma, substitutions, this::machineDeterminer);
    return getArgumentForGeneratedLemma(proposition, stepFactory, lemma, substitutions);
  }

  private MetamathMachine machineDeterminer(ParseNode node) {
    if (node.stmt.getLabel().equals("wcel")) {
      String setLabel = node.child[1].stmt.getLabel();
      if (setLabel.equals("cn0")) return safeUse(NN0ClosureMachine.getInstance());
      if (setLabel.equals("cn")) return safeUse(NNClosureMachine.getInstance());
      if (setLabel.equals("cc")) return safeUse(CCClosureMachine.getInstance());
    }
    if (node.stmt.getLabel().equals("wceq")) {
      return safeUse(NNSumProductExpressionMachine.getInstance());
    }
    if (node.stmt.getLabel().equals("wbr")) {
      String relationLabel = node.child[2].stmt.getLabel();
      if (relationLabel.equals("clt")) return safeUse(NNLessThanMachine.getInstance());
    }
    if (node.stmt.getLabel().equals("wn")) {
      if (node.child[0].stmt.getLabel().equals("wbr")) {
        if (node.child[0].child[2].stmt.getLabel().equals("cdvds"))
          return safeUse(PrimeNumberMachine.getInstance());
      }
    }
    return null;
  }

  private NumberRepresentation nextPrime(long number) {
    for (long prime : primes) {
      if (prime > number) return new NumberRepresentation(prime);
    }
    throw new IllegalStateException("Provided number is too big.");
  }

  private boolean isPrime(long number) {
    Preconditions.checkArgument(number <= MAX_PRIME);
    return primes.contains(number);
  }

  private static TreeSet<Long> getSortedPrimes() {
    var primes = new TreeSet<Long>();
    for (long i = 2; i < MAX_PRIME; i++) {
      long limit = (long) Math.sqrt(i);
      boolean isPrime = true;
      for (long factor : primes) {
        if (factor > limit) break;
        if (i % factor == 0) {
          isPrime = false;
          break;
        }
      }
      if (isPrime) primes.add(i);
    }
    return primes;
  }

  private static Stmt getSmallPrimeStmt(long smallPrime) {
    return Databases.getSetMMAssrt(smallPrime + "prm");
  }
}
