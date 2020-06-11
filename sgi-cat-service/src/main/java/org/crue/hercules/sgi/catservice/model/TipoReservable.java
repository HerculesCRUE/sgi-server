package org.crue.hercules.sgi.catservice.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.crue.hercules.sgi.catservice.enums.EstadoTipoReservableEnum;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * tipo_reservable
 */
@Entity
@Table(name = "tipo_reservable")
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class TipoReservable extends BaseEntity {
  /**
   *
   */
  private static final long serialVersionUID = 1L;

  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tipo_reservable_seq")
  @SequenceGenerator(name = "tipo_reservable_seq", sequenceName = "tipo_reservable_seq", allocationSize = 1)
  private Long id;

  /** Descripción. */
  @Column(name = "descripcion")
  private String descripcion;

  /** Servicio */
  @OneToOne
  @JoinColumn(name = "servicio_id", nullable = true)
  private Servicio servicio;

  /** Duración mínima en minutos. */
  @Size(max = 3)
  @Column(name = "duracion_min")
  private Integer duracionMin;

  /** Días de antelación. */
  @Size(max = 2)
  @Column(name = "dias_ante_max")
  private Integer diasAnteMax;

  /** Reserva múltiple. */
  @Column(name = "reserva_multi")
  private Boolean reservaMulti;

  /** Vista máxima de calendario en días. */
  @Size(max = 2)
  @Column(name = "dias_vista_max_calen")
  private Integer diasVistaMaxCalen;

  /** Hora antelación mínima. */
  @Size(max = 8)
  @Column(name = "horas_ante_min")
  private Integer horasAnteMin;

  /** Horas antelación anular usuario. */
  @Size(max = 8)
  @Column(name = "horas_ante_anular")
  private Integer horasAnteAnular;

  /** Estado. */
  @Column(name = "estado", length = 50, nullable = false)
  @NotNull
  private EstadoTipoReservableEnum estado;

}