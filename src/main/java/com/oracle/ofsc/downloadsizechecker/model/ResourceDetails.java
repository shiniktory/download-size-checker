package com.oracle.ofsc.downloadsizechecker.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Represents metadata of the resource.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResourceDetails implements Serializable {

   /**
    * Absolute resource URL.
    */
   private String url;

   /**
    * Resource content type.
    */
   private String contentType;

   /**
    * Amount of bytes for download.
    */
   private long bytesForDownload;

}
