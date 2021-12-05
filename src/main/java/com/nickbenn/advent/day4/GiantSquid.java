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
package com.nickbenn.advent.day4;

import com.nickbenn.advent.util.Defaults;
import com.nickbenn.advent.util.Parser;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GiantSquid {

  private static final Pattern VALUE_SPLITTER = Pattern.compile("\\s*,\\s*");
  private static final Pattern ROW_SPLITTER = Pattern.compile("\\r?\\n");
  private static final Pattern COLUMN_SPLITTER = Pattern.compile("\\s+");

  private final int[] numbersDrawn;
  private final int[] scores;

  public GiantSquid(String filename) throws URISyntaxException, IOException {
    int[] bitCount = {0};
    //noinspection ConstantConditions
    String[] parts = new Parser
        .Builder(getClass().getResource(filename).toURI())
        .setTrimmed(true)
        .build()
        .lineGroupStream()
        .toArray(String[]::new);
    numbersDrawn = VALUE_SPLITTER
        .splitAsStream(parts[0])
        .mapToInt(Integer::parseInt)
        .toArray();
    Set<Board> boards = Stream
        .of(Arrays.copyOfRange(parts, 1, parts.length))
        .map(Board::new)
        .collect(Collectors.toSet());
    scores = play(boards);
  }

  public static void main(String[] args) throws URISyntaxException, IOException {
    GiantSquid squid = new GiantSquid(Defaults.FILENAME);
    System.out.println(squid.getFirstWinningScore());
    System.out.println(squid.getLastWinningScore());
  }

  public int getFirstWinningScore() {
    return scores[0];
  }

  public int getLastWinningScore() {
    return scores[scores.length - 1];
  }

  private int[] play(Set<Board> boards) {
    Set<Board> boardsInPlay = new HashSet<>(boards);
    int[] scores = new int[boardsInPlay.size()];
    int scoreIndex = 0;
    outer:
    for (int draw : numbersDrawn) {
      for (Iterator<Board> boardIter = boardsInPlay.iterator(); boardIter.hasNext(); ) {
        Board board = boardIter.next();
        if (board.draw(draw)) {
          boardIter.remove();
          scores[scoreIndex++] = draw * board.sumUncovered();
          if (boardsInPlay.isEmpty()) {
            break outer;
          }
        }
      }
    }
    return scores;
  }

  private static class Board {

    private final int[][] numbers;
    private final int boardSize;
    private final boolean[][] marks;
    private final int[] rowTotals;
    private final int[] columnTotals;

    public Board(String input) {
      this.numbers = ROW_SPLITTER
          .splitAsStream(input)
          .map(String::trim)
          .map((row) -> COLUMN_SPLITTER.splitAsStream(row).mapToInt(Integer::parseInt).toArray())
          .toArray(int[][]::new);
      boardSize = numbers.length;
      marks = new boolean[boardSize][boardSize];
      rowTotals = new int[boardSize];
      columnTotals = new int[boardSize];
    }

    public boolean draw(int value) {
      boolean bingo = false;
      outer:
      for (int rowIndex = 0; rowIndex < numbers.length; rowIndex++) {
        for (int columnIndex = 0; columnIndex < numbers[rowIndex].length; columnIndex++) {
          if (!marks[rowIndex][columnIndex] && numbers[rowIndex][columnIndex] == value) {
            marks[rowIndex][columnIndex] = true;
            if (++rowTotals[rowIndex] >= boardSize || ++columnTotals[columnIndex] >= boardSize) {
              bingo = true;
              break outer;
            }
          }
        }
      }
      return bingo;
    }

    public int sumUncovered() {
      int sum = 0;
      for (int rowIndex = 0; rowIndex < numbers.length; rowIndex++) {
        for (int columnIndex = 0; columnIndex < numbers[rowIndex].length; columnIndex++) {
          if (!marks[rowIndex][columnIndex]) {
            sum += numbers[rowIndex][columnIndex];
            marks[rowIndex][columnIndex] = true;
          }
        }
      }
      return sum;
    }

  }

}
