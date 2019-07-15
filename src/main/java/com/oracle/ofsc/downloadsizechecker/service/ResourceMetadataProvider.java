package com.oracle.ofsc.downloadsizechecker.service;


import com.oracle.ofsc.downloadsizechecker.model.ResourceDetails;

import java.net.URL;

/**
 * Provides metadata of the specified resource.
 */
public interface ResourceMetadataProvider {

   /**
    * Returns {@link ResourceDetails} for the given <code>resourceUrl</code>.
    *
    * @param resourceUrl {@link URL} of the resource to analyze
    * @return {@link ResourceDetails} for the given <code>resourceUrl</code>
    */
   ResourceDetails getResourceDetails(URL resourceUrl);

}
