package io.github.shurupov.logstamp.interceptor.receiver;

import io.github.shurupov.logstamp.core.StampContext;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;

@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
@Component
@WebFilter(filterName = "extractStampFilter", urlPatterns = "/*")
@RequiredArgsConstructor
public class ExtractStampReceiver extends OncePerRequestFilter {

  private final StampContext stampContext;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {

    ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request);

    stampContext.addInitiator("rest");
    stampContext.addIdentifiers(requestWrapper);
    try {
      filterChain.doFilter(requestWrapper, response);
    } finally {
      stampContext.clear();
    }
  }
}
