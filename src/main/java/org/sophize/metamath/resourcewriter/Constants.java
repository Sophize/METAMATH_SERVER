package org.sophize.metamath.resourcewriter;

import java.util.List;
import java.util.Map;

class Constants {
  static final Map<String, List<String>> MINIMAL_AXIOMS =
      Map.of(
          "set.mm",
          List.of(
              "ax-1",
              "ax-2",
              "ax-3",
              "ax-mp",
              "ax-gen",
              "ax-4",
              "ax-5",
              "ax-6",
              "ax-7",
              "ax-8",
              "ax-9",
              "ax-10",
              "ax-11",
              "ax-12",
              "ax-13",
              "ax-ext",
              "ax-rep",
              "ax-pow",
              "ax-un",
              "ax-reg",
              "ax-inf",
              "ax-ac",
              "ax-groth"),
          "iset.mm",
          List.of(
              "ax-1",
              "ax-2",
              "ax-mp",
              "ax-ia1",
              "ax-ia2",
              "ax-ia3",
              "ax-io",
              "ax-in1",
              "ax-in2",
              "ax-4",
              "ax-5",
              "ax-i5r",
              "ax-7",
              "ax-gen",
              "ax-ial",
              "ax-ie1",
              "ax-ie2",
              "ax-8",
              "ax-i9",
              "ax-10",
              "ax-11",
              "ax-i12",
              "ax-bnd",
              "ax-13",
              "ax-14",
              "ax-17",
              "ax-ext",
              "ax-coll",
              "ax-sep",
              "ax-pow",
              "ax-pr",
              "ax-un",
              "ax-setind",
              "ax-iinf"));

  private Constants() {}
}
