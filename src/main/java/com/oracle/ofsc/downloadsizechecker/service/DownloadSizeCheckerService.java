package com.oracle.ofsc.downloadsizechecker.service;


import com.oracle.ofsc.downloadsizechecker.model.SizeCheckReport;

/**
 * Analyses provided resources for download size.
 */
public interface DownloadSizeCheckerService {

   /**
    * Returns {@link SizeCheckReport} which contains details of the performed resource analysis.
    *
    * @param targetUrl            string representation of the resource to analyze
    * @param resourceFiltersQuery combination of string representations of embedded resources filters
    * @return {@link SizeCheckReport} which contains details of the performed resource analysis
    */
   SizeCheckReport getAnalysisReport(String targetUrl, String resourceFiltersQuery);
}
