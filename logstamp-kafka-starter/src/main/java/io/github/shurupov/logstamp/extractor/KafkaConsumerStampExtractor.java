package io.github.shurupov.logstamp.extractor;

import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.header.Header;
import org.springframework.stereotype.Component;

@Component
public class KafkaConsumerStampExtractor implements StampExtractor<ConsumerRecord<Object, ?>> {

  @Override
  public boolean canExtract(Object container) {
    return container instanceof ConsumerRecord;
  }

  @Override
  public Map<String, String> typedExtract(ConsumerRecord<Object, ?> consumerRecord) {
    Map<String, String> extracted = new HashMap<>();

    for (Header header : consumerRecord.headers()) {
      if (header.key().startsWith("_")) {
        continue;
      }
      extracted.put(header.key(), new String(header.value()));
    }

    return extracted;
  }
}
