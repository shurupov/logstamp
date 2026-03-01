package io.github.shurupov.logstamp.aspect;

import io.github.shurupov.logstamp.core.StampContext;
import java.lang.reflect.Method;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class AddStampsAspect {

  private final StampContext stampContext;

  @Around("@annotation(io.github.shurupov.logstamp.aspect.AddStamps)")
  public Object addCrossIdentifier(ProceedingJoinPoint joinPoint) throws Throwable {
    try {
      if (joinPoint.getArgs().length > 0) {
        Object argument = joinPoint.getArgs()[0];
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        AddStamps annotation = method.getAnnotation(AddStamps.class);
        stampContext.addInitiator(annotation.initiator());
        stampContext.addIdentifiers(argument);
      }
      return joinPoint.proceed();
    } finally {
      stampContext.clear();
    }
  }
}
