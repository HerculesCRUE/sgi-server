package org.crue.hercules.sgi.catservice.enums;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * EstadoTipoReservableEnum.
 */
public enum EstadoTipoReservableEnum {

  /** ALTA. */
  ALTA("alta"),

  /** BAJA. */
  BAJA("baja");

  /**
   * The value.
   */
  private String value;

  /**
   * Private constructor to asign the value.
   *
   * @param the value.
   */
  private EstadoTipoReservableEnum(final String value) {
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