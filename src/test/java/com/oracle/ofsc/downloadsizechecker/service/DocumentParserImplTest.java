package com.oracle.ofsc.downloadsizechecker.service;

import com.oracle.ofsc.downloadsizechecker.TestUtility;
import com.oracle.ofsc.downloadsizechecker.model.ResourceFilter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DocumentParserImplTest {

   @Autowired
   private DocumentParser documentParser;

   private String documentWithContentUrl;
   private String emptyDocumentUrl;

   @LocalServerPort
   private int randomServerPort;

   private List<String> expectedImages;
   private List<String> expectedStyles;
   private List<String> expectedScripts;
   private List<String> expectedFonts;
   private List<String> expectedAllResources;

   @Before
   public void initUrl() throws IOException {
      String testPageUrl = TestUtility.constructTestResourceUrl(randomServerPort, "testPage.html");
      documentWithContentUrl = new URL(testPageUrl).toString();

      String testEmptyPageUrl = TestUtility.constructTestResourceUrl(randomServerPort, "testEmptyPage.html");
      emptyDocumentUrl = new URL(testEmptyPageUrl).toString();

      expectedAllResources = new ArrayList<>();

      expectedImages = Arrays.asList(
            "https://developer.mozilla.org/static/img/favicon144.e7e21ca263ca.png",
            "https://developer.mozilla.org/static/img/favicon114.d526f38b09c5.png",
            "https://developer.mozilla.org/static/img/favicon72.cc65d1d762a0.png",
            "https://developer.mozilla.org/static/img/favicon57.de33179910ae.png",
            "https://developer.mozilla.org/static/img/favicon32.7f3da72dcea1.png"
      );
      expectedAllResources.addAll(expectedImages);

      expectedStyles = Arrays.asList(
            "https://developer.mozilla.org/static/build/styles/mdn.0fe6d2a68b73.css",
            "https://developer.mozilla.org/static/build/styles/home.af64ab7320bf.css"
      );
      expectedAllResources.addAll(expectedStyles);

      expectedScripts = Arrays.asList(
            "https://cdn.speedcurve.com/js/lux.js?id=108906238",
            "https://developer.mozilla.org/static/jsi18n/ru/javascript.d69af44e1987.js",
            "https://developer.mozilla.org/static/build/js/main.b0f831aaadf5.js",
            "https://developer.mozilla.org/static/build/js/newsletter.532963e204cb.js"
      );
      expectedAllResources.addAll(expectedScripts);

      expectedFonts = Collections.singletonList(
            "https://developer.mozilla.org/static/fonts/locales/ZillaSlab-Regular.subset.bbc33fb47cf6.woff2"
      );
      expectedAllResources.addAll(expectedFonts);
   }

   @Test
   public void testGetEmbeddedResourcesLinksFonts() throws Exception {
      // PREDICATE

      // FUNCTIONALITY // TESTS
      assertEmbeddedResourcesLinks(expectedFonts, documentWithContentUrl, Collections.singletonList(ResourceFilter.FONT));
      assertEmbeddedResourcesLinks(Collections.emptyList(), emptyDocumentUrl, Collections.singletonList(ResourceFilter.FONT));
   }

   @Test
   public void testGetEmbeddedResourcesLinksScripts() throws Exception {
      // PREDICATE

      // FUNCTIONALITY //TESTS
      assertEmbeddedResourcesLinks(expectedScripts, documentWithContentUrl, Collections.singletonList(ResourceFilter.SCRIPT));
      assertEmbeddedResourcesLinks(Collections.emptyList(), emptyDocumentUrl, Collections.singletonList(ResourceFilter.SCRIPT));
   }

   @Test
   public void testGetEmbeddedResourcesLinksStyles() throws Exception {
      // PREDICATE

      // FUNCTIONALITY //TESTS
      assertEmbeddedResourcesLinks(expectedStyles, documentWithContentUrl, Collections.singletonList(ResourceFilter.STYLE));
      assertEmbeddedResourcesLinks(Collections.emptyList(), emptyDocumentUrl, Collections.singletonList(ResourceFilter.STYLE));
   }

   @Test
   public void testGetEmbeddedResourcesLinksImages() throws Exception {
      // PREDICATE

      // FUNCTIONALITY //TESTS
      assertEmbeddedResourcesLinks(expectedImages, documentWithContentUrl, Collections.singletonList(ResourceFilter.IMAGE));
      assertEmbeddedResourcesLinks(Collections.emptyList(), emptyDocumentUrl, Collections.singletonList(ResourceFilter.IMAGE));
   }

   @Test
   public void testGetEmbeddedResourcesLinksAll() throws Exception {
      //PREDICATE
      List<ResourceFilter> allFilters = Arrays.asList(ResourceFilter.IMAGE,
            ResourceFilter.SCRIPT,
            ResourceFilter.STYLE,
            ResourceFilter.FONT);

      //FUNCTIONALITY //TESTS
      assertEmbeddedResourcesLinks(expectedAllResources, documentWithContentUrl, Collections.singletonList(ResourceFilter.ALL));
      assertEmbeddedResourcesLinks(expectedAllResources, documentWithContentUrl, allFilters);

      assertEmbeddedResourcesLinks(Collections.emptyList(), emptyDocumentUrl, Collections.singletonList(ResourceFilter.ALL));
      assertEmbeddedResourcesLinks(Collections.emptyList(), emptyDocumentUrl, allFilters);
   }

   private void assertEmbeddedResourcesLinks(List<String> expectedLinks, String documentUrl, List<ResourceFilter> resourceFilters) throws IOException {
      // FUNCTIONALITY
      List<String> actualLinks = documentParser.getEmbeddedResourcesLinks(documentUrl, resourceFilters);

      // TESTS
      assertEquals(expectedLinks.size(), actualLinks.size());
      assertTrue(expectedLinks.containsAll(actualLinks));
      assertTrue(actualLinks.containsAll(expectedLinks));
   }
}