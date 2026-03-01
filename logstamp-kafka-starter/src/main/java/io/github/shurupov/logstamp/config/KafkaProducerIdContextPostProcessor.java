package io.github.shurupov.logstamp.config;

import io.github.shurupov.logstamp.interceptor.pass.KafkaProducerStampInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaProducerIdContextPostProcessor implements BeanPostProcessor {

  private final KafkaProducerStampInterceptor interceptor;

  @Override
  public Object postProcessAfterInitialization(Object bean, String beanName) {
    if (bean instanceof KafkaTemplate) {
      KafkaTemplate<?, ?> kafkaTemplate = (KafkaTemplate<?, ?>) bean;
      kafkaTemplate.setProducerInterceptor(interceptor);
    }
    return bean;
  }
}
