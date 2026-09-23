package io.github.shurupov.logstamp.extractor;


import org.springframework.web.util.ContentCachingRequestWrapper;

public interface HttpRequestStampExtractor extends StampExtractor<ContentCachingRequestWrapper> {

  @Override
  default boolean canExtract(Object container) {
    return container instanceof ContentCachingRequestWrapper;
  }
}
