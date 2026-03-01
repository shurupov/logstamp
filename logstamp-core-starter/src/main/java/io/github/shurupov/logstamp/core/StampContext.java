package io.github.shurupov.logstamp.core;

import io.github.shurupov.logstamp.extractor.StampExtractor;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class StampContext {

  private static final String INITIATOR_FIELD_NAME = "initiator";

  @Value("stamplog.log-field-prefix:")
  private final String stampLogFieldPrefix;
  private final ThreadLocal<Map<String, String>> context = new ThreadLocal<>();
  private final List<StampExtractor<?>> stampExtractors;

  public void addInitiator(String initiator) {
    MDC.put(stampLogFieldPrefix + INITIATOR_FIELD_NAME, initiator);
  }

  public Map<String, String> get() {
    if (context.get() == null) {
      context.set(new HashMap<>());
    }
    return context.get();
  }

  public void add(Map<String, String> context) {
    for (Map.Entry<String, String> contextEntry : context.entrySet()) {
      add(contextEntry.getKey(), contextEntry.getValue());
    }
  }

  public void add(String name, String value) {
    get().put(name, value);
    MDC.put(stampLogFieldPrefix + name, value);
  }

  public void clear() {
    for (String contextEntryKey : get().keySet()) {
      MDC.remove(stampLogFieldPrefix + contextEntryKey);
    }
    MDC.remove(stampLogFieldPrefix + INITIATOR_FIELD_NAME);
    context.remove();
  }

  public void addIdentifiers(Object event) {
    for (StampExtractor<?> extractor : stampExtractors) {
      if (extractor.canExtract(event)) {
        try {
          Map<String, String> extracted = extractor.extract(event);
          add(extracted);
        } catch (Exception e) {
          log.warn("Failed to extract stamp", e);
        }
      }
    }
  }
}
