package org.crue.hercules.sgi.catservice.enums;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * EstadoRegistroEnum.
 */
public enum EstadoRegistroEnum {

  /** Activo. */
  ACTIVO("activo"),

  /** Inactivo. */
  INACTIVO("inactivo");

  /**
   * The value.
   */
  private String value;

  /**
   * Private constructor to asign the value.
   *
   * @param the value.
   */
  private EstadoRegistroEnum(final String value) {
    this.value = value;
  }

  /**
   * Return the value.
   *
   * @return the value
   */
  @JsonValue
  public String getValue() {
    return this.value;
  }

}