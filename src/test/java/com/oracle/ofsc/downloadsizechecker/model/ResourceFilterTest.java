package com.oracle.ofsc.downloadsizechecker.model;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

import static com.oracle.ofsc.downloadsizechecker.model.ResourceFilter.*;
import static org.junit.Assert.*;


public class ResourceFilterTest {

   @Test
   public void testFindByFilter() throws Exception {
      // FUNCTIONALITY // TESTS
      assertEquals(ALL, findByFilter("*"));
      assertEquals(IMAGE, findByFilter("img"));
      assertEquals(IMAGE, findByFilter("IMG"));
      assertEquals(SCRIPT, findByFilter("Script"));
      assertEquals(STYLE, findByFilter("style"));

      try {
         String notSupportedFilter = "not_supported_filter";
         findByFilter(notSupportedFilter);
         fail("Expected UnsupportedOperationException caused by unsupported filter type, but wasn't thrown. Filter: " + notSupportedFilter);
      } catch (UnsupportedOperationException e) {
         // expected behaviour
      }
   }

   @Test
   public void testFindByFilterQueryString() throws Exception {
      // FUNCTIONALITY // TESTS
      assertEquals(Collections.singletonList(ALL), findByFilterQueryString("*"));
      assertEquals(Arrays.asList(IMAGE, SCRIPT), findByFilterQueryString("img;script"));
      assertEquals(Arrays.asList(IMAGE, SCRIPT), findByFilterQueryString("img;script;"));
      assertEquals(Arrays.asList(IMAGE, SCRIPT, FONT), findByFilterQueryString("img;script;font"));
      assertEquals(Arrays.asList(FONT, STYLE), findByFilterQueryString("font;style;"));
      assertEquals(Arrays.asList(FONT, STYLE), findByFilterQueryString(";font;style;"));
   }
}