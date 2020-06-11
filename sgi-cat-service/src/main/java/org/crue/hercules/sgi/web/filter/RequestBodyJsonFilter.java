package org.crue.hercules.sgi.web.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.crue.hercules.sgi.web.http.MultiReadEnabledHttpRequest;
import org.springframework.web.filter.GenericFilterBean;

/**
 * RequestBodyJsonFilter
 * 
 * Wraps a {@link ServletRequest} in a
 * {@link javax.servlet.http.HttpServletRequestWrapper}
 */
public class RequestBodyJsonFilter extends GenericFilterBean {
  @Override
  public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
      throws IOException, ServletException {
    HttpServletRequest currentRequest = (HttpServletRequest) servletRequest;
    MultiReadEnabledHttpRequest multiReadEnabledHttpRequest = new MultiReadEnabledHttpRequest(currentRequest);
    chain.doFilter(multiReadEnabledHttpRequest, servletResponse);
  }
}