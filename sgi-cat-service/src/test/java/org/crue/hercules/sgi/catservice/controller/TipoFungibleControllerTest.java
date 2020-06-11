package org.crue.hercules.sgi.catservice.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.crue.hercules.sgi.catservice.exceptions.TipoFungibleNotFoundException;
import org.crue.hercules.sgi.catservice.model.Seccion;
import org.crue.hercules.sgi.catservice.model.Servicio;
import org.crue.hercules.sgi.catservice.model.TipoFungible;
import org.crue.hercules.sgi.catservice.service.TipoFungibleService;
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
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

/**
 * TipoFungibleControllerTest
 */
@WebMvcTest(TipoFungibleController.class)
public class TipoFungibleControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private TipoFungibleService tipoFungibleService;

  /* Retorna una lista TipoFungible y comprueba los datos */
  @Test
  public void getTipoFungible_ReturnsTipoFungibleList() throws Exception {

    Seccion seccion = new Seccion(1L, "Seccion 1", "Seccion create test");
    Servicio servicio = new Servicio(1L, "Servicio 1", "Serv1", "Nombre Apellidos", seccion);

    BDDMockito.given(tipoFungibleService.findAll()).willReturn(Arrays
        .asList(new TipoFungible(1L, "TipoFungible1", servicio), new TipoFungible(2L, "TipoFungible2", servicio)));

    mockMvc
        .perform(MockMvcRequestBuilders.get(ConstantesCat.TIPO_FUNGIBLE_CONTROLLER_BASE_PATH + ConstantesCat.PATH_ALL)
            .contentType(MediaType.APPLICATION_JSON))
        .andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(2)))

        .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1))
        .andExpect(MockMvcResultMatchers.jsonPath("$[0].nombre").value("TipoFungible1"))
        .andExpect(MockMvcResultMatchers.jsonPath("$[0].servicio").value(servicio))

        .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(2))
        .andExpect(MockMvcResultMatchers.jsonPath("$[1].nombre").value("TipoFungible2"))
        .andExpect(MockMvcResultMatchers.jsonPath("$[1].servicio").value(servicio));
  }

  /* Retorna una lista vacía */
  @Test
  public void getTipoFungible_ReturnsEmptyList() throws Exception {
    BDDMockito.given(tipoFungibleService.findAll()).willReturn(Collections.emptyList());

    mockMvc
        .perform(MockMvcRequestBuilders.get(ConstantesCat.TIPO_FUNGIBLE_CONTROLLER_BASE_PATH + ConstantesCat.PATH_ALL))
        .andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(0)));
  }

  /* Retorna un TipoFungible por id y comprueba los datos */
  @Test
  public void getTipoFungible_WithId_ReturnsTipoFungible() throws Exception {

    Seccion seccion = new Seccion(1L, "Seccion 1", "Seccion create test");
    Servicio servicio = new Servicio(1L, "Servicio 1", "Serv1", "Nombre Apellidos", seccion);

    BDDMockito.given(tipoFungibleService.findById(ArgumentMatchers.anyLong()))
        .willReturn((new TipoFungible(1L, "TipoFungible1", servicio)));

    mockMvc
        .perform(MockMvcRequestBuilders
            .get(ConstantesCat.TIPO_FUNGIBLE_CONTROLLER_BASE_PATH + ConstantesCat.PATH_PARAMETER_ID, 1L))
        .andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("id").value(1))
        .andExpect(MockMvcResultMatchers.jsonPath("nombre").value("TipoFungible1"))
        .andExpect(MockMvcResultMatchers.jsonPath("servicio").value(servicio));
    ;
  }

  @Test
  public void getTipoFungible_NotFound_Returns404() throws Exception {
    BDDMockito.given(tipoFungibleService.findById(ArgumentMatchers.anyLong())).will((InvocationOnMock invocation) -> {
      throw new TipoFungibleNotFoundException(invocation.getArgument(0));
    });
    mockMvc
        .perform(MockMvcRequestBuilders
            .get(ConstantesCat.TIPO_FUNGIBLE_CONTROLLER_BASE_PATH + ConstantesCat.PATH_PARAMETER_ID, 1L))
        .andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isNotFound());
  }

  /* Crear TipoFungible */
  @Test
  public void addTipoFungible_ReturnsTipoFungible() throws Exception {

    Seccion seccion = new Seccion(1L, "Seccion 1", "Seccion create test");
    Servicio servicio = new Servicio(1L, "Servicio 1", "Serv1", "Nombre Apellidos", seccion);

    // given: Un TipoFungible nuevo
    String nuevoTipoFungibleJson = "{\"nombre\": \"TipoFungible1\", \"servicio\": {\"nombre\": \"Servicio1\", \"abreviatura\": \"Serv1\", \"contacto\": \"Contacto 1\", \"seccion\": {\"id\": \"1\",\"nombre\": \"Seccion 1\", \"descripcion\": \"Seccion servicio 1\"}}}";

    TipoFungible tipoFungible = new TipoFungible();
    tipoFungible.setId(1L);
    tipoFungible.setNombre("TipoFungible1");
    tipoFungible.setServicio(servicio);

    BDDMockito.given(tipoFungibleService.create(ArgumentMatchers.<TipoFungible>any())).willReturn(tipoFungible);

    // when: Creamos un TipoFungible

    mockMvc
        .perform(MockMvcRequestBuilders.post(ConstantesCat.TIPO_FUNGIBLE_CONTROLLER_BASE_PATH)
            .contentType(MediaType.APPLICATION_JSON).content(nuevoTipoFungibleJson))
        .andDo(MockMvcResultHandlers.print())
        // then: Crea el nuevo TipoFungible y lo devuelve
        .andExpect(MockMvcResultMatchers.status().isCreated()).andExpect(MockMvcResultMatchers.jsonPath("id").value(1))
        .andExpect(MockMvcResultMatchers.jsonPath("nombre").value("TipoFungible1"))
        .andExpect(MockMvcResultMatchers.jsonPath("servicio").value(servicio));
  }

  @Test
  public void addTipoFungible_Error_Returns400() throws Exception {
    // given: Un TipoFungible nuevo que produce un error al crearse
    String nuevoTipoFungibleJson = "{\"id\": 1, \"nombre\": \"TipoFungible1\", \"servicio\": {\"nombre\": \"Servicio1\", \"abreviatura\": \"Serv1\", \"contacto\": \"Contacto 1\", \"seccion\": {\"id\": \"1\",\"nombre\": \"Seccion 1\", \"descripcion\": \"Seccion servicio 1\"}}}";

    BDDMockito.given(tipoFungibleService.create(ArgumentMatchers.<TipoFungible>any()))
        .willThrow(new IllegalArgumentException());

    // when: Creamos un TipoFungible
    mockMvc
        .perform(MockMvcRequestBuilders.post(ConstantesCat.TIPO_FUNGIBLE_CONTROLLER_BASE_PATH)
            .contentType(MediaType.APPLICATION_JSON).content(nuevoTipoFungibleJson))
        .andDo(MockMvcResultHandlers.print())

        .andExpect(MockMvcResultMatchers.status().isBadRequest());

  }

  /* Modificar un TipoFungible */
  @Test
  public void replaceTipoFungible_ReturnsTipoFungible() throws Exception {
    // given: Un TipoFungible a modificar
    String replaceTipoFungibleJson = "{\"nombre\": \"TipoFungible1\", \"servicio\": {\"nombre\": \"Servicio1\", \"abreviatura\": \"Serv1\", \"contacto\": \"Contacto 1\", \"seccion\": {\"id\": \"1\",\"nombre\": \"Seccion 1\", \"descripcion\": \"Seccion servicio 1\"}}}";

    Seccion seccion = new Seccion(1L, "Seccion 1", "Seccion create test");
    Servicio servicio = new Servicio(1L, "Servicio 1", "Serv1", "Nombre Apellidos", seccion);

    TipoFungible tipoFungible = new TipoFungible();
    tipoFungible.setId(1L);
    tipoFungible.setNombre("Replace TipoFungible1");
    tipoFungible.setServicio(servicio);

    BDDMockito.given(tipoFungibleService.update(ArgumentMatchers.<TipoFungible>any())).willReturn(tipoFungible);

    mockMvc
        .perform(MockMvcRequestBuilders
            .put(ConstantesCat.TIPO_FUNGIBLE_CONTROLLER_BASE_PATH + ConstantesCat.PATH_PARAMETER_ID, 1L)
            .contentType(MediaType.APPLICATION_JSON).content(replaceTipoFungibleJson))
        .andDo(MockMvcResultHandlers.print())
        // then: Modifica el TipoFungible y lo devuelve
        .andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.jsonPath("id").value(1))
        .andExpect(MockMvcResultMatchers.jsonPath("nombre").value("Replace TipoFungible1"))
        .andExpect(MockMvcResultMatchers.jsonPath("servicio").value(servicio));

  }

  @Test
  public void replaceTipoFungible_NotFound() throws Exception {
    // given: Un TipoFungible a modificar
    String replaceTipoFungibleJson = "{\"id\": \"1\",\"nombre\": \"TipoFungible1\", \"servicio\": {\"nombre\": \"Servicio1\", \"abreviatura\": \"Serv1\", \"contacto\": \"Contacto 1\", \"seccion\": {\"id\": \"1\",\"nombre\": \"Seccion 1\", \"descripcion\": \"Seccion servicio 1\"}}}";

    TipoFungible tipoFungible = new TipoFungible();
    tipoFungible.setId(1L);
    tipoFungible.setNombre("Replace TipoFungible1");

    BDDMockito.given(tipoFungibleService.update(ArgumentMatchers.<TipoFungible>any()))
        .will((InvocationOnMock invocation) -> {
          throw new TipoFungibleNotFoundException(((TipoFungible) invocation.getArgument(0)).getId());
        });
    mockMvc
        .perform(MockMvcRequestBuilders
            .put(ConstantesCat.TIPO_FUNGIBLE_CONTROLLER_BASE_PATH + ConstantesCat.PATH_PARAMETER_ID, 1L)
            .contentType(MediaType.APPLICATION_JSON).content(replaceTipoFungibleJson))
        .andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isNotFound());

  }

  /* Eliminar un TipoFungible */
  @Test
  public void deleteTipoFungible() throws Exception {

    mockMvc
        .perform(MockMvcRequestBuilders
            .delete(ConstantesCat.TIPO_FUNGIBLE_CONTROLLER_BASE_PATH + ConstantesCat.PATH_PARAMETER_ID, 1L)
            .contentType(MediaType.APPLICATION_JSON))
        .andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isOk());
  }

  /* Retorna lista paginada y filtrada TipoFungible */
  @Test
  public void getTipoFungiblePagedFilter_ReturnsTipoFungibleList() throws Exception {

    Seccion seccion = new Seccion(1L, "Seccion 1", "Seccion create test");
    Servicio servicio = new Servicio(1L, "Servicio 1", "Serv1", "Nombre Apellidos", seccion);

    List<TipoFungible> tipoFungibleList = new ArrayList<>();
    tipoFungibleList.add(new TipoFungible(1L, "TipoFungible1", servicio));
    tipoFungibleList.add(new TipoFungible(2L, "TipoFungible2", servicio));

    Page<TipoFungible> pagedTipoFungible = new PageImpl<TipoFungible>(tipoFungibleList);

    BDDMockito.given(tipoFungibleService.findAllLike(ArgumentMatchers.any(), ArgumentMatchers.any()))
        .willReturn(pagedTipoFungible);

    mockMvc
        .perform(MockMvcRequestBuilders.get(
            ConstantesCat.TIPO_FUNGIBLE_CONTROLLER_BASE_PATH + ConstantesCat.PATH_ALL + ConstantesCat.PATH_PAGEFILTERED)
            .contentType(MediaType.APPLICATION_JSON))
        .andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.totalPages").value(1))
        .andExpect(MockMvcResultMatchers.jsonPath("$.totalElements").value(2))
        .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].id").value(1))
        .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].nombre").value("TipoFungible1"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].servicio.id").value(servicio.getId()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].id").value(2))
        .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].nombre").value("TipoFungible2"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].servicio.id").value(servicio.getId()));
  }

  /* Retorna una lista paginada */
  @Test
  public void getTipoFungiblePageable_ReturnsTipoFungibleList() throws Exception {

    Seccion seccion = new Seccion(1L, "Seccion 1", "Seccion create test");
    Servicio servicio = new Servicio(1L, "Servicio 1", "Serv1", "Nombre Apellidos", seccion);

    List<TipoFungible> tipoFungibleList = new ArrayList<>();
    tipoFungibleList.add(new TipoFungible(1L, "TipoFungible1", servicio));
    tipoFungibleList.add(new TipoFungible(2L, "TipoFungible2", servicio));

    Page<TipoFungible> pagedTipoFungible = new PageImpl<TipoFungible>(tipoFungibleList);

    BDDMockito.given(tipoFungibleService.findAll(ArgumentMatchers.any())).willReturn(pagedTipoFungible);

    mockMvc

        .perform(MockMvcRequestBuilders
            .get(ConstantesCat.TIPO_FUNGIBLE_CONTROLLER_BASE_PATH + ConstantesCat.PATH_ALL + ConstantesCat.PATH_PAGE)
            .contentType(MediaType.APPLICATION_JSON))
        .andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.totalPages").value(1))
        .andExpect(MockMvcResultMatchers.jsonPath("$.totalElements").value(2))
        .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].id").value(1))
        .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].nombre").value("TipoFungible1"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].servicio.id").value(servicio.getId()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].id").value(2))
        .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].nombre").value("TipoFungible2"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].servicio.id").value(servicio.getId()));
  }

}