package org.crue.hercules.sgi.catservice.view;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * CatServiceError
 */
@Data
@AllArgsConstructor
public class CatServiceError implements Serializable {

  /** Serial version. */
  private static final long serialVersionUID = 1L;

  /** HTTP status code. */
  @JsonSerialize
  private HttpStatus status;

  /** Mensaje de error. */
  private String message;

  /** Lista de errores concreto. */
  private List<String> errors;

  /**
   * Constructor con un solo error.
   * 
   * @param status  {@link HttpStatus}.
   * @param message mensaje de error.
   * @param error   descripción del error concreto.
   */
  public CatServiceError(HttpStatus status, String message, String error) {
    super();
    this.status = status;
    this.message = message;
    this.errors = Arrays.asList(error);
  }

}
