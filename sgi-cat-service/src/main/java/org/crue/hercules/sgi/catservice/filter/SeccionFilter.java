package org.crue.hercules.sgi.catservice.filter;

import java.io.Serializable;
import java.util.Collection;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * SeccionFilter
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SeccionFilter implements Serializable {

  /*** Serial version. */
  private static final long serialVersionUID = 1L;

  /** Id. */
  private Long id;

  /** Id collection. */
  private Collection<Long> ids;

  /** Nombre. */
  private String nombre;

  /** Descripcion. */
  private String descripcion;

}