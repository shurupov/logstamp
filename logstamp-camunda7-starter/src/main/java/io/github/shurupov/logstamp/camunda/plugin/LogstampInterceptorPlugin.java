package io.github.shurupov.logstamp.camunda.plugin;

import io.github.shurupov.logstamp.camunda.delegate.LogstampDelegateInterceptor;
import io.github.shurupov.logstamp.core.StampContext;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.camunda.bpm.engine.impl.cfg.ProcessEnginePlugin;

@RequiredArgsConstructor
public class LogstampInterceptorPlugin implements ProcessEnginePlugin {

  private final StampContext stampContext;

  @Override
  public void preInit(ProcessEngineConfigurationImpl config) {
    config.setDelegateInterceptor(new LogstampDelegateInterceptor(stampContext));
  }

  @Override
  public void postInit(ProcessEngineConfigurationImpl config) {
  }

  @Override
  public void postProcessEngineBuild(ProcessEngine engine) {
  }
}
