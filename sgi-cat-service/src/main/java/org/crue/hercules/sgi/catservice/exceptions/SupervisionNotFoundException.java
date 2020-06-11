package org.crue.hercules.sgi.catservice.exceptions;

/**
 * SupervisionNotFoundException
 */
public class SupervisionNotFoundException extends CatServiceNotFoundException {

  /**
  *
  */
  private static final long serialVersionUID = 1L;

  public SupervisionNotFoundException(Long supervisionId) {
    super("Supervision " + supervisionId + " does not exist.");
  }
}