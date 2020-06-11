package org.crue.hercules.sgi.catservice.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tipo_fungible")
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class TipoFungible extends BaseEntity {

  /**
   *
   */
  private static final long serialVersionUID = 1L;

  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tipo_fungible_seq")
  @SequenceGenerator(name = "tipo_fungible_seq", sequenceName = "tipo_fungible_seq", allocationSize = 1)
  private Long id;

  /** Nombre. */
  @Column(name = "nombre")
  private String nombre;

  /** Servicio. */
  @ManyToOne
  @JoinColumn(name = "servicio_id", nullable = true)
  private Servicio servicio;

}