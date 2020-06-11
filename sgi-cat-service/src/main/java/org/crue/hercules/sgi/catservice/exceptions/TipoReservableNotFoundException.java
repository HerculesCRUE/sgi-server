package org.crue.hercules.sgi.catservice.exceptions;

/**
 * TipoReservableNotFoundException
 */
public class TipoReservableNotFoundException extends CatServiceNotFoundException {

  /**
  *
  */
  private static final long serialVersionUID = 1L;

  public TipoReservableNotFoundException(Long tipoReservableId) {
    super("TipoReservable " + tipoReservableId + " does not exist.");
  }

}