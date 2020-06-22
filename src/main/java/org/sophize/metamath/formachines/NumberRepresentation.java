package org.sophize.metamath.formachines;

import mmj.lang.ParseNode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class NumberRepresentation {
  public final long number;
  public final boolean decimalFormat;
  public final int leadingZeros;

  public static final NumberRepresentation DECIMAL_10 = new NumberRepresentation(10L, true);
  public static final NumberRepresentation PRIMITIVE_10 = new NumberRepresentation(10L, false);
  public static final String DECIMAL_CONSTRUCTOR = ";";

  public NumberRepresentation(long number, boolean decimalFormat, int leadingZeros) {
    this.number = number;
    this.decimalFormat = decimalFormat;
    this.leadingZeros = leadingZeros;
    if (number < 0 || (number > 10 && !decimalFormat)) {
      throw new UnsupportedOperationException();
    }
    if ((leadingZeros > 0 && !decimalFormat) || leadingZeros < 0) {
      throw new IllegalStateException("Invalid number format");
    }
  }

  public NumberRepresentation(long number, boolean decimalFormat) {
    this(number, decimalFormat, 0);
  }

  public static NumberRepresentation fromParseNode(ParseNode node) {
    if (node == null) return null;
    String label = node.stmt.getLabel();
    if (label.equals("cdc")) {
      if (node.child.length != 2) return null;
      NumberRepresentation child0 = NumberRepresentation.fromParseNode(node.child[0]);
      NumberRepresentation child1 = NumberRepresentation.fromParseNode(node.child[1]);
      if (child0 == null || child1 == null) return null;
      if (child1.number >= 10) return null; // We dont handle these unorthodox numbers;
      int leadingZeros = child0.leadingZeros;
      if (child0.number == 0) leadingZeros++;
      return new NumberRepresentation(child0.number * 10 + child1.number, true, leadingZeros);
    }

    if(label.equals("cc0")) return new NumberRepresentation(0);
    if (!label.startsWith("c")) return null;
    try {
      return new NumberRepresentation(Integer.parseInt(label.substring(1)), false);
    } catch (Exception e) {
    }

    return null;
  }

  public static NumberRepresentation fromTokens(String[] tokens) {
    if (tokens.length == 0) return null;
    if (tokens.length == 1 && tokens[0].equals("10")) return PRIMITIVE_10;
    int tokenIndex = 0;
    while (tokens[tokenIndex].equals(DECIMAL_CONSTRUCTOR)) tokenIndex++;
    int numDecimalConstructor = tokenIndex;
    while (tokens[tokenIndex].equals("0")) tokenIndex++;
    int numLeadingZeroes = tokenIndex - numDecimalConstructor;
    int numDigits = 0;
    long number = 0;

    for (; tokenIndex < tokens.length; tokenIndex++) {
      try {
        Integer digit = Integer.parseInt(tokens[tokenIndex]);
        if (digit < 0 || digit > 9) return null;
        number = number * 10 + digit;
        numDigits++;
      } catch (Exception e) {
        return null;
      }
    }
    if (numDecimalConstructor != numLeadingZeroes + numDigits - 1) return null;
    return new NumberRepresentation(number, numDecimalConstructor > 0, numLeadingZeroes);
  }

  public static NumberRepresentation fromDigits(List<Integer> digits) {
    if (digits == null) return null;
    int numLeadingZeros = 0;
    int number = 0;
    for (var digit : digits) {
      if (number == 0 && digit == 0) numLeadingZeros++;
      number = number * 10 + digit;
    }
    return new NumberRepresentation(number, number >= 10 || numLeadingZeros > 0, numLeadingZeros);
  }

  public NumberRepresentation(long number) {
    this(number, number >= 10, 0);
  }

  public String toString() {
    if (!decimalFormat) return Long.toString(number);
    var digits = getDigits();
    return (DECIMAL_CONSTRUCTOR + " ").repeat(digits.size() - 1)
        + digits.stream().map(digit -> Long.toString(digit)).collect(Collectors.joining(" "));
  }

  public String nameForId() {
    return ((!decimalFormat && number == 10) ? "prm" : "")
        + "0".repeat(leadingZeros)
        + Long.toString(number);
  }

  public List<Long> getDigits() {
    if (number == 0) {
      return new ArrayList<>(Collections.nCopies(leadingZeros + 1, 0L));
    }
    List<Long> digits = new ArrayList<>();
    for (long num = number; num > 0; num /= 10) digits.add(num % 10);
    digits.addAll(Collections.nCopies(leadingZeros, 0L));
    Collections.reverse(digits);
    return digits;
  }

  public static boolean isNumberSymbol(String symbol) {
    if (symbol.equals(DECIMAL_CONSTRUCTOR)) return true;
    if (symbol.length() != 1) return false;
    int digit = symbol.codePointAt(0) - "0".codePointAt(0);
    return digit >= 0 && digit <= 9;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    NumberRepresentation that = (NumberRepresentation) o;
    return number == that.number
        && decimalFormat == that.decimalFormat
        && leadingZeros == that.leadingZeros;
  }

  @Override
  public int hashCode() {
    return Objects.hash(number, decimalFormat, leadingZeros);
  }
}
