package org.crue.hercules.sgi.catservice.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.crue.hercules.sgi.catservice.exceptions.SeccionNotFoundException;
import org.crue.hercules.sgi.catservice.filter.SeccionFilter;
import org.crue.hercules.sgi.catservice.model.Seccion;
import org.crue.hercules.sgi.catservice.service.SeccionService;
import org.crue.hercules.sgi.catservice.util.ConstantesCat;
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
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

/**
 * SeccionControllerTest
 */
@WebMvcTest(SeccionController.class)
public class SeccionControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private SeccionService seccionService;

  @Test
  public void getSecciones_ReturnsSeccionesList() throws Exception {

    // given: dos secciones
    BDDMockito.given(seccionService.findAll()).willReturn(Arrays.asList(
        new Seccion(1L, "Seccion 1", "Seccion create test"), new Seccion(2L, "Seccion 2", "Seccion create test")));

    // when: se realiza la búsqueda de las secciones
    mockMvc
        .perform(MockMvcRequestBuilders.get(ConstantesCat.SECCION_CONTROLLER_BASE_PATH + ConstantesCat.PATH_ALL)
            .contentType(MediaType.APPLICATION_JSON))
        // then: se recuperan las dos secciones
        .andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(2)))

        .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1))
        .andExpect(MockMvcResultMatchers.jsonPath("$[0].nombre").value("Seccion 1"))
        .andExpect(MockMvcResultMatchers.jsonPath("$[0].descripcion").value("Seccion create test"))

        .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(2))
        .andExpect(MockMvcResultMatchers.jsonPath("$[1].nombre").value("Seccion 2"))
        .andExpect(MockMvcResultMatchers.jsonPath("$[1].descripcion").value("Seccion create test"));

  }

  @Test
  public void getSecciones_ReturnsEmptyList() throws Exception {
    // given: lista de secciones vacía
    BDDMockito.given(seccionService.findAll()).willReturn(Collections.emptyList());

    // when: se realiza la búsqueda de las secciones
    mockMvc.perform(MockMvcRequestBuilders.get(ConstantesCat.SECCION_CONTROLLER_BASE_PATH + ConstantesCat.PATH_ALL))
        .andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isOk());
  }

  @Test
  public void getSeccionesPageable_ReturnsSeccionsList() throws Exception {

    // given: lista de 2 secciones
    List<Seccion> seccionList = new ArrayList<>();
    seccionList.add(new Seccion(1L, "Seccion 1", "Seccion create test"));
    seccionList.add(new Seccion(2L, "Seccion 2", "Seccion create test"));

    Page<Seccion> pagedSeccion = new PageImpl<Seccion>(seccionList);

    BDDMockito.given(seccionService.findAll(ArgumentMatchers.<Pageable>any())).willReturn(pagedSeccion);

    // when: se realiza la búsqueda de las secciones
    mockMvc
        .perform(MockMvcRequestBuilders
            .get(ConstantesCat.SECCION_CONTROLLER_BASE_PATH + ConstantesCat.PATH_ALL + ConstantesCat.PATH_PAGE)

            .contentType(MediaType.APPLICATION_JSON).param("descripcion", "Seccion").param("page", "0")
            .param("size", "10").param("sort", "id,desc"))
        .andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isOk())
        .andDo(MockMvcResultHandlers.print())
        // Then: se recupera page con dos secciones
        .andExpect(MockMvcResultMatchers.jsonPath("$").hasJsonPath())
        .andExpect(MockMvcResultMatchers.jsonPath("$.totalElements").value(2))
        .andExpect(MockMvcResultMatchers.jsonPath("$.totalPages").value(1))
        .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].id").value(1))
        .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].nombre").value("Seccion 1"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].descripcion").value("Seccion create test"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].id").value(2))
        .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].nombre").value("Seccion 2"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].descripcion").value("Seccion create test"));
  }

  @Test
  public void getSeccionesPageable_ReturnsEmptyList() throws Exception {

    // given: lista de secciones vacía
    List<Seccion> seccionList = new ArrayList<>();

    Page<Seccion> pagedSeccion = new PageImpl<Seccion>(seccionList);

    BDDMockito.given(seccionService.findAll(ArgumentMatchers.<Pageable>any())).willReturn(pagedSeccion);

    // when: se realiza la búsqueda de las secciones
    mockMvc
        .perform(MockMvcRequestBuilders
            .get(ConstantesCat.SECCION_CONTROLLER_BASE_PATH + ConstantesCat.PATH_ALL + ConstantesCat.PATH_PAGE)

            .contentType(MediaType.APPLICATION_JSON).param("descripcion", "Seccion").param("page", "0")
            .param("size", "10").param("sort", "id,desc"))
        .andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isOk())
        .andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.jsonPath("$").hasJsonPath())
        // then: se recupera page vacía
        .andExpect(MockMvcResultMatchers.jsonPath("$.totalElements").value(0))
        .andExpect(MockMvcResultMatchers.jsonPath("$.totalPages").value(1));
  }

  @Test
  public void getSeccionsPagedFilter_ReturnsSeccionsList() throws Exception {
    // given: lista de 2 secciones
    List<Seccion> seccionList = new ArrayList<>();
    seccionList.add(new Seccion(1L, "Seccion 1", "Seccion create test"));
    seccionList.add(new Seccion(2L, "Seccion 2", "Seccion create test"));

    Page<Seccion> pagedSeccion = new PageImpl<Seccion>(seccionList);

    BDDMockito
        .given(seccionService.findAllLike(ArgumentMatchers.<SeccionFilter>any(), ArgumentMatchers.<Pageable>any()))
        .willReturn(pagedSeccion);

    // when: se realiza la búsqueda de las secciones filtradas
    mockMvc
        .perform(MockMvcRequestBuilders
            .get(ConstantesCat.SECCION_CONTROLLER_BASE_PATH + ConstantesCat.PATH_ALL + ConstantesCat.PATH_PAGEFILTERED)

            .contentType(MediaType.APPLICATION_JSON).param("descripcion", "Seccion").param("page", "0")
            .param("size", "10").param("sort", "id,desc"))
        .andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isOk())
        .andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.jsonPath("$").hasJsonPath())
        // then: se recupera page con dos secciones
        .andExpect(MockMvcResultMatchers.jsonPath("$.totalElements").value(2))
        .andExpect(MockMvcResultMatchers.jsonPath("$.totalPages").value(1))
        .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].id").value(1))
        .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].nombre").value("Seccion 1"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].descripcion").value("Seccion create test"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].id").value(2))
        .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].nombre").value("Seccion 2"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].descripcion").value("Seccion create test"));
  }

  @Test
  public void getSeccionsPagedFilter_ReturnsEmptyList() throws Exception {
    // given: lista de secciones vacía
    List<Seccion> seccionList = new ArrayList<>();

    Page<Seccion> pagedSeccion = new PageImpl<Seccion>(seccionList);

    BDDMockito
        .given(seccionService.findAllLike(ArgumentMatchers.<SeccionFilter>any(), ArgumentMatchers.<Pageable>any()))
        .willReturn(pagedSeccion);

    // when: se realiza la búsqueda de las secciones filtradas
    mockMvc
        .perform(MockMvcRequestBuilders
            .get(ConstantesCat.SECCION_CONTROLLER_BASE_PATH + ConstantesCat.PATH_ALL + ConstantesCat.PATH_PAGEFILTERED)

            .contentType(MediaType.APPLICATION_JSON).param("descripcion", "Seccion").param("page", "0")
            .param("size", "10").param("sort", "id,desc"))
        .andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isOk())
        .andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.jsonPath("$").hasJsonPath())
        // then: se recupera page vacía
        .andExpect(MockMvcResultMatchers.jsonPath("$.totalElements").value(0))
        .andExpect(MockMvcResultMatchers.jsonPath("$.totalPages").value(1));
  }

  @Test
  public void newSeccion_ReturnsSeccion() throws Exception {
    // given: Una seccion nueva
    String nuevaSeccionJson = "{\"nombre\": \"Seccion1\", \"descripcion\": \"Seccion 1\"}";

    Seccion seccion = new Seccion();
    seccion.setId(1L);
    seccion.setNombre("Seccion1");
    seccion.setDescripcion("Seccion 1");

    BDDMockito.given(seccionService.create(ArgumentMatchers.<Seccion>any())).willReturn(seccion);

    // when: Creamos una seccion
    mockMvc
        .perform(MockMvcRequestBuilders.post(ConstantesCat.SECCION_CONTROLLER_BASE_PATH)
            .contentType(MediaType.APPLICATION_JSON).content(nuevaSeccionJson))
        .andDo(MockMvcResultHandlers.print())
        // then: Crea la nueva seccion y la devuelve
        .andExpect(MockMvcResultMatchers.status().isCreated()).andExpect(MockMvcResultMatchers.jsonPath("id").value(1))
        .andExpect(MockMvcResultMatchers.jsonPath("nombre").value("Seccion1"));
  }

  // @Test
  public void newSeccion_Error_Returns400() throws Exception {
    // given: Una seccion nueva que produce un error al crearse
    String nuevoSeccionJson = "{\"id\": 1, \"nombre\": \"Seccion1\", \"descripcion\": \"Seccion 1\"}";

    BDDMockito.given(seccionService.create(ArgumentMatchers.<Seccion>any())).willThrow(new IllegalArgumentException());

    // when: Creamos una seccion
    mockMvc
        .perform(MockMvcRequestBuilders.post(ConstantesCat.SECCION_CONTROLLER_BASE_PATH)
            .contentType(MediaType.APPLICATION_JSON).content(nuevoSeccionJson))
        .andDo(MockMvcResultHandlers.print())
        // then: Devueve un error 400
        .andExpect(MockMvcResultMatchers.status().isBadRequest());

  }

  @Test
  public void replaceSeccion_ReturnsSeccion() throws Exception {
    // given: Una seccion a modificar
    String replaceSeccionJson = "{\"nombre\": \"Seccion1\", \"descipcion\": \"Seccion 1\"}";

    Seccion abreviatura = new Seccion();
    abreviatura.setId(1L);
    abreviatura.setDescripcion("Replace Seccion1");

    BDDMockito.given(seccionService.update(ArgumentMatchers.<Seccion>any())).willReturn(abreviatura);

    mockMvc
        .perform(
            MockMvcRequestBuilders.put(ConstantesCat.SECCION_CONTROLLER_BASE_PATH + ConstantesCat.PATH_PARAMETER_ID, 1L)
                .contentType(MediaType.APPLICATION_JSON).content(replaceSeccionJson))
        .andDo(MockMvcResultHandlers.print())
        // then: Modifica la seccion y lo devuelve
        .andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.jsonPath("id").value(1))
        .andExpect(MockMvcResultMatchers.jsonPath("descripcion").value("Replace Seccion1"));

  }

  // @Test
  public void replaceSeccion_NotFound() throws Exception {
    // given: Una seccion a modificar
    String replaceSeccionJson = "{\"id\": \"1\",\"nombre\": \"Seccion1\", \"descripcion\": \"Seccion 1\"}";

    Seccion seccion = new Seccion();
    seccion.setId(1L);
    seccion.setDescripcion("Replace Seccion1");

    BDDMockito.given(seccionService.update(ArgumentMatchers.<Seccion>any())).will((InvocationOnMock invocation) -> {
      throw new SeccionNotFoundException(((Seccion) invocation.getArgument(0)).getId());
    });
    mockMvc
        .perform(
            MockMvcRequestBuilders.put(ConstantesCat.SECCION_CONTROLLER_BASE_PATH + ConstantesCat.PATH_PARAMETER_ID, 1L)
                .contentType(MediaType.APPLICATION_JSON).content(replaceSeccionJson))
        .andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isNotFound());

  }

  @Test
  public void getSeccion_WithId_ReturnsSeccion() throws Exception {
    // given: una sección
    BDDMockito.given(seccionService.findById(ArgumentMatchers.anyLong()))
        .willReturn((new Seccion(1L, "Seccion 1", "Seccion create test")));

    // when: se busca la sección por id
    mockMvc
        .perform(MockMvcRequestBuilders
            .get(ConstantesCat.SECCION_CONTROLLER_BASE_PATH + ConstantesCat.PATH_PARAMETER_ID, 1L))
        .andDo(MockMvcResultHandlers.print())
        // then: se recupera la sección
        .andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.jsonPath("id").value(1))
        .andExpect(MockMvcResultMatchers.jsonPath("nombre").value("Seccion 1"));
  }

  // @Test
  public void getSeccion_NotFound_Returns404() throws Exception {
    // given: Se lanza excepción SeccionNotFoundException
    BDDMockito.given(seccionService.findById(ArgumentMatchers.anyLong())).will((InvocationOnMock invocation) -> {
      throw new SeccionNotFoundException(invocation.getArgument(0));
    });
    // when: se busca sección por id
    mockMvc
        .perform(MockMvcRequestBuilders
            .get(ConstantesCat.SECCION_CONTROLLER_BASE_PATH + ConstantesCat.PATH_PARAMETER_ID, 1L))
        .andDo(MockMvcResultHandlers.print())
        // then: se devuelve excepción not found.
        .andExpect(MockMvcResultMatchers.status().isNotFound());
  }

  @Test
  public void delete_Success() throws Exception {
    // when: se invoca el borrado de sección por id
    mockMvc
        .perform(MockMvcRequestBuilders
            .delete(ConstantesCat.SECCION_CONTROLLER_BASE_PATH + ConstantesCat.PATH_PARAMETER_ID, 1L))
        // then: se devulve ok
        .andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isOk());

  }

}
