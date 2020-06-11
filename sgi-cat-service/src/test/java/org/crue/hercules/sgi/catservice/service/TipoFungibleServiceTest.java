package org.crue.hercules.sgi.catservice.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.crue.hercules.sgi.catservice.exceptions.TipoFungibleNotFoundException;
import org.crue.hercules.sgi.catservice.filter.TipoFungibleFilter;
import org.crue.hercules.sgi.catservice.model.Seccion;
import org.crue.hercules.sgi.catservice.model.Servicio;
import org.crue.hercules.sgi.catservice.model.TipoFungible;
import org.crue.hercules.sgi.catservice.repository.TipoFungibleRepository;
import org.crue.hercules.sgi.catservice.service.impl.TipoFungibleServiceImpl;
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
 * TipoFungibleServiceTest
 */
@ExtendWith(MockitoExtension.class)
public class TipoFungibleServiceTest {
  @Mock
  private TipoFungibleRepository tipoFungibleRepository;

  @Mock
  private TipoFungibleService tipoFungibleService;

  @BeforeEach
  public void setUp() throws Exception {
    tipoFungibleService = new TipoFungibleServiceImpl(tipoFungibleRepository);
  }

  @Test
  public void findAll_ReturnsTipoFungibleList() {

    Seccion seccion = new Seccion(1L, "Seccion 1", "Seccion create test");
    Servicio servicio = new Servicio(1L, "Servicio 1", "Serv1", "Nombre Apellidos", seccion);

    // given: dos tipos fungibles
    TipoFungible tipoFungible = new TipoFungible(1L, "TipoFungible1", servicio);
    TipoFungible tipoFungible2 = new TipoFungible(2L, "TipoFungible1", servicio);

    List<TipoFungible> tipoFungibleResponseList = new ArrayList<TipoFungible>();
    tipoFungibleResponseList.add(tipoFungible);
    tipoFungibleResponseList.add(tipoFungible2);

    BDDMockito.given(tipoFungibleRepository.findAll()).willReturn(tipoFungibleResponseList);

    // when: Se realiza la búsqueda de los tipos fungibles
    List<TipoFungible> tipoFungibleList = tipoFungibleService.findAll();

    // then: Recuperamos los tipos fungibles
    Assertions.assertThat(tipoFungibleList.size()).isEqualTo(2);
    Assertions.assertThat(tipoFungibleList.containsAll(Arrays.asList(tipoFungible, tipoFungible2)));

  }

  @Test
  public void findAll_ReturnsEmptyList() throws Exception {

    // given: Una lista vacía
    BDDMockito.given(tipoFungibleService.findAll()).willReturn(Collections.emptyList());

    // when: Se realiza la búsqueda de tipos fungibles
    List<TipoFungible> tipoFungibleList = tipoFungibleService.findAll();

    // then: Recuperamos la lista vacía
    Assertions.assertThat(tipoFungibleList);

  }

  @Test
  public void find_WithId_ReturnsTipoFungible() {
    // given: El id de un tipo fungible
    Seccion seccion = new Seccion(1L, "Seccion 1", "Seccion create test");
    Servicio servicio = new Servicio(1L, "Servicio 1", "Serv1", "Nombre Apellidos", seccion);

    BDDMockito.given(tipoFungibleRepository.findById(1L))
        .willReturn(Optional.of(new TipoFungible(1L, "TipoFungible1", servicio)));

    // when: Buscamos por id
    TipoFungible tipoFungible = tipoFungibleService.findById(1L);

    // then: Recuperamos el tipo fungible
    Assertions.assertThat(tipoFungible.getId()).isEqualTo(1L);
    Assertions.assertThat(tipoFungible.getNombre()).isEqualTo("TipoFungible1");
    Assertions.assertThat(tipoFungible.getServicio()).isEqualTo(servicio);

  }

  @Test
  public void find_NotFound_ThrowsTipoFungibleNotFoundException() throws Exception {
    BDDMockito.given(tipoFungibleRepository.findById(1L)).willReturn(Optional.empty());

    Assertions.assertThatThrownBy(() -> tipoFungibleService.findById(1L))
        .isInstanceOf(TipoFungibleNotFoundException.class);
  }

  @Test
  public void findAllPageable_ReturnsTipoFungibleList() {
    // given: dos tipos fungibles

    Seccion seccion = new Seccion(1L, "Seccion 1", "Seccion create test");
    Servicio servicio = new Servicio(1L, "Servicio 1", "Serv1", "Nombre Apellidos", seccion);

    TipoFungible tipoFungible = new TipoFungible(1L, "TipoFungible1", servicio);
    TipoFungible tipoFungible2 = new TipoFungible(2L, "TipoFungible1", servicio);

    List<TipoFungible> tipoFungibleResponseList = new ArrayList<TipoFungible>();
    tipoFungibleResponseList.add(tipoFungible);
    tipoFungibleResponseList.add(tipoFungible2);
    Page<TipoFungible> pagedResponse = new PageImpl<TipoFungible>(tipoFungibleResponseList);
    BDDMockito.given(tipoFungibleRepository.findAll(PageRequest.of(0, 5, Sort.unsorted()))).willReturn(pagedResponse);

    // when: Buscamos los tipos fungibles por magnitud id
    Pageable pageable = PageRequest.of(0, 5, Sort.unsorted());

    Page<TipoFungible> tipoFungibleEncontrados = tipoFungibleService.findAll(pageable);

    // then: Recuperamos los tipos fungibles
    Assertions.assertThat(tipoFungibleEncontrados.getContent().size()).isEqualTo(2);
    Assertions.assertThat(tipoFungibleEncontrados.getContent().containsAll(Arrays.asList(tipoFungible, tipoFungible2)));

  }

  @Test
  public void findAllPageable_ReturnsEmptyList() {
    // given: Una lista vacía
    Page<TipoFungible> pagedResponse = new PageImpl<TipoFungible>(Collections.emptyList());
    Pageable pageable = PageRequest.of(0, 5, Sort.unsorted());

    BDDMockito.given(tipoFungibleRepository.findAll(PageRequest.of(0, 5, Sort.unsorted()))).willReturn(pagedResponse);

    // when: Se realiza la búsqueda de tipos fungibles
    Page<TipoFungible> tipoFungiblePageableList = tipoFungibleService.findAll(pageable);

    // then: Recuperamos la lista vacía
    Assertions.assertThat(tipoFungiblePageableList);
  }

  @Test
  public void findAllLike_ReturnsTipoFungibleList() {
    // given: dos tipos fungibles
    Seccion seccion = new Seccion(1L, "Seccion 1", "Seccion create test");
    Servicio servicio = new Servicio(1L, "Servicio 1", "Serv1", "Nombre Apellidos", seccion);

    TipoFungible tipoFungible = new TipoFungible(1L, "TipoFungible1", servicio);
    TipoFungible tipoFungible2 = new TipoFungible(2L, "TipoFungible1", servicio);

    List<TipoFungible> tipoFungibleResponseList = new ArrayList<TipoFungible>();
    tipoFungibleResponseList.add(tipoFungible);
    tipoFungibleResponseList.add(tipoFungible2);

    Page<TipoFungible> pagedResponse = new PageImpl<TipoFungible>(tipoFungibleResponseList);

    BDDMockito.given(tipoFungibleRepository.findAll(ArgumentMatchers.<Specification<TipoFungible>>any(),
        ArgumentMatchers.<Pageable>any())).willReturn(pagedResponse);

    Pageable pageable = PageRequest.of(0, 5, Sort.unsorted());
    TipoFungibleFilter tipoFungibleFilter = new TipoFungibleFilter();
    tipoFungibleFilter.setId(1L);
    tipoFungibleFilter.setNombre("TipoFungible1");
    tipoFungibleFilter.setServicioId(servicio.getId());

    Page<TipoFungible> tipoFungibleEncontrados = tipoFungibleService.findAllLike(tipoFungibleFilter, pageable);

    // then: Recuperamos los tipos fungibles
    Assertions.assertThat(tipoFungibleEncontrados.getContent().size()).isEqualTo(2);
    Assertions.assertThat(tipoFungibleEncontrados.getContent().containsAll(Arrays.asList(tipoFungible, tipoFungible2)));

  }

  @Test
  public void findAllLike_ReturnsEmptyList() {
    // given: Una lista vacía
    Page<TipoFungible> pagedResponse = new PageImpl<TipoFungible>(Collections.emptyList());
    Pageable pageable = PageRequest.of(0, 5, Sort.unsorted());
    TipoFungibleFilter tipoFungibleFilter = new TipoFungibleFilter();

    BDDMockito.given(tipoFungibleRepository.findAll(ArgumentMatchers.<Specification<TipoFungible>>any(),
        ArgumentMatchers.<Pageable>any())).willReturn(pagedResponse);

    // when: Se realiza la búsqueda de tipos fungibles
    Page<TipoFungible> tipoFungibleList = tipoFungibleService.findAllLike(tipoFungibleFilter, pageable);

    // then: Recuperamos la lista vacía
    Assertions.assertThat(tipoFungibleList);
  }

  @Test
  public void create_ReturnsTipoFungible() {
    // given: Un tipo fungible
    Seccion seccion = new Seccion(1L, "Seccion 1", "Seccion create test");
    Servicio servicio = new Servicio(1L, "Servicio 1", "Serv1", "Nombre Apellidos", seccion);

    TipoFungible tipoFungible = new TipoFungible(null, "TipoFungible1", servicio);

    BDDMockito.given(tipoFungibleRepository.save(tipoFungible)).will((InvocationOnMock invocation) -> {
      TipoFungible tipoFungibleCreada = invocation.getArgument(0);
      tipoFungibleCreada.setId(1L);
      return tipoFungibleCreada;
    });
    // when: Creamos el tipo fungible
    TipoFungible tipoFungibleCreada = tipoFungibleService.create(tipoFungible);

    // then: El tipo fungible se crea correctamente.
    Assertions.assertThat(tipoFungibleCreada.getId()).isEqualTo(1L);
    Assertions.assertThat(tipoFungibleCreada.getNombre()).isEqualTo("TipoFungible1");
    Assertions.assertThat(tipoFungibleCreada.getServicio()).isEqualTo(servicio);

  }

  @Test
  public void create_TipoFungibleWithId_ThrowsIllegalArgumentException() {
    // given: Un nuevo tipo fungible que ya tiene id
    Seccion seccion = new Seccion(1L, "Seccion 1", "Seccion create test");
    Servicio servicio = new Servicio(1L, "Servicio 1", "Serv1", "Nombre Apellidos", seccion);

    TipoFungible tipoFungible = new TipoFungible(1L, "TipoFungible1", servicio);

    // then: Lanza una excepcion porque el tipo fungible ya tiene id
    Assertions.assertThatThrownBy(() -> tipoFungibleService.create(tipoFungible))
        .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  public void replace_ReturnsTipoFungible() {
    // given: Un nuevo tipo fungible con el servicio actualizado
    Seccion seccion = new Seccion(1L, "Seccion 1", "Seccion create test");
    Servicio servicio = new Servicio(1L, "Servicio 1", "Serv1", "Nombre Apellidos", seccion);

    TipoFungible tipoFungibleServicioActualizado = new TipoFungible(1L, "TipoFungible1 Actualizado", servicio);

    TipoFungible tipoFungible = new TipoFungible(1L, "TipoFungible1", servicio);

    BDDMockito.given(tipoFungibleRepository.findById(1L)).willReturn(Optional.of(tipoFungible));
    BDDMockito.given(tipoFungibleRepository.save(tipoFungible)).willReturn(tipoFungibleServicioActualizado);

    // when: Actualizamos el tipo fungible
    TipoFungible tipoFungibleActualizado = tipoFungibleService.update(tipoFungible);

    // then: El tipo fungible se actualiza correctamente.
    Assertions.assertThat(tipoFungibleActualizado.getId()).isEqualTo(1L);
    Assertions.assertThat(tipoFungibleActualizado.getNombre()).isEqualTo("TipoFungible1 Actualizado");
    Assertions.assertThat(tipoFungibleActualizado.getServicio()).isEqualTo(servicio);

  }

  @Test
  public void replace_ThrowsTipoFungibleNotFoundException() {
    // given: Un nuevo tipo fungible a actualizar
    Seccion seccion = new Seccion(1L, "Seccion 1", "Seccion create test");
    Servicio servicio = new Servicio(1L, "Servicio 1", "Serv1", "Nombre Apellidos", seccion);

    TipoFungible tipoFungible = new TipoFungible(1L, "TipoFungible1", servicio);

    // then: Lanza una excepcion porque el tipo fungible no existe
    Assertions.assertThatThrownBy(() -> tipoFungibleService.update(tipoFungible))
        .isInstanceOf(TipoFungibleNotFoundException.class);

  }

  @Test
  public void remove() {
    BDDMockito.doNothing().when(tipoFungibleRepository).deleteById(ArgumentMatchers.<Long>any());
    tipoFungibleService.delete(ArgumentMatchers.<Long>any());
  }
}