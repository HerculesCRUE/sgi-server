package org.crue.hercules.sgi.catservice.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.crue.hercules.sgi.catservice.exceptions.ServicioNotFoundException;
import org.crue.hercules.sgi.catservice.filter.ServicioFilter;
import org.crue.hercules.sgi.catservice.model.Seccion;
import org.crue.hercules.sgi.catservice.model.Servicio;
import org.crue.hercules.sgi.catservice.service.ServicioService;
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
 * ServicioControllerTest
 */
@WebMvcTest(ServicioController.class)
public class ServicioControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private ServicioService seccionService;

  @Test
  public void getServicios_ReturnsServiciosList() throws Exception {
    // given: lista de dos servicios
    Seccion seccion = new Seccion(1L, "Seccion 1", "Seccion create test");

    BDDMockito.given(seccionService.findAll())
        .willReturn(Arrays.asList(new Servicio(1L, "Servicio 1", "Serv1", "Nombre Apellidos", seccion),
            new Servicio(2L, "Servicio 2", "Serv2", "Nombre Apellidos", seccion)));

    // when: se realiza la búsqueda de servicios
    mockMvc
        .perform(MockMvcRequestBuilders.get(ConstantesCat.SERVICIO_CONTROLLER_BASE_PATH + ConstantesCat.PATH_ALL)
            .contentType(MediaType.APPLICATION_JSON))
        .andDo(MockMvcResultHandlers.print())
        // then: se recupera página con dos servicios
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(2)))

        .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1))
        .andExpect(MockMvcResultMatchers.jsonPath("$[0].nombre").value("Servicio 1"))
        .andExpect(MockMvcResultMatchers.jsonPath("$[0].abreviatura").value("Serv1"))
        .andExpect(MockMvcResultMatchers.jsonPath("$[0].contacto").value("Nombre Apellidos"))
        .andExpect(MockMvcResultMatchers.jsonPath("$[0].seccion.id").value(1))
        .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(2))
        .andExpect(MockMvcResultMatchers.jsonPath("$[1].nombre").value("Servicio 2"))
        .andExpect(MockMvcResultMatchers.jsonPath("$[1].abreviatura").value("Serv2"))
        .andExpect(MockMvcResultMatchers.jsonPath("$[1].contacto").value("Nombre Apellidos"))
        .andExpect(MockMvcResultMatchers.jsonPath("$[1].seccion.id").value(1));

  }

  @Test
  public void getServicios_ReturnsEmptyList() throws Exception {
    // given: Lista de servicios vacía
    BDDMockito.given(seccionService.findAll()).willReturn(Collections.emptyList());

    // when:: se realiza la búsqueda de secciones
    mockMvc.perform(MockMvcRequestBuilders.get(ConstantesCat.SERVICIO_CONTROLLER_BASE_PATH + ConstantesCat.PATH_ALL))
        .andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isOk());
  }

  @Test
  public void getServiciosPageable_ReturnsServiciosList() throws Exception {
    // given: Lsita de dos servicios
    Seccion seccion = new Seccion(1L, "Seccion 1", "Seccion create test");

    List<Servicio> seccionList = new ArrayList<>();
    seccionList.add(new Servicio(1L, "Servicio 1", "Serv1", "Nombre Apellidos", seccion));
    seccionList.add(new Servicio(2L, "Servicio 2", "Serv2", "Nombre Apellidos", seccion));

    Page<Servicio> pagedServicio = new PageImpl<Servicio>(seccionList);

    BDDMockito.given(seccionService.findAll(ArgumentMatchers.<Pageable>any())).willReturn(pagedServicio);

    // when: se realiza la búsqueda de servicios
    mockMvc
        .perform(MockMvcRequestBuilders
            .get(ConstantesCat.SERVICIO_CONTROLLER_BASE_PATH + ConstantesCat.PATH_ALL + ConstantesCat.PATH_PAGE)

            .contentType(MediaType.APPLICATION_JSON).param("abreviatura", "Servicio").param("page", "0")
            .param("size", "10").param("sort", "id,desc"))
        .andDo(MockMvcResultHandlers.print())
        // then: se devuelve la página con dos servicios
        .andExpect(MockMvcResultMatchers.status().isOk()).andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.jsonPath("$").hasJsonPath())
        .andExpect(MockMvcResultMatchers.jsonPath("$.totalElements").value(2))
        .andExpect(MockMvcResultMatchers.jsonPath("$.totalPages").value(1))
        .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].id").value(1))
        .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].nombre").value("Servicio 1"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].abreviatura").value("Serv1"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].contacto").value("Nombre Apellidos"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].seccion.id").value(1))
        .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].id").value(2))
        .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].nombre").value("Servicio 2"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].abreviatura").value("Serv2"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].contacto").value("Nombre Apellidos"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].seccion.id").value(1));
    ;
  }

  @Test
  public void getServiciosPageable_ReturnsEmptyList() throws Exception {

    // given: lista vacía de servicios
    List<Servicio> seccionList = new ArrayList<>();

    Page<Servicio> pagedServicio = new PageImpl<Servicio>(seccionList);

    BDDMockito.given(seccionService.findAll(ArgumentMatchers.<Pageable>any())).willReturn(pagedServicio);

    // when: se realiza la búsqueda de servicios
    mockMvc
        .perform(MockMvcRequestBuilders
            .get(ConstantesCat.SERVICIO_CONTROLLER_BASE_PATH + ConstantesCat.PATH_ALL + ConstantesCat.PATH_PAGE)

            .contentType(MediaType.APPLICATION_JSON).param("abreviatura", "Servicio").param("page", "0")
            .param("size", "10").param("sort", "id,desc"))
        .andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isOk())
        .andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.jsonPath("$").hasJsonPath())
        // then: se devuelve página vacía
        .andExpect(MockMvcResultMatchers.jsonPath("$.totalElements").value(0))
        .andExpect(MockMvcResultMatchers.jsonPath("$.totalPages").value(1));
  }

  @Test
  public void getServiciosPagedFilter_ReturnsServiciosList() throws Exception {
    // given: lista de dos servicios
    Seccion seccion = new Seccion(1L, "Seccion 1", "Seccion create test");

    List<Servicio> servicioList = new ArrayList<>();
    servicioList.add(new Servicio(1L, "Servicio 1", "Serv1", "Nombre Apellidos", seccion));
    servicioList.add(new Servicio(2L, "Servicio 2", "Serv2", "Nombre Apellidos", seccion));
    Page<Servicio> pagedServicio = new PageImpl<Servicio>(servicioList);

    BDDMockito
        .given(seccionService.findAllLike(ArgumentMatchers.<ServicioFilter>any(), ArgumentMatchers.<Pageable>any()))
        .willReturn(pagedServicio);

    // when: se realiza la búsqueda de servicios
    mockMvc
        .perform(MockMvcRequestBuilders
            .get(ConstantesCat.SERVICIO_CONTROLLER_BASE_PATH + ConstantesCat.PATH_ALL + ConstantesCat.PATH_PAGEFILTERED)

            .contentType(MediaType.APPLICATION_JSON).param("abreviatura", "Servicio").param("page", "0")
            .param("size", "10").param("sort", "id,desc"))
        .andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isOk())
        .andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.jsonPath("$").hasJsonPath())
        // then: se recupera la página con dos servicios
        .andExpect(MockMvcResultMatchers.jsonPath("$.totalElements").value(2))
        .andExpect(MockMvcResultMatchers.jsonPath("$.totalPages").value(1))
        .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].id").value(1))
        .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].nombre").value("Servicio 1"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].abreviatura").value("Serv1"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].contacto").value("Nombre Apellidos"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].seccion.id").value(1))
        .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].id").value(2))
        .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].nombre").value("Servicio 2"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].abreviatura").value("Serv2"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].contacto").value("Nombre Apellidos"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].seccion.id").value(1));
  }

  @Test
  public void getServiciosPagedFilter_ReturnsEmptyList() throws Exception {
    // given: lista vacía de servicios
    List<Servicio> seccionList = new ArrayList<>();

    Page<Servicio> pagedServicio = new PageImpl<Servicio>(seccionList);

    BDDMockito
        .given(seccionService.findAllLike(ArgumentMatchers.<ServicioFilter>any(), ArgumentMatchers.<Pageable>any()))
        .willReturn(pagedServicio);

    // when: se realiza la búsqueda de servicios
    mockMvc
        .perform(MockMvcRequestBuilders
            .get(ConstantesCat.SERVICIO_CONTROLLER_BASE_PATH + ConstantesCat.PATH_ALL + ConstantesCat.PATH_PAGEFILTERED)

            .contentType(MediaType.APPLICATION_JSON).param("abreviatura", "Servicio").param("page", "0")
            .param("size", "10").param("sort", "id,desc"))
        .andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isOk())
        .andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.jsonPath("$").hasJsonPath())
        // then: se recupera la página vacía de servicios
        .andExpect(MockMvcResultMatchers.jsonPath("$.totalElements").value(0))
        .andExpect(MockMvcResultMatchers.jsonPath("$.totalPages").value(1));
  }

  @Test
  public void newServicio_ReturnsServicio() throws Exception {
    // given: Un servicio nuevo
    String nuevoServicioJson = "{\"nombre\": \"Servicio1\", \"abreviatura\": \"Serv1\", \"contacto\": \"Contacto 1\", \"seccion\": {\"id\": \"1\",\"nombre\": \"Seccion 1\", \"descripcion\": \"Seccion servicio 1\"}}";

    Seccion seccion = new Seccion(1L, "Seccion 1", "Seccion create test");

    Servicio servicio = new Servicio();
    servicio.setId(1L);
    servicio.setNombre("Servicio1");
    servicio.setAbreviatura("Serv1");
    servicio.setContacto("Contacto 1");
    servicio.setSeccion(seccion);

    BDDMockito.given(seccionService.create(ArgumentMatchers.<Servicio>any())).willReturn(servicio);

    // when: Creamos un servicio
    mockMvc
        .perform(MockMvcRequestBuilders.post(ConstantesCat.SERVICIO_CONTROLLER_BASE_PATH)
            .contentType(MediaType.APPLICATION_JSON).content(nuevoServicioJson))
        .andDo(MockMvcResultHandlers.print())
        // then: Crea el nuevo servicio y lo devuelve
        .andExpect(MockMvcResultMatchers.status().isCreated()).andExpect(MockMvcResultMatchers.jsonPath("id").value(1))
        .andExpect(MockMvcResultMatchers.jsonPath("nombre").value("Servicio1"));
  }

  // @Test
  public void newServicio_Error_Returns400() throws Exception {
    // given: Un servicio nuevo que produce un error al crearse
    String nuevoServicioJson = "{\"nombre\": \"Servicio1\", \"abreviatura\": \"Serv1\", \"contacto\": \"Contacto 1\", \"seccion\": {\"id\": \"1\",\"nombre\": \"Seccion 1\", \"descripcion\": \"Seccion servicio 1\"}}";

    BDDMockito.given(seccionService.create(ArgumentMatchers.<Servicio>any())).willThrow(new IllegalArgumentException());

    // when: Creamos un servicio
    mockMvc
        .perform(MockMvcRequestBuilders.post(ConstantesCat.SERVICIO_CONTROLLER_BASE_PATH)
            .contentType(MediaType.APPLICATION_JSON).content(nuevoServicioJson))
        .andDo(MockMvcResultHandlers.print())
        // then: Devueve un error 400
        .andExpect(MockMvcResultMatchers.status().isBadRequest());

  }

  @Test
  public void replaceServicio_ReturnsServicio() throws Exception {
    // given: Un servicio a modificar
    String replaceServicioJson = "{\"nombre\": \"Servicio1\", \"abreviatura\": \"Servi1\", \"contacto\": \"Contacto 1\", \"seccion\": {\"id\": \"1\",\"nombre\": \"Seccion 1\", \"descripcion\": \"Seccion servicio 1\"}}";

    Seccion seccion = new Seccion(1L, "Seccion 1", "Seccion create test");

    Servicio servicio = new Servicio();
    servicio.setId(1L);
    servicio.setNombre("Servicio1");
    servicio.setAbreviatura("Servi1");
    servicio.setContacto("Contacto 1");
    servicio.setSeccion(seccion);
    BDDMockito.given(seccionService.update(ArgumentMatchers.<Servicio>any())).willReturn(servicio);

    mockMvc
        .perform(MockMvcRequestBuilders
            .put(ConstantesCat.SERVICIO_CONTROLLER_BASE_PATH + ConstantesCat.PATH_PARAMETER_ID, 1L)
            .contentType(MediaType.APPLICATION_JSON).content(replaceServicioJson))
        .andDo(MockMvcResultHandlers.print())
        // then: Modifica el servicio y lo devuelve
        .andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.jsonPath("id").value(1))
        .andExpect(MockMvcResultMatchers.jsonPath("abreviatura").value("Servi1"));

  }

  // @Test
  public void replaceServicio_NotFound() throws Exception {
    // given: Un servicio a modificar
    String replaceServicioJson = "{\"nombre\": \"Servicio1\", \"abreviatura\": \"Serv1\", \"contacto\": \"Contacto 1\", \"seccion\": {\"id\": \"1\",\"nombre\": \"Seccion 1\", \"descripcion\": \"Seccion servicio 1\"}}";

    Seccion seccion = new Seccion(1L, "Seccion 1", "Seccion create test");

    Servicio servicio = new Servicio();
    servicio.setId(1L);
    servicio.setNombre("Servicio1");
    servicio.setAbreviatura("Serv1");
    servicio.setContacto("Contacto 1");
    servicio.setSeccion(seccion);

    BDDMockito.given(seccionService.update(ArgumentMatchers.<Servicio>any())).will((InvocationOnMock invocation) -> {
      throw new ServicioNotFoundException(((Servicio) invocation.getArgument(0)).getId());
    });
    mockMvc
        .perform(MockMvcRequestBuilders
            .put(ConstantesCat.SERVICIO_CONTROLLER_BASE_PATH + ConstantesCat.PATH_PARAMETER_ID, 1L)
            .contentType(MediaType.APPLICATION_JSON).content(replaceServicioJson))
        .andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isNotFound());

  }

  @Test
  public void getServicio_WithId_ReturnsServicio() throws Exception {

    Seccion seccion = new Seccion(1L, "Seccion 1", "Seccion create test");
    BDDMockito.given(seccionService.findById(ArgumentMatchers.anyLong()))
        .willReturn(new Servicio(1L, "Servicio 1", "Serv1", "Nombre Apellidos", seccion));

    mockMvc
        .perform(MockMvcRequestBuilders
            .get(ConstantesCat.SERVICIO_CONTROLLER_BASE_PATH + ConstantesCat.PATH_PARAMETER_ID, 1L))
        .andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("id").value(1))
        .andExpect(MockMvcResultMatchers.jsonPath("nombre").value("Servicio 1"))
        .andExpect(MockMvcResultMatchers.jsonPath("abreviatura").value("Serv1"));
  }

  // @Test
  public void getServicio_NotFound_Returns404() throws Exception {
    BDDMockito.given(seccionService.findById(ArgumentMatchers.anyLong())).will((InvocationOnMock invocation) -> {
      throw new ServicioNotFoundException(invocation.getArgument(0));
    });
    mockMvc
        .perform(MockMvcRequestBuilders
            .get(ConstantesCat.SERVICIO_CONTROLLER_BASE_PATH + ConstantesCat.PATH_PARAMETER_ID, 1L))
        .andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isNotFound());
  }

  @Test
  public void delete_Success() throws Exception {
    mockMvc
        .perform(MockMvcRequestBuilders
            .delete(ConstantesCat.SERVICIO_CONTROLLER_BASE_PATH + ConstantesCat.PATH_PARAMETER_ID, 1L))
        .andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isOk());

  }

}
