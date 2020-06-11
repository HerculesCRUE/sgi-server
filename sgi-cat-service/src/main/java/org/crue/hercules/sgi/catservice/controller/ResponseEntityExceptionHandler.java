package org.crue.hercules.sgi.catservice.controller;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import org.crue.hercules.sgi.catservice.exceptions.CatServiceNotFoundException;
import org.crue.hercules.sgi.catservice.view.CatServiceError;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

/**
 * ResponseEntityExceptionHandler
 */
@ControllerAdvice
public class ResponseEntityExceptionHandler
    extends org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler {

  /**
   * Let Spring BasicErrorController handle the exception, we just override the
   * status code
   * 
   * @param response the response with HttpStatus.NOT_FOUND
   * @throws IOException if there is some problem sending the error
   */
  @ExceptionHandler({ CatServiceNotFoundException.class })
  public void springHandleNotFound(HttpServletResponse response) throws IOException {
    response.sendError(HttpStatus.NOT_FOUND.value());
  }

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers,
      HttpStatus status, WebRequest request) {
    final List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
    final List<ObjectError> globalErrors = ex.getBindingResult().getGlobalErrors();

    List<String> errores = fieldErrors.stream().map(
        fieldError -> fieldError.getObjectName() + " " + fieldError.getField() + " " + fieldError.getDefaultMessage())
        .collect(Collectors.toList());

    errores.addAll(
        globalErrors.stream().map(globalError -> globalError.getObjectName() + " " + globalError.getDefaultMessage())
            .collect(Collectors.toList()));

    CatServiceError catServiceError = new CatServiceError(HttpStatus.BAD_REQUEST, "Error validación", errores);
    return new ResponseEntity<Object>(catServiceError, headers, status);
  }

  /**
   * Controla las validaciones hechas con {@link IllegalArgumentException}.
   * 
   * @param exception una {@link IllegalArgumentException}.
   * @return la respuesta con el mensaje de error
   * @throws IOException if there is some problem sending the error
   */
  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<Object> springHandleIllegalArgumentException(IllegalArgumentException exception)
      throws IOException {
    CatServiceError catServiceError = new CatServiceError(HttpStatus.BAD_REQUEST, "Error validación",
        exception.getMessage());
    return new ResponseEntity<Object>(catServiceError, new HttpHeaders(), catServiceError.getStatus());
  }

  /**
   * Si se produce cualquier excepcion que no este tratada en un handler
   * especifico se envia un error 500
   * 
   * @param exception una {@link Exception}.
   * @return la respuesta con el mensaje de error
   * @throws IOException if there is some problem sending the error
   */
  @ExceptionHandler(Exception.class)
  public ResponseEntity<Object> springHandleAll(Exception exception) throws IOException {
    CatServiceError catServiceError = new CatServiceError(HttpStatus.INTERNAL_SERVER_ERROR, "Error interno",
        exception.getMessage());
    return new ResponseEntity<Object>(catServiceError, new HttpHeaders(), catServiceError.getStatus());
  }

}