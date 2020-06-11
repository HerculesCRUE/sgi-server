package org.crue.hercules.sgi.catservice.filter;

import java.io.Serializable;
import java.util.Collection;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ServicioFilter
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServicioFilter implements Serializable {

  /*** Serial version. */
  private static final long serialVersionUID = 1L;

  /** Id. */
  private Long id;

  /** Id collection. */
  private Collection<Long> ids;

  /** Nombre. */
  private String nombre;

  /** Abreviatura. */
  private String abreviatura;

  /** Contacto. */
  private String contacto;

  /** Id sección. */
  private Long idSeccion;

}