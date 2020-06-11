package org.crue.hercules.sgi.catservice.filter;

import java.io.Serializable;
import java.util.Collection;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * SupervisionFilter
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SupervisionFilter implements Serializable {

  /*** Serial version. */
  private static final long serialVersionUID = 1L;
  private Long id;

  /** Id collection. */
  private Collection<Long> ids;

  /** UsuarioRef. */
  private String usuarioRef;

  /** Id servicio. */
  private Long servicioId;
}