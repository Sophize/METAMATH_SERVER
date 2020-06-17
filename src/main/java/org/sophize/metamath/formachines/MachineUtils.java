package org.sophize.metamath.formachines;

import com.google.common.base.Strings;
import mmj.lang.*;
import mmj.verify.Grammar;
import org.sophize.datamodel.MachineResponse;
import org.sophize.datamodel.ResourcePointer;
import org.sophize.datamodel.ResourceType;
import org.sophize.datamodel.TruthValue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.sophize.metamath.Utils.getLookupTermsForParseNode;

public class MachineUtils {
  public static final String SET_DB = "set.mm";
  public static final String ISET_DB = "iset.mm";
  public static final String NF_DB = "nf.mm";

  public static String[] toStringArray(List<ResourcePointer> ptrs) {
    return ptrs.stream().map(ResourcePointer::toString).toArray(String[]::new);
  }

  public static String[] toStringRefArray(List<Stmt> ptrs) {
    return ptrs.stream()
        .map(stmt -> ResourcePointer.assignable(ResourceType.PROPOSITION, stmt.getLabel()))
        .map(ResourcePointer::toString)
        .toArray(String[]::new);
  }

  public static List<String> getLookupTerms(String statement, String dbName) {
    Grammar grammar = Databases.getGrammar(dbName);

    String[] tokens = statement.split(" ");
    List<Sym> symList = new ArrayList<>();
    for (String token : tokens) {
      token = token.trim();
      if (Strings.isNullOrEmpty(token)) continue;
      if (token.equals("$p") || token.equals("$.")) continue;
      symList.add(grammar.symTbl.get(token));
    }

    Formula formula = new Formula(symList);
    ParseTree tree = grammar.parseFormulaWithoutSafetyNet(formula, new Hyp[0], 400000000);
    return getLookupTermsForParseNode(tree.getRoot());
  }

  public static String[] tokenizeString(String s) {
    if (s == null) return new String[0];
    return s.trim().split("\\s+");
  }

  public static String[] stripMarker(String[] tokens) {
    if (tokens == null
        || tokens.length < 2
        || !tokens[0].equals("$p")
        || !(tokens[tokens.length - 1].equals("$."))) return tokens;
    return Arrays.copyOfRange(tokens, 1, tokens.length - 1);
  }

  public static List<Integer> getDigitsLenient(String s) {
    if (!s.matches("^[\\s0-9;]+$")) return null;
    int codePoint0 = "0".codePointAt(0);
    return s.codePoints()
        .map(d -> d - codePoint0)
        .filter(d -> d >= 0 && d <= 9)
        .boxed()
        .collect(Collectors.toList());
  }

  static MachineResponse responseWithMessage(TruthValue value, String message) {
    MachineResponse response = new MachineResponse();
    response.setTruthValue(value);
    response.setMessage(message);
    return response;
  }

  private MachineUtils() {}
}
