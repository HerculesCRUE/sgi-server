package org.crue.hercules.sgi.catservice.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.crue.hercules.sgi.catservice.exceptions.SupervisionNotFoundException;
import org.crue.hercules.sgi.catservice.model.Seccion;
import org.crue.hercules.sgi.catservice.model.Servicio;
import org.crue.hercules.sgi.catservice.model.Supervision;
import org.crue.hercules.sgi.catservice.service.SupervisionService;
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
 * SupervisionControllerTest
 */
@WebMvcTest(SupervisionController.class)
public class SupervisionControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private SupervisionService supervisionService;

  /* Retorna una lista Supervision y comprueba los datos */
  @Test
  public void getSupervision_ReturnsSupervisionList() throws Exception {

    Seccion seccion = new Seccion(1L, "Seccion 1", "Seccion create test");
    Servicio servicio = new Servicio(1L, "Servicio 1", "Serv1", "Nombre Apellidos", seccion);

    BDDMockito.given(supervisionService.findAll()).willReturn(
        Arrays.asList(new Supervision(1L, "UsuarioRef1", servicio), new Supervision(2L, "UsuarioRef2", servicio)));

    mockMvc
        .perform(MockMvcRequestBuilders.get(ConstantesCat.SUPERVISION_CONTROLLER_BASE_PATH + ConstantesCat.PATH_ALL)
            .contentType(MediaType.APPLICATION_JSON))
        .andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(2)))

        .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1))
        .andExpect(MockMvcResultMatchers.jsonPath("$[0].usuarioRef").value("UsuarioRef1"))
        .andExpect(MockMvcResultMatchers.jsonPath("$[0].servicio").value(servicio))

        .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(2))
        .andExpect(MockMvcResultMatchers.jsonPath("$[1].usuarioRef").value("UsuarioRef2"))
        .andExpect(MockMvcResultMatchers.jsonPath("$[1].servicio").value(servicio));
  }

  /* Retorna una lista vacía */
  @Test
  public void getSupervision_ReturnsEmptyList() throws Exception {
    BDDMockito.given(supervisionService.findAll()).willReturn(Collections.emptyList());

    mockMvc.perform(MockMvcRequestBuilders.get(ConstantesCat.SUPERVISION_CONTROLLER_BASE_PATH + ConstantesCat.PATH_ALL))
        .andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(0)));
  }

  /* Retorna un Supervision por id y comprueba los datos */
  @Test
  public void getSupervision_WithId_ReturnsSupervision() throws Exception {

    Seccion seccion = new Seccion(1L, "Seccion 1", "Seccion create test");
    Servicio servicio = new Servicio(1L, "Servicio 1", "Serv1", "Nombre Apellidos", seccion);

    BDDMockito.given(supervisionService.findById(ArgumentMatchers.anyLong()))
        .willReturn((new Supervision(1L, "UsuarioRef1", servicio)));

    mockMvc
        .perform(MockMvcRequestBuilders
            .get(ConstantesCat.SUPERVISION_CONTROLLER_BASE_PATH + ConstantesCat.PATH_PARAMETER_ID, 1L))
        .andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("id").value(1))
        .andExpect(MockMvcResultMatchers.jsonPath("usuarioRef").value("UsuarioRef1"))
        .andExpect(MockMvcResultMatchers.jsonPath("servicio").value(servicio));
    ;
  }

  @Test
  public void getSupervision_NotFound_Returns404() throws Exception {
    BDDMockito.given(supervisionService.findById(ArgumentMatchers.anyLong())).will((InvocationOnMock invocation) -> {
      throw new SupervisionNotFoundException(invocation.getArgument(0));
    });
    mockMvc
        .perform(MockMvcRequestBuilders
            .get(ConstantesCat.SUPERVISION_CONTROLLER_BASE_PATH + ConstantesCat.PATH_PARAMETER_ID, 1L))
        .andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isNotFound());
  }

  /* Crear Supervision */
  @Test
  public void addSupervision_ReturnsSupervision() throws Exception {

    Seccion seccion = new Seccion(1L, "Seccion 1", "Seccion create test");
    Servicio servicio = new Servicio(1L, "Servicio 1", "Serv1", "Nombre Apellidos", seccion);

    // given: Un Supervision nuevo
    String nuevoSupervisionJson = "{\"usuarioRef\": \"UsuarioRef1\", \"servicio\": {\"nombre\": \"Servicio1\", \"abreviatura\": \"Serv1\", \"contacto\": \"Contacto 1\", \"seccion\": {\"id\": \"1\",\"nombre\": \"Seccion 1\", \"descripcion\": \"Seccion servicio 1\"}}}";

    Supervision supervision = new Supervision();
    supervision.setId(1L);
    supervision.setUsuarioRef("UsuarioRef1");
    supervision.setServicio(servicio);

    BDDMockito.given(supervisionService.create(ArgumentMatchers.<Supervision>any())).willReturn(supervision);

    // when: Creamos un Supervision

    mockMvc
        .perform(MockMvcRequestBuilders.post(ConstantesCat.SUPERVISION_CONTROLLER_BASE_PATH)
            .contentType(MediaType.APPLICATION_JSON).content(nuevoSupervisionJson))
        .andDo(MockMvcResultHandlers.print())
        // then: Crea el nuevo Supervision y lo devuelve
        .andExpect(MockMvcResultMatchers.status().isCreated()).andExpect(MockMvcResultMatchers.jsonPath("id").value(1))
        .andExpect(MockMvcResultMatchers.jsonPath("usuarioRef").value("UsuarioRef1"))
        .andExpect(MockMvcResultMatchers.jsonPath("servicio").value(servicio));
  }

  @Test
  public void addSupervision_Error_Returns400() throws Exception {
    // given: Un Supervision nuevo que produce un error al crearse
    String nuevoSupervisionJson = "{\"id\": 1, \"usuario_ref\": \"UsuarioRef1\", \"servicio\": {\"nombre\": \"Servicio1\", \"abreviatura\": \"Serv1\", \"contacto\": \"Contacto 1\", \"seccion\": {\"id\": \"1\",\"nombre\": \"Seccion 1\", \"descripcion\": \"Seccion servicio 1\"}}}";

    BDDMockito.given(supervisionService.create(ArgumentMatchers.<Supervision>any()))
        .willThrow(new IllegalArgumentException());

    // when: Creamos un Supervision
    mockMvc
        .perform(MockMvcRequestBuilders.post(ConstantesCat.SUPERVISION_CONTROLLER_BASE_PATH)
            .contentType(MediaType.APPLICATION_JSON).content(nuevoSupervisionJson))
        .andDo(MockMvcResultHandlers.print())

        .andExpect(MockMvcResultMatchers.status().isBadRequest());

  }

  /* Modificar un Supervision */
  @Test
  public void replaceSupervision_ReturnsSupervision() throws Exception {
    // given: Un Supervision a modificar
    String replaceSupervisionJson = "{\"usuarioRef\": \"UsuarioRef1\", \"servicio\": {\"nombre\": \"Servicio1\", \"abreviatura\": \"Serv1\", \"contacto\": \"Contacto 1\", \"seccion\": {\"id\": \"1\",\"nombre\": \"Seccion 1\", \"descripcion\": \"Seccion servicio 1\"}}}";

    Seccion seccion = new Seccion(1L, "Seccion 1", "Seccion create test");
    Servicio servicio = new Servicio(1L, "Servicio 1", "Serv1", "Nombre Apellidos", seccion);

    Supervision supervision = new Supervision();
    supervision.setId(1L);
    supervision.setUsuarioRef("Replace UsuarioRef1");
    supervision.setServicio(servicio);

    BDDMockito.given(supervisionService.update(ArgumentMatchers.<Supervision>any())).willReturn(supervision);

    mockMvc
        .perform(MockMvcRequestBuilders
            .put(ConstantesCat.SUPERVISION_CONTROLLER_BASE_PATH + ConstantesCat.PATH_PARAMETER_ID, 1L)
            .contentType(MediaType.APPLICATION_JSON).content(replaceSupervisionJson))
        .andDo(MockMvcResultHandlers.print())
        // then: Modifica el Supervision y lo devuelve
        .andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.jsonPath("id").value(1))
        .andExpect(MockMvcResultMatchers.jsonPath("usuarioRef").value("Replace UsuarioRef1"))
        .andExpect(MockMvcResultMatchers.jsonPath("servicio").value(servicio));

  }

  @Test
  public void replaceSupervision_NotFound() throws Exception {
    // given: Un Supervision a modificar
    String replaceSupervisionJson = "{\"id\": \"1\",\"usuario_ref\": \"UsuarioRef1\", \"servicio\": {\"nombre\": \"Servicio1\", \"abreviatura\": \"Serv1\", \"contacto\": \"Contacto 1\", \"seccion\": {\"id\": \"1\",\"nombre\": \"Seccion 1\", \"descripcion\": \"Seccion servicio 1\"}}}";

    Supervision supervision = new Supervision();
    supervision.setId(1L);
    supervision.setUsuarioRef("Replace UsuarioRef1");

    BDDMockito.given(supervisionService.update(ArgumentMatchers.<Supervision>any()))
        .will((InvocationOnMock invocation) -> {
          throw new SupervisionNotFoundException(((Supervision) invocation.getArgument(0)).getId());
        });
    mockMvc
        .perform(MockMvcRequestBuilders
            .put(ConstantesCat.SUPERVISION_CONTROLLER_BASE_PATH + ConstantesCat.PATH_PARAMETER_ID, 1L)
            .contentType(MediaType.APPLICATION_JSON).content(replaceSupervisionJson))
        .andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isNotFound());

  }

  /* Eliminar un Supervision */
  @Test
  public void deleteSupervision() throws Exception {

    mockMvc

        .perform(MockMvcRequestBuilders
            .delete(ConstantesCat.SUPERVISION_CONTROLLER_BASE_PATH + ConstantesCat.PATH_PARAMETER_ID, 1L)
            .contentType(MediaType.APPLICATION_JSON))
        .andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isOk());
  }

  /* Retorna lista paginada y filtrada Supervision */
  @Test
  public void getSupervisionPagedFilter_ReturnsSupervisionList() throws Exception {

    Seccion seccion = new Seccion(1L, "Seccion 1", "Seccion create test");
    Servicio servicio = new Servicio(1L, "Servicio 1", "Serv1", "Nombre Apellidos", seccion);

    List<Supervision> supervisionList = new ArrayList<>();
    supervisionList.add(new Supervision(1L, "UsuarioRef1", servicio));
    supervisionList.add(new Supervision(2L, "UsuarioRef2", servicio));

    Page<Supervision> pagedSupervision = new PageImpl<Supervision>(supervisionList);

    BDDMockito.given(supervisionService.findAllLike(ArgumentMatchers.any(), ArgumentMatchers.any()))
        .willReturn(pagedSupervision);

    mockMvc
        .perform(MockMvcRequestBuilders.get(
            ConstantesCat.SUPERVISION_CONTROLLER_BASE_PATH + ConstantesCat.PATH_ALL + ConstantesCat.PATH_PAGEFILTERED)
            .contentType(MediaType.APPLICATION_JSON))
        .andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.totalPages").value(1))
        .andExpect(MockMvcResultMatchers.jsonPath("$.totalElements").value(2))
        .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].id").value(1))
        .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].usuarioRef").value("UsuarioRef1"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].servicio.id").value(servicio.getId()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].id").value(2))
        .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].usuarioRef").value("UsuarioRef2"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].servicio.id").value(servicio.getId()));
  }

  /* Retorna una lista paginada */
  @Test
  public void getSupervisionPageable_ReturnsSupervisionList() throws Exception {

    Seccion seccion = new Seccion(1L, "Seccion 1", "Seccion create test");
    Servicio servicio = new Servicio(1L, "Servicio 1", "Serv1", "Nombre Apellidos", seccion);

    List<Supervision> supervisionList = new ArrayList<>();
    supervisionList.add(new Supervision(1L, "UsuarioRef1", servicio));
    supervisionList.add(new Supervision(2L, "UsuarioRef2", servicio));

    Page<Supervision> pagedSupervision = new PageImpl<Supervision>(supervisionList);

    BDDMockito.given(supervisionService.findAll(ArgumentMatchers.any())).willReturn(pagedSupervision);

    mockMvc

        .perform(MockMvcRequestBuilders
            .get(ConstantesCat.SUPERVISION_CONTROLLER_BASE_PATH + ConstantesCat.PATH_ALL + ConstantesCat.PATH_PAGE)
            .contentType(MediaType.APPLICATION_JSON))
        .andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.totalPages").value(1))
        .andExpect(MockMvcResultMatchers.jsonPath("$.totalElements").value(2))
        .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].id").value(1))
        .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].usuarioRef").value("UsuarioRef1"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].servicio.id").value(servicio.getId()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].id").value(2))
        .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].usuarioRef").value("UsuarioRef2"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].servicio.id").value(servicio.getId()));
  }
}