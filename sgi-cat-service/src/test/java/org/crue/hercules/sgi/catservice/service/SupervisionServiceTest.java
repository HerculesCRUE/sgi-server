package org.crue.hercules.sgi.catservice.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.crue.hercules.sgi.catservice.exceptions.SupervisionNotFoundException;
import org.crue.hercules.sgi.catservice.filter.SupervisionFilter;
import org.crue.hercules.sgi.catservice.model.Seccion;
import org.crue.hercules.sgi.catservice.model.Servicio;
import org.crue.hercules.sgi.catservice.model.Supervision;
import org.crue.hercules.sgi.catservice.repository.SupervisionRepository;
import org.crue.hercules.sgi.catservice.service.impl.SupervisionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

/**
 * SupervisionServiceTest
 */
@ExtendWith(MockitoExtension.class)
public class SupervisionServiceTest {
  @Mock
  private SupervisionRepository supervisionRepository;

  @Mock
  private SupervisionService supervisionService;

  @BeforeEach
  public void setUp() throws Exception {
    supervisionService = new SupervisionServiceImpl(supervisionRepository);
  }

  @Test
  public void findAll_ReturnsSupervisionList() {

    Seccion seccion = new Seccion(1L, "Seccion 1", "Seccion create test");
    Servicio servicio = new Servicio(1L, "Servicio 1", "Serv1", "Nombre Apellidos", seccion);

    // given: dos supervisiones
    Supervision supervision1 = new Supervision(1L, "UsuarioRef1", servicio);
    Supervision supervision2 = new Supervision(2L, "UsuarioRef2", servicio);

    List<Supervision> supervisionResponseList = new ArrayList<Supervision>();
    supervisionResponseList.add(supervision1);
    supervisionResponseList.add(supervision2);

    BDDMockito.given(supervisionRepository.findAll()).willReturn(supervisionResponseList);

    // when: Se realiza la búsqueda de las supervisiones
    List<Supervision> supervisionList = supervisionService.findAll();

    // then: Recuperamos las supervisiones
    Assertions.assertThat(supervisionList.size()).isEqualTo(2);
    Assertions.assertThat(supervisionList.containsAll(Arrays.asList(supervision1, supervision2)));

  }

  @Test
  public void findAll_ReturnsEmptyList() throws Exception {

    // given: Una lista vacía
    BDDMockito.given(supervisionService.findAll()).willReturn(Collections.emptyList());

    // when: Se realiza la búsqueda de supervisiones
    List<Supervision> supervisionList = supervisionService.findAll();

    // then: Recuperamos la lista vacía
    Assertions.assertThat(supervisionList);

  }

  @Test
  public void find_WithId_ReturnsSupervision() {
    // given: El id de una supervision
    Seccion seccion = new Seccion(1L, "Seccion 1", "Seccion create test");
    Servicio servicio = new Servicio(1L, "Servicio 1", "Serv1", "Nombre Apellidos", seccion);

    BDDMockito.given(supervisionRepository.findById(1L))
        .willReturn(Optional.of(new Supervision(1L, "UsuarioRef1", servicio)));

    // when: Buscamos por id
    Supervision supervision = supervisionService.findById(1L);

    // then: Recuperamos la supervision
    Assertions.assertThat(supervision.getId()).isEqualTo(1L);
    Assertions.assertThat(supervision.getUsuarioRef()).isEqualTo("UsuarioRef1");
    Assertions.assertThat(supervision.getServicio()).isEqualTo(servicio);

  }

  @Test
  public void find_NotFound_ThrowsSupervisionNotFoundException() throws Exception {
    BDDMockito.given(supervisionRepository.findById(1L)).willReturn(Optional.empty());

    Assertions.assertThatThrownBy(() -> supervisionService.findById(1L))
        .isInstanceOf(SupervisionNotFoundException.class);
  }

  @Test
  public void findAllPageable_ReturnsSupervisionList() {

    // given: dos supervisiones
    Seccion seccion = new Seccion(1L, "Seccion 1", "Seccion create test");
    Servicio servicio = new Servicio(1L, "Servicio 1", "Serv1", "Nombre Apellidos", seccion);

    Supervision supervision1 = new Supervision(1L, "UsuarioRef1", servicio);
    Supervision supervision2 = new Supervision(2L, "UsuarioRef2", servicio);

    List<Supervision> supervisionResponseList = new ArrayList<Supervision>();
    supervisionResponseList.add(supervision1);
    supervisionResponseList.add(supervision2);
    Page<Supervision> pagedResponse = new PageImpl<Supervision>(supervisionResponseList);
    BDDMockito.given(supervisionRepository.findAll(PageRequest.of(0, 5, Sort.unsorted()))).willReturn(pagedResponse);

    // when: Buscamos las supervisiones por magnitud id
    Pageable pageable = PageRequest.of(0, 5, Sort.unsorted());

    Page<Supervision> supervisionEncontrados = supervisionService.findAll(pageable);

    // then: Recuperamos las supervisiones
    Assertions.assertThat(supervisionEncontrados.getContent().size()).isEqualTo(2);
    Assertions.assertThat(supervisionEncontrados.getContent().containsAll(Arrays.asList(supervision1, supervision2)));

  }

  @Test
  public void findAllPageable_ReturnsEmptyList() {
    // given: Una lista vacía
    Page<Supervision> pagedResponse = new PageImpl<Supervision>(Collections.emptyList());
    Pageable pageable = PageRequest.of(0, 5, Sort.unsorted());

    BDDMockito.given(supervisionRepository.findAll(PageRequest.of(0, 5, Sort.unsorted()))).willReturn(pagedResponse);

    // when: Se realiza la búsqueda de supervisiones
    Page<Supervision> supervisionPageableList = supervisionService.findAll(pageable);

    // then: Recuperamos la lista vacía
    Assertions.assertThat(supervisionPageableList);
  }

  @Test
  public void findAllLike_ReturnsSupervisionList() {
    // given: dos supervisiones
    Seccion seccion = new Seccion(1L, "Seccion 1", "Seccion create test");
    Servicio servicio = new Servicio(1L, "Servicio 1", "Serv1", "Nombre Apellidos", seccion);

    Supervision supervision1 = new Supervision(1L, "UsurioRef1", servicio);
    Supervision supervision2 = new Supervision(2L, "UsuarioRef2", servicio);

    List<Supervision> supervisionResponseList = new ArrayList<Supervision>();
    supervisionResponseList.add(supervision1);
    supervisionResponseList.add(supervision2);

    Page<Supervision> pagedResponse = new PageImpl<Supervision>(supervisionResponseList);

    BDDMockito.given(supervisionRepository.findAll(ArgumentMatchers.<Specification<Supervision>>any(),
        ArgumentMatchers.<Pageable>any())).willReturn(pagedResponse);

    Pageable pageable = PageRequest.of(0, 5, Sort.unsorted());
    SupervisionFilter supervisionFilter = new SupervisionFilter();
    supervisionFilter.setId(1L);
    supervisionFilter.setUsuarioRef("UsuarioRef1");
    supervisionFilter.setServicioId(servicio.getId());

    Page<Supervision> supervisionEncontrados = supervisionService.findAllLike(supervisionFilter, pageable);

    // then: Recuperamos las supervisiones
    Assertions.assertThat(supervisionEncontrados.getContent().size()).isEqualTo(2);
    Assertions.assertThat(supervisionEncontrados.getContent().containsAll(Arrays.asList(supervision1, supervision2)));

  }

  @Test
  public void findAllLike_ReturnsEmptyList() {
    // given: Una lista vacía
    Page<Supervision> pagedResponse = new PageImpl<Supervision>(Collections.emptyList());
    Pageable pageable = PageRequest.of(0, 5, Sort.unsorted());
    SupervisionFilter supervisionFilter = new SupervisionFilter();

    BDDMockito.given(supervisionRepository.findAll(ArgumentMatchers.<Specification<Supervision>>any(),
        ArgumentMatchers.<Pageable>any())).willReturn(pagedResponse);

    // when: Se realiza la búsqueda de supervision
    Page<Supervision> supervisionList = supervisionService.findAllLike(supervisionFilter, pageable);

    // then: Recuperamos la lista vacía
    Assertions.assertThat(supervisionList);
  }

  @Test
  public void create_ReturnsSupervision() {

    // given: Una supervision
    Seccion seccion = new Seccion(1L, "Seccion 1", "Seccion create test");
    Servicio servicio = new Servicio(1L, "Servicio 1", "Serv1", "Nombre Apellidos", seccion);

    Supervision supervision = new Supervision(null, "UsuarioRef1", servicio);

    BDDMockito.given(supervisionRepository.save(supervision)).will((InvocationOnMock invocation) -> {
      Supervision supervisionCreada = invocation.getArgument(0);
      supervisionCreada.setId(1L);
      return supervisionCreada;
    });

    // when: Creamos supervision
    Supervision supervisionCreada = supervisionService.create(supervision);

    // then: supervision se crea correctamente.
    Assertions.assertThat(supervisionCreada.getId()).isEqualTo(1L);
    Assertions.assertThat(supervisionCreada.getUsuarioRef()).isEqualTo("UsuarioRef1");
    Assertions.assertThat(supervisionCreada.getServicio()).isEqualTo(servicio);

  }

  @Test
  public void create_SupervisionWithId_ThrowsIllegalArgumentException() {
    // given: Una nueva supervision que ya tiene id
    Seccion seccion = new Seccion(1L, "Seccion 1", "Seccion create test");
    Servicio servicio = new Servicio(1L, "Servicio 1", "Serv1", "Nombre Apellidos", seccion);

    Supervision supervision = new Supervision(1L, "UsuarioRef1", servicio);

    // then: Lanza una excepcion porque la supervision ya tiene id
    Assertions.assertThatThrownBy(() -> supervisionService.create(supervision))
        .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  public void replace_ReturnsSupervision() {
    // given: Una nueva supervision con el servicio actualizado
    Seccion seccion = new Seccion(1L, "Seccion 1", "Seccion create test");
    Servicio servicio = new Servicio(1L, "Servicio 1", "Serv1", "Nombre Apellidos", seccion);

    Supervision supervisionServicioActualizado = new Supervision(1L, "UsuarioRef1 Actualizado", servicio);

    Supervision supervision = new Supervision(1L, "UsuarioRef1", servicio);

    BDDMockito.given(supervisionRepository.findById(1L)).willReturn(Optional.of(supervision));
    BDDMockito.given(supervisionRepository.save(supervision)).willReturn(supervisionServicioActualizado);

    // when: Actualizamos la supervision
    Supervision supervisionActualizado = supervisionService.update(supervision);

    // then: La supervision se actualiza correctamente.
    Assertions.assertThat(supervisionActualizado.getId()).isEqualTo(1L);
    Assertions.assertThat(supervisionActualizado.getUsuarioRef()).isEqualTo("UsuarioRef1 Actualizado");
    Assertions.assertThat(supervisionActualizado.getServicio()).isEqualTo(servicio);

  }

  @Test
  public void replace_ThrowsSupervisionNotFoundException() {
    // given: Una nueva supervision a actualizar
    Seccion seccion = new Seccion(1L, "Seccion 1", "Seccion create test");
    Servicio servicio = new Servicio(1L, "Servicio 1", "Serv1", "Nombre Apellidos", seccion);

    Supervision supervision = new Supervision(1L, "UsuarioRef1", servicio);

    // then: Lanza una excepcion porque la supervision no existe
    Assertions.assertThatThrownBy(() -> supervisionService.update(supervision))
        .isInstanceOf(SupervisionNotFoundException.class);

  }

  @Test
  public void remove() {
    BDDMockito.doNothing().when(supervisionRepository).deleteById(ArgumentMatchers.<Long>any());
    supervisionService.delete(ArgumentMatchers.<Long>any());
  }
}