package org.crue.hercules.sgi.catservice.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.crue.hercules.sgi.catservice.enums.EstadoTipoReservableEnum;
import org.crue.hercules.sgi.catservice.exceptions.TipoReservableNotFoundException;
import org.crue.hercules.sgi.catservice.filter.TipoReservableFilter;
import org.crue.hercules.sgi.catservice.model.Seccion;
import org.crue.hercules.sgi.catservice.model.Servicio;
import org.crue.hercules.sgi.catservice.model.TipoReservable;
import org.crue.hercules.sgi.catservice.service.TipoReservableService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.invocation.InvocationOnMock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

/**
 * TipoReservableControllerTest
 */
@WebMvcTest(TipoReservableController.class)
public class TipoReservableControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private TipoReservableService tipoReservableService;

  /** TipoReservable controller path. */
  private static final String CONTROLLER_BASE_PATH = "/tiporeservables";

  @Test
  public void getTipoReservable_ReturnsTipoReservableList() throws Exception {

    BDDMockito.given(tipoReservableService.findAll()).willReturn(Arrays
        .asList(generarMockTipoReservable(1L, "TipoReservable1"), generarMockTipoReservable(2L, "TipoReservable2")));

    mockMvc.perform(MockMvcRequestBuilders.get(CONTROLLER_BASE_PATH + "/all").contentType(MediaType.APPLICATION_JSON))
        .andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(2)))

        .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1))
        .andExpect(MockMvcResultMatchers.jsonPath("$[0].descripcion").value("TipoReservable1"))
        .andExpect(MockMvcResultMatchers.jsonPath("$[0].diasAnteMax").value(2))
        .andExpect(MockMvcResultMatchers.jsonPath("$[0].duracionMin").value(2))
        .andExpect(MockMvcResultMatchers.jsonPath("$[0].estado").value("alta"))
        .andExpect(MockMvcResultMatchers.jsonPath("$[0].horasAnteMin").value(3))
        .andExpect(MockMvcResultMatchers.jsonPath("$[0].horasAnteAnular").value(4))
        .andExpect(MockMvcResultMatchers.jsonPath("$[0].diasVistaMaxCalen").value(3))
        .andExpect(MockMvcResultMatchers.jsonPath("$[0].reservaMulti").value(false))
        .andExpect(MockMvcResultMatchers.jsonPath("$[0].servicio.nombre").value("Servicio1"))

        .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(2))
        .andExpect(MockMvcResultMatchers.jsonPath("$[1].descripcion").value("TipoReservable2"))
        .andExpect(MockMvcResultMatchers.jsonPath("$[1].diasAnteMax").value(2))
        .andExpect(MockMvcResultMatchers.jsonPath("$[1].duracionMin").value(2))
        .andExpect(MockMvcResultMatchers.jsonPath("$[1].estado").value("alta"))
        .andExpect(MockMvcResultMatchers.jsonPath("$[1].horasAnteMin").value(3))
        .andExpect(MockMvcResultMatchers.jsonPath("$[1].horasAnteAnular").value(4))
        .andExpect(MockMvcResultMatchers.jsonPath("$[1].diasVistaMaxCalen").value(3))
        .andExpect(MockMvcResultMatchers.jsonPath("$[1].reservaMulti").value(false))
        .andExpect(MockMvcResultMatchers.jsonPath("$[1].servicio.nombre").value("Servicio2"));

  }

  @Test
  public void getTipoReservable_ReturnsEmptyList() throws Exception {
    BDDMockito.given(tipoReservableService.findAll()).willReturn(Collections.emptyList());

    mockMvc.perform(MockMvcRequestBuilders.get(CONTROLLER_BASE_PATH + "/all")).andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(0)));
  }

  @Test
  public void getTipoReservable_WithId_ReturnsTipoReservable() throws Exception {
    BDDMockito.given(tipoReservableService.findById(ArgumentMatchers.anyLong()))
        .willReturn((generarMockTipoReservable(1L, "TipoReservable1")));

    mockMvc.perform(MockMvcRequestBuilders.get(CONTROLLER_BASE_PATH + "/{id}", 1L)).andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.jsonPath("id").value(1))
        .andExpect(MockMvcResultMatchers.jsonPath("descripcion").value("TipoReservable1"));
    ;
  }

  @Test
  public void getTipoReservable_NotFound_Returns404() throws Exception {
    BDDMockito.given(tipoReservableService.findById(ArgumentMatchers.anyLong())).will((InvocationOnMock invocation) -> {
      throw new TipoReservableNotFoundException(invocation.getArgument(0));
    });
    mockMvc.perform(MockMvcRequestBuilders.get(CONTROLLER_BASE_PATH + "/{id}", 1L)).andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.status().isNotFound());
  }

  @Test
  public void newTipoReservable_ReturnsTipoReservable() throws Exception {
    // given: Un tipo reservable nuevo
    String nuevoTipoReservableJson = "{\"descripcion\": \"TipoReservable1\", \"duracionMin\": 3, \"diasAnteMax\": 2, \"reservaMulti\": true, \"diasVistaMaxCalen\": 3, \"horasAnteAnular\": 4, \"estado\": \"baja\", \"servicio\":{\"id\": \"1\", \"nombre\": \"Servicio1\", \"abreviatura\": \"Serv1\", \"contacto\": \"Contacto 1\", \"seccion\": {\"id\": \"1\",\"nombre\": \"Seccion 1\", \"descripcion\": \"Seccion servicio 1\"}}}";

    TipoReservable tipoReservable = generarMockTipoReservable(1L, "TipoReservable1");

    BDDMockito.given(tipoReservableService.create(ArgumentMatchers.<TipoReservable>any())).willReturn(tipoReservable);

    // when: Creamos un tipo reservable
    mockMvc
        .perform(MockMvcRequestBuilders.post(CONTROLLER_BASE_PATH).contentType(MediaType.APPLICATION_JSON)
            .content(nuevoTipoReservableJson))
        .andDo(MockMvcResultHandlers.print())
        // then: Crea el nuevo tipo reservable y lo devuelve
        .andExpect(MockMvcResultMatchers.status().isCreated()).andExpect(MockMvcResultMatchers.jsonPath("id").value(1))
        .andExpect(MockMvcResultMatchers.jsonPath("descripcion").value("TipoReservable1"));
  }

  @Test
  public void newTipoReservable_Error_Returns400() throws Exception {
    // given: Un tipo reservable nuevo que produce un error al crearse
    String nuevoTipoReservableJson = "{\"descripcion\": \"TipoReservable1\", \"duracionMin\": 3, \"diasAnteMax\": 2, \"reservaMulti\": true, \"diasVistaMaxCalen\": 3, \"horasAnteAnular\": 4, \"estado\": \"baja\", \"servicio\":{\"id\": \"1\", \"nombre\": \"Servicio1\", \"abreviatura\": \"Serv1\", \"contacto\": \"Contacto 1\", \"seccion\": {\"id\": \"1\",\"nombre\": \"Seccion 1\", \"descripcion\": \"Seccion servicio 1\"}}}";

    BDDMockito.given(tipoReservableService.create(ArgumentMatchers.<TipoReservable>any()))
        .willThrow(new IllegalArgumentException());

    // when: Creamos un tipo reservable
    mockMvc
        .perform(MockMvcRequestBuilders.post(CONTROLLER_BASE_PATH).contentType(MediaType.APPLICATION_JSON)
            .content(nuevoTipoReservableJson))
        .andDo(MockMvcResultHandlers.print())
        // then: Devueve un error 400
        .andExpect(MockMvcResultMatchers.status().isBadRequest());

  }

  @Test
  public void replaceTipoReservable_ReturnsTipoReservable() throws Exception {
    // given: Un tipo reservable a modificar
    String replaceTipoReservableJson = "{\"id\": 1, \"descripcion\": \"TipoReservable1\", \"duracionMin\": 3, \"diasAnteMax\": 2, \"reservaMulti\": true, \"diasVistaMaxCalen\": 3, \"horasAnteAnular\": 4, \"estado\": \"baja\", \"servicio\":{\"id\": \"1\", \"nombre\": \"Servicio1\", \"abreviatura\": \"Serv1\", \"contacto\": \"Contacto 1\", \"seccion\": {\"id\": \"1\",\"nombre\": \"Seccion 1\", \"descripcion\": \"Seccion servicio 1\"}}}";

    TipoReservable tipoReservable = generarMockTipoReservable(1L, "Replace TipoReservable1");

    BDDMockito.given(tipoReservableService.update(ArgumentMatchers.<TipoReservable>any())).willReturn(tipoReservable);

    mockMvc
        .perform(MockMvcRequestBuilders.put(CONTROLLER_BASE_PATH + "/{id}", 1L).contentType(MediaType.APPLICATION_JSON)
            .content(replaceTipoReservableJson))
        .andDo(MockMvcResultHandlers.print())
        // then: Modifica el tipo reservable y lo devuelve
        .andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.jsonPath("id").value(1))
        .andExpect(MockMvcResultMatchers.jsonPath("descripcion").value("Replace TipoReservable1"));

  }

  @Test
  public void replaceTipoReservable_NotFound() throws Exception {
    // given: Un tipo reservable a modificar
    String replaceTipoReservableJson = "{\"id\": 1, \"descripcion\": \"TipoReservable1\", \"duracionMin\": 3, \"diasAnteMax\": 2, \"reservaMulti\": true, \"diasVistaMaxCalen\": 3, \"horasAnteAnular\": 4, \"estado\": \"baja\", \"servicio\":{\"id\": \"1\", \"nombre\": \"Servicio1\", \"abreviatura\": \"Serv1\", \"contacto\": \"Contacto 1\", \"seccion\": {\"id\": \"1\",\"nombre\": \"Seccion 1\", \"descripcion\": \"Seccion servicio 1\"}}}";

    BDDMockito.given(tipoReservableService.update(ArgumentMatchers.<TipoReservable>any()))
        .will((InvocationOnMock invocation) -> {
          throw new TipoReservableNotFoundException(((TipoReservable) invocation.getArgument(0)).getId());
        });
    mockMvc
        .perform(MockMvcRequestBuilders.put(CONTROLLER_BASE_PATH + "/{id}", 1L).contentType(MediaType.APPLICATION_JSON)
            .content(replaceTipoReservableJson))
        .andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isNotFound());

  }

  @Test
  public void getTipoReservablesPagedFilter_ReturnsTipoReservablesList() throws Exception {

    TipoReservableFilter filter = new TipoReservableFilter();
    filter.setDescripcion("TipoReservable1");

    BDDMockito
        .given(tipoReservableService.findAllLike(ArgumentMatchers.<TipoReservableFilter>any(),
            ArgumentMatchers.<PageRequest>any()))
        .willReturn(new PageImpl<TipoReservable>(Arrays.asList(generarMockTipoReservable(1L, "TipoReservable1"),
            generarMockTipoReservable(2L, "TipoReservable2"))));

    mockMvc.perform(MockMvcRequestBuilders.get(CONTROLLER_BASE_PATH + "/all/pagefiltered")

        .contentType(MediaType.APPLICATION_JSON).param("descripcion", "TipoReservable1"))
        .andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isOk())
        .andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.jsonPath("$.totalPages").value(1))
        .andExpect(MockMvcResultMatchers.jsonPath("$.totalElements").value(2))
        .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].descripcion").value("TipoReservable1"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].diasAnteMax").value(2))
        .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].duracionMin").value(2))
        .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].estado").value("alta"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].horasAnteMin").value(3))
        .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].horasAnteAnular").value(4))
        .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].diasVistaMaxCalen").value(3))
        .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].reservaMulti").value(false))
        .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].servicio.nombre").value("Servicio1"))

        .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].id").value(2))
        .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].descripcion").value("TipoReservable2"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].diasAnteMax").value(2))
        .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].duracionMin").value(2))
        .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].estado").value("alta"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].horasAnteMin").value(3))
        .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].horasAnteAnular").value(4))
        .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].diasVistaMaxCalen").value(3))
        .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].reservaMulti").value(false))
        .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].servicio.nombre").value("Servicio2"));

  }

  @Test
  public void getTipoReservablePageable_ReturnsTipoReservablePage() throws Exception {

    List<TipoReservable> tipoReservableList = new ArrayList<>();
    tipoReservableList.add(generarMockTipoReservable(1L, "TipoReservable1"));
    tipoReservableList.add(generarMockTipoReservable(2L, "TipoReservable2"));

    Page<TipoReservable> pagedTipoReservable = new PageImpl<TipoReservable>(tipoReservableList);

    BDDMockito.given(tipoReservableService.findAll(ArgumentMatchers.<PageRequest>any()))
        .willReturn(pagedTipoReservable);

    mockMvc
        .perform(MockMvcRequestBuilders.get(CONTROLLER_BASE_PATH + "/all/page").contentType(MediaType.APPLICATION_JSON))
        .andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.totalPages").value(1))
        .andExpect(MockMvcResultMatchers.jsonPath("$.totalElements").value(2))
        .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].descripcion").value("TipoReservable1"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].diasAnteMax").value(2))
        .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].duracionMin").value(2))
        .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].estado").value("alta"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].horasAnteMin").value(3))
        .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].horasAnteAnular").value(4))
        .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].diasVistaMaxCalen").value(3))
        .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].reservaMulti").value(false))
        .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].servicio.nombre").value("Servicio1"))

        .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].id").value(2))
        .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].descripcion").value("TipoReservable2"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].diasAnteMax").value(2))
        .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].duracionMin").value(2))
        .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].estado").value("alta"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].horasAnteMin").value(3))
        .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].horasAnteAnular").value(4))
        .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].diasVistaMaxCalen").value(3))
        .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].reservaMulti").value(false))
        .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].servicio.nombre").value("Servicio2"));

  }

  @Test
  public void removeTipoReservable_ReturnsOk() throws Exception {
    mockMvc
        .perform(
            MockMvcRequestBuilders.delete(CONTROLLER_BASE_PATH + "/{id}", 1L).contentType(MediaType.APPLICATION_JSON))
        .andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isOk());
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
