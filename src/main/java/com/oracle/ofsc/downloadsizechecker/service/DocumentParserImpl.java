package com.oracle.ofsc.downloadsizechecker.service;

import com.oracle.ofsc.downloadsizechecker.model.ResourceFilter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DocumentParserImpl implements DocumentParser {

   /**
    * Key to get absolute URL of src attribute.
    */
   private static final String ABSOLUTE_SRC_KEY = "abs:src";

   /**
    * Key to get absolute URL of href attribute.
    */
   private static final String ABSOLUTE_HREF_KEY = "abs:href";

   /**
    * Selector to get font {@link Elements} with URLs.
    */
   private static final String FONT_LINK_SELECTOR = "link[href][type^='font/']";

   /**
    * Selector to get stylesheet {@link Elements} with URLs.
    */
   private static final String STYLESHEET_LINK_SELECTOR = "link[href][rel='stylesheet']";

   /**
    * Selector to get script {@link Elements} with URLs.
    */
   private static final String SCRIPT_LINK_SELECTOR = "script[src]";

   /**
    * Selector to get icon {@link Elements} with URLs.
    */
   private static final String ICON_LINK_SELECTOR = "link[href][rel*='icon']";

   /**
    * Selector to get images {@link Elements} with URLs.
    */
   private static final String IMAGE_LINK_SELECTOR = "img[src]";


   @Override
   public List<String> getEmbeddedResourcesLinks(String documentUrl, List<ResourceFilter> resourceFilters) throws IOException {
      Document document = Jsoup.connect(documentUrl).get();

      return getEmbeddedResourcesLinksImpl(resourceFilters, document);
   }

   private List<String> getEmbeddedResourcesLinksImpl(List<ResourceFilter> resourceFilters, Document document) {
      List<String> resourcesUrls = new ArrayList<>();

      if (isFilterAllOrSpecific(resourceFilters, ResourceFilter.IMAGE)) {
         List<String> imagesLinks = getResourceUrls(document, IMAGE_LINK_SELECTOR, ABSOLUTE_SRC_KEY);
         resourcesUrls.addAll(imagesLinks);

         List<String> iconsLinks = getResourceUrls(document, ICON_LINK_SELECTOR, ABSOLUTE_HREF_KEY);
         resourcesUrls.addAll(iconsLinks);
      }

      if (isFilterAllOrSpecific(resourceFilters, ResourceFilter.SCRIPT)) {
         List<String> scriptsLinks = getResourceUrls(document, SCRIPT_LINK_SELECTOR, ABSOLUTE_SRC_KEY);
         resourcesUrls.addAll(scriptsLinks);
      }

      if (isFilterAllOrSpecific(resourceFilters, ResourceFilter.STYLE)) {
         List<String> stylesLinks = getResourceUrls(document, STYLESHEET_LINK_SELECTOR, ABSOLUTE_HREF_KEY);
         resourcesUrls.addAll(stylesLinks);
      }

      if (isFilterAllOrSpecific(resourceFilters, ResourceFilter.FONT)) {
         List<String> fontsLinks = getResourceUrls(document, FONT_LINK_SELECTOR, ABSOLUTE_HREF_KEY);
         resourcesUrls.addAll(fontsLinks);
      }

      return resourcesUrls;
   }

   /**
    * Returns true if <code>resourceFilters</code> contains {@link ResourceFilter#ALL}
    * or the specified one.
    *
    * @param resourceFilters filters for embedded resources
    * @param specificFilter  specific filter for embedded resources
    * @return true if <code>resourceFilters</code> contains {@link ResourceFilter#ALL}
    * or the specified one
    */
   private boolean isFilterAllOrSpecific(List<ResourceFilter> resourceFilters, ResourceFilter specificFilter) {

      return resourceFilters.contains(ResourceFilter.ALL) ||
            resourceFilters.contains(specificFilter);
   }

   private List<String> getResourceUrls(Document document, String elementSelector, String attributeKey) {
      Elements resourceElements = document.select(elementSelector);

      return resourceElements.stream()
            .map(element -> element.attr(attributeKey))
            .collect(Collectors.toList());
   }
}
