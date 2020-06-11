package org.crue.hercules.sgi.catservice.repository;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.crue.hercules.sgi.catservice.filter.ServicioFilter;
import org.crue.hercules.sgi.catservice.model.Seccion;
import org.crue.hercules.sgi.catservice.model.Servicio;
import org.crue.hercules.sgi.catservice.repository.specification.ServicioSpecifications;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

/**
 * ServicioRepositoryTest
 */
@DataJpaTest
public class ServicioRepositoryTest {

  @Autowired
  private TestEntityManager entityManager;

  @Autowired
  private SeccionRepository seccionRepository;

  @Autowired
  private ServicioRepository repository;

  @Test
  public void create_ReturnServicio() throws Exception {
    Seccion seccion = new Seccion(null, "Seccion 1", "Seccion create test");
    entityManager.persistFlushFind(seccionRepository.save(seccion));

    Servicio nuevoServicio = new Servicio(null, "Servicio 1", "Serv1", "Nombre Apellidos", seccion);
    entityManager.persistFlushFind(repository.save(nuevoServicio));

    Optional<Servicio> servicioCreated = repository.findById(nuevoServicio.getId());

    Assertions.assertThat(servicioCreated).isPresent().containsInstanceOf(Servicio.class)
        .hasValueSatisfying(servicio -> {
          Assertions.assertThat(servicio.getId()).isEqualTo(nuevoServicio.getId());
          Assertions.assertThat(servicio.getNombre()).isEqualTo(nuevoServicio.getNombre());
          Assertions.assertThat(servicio.getAbreviatura()).isEqualTo(nuevoServicio.getAbreviatura());
          Assertions.assertThat(servicio.getContacto()).isEqualTo(nuevoServicio.getContacto());
          Assertions.assertThat(servicio.getSeccion().getId()).isEqualTo(nuevoServicio.getSeccion().getId());
        });
  }

  @Test
  public void update_ReturnServicio() throws Exception {

    Seccion seccion = new Seccion(null, "Seccion 1", "Seccion create test");
    entityManager.persistFlushFind(seccionRepository.save(seccion));

    Servicio servicioSaved = entityManager
        .persistFlushFind(new Servicio(null, "Servicio 1", "Serv1", "Nombre Apellidos", seccion));
    servicioSaved.setNombre("Servicio 1");
    servicioSaved.setAbreviatura("Serv1");
    servicioSaved.setContacto("Nombre Apellidos");
    servicioSaved.setSeccion(seccion);
    repository.save(servicioSaved);

    Optional<Servicio> servicioUpdated = repository.findById(servicioSaved.getId());

    Assertions.assertThat(servicioUpdated).isPresent().containsInstanceOf(Servicio.class)
        .hasValueSatisfying(servicio -> {
          Assertions.assertThat(servicio.getId()).isEqualTo(servicioSaved.getId());
          Assertions.assertThat(servicio.getNombre()).isEqualTo(servicioSaved.getNombre());
          Assertions.assertThat(servicio.getAbreviatura()).isEqualTo(servicioSaved.getAbreviatura());
          Assertions.assertThat(servicio.getContacto()).isEqualTo(servicioSaved.getContacto());
          Assertions.assertThat(servicio.getSeccion().getId()).isEqualTo(servicioSaved.getSeccion().getId());
        });
  }

  @Test
  public void delete_WithId_DoNotGetServicio() throws Exception {

    List<Servicio> servicioList = new LinkedList<>();
    Seccion seccion = new Seccion(null, "Seccion 1", "Seccion create test");
    entityManager.persistFlushFind(seccionRepository.save(seccion));

    Servicio servicioToDelete = entityManager
        .persistFlushFind(new Servicio(null, "Servicio 1", "Serv1", "Nombre Apellidos", seccion));
    servicioList
        .add(entityManager.persistFlushFind(new Servicio(null, "Servicio 2", "Serv2", "Nombre Apellidos", seccion)));

    repository.deleteById(servicioToDelete.getId());
    List<Servicio> result = repository.findAll();

    Assertions.assertThat(servicioList).isEqualTo(result);
  }

  @Test
  public void findAll_ReturnServicioList() throws Exception {
    Seccion seccion = new Seccion(null, "Seccion 1", "Seccion create test");
    entityManager.persistFlushFind(seccionRepository.save(seccion));

    List<Servicio> servicioList = new LinkedList<>();
    servicioList
        .add(entityManager.persistFlushFind(new Servicio(null, "Servicio 1", "Serv1", "Nombre Apellidos", seccion)));
    servicioList
        .add(entityManager.persistFlushFind(new Servicio(null, "Servicio 2", "Serv2", "Nombre Apellidos", seccion)));

    List<Servicio> result = repository.findAll();

    Assertions.assertThat(servicioList).isEqualTo(result);
  }

  @Test
  public void findAllPage_ReturnsPage() throws Exception {
    Seccion seccion = new Seccion(null, "Seccion 1", "Seccion create test");
    entityManager.persistFlushFind(seccionRepository.save(seccion));

    entityManager.persistFlushFind(new Servicio(null, "Servicio 1", "Serv1", "Nombre Apellidos", seccion));
    entityManager.persistFlushFind(new Servicio(null, "Servicio 2", "Serv2", "Nombre Apellidos", seccion));
    Servicio result = entityManager
        .persistFlushFind(new Servicio(null, "Servicio 3", "Serv3", "Nombre Apellidos", seccion));

    Page<Servicio> resultPage = repository.findAll(PageRequest.of(1, 2, Sort.unsorted()));

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
    Seccion seccion = new Seccion(null, "Seccion 1", "Seccion create test");
    entityManager.persistFlushFind(seccionRepository.save(seccion));

    ServicioFilter filter = new ServicioFilter();
    filter.setNombre("Serv0");

    entityManager.persistFlushFind(new Servicio(null, "Serv01", "Serv1", "Nombre Apellidos", seccion));
    entityManager.persistFlushFind(new Servicio(null, "Serv02", "Serv2", "Nombre Apellidos", seccion));
    Servicio result = entityManager
        .persistFlushFind(new Servicio(null, "Serv03", "Serv3", "Nombre Apellidos", seccion));
    entityManager.persistFlushFind(new Servicio(null, "Ser 4", "Serv4", "Nombre Apellidos", seccion));
    entityManager.persistFlushFind(new Servicio(null, "Ser 5", "Serv5", "Nombre Apellidos", seccion));

    Page<Servicio> resultPage = repository.findAll(ServicioSpecifications.byServicioFilter(filter),
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
  public void findById_ReturnsServicio() throws Exception {
    Seccion seccion = new Seccion(null, "Seccion 1", "Seccion create test");
    entityManager.persistFlushFind(seccionRepository.save(seccion));

    final Servicio savedServicio = entityManager
        .persistFlushFind(new Servicio(null, "Servicio 1", "Serv1", "Nombre Apellidos", seccion));

    final Optional<Servicio> servicioFind = repository.findById(savedServicio.getId());

    Assertions.assertThat(servicioFind).isPresent().containsInstanceOf(Servicio.class).hasValueSatisfying(servicio -> {
      Assertions.assertThat(servicio.getId()).isEqualTo(savedServicio.getId());
      Assertions.assertThat(servicio.getNombre()).isEqualTo(savedServicio.getNombre());
      Assertions.assertThat(servicio.getAbreviatura()).isEqualTo(savedServicio.getAbreviatura());
      Assertions.assertThat(servicio.getContacto()).isEqualTo(savedServicio.getContacto());
      Assertions.assertThat(servicio.getSeccion().getId()).isEqualTo(savedServicio.getSeccion().getId());
    });
  }

}