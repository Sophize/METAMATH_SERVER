package org.sophize.metamath.formachines;

import mmj.lang.ParseNode;
import mmj.lang.Sym;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class ParseNodeHelpers {

  private static String DIVIDER = ".";
  private static Set<String> NUMBER_LABELS =
      Set.of("cdc", "cc0", "c1", "c2", "c3", "c4", "c5", "c6", "c7", "c8", "c9", "c10");
  private static Set<String> SPECIAL_SET_STATEMENTS = Set.of("cn", "cn0", "cc", "cr");
  private static Map<String, String> LABEL_REPLACEMENTS =
      Map.ofEntries(
          Map.entry("wcel", "in"),
          Map.entry("wceq", "eq"),
          Map.entry("caddc", "p"),
          Map.entry("cmul", "t"),
          Map.entry("cprime", "prm"));

  public static String getLabel(ParseNode node) {
    String special = specialLabel(node);
    if (special != null) return special;
    String topLevelLabel = node.stmt.getLabel();
    topLevelLabel = LABEL_REPLACEMENTS.getOrDefault(topLevelLabel, topLevelLabel);
    int numChildren = node.child.length;
    if (numChildren == 0) return topLevelLabel;
    if (numChildren == 1) return topLevelLabel + DIVIDER + getLabel(node.child[0]);
    if (numChildren == 2)
      return getLabel(node.child[0]) + DIVIDER + topLevelLabel + DIVIDER + getLabel(node.child[1]);
    var labelBuilder = new StringBuilder(getLabel(node.child[0]));
    for (int i = 1; i < numChildren; i++) {
      labelBuilder.append(DIVIDER).append(topLevelLabel).append(i);
      labelBuilder.append(DIVIDER).append(getLabel(node.child[i]));
    }
    return labelBuilder.toString();
  }

  public static boolean isNumberExpression(ParseNode node) {
    return NUMBER_LABELS.contains(node.stmt.getLabel());
  }

  public static boolean isSumOrNumberExpression(ParseNode node) {
    if (isNumberExpression(node)) return true;
    if (!node.stmt.getLabel().equals("co")) return false;
    if (!node.child[2].stmt.getLabel().equals("caddc")) return false;
    return isSumOrNumberExpression(node.child[0]) && isSumOrNumberExpression(node.child[1]);
  }

  public static boolean isSumOrProductOrNumberExpression(ParseNode node) {
    if (isNumberExpression(node)) return true;
    if (!node.stmt.getLabel().equals("co")) return false;
    if (!node.child[2].stmt.getLabel().equals("caddc")
        && !node.child[2].stmt.getLabel().equals("cmul")) return false;
    return isSumOrProductOrNumberExpression(node.child[0])
        && isSumOrProductOrNumberExpression(node.child[1]);
  }

  public static boolean isSumExpressionStatement(ParseNode node) {
    if (!node.stmt.getLabel().equals("wceq")) return false;
    return isSumOrNumberExpression(node.child[0]) && isSumOrNumberExpression(node.child[1]);
  }

  public static boolean isSumOrProductExpressionStatement(ParseNode node) {
    if (!node.stmt.getLabel().equals("wceq")) return false;
    return isSumOrProductOrNumberExpression(node.child[0])
        && isSumOrProductOrNumberExpression(node.child[1]);
  }

  public static NumberRepresentation getValue(ParseNode node) {
    if (isNumberExpression(node)) return NumberRepresentation.fromParseNode(node);
    if (node.stmt.getLabel().equals("co")) {
      if (node.child[2].stmt.getLabel().equals("caddc")) {
        var op1 = getValue(node.child[0]);
        var op2 = getValue(node.child[1]);
        if (op1 == null || op2 == null) return null;
        return new NumberRepresentation(op1.number + op2.number);
      } else if (node.child[2].stmt.getLabel().equals("cmul")) {
        var op1 = getValue(node.child[0]);
        var op2 = getValue(node.child[1]);
        if (op1 == null || op2 == null) return null;
        return new NumberRepresentation(op1.number * op2.number);
      }
    }
    return null;
  }

  static String asString(ParseNode node, String label, String startTag) {
    return String.join(" ", label, startTag, asStatement(node), "$.");
  }

  static String asStatement(ParseNode node) {
    return "|- " + asExpression(node);
  }

  public static String asExpression(ParseNode node) {
    var formula = node.convertToFormula();
    return Arrays.stream(formula.getSym())
        .skip(1)
        .map(Sym::toString)
        .collect(Collectors.joining(" "));
  }

  private static String specialLabel(ParseNode node) {
    String label = node.stmt.getLabel();
    if (NUMBER_LABELS.contains(label)) {
      return NumberRepresentation.fromParseNode(node).nameForId();
    }
    if (SPECIAL_SET_STATEMENTS.contains(label)) {
      return node.stmt.getFormula().getSym()[1].toString();
    }
    if (label.equals("wbr") || label.equals("co")) {
      var leftSide = getLabel(node.child[0]);
      var rightSide = getLabel(node.child[1]);
      var center = node.child[2].stmt.getLabel();
      center = LABEL_REPLACEMENTS.getOrDefault(center, center);
      return DIVIDER + leftSide + DIVIDER + center + DIVIDER + rightSide + DIVIDER;
    }
    return null;
  }
}
