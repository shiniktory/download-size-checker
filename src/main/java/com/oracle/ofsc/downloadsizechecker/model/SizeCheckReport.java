package com.oracle.ofsc.downloadsizechecker.model;


import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * Represents the result of resource analysis.
 */
@Data
@NoArgsConstructor
public class SizeCheckReport implements Serializable {

   /**
    * Metadata of the root resource
    * (the one which url was provided for analysis).
    */
   private ResourceDetails rootResourceDetails;

   /**
    * The amount of total bytes for download
    * (including embedded resources).
    */
   private long totalBytesForDownload;

   /**
    * The amount of total requests made for analysis.
    */
   private long totalRequestsMade;

   /**
    * Metadata of the embedded resources if any.
    */
   private List<ResourceDetails> embeddedResourcesDetails;

}
