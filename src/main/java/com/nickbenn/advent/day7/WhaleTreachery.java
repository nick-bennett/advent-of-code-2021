/*
 *  Copyright 2021 Nicholas Bennett.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.nickbenn.advent.day7;

import com.nickbenn.advent.util.Defaults;
import com.nickbenn.advent.util.Parser;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.function.Function;
import java.util.function.IntUnaryOperator;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

public class WhaleTreachery {

  private static final Pattern VALUE_SPLITTER = Pattern.compile("\\s*,\\s*");

  private final int[] data;

  public WhaleTreachery(String filename) throws URISyntaxException, IOException {
    //noinspection ConstantConditions
    data = new Parser
        .Builder(getClass().getResource(filename).toURI())
        .setStripped(true)
        .setTrimmed(true)
        .build()
        .lineStream()
        .flatMap(VALUE_SPLITTER::splitAsStream)
        .mapToInt(Integer::parseInt)
        .sorted()
        .toArray();
  }

  public static void main(String[] args) throws URISyntaxException, IOException {
    WhaleTreachery treachery = new WhaleTreachery(Defaults.FILENAME);
    System.out.println(treachery.getLinearFuelCost());
    System.out.println(treachery.getTriangularFuelCost());
  }

  public int getLinearFuelCost() {
    int median = getMedian();
    return getFuelCost(median, Math::abs);
  }

  public int getTriangularFuelCost() {
    double mean = getMean();
    int median = getMedian();
    int start = (int) ((mean < median) ? Math.floor(mean) : Math.ceil(mean));
    int step = (start > median) ? -1 : 1;
    int bestCost = Integer.MAX_VALUE;
    for (int position = start; position != median; position += step) {
      bestCost = Math.min(bestCost,
          getFuelCost(position, (diff) -> (diff * diff + Math.abs(diff)) / 2));
    }
    return bestCost;
  }

  private int getMedian() {
    int length = data.length;
    int midpoint = data.length / 2;
    int median = data[midpoint];
    if ((length & 1) == 0) {
      median = (median + data[midpoint - 1]) / 2;
    }
    return median;
  }

  private double getMean() {
    return IntStream
        .of(data)
        .average()
        .orElse(0);
  }

  private int getFuelCost(int position, IntUnaryOperator costFunction) {
    return IntStream
        .of(data)
        .map((value) -> position - value)
        .map(costFunction)
        .sum();
  }

}
