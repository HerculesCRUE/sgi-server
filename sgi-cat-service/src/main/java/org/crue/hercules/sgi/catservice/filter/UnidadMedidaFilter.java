package org.crue.hercules.sgi.catservice.filter;

import java.io.Serializable;
import java.util.Collection;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * UnidadMedidaFilter
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UnidadMedidaFilter implements Serializable {

  /*** Serial version. */
  private static final long serialVersionUID = 1L;
  private Long id;

  /** Id collection. */
  private Collection<Long> ids;

  /** Abreviatura. */
  private String abreviatura;

  /** Abreviatura. */
  private String descripcion;

}