package com.oracle.ofsc.downloadsizechecker.utility;

import com.oracle.ofsc.downloadsizechecker.exception.InvalidUrlException;
import org.junit.Test;

import static org.junit.Assert.fail;


public class ValidationUtilTest {

   @Test
   public void testValidateUrl() throws Exception {
      assertUrlValidation(null, false);
      assertUrlValidation("", false);
      assertUrlValidation(" ", false);
      assertUrlValidation("/someUrl", true);
      assertUrlValidation("/someUrl/otherUrl", true);
   }

   private void assertUrlValidation(String url, boolean shouldBeValid) {
      try {
         ValidationUtil.validateUrl(url);

         failValidUrl(url, shouldBeValid);
      } catch (InvalidUrlException e) {
         e.printStackTrace();
         failInvalidUrl(url, shouldBeValid);
      }
   }

   private void failInvalidUrl(String url, boolean shouldBeValid) {
      if (shouldBeValid) {
         fail("Should be valid, but was not. URL: " + url);
      }
   }

   private void failValidUrl(String url, boolean shouldBeValid) {
      if (!shouldBeValid) {
         fail("Should be invalid, but was not. URL: " + url);
      }
   }

}