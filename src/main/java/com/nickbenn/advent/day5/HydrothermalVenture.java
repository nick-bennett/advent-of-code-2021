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
package com.nickbenn.advent.day5;

import com.nickbenn.advent.util.Defaults;
import com.nickbenn.advent.util.Parser;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class HydrothermalVenture {

  private static final Pattern LINE_PATTERN = Pattern.compile(
      "^\\s*(?<x1>\\d+)\\s*,\\s*(?<y1>\\d+)\\s*->\\s*(?<x2>\\d+)\\s*,\\s*(?<y2>\\d+)\\s*$");

  private final List<Pair> pairs;

  public HydrothermalVenture(String filename) throws URISyntaxException, IOException {
    //noinspection ConstantConditions
    pairs = new Parser
        .Builder(getClass().getResource(filename).toURI())
        .setStripped(true)
        .build()
        .lineStream()
        .map(LINE_PATTERN::matcher)
        .filter(Matcher::matches)
        .map((matcher) -> new Pair(
            new Point(Integer.parseInt(matcher.group("x1")), Integer.parseInt(matcher.group("y1"))),
            new Point(Integer.parseInt(matcher.group("x2")), Integer.parseInt(matcher.group("y2")))
        ))
        .collect(Collectors.toList());
  }

  public static void main(String[] args) throws URISyntaxException, IOException {
    HydrothermalVenture venture = new HydrothermalVenture(Defaults.FILENAME);
    System.out.println(venture.countOverlaps((pair) -> pair.rectilinear));
    System.out.println(venture.countOverlaps((pair) -> true));
  }

  public int countOverlaps(Predicate<Pair> filter) {
    int[] extremes = new int[2];
    List<Pair> rectilinearPairs = pairs
        .stream()
        .filter(filter)
        .peek((pair) -> {
          extremes[0] = Math.max(extremes[0], Math.max(pair.p.x, pair.q.x));
          extremes[1] = Math.max(extremes[1], Math.max(pair.p.y, pair.q.y));
        })
        .collect(Collectors.toList());
    int[][] counts = new int[extremes[1] + 1][extremes[0] + 1];
    int[] overlaps = new int[1];
    rectilinearPairs.forEach((pair) -> {
      int deltaX = pair.q.x - pair.p.x;
      int deltaY = pair.q.y - pair.p.y;
      int steps = Math.max(Math.abs(deltaX), Math.abs(deltaY)) + 1;
      int incrementX = (int) Math.signum(deltaX);
      int incrementY = (int) Math.signum(deltaY);
      for (int step = 0, x = pair.p.x, y = pair.p.y;
          step < steps;
          step++, x += incrementX, y += incrementY) {
        if (++counts[y][x] == 2) {
          overlaps[0]++;
        }
      }
    });
    return overlaps[0];
  }

  private static class Point {

    private final int x;
    private final int y;

    private Point(int x, int y) {
      this.x = x;
      this.y = y;
    }

  }

  public static class Pair {

    private final Point p;
    private final Point q;
    private final boolean rectilinear;

    private Pair(Point p, Point q) {
      this.p = p;
      this.q = q;
      rectilinear = (p.x == q.x || p.y == q.y);
    }

    public Point getP() {
      return p;
    }

    public Point getQ() {
      return q;
    }

    public boolean isRectilinear() {
      return rectilinear;
    }

  }

}
