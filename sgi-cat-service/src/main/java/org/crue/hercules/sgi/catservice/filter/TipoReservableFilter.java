package org.crue.hercules.sgi.catservice.filter;

import java.io.Serializable;
import java.util.Collection;

import org.crue.hercules.sgi.catservice.enums.EstadoTipoReservableEnum;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * TipoReservableFilter
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TipoReservableFilter implements Serializable {

  /*** Serial version. */
  private static final long serialVersionUID = 1L;
  private Long id;

  /** Id collection. */
  private Collection<Long> ids;

  /** Descripcion. */
  private String descripcion;

  /** Servicio. */
  private Long servicioId;

  /** Duración mínima en minutos. */
  private Integer duracionMin;

  /** Días de antelación. */
  private Integer diasAnteMax;

  /** Reserva múltiple. */
  private Boolean reservaMulti;

  /** Vista máxima de calendario en días. */
  private Integer diasVistaMaxCalen;

  /** Hora antelación mínima. */
  private Integer horasAnteMin;

  /** Horas antelación anular usuario. */
  private Integer horasAnteAnular;

  /** Estado. */
  private EstadoTipoReservableEnum estado;

}