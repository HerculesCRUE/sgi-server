package org.crue.hercules.sgi.catservice.exceptions;

/**
 * UnidadMedidaNotFoundException
 */
public class UnidadMedidaNotFoundException extends CatServiceNotFoundException {

  /**
  *
  */
  private static final long serialVersionUID = 1L;

  public UnidadMedidaNotFoundException(Object unidadMedida) {
    super("UnidadMedida " + unidadMedida.toString() + " does not exist.");
  }

}