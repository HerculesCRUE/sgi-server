package org.crue.hercules.sgi.catservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import lombok.extern.slf4j.Slf4j;

/**
 * CorsConfig
 * 
 * Cross-origin resource sharing (CORS) configuration.
 */
@Configuration
@Slf4j
public class CorsConfig {

  /**
   * Global CORS configuration
   * 
   * @return WebMvcConfigurer
   * @see <a href=
   *      "https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#boot-features-cors">CORS</a>
   */
  @Bean
  public WebMvcConfigurer corsConfigurer() {
    return new WebMvcConfigurer() {
      @Override
      public void addCorsMappings(CorsRegistry registry) {
        log.debug("addCorsMappings(CorsRegistry registry) - start");
        registry.addMapping("/**").allowedMethods("GET", "POST", "PUT", "DELETE");
        log.debug("addCorsMappings(CorsRegistry registry) - end");
      }
    };
  }

}
