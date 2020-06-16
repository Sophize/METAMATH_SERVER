package org.sophize.metamath.formachines;

import mmj.util.BatchMMJ2;
import org.sophize.datamodel.MachineRequest;
import org.sophize.datamodel.MachineResponse;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

@SpringBootApplication
@RestController
public class SpringbootApplication {

  private static final String RUN_PARMS_FILE = "run_parms/RunParmsServer.txt";

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
    return "Hello world from METAMATH_SERVER!";
  }

  @PostMapping("/machine_request")
  public MachineResponse machineRequest(@RequestBody MachineRequest request) {
    MachineId machineId = MachineId.fromValue(request.getMachinePtr());
    if (machineId == null) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Machine not managed by this server");
    }
    // TODO: Implement.
    return new MachineResponse();
  }

  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  @ExceptionHandler(Exception.class)
  public String handleError(HttpServletRequest req, Exception ex) {
    System.out.println("Request: " + req.getRequestURL() + " raised " + ex);
    return ex.getMessage();
  }
}
