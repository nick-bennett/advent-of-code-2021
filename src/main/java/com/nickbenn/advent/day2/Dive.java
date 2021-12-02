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
package com.nickbenn.advent.day2;

import com.nickbenn.advent.util.Defaults;
import com.nickbenn.advent.util.Parser;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Dive {

  private static final Pattern COMMAND_PATTERN = Pattern.compile(
      "^\\s*(?:forward\\s+(?<forward>\\d+)|down\\s+(?<down>\\d+)|up\\s+(?<up>\\d+))\\s*$");

  private final List<String> data;

  public Dive(String filename) throws URISyntaxException, IOException {
    //noinspection ConstantConditions
    data = new Parser
        .Builder(getClass().getResource(filename).toURI())
        .build()
        .lineStream()
        .collect(Collectors.toList());
  }

  public static void main(String[] args) throws URISyntaxException, IOException {
    Dive dive = new Dive(Defaults.FILENAME);
    System.out.println(dive.positionDotProduct());
    System.out.println(dive.positionDotProductAim());
  }

  public long positionDotProduct() {
    int[] position = {0, 0};
    data
        .stream()
        .map(COMMAND_PATTERN::matcher)
        .filter(Matcher::matches)
        .forEach((matcher) -> {
          position[0] += parseInt(matcher.group("forward"), 0);
          position[1] += parseInt(matcher.group("down"), 0) - parseInt(matcher.group("up"), 0);
        });
    return (long) position[0] * position[1];
  }

  public long positionDotProductAim() {
    long[] position = {0, 0, 0};
    data
        .stream()
        .map(COMMAND_PATTERN::matcher)
        .filter(Matcher::matches)
        .forEach((matcher) -> {
          int forward = parseInt(matcher.group("forward"), 0);
          position[0] += forward;
          position[2] += position[1] * forward;
          position[1] += parseInt(matcher.group("down"), 0) -  parseInt(matcher.group("up"), 0);
        });
    return position[0] * position[2];
  }

  private int parseInt(String input, int emptyValue) {
    return (input != null && !input.isEmpty()) ? Integer.parseInt(input) : emptyValue;
  }

}
