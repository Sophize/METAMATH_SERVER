package org.sophize.metamath.server;

import com.google.common.base.Preconditions;
import mmj.lang.*;
import mmj.pa.ProofAsst;
import mmj.pa.ProofAsstPreferences;
import mmj.tl.TheoremLoader;
import mmj.tl.TlPreferences;
import mmj.util.OutputBoss;
import mmj.verify.Grammar;
import mmj.verify.VerifyProofs;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Databases {
  public static final String SET_DB = "set.mm";
  public static final String ISET_DB = "iset.mm";
  public static final String NF_DB = "nf.mm";

  private static Map<String, Grammar> grammars = new HashMap<>();

  public static void addGrammar(
      Messages messages,
      OutputBoss outputBoss,
      LogicalSystem logicalSystem,
      VerifyProofs verifyProofs,
      Grammar grammar,
      WorkVarManager workVarManager,
      ProofAsstPreferences proofAsstPreferences,
      ProofAsst proofAsst,
      TlPreferences tlPreferences,
      TheoremLoader theoremLoader,
      File svcFolder,
      Map<String, String> svcArgs) {
    String databaseName = svcArgs.get("databaseName");
    if (!List.of(SET_DB, ISET_DB, NF_DB).contains(databaseName)) {
      throw new IllegalStateException("Unknown database: " + databaseName);
    }
    grammars.put(databaseName, grammar);
  }

  public static Grammar getGrammar(String name) {
    return grammars.get(name);
  }

  public static Stmt getStmt(String dbName, String label) {
    return grammars.get(dbName).stmtTbl.get(label);
  }

  public static Stmt getSetMMStmt(String label) {
    Stmt stmt = grammars.get(SET_DB).stmtTbl.get(label);
    Preconditions.checkArgument(stmt != null);
    return stmt;
  }

  public static Var getSetMMVar(String varName) {
    Sym sym = grammars.get(SET_DB).symTbl.get(varName);
    Preconditions.checkArgument(sym instanceof Var);
    return (Var) sym;
  }

  public static Assrt getSetMMAssrt(String label) {
    var stmt = getSetMMStmt(label);
    Preconditions.checkArgument(stmt instanceof Assrt);
    return (Assrt) stmt;
  }
}
