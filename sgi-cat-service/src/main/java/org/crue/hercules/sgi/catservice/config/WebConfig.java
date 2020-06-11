package org.crue.hercules.sgi.catservice.config;

import java.util.List;

import javax.servlet.ServletRequest;

import org.crue.hercules.sgi.web.filter.RequestBodyJsonFilter;
import org.crue.hercules.sgi.web.method.support.RequestBodyJsonArgumentResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

/**
 * WebConfig
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

  @Autowired
  List<HttpMessageConverter<?>> converters;

  /**
   * RequestBodyJsonFilter
   * 
   * Wraps a {@link ServletRequest} in a
   * {@link javax.servlet.http.HttpServletRequestWrapper}
   * 
   * @return RequestBodyJsonFilter
   */
  @Bean
  public RequestBodyJsonFilter requestBodyJsonFilter() {
    return new RequestBodyJsonFilter();
  }

  /**
   * Add resolvers to support custom controller method argument types.
   * <p>
   * This does not override the built-in support for resolving handler method
   * arguments. To customize the built-in support for argument resolution,
   * configure {@link RequestMappingHandlerAdapter} directly.
   * 
   * @param argumentResolvers initially an empty list
   */
  @Override
  public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
    argumentResolvers.add(new RequestBodyJsonArgumentResolver(converters));
  }
}