package org.crue.hercules.sgi.web.method.support;

import java.io.IOException;
import java.lang.reflect.Type;
import java.security.InvalidParameterException;
import java.util.List;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.MissingNode;

import org.crue.hercules.sgi.web.bind.annotation.RequestBodyJsonArgument;
import org.crue.hercules.sgi.web.http.MultiReadEnabled;
import org.crue.hercules.sgi.web.http.MultiReadEnabledHttpRequest;
import org.springframework.core.Conventions;
import org.springframework.core.MethodParameter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.mvc.method.annotation.AbstractMessageConverterMethodArgumentResolver;

/**
 * RequestBodyJsonArgumentResolver
 * 
 * {@link org.springframework.web.method.support.HandlerMethodArgumentResolver}
 * for {@link RequestBodyJsonArgumentResolver} annotations.
 */
public class RequestBodyJsonArgumentResolver extends AbstractMessageConverterMethodArgumentResolver {
  ObjectMapper mapper = new ObjectMapper();

  public RequestBodyJsonArgumentResolver(List<HttpMessageConverter<?>> converters) {
    super(converters);
  }

  /**
   * Whether the given {@linkplain MethodParameter method parameter} is supported
   * by this resolver.
   * 
   * @param parameter the method parameter to check
   * @return {@code true} if this resolver supports the supplied parameter;
   *         {@code false} otherwise
   */
  public boolean supportsParameter(MethodParameter parameter) {
    return parameter.hasParameterAnnotation(RequestBodyJsonArgument.class);
  }

  /**
   * Resolves a method parameter into an argument value from a given request. A
   * {@link ModelAndViewContainer} provides access to the model for the request. A
   * {@link WebDataBinderFactory} provides a way to create a {@link WebDataBinder}
   * instance when needed for data binding and type conversion purposes.
   * 
   * @param parameter     the method parameter to resolve. This parameter must
   *                      have previously been passed to
   *                      {@link #supportsParameter} which must have returned
   *                      {@code true}.
   * @param mavContainer  the ModelAndViewContainer for the current request
   * @param webRequest    the current request
   * @param binderFactory a factory for creating {@link WebDataBinder} instances
   * @return the resolved argument value, or {@code null} if not resolvable
   * @throws Exception in case of errors with the preparation of argument values
   */
  public Object resolveArgument(MethodParameter parameter, @Nullable ModelAndViewContainer mavContainer,
      NativeWebRequest webRequest, @Nullable WebDataBinderFactory binderFactory) throws Exception {
    parameter = parameter.nestedIfOptional();

    ServletRequest originalServletRequest = webRequest.getNativeRequest(HttpServletRequest.class);
    if (MultiReadEnabled.class.isAssignableFrom(originalServletRequest.getClass())) {
      ((MultiReadEnabled) originalServletRequest).enableMultiRead();
    } else {
      throw new InvalidParameterException(
          "Request must be MultiReadEnabledHttpRequest.  Please, register RequestBodyJsonFilter Filter class.");
    }

    Object arg = this.readWithMessageConverters(webRequest, parameter, parameter.getNestedGenericParameterType());
    String name = Conventions.getVariableNameForParameter(parameter);
    if (binderFactory != null) {
      WebDataBinder binder = binderFactory.createBinder(webRequest, arg, name);
      if (arg != null) {
        this.validateIfApplicable(binder, parameter);
        if (binder.getBindingResult().hasErrors() && this.isBindExceptionRequired(binder, parameter)) {
          throw new MethodArgumentNotValidException(parameter, binder.getBindingResult());
        }
      }

      if (mavContainer != null) {
        mavContainer.addAttribute(BindingResult.MODEL_KEY_PREFIX + name, binder.getBindingResult());
      }
    }

    return this.adaptArgumentIfNecessary(arg, parameter);
  }

  /**
   * Create the method argument value of the expected parameter type by reading
   * from the given HttpInputMessage.
   * 
   * @param <T>        the expected type of the argument value to be created
   * @param webRequest the current request
   * @param parameter  the method parameter descriptor
   * @param paramType  the method parameter type
   * @return the created method argument value
   * @throws IOException                        if the reading from the request
   *                                            fails
   * @throws HttpMediaTypeNotSupportedException if no suitable message converter
   *                                            is found
   */
  protected <T> Object readWithMessageConverters(NativeWebRequest webRequest, MethodParameter parameter, Type paramType)
      throws IOException, HttpMediaTypeNotSupportedException, HttpMessageNotReadableException {
    HttpServletRequest originalServletRequest = (HttpServletRequest) webRequest
        .getNativeRequest(HttpServletRequest.class);
    HttpServletRequest servletRequest = servletRequestJsonParameterExtractor(originalServletRequest,
        parameter.getParameterName());
    Assert.state(servletRequest != null, "No HttpServletRequest");
    ServletServerHttpRequest inputMessage = new ServletServerHttpRequest(servletRequest);
    Object arg = this.readWithMessageConverters(inputMessage, parameter, paramType);
    if (arg == null && this.checkRequired(parameter)) {
      throw new HttpMessageNotReadableException(
          "Required request body is missing: " + parameter.getExecutable().toGenericString(), inputMessage);
    } else {
      return arg;
    }
  }

  /**
   * Checks if the parameter is required
   * 
   * @param parameter the method parameter descriptor
   * @return {@code true} if this parameter is required; {@code false} otherwise
   */
  protected boolean checkRequired(MethodParameter parameter) {
    RequestBodyJsonArgument jsonArgument = (RequestBodyJsonArgument) parameter
        .getParameterAnnotation(RequestBodyJsonArgument.class);
    return jsonArgument != null && jsonArgument.required() && !parameter.isOptional();
  }

  /**
   * Given a JSON request content, only the named element inside the JSON content
   * is returned.
   * 
   * @param servletRequest the current request
   * @param name           the JSON named property to extract
   * @return HttpServletRequest the wrapped request wich content is the named
   *         element
   * @throws IOException if the reading from the request fails
   */
  protected HttpServletRequest servletRequestJsonParameterExtractor(HttpServletRequest servletRequest, String name)
      throws IOException {
    MultiReadEnabledHttpRequest wrappedRequest = new MultiReadEnabledHttpRequest(servletRequest, true) {
      @Override
      public String getContent() throws Exception {
        String originalBody = super.getContent();
        JsonNode rootNode = mapper.readTree(originalBody);
        JsonNode subtree = rootNode.path(name);
        if (subtree == null || subtree instanceof MissingNode) {
          return "";
        }
        String newBody = mapper.writer().writeValueAsString(subtree);
        return newBody;
      }
    };

    return wrappedRequest;
  }

}
