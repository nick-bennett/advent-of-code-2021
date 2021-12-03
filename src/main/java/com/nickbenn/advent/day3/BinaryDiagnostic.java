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
package com.nickbenn.advent.day3;

import com.nickbenn.advent.util.Defaults;
import com.nickbenn.advent.util.Parser;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.IntConsumer;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class BinaryDiagnostic {

  private final int[] data;
  private final int bitCount;

  public BinaryDiagnostic(String filename) throws URISyntaxException, IOException {
    int[] bitCount = {0};
    //noinspection ConstantConditions
    data = new Parser
        .Builder(getClass().getResource(filename).toURI())
        .setStripped(true)
        .setTrimmed(true)
        .build()
        .lineStream()
        .peek((line) -> bitCount[0] = Math.max(bitCount[0], line.length()))
        .mapToInt((line) -> Integer.parseInt(line, 2))
        .toArray();
    this.bitCount = bitCount[0];
  }

  public static void main(String[] args) throws URISyntaxException, IOException {
    BinaryDiagnostic diagnostic = new BinaryDiagnostic(Defaults.FILENAME);
    System.out.println(diagnostic.gammaEpsilonProduct());
    System.out.println(diagnostic.generatorScrubberProduct());
  }

  public long gammaEpsilonProduct() {
    int[] counts = new int[bitCount];
    for (int value : data) {
      for (int position = 0; position < counts.length; position++, value >>>= 1) {
        counts[position] += ((value & 1) << 1) - 1;
      }
    }
    long gamma = 0;
    long epsilon = 0;
    for (int position = counts.length - 1; position >= 0; position--) {
      gamma <<= 1;
      epsilon <<= 1;
      if (counts[position] > 0) {
        gamma++;
      } else {
        epsilon++;
      }
    }
    return gamma * epsilon;
  }

  public long generatorScrubberProduct() {
    List<Integer> data = getDataList();
    for (int position = bitCount - 1; position >= 0 && data.size() > 1; position--) {
      int mask = 1 << position;
      if (countBits(data, mask) >= 0) {
        data.removeIf((value) -> (value & mask) == 0);
      } else {
        data.removeIf((value) -> (value & mask) != 0);
      }
    }
    int generator = data.get(0);
    data = getDataList();
    for (int position = bitCount - 1; position >= 0 && data.size() > 1; position--) {
      int mask = 1 << position;
      if (countBits(data, mask) >= 0) {
        data.removeIf((value) -> (value & mask) != 0);
      } else {
        data.removeIf((value) -> (value & mask) == 0);
      }
    }
    int scrubber = data.get(0);
    return (long) generator * scrubber;
  }

  private List<Integer> getDataList() {
    return IntStream
        .of(data)
        .boxed()
        .collect(Collectors.toList());
  }

  private int countBits(List<Integer> data, int mask) {
    int count = 0;
    for (int value : data) {
      count += ((value & mask) == 0) ? -1 : 1;
    }
    return count;
  }

}
