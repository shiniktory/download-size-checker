package com.oracle.ofsc.downloadsizechecker.controller;

import com.oracle.ofsc.downloadsizechecker.model.SizeCheckReport;
import com.oracle.ofsc.downloadsizechecker.service.DownloadSizeCheckerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * RestController provides functionality to analyze given resources for
 * download size.
 */
@RestController
@Slf4j
public class DownloadSizeCheckerController {

   @Autowired
   private DownloadSizeCheckerService downloadSizeCheckerService;

   @GetMapping("/checker/analyze")
   public SizeCheckReport analyzeDownloadSize(@RequestParam("url") String targetUrl,
                                              @RequestParam(value = "filter", required = false) String filterQueryString) {

      log.info("Hit /checker/analyze endpoint with parameter: url={}, filter={}", targetUrl, filterQueryString);

      return downloadSizeCheckerService.getAnalysisReport(targetUrl, filterQueryString);
   }
}
