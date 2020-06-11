package org.crue.hercules.sgi.catservice.repository;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.crue.hercules.sgi.catservice.model.Seccion;
import org.crue.hercules.sgi.catservice.model.Servicio;
import org.crue.hercules.sgi.catservice.model.TipoFungible;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

/**
 * TipoFungibleRepositoryTest
 */
@DataJpaTest
public class TipoFungibleRepositoryTest {
  @Autowired
  private TestEntityManager entityManager;

  @Autowired
  private TipoFungibleRepository tipoFungibleRepository;

  @Test
  public void findById_ReturnsTipoFungible() throws Exception {

    Seccion seccion = entityManager.persistAndFlush(new Seccion(null, "Seccion 1", "Seccion create test"));
    Servicio servicio = entityManager
        .persistAndFlush(new Servicio(null, "Servicio 1", "Serv1", "Nombre Apellidos", seccion));

    TipoFungible savedTipoFungible = entityManager.persistFlushFind(new TipoFungible(null, "TipoFungible1", servicio));

    Optional<TipoFungible> tipoFungible = tipoFungibleRepository.findById(savedTipoFungible.getId());

    Assertions.assertThat(tipoFungible).isPresent().containsInstanceOf(TipoFungible.class).hasValueSatisfying(a -> {
      Assertions.assertThat(a.getId()).isEqualTo(savedTipoFungible.getId());
      Assertions.assertThat(a.getNombre()).isEqualTo(savedTipoFungible.getNombre());
      Assertions.assertThat(a.getServicio()).isEqualTo(savedTipoFungible.getServicio());
    });
  }

  @Test
  public void findById_NotFound() {

    Optional<TipoFungible> tipoFungibleRecuperado = tipoFungibleRepository.findById(1L);
    Assertions.assertThat(tipoFungibleRecuperado.isPresent()).isFalse();
  }
}