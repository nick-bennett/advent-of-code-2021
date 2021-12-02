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
package com.nickbenn.advent.day1;

import com.nickbenn.advent.util.Defaults;
import com.nickbenn.advent.util.Parser;
import java.io.IOException;
import java.net.URISyntaxException;

public class SonarSweep {

  private final int[] data;

  public SonarSweep(String filename) throws IOException, URISyntaxException {
    //noinspection ConstantConditions
    data = new Parser
        .Builder(getClass().getResource(filename).toURI())
        .build()
        .intStream()
        .toArray();
  }

  public static void main(String[] args) throws URISyntaxException, IOException {
    SonarSweep sweep = new SonarSweep(Defaults.FILENAME);
    System.out.println(sweep.countMovingSumIncreases(1));
    System.out.println(sweep.countMovingSumIncreases(3));
  }

  public int countMovingSumIncreases(int window) {
    int increases = 0;
    int previous = 0;
    int current = data[0];
    for (int i = 1; i < window; i++) {
      current += data[i];
      previous += data[i - 1];
    }
    for (int i = window; i < data.length; i++) {
      current -= data[i - window];
      current += data[i];
      previous += data[i - 1];
      if (current > previous) {
        increases++;
      }
      previous -= data[i - window];
    }
    return increases;
  }

}
