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

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Servicio
 */
@Entity
@Table(name = "servicio")
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class Servicio extends BaseEntity {

  /** Serial version */
  private static final long serialVersionUID = 1L;

  /** Id. */
  @Id
  @Column(name = "id", nullable = false)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "servicio_seq")
  @SequenceGenerator(name = "servicio_seq", sequenceName = "servicio_seq", allocationSize = 1)
  private Long id;

  /** Nombre. */
  @Column(name = "nombre", length = 100, nullable = false)
  @NotNull
  @Size(max = 100)
  private String nombre;

  /** Abreviatura. */
  @Column(name = "abreviatura", length = 10, nullable = false)
  @NotNull
  @Size(max = 10)
  private String abreviatura;

  /** Contacto. */
  @Column(name = "contacto", length = 100, nullable = false)
  @NotNull
  @Size(max = 500)
  private String contacto;

  /** Sección */
  @OneToOne
  @JoinColumn(name = "seccion_id", nullable = true)
  private Seccion seccion;
}