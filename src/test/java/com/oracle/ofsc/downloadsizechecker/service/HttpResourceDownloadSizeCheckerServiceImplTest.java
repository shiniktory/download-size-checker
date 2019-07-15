package com.oracle.ofsc.downloadsizechecker.service;

import com.oracle.ofsc.downloadsizechecker.model.ResourceDetails;
import com.oracle.ofsc.downloadsizechecker.model.ResourceFilter;
import com.oracle.ofsc.downloadsizechecker.model.SizeCheckReport;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.util.CollectionUtils;

import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static com.oracle.ofsc.downloadsizechecker.TestUtility.constructRandomResourceUrl;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class HttpResourceDownloadSizeCheckerServiceImplTest {

   @Mock
   private ResourceMetadataProvider resourceMetadataProvider;

   @Mock
   private DocumentParser documentParser;

   @InjectMocks
   private DownloadSizeCheckerService downloadSizeCheckerService = new HttpResourceDownloadSizeCheckerServiceImpl();

   private String rootResourceUrlString;
   private String resourceFilterCode;
   private URL rootResourceUrl;
   private String embeddedResourceUrlString;
   private URL embeddedResourceUrl;
   private List<ResourceFilter> resourceFilters;

   @Before
   public void setUp() throws Exception {
      rootResourceUrlString = constructRandomResourceUrl();
      resourceFilters = Collections.singletonList(ResourceFilter.ALL);
      resourceFilterCode = ResourceFilter.ALL.getFilterCode();
      rootResourceUrl = new URL(rootResourceUrlString);

      embeddedResourceUrlString = constructRandomResourceUrl();
      embeddedResourceUrl = new URL(embeddedResourceUrlString);
   }

   @Test
   public void testGetAnalysisReportForImage() throws Exception {
      // PREDICATE
      ResourceDetails expectedResourceDetails = new ResourceDetails(rootResourceUrlString, MediaType.IMAGE_JPEG_VALUE, new Random().nextLong());

      when(resourceMetadataProvider.getResourceDetails(eq(rootResourceUrl)))
            .thenReturn(expectedResourceDetails);

      // FUNCTIONALITY
      SizeCheckReport actAnalysisReport = downloadSizeCheckerService.getAnalysisReport(rootResourceUrlString, resourceFilterCode);

      // TESTS
      verify(resourceMetadataProvider, only()).getResourceDetails(eq(rootResourceUrl));
      verifyZeroInteractions(documentParser);

      assertNotNull(actAnalysisReport);
      assertEquals(1, actAnalysisReport.getTotalRequestsMade()); //1 document request

      assertEquals(expectedResourceDetails.getBytesForDownload(), actAnalysisReport.getTotalBytesForDownload());
      assertEquals(expectedResourceDetails, actAnalysisReport.getRootResourceDetails());
      assertTrue(CollectionUtils.isEmpty(actAnalysisReport.getEmbeddedResourcesDetails()));
   }

   @Test
   public void testGetAnalysisReportForDocument() throws Exception {
      // PREDICATE
      Random random = new Random();
      ResourceDetails expectedDocumentDetails = new ResourceDetails(rootResourceUrlString,
            MediaType.TEXT_HTML_VALUE, random.nextLong());

      when(resourceMetadataProvider.getResourceDetails(eq(rootResourceUrl)))
            .thenReturn(expectedDocumentDetails);

      // embedded resources predicate
      ResourceDetails expEmbeddedResourceDetails = new ResourceDetails(
            embeddedResourceUrlString,
            "application/javascript",
            random.nextLong());

      when(documentParser.getEmbeddedResourcesLinks(eq(rootResourceUrlString), eq(resourceFilters)))
            .thenReturn(Collections.singletonList(expEmbeddedResourceDetails.getUrl()));

      when(resourceMetadataProvider.getResourceDetails(eq(embeddedResourceUrl)))
            .thenReturn(expEmbeddedResourceDetails);


      // FUNCTIONALITY
      SizeCheckReport actAnalysisReport = downloadSizeCheckerService.getAnalysisReport(rootResourceUrlString, resourceFilterCode);

      // TESTS
      verify(resourceMetadataProvider).getResourceDetails(eq(rootResourceUrl));
      verify(resourceMetadataProvider).getResourceDetails(eq(embeddedResourceUrl));
      verify(documentParser).getEmbeddedResourcesLinks(eq(rootResourceUrlString), eq(resourceFilters));

      verifyNoMoreInteractions(resourceMetadataProvider, documentParser);

      assertNotNull(actAnalysisReport);
      assertEquals(2, actAnalysisReport.getTotalRequestsMade()); // 1 document + 1 resource requests
      assertEquals(expectedDocumentDetails, actAnalysisReport.getRootResourceDetails());

      List<ResourceDetails> actEmbeddedResourcesDetails = actAnalysisReport.getEmbeddedResourcesDetails();
      assertEquals(1, actEmbeddedResourcesDetails.size());

      ResourceDetails actEmbeddedResource = actEmbeddedResourcesDetails.iterator().next();
      assertEquals(expEmbeddedResourceDetails, actEmbeddedResource);

      long expectedTotalBytesToDownload = expectedDocumentDetails.getBytesForDownload() + expEmbeddedResourceDetails.getBytesForDownload();
      assertEquals(expectedTotalBytesToDownload, actAnalysisReport.getTotalBytesForDownload());
   }

   @Test
   public void testGetAnalysisReportForEmptyDocument() throws Exception {
      // PREDICATE
      Random random = new Random();
      ResourceDetails expectedDocumentDetails = new ResourceDetails(rootResourceUrlString,
            MediaType.TEXT_HTML_VALUE, random.nextLong());

      when(resourceMetadataProvider.getResourceDetails(eq(rootResourceUrl)))
            .thenReturn(expectedDocumentDetails);

      // FUNCTIONALITY
      SizeCheckReport actAnalysisReport = downloadSizeCheckerService.getAnalysisReport(rootResourceUrlString, resourceFilterCode);

      // TESTS
      verify(resourceMetadataProvider, only()).getResourceDetails(eq(rootResourceUrl));
      verify(documentParser, only()).getEmbeddedResourcesLinks(eq(rootResourceUrlString), eq(resourceFilters));

      assertNotNull(actAnalysisReport);
      assertEquals(1, actAnalysisReport.getTotalRequestsMade()); // 1 document requests
      assertEquals(expectedDocumentDetails, actAnalysisReport.getRootResourceDetails());
      assertEquals(expectedDocumentDetails.getBytesForDownload(), actAnalysisReport.getTotalBytesForDownload());

      assertTrue(CollectionUtils.isEmpty(actAnalysisReport.getEmbeddedResourcesDetails()));
   }
}