package org.crue.hercules.sgi.catservice.exceptions;

/**
 * TipoFungibleNotFoundException
 */
public class TipoFungibleNotFoundException extends CatServiceNotFoundException {
  /**
  *
  */
  private static final long serialVersionUID = 1L;

  public TipoFungibleNotFoundException(Long tipoFungibleId) {
    super("TipoFungible " + tipoFungibleId + " does not exist.");
  }
}