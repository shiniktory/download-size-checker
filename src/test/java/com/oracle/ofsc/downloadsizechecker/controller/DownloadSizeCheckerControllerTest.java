package com.oracle.ofsc.downloadsizechecker.controller;

import com.oracle.ofsc.downloadsizechecker.TestUtility;
import com.oracle.ofsc.downloadsizechecker.model.SizeCheckReport;
import com.oracle.ofsc.downloadsizechecker.service.DownloadSizeCheckerService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

import static com.oracle.ofsc.downloadsizechecker.TestUtility.constructRandomResourceUrl;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DownloadSizeCheckerControllerTest {

   @MockBean
   private DownloadSizeCheckerService downloadSizeCheckerService;

   private TestRestTemplate testRestTemplate = new TestRestTemplate();

   @LocalServerPort
   private int applicationPort;

   private String rootUrl;

   @Before
   public void setUp() {
      rootUrl = "http://localhost:" + applicationPort;
   }

   @Test
   public void testAnalyzeDownloadSize() throws Exception {
      // PREDICATE

      String resourceUrl = constructRandomResourceUrl();
      SizeCheckReport expSizeCheckReport = TestUtility.constructRandomSizeCheckReport(resourceUrl);
      String resourceFilter = "*";

      when(downloadSizeCheckerService.getAnalysisReport(eq(resourceUrl), eq(resourceFilter)))
            .thenReturn(expSizeCheckReport);

      Map<String, String> params = new HashMap<>();
      params.put("url", resourceUrl);
      params.put("filter", resourceFilter);

      // FUNCTIONALITY
      SizeCheckReport actSizeCheckReport = testRestTemplate.getForObject(rootUrl + "/checker/analyze?url={url}&filter={filter}",
            SizeCheckReport.class, params);

      // TESTS
      assertNotNull(actSizeCheckReport);
      assertEquals(expSizeCheckReport, actSizeCheckReport);
   }
}