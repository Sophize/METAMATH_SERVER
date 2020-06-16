package org.sophize.metamath;

import mmj.lang.*;

import java.util.*;
import java.util.stream.Collectors;

public class Utils {
  // This tag is used by Sophize to detect that the text is in "argument-table" format and not the
  // usual metamath language.
  private static final String ARGUMENT_PREFIX = "ARGUMENT";
  private static final String ARGUMENT_TABLE_HEADER =
      "| Step | Hyp | Ref | Expression |\n|---|---|---|---|\n";
  public static final String DEDUP_POSTFIX = "--";

  public static boolean isTrue(Boolean b) {
    return b != null && b;
  }

  public static String varToLookupTerm(VarHyp hyp) {
    return "#T_" + hyp.getLabel();
  }

  public static List<String> getArgumentHeaders(List<Var> dummyVariables) {
    return Arrays.asList(
        ARGUMENT_PREFIX, getDummyVariablesHeader(dummyVariables), ARGUMENT_TABLE_HEADER);
  }

  public static List<String> getLookupTermsForParseNode(ParseNode node) {
    List<List<String>> childNodes = new ArrayList<>();
    for (ParseNode childNode : node.child) {
      childNodes.add(getLookupTermsForParseNode(childNode));
    }

    Sym[] formula = node.stmt.getFormula().getSym();

    if (node.stmt instanceof VarHyp) return Arrays.asList(varToLookupTerm((VarHyp) node.stmt));

    VarHyp[] varHypArray = node.stmt.getMandVarHypArray();
    /// TODO: check comment in ParseTree.java
    myAssert(childNodes.size() == varHypArray.length);

    /*
    Using VarHyp as key doesn't work in some cases: for example:
    In wcel, symbols refer to activeVarHyp - cA and cB
    But in varHypArray has wcel.cA and wcel.cB
    */
    Map<String, Integer> varIdToChildIndex = new HashMap<>();
    for (int i = 0; i < varHypArray.length; i++) {
      varIdToChildIndex.put(varHypArray[i].getFormula().getSym()[1].getId(), i);
    }
    long numConstItems = getNumConstChars(node.stmt);

    int constItemIndex = 0;
    List<String> lookupTerms = new ArrayList<>();
    for (int symIndex = 1; symIndex < formula.length; symIndex++) {
      Sym sym = formula[symIndex];
      String id = sym.getId();
      if (sym instanceof Cnst) {
        if (id.equals("(") || id.equals(")")) continue;

        String assignableId =
            getAssignableIdForTermInStmt(node.stmt.getLabel(), constItemIndex, numConstItems);
        lookupTerms.add("#T_" + assignableId);
        constItemIndex++;
      } else {
        myAssert(sym instanceof Var);
        Integer index = varIdToChildIndex.get(id);
        myAssert(index != null);
        lookupTerms.addAll(childNodes.get(index));
      }
    }
    return lookupTerms;
  }

  public static long getNumConstChars(Stmt wffStatement) {
    return Arrays.stream(wffStatement.getFormula().getSym())
            .skip(1) // we don't want to count the typecode.
            .filter(sym -> !sym.getId().equals("(") && !sym.getId().equals(")"))
            .filter(sym -> !(sym instanceof Var))
            .count();
  }

  public static String getAssignableIdForTermInStmt(String stmtLabel, int termIndex, long
          totalTerm) {
    return stmtLabel + ((totalTerm > 1) ? (DEDUP_POSTFIX + (termIndex + 1)) : "");
  }

  public static <K, V> void putIfNotDifferent(Map<K, V> map, K key, V value) {
    V existing = map.putIfAbsent(key, value);

    myAssert(existing == null || value.equals(existing));
  }

  public static void myAssert(boolean val) {
    if (!val) {
      throw new IllegalStateException("This shouldn't happen!");
    }
  }

  private static String getDummyVariablesHeader(List<Var> dummyVariables) {
    if (dummyVariables == null || dummyVariables.isEmpty()) return "";
    return dummyVariables.stream().map(Var::getId).collect(Collectors.joining(" ")) + "\n";
  }

  private Utils() {}
}
