package org.crue.hercules.sgi.catservice.repository;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.crue.hercules.sgi.catservice.model.Seccion;
import org.crue.hercules.sgi.catservice.model.Servicio;
import org.crue.hercules.sgi.catservice.model.Supervision;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

/**
 * SupervisionRepositoryTest
 */
@DataJpaTest
public class SupervisionRepositoryTest {
  @Autowired
  private TestEntityManager entityManager;

  @Autowired
  private SupervisionRepository supervisionRepository;

  @Test
  public void findById_ReturnsSupervision() throws Exception {

    Seccion seccion = entityManager.persistAndFlush(new Seccion(null, "Seccion 1", "Seccion create test"));
    Servicio servicio = entityManager
        .persistAndFlush(new Servicio(null, "Servicio 1", "Serv1", "Nombre Apellidos", seccion));

    Supervision savedSupervision = entityManager.persistFlushFind(new Supervision(null, "UsuarioRef1", servicio));

    Optional<Supervision> supervision = supervisionRepository.findById(savedSupervision.getId());

    Assertions.assertThat(supervision).isPresent().containsInstanceOf(Supervision.class).hasValueSatisfying(a -> {
      Assertions.assertThat(a.getId()).isEqualTo(savedSupervision.getId());
      Assertions.assertThat(a.getUsuarioRef()).isEqualTo(savedSupervision.getUsuarioRef());
      Assertions.assertThat(a.getServicio()).isEqualTo(savedSupervision.getServicio());
    });
  }

  @Test
  public void findById_NotFound() {

    Optional<Supervision> supervisionRecuperado = supervisionRepository.findById(1L);
    Assertions.assertThat(supervisionRecuperado.isPresent()).isFalse();
  }
}