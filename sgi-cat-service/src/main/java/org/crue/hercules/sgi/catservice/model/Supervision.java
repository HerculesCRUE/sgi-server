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

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "supervision")
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class Supervision extends BaseEntity {

  /**
   *
   */
  private static final long serialVersionUID = 1L;

  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "supervision_seq")
  @SequenceGenerator(name = "supervision_seq", sequenceName = "supervision_seq", allocationSize = 1)
  private Long id;

  /** UsuarioRef. */
  @Column(name = "usuario_ref")
  private String usuarioRef;

  /** Servicio. */
  @OneToOne
  @JoinColumn(name = "servicio_id", nullable = true)
  private Servicio servicio;

}