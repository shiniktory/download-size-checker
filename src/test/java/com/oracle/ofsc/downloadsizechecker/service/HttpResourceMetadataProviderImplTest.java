package com.oracle.ofsc.downloadsizechecker.service;

import com.oracle.ofsc.downloadsizechecker.TestUtility;
import com.oracle.ofsc.downloadsizechecker.model.ResourceDetails;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.net.URL;

import static org.junit.Assert.*;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class HttpResourceMetadataProviderImplTest {

   @Autowired
   private ResourceMetadataProvider resourceMetadataProvider;

   private URL documentWithContentUrl;
   private URL imageUrl;

   @LocalServerPort
   private int randomServerPort;

   @Before
   public void setUp() throws Exception {
      String testPageUrl = TestUtility.constructTestResourceUrl(randomServerPort, "testPage.html");
      documentWithContentUrl = new URL(testPageUrl);

      String testImageUrl = TestUtility.constructTestResourceUrl(randomServerPort, "Mozilla-Firefox.jpg");
      imageUrl = new URL(testImageUrl);
   }

   @Test
   public void testGetResourceDetailsPage() throws Exception {
      // PREDICATE
      long expectedFileSize = getExpectedFileSize(documentWithContentUrl);

      // FUNCTIONALITY
      ResourceDetails htmlPageDetails = resourceMetadataProvider.getResourceDetails(documentWithContentUrl);

      // TESTS
      assertNotNull(htmlPageDetails);
      assertEquals(documentWithContentUrl.toString(), htmlPageDetails.getUrl());
      assertEquals(MediaType.TEXT_HTML_VALUE, htmlPageDetails.getContentType());
      assertEquals(expectedFileSize, htmlPageDetails.getBytesForDownload());
   }

   @Test
   public void testGetResourceDetailsImage() throws Exception {
      // PREDICATE
      long expectedFileSize = getExpectedFileSize(imageUrl);

      // FUNCTIONALITY
      ResourceDetails imageDetails = resourceMetadataProvider.getResourceDetails(imageUrl);

      // TESTS
      assertNotNull(imageDetails);
      assertEquals(imageUrl.toString(), imageDetails.getUrl());
      assertEquals(MediaType.IMAGE_JPEG_VALUE, imageDetails.getContentType());
      assertEquals(expectedFileSize, imageDetails.getBytesForDownload());
   }

   private long getExpectedFileSize(URL fileUrl) throws IOException {
      Resource imageResource = new ClassPathResource("/static" + fileUrl.getPath());
      assertTrue(imageResource.exists());

      return imageResource.getFile().length();
   }
}