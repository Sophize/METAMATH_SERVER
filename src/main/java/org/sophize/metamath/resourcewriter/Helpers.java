package org.sophize.metamath.resourcewriter;

import mmj.lang.Assrt;
import mmj.lang.Axiom;
import org.sophize.datamodel.Citation;

class Helpers {
  static boolean areSameProps(TempProposition t1, TempProposition t2) {
    Assrt a1 = t1.primaryAssrt();
    Assrt a2 = t2.primaryAssrt();

    if (!t1.distinctVarsStatement().equals(t2.distinctVarsStatement())) return false;
    if (!a1.getFormula().toString().equals(a2.getFormula().toString())) return false;
    if (a1.getLogHypArray().length != a2.getLogHypArray().length) return false;

    for (int i = 0; i < a2.getLogHypArray().length; i++) {
      // If the order of same set of hypothesis is different in original and alternate, we treat
      // them as different propositions. We may want to change this in the future.
      if (!getFormulaForHypothesis(a1, i).equals(getFormulaForHypothesis(a2, i))) return false;
    }
    return true;
  }

  static Citation getCitation(String label) {
    Citation citation = new Citation();
    String citationText = "See " + label + " on Metamath";
    // TODO: this doesn't work for resources outside set.mm
    String citationLink = "http://us.metamath.org/mpegif/" + label + ".html";
    citation.setTextCitation("[" + citationText + "](" + citationLink + ")");
    return citation;
  }

  static String toResourceRemark(String description) {
    if (description == null) return "";
    String withoutNewlines = description.replaceAll("\\n\\s+", " ");
    return withoutNewlines;
    // TODO: labels be preceded with a tilde (~) and math symbol tokens be enclosed in grave
    // accents (` `)
  }

  private static String getFormulaForHypothesis(Assrt assrt, int index) {
    return assrt.getLogHypArray()[index].getFormula().toString();
  }
}
