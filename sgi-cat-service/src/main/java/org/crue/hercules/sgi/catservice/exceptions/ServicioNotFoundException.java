package org.crue.hercules.sgi.catservice.exceptions;

/**
 * ServiceNotFoundException
 */
public class ServicioNotFoundException extends CatServiceNotFoundException {

  /**
   * Serial version.
   */
  private static final long serialVersionUID = 1L;

  public ServicioNotFoundException(Long servicioId) {
    super("Servicio " + servicioId + " does not exist.");
  }

}