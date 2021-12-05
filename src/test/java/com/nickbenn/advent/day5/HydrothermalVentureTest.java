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

import static org.junit.jupiter.api.Assertions.*;

import com.nickbenn.advent.day5.HydrothermalVenture.Pair;
import com.nickbenn.advent.util.Defaults;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.function.Predicate;
import org.junit.jupiter.api.Test;

class HydrothermalVentureTest {

  @Test
  void countOverlaps_rectilinear() throws URISyntaxException, IOException {
    HydrothermalVenture venture = new HydrothermalVenture(Defaults.TEST_FILENAME);
    assertEquals(5, venture.countOverlaps(Pair::isRectilinear));
  }

  @Test
  void countOverlaps_all() throws URISyntaxException, IOException {
    HydrothermalVenture venture = new HydrothermalVenture(Defaults.TEST_FILENAME);
    assertEquals(12, venture.countOverlaps((pair) -> true));
  }

}