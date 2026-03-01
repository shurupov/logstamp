package io.github.shurupov.logstamp.interceptor.pass;

import static io.github.shurupov.logstamp.core.StampConverter.camelToKebabCase;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import io.github.shurupov.logstamp.core.StampContext;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ContextFeignRequestInterceptor implements RequestInterceptor {

  @Value("${logstamp.http-header-name-prefix:x-stamp-}")
  private final String headerNamePrefix;
  private final StampContext stampContext;

  @Override
  public void apply(RequestTemplate template) {
    for (Map.Entry<String, String> contextEntry : stampContext.get().entrySet()) {
      template.header(
          headerNamePrefix + camelToKebabCase(contextEntry.getKey()),
          contextEntry.getValue()
      );
    }
  }
}
