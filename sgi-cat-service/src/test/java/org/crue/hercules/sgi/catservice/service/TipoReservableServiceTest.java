package org.crue.hercules.sgi.catservice.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.crue.hercules.sgi.catservice.enums.EstadoTipoReservableEnum;
import org.crue.hercules.sgi.catservice.exceptions.TipoReservableNotFoundException;
import org.crue.hercules.sgi.catservice.filter.TipoReservableFilter;
import org.crue.hercules.sgi.catservice.model.Seccion;
import org.crue.hercules.sgi.catservice.model.Servicio;
import org.crue.hercules.sgi.catservice.model.TipoReservable;
import org.crue.hercules.sgi.catservice.repository.TipoReservableRepository;
import org.crue.hercules.sgi.catservice.service.impl.TipoReservableServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

/**
 * TipoReservableServiceTest
 */
@ExtendWith(MockitoExtension.class)
public class TipoReservableServiceTest {

  @Mock
  private TipoReservableRepository tipoReservableRepository;

  private TipoReservableService tipoReservableService;

  @BeforeEach
  public void setUp() throws Exception {
    tipoReservableService = new TipoReservableServiceImpl(tipoReservableRepository);
  }

  @Test
  public void find_WithId_ReturnsTipoReservable() {
    BDDMockito.given(tipoReservableRepository.findById(1L))
        .willReturn(Optional.of(generarMockTipoReservable(1L, "TipoReservable1")));

    TipoReservable tipoReservable = tipoReservableService.findById(1L);

    Assertions.assertThat(tipoReservable.getId()).isEqualTo(1L);

    Assertions.assertThat(tipoReservable.getDescripcion()).isEqualTo("TipoReservable1");

  }

  @Test
  public void find_NotFound_ThrowsTipoReservableNotFoundException() throws Exception {
    BDDMockito.given(tipoReservableRepository.findById(1L)).willReturn(Optional.empty());

    Assertions.assertThatThrownBy(() -> tipoReservableService.findById(1L))
        .isInstanceOf(TipoReservableNotFoundException.class);
  }

  @Test
  public void findAllPageable_ReturnTipoReservablePage() {
    // given: dos tipos de reservables

    TipoReservable tipoReservable1 = generarMockTipoReservable(1L, "TipoReservable1");
    TipoReservable tipoReservable2 = generarMockTipoReservable(2L, "TipoReservable2");

    List<TipoReservable> tipoReservableResponseList = new ArrayList<TipoReservable>();
    tipoReservableResponseList.add(tipoReservable1);
    tipoReservableResponseList.add(tipoReservable2);
    Page<TipoReservable> pagedResponse = new PageImpl<TipoReservable>(tipoReservableResponseList);
    BDDMockito.given(tipoReservableRepository.findAll(PageRequest.of(0, 5, Sort.unsorted()))).willReturn(pagedResponse);

    // when: Buscamos los tipos de reservables por id
    Pageable pageable = PageRequest.of(0, 5, Sort.unsorted());

    Page<TipoReservable> tipoReservablesEncontrados = tipoReservableService.findAll(pageable);

    // then: Recuperamos los tipos de reservables
    Assertions.assertThat(tipoReservablesEncontrados.getContent().size()).isEqualTo(2);
    Assertions.assertThat(
        tipoReservablesEncontrados.getContent().containsAll(Arrays.asList(tipoReservable1, tipoReservable2)));

  }

  @Test
  public void findAllLikePageable_ReturnTipoReservablePage() {
    // given: dos tipos de reservables

    TipoReservable tipoReservable1 = generarMockTipoReservable(1L, "TipoReservable1");
    TipoReservable tipoReservable2 = generarMockTipoReservable(2L, "TipoReservable2");

    List<TipoReservable> tipoReservableResponseList = new ArrayList<TipoReservable>();
    tipoReservableResponseList.add(tipoReservable1);
    tipoReservableResponseList.add(tipoReservable2);
    Page<TipoReservable> pagedResponse = new PageImpl<TipoReservable>(tipoReservableResponseList);
    BDDMockito.given(tipoReservableRepository.findAll(ArgumentMatchers.<Specification<TipoReservable>>any(),
        ArgumentMatchers.<Pageable>any())).willReturn(pagedResponse);

    // when: Buscamos tipo reservable mediante filtro
    Pageable pageable = PageRequest.of(0, 5, Sort.unsorted());

    TipoReservableFilter filter = new TipoReservableFilter();
    filter.setDescripcion("TipoReservable2");
    Page<TipoReservable> tipoReservableBuscado = tipoReservableService.findAllLike(filter, pageable);

    // then: Recuperamos los tipos de reservables
    Assertions.assertThat(tipoReservableBuscado.getContent().size()).isEqualTo(2);
    Assertions
        .assertThat(tipoReservableBuscado.getContent().containsAll(Arrays.asList(tipoReservable1, tipoReservable2)));

  }

  @Test
  public void findAllLike_ReturnEmptyList() {
    // given: no hay TipoReservable

    Page<TipoReservable> pagedResponse = new PageImpl<TipoReservable>(Collections.emptyList());
    BDDMockito.given(tipoReservableRepository.findAll(ArgumentMatchers.<Specification<TipoReservable>>any(),
        ArgumentMatchers.<Pageable>any())).willReturn(pagedResponse);

    // when: Buscamos tipo reservable mediante filtro
    Pageable pageable = PageRequest.of(0, 5, Sort.unsorted());

    TipoReservableFilter filter = new TipoReservableFilter();
    filter.setDescripcion("TipoReservable2");
    Page<TipoReservable> tipoReservableBuscado = tipoReservableService.findAllLike(filter, pageable);

    // then: Recuperamos los tipos de reservable vacíos
    Assertions.assertThat(tipoReservableBuscado.getContent().isEmpty());
  }

  @Test
  public void create_ReturnsTipoReservable() {
    // given: Un nuevo proyecto con un código que no esta repetido
    TipoReservable tipoReservableNew = generarMockTipoReservable(null, "TipoReservableNew");

    TipoReservable tipoReservable = generarMockTipoReservable(1L, "TipoReservableNew");

    BDDMockito.given(tipoReservableRepository.save(tipoReservableNew)).willReturn(tipoReservable);

    // when: Creamos el proyecto
    TipoReservable tipoReservableCreado = tipoReservableService.create(tipoReservableNew);

    // then: El proyecto se crea correctamente
    Assertions.assertThat(tipoReservableCreado).isNotNull();
    Assertions.assertThat(tipoReservableCreado.getId()).isEqualTo(1L);
    Assertions.assertThat(tipoReservableCreado.getDescripcion()).isEqualTo("TipoReservableNew");
  }

  @Test
  public void create_TipoReservableWithId_ThrowsIllegalArgumentException() {
    // given: Un nuevo tipo de reservable que ya tiene id
    TipoReservable tipoReservableNew = generarMockTipoReservable(1L, "TipoReservableNew");
    // when: Creamos el tipo de reservable
    // then: Lanza una excepcion porque el tipo reservable ya tiene id
    Assertions.assertThatThrownBy(() -> tipoReservableService.create(tipoReservableNew))
        .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  public void update_ReturnsTipoReservable() {
    // given: Un tipo de reservable con la descripción actualizada
    TipoReservable tipoReservableActualizado = generarMockTipoReservable(1L, "TipoReservableUpdate");

    TipoReservable tipoReservable = generarMockTipoReservable(1L, "TipoReservable");

    BDDMockito.given(tipoReservableRepository.findById(1L)).willReturn(Optional.of(tipoReservable));
    BDDMockito.given(tipoReservableRepository.save(tipoReservable)).willReturn(tipoReservableActualizado);

    // when: Actualizamos el tipo de reservable
    tipoReservableActualizado = tipoReservableService.update(tipoReservable);

    // then: El tipo de reservable se actualiza correctamente.
    Assertions.assertThat(tipoReservableActualizado.getId()).isEqualTo(1L);
    Assertions.assertThat(tipoReservableActualizado.getDescripcion()).isEqualTo("TipoReservableUpdate");

  }

  @Test
  public void update_ThrowsSeccionNotFoundException() {
    // given: Un tipo de reservable a actualizar
    TipoReservable tipoReservable = generarMockTipoReservable(1L, "TipoReservable");

    // when: actualizamos el tipo de reservable
    // then: Lanza una excepcion porque el tipo de reservable no existe
    Assertions.assertThatThrownBy(() -> tipoReservableService.update(tipoReservable))
        .isInstanceOf(TipoReservableNotFoundException.class);

  }

  @Test
  public void delete_Success() {
    BDDMockito.doNothing().when(tipoReservableRepository).deleteById(ArgumentMatchers.<Long>any());
    tipoReservableService.delete(ArgumentMatchers.<Long>any());
  }

  /**
   * Función que devuelve un objeto TipoReservable
   * 
   * @param id          id del tipoReservable
   * @param descripcion la descripción del tipo de reservable
   * @return el objeto tipo reservable
   */

  public TipoReservable generarMockTipoReservable(Long id, String descripcion) {
    Seccion seccion = new Seccion();
    seccion.setId(id);
    seccion.setDescripcion("Seccion" + id);
    seccion.setNombre("Secc" + id);

    Servicio servicio = new Servicio();
    servicio.setId(id);
    servicio.setAbreviatura("SV" + id);
    servicio.setNombre("Servicio" + id);
    servicio.setContacto("Contacto" + id);
    servicio.setSeccion(seccion);

    TipoReservable tipoReservable = new TipoReservable();
    tipoReservable.setId(id);
    tipoReservable.setDescripcion(descripcion);
    tipoReservable.setDiasAnteMax(2);
    tipoReservable.setDuracionMin(2);
    tipoReservable.setEstado(EstadoTipoReservableEnum.ALTA);
    tipoReservable.setHorasAnteMin(3);
    tipoReservable.setHorasAnteAnular(4);
    tipoReservable.setDiasVistaMaxCalen(3);
    tipoReservable.setReservaMulti(false);
    tipoReservable.setServicio(servicio);

    return tipoReservable;
  }
}