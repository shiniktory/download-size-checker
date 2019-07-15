package com.oracle.ofsc.downloadsizechecker.service;


import com.oracle.ofsc.downloadsizechecker.model.ResourceFilter;

import java.io.IOException;
import java.util.List;

/**
 * Provides functionality for parsing documents.
 */
public interface DocumentParser {

   /**
    * Returns the list of embedded resources absolute links
    * found in the document by the given <code>documentUrl</code>
    * and which suite the specified <code>resourceFilters</code>.
    *
    * @param documentUrl    string representation of the document's URL
    * @param resourceFilters the list of filters for the embedded resources
    * @return the list of embedded resources absolute links from the document
    * @throws IOException if something went wrong during document parsing
    */
   List<String> getEmbeddedResourcesLinks(String documentUrl, List<ResourceFilter> resourceFilters) throws IOException;
}
