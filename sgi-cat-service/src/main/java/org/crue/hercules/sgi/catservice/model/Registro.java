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

import org.crue.hercules.sgi.catservice.enums.EstadoRegistroEnum;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Registro
 */
@Entity
@Table(name = "registro")
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class Registro extends BaseEntity {

  /** Serial version */
  private static final long serialVersionUID = 1L;

  /** Id. */
  @Id
  @Column(name = "id", nullable = false)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "registro_seq")
  @SequenceGenerator(name = "registro_seq", sequenceName = "registro_seq", allocationSize = 1)
  private Long id;

  /** Referencia usuario. */
  @Column(name = "usuario_ref", length = 50, nullable = false)
  @NotNull
  @Size(max = 50)
  private String usuarioRef;

  /** Servicio */
  @OneToOne
  @JoinColumn(name = "servicio_id")
  private Servicio servicio;

  /** Estado del registro. */
  @Column(name = "estado_registro", nullable = false)
  @NotNull
  private EstadoRegistroEnum estado;

  /** Indiciador de entrega papel. */
  @Column(name = "entrega_papel", nullable = false)
  @NotNull
  private Boolean entregaPapel;

  /** Indiciador de aceptación de condiciones. */
  @Column(name = "acepta_condiciones", nullable = false)
  @NotNull
  private Boolean aceptaCondiciones;

  /** Observaciones del registro. */
  @Column(name = "observaciones", length = 250, nullable = true)
  @Size(max = 250)
  private String observaciones;
}