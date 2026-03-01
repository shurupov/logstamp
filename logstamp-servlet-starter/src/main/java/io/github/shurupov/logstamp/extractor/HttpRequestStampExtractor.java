package io.github.shurupov.logstamp.extractor;


import io.github.shurupov.logstamp.CachedBodyHttpServletRequest;

public interface HttpRequestStampExtractor extends StampExtractor<CachedBodyHttpServletRequest> {

  @Override
  default boolean canExtract(Object container) {
    return container instanceof CachedBodyHttpServletRequest;
  }
}
