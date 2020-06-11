package org.crue.hercules.sgi.catservice.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * unidad_medida
 */
@Entity
@Table(name = "unidad_medida")
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class UnidadMedida extends BaseEntity {
  /**
   *
   */
  private static final long serialVersionUID = 1L;

  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "unidad_medida_seq")
  @SequenceGenerator(name = "unidad_medida_seq", sequenceName = "unidad_medida_seq", allocationSize = 1)
  private Long id;

  /** Abreviatura. */
  @Column(name = "abreviatura")
  private String abreviatura;

  /** Abreviatura. */
  @Column(name = "descripcion")
  private String descripcion;

}