package io.github.shurupov.logstamp.config;

import io.github.shurupov.logstamp.camunda.plugin.LogstampInterceptorPlugin;
import io.github.shurupov.logstamp.core.StampContext;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.impl.cfg.ProcessEnginePlugin;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class CamundaPluginConfig {

  @Bean
  public ProcessEnginePlugin startProcessInterceptorPlugin(StampContext stampContext) {
    return new LogstampInterceptorPlugin(stampContext);
  }
}
