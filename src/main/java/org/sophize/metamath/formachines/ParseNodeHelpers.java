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
  private static Map<String, String> LABEL_REPLACEMENTS = Map.of("wcel", "e");

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

  static String asString(ParseNode node, String startTag) {
    return String.join(" ", getLabel(node), startTag, asString(node), "$.");
  }

  static String asString(ParseNode node) {
    var formula = node.convertToFormula();
    return "|- "
        + Arrays.stream(formula.getSym())
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
    if (label.equals("wbr")) {
      var leftSide = getLabel(node.child[0]);
      var rightSide = getLabel(node.child[1]);
      return leftSide + DIVIDER + node.child[2].stmt.getLabel() + DIVIDER + rightSide;
    }
    return null;
  }
}
