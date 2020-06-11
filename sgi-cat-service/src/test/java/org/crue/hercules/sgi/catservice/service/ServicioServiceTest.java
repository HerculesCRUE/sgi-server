package org.crue.hercules.sgi.catservice.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.crue.hercules.sgi.catservice.exceptions.ServicioNotFoundException;
import org.crue.hercules.sgi.catservice.filter.ServicioFilter;
import org.crue.hercules.sgi.catservice.model.Seccion;
import org.crue.hercules.sgi.catservice.model.Servicio;
import org.crue.hercules.sgi.catservice.repository.ServicioRepository;
import org.crue.hercules.sgi.catservice.service.impl.ServicioServiceImpl;
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
 * ServicioServiceTest
 */
@ExtendWith(MockitoExtension.class)
public class ServicioServiceTest {

  @Mock
  private ServicioRepository servicioRepository;

  private ServicioService servicioService;

  @BeforeEach
  public void setUp() throws Exception {
    servicioService = new ServicioServiceImpl(servicioRepository);
  }

  @Test
  public void create_ReturnsServicio() {
    // given: Un nuevo servicio
    Seccion seccion = new Seccion(1L, "Seccion 1", "Seccion create test");
    Servicio servicio = new Servicio(null, "Servicio 1", "Serv1", "Nombre Apellidos", seccion);

    BDDMockito.given(servicioRepository.save(servicio)).will((InvocationOnMock invocation) -> {
      Servicio servicioCreado = invocation.getArgument(0);
      servicioCreado.setId(1L);
      return servicioCreado;
    });
    // when: Creamos la servicio
    Servicio servicioCreado = servicioService.create(servicio);

    // then: La servicio se crea correctamente.
    Assertions.assertThat(servicioCreado.getId()).isEqualTo(1L);
    Assertions.assertThat(servicioCreado.getNombre()).isEqualTo("Servicio 1");
    Assertions.assertThat(servicioCreado.getAbreviatura()).isEqualTo("Serv1");
    Assertions.assertThat(servicioCreado.getContacto()).isEqualTo("Nombre Apellidos");
    Assertions.assertThat(servicioCreado.getSeccion().getId()).isEqualTo(1L);
  }

  @Test
  public void create_ServicioWithId_ThrowsIllegalArgumentException() {
    // given: Un nuevo servicio que ya tiene id
    Seccion seccion = new Seccion(1L, "Seccion 1", "Seccion create test");
    Servicio servicio = new Servicio(1L, "Servicio 1", "Serv1", "Nombre Apellidos", seccion);

    // when: Creamos el servicio
    // then: Lanza una excepcion porque el servicio ya tiene id
    Assertions.assertThatThrownBy(() -> servicioService.create(servicio)).isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  public void update_ReturnsServicio() {
    // given: Una nueva servicio con el contacto actualizado
    Seccion seccion = new Seccion(1L, "Seccion 1", "Seccion create test");
    Servicio servicioContactoActualizado = new Servicio(1L, "Servicio 1", "Serv1", "Nombre Apellidos1", seccion);

    Servicio servicio = new Servicio(1L, "Servicio 1", "Serv1", "Nombre Apellidos", seccion);

    BDDMockito.given(servicioRepository.findById(1L)).willReturn(Optional.of(servicio));
    BDDMockito.given(servicioRepository.save(servicio)).willReturn(servicioContactoActualizado);

    // when: Actualizamos el servicio
    Servicio servicioActualizado = servicioService.update(servicio);

    // then: El servicio se actualiza correctamente.
    Assertions.assertThat(servicioActualizado.getId()).isEqualTo(1L);
    Assertions.assertThat(servicioActualizado.getNombre()).isEqualTo("Servicio 1");
    Assertions.assertThat(servicioActualizado.getAbreviatura()).isEqualTo("Serv1");
    Assertions.assertThat(servicioActualizado.getContacto()).isEqualTo("Nombre Apellidos1");
    Assertions.assertThat(servicioActualizado.getSeccion().getId()).isEqualTo(1L);
  }

  @Test
  public void update_ThrowsServicioNotFoundException() {
    // given: Un nuevo servicio a actualizar
    Seccion seccion = new Seccion(1L, "Seccion 1", "Seccion create test");
    Servicio servicio = new Servicio(1L, "Servicio 1", "Serv1", "Nombre Apellidos1", seccion);

    // when: actualizamos el servicio
    // then: Lanza una excepcion porque el servicio no existe
    Assertions.assertThatThrownBy(() -> servicioService.update(servicio)).isInstanceOf(ServicioNotFoundException.class);

  }

  @Test
  public void find_WithId_ReturnsServicio() {

    // given: Dado un servicio con id 1
    Seccion seccion = new Seccion(1L, "Seccion 1", "Seccion create test");
    Servicio servicio = new Servicio(1L, "Servicio 1", "Serv1", "Nombre Apellidos", seccion);

    BDDMockito.given(servicioRepository.findById(1L)).willReturn(Optional.of(servicio));

    Servicio servicioFind = servicioService.findById(1L);

    Assertions.assertThat(servicioFind.getId()).isEqualTo(1L);
    Assertions.assertThat(servicioFind.getNombre()).isEqualTo("Servicio 1");
    Assertions.assertThat(servicioFind.getAbreviatura()).isEqualTo("Serv1");
    Assertions.assertThat(servicioFind.getContacto()).isEqualTo("Nombre Apellidos");
    Assertions.assertThat(servicioFind.getSeccion().getId()).isEqualTo(1L);

  }

  @Test
  public void find_NotFound_ThrowsServicioNotFoundException() throws Exception {
    BDDMockito.given(servicioRepository.findById(1L)).willReturn(Optional.empty());

    Assertions.assertThatThrownBy(() -> servicioService.findById(1L)).isInstanceOf(ServicioNotFoundException.class);
  }

  @Test
  public void findAll_ReturnServicioList() {
    // given: dos servicioes
    Seccion seccion = new Seccion(1L, "Seccion 1", "Seccion create test");
    Servicio servicio = new Servicio(1L, "Servicio 1", "Serv1", "Nombre Apellidos", seccion);
    Servicio servicio2 = new Servicio(2L, "Servicio 2", "Serv2", "Nombre Apellidos", seccion);

    List<Servicio> servicioResponseList = new ArrayList<Servicio>();
    servicioResponseList.add(servicio);
    servicioResponseList.add(servicio2);
    Page<Servicio> pagedResponse = new PageImpl<Servicio>(servicioResponseList);
    BDDMockito.given(servicioRepository.findAll(PageRequest.of(0, 5, Sort.unsorted()))).willReturn(pagedResponse);

    // when: Buscamos las servicioes por id
    Pageable pageable = PageRequest.of(0, 5, Sort.unsorted());

    Page<Servicio> serviciosEncontradas = servicioService.findAll(pageable);

    // then: Recuperamos las servicioes
    Assertions.assertThat(serviciosEncontradas.getContent().size()).isEqualTo(2);
    Assertions.assertThat(serviciosEncontradas.getContent().containsAll(Arrays.asList(servicio, servicio2)));

  }

  @Test
  public void findAll_ReturnEmptyList() {
    // given: no hay servicioes

    Page<Servicio> pagedResponse = new PageImpl<Servicio>(Collections.emptyList());
    BDDMockito.given(servicioRepository.findAll(PageRequest.of(0, 5, Sort.unsorted()))).willReturn(pagedResponse);

    // when: Buscamos las unidades por magnitud id
    Pageable pageable = PageRequest.of(0, 5, Sort.unsorted());

    Page<Servicio> serviciosEncontradas = servicioService.findAll(pageable);

    // then: Recuperamos las unidades medida
    Assertions.assertThat(serviciosEncontradas.getContent().isEmpty());
  }

  @Test
  public void findAllLike_ReturnServicioList() {
    // given: dos servicioes
    Seccion seccion = new Seccion(1L, "Seccion 1", "Seccion create test");
    Servicio servicio = new Servicio(1L, "Servicio 1", "Serv1", "Nombre Apellidos", seccion);
    Servicio servicio2 = new Servicio(2L, "Servicio 2", "Serv2", "Nombre Apellidos", seccion);

    List<Servicio> servicioResponseList = new ArrayList<Servicio>();
    servicioResponseList.add(servicio);
    servicioResponseList.add(servicio2);
    Page<Servicio> pagedResponse = new PageImpl<Servicio>(servicioResponseList);

    BDDMockito.given(
        servicioRepository.findAll(ArgumentMatchers.<Specification<Servicio>>any(), ArgumentMatchers.<Pageable>any()))
        .willReturn(pagedResponse);

    // when: Buscamos las servicioes por id
    Pageable pageable = PageRequest.of(0, 5, Sort.unsorted());

    ServicioFilter filter = new ServicioFilter();
    filter.setNombre("Servicio");
    Page<Servicio> serviciosEncontradas = servicioService.findAllLike(filter, pageable);

    // then: Recuperamos las servicioes
    Assertions.assertThat(serviciosEncontradas.getContent().size()).isEqualTo(2);
    Assertions.assertThat(serviciosEncontradas.getContent().containsAll(Arrays.asList(servicio, servicio2)));

  }

  @Test
  public void findAllLike_ReturnEmptyList() {
    // given: no hay servicioes

    Page<Servicio> pagedResponse = new PageImpl<Servicio>(Collections.emptyList());
    BDDMockito.given(
        servicioRepository.findAll(ArgumentMatchers.<Specification<Servicio>>any(), ArgumentMatchers.<Pageable>any()))
        .willReturn(pagedResponse);

    // when: Buscamos las unidades por magnitud id
    Pageable pageable = PageRequest.of(0, 5, Sort.unsorted());

    ServicioFilter filter = new ServicioFilter();
    filter.setNombre("Servicio");
    Page<Servicio> serviciosEncontradas = servicioService.findAllLike(filter, pageable);

    // then: Recuperamos las unidades medida
    Assertions.assertThat(serviciosEncontradas.getContent().isEmpty());
  }

  @Test
  public void findAll_WithoutPageable_ReturnServicioList() {
    // given: dos servicioes

    Seccion seccion = new Seccion(1L, "Seccion 1", "Seccion create test");
    Servicio servicio = new Servicio(1L, "Servicio 1", "Serv1", "Nombre Apellidos", seccion);
    Servicio servicio2 = new Servicio(2L, "Servicio 2", "Serv2", "Nombre Apellidos", seccion);
    List<Servicio> servicioResponseList = new ArrayList<Servicio>();
    servicioResponseList.add(servicio);
    servicioResponseList.add(servicio2);

    BDDMockito.given(servicioRepository.findAll()).willReturn(servicioResponseList);

    // when: Buscamos las servicioes por id
    List<Servicio> serviciosEncontradas = servicioService.findAll();

    // then: Recuperamos las servicioes
    Assertions.assertThat(serviciosEncontradas.size()).isEqualTo(2);
    Assertions.assertThat(serviciosEncontradas.containsAll(Arrays.asList(servicio, servicio2)));

  }

  @Test
  public void findAllLike_WithoutPageable_ReturnEmptyList() {
    // given: no hay servicioes
    BDDMockito.given(servicioRepository.findAll()).willReturn(Collections.emptyList());

    List<Servicio> serviciosEncontradas = servicioService.findAll();

    // then: Recuperamos las unidades medida
    Assertions.assertThat(serviciosEncontradas.isEmpty());
  }

  @Test
  public void delete_Success() {
    servicioService.delete(ArgumentMatchers.<Long>any());
  }
}