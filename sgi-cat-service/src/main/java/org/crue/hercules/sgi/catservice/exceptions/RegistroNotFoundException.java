package org.crue.hercules.sgi.catservice.exceptions;

/**
 * RegistroNotFoundException
 */
public class RegistroNotFoundException extends CatServiceNotFoundException {

  /**
   * Serial version.
   */
  private static final long serialVersionUID = 1L;

  public RegistroNotFoundException(Long registroId) {
    super("Registro " + registroId + " does not exist.");
  }

}