package com.oracle.ofsc.downloadsizechecker.exception;

/**
 * Represents error when requested resource cannot be accessed
 * or something went wrong during its access.
 */
public class ResourceAccessException extends RuntimeException {

   public ResourceAccessException(String message) {
      super(message);
   }

   public ResourceAccessException(String message, Throwable cause) {
      super(message, cause);
   }
}
