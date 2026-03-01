package io.github.shurupov.logstamp.extractor;

import java.util.Map;

public interface StampExtractor<T> {
  boolean canExtract(Object container);
  default Map<String, String> extract(Object container) {
    return typedExtract((T) container);
  }

  Map<String, String> typedExtract(T container);
}
