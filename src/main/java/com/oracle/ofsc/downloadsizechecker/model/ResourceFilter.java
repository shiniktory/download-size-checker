package com.oracle.ofsc.downloadsizechecker.model;


import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Contains supported resource filters for analysis.
 */
public enum ResourceFilter {

   /**
    * Any resource of the supported resources will be included.
    */
   ALL("*"),

   /**
    * Only font resources will be included.
    */
   FONT("font"),

   /**
    * Only image resources will be included.
    */
   IMAGE("img"),

   /**
    * Only script resources will be included.
    */
   SCRIPT("script"),

   /**
    * Only stylesheet resources will be included.
    */
   STYLE("style");

   /**
    * Short code to identify filter.
    */
   @Getter
   private String filterCode;

   ResourceFilter(String filterCode) {
      this.filterCode = filterCode;
   }

   /**
    * Returns {@link ResourceFilter} found by the specified <code>filterCode</code>.
    *
    * @param filterCode short code to find filter
    * @return {@link ResourceFilter} found by the specified <code>filterCode</code>
    * @throws UnsupportedOperationException if such {@link ResourceFilter} is not found
    */
   public static ResourceFilter findByFilter(String filterCode) {

      return Arrays.stream(values())
            .filter(resourceFilter -> resourceFilter.filterCode.equalsIgnoreCase(filterCode))
            .findFirst()
            .orElseThrow(() -> new UnsupportedOperationException("Filter " + filterCode + " is not supported"));
   }

   /**
    * Returns the list of {@link ResourceFilter}s respective to <code>resourceFilterString</code>
    * contains {@link ResourceFilter#filterCode}s separated by semicolon.
    *
    * @param resourceFilterString string contains {@link ResourceFilter#filterCode}s separated by semicolon
    * @return the list of {@link ResourceFilter}s respective to <code>resourceFilterString</code>
    */
   public static List<ResourceFilter> findByFilterQueryString(String resourceFilterString) {
      String[] filterCodes = StringUtils.split(resourceFilterString, ";");

      return Arrays.stream(filterCodes)
            .filter(StringUtils::isNotBlank)
            .map(ResourceFilter::findByFilter)
            .collect(Collectors.toList());
   }
}
