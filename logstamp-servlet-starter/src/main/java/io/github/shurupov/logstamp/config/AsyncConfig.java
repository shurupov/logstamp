package io.github.shurupov.logstamp.config;

import java.util.concurrent.Executor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.AsyncAnnotationBeanPostProcessor;
import org.springframework.scheduling.annotation.AsyncConfigurer;

@Configuration
@Slf4j
@RequiredArgsConstructor
@ConditionalOnBean(value = AsyncAnnotationBeanPostProcessor.class)
public class AsyncConfig implements AsyncConfigurer {

    @Qualifier("asyncTaskExecutor")
    private final TaskExecutor asyncTaskExecutor;

    @Override
    public Executor getAsyncExecutor() {
        return asyncTaskExecutor;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return (e, method, params) -> {
            log.error("Failed in async method {}", method.getName(), e);
        };
    }
}
