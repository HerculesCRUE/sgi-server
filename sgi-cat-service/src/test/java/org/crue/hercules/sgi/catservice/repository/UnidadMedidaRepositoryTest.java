package org.crue.hercules.sgi.catservice.repository;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.crue.hercules.sgi.catservice.filter.UnidadMedidaFilter;
import org.crue.hercules.sgi.catservice.model.UnidadMedida;
import org.crue.hercules.sgi.catservice.repository.specification.UnidadMedidaSpecifications;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

/**
 * UnidadMedidaRepositoryTest
 */
@DataJpaTest
public class UnidadMedidaRepositoryTest {

  @Autowired
  private TestEntityManager entityManager;

  @Autowired
  private UnidadMedidaRepository repository;

  @Test
  public void create_ReturnUnidadMedida() throws Exception {

    UnidadMedida unidadMedida = new UnidadMedida(null, "UM1", "UnidadMedida1");
    entityManager.persistFlushFind(repository.save(unidadMedida));

    Optional<UnidadMedida> unidadMedidaCreated = repository.findById(unidadMedida.getId());

    Assertions.assertThat(unidadMedidaCreated).isPresent().containsInstanceOf(UnidadMedida.class)
        .hasValueSatisfying(a -> {
          Assertions.assertThat(a.getId()).isEqualTo(unidadMedida.getId());
          Assertions.assertThat(a.getAbreviatura()).isEqualTo(unidadMedida.getAbreviatura());
          Assertions.assertThat(a.getDescripcion()).isEqualTo(unidadMedida.getDescripcion());
        });
  }

  @Test
  public void update_ReturnUnidadMedida() throws Exception {

    UnidadMedida unidadMedidaSaved = entityManager.persistFlushFind(new UnidadMedida(null, "UM1", "UnidadMedida1"));
    unidadMedidaSaved.setAbreviatura("UM01");
    unidadMedidaSaved.setDescripcion("UnidadMedida01");
    repository.save(unidadMedidaSaved);

    Optional<UnidadMedida> unidadMedidaUpdated = repository.findById(unidadMedidaSaved.getId());

    Assertions.assertThat(unidadMedidaUpdated).isPresent().containsInstanceOf(UnidadMedida.class)
        .hasValueSatisfying(a -> {
          Assertions.assertThat(a.getId()).isEqualTo(unidadMedidaSaved.getId());
          Assertions.assertThat(a.getAbreviatura()).isEqualTo(unidadMedidaSaved.getAbreviatura());
          Assertions.assertThat(a.getDescripcion()).isEqualTo(unidadMedidaSaved.getDescripcion());
        });
  }

  @Test
  public void delete_WithId_DoNotGetUnidadMedida() throws Exception {

    List<UnidadMedida> unidadMedidaList = new LinkedList<>();
    UnidadMedida unidadMedidaToDelete = entityManager.persistFlushFind(new UnidadMedida(null, "UM1", "UnidadMedida1"));
    unidadMedidaList.add(entityManager.persistFlushFind(new UnidadMedida(null, "UM2", "UnidadMedida2")));

    repository.deleteById(unidadMedidaToDelete.getId());
    List<UnidadMedida> result = repository.findAll();

    Assertions.assertThat(unidadMedidaList).isEqualTo(result);
  }

  @Test
  public void findAll_ReturnUnidadMedidaList() throws Exception {

    List<UnidadMedida> unidadMedidaList = new LinkedList<>();
    unidadMedidaList.add(entityManager.persistFlushFind(new UnidadMedida(null, "UM1", "UnidadMedida1")));
    unidadMedidaList.add(entityManager.persistFlushFind(new UnidadMedida(null, "UM2", "UnidadMedida2")));

    List<UnidadMedida> result = repository.findAll();

    Assertions.assertThat(unidadMedidaList).isEqualTo(result);
  }

  @Test
  public void findAllPage_ReturnsPage() throws Exception {

    entityManager.persistFlushFind(new UnidadMedida(null, "UM1", "UnidadMedida1"));
    entityManager.persistFlushFind(new UnidadMedida(null, "UM2", "UnidadMedida2"));
    UnidadMedida result = entityManager.persistFlushFind(new UnidadMedida(null, "UM3", "UnidadMedida3"));

    Page<UnidadMedida> resultPage = repository.findAll(PageRequest.of(1, 2, Sort.unsorted()));

    Assertions.assertThat(resultPage.getContent().size()).isEqualTo(1);
    Assertions.assertThat(resultPage.getContent().get(0)).isEqualTo(result);
    Assertions.assertThat(resultPage.getTotalElements()).isEqualTo(3);
    Assertions.assertThat(resultPage.getTotalPages()).isEqualTo(2);
    Assertions.assertThat(resultPage.getPageable().getPageSize()).isEqualTo(2);
    Assertions.assertThat(resultPage.getPageable().getPageNumber()).isEqualTo(1);
    Assertions.assertThat(resultPage.getPageable().hasPrevious()).isTrue();
  }

  @Test
  public void findAllFilteredPage_ReturnsPage() throws Exception {

    UnidadMedidaFilter filter = new UnidadMedidaFilter();
    filter.setAbreviatura("UM0");

    entityManager.persistFlushFind(new UnidadMedida(null, "UM01", "UnidadMedida01"));
    entityManager.persistFlushFind(new UnidadMedida(null, "UM02", "UnidadMedida02"));
    UnidadMedida result = entityManager.persistFlushFind(new UnidadMedida(null, "UM03", "UnidadMedida03"));
    entityManager.persistFlushFind(new UnidadMedida(null, "UM4", "UnidadMedida4"));
    entityManager.persistFlushFind(new UnidadMedida(null, "UM5", "UnidadMedida5"));

    Page<UnidadMedida> resultPage = repository.findAll(UnidadMedidaSpecifications.byUnidadMedidaFilter(filter),
        PageRequest.of(1, 2, Sort.unsorted()));

    Assertions.assertThat(resultPage.getContent().size()).isEqualTo(1);
    Assertions.assertThat(resultPage.getContent().get(0)).isEqualTo(result);
    Assertions.assertThat(resultPage.getTotalElements()).isEqualTo(3);
    Assertions.assertThat(resultPage.getTotalPages()).isEqualTo(2);
    Assertions.assertThat(resultPage.getPageable().getPageSize()).isEqualTo(2);
    Assertions.assertThat(resultPage.getPageable().getPageNumber()).isEqualTo(1);
    Assertions.assertThat(resultPage.getPageable().hasPrevious()).isTrue();
  }

  @Test
  public void findById_ReturnsUnidadMedida() throws Exception {

    final UnidadMedida savedUnidadMedida = entityManager
        .persistFlushFind(new UnidadMedida(null, "UM1", "UnidadMedida1"));

    final Optional<UnidadMedida> unidadMedida = repository.findById(savedUnidadMedida.getId());

    Assertions.assertThat(unidadMedida).isPresent().containsInstanceOf(UnidadMedida.class).hasValueSatisfying(a -> {
      Assertions.assertThat(a.getId()).isEqualTo(savedUnidadMedida.getId());
      Assertions.assertThat(a.getAbreviatura()).isEqualTo(savedUnidadMedida.getAbreviatura());
      Assertions.assertThat(a.getDescripcion()).isEqualTo(savedUnidadMedida.getDescripcion());
    });
  }

  @Test
  public void findByAbreviatura_ReturnsUnidadMedida() throws Exception {

    final UnidadMedida savedUnidadMedida = entityManager
        .persistFlushFind(new UnidadMedida(null, "UM1", "UnidadMedida1"));

    final Optional<UnidadMedida> unidadMedida = repository.findByAbreviatura("UM1");

    Assertions.assertThat(unidadMedida).isPresent().containsInstanceOf(UnidadMedida.class).hasValueSatisfying(a -> {
      Assertions.assertThat(a.getId()).isEqualTo(savedUnidadMedida.getId());
      Assertions.assertThat(a.getAbreviatura()).isEqualTo(savedUnidadMedida.getAbreviatura());
      Assertions.assertThat(a.getDescripcion()).isEqualTo(savedUnidadMedida.getDescripcion());
    });
  }
}