package org.crue.hercules.sgi.catservice.filter;

import java.io.Serializable;
import java.util.Collection;

import org.crue.hercules.sgi.catservice.enums.EstadoRegistroEnum;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * RegistroFilter
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegistroFilter implements Serializable {

  /*** Serial version. */
  private static final long serialVersionUID = 1L;

  /** Id. */
  private Long id;

  /** Id collection. */
  private Collection<Long> ids;

  /** Referencia usuario. */
  private String usuarioRef;

  /** Id del servicio. */
  private String servicioId;

  /** Estado del registro. */
  private EstadoRegistroEnum estadoRegistro;

  /** Indiciador de entrega papel. */
  private Boolean entregaPapel;

  /** Indiciador de aceptación de condiciones. */
  private Boolean aceptaCondiciones;

  /** Observaciones del registro. */
  private String observaciones;

}