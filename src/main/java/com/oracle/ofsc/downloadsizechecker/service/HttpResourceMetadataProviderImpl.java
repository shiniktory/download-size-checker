package com.oracle.ofsc.downloadsizechecker.service;

import com.oracle.ofsc.downloadsizechecker.exception.ResourceAccessException;
import com.oracle.ofsc.downloadsizechecker.model.ResourceDetails;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;

@Service
public class HttpResourceMetadataProviderImpl implements ResourceMetadataProvider {

   @Override
   public ResourceDetails getResourceDetails(URL resourceUrl) {
      String fullUrlString = resourceUrl.toString();
      HttpURLConnection connection = null;

      try {
         connection = (HttpURLConnection) resourceUrl.openConnection();

         connection.setRequestMethod(HttpMethod.HEAD.name());
         connection.setRequestProperty("User-Agent", "DownloadSizeCheckerService");
         connection.setRequestProperty("Accept", "*/*");

         return new ResourceDetails(
               fullUrlString,
               connection.getContentType(),
               connection.getContentLengthLong()
         );
      } catch (IOException e) {
         throw new ResourceAccessException("Error during accessing the resource with URL: " + fullUrlString, e);
      } finally {
         closeConnection(connection);
      }
   }

   private void closeConnection(HttpURLConnection connection) {
      if (Objects.nonNull(connection)) {
         connection.disconnect();
      }
   }
}
