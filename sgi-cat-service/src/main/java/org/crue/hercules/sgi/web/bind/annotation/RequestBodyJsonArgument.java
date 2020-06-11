package org.crue.hercules.sgi.web.bind.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
/**
 * RequestBodyJsonArgument
 * 
 * This anotation allows to extract Controller arguments from RequestBody JSON
 * first-level elements.
 */
public @interface RequestBodyJsonArgument {

  /**
   * Whether argument content is required.
   * <p>
   * Default is {@code true}, leading to an exception thrown in case there is no
   * argument content. Switch this to {@code false} if you prefer {@code null} to
   * be passed when the parameter content is {@code null}.
   * 
   * @return Whether argument content is required
   */
  boolean required() default true;

}
