package io.github.shurupov.logstamp.extractor;

import static io.github.shurupov.logstamp.core.StampConverter.kebabToCamelCase;

import io.github.shurupov.logstamp.CachedBodyHttpServletRequest;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DefaultHttpRequestStampExtractor implements HttpRequestStampExtractor {

  @Value("${logstamp.http-header-name-prefix:x-stamp-}")
  private final String headerNamePrefix;

  @Override
  public Map<String, String> typedExtract(CachedBodyHttpServletRequest request) {
    Map<String, String> identifiers = new HashMap<>();
    for (String headerName: Collections.list(request.getHeaderNames())) {
      if (!headerName.startsWith(headerNamePrefix)) {
        continue;
      }
      extractFromHeader(identifiers, headerName, request);
    }
    return identifiers;
  }

  private void extractFromHeader(Map<String, String> identifiers, String headerName, HttpServletRequest request) {
    String headerValue = request.getHeader(headerName);
    String contextEntryName = kebabToCamelCase(headerName.substring(headerNamePrefix.length()));
    identifiers.put(contextEntryName, headerValue);
  }
}
