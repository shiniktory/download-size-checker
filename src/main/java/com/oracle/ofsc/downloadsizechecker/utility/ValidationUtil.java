package com.oracle.ofsc.downloadsizechecker.utility;

import com.oracle.ofsc.downloadsizechecker.exception.InvalidUrlException;
import org.apache.commons.lang3.StringUtils;

/**
 * Contains methods for validation functionality.
 */
public class ValidationUtil {

   /**
    * Validates the given <code>targetUrl</code>
    * for blank.
    */
   public static void validateUrl(String targetUrl) {
      if (StringUtils.isBlank(targetUrl)) {
         throw new InvalidUrlException("URL is blank");
      }
   }
}
