package org.crue.hercules.sgi.catservice.repository;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.crue.hercules.sgi.catservice.filter.SeccionFilter;
import org.crue.hercules.sgi.catservice.model.Seccion;
import org.crue.hercules.sgi.catservice.repository.specification.SeccionSpecifications;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

/**
 * SeccionRepositoryTest
 */
@DataJpaTest
public class SeccionRepositoryTest {

  @Autowired
  private TestEntityManager entityManager;

  @Autowired
  private SeccionRepository repository;

  @Test
  public void create_ReturnSeccion() throws Exception {

    Seccion nuevaSeccion = new Seccion(null, "Seccion 1", "Seccion create test");
    entityManager.persistFlushFind(repository.save(nuevaSeccion));

    Optional<Seccion> seccionCreated = repository.findById(nuevaSeccion.getId());

    Assertions.assertThat(seccionCreated).isPresent().containsInstanceOf(Seccion.class).hasValueSatisfying(seccion -> {
      Assertions.assertThat(seccion.getId()).isEqualTo(nuevaSeccion.getId());
      Assertions.assertThat(seccion.getNombre()).isEqualTo(nuevaSeccion.getNombre());
      Assertions.assertThat(seccion.getDescripcion()).isEqualTo(nuevaSeccion.getDescripcion());
    });
  }

  @Test
  public void update_ReturnSeccion() throws Exception {

    Seccion seccionSaved = entityManager.persistFlushFind(new Seccion(null, "Seccion 1", "Seccion 1 test"));
    seccionSaved.setNombre("Seccion 1");
    seccionSaved.setDescripcion("Seccion 1 test actualizada");
    repository.save(seccionSaved);

    Optional<Seccion> seccionUpdated = repository.findById(seccionSaved.getId());

    Assertions.assertThat(seccionUpdated).isPresent().containsInstanceOf(Seccion.class).hasValueSatisfying(seccion -> {
      Assertions.assertThat(seccion.getId()).isEqualTo(seccionSaved.getId());
      Assertions.assertThat(seccion.getNombre()).isEqualTo(seccionSaved.getNombre());
      Assertions.assertThat(seccion.getDescripcion()).isEqualTo(seccionSaved.getDescripcion());
    });
  }

  @Test
  public void delete_WithId_DoNotGetSeccion() throws Exception {

    List<Seccion> seccionList = new LinkedList<>();
    Seccion seccionToDelete = entityManager.persistFlushFind(new Seccion(null, "Seccion 1", "Seccion 1 test"));
    seccionList.add(entityManager.persistFlushFind(new Seccion(null, "Seccion 2", "Seccion 2 test")));

    repository.deleteById(seccionToDelete.getId());
    List<Seccion> result = repository.findAll();

    Assertions.assertThat(seccionList).isEqualTo(result);
  }

  @Test
  public void findAll_ReturnSeccionList() throws Exception {

    List<Seccion> seccionList = new LinkedList<>();
    seccionList.add(entityManager.persistFlushFind(new Seccion(null, "Seccion 1", "Seccion 1 test")));
    seccionList.add(entityManager.persistFlushFind(new Seccion(null, "Seccion 2", "Seccion 2 test")));

    List<Seccion> result = repository.findAll();

    Assertions.assertThat(seccionList).isEqualTo(result);
  }

  @Test
  public void findAllPage_ReturnsPage() throws Exception {

    entityManager.persistFlushFind(new Seccion(null, "Seccion 1", "Seccion 1 test"));
    entityManager.persistFlushFind(new Seccion(null, "Seccion 2", "Seccion 2 test"));
    Seccion result = entityManager.persistFlushFind(new Seccion(null, "Seccion 3", "Seccion 3 test"));

    Page<Seccion> resultPage = repository.findAll(PageRequest.of(1, 2, Sort.unsorted()));

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

    SeccionFilter filter = new SeccionFilter();
    filter.setNombre("Seccion");

    entityManager.persistFlushFind(new Seccion(null, "Seccion 1", "Seccion 1 test"));
    entityManager.persistFlushFind(new Seccion(null, "Seccion 2", "Seccion 2 test"));
    Seccion result = entityManager.persistFlushFind(new Seccion(null, "Seccion 3", "Seccion 3 test"));
    entityManager.persistFlushFind(new Seccion(null, "Sec 4", "Seccion 4 test"));
    entityManager.persistFlushFind(new Seccion(null, "Sec 5", "Seccion 5 test"));

    Page<Seccion> resultPage = repository.findAll(SeccionSpecifications.bySeccionFilter(filter),
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
  public void findById_ReturnsSeccion() throws Exception {

    final Seccion savedSeccion = entityManager.persistFlushFind(new Seccion(null, "Seccion 1", "Seccion 1 test"));

    final Optional<Seccion> seccionFind = repository.findById(savedSeccion.getId());

    Assertions.assertThat(seccionFind).isPresent().containsInstanceOf(Seccion.class).hasValueSatisfying(seccion -> {
      Assertions.assertThat(seccion.getId()).isEqualTo(savedSeccion.getId());
      Assertions.assertThat(seccion.getNombre()).isEqualTo(savedSeccion.getNombre());
      Assertions.assertThat(seccion.getDescripcion()).isEqualTo(savedSeccion.getDescripcion());
    });
  }

}