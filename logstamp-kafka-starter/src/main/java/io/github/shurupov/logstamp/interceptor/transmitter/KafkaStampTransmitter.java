package io.github.shurupov.logstamp.interceptor.transmitter;

import io.github.shurupov.logstamp.core.StampContext;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerInterceptor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaStampTransmitter<K, V> implements ProducerInterceptor<K, V> {

  private final StampContext stampContext;

  @Override
  public ProducerRecord<K, V> onSend(ProducerRecord<K, V> record) {
    for (Map.Entry<String, String> contextEntry : stampContext.get().entrySet()) {
      record.headers().add(contextEntry.getKey(), contextEntry.getValue().getBytes(StandardCharsets.UTF_8));
    }
    return record;
  }

  @Override
  public void onAcknowledgement(RecordMetadata metadata, Exception exception) {

  }

  @Override
  public void close() {

  }

  @Override
  public void configure(Map<String, ?> configs) {

  }
}
