package com.nickbenn.advent.day7;

import static org.junit.jupiter.api.Assertions.*;

import com.nickbenn.advent.util.Defaults;
import java.io.IOException;
import java.net.URISyntaxException;
import org.junit.jupiter.api.Test;

class WhaleTreacheryTest {

  @Test
  void getLinearFuelCost() throws URISyntaxException, IOException {
    WhaleTreachery treachery = new WhaleTreachery(Defaults.TEST_FILENAME);
    assertEquals(37, treachery.getLinearFuelCost());
  }

  @Test
  void getTriangularFuelCost() throws URISyntaxException, IOException {
    WhaleTreachery treachery = new WhaleTreachery(Defaults.TEST_FILENAME);
    assertEquals(168, treachery.getTriangularFuelCost());
  }

}