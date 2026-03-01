package io.github.shurupov.logstamp.config;

import io.github.shurupov.logstamp.core.StampContext;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.slf4j.MDC;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskDecorator;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.AsyncAnnotationBeanPostProcessor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

@Configuration
@RequiredArgsConstructor
@ConditionalOnBean(value = AsyncAnnotationBeanPostProcessor.class)
public class AsyncExecutorConfig {

  private final StampContext stampContext;

  @Bean(name = "asyncTaskExecutor")
  public TaskExecutor asyncTaskExecutor() {
    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    executor.setCorePoolSize(5);
    executor.setMaxPoolSize(20);
    executor.setQueueCapacity(100);
    executor.setThreadNamePrefix("Async-");
    executor.setTaskDecorator(new MdcTaskDecorator(stampContext));
    executor.initialize();
    return executor;
  }

  public static class MdcTaskDecorator implements TaskDecorator {

    private final StampContext contextHolder;

    public MdcTaskDecorator(StampContext crossIdHolder) {
      this.contextHolder = crossIdHolder;
    }

    @Override
    public Runnable decorate(Runnable runnable) {
      Map<String, String> contextMap = MDC.getCopyOfContextMap();
      RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
      final Map<String, String> crossIdContext = contextHolder.get();

      return () -> {
        try {
          RequestContextHolder.setRequestAttributes(requestAttributes);
          if (contextMap != null) {
            MDC.setContextMap(contextMap);
          }
          contextHolder.add(crossIdContext);
          runnable.run();
        } finally {
          MDC.clear();
          RequestContextHolder.resetRequestAttributes();
          contextHolder.clear();
        }
      };
    }
  }
}
