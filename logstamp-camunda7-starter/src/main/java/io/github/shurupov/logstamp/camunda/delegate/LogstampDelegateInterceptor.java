package io.github.shurupov.logstamp.camunda.delegate;

import io.github.shurupov.logstamp.core.StampContext;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.BaseDelegateExecution;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.impl.delegate.DelegateInvocation;
import org.camunda.bpm.engine.impl.interceptor.DelegateInterceptor;

@Slf4j
@RequiredArgsConstructor
public class LogstampDelegateInterceptor implements DelegateInterceptor {

  private static final String STAMPS_VARIABLE_NAME = "LOG_STAMPS";

  private final StampContext stampContext;

  @Override
  public void handleInvocation(DelegateInvocation delegateInvocation) throws Exception {
    BaseDelegateExecution execution = delegateInvocation.getContextExecution();
    try {

      if (execution.hasVariable(STAMPS_VARIABLE_NAME)) {
        Map<String, String> stampContextSnapShot = (Map<String, String>) execution.getVariable(STAMPS_VARIABLE_NAME);
        stampContext.add(stampContextSnapShot);
      }

      if (execution instanceof DelegateExecution delegateExecution) {
        stampContext.addInitiator("camunda task " + delegateExecution.getCurrentActivityName());
      } else {
        stampContext.addInitiator("camunda");
      }
    } catch (Exception e) {
      log.warn("Failed to get stamp context", e);
    }
    try {
      delegateInvocation.proceed();
    } finally {
      try {
        execution.setVariable(STAMPS_VARIABLE_NAME, stampContext.get());
        stampContext.clear();
      } catch (Exception e) {
        log.warn("Failed to clear stamp context", e);
      }
    }

  }
}
