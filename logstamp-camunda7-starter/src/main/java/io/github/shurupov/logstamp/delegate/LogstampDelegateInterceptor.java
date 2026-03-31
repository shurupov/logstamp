package io.github.shurupov.logstamp.delegate;

import io.github.shurupov.logstamp.core.StampContext;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.BaseDelegateExecution;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.impl.delegate.DelegateInvocation;
import org.camunda.bpm.engine.impl.interceptor.DelegateInterceptor;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class LogstampDelegateInterceptor implements DelegateInterceptor {

  private static final String STAMP_CONTEXT_VARIABLE_NAME = "STAMP_CONTEXT";

  private final StampContext stampContext;

  @Override
  public void handleInvocation(DelegateInvocation delegateInvocation) throws Exception {
    BaseDelegateExecution execution = delegateInvocation.getContextExecution();
    try {
      Map<String, String> stampContextSnapShot = (Map<String, String>) execution.getVariable(STAMP_CONTEXT_VARIABLE_NAME);

      if (execution instanceof DelegateExecution delegateExecution) {
        stampContext.addInitiator("camunda task " + delegateExecution.getCurrentActivityName());
      } else {
        stampContext.addInitiator("camunda");
      }

      stampContext.add(stampContextSnapShot);
    } catch (Exception e) {
      log.warn("Failed to get stamp context", e);
    }
    delegateInvocation.proceed();
    try {
      execution.setVariable(STAMP_CONTEXT_VARIABLE_NAME, stampContext.get());
      stampContext.clear();
    } catch (Exception e) {
      log.warn("Failed to clear stamp context", e);
    }
  }
}
