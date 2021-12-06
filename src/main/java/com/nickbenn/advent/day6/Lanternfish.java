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
package com.nickbenn.advent.day6;

import com.nickbenn.advent.util.Defaults;
import com.nickbenn.advent.util.Parser;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

public class Lanternfish {

  private static final Pattern INPUT_SPLITTER = Pattern.compile("\\s*,\\s*");
  private static final int MAX_REPRODUCTION_TIMER = 8;
  private static final int POST_REPRODUCTION_TIMER = 6;

  private final long[] counts;

  public Lanternfish(String filename) throws URISyntaxException, IOException {
    //noinspection ConstantConditions
    counts = new Parser
        .Builder(getClass().getResource(filename).toURI())
        .setStripped(true)
        .setTrimmed(true)
        .build()
        .lineStream()
        .flatMap(INPUT_SPLITTER::splitAsStream)
        .mapToInt(Integer::parseInt)
        .collect(
            () -> new long[MAX_REPRODUCTION_TIMER + 1],
            (long[] counts, int timerValue) -> counts[timerValue]++,
            (long[] counts1, long[] counts2) -> {
              for (int i = 0; i <= counts1.length; i++) {
                counts1[i] += counts2[i];
              }
            }
        );
  }

  public static void main(String[] args) throws URISyntaxException, IOException {
    Lanternfish lanternfish = new Lanternfish(Defaults.FILENAME);
    System.out.println(lanternfish.count(80));
    System.out.println(lanternfish.count(176)); // Advance another 176 after the first 80.
  }

  public long count(int generations) {
    for (int i = 0; i < generations; i++) {
      advance();
    }
    return LongStream
        .of(counts)
        .sum();
  }

  private void advance() {
    long nextGenerationCount = counts[0];
    System.arraycopy(counts, 1, counts, 0, counts.length - 1);
    counts[MAX_REPRODUCTION_TIMER] = nextGenerationCount;
    counts[POST_REPRODUCTION_TIMER] += nextGenerationCount;
  }

}
