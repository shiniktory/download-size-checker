package com.oracle.ofsc.downloadsizechecker.service;

import com.oracle.ofsc.downloadsizechecker.exception.InvalidUrlException;
import com.oracle.ofsc.downloadsizechecker.exception.ResourceAccessException;
import com.oracle.ofsc.downloadsizechecker.model.ResourceDetails;
import com.oracle.ofsc.downloadsizechecker.model.ResourceFilter;
import com.oracle.ofsc.downloadsizechecker.model.SizeCheckReport;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static com.oracle.ofsc.downloadsizechecker.utility.ValidationUtil.validateUrl;

@Service
@Slf4j
public class HttpResourceDownloadSizeCheckerServiceImpl implements DownloadSizeCheckerService {

   @Autowired
   private ResourceMetadataProvider resourceMetadataProvider;

   @Autowired
   private DocumentParser documentParser;

   @Override
   public SizeCheckReport getAnalysisReport(String targetUrl, String resourceFiltersQuery) {
      validateUrl(targetUrl);

      List<ResourceFilter> resourceFilterTypes = getResourceFilters(resourceFiltersQuery);

      SizeCheckReport sizeCheckReport = new SizeCheckReport();
      AtomicInteger totalRequestsCount = new AtomicInteger(0);

      ResourceDetails rootResourceDetails = getResourceDetails(totalRequestsCount, targetUrl);
      sizeCheckReport.setRootResourceDetails(rootResourceDetails);

      log.debug("Main resource details: {}", rootResourceDetails);

      long totalBytesForDownload = rootResourceDetails.getBytesForDownload();

      if (StringUtils.startsWith(rootResourceDetails.getContentType(), MediaType.TEXT_HTML_VALUE)) {

         List<ResourceDetails> embeddedResourcesDetails = getEmbeddedResourceDetails(targetUrl, resourceFilterTypes, totalRequestsCount);
         sizeCheckReport.setEmbeddedResourcesDetails(embeddedResourcesDetails);

         totalBytesForDownload += getEmbeddedResourcesSize(embeddedResourcesDetails);
      }

      sizeCheckReport.setTotalBytesForDownload(totalBytesForDownload);
      sizeCheckReport.setTotalRequestsMade(totalRequestsCount.get());

      log.debug("Analysis report: {}", sizeCheckReport);

      return sizeCheckReport;
   }

   private long getEmbeddedResourcesSize(List<ResourceDetails> embeddedResourcesDetails) {

      return embeddedResourcesDetails.stream()
                  .mapToLong(ResourceDetails::getBytesForDownload)
                  .sum();
   }

   private List<ResourceFilter> getResourceFilters(String resourceFilterString) {
      List<ResourceFilter> resourceFilterTypes;

      if (StringUtils.isBlank(resourceFilterString)) {
         resourceFilterTypes = Collections.singletonList(ResourceFilter.ALL);
      } else {
         resourceFilterTypes = ResourceFilter.findByFilterQueryString(resourceFilterString);
      }

      return resourceFilterTypes;
   }

   private List<ResourceDetails> getEmbeddedResourceDetails(String targetUrl, List<ResourceFilter> resourceFilters, AtomicInteger totalRequestsCount) {
      try {
         List<String> embeddedResourcesLinks = documentParser.getEmbeddedResourcesLinks(targetUrl, resourceFilters);

         return embeddedResourcesLinks.stream()
               .map(url -> getResourceDetails(totalRequestsCount, url))
               .collect(Collectors.toList());

      } catch (IOException e) {
         throw new ResourceAccessException("Error during accessing the resource with URL: " + targetUrl, e);
      }
   }

   private ResourceDetails getResourceDetails(AtomicInteger totalRequestsCount, String url) {
      URL embeddedResourceUrl = getResourceUrl(url);
      ResourceDetails embeddedResourceDetails = resourceMetadataProvider.getResourceDetails(embeddedResourceUrl);
      totalRequestsCount.incrementAndGet();

      return embeddedResourceDetails;
   }

   private URL getResourceUrl(String targetUrl) {
      try {
         return new URL(targetUrl);
      } catch (MalformedURLException e) {
         throw new InvalidUrlException(e);
      }
   }
}
