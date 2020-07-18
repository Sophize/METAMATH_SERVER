package org.sophize.metamath.server;

import com.google.common.base.Strings;
import mmj.util.BatchMMJ2;
import org.sophize.datamodel.ProofRequest;
import org.sophize.datamodel.ProofResponse;
import org.sophize.datamodel.TruthValue;
import org.sophize.metamath.Utils;
import org.sophize.metamath.server.machines.MetamathMachine;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import static org.sophize.datamodel.TruthValue.TRUE;
import static org.sophize.datamodel.TruthValue.UNKNOWN;
import static org.sophize.metamath.Utils.isTrue;

@SpringBootApplication
@RestController
public class SpringbootApplication {

  private static final String RUN_PARMS_FILE = "Resource:/RunParmsServer.txt";

  public static void main(String[] args) {
    SpringApplication.run(SpringbootApplication.class, args);
  }

  @PostConstruct
  public void initStatic() {
    BatchMMJ2 batchMMJ2 = new BatchMMJ2();
    batchMMJ2.generateSvcCallback(new String[] {RUN_PARMS_FILE}, Databases::addGrammar);
  }

  @GetMapping("/")
  public String hello() {
    return "Hello to the world from METAMATH_SERVER!";
  }

  @PostMapping("/proof_request")
  public ProofResponse ProofRequest(@RequestBody ProofRequest request) {
    MachineId machineId = MachineId.fromValue(request.getMachinePtr());
    if (machineId == null) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Machine not managed by this server");
    }
    return getResponseFromMachine(machineId.getMachine(), request);
  }

  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  @ExceptionHandler(Exception.class)
  public String handleError(HttpServletRequest req, Exception ex) {
    System.out.println("Request: " + req.getRequestURL() + " raised " + ex);
    return ex.getMessage();
  }

  private static ProofResponse getResponseFromMachine(MetamathMachine m, ProofRequest request) {
    if (request == null
        || request.getProposition() == null
        || Strings.isNullOrEmpty(request.getProposition().toString())) {
      return MachineUtils.responseWithMessage(UNKNOWN, "Proposition provided is empty");
    }
    MetamathProposition parsed = m.parseStrict(request.getProposition());
    if (parsed == null && Utils.isTrue(request.getParseLenient())) {
      parsed = m.parseLenient(request.getProposition());
    }
    if (parsed == null) {
      String reason = "The provided expression doesn't parse.";
      if (Utils.isTrue(request.getParseLenient())) reason = "Couldn't understand the input.";
      return MachineUtils.responseWithMessage(UNKNOWN, reason);
    }
    String notProvableJustification = m.getNotProvableReason(parsed);
    if (notProvableJustification != null) {
      return MachineUtils.responseWithMessage(UNKNOWN, notProvableJustification);
    }
    if (!isTrue(request.getFetchProof()) && !isTrue(request.getParseLenient())) {
      return MachineUtils.responseWithMessage(TRUE, "");
    }

    MachineProof proof = m.getProof(parsed);
    proof = new ProofCollapser(ProofExpander.expand(proof, parsed)).collapse(parsed);

    var existingPropositionPtr = proof.getExistingPropositionPtr();
    ProofResponse response = new ProofResponse();
    response.setTruthValue(TruthValue.TRUE);
    if (existingPropositionPtr != null) {
      response.setExistingPropositionPtr(existingPropositionPtr.toString());
      return response;
    }
    response.setResolvedProposition(parsed.toProposition());
    if (!isTrue(request.getFetchProof())) return response;

    response.setProofPropositions(proof.getPropositions());
    response.setProofArguments(proof.getArguments());
    return response;
  }
}
