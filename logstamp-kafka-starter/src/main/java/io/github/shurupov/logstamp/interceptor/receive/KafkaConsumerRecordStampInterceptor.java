package io.github.shurupov.logstamp.interceptor.receive;

import io.github.shurupov.logstamp.core.StampContext;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.listener.RecordInterceptor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaConsumerRecordStampInterceptor<K, V> implements RecordInterceptor<K, V> {

  private final StampContext stampContext;

  @Override
  public ConsumerRecord<K, V> intercept(ConsumerRecord<K, V> record, Consumer<K, V> consumer) {
    stampContext.addInitiator("kafka");
    stampContext.addIdentifiers(record);
    return record;
  }

  @Override
  public void afterRecord(ConsumerRecord<K, V> record, Consumer<K, V> consumer) {
    stampContext.clear();
  }
}
