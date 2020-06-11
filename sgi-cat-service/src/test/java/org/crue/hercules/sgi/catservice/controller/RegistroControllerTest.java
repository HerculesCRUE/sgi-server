package org.crue.hercules.sgi.catservice.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.crue.hercules.sgi.catservice.enums.EstadoRegistroEnum;
import org.crue.hercules.sgi.catservice.exceptions.RegistroNotFoundException;
import org.crue.hercules.sgi.catservice.filter.RegistroFilter;
import org.crue.hercules.sgi.catservice.model.Seccion;
import org.crue.hercules.sgi.catservice.model.Servicio;
import org.crue.hercules.sgi.catservice.model.Registro;
import org.crue.hercules.sgi.catservice.service.RegistroService;
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
 * RegistroControllerTest
 */
@WebMvcTest(RegistroController.class)
public class RegistroControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private RegistroService registroService;

  @Test
  public void getRegistros_ReturnsRegistrosList() throws Exception {
    // given: lista de dos registros
    Seccion seccion = new Seccion(1L, "Seccion 1", "Seccion create test");
    Servicio servicio = new Servicio(1L, "Servicio 1", "Serv1", "Nombre Apellidos", seccion);

    BDDMockito.given(registroService.findAll())
        .willReturn(Arrays.asList(
            new Registro(1L, "user-998", servicio, EstadoRegistroEnum.ACTIVO, Boolean.TRUE, Boolean.TRUE, "registro 1"),
            new Registro(2L, "user-568", servicio, EstadoRegistroEnum.INACTIVO, Boolean.TRUE, Boolean.FALSE,
                "registro 2")));

    // when: se realiza la búsqueda de registros
    mockMvc
        .perform(MockMvcRequestBuilders.get(ConstantesCat.REGISTRO_CONTROLLER_BASE_PATH + ConstantesCat.PATH_ALL)
            .contentType(MediaType.APPLICATION_JSON))
        .andDo(MockMvcResultHandlers.print())
        // then: se recupera página con dos registros
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(2)))
        .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1))
        .andExpect(MockMvcResultMatchers.jsonPath("$[0].usuarioRef").value("user-998"))
        .andExpect(MockMvcResultMatchers.jsonPath("$[0].estado").value(EstadoRegistroEnum.ACTIVO.getValue()))
        .andExpect(MockMvcResultMatchers.jsonPath("$[0].entregaPapel").value(Boolean.TRUE))
        .andExpect(MockMvcResultMatchers.jsonPath("$[0].aceptaCondiciones").value(Boolean.TRUE))
        .andExpect(MockMvcResultMatchers.jsonPath("$[0].observaciones").value("registro 1"))
        .andExpect(MockMvcResultMatchers.jsonPath("$[0].servicio.id").value(1))
        .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(2))
        .andExpect(MockMvcResultMatchers.jsonPath("$[1].usuarioRef").value("user-568"))
        .andExpect(MockMvcResultMatchers.jsonPath("$[1].estado").value(EstadoRegistroEnum.INACTIVO.getValue()))
        .andExpect(MockMvcResultMatchers.jsonPath("$[1].entregaPapel").value(Boolean.TRUE))
        .andExpect(MockMvcResultMatchers.jsonPath("$[1].aceptaCondiciones").value(Boolean.FALSE))
        .andExpect(MockMvcResultMatchers.jsonPath("$[1].servicio.id").value(1))
        .andExpect(MockMvcResultMatchers.jsonPath("$[1].observaciones").value("registro 2"));

  }

  @Test
  public void getRegistros_ReturnsEmptyList() throws Exception {
    // given: Lista de registros vacía
    BDDMockito.given(registroService.findAll()).willReturn(Collections.emptyList());

    // when:: se realiza la búsqueda de registros
    mockMvc.perform(MockMvcRequestBuilders.get(ConstantesCat.REGISTRO_CONTROLLER_BASE_PATH + ConstantesCat.PATH_ALL))
        .andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isOk());
  }

  @Test
  public void getRegistrosPageable_ReturnsRegistrosList() throws Exception {
    // given: Lista de dos registros

    Seccion seccion = new Seccion(1L, "Seccion 1", "Seccion create test");
    Servicio servicio = new Servicio(1L, "Servicio 1", "Serv1", "Nombre Apellidos", seccion);

    List<Registro> registrosList = new ArrayList<>();
    registrosList.add(
        new Registro(1L, "user-998", servicio, EstadoRegistroEnum.ACTIVO, Boolean.TRUE, Boolean.TRUE, "registro 1"));
    registrosList.add(
        new Registro(2L, "user-568", servicio, EstadoRegistroEnum.INACTIVO, Boolean.TRUE, Boolean.FALSE, "registro 2"));

    Page<Registro> pagedRegistro = new PageImpl<Registro>(registrosList);

    BDDMockito.given(registroService.findAll(ArgumentMatchers.<Pageable>any())).willReturn(pagedRegistro);

    // when: se realiza la búsqueda de registros
    mockMvc
        .perform(MockMvcRequestBuilders
            .get(ConstantesCat.REGISTRO_CONTROLLER_BASE_PATH + ConstantesCat.PATH_ALL + ConstantesCat.PATH_PAGE)

            .contentType(MediaType.APPLICATION_JSON).param("usuarioRef", "user").param("page", "0").param("size", "10")
            .param("sort", "id,desc"))
        .andDo(MockMvcResultHandlers.print())
        // then: se devuelve la página con dos servicios
        .andExpect(MockMvcResultMatchers.status().isOk()).andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.jsonPath("$").hasJsonPath())
        .andExpect(MockMvcResultMatchers.jsonPath("$.totalElements").value(2))
        .andExpect(MockMvcResultMatchers.jsonPath("$.totalPages").value(1))
        .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].id").value(1))
        .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].usuarioRef").value("user-998"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].estado").value(EstadoRegistroEnum.ACTIVO.getValue()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].entregaPapel").value(Boolean.TRUE))
        .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].aceptaCondiciones").value(Boolean.TRUE))
        .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].observaciones").value("registro 1"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].servicio.id").value(1))
        .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].id").value(2))
        .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].usuarioRef").value("user-568"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].estado").value(EstadoRegistroEnum.INACTIVO.getValue()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].entregaPapel").value(Boolean.TRUE))
        .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].aceptaCondiciones").value(Boolean.FALSE))
        .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].servicio.id").value(1))
        .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].observaciones").value("registro 2"));
    ;
  }

  @Test
  public void getRegistrosPageable_ReturnsEmptyList() throws Exception {

    // given: lista vacía de registros
    List<Registro> registrosList = new ArrayList<>();

    Page<Registro> pagedRegistro = new PageImpl<Registro>(registrosList);

    BDDMockito.given(registroService.findAll(ArgumentMatchers.<Pageable>any())).willReturn(pagedRegistro);

    // when: se realiza la búsqueda de registros
    mockMvc
        .perform(MockMvcRequestBuilders
            .get(ConstantesCat.REGISTRO_CONTROLLER_BASE_PATH + ConstantesCat.PATH_ALL + ConstantesCat.PATH_PAGE)

            .contentType(MediaType.APPLICATION_JSON).param("usuarioRef", "user").param("page", "0").param("size", "10")
            .param("sort", "id,desc"))
        .andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isOk())
        .andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.jsonPath("$").hasJsonPath())
        // then: se devuelve página vacía
        .andExpect(MockMvcResultMatchers.jsonPath("$.totalElements").value(0))
        .andExpect(MockMvcResultMatchers.jsonPath("$.totalPages").value(1));
  }

  @Test
  public void getRegistrosPagedFilter_ReturnsRegistrosList() throws Exception {
    // given: lista de dos registros
    Seccion seccion = new Seccion(1L, "Seccion 1", "Seccion create test");
    Servicio servicio = new Servicio(1L, "Servicio 1", "Serv1", "Nombre Apellidos", seccion);

    List<Registro> registrosList = new ArrayList<>();
    registrosList.add(
        new Registro(1L, "user-998", servicio, EstadoRegistroEnum.ACTIVO, Boolean.TRUE, Boolean.TRUE, "registro 1"));
    registrosList.add(
        new Registro(2L, "user-568", servicio, EstadoRegistroEnum.INACTIVO, Boolean.TRUE, Boolean.FALSE, "registro 2"));
    Page<Registro> pagedRegistro = new PageImpl<Registro>(registrosList);

    BDDMockito
        .given(registroService.findAllLike(ArgumentMatchers.<RegistroFilter>any(), ArgumentMatchers.<Pageable>any()))
        .willReturn(pagedRegistro);

    // when: se realiza la búsqueda de servicios
    mockMvc
        .perform(MockMvcRequestBuilders
            .get(ConstantesCat.REGISTRO_CONTROLLER_BASE_PATH + ConstantesCat.PATH_ALL + ConstantesCat.PATH_PAGEFILTERED)

            .contentType(MediaType.APPLICATION_JSON).param("usuarioRef", "user").param("page", "0").param("size", "10")
            .param("sort", "id,desc"))
        .andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isOk())
        .andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.jsonPath("$").hasJsonPath())
        // then: se recupera la página con dos registros
        .andExpect(MockMvcResultMatchers.jsonPath("$.totalElements").value(2))
        .andExpect(MockMvcResultMatchers.jsonPath("$.totalPages").value(1))
        .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].id").value(1))
        .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].usuarioRef").value("user-998"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].estado").value(EstadoRegistroEnum.ACTIVO.getValue()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].entregaPapel").value(Boolean.TRUE))
        .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].aceptaCondiciones").value(Boolean.TRUE))
        .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].observaciones").value("registro 1"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].servicio.id").value(1))
        .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].id").value(2))
        .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].usuarioRef").value("user-568"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].estado").value(EstadoRegistroEnum.INACTIVO.getValue()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].entregaPapel").value(Boolean.TRUE))
        .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].aceptaCondiciones").value(Boolean.FALSE))
        .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].servicio.id").value(1))
        .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].observaciones").value("registro 2"));
  }

  @Test
  public void getRegistrosPagedFilter_ReturnsEmptyList() throws Exception {
    // given: lista vacía de registros
    List<Registro> registrosList = new ArrayList<>();

    Page<Registro> pagedRegistro = new PageImpl<Registro>(registrosList);

    BDDMockito
        .given(registroService.findAllLike(ArgumentMatchers.<RegistroFilter>any(), ArgumentMatchers.<Pageable>any()))
        .willReturn(pagedRegistro);

    // when: se realiza la búsqueda de rgistros
    mockMvc
        .perform(MockMvcRequestBuilders
            .get(ConstantesCat.REGISTRO_CONTROLLER_BASE_PATH + ConstantesCat.PATH_ALL + ConstantesCat.PATH_PAGEFILTERED)

            .contentType(MediaType.APPLICATION_JSON).param("usuarioRef", "user").param("page", "0").param("size", "10")
            .param("sort", "id,desc"))
        .andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isOk())
        .andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.jsonPath("$").hasJsonPath())
        // then: se recupera la página vacía de registros
        .andExpect(MockMvcResultMatchers.jsonPath("$.totalElements").value(0))
        .andExpect(MockMvcResultMatchers.jsonPath("$.totalPages").value(1));
  }

  @Test
  public void newRegistro_ReturnsRegistro() throws Exception {
    // given: Un registro nuevo
    String nuevoRegistroJson = "{\"usuarioRef\": \"user-1\", \"servicio\": {\"id\": \"1\", \"nombre\": \"Servicio1\", \"abreviatura\": \"Serv1\", \"contacto\": \"Contacto 1\", \"seccion\": {\"id\": \"1\",\"nombre\": \"Seccion 1\", \"descripcion\": \"Seccion servicio 1\"}}, \"contacto\": \"Contacto 1\", \"seccion\": {\"id\": \"1\",\"nombre\": \"Seccion 1\", \"descripcion\": \"Seccion servicio 1\"}, \"estado\": \"activo\",\"entregaPapel\": true,\"aceptaCondiciones\": true,\"observaciones\": \"nuevo registro\"}";

    Seccion seccion = new Seccion(1L, "Seccion 1", "Seccion servicio 1");
    Servicio servicio = new Servicio(1L, "Servicio 1", "Serv1", "Nombre Apellidos", seccion);

    Registro registro = new Registro(1L, "user-1", servicio, EstadoRegistroEnum.ACTIVO, Boolean.TRUE, Boolean.TRUE,
        "registro 1");

    BDDMockito.given(registroService.create(ArgumentMatchers.<Registro>any())).willReturn(registro);

    // when: Creamos un registro
    mockMvc
        .perform(MockMvcRequestBuilders.post(ConstantesCat.REGISTRO_CONTROLLER_BASE_PATH)
            .contentType(MediaType.APPLICATION_JSON).content(nuevoRegistroJson))
        .andDo(MockMvcResultHandlers.print())
        // then: Crea el nuevo registro y lo devuelve
        .andExpect(MockMvcResultMatchers.status().isCreated()).andExpect(MockMvcResultMatchers.jsonPath("id").value(1))
        .andExpect(MockMvcResultMatchers.jsonPath("usuarioRef").value("user-1"));
  }

  // @Test
  public void newRegistro_Error_Returns400() throws Exception {
    // given: Un registro nuevo que produce un error al crearse
    String nuevoRegistroJson = "{\"usuarioRef\": \"user-1\", \"servicio\": {\"id\": \"1\", \"nombre\": \"Servicio1\", \"abreviatura\": \"Serv1\", \"contacto\": \"Contacto 1\", \"seccion\": {\"id\": \"1\",\"nombre\": \"Seccion 1\", \"descripcion\": \"Seccion servicio 1\"}}, \"contacto\": \"Contacto 1\", \"seccion\": {\"id\": \"1\",\"nombre\": \"Seccion 1\", \"descripcion\": \"Seccion servicio 1\"}, \"estado\": \"activo\",\"entregaPapel\": true,\"aceptaCondiciones\": true,\"observaciones\": \"nuevo registro\"}";

    BDDMockito.given(registroService.create(ArgumentMatchers.<Registro>any()))
        .willThrow(new IllegalArgumentException());

    // when: Creamos un registro
    mockMvc
        .perform(MockMvcRequestBuilders.post(ConstantesCat.REGISTRO_CONTROLLER_BASE_PATH)
            .contentType(MediaType.APPLICATION_JSON).content(nuevoRegistroJson))
        .andDo(MockMvcResultHandlers.print())
        // then: Devueve un error 400
        .andExpect(MockMvcResultMatchers.status().isBadRequest());

  }

  @Test
  public void replaceRegistro_ReturnsRegistro() throws Exception {
    // given: Un registro a modificar2
    String replaceRegistroJson = "{\"id\": 1, \"usuarioRef\": \"user-1\", \"servicio\": {\"id\": \"1\", \"nombre\": \"Servicio1\", \"abreviatura\": \"Serv1\", \"contacto\": \"Contacto 1\", \"seccion\": {\"id\": \"1\",\"nombre\": \"Seccion 1\", \"descripcion\": \"Seccion servicio 1\"}}, \"contacto\": \"Contacto 1\", \"seccion\": {\"id\": \"1\",\"nombre\": \"Seccion 1\", \"descripcion\": \"Seccion servicio 1\"}, \"estado\": \"activo\",\"entregaPapel\": true,\"aceptaCondiciones\": true,\"observaciones\": \"nuevo registro\"}";

    Seccion seccion = new Seccion(1L, "Seccion 1", "Seccion servicio 1");
    Servicio servicio = new Servicio(1L, "Servicio 1", "Serv1", "Nombre Apellidos", seccion);

    Registro registro = new Registro(1L, "user-988", servicio, EstadoRegistroEnum.ACTIVO, Boolean.TRUE, Boolean.TRUE,
        "registro 1");

    BDDMockito.given(registroService.update(ArgumentMatchers.<Registro>any())).willReturn(registro);

    mockMvc
        .perform(MockMvcRequestBuilders
            .put(ConstantesCat.REGISTRO_CONTROLLER_BASE_PATH + ConstantesCat.PATH_PARAMETER_ID, 1L)
            .contentType(MediaType.APPLICATION_JSON).content(replaceRegistroJson))
        .andDo(MockMvcResultHandlers.print())
        // then: Modifica el registro y lo devuelve
        .andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.jsonPath("id").value(1))
        .andExpect(MockMvcResultMatchers.jsonPath("usuarioRef").value("user-988"));

  }

  // @Test
  public void replaceRegistro_NotFound() throws Exception {
    // given: Un registro a modificar
    String replaceRegistroJson = "{\"usuarioRef\": \"user-1\", \"servicio\": {\"id\": \"1\", \"nombre\": \"Servicio1\", \"abreviatura\": \"Serv1\", \"contacto\": \"Contacto 1\", \"seccion\": {\"id\": \"1\",\"nombre\": \"Seccion 1\", \"descripcion\": \"Seccion servicio 1\"}}, \"contacto\": \"Contacto 1\", \"seccion\": {\"id\": \"1\",\"nombre\": \"Seccion 1\", \"descripcion\": \"Seccion servicio 1\"}, \"estado\": \"activo\",\"entregaPapel\": true, ,\"aceptaCondiciones\": true,\"observaciones\": \"nuevo registro\"}";

    BDDMockito.given(registroService.update(ArgumentMatchers.<Registro>any())).will((InvocationOnMock invocation) -> {
      throw new RegistroNotFoundException(((Registro) invocation.getArgument(0)).getId());
    });
    mockMvc
        .perform(MockMvcRequestBuilders
            .put(ConstantesCat.REGISTRO_CONTROLLER_BASE_PATH + ConstantesCat.PATH_PARAMETER_ID, 1L)
            .contentType(MediaType.APPLICATION_JSON).content(replaceRegistroJson))
        .andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isNotFound());

  }

  @Test
  public void getRegistro_WithId_ReturnsRegistro() throws Exception {

    // given: Un registro

    Seccion seccion = new Seccion(1L, "Seccion 1", "Seccion servicio 1");
    Servicio servicio = new Servicio(1L, "Servicio 1", "Serv1", "Nombre Apellidos", seccion);

    Registro registro = new Registro(1L, "user-988", servicio, EstadoRegistroEnum.ACTIVO, Boolean.TRUE, Boolean.TRUE,
        "registro 1");
    BDDMockito.given(registroService.findById(ArgumentMatchers.anyLong())).willReturn(registro);

    mockMvc
        .perform(MockMvcRequestBuilders
            .get(ConstantesCat.REGISTRO_CONTROLLER_BASE_PATH + ConstantesCat.PATH_PARAMETER_ID, 1L))
        .andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("id").value(1))
        .andExpect(MockMvcResultMatchers.jsonPath("$.usuarioRef").value("user-988"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.estado").value(EstadoRegistroEnum.ACTIVO.getValue()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.entregaPapel").value(Boolean.TRUE))
        .andExpect(MockMvcResultMatchers.jsonPath("$.aceptaCondiciones").value(Boolean.TRUE))
        .andExpect(MockMvcResultMatchers.jsonPath("$.observaciones").value("registro 1"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.servicio.id").value(1));
  }

  // @Test
  public void getRegistro_NotFound_Returns404() throws Exception {
    BDDMockito.given(registroService.findById(ArgumentMatchers.anyLong())).will((InvocationOnMock invocation) -> {
      throw new RegistroNotFoundException(invocation.getArgument(0));
    });
    mockMvc
        .perform(MockMvcRequestBuilders
            .get(ConstantesCat.REGISTRO_CONTROLLER_BASE_PATH + ConstantesCat.PATH_PARAMETER_ID, 1L))
        .andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isNotFound());
  }

  @Test
  public void delete_Success() throws Exception {
    mockMvc
        .perform(MockMvcRequestBuilders
            .delete(ConstantesCat.REGISTRO_CONTROLLER_BASE_PATH + ConstantesCat.PATH_PARAMETER_ID, 1L))
        .andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isOk());

  }

}
