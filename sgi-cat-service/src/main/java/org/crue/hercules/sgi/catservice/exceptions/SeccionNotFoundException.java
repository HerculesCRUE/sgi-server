package org.crue.hercules.sgi.catservice.exceptions;

/**
 * SeccionNotFoundException
 */
public class SeccionNotFoundException extends CatServiceNotFoundException {

  /**
   * Serial version.
   */
  private static final long serialVersionUID = 1L;

  public SeccionNotFoundException(Long seccionId) {
    super("Seccion " + seccionId + " does not exist.");
  }

}