package org.crue.hercules.sgi.catservice.repository;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.crue.hercules.sgi.catservice.enums.EstadoRegistroEnum;
import org.crue.hercules.sgi.catservice.filter.RegistroFilter;
import org.crue.hercules.sgi.catservice.model.Registro;
import org.crue.hercules.sgi.catservice.model.Seccion;
import org.crue.hercules.sgi.catservice.model.Servicio;
import org.crue.hercules.sgi.catservice.repository.specification.RegistroSpecifications;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

/**
 * RegistroRepositoryTest
 */
@DataJpaTest
public class RegistroRepositoryTest {

  @Autowired
  private TestEntityManager entityManager;

  @Autowired
  private SeccionRepository seccionRepository;

  @Autowired
  private ServicioRepository servicioRepository;

  @Autowired
  private RegistroRepository repository;

  @Test
  public void create_ReturnRegistro() throws Exception {

    Seccion seccion = new Seccion(null, "Seccion 1", "Seccion create test");
    entityManager.persistFlushFind(seccionRepository.save(seccion));

    Servicio servicio = new Servicio(null, "Servicio 1", "Serv1", "Nombre Apellidos", seccion);
    entityManager.persistFlushFind(servicioRepository.save(servicio));

    Registro nuevoRegistro = new Registro(null, "user-998", servicio, EstadoRegistroEnum.ACTIVO, Boolean.TRUE,
        Boolean.FALSE, "nuevo registro");
    entityManager.persistFlushFind(repository.save(nuevoRegistro));

    Optional<Registro> registroCreated = repository.findById(nuevoRegistro.getId());

    Assertions.assertThat(registroCreated).isPresent().containsInstanceOf(Registro.class)
        .hasValueSatisfying(registro -> {
          Assertions.assertThat(registro.getId()).isEqualTo(nuevoRegistro.getId());
          Assertions.assertThat(registro.getUsuarioRef()).isEqualTo(nuevoRegistro.getUsuarioRef());
          Assertions.assertThat(registro.getServicio().getId()).isEqualTo(nuevoRegistro.getServicio().getId());
          Assertions.assertThat(registro.getEntregaPapel()).isEqualTo(nuevoRegistro.getEntregaPapel());
          Assertions.assertThat(registro.getAceptaCondiciones()).isEqualTo(nuevoRegistro.getAceptaCondiciones());
          Assertions.assertThat(registro.getObservaciones()).isEqualTo(nuevoRegistro.getObservaciones());
        });
  }

  @Test
  public void update_ReturnRegistro() throws Exception {

    Seccion seccion = new Seccion(null, "Seccion 1", "Seccion create test");
    entityManager.persistFlushFind(seccionRepository.save(seccion));

    Servicio servicio = new Servicio(null, "Servicio 1", "Serv1", "Nombre Apellidos", seccion);
    entityManager.persistFlushFind(servicioRepository.save(servicio));

    Registro registroSaved = entityManager.persistFlushFind(new Registro(null, "user-998", servicio,
        EstadoRegistroEnum.ACTIVO, Boolean.TRUE, Boolean.FALSE, "nuevo registro"));
    registroSaved.setUsuarioRef("user-2525");
    repository.save(registroSaved);

    Optional<Registro> registroUpdated = repository.findById(registroSaved.getId());

    Assertions.assertThat(registroUpdated).isPresent().containsInstanceOf(Registro.class)
        .hasValueSatisfying(registro -> {
          Assertions.assertThat(registro.getId()).isEqualTo(registroSaved.getId());
          Assertions.assertThat(registro.getUsuarioRef()).isEqualTo(registroSaved.getUsuarioRef());
          Assertions.assertThat(registro.getServicio().getId()).isEqualTo(registroSaved.getServicio().getId());
          Assertions.assertThat(registro.getEntregaPapel()).isEqualTo(registroSaved.getEntregaPapel());
          Assertions.assertThat(registro.getAceptaCondiciones()).isEqualTo(registroSaved.getAceptaCondiciones());
          Assertions.assertThat(registro.getObservaciones()).isEqualTo(registroSaved.getObservaciones());
        });
  }

  @Test
  public void delete_WithId_DoNotGetRegistro() throws Exception {

    Seccion seccion = new Seccion(null, "Seccion 1", "Seccion create test");
    entityManager.persistFlushFind(seccionRepository.save(seccion));

    Servicio servicio = new Servicio(null, "Servicio 1", "Serv1", "Nombre Apellidos", seccion);
    entityManager.persistFlushFind(servicioRepository.save(servicio));

    List<Registro> registroList = new LinkedList<>();
    Registro seccionToDelete = entityManager.persistFlushFind(new Registro(null, "user-998", servicio,
        EstadoRegistroEnum.ACTIVO, Boolean.TRUE, Boolean.FALSE, "nuevo registro"));
    registroList.add(entityManager.persistFlushFind(new Registro(null, "user-956", servicio,
        EstadoRegistroEnum.INACTIVO, Boolean.TRUE, Boolean.FALSE, "nuevo registro 2")));

    repository.deleteById(seccionToDelete.getId());
    List<Registro> result = repository.findAll();

    Assertions.assertThat(registroList).isEqualTo(result);
  }

  @Test
  public void findAll_ReturnRegistroList() throws Exception {
    Seccion seccion = new Seccion(null, "Seccion 1", "Seccion create test");
    entityManager.persistFlushFind(seccionRepository.save(seccion));

    Servicio servicio = new Servicio(null, "Servicio 1", "Serv1", "Nombre Apellidos", seccion);
    entityManager.persistFlushFind(servicioRepository.save(servicio));

    List<Registro> registroList = new LinkedList<>();
    registroList.add(entityManager.persistFlushFind(new Registro(null, "user-998", servicio, EstadoRegistroEnum.ACTIVO,
        Boolean.TRUE, Boolean.FALSE, "nuevo registro")));
    registroList.add(entityManager.persistFlushFind(new Registro(null, "user-956", servicio,
        EstadoRegistroEnum.INACTIVO, Boolean.TRUE, Boolean.FALSE, "nuevo registro 2")));

    List<Registro> result = repository.findAll();

    Assertions.assertThat(registroList).isEqualTo(result);
  }

  @Test
  public void findAllPage_ReturnsPage() throws Exception {

    Seccion seccion = new Seccion(null, "Seccion 1", "Seccion create test");
    entityManager.persistFlushFind(seccionRepository.save(seccion));

    Servicio servicio = new Servicio(null, "Servicio 1", "Serv1", "Nombre Apellidos", seccion);
    entityManager.persistFlushFind(servicioRepository.save(servicio));

    entityManager.persistFlushFind(new Registro(null, "user-998", servicio, EstadoRegistroEnum.ACTIVO, Boolean.TRUE,
        Boolean.FALSE, "nuevo registro"));
    entityManager.persistFlushFind(new Registro(null, "user-956", servicio, EstadoRegistroEnum.INACTIVO, Boolean.TRUE,
        Boolean.FALSE, "nuevo registro 2"));
    Registro result = entityManager.persistFlushFind(new Registro(null, "user-956", servicio,
        EstadoRegistroEnum.INACTIVO, Boolean.TRUE, Boolean.TRUE, "nuevo registro 3"));

    Page<Registro> resultPage = repository.findAll(PageRequest.of(1, 2, Sort.unsorted()));

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

    Servicio servicio = new Servicio(null, "Servicio 1", "Serv1", "Nombre Apellidos", seccion);
    entityManager.persistFlushFind(servicioRepository.save(servicio));

    RegistroFilter filter = new RegistroFilter();
    filter.setUsuarioRef("user-9");
    filter.setEstadoRegistro(EstadoRegistroEnum.ACTIVO);

    entityManager.persistFlushFind(new Registro(null, "user-998", servicio, EstadoRegistroEnum.ACTIVO, Boolean.TRUE,
        Boolean.FALSE, "nuevo registro"));
    entityManager.persistFlushFind(new Registro(null, "user-956", servicio, EstadoRegistroEnum.ACTIVO, Boolean.TRUE,
        Boolean.FALSE, "nuevo registro 2"));
    Registro result = entityManager.persistFlushFind(new Registro(null, "user-9898", servicio,
        EstadoRegistroEnum.ACTIVO, Boolean.TRUE, Boolean.FALSE, "nuevo registro 3"));
    entityManager.persistFlushFind(new Registro(null, "user-555", servicio, EstadoRegistroEnum.ACTIVO, Boolean.TRUE,
        Boolean.FALSE, "nuevo registro 4"));
    entityManager.persistFlushFind(new Registro(null, "user-444", servicio, EstadoRegistroEnum.INACTIVO, Boolean.TRUE,
        Boolean.FALSE, "nuevo registro 5"));

    Page<Registro> resultPage = repository.findAll(RegistroSpecifications.byRegistroFilter(filter),
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
  public void findById_ReturnsRegistro() throws Exception {
    Seccion seccion = new Seccion(null, "Seccion 1", "Seccion create test");
    entityManager.persistFlushFind(seccionRepository.save(seccion));

    Servicio servicio = new Servicio(null, "Servicio 1", "Serv1", "Nombre Apellidos", seccion);
    entityManager.persistFlushFind(servicioRepository.save(servicio));

    final Registro savedRegistro = entityManager.persistFlushFind(new Registro(null, "user-998", servicio,
        EstadoRegistroEnum.ACTIVO, Boolean.TRUE, Boolean.FALSE, "nuevo registro"));

    final Optional<Registro> registroFind = repository.findById(savedRegistro.getId());

    Assertions.assertThat(registroFind).isPresent().containsInstanceOf(Registro.class).hasValueSatisfying(registro -> {
      Assertions.assertThat(registro.getId()).isEqualTo(savedRegistro.getId());
      Assertions.assertThat(registro.getUsuarioRef()).isEqualTo(savedRegistro.getUsuarioRef());
      Assertions.assertThat(registro.getServicio().getId()).isEqualTo(savedRegistro.getServicio().getId());
      Assertions.assertThat(registro.getEntregaPapel()).isEqualTo(savedRegistro.getEntregaPapel());
      Assertions.assertThat(registro.getAceptaCondiciones()).isEqualTo(savedRegistro.getAceptaCondiciones());
      Assertions.assertThat(registro.getObservaciones()).isEqualTo(savedRegistro.getObservaciones());
    });
  }

}