package org.crue.hercules.sgi.catservice.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.crue.hercules.sgi.catservice.exceptions.SeccionNotFoundException;
import org.crue.hercules.sgi.catservice.filter.SeccionFilter;
import org.crue.hercules.sgi.catservice.model.Seccion;
import org.crue.hercules.sgi.catservice.repository.SeccionRepository;
import org.crue.hercules.sgi.catservice.service.impl.SeccionServiceImpl;
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
 * SeccionServiceTest
 */
@ExtendWith(MockitoExtension.class)
public class SeccionServiceTest {

  @Mock
  private SeccionRepository seccionRepository;

  private SeccionService seccionService;

  @BeforeEach
  public void setUp() throws Exception {
    seccionService = new SeccionServiceImpl(seccionRepository);
  }

  @Test
  public void create_ReturnsSeccion() {
    // given: Una nueva sección
    Seccion seccion = new Seccion(null, "Seccion 1", "Seccion create test");

    BDDMockito.given(seccionRepository.save(seccion)).will((InvocationOnMock invocation) -> {
      Seccion seccionCreada = invocation.getArgument(0);
      seccionCreada.setId(1L);
      return seccionCreada;
    });
    // when: Creamos la sección
    Seccion seccionCreada = seccionService.create(seccion);

    // then: La sección se crea correctamente.
    Assertions.assertThat(seccionCreada.getId()).isEqualTo(1L);
    Assertions.assertThat(seccionCreada.getNombre()).isEqualTo("Seccion 1");
    Assertions.assertThat(seccionCreada.getDescripcion()).isEqualTo("Seccion create test");

  }

  @Test
  public void create_SeccionWithId_ThrowsIllegalArgumentException() {
    // given: Una nueva sección que ya tiene id
    Seccion seccion = new Seccion(1L, "Seccion 1", "Seccion create test");
    // when: Creamos la seccion
    // then: Lanza una excepcion porque la seccion ya tiene id
    Assertions.assertThatThrownBy(() -> seccionService.create(seccion)).isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  public void update_ReturnsSeccion() {
    // given: Una nueva sección con la descripción actualizada
    Seccion seccionDescripcionActualizada = new Seccion(1L, "Seccion 1", "Seccion create test actualizada");

    Seccion seccion = new Seccion(1L, "Seccion 1", "Seccion create test");

    BDDMockito.given(seccionRepository.findById(1L)).willReturn(Optional.of(seccion));
    BDDMockito.given(seccionRepository.save(seccion)).willReturn(seccionDescripcionActualizada);

    // when: Actualizamos la seccion
    Seccion seccionActualizada = seccionService.update(seccion);

    // then: La sección se actualiza correctamente.
    Assertions.assertThat(seccionActualizada.getId()).isEqualTo(1L);
    Assertions.assertThat(seccionActualizada.getNombre()).isEqualTo("Seccion 1");
    Assertions.assertThat(seccionActualizada.getDescripcion()).isEqualTo("Seccion create test actualizada");

  }

  @Test
  public void update_ThrowsSeccionNotFoundException() {
    // given: Una nueva sección a actualizar
    Seccion seccion = new Seccion(1L, "Seccion 1", "Seccion create test");

    // when: actualizamos la seccion
    // then: Lanza una excepcion porque la seccion no existe
    Assertions.assertThatThrownBy(() -> seccionService.update(seccion)).isInstanceOf(SeccionNotFoundException.class);

  }

  @Test
  public void find_WithId_ReturnsSeccion() {
    BDDMockito.given(seccionRepository.findById(1L))
        .willReturn(Optional.of(new Seccion(1L, "Seccion 1", "Seccion 1 tests")));

    Seccion seccion = seccionService.findById(1L);

    Assertions.assertThat(seccion.getId()).isEqualTo(1L);
    Assertions.assertThat(seccion.getNombre()).isEqualTo("Seccion 1");
    Assertions.assertThat(seccion.getDescripcion()).isEqualTo("Seccion 1 tests");

  }

  @Test
  public void find_NotFound_ThrowsSeccionNotFoundException() throws Exception {
    BDDMockito.given(seccionRepository.findById(1L)).willReturn(Optional.empty());

    Assertions.assertThatThrownBy(() -> seccionService.findById(1L)).isInstanceOf(SeccionNotFoundException.class);
  }

  @Test
  public void findAll_ReturnSeccionList() {
    // given: dos secciones

    Seccion seccion = new Seccion(1L, "Seccion1", "UM1");
    Seccion seccion2 = new Seccion(2L, "Seccion1", "UM2");

    List<Seccion> seccionResponseList = new ArrayList<Seccion>();
    seccionResponseList.add(seccion);
    seccionResponseList.add(seccion2);
    Page<Seccion> pagedResponse = new PageImpl<Seccion>(seccionResponseList);
    BDDMockito.given(seccionRepository.findAll(PageRequest.of(0, 5, Sort.unsorted()))).willReturn(pagedResponse);

    // when: Buscamos las secciones por id
    Pageable pageable = PageRequest.of(0, 5, Sort.unsorted());

    Page<Seccion> seccionsEncontradas = seccionService.findAll(pageable);

    // then: Recuperamos las secciones
    Assertions.assertThat(seccionsEncontradas.getContent().size()).isEqualTo(2);
    Assertions.assertThat(seccionsEncontradas.getContent().containsAll(Arrays.asList(seccion, seccion2)));

  }

  @Test
  public void findAll_ReturnEmptyList() {
    // given: no hay secciones

    Page<Seccion> pagedResponse = new PageImpl<Seccion>(Collections.emptyList());
    BDDMockito.given(seccionRepository.findAll(PageRequest.of(0, 5, Sort.unsorted()))).willReturn(pagedResponse);

    // when: Buscamos las unidades por magnitud id
    Pageable pageable = PageRequest.of(0, 5, Sort.unsorted());

    Page<Seccion> seccionsEncontradas = seccionService.findAll(pageable);

    // then: Recuperamos las unidades medida
    Assertions.assertThat(seccionsEncontradas.getContent().isEmpty());
  }

  @Test
  public void findAllLike_ReturnSeccionList() {
    // given: dos secciones

    Seccion seccion = new Seccion(1L, "Seccion1", "UM1");
    Seccion seccion2 = new Seccion(2L, "Seccion2", "UM2");

    List<Seccion> seccionResponseList = new ArrayList<Seccion>();
    seccionResponseList.add(seccion);
    seccionResponseList.add(seccion2);
    Page<Seccion> pagedResponse = new PageImpl<Seccion>(seccionResponseList);

    BDDMockito
        .given(
            seccionRepository.findAll(ArgumentMatchers.<Specification<Seccion>>any(), ArgumentMatchers.<Pageable>any()))
        .willReturn(pagedResponse);

    // when: Buscamos las secciones por id
    Pageable pageable = PageRequest.of(0, 5, Sort.unsorted());

    SeccionFilter filter = new SeccionFilter();
    filter.setNombre("Seccion");
    Page<Seccion> seccionsEncontradas = seccionService.findAllLike(filter, pageable);

    // then: Recuperamos las secciones
    Assertions.assertThat(seccionsEncontradas.getContent().size()).isEqualTo(2);
    Assertions.assertThat(seccionsEncontradas.getContent().containsAll(Arrays.asList(seccion, seccion2)));

  }

  @Test
  public void findAllLike_ReturnEmptyList() {
    // given: no hay secciones

    Page<Seccion> pagedResponse = new PageImpl<Seccion>(Collections.emptyList());
    BDDMockito
        .given(
            seccionRepository.findAll(ArgumentMatchers.<Specification<Seccion>>any(), ArgumentMatchers.<Pageable>any()))
        .willReturn(pagedResponse);

    // when: Buscamos las unidades por magnitud id
    Pageable pageable = PageRequest.of(0, 5, Sort.unsorted());

    SeccionFilter filter = new SeccionFilter();
    filter.setNombre("Seccion");
    Page<Seccion> seccionsEncontradas = seccionService.findAllLike(filter, pageable);

    // then: Recuperamos las unidades medida
    Assertions.assertThat(seccionsEncontradas.getContent().isEmpty());
  }

  @Test
  public void findAll_WithoutPageable_ReturnSeccionList() {
    // given: dos secciones

    Seccion seccion = new Seccion(1L, "Seccion1", "UM1");
    Seccion seccion2 = new Seccion(2L, "Seccion2", "UM2");

    List<Seccion> seccionResponseList = new ArrayList<Seccion>();
    seccionResponseList.add(seccion);
    seccionResponseList.add(seccion2);

    BDDMockito.given(seccionRepository.findAll()).willReturn(seccionResponseList);

    // when: Buscamos las secciones por id
    List<Seccion> seccionsEncontradas = seccionService.findAll();

    // then: Recuperamos las secciones
    Assertions.assertThat(seccionsEncontradas.size()).isEqualTo(2);
    Assertions.assertThat(seccionsEncontradas.containsAll(Arrays.asList(seccion, seccion2)));

  }

  @Test
  public void findAllLike_WithoutPageable_ReturnEmptyList() {
    // given: no hay secciones
    BDDMockito.given(seccionRepository.findAll()).willReturn(Collections.emptyList());

    List<Seccion> seccionsEncontradas = seccionService.findAll();

    // then: Recuperamos las unidades medida
    Assertions.assertThat(seccionsEncontradas.isEmpty());
  }

  @Test
  public void delete_Success() {
     BDDMockito.doNothing().when(seccionRepository).deleteById(ArgumentMatchers.<Long>any());
     seccionService.delete(ArgumentMatchers.<Long>any());
  }
}