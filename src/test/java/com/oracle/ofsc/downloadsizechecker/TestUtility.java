package com.oracle.ofsc.downloadsizechecker;


import com.oracle.ofsc.downloadsizechecker.model.ResourceDetails;
import com.oracle.ofsc.downloadsizechecker.model.SizeCheckReport;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.Random;

/**
 * Provides methods for tests support.
 */
public class TestUtility {

   public static String constructTestResourceUrl(int port, String resource) {
      return String.format("http://localhost:%d/%s", port, resource);
   }

   public static String constructRandomResourceUrl() {
      return "http://localhost:1234/testUrl/" + new Random().nextInt();
   }

   public static SizeCheckReport constructRandomSizeCheckReport(String resourceUrl) {
      SizeCheckReport sizeCheckReport = new SizeCheckReport();
      Random random = new Random();

      sizeCheckReport.setTotalBytesForDownload(random.nextLong());
      sizeCheckReport.setTotalRequestsMade(random.nextInt());

      ResourceDetails resourceDetails = constructRandomResourceDetails(resourceUrl, random, MediaType.TEXT_HTML_VALUE);

      sizeCheckReport.setRootResourceDetails(resourceDetails);

      sizeCheckReport.setEmbeddedResourcesDetails(Arrays.asList(
            constructRandomResourceDetails(resourceUrl + random.nextInt(), random, MediaType.IMAGE_JPEG_VALUE),
            constructRandomResourceDetails(resourceUrl + random.nextInt(), random, MediaType.APPLICATION_PDF_VALUE)
      ));

      return sizeCheckReport;
   }

   private static ResourceDetails constructRandomResourceDetails(String resourceUrl, Random random, String textHtmlValue) {

      return new ResourceDetails(resourceUrl,
            textHtmlValue,
            random.nextLong());
   }
}
