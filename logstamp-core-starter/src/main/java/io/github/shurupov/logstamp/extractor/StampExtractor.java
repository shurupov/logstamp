package io.github.shurupov.logstamp.extractor;

import java.util.Map;

public interface StampExtractor<T> {
  boolean canExtract(Object container) throws Exception;
  default Map<String, String> extract(Object container) throws Exception {
    return typedExtract((T) container);
  }

  Map<String, String> typedExtract(T container) throws Exception;
}
