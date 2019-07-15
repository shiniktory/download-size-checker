package com.oracle.ofsc.downloadsizechecker.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception represents situation when URL is invalid.
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidUrlException extends RuntimeException {

   public InvalidUrlException(String message) {
      super(message);
   }

   public InvalidUrlException(Throwable cause) {
      super(cause);
   }
}
