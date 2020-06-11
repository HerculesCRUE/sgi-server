package org.crue.hercules.sgi.catservice.controller;

import static org.mockito.ArgumentMatchers.anyLong;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.crue.hercules.sgi.catservice.exceptions.UnidadMedidaNotFoundException;
import org.crue.hercules.sgi.catservice.filter.UnidadMedidaFilter;
import org.crue.hercules.sgi.catservice.model.UnidadMedida;
import org.crue.hercules.sgi.catservice.service.UnidadMedidaService;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

/**
 * UnidadMedidaControllerTest
 */
@WebMvcTest(UnidadMedidaController.class)
public class UnidadMedidaControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private UnidadMedidaService unidadMedidaService;

  @Test
  public void newUnidadMedida_ReturnsUnidadMedida() throws Exception {

    final String url = new StringBuilder(ConstantesCat.UNIDAD_MEDIDA_CONTROLLER_BASE_PATH).toString();
    String nuevoUnidadMedidaJson = "{\"abreviatura\": \"UM1\", \"descripcion\": \"UnidadMedida1\"}";
    UnidadMedida response = new UnidadMedida(1L, "UM1", "UnidadMedida1");

    BDDMockito.given(unidadMedidaService.create(ArgumentMatchers.<UnidadMedida>any())).willReturn(response);

    mockMvc
        .perform(
            MockMvcRequestBuilders.post(url).contentType(MediaType.APPLICATION_JSON).content(nuevoUnidadMedidaJson))
        .andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isCreated())
        .andExpect(MockMvcResultMatchers.jsonPath("id").value(response.getId()))
        .andExpect(MockMvcResultMatchers.jsonPath("abreviatura").value(response.getAbreviatura()))
        .andExpect(MockMvcResultMatchers.jsonPath("descripcion").value(response.getDescripcion()));
  }

  @Test
  public void newUnidadMedida_Returns400() throws Exception {

    final String url = new StringBuilder(ConstantesCat.UNIDAD_MEDIDA_CONTROLLER_BASE_PATH).toString();
    String nuevoUnidadMedidaJson = "{\"id\": 1, \"abreviatura\": \"UM1\", \"descripcion\": \"UnidadMedida1\"}";

    BDDMockito.given(unidadMedidaService.create(ArgumentMatchers.<UnidadMedida>any()))
        .willThrow(new IllegalArgumentException());

    mockMvc
        .perform(
            MockMvcRequestBuilders.post(url).contentType(MediaType.APPLICATION_JSON).content(nuevoUnidadMedidaJson))
        .andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isBadRequest());
  }

  @Test
  public void replaceUnidadMedida_ReturnsUnidadMedida() throws Exception {

    UnidadMedida response = new UnidadMedida(1L, "UM01", "UnidadMedida01");
    final String url = new StringBuilder(ConstantesCat.UNIDAD_MEDIDA_CONTROLLER_BASE_PATH)//
        .append(ConstantesCat.PATH_PARAMETER_ID)//
        .toString();
    String replaceUnidadMedidaJson = "{\"descripcion\": \"UnidadMedida01\", \"abreviatura\": \"UM01\"}";

    BDDMockito.given(unidadMedidaService.update(ArgumentMatchers.<UnidadMedida>any())).willReturn(response);

    mockMvc
        .perform(MockMvcRequestBuilders.put(url, response.getId()).contentType(MediaType.APPLICATION_JSON)
            .content(replaceUnidadMedidaJson))
        .andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("id").value(response.getId()))
        .andExpect(MockMvcResultMatchers.jsonPath("abreviatura").value(response.getAbreviatura()))
        .andExpect(MockMvcResultMatchers.jsonPath("descripcion").value(response.getDescripcion()));
  }

  @Test
  public void replaceUnidadMedida_Returns404() throws Exception {

    UnidadMedida response = new UnidadMedida(1L, "UM01", "UnidadMedida01");
    final String url = new StringBuilder(ConstantesCat.UNIDAD_MEDIDA_CONTROLLER_BASE_PATH)//
        .append(ConstantesCat.PATH_PARAMETER_ID)//
        .toString();
    String replaceUnidadMedidaJson = "{\"descripcion\": \"UnidadMedida01\", \"abreviatura\": \"UM01\"}";

    BDDMockito.given(unidadMedidaService.update(ArgumentMatchers.<UnidadMedida>any()))
        .will((InvocationOnMock invocation) -> {
          throw new UnidadMedidaNotFoundException(((UnidadMedida) invocation.getArgument(0)).getId());
        });
    mockMvc
        .perform(MockMvcRequestBuilders.put(url, response.getId()).contentType(MediaType.APPLICATION_JSON)
            .content(replaceUnidadMedidaJson))
        .andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isNotFound());
  }

  @Test
  public void replaceUnidadMedida_Returns405() throws Exception {

    UnidadMedida response = new UnidadMedida(null, "UM01", "UnidadMedida01");
    final String url = new StringBuilder(ConstantesCat.UNIDAD_MEDIDA_CONTROLLER_BASE_PATH)//
        .append(ConstantesCat.PATH_PARAMETER_ID)//
        .toString();
    String replaceUnidadMedidaJson = "{\"descripcion\": \"UnidadMedida01\", \"abreviatura\": \"UM01\"}";

    BDDMockito.given(unidadMedidaService.update(ArgumentMatchers.<UnidadMedida>isNull()))
        .willThrow(new IllegalArgumentException());

    mockMvc
        .perform(MockMvcRequestBuilders.put(url, response.getId()).contentType(MediaType.APPLICATION_JSON)
            .content(replaceUnidadMedidaJson))
        .andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isMethodNotAllowed());
  }

  @Test
  public void delete_WithId_DoNotGetUnidadMedida() throws Exception {

    UnidadMedida unidaMedida = new UnidadMedida(1L, "UM01", "UnidadMedida01");
    final String url = new StringBuilder(ConstantesCat.UNIDAD_MEDIDA_CONTROLLER_BASE_PATH)//
        .append(ConstantesCat.PATH_PARAMETER_ID)//
        .toString();
    BDDMockito.doNothing().when(unidadMedidaService).delete(anyLong());

    mockMvc.perform(MockMvcRequestBuilders.delete(url, unidaMedida.getId()).contentType(MediaType.APPLICATION_JSON))
        .andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isOk());
  }

  @Test
  public void delete_WithId_Returns405() throws Exception {

    UnidadMedida unidaMedida = new UnidadMedida(null, "UM01", "UnidadMedida01");
    final String url = new StringBuilder(ConstantesCat.UNIDAD_MEDIDA_CONTROLLER_BASE_PATH)//
        .append(ConstantesCat.PATH_PARAMETER_ID)//
        .toString();

    BDDMockito.willThrow(new IllegalArgumentException()).given(unidadMedidaService).delete(anyLong());

    mockMvc.perform(MockMvcRequestBuilders.delete(url, unidaMedida.getId()).contentType(MediaType.APPLICATION_JSON))
        .andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isMethodNotAllowed());
  }

  @Test
  public void delete_WithId_Returns404() throws Exception {

    UnidadMedida unidaMedida = new UnidadMedida(1L, "UM01", "UnidadMedida01");
    final String url = new StringBuilder(ConstantesCat.UNIDAD_MEDIDA_CONTROLLER_BASE_PATH)//
        .append(ConstantesCat.PATH_PARAMETER_ID)//
        .toString();

    BDDMockito.willThrow(new UnidadMedidaNotFoundException(unidaMedida.getId())).given(unidadMedidaService)
        .delete(anyLong());

    mockMvc.perform(MockMvcRequestBuilders.delete(url, unidaMedida.getId()).contentType(MediaType.APPLICATION_JSON))
        .andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isNotFound());
  }

  @Test
  public void getUnidadMedidaAll_ReturnsUnidadMedidaList() throws Exception {

    final String url = new StringBuilder(ConstantesCat.UNIDAD_MEDIDA_CONTROLLER_BASE_PATH)//
        .append(ConstantesCat.PATH_ALL)//
        .toString();

    List<UnidadMedida> response = new LinkedList<UnidadMedida>();
    response.add(new UnidadMedida(1L, "UM1", "UnidadMedida1"));
    response.add(new UnidadMedida(2L, "UM2", "UnidadMedida2"));

    BDDMockito.given(unidadMedidaService.findAll()).willReturn(response);

    mockMvc.perform(MockMvcRequestBuilders.get(url).contentType(MediaType.APPLICATION_JSON))
        .andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(2)))
        .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(response.get(0).getId()))
        .andExpect(MockMvcResultMatchers.jsonPath("$[0].abreviatura").value(response.get(0).getAbreviatura()))
        .andExpect(MockMvcResultMatchers.jsonPath("$[0].descripcion").value(response.get(0).getDescripcion()))
        .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(response.get(1).getId()))
        .andExpect(MockMvcResultMatchers.jsonPath("$[1].abreviatura").value(response.get(1).getAbreviatura()))
        .andExpect(MockMvcResultMatchers.jsonPath("$[1].descripcion").value(response.get(1).getDescripcion()));
  }

  @Test
  public void getUnidadMedidaAll_ReturnsEmptyList() throws Exception {

    final String url = new StringBuilder(ConstantesCat.UNIDAD_MEDIDA_CONTROLLER_BASE_PATH)//
        .append(ConstantesCat.PATH_ALL)//
        .toString();

    BDDMockito.given(unidadMedidaService.findAll()).willReturn(Collections.emptyList());

    mockMvc.perform(MockMvcRequestBuilders.get(url)).andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(0)));
  }

  @Test
  public void getUnidadMedidaAllPage_ReturnsPage() throws Exception {

    final HashMap<String, String> urlVariables = new HashMap<>();
    urlVariables.put("size", "2");
    urlVariables.put("page", "1");

    String url = new StringBuilder(ConstantesCat.UNIDAD_MEDIDA_CONTROLLER_BASE_PATH)//
        .append(ConstantesCat.PATH_ALL)//
        .append(ConstantesCat.PATH_PAGE)//
        .append(ConstantesCat.PATH_PARAMETERS_PAGE)//
        .toString();

    List<UnidadMedida> response = new ArrayList<>();
    response.add(new UnidadMedida(1L, "UM1", "UnidadMedida1"));
    response.add(new UnidadMedida(2L, "UM2", "UnidadMedida2"));
    response.add(new UnidadMedida(3L, "UM3", "UnidadMedida3"));

    PageRequest pageable = PageRequest.of(Integer.parseInt(urlVariables.get("page")),
        Integer.parseInt(urlVariables.get("size")), Sort.unsorted());

    Page<UnidadMedida> pageResponse = new PageImpl<UnidadMedida>(response.subList(2, 3), pageable, response.size());

    BDDMockito.given(unidadMedidaService.findAll(pageable)).willReturn(pageResponse);

    mockMvc
        .perform(MockMvcRequestBuilders.get(url, urlVariables.get("size"), urlVariables.get("page"))
            .contentType(MediaType.APPLICATION_JSON))
        .andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isOk())
        .andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.jsonPath("$").hasJsonPath())
        .andExpect(MockMvcResultMatchers.jsonPath("$.content", Matchers.hasSize(1)))
        .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].id").value(response.get(2).getId()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].abreviatura").value(response.get(2).getAbreviatura()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].descripcion").value(response.get(2).getDescripcion()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.pageable.pageSize").value(urlVariables.get("size")))
        .andExpect(MockMvcResultMatchers.jsonPath("$.pageable.pageNumber").value(urlVariables.get("page")))
        .andExpect(MockMvcResultMatchers.jsonPath("$.totalElements").value(response.size()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.first").value("false"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.last").value("true"));
  }

  @Test
  public void getUnidadMedidaAllPageFiltered_ReturnsPage() throws Exception {

    final HashMap<String, String> urlVariables = new HashMap<>();
    urlVariables.put("size", "2");
    urlVariables.put("page", "0");
    urlVariables.put("filtroAbreviatura", "UM0");

    String url = new StringBuilder(ConstantesCat.UNIDAD_MEDIDA_CONTROLLER_BASE_PATH)//
        .append(ConstantesCat.PATH_ALL)//
        .append(ConstantesCat.PATH_PAGEFILTERED)//
        .append(ConstantesCat.PATH_PARAMETERS_PAGE)//
        .append("&abreviatura={filtroAbreviatura}")//
        .toString();

    List<UnidadMedida> response = new ArrayList<>();
    response.add(new UnidadMedida(3L, "UM03", "UnidadMedida03"));

    UnidadMedidaFilter filter = new UnidadMedidaFilter();
    filter.setAbreviatura(urlVariables.get("filtroAbreviatura"));

    PageRequest pageable = PageRequest.of(Integer.parseInt(urlVariables.get("page")),
        Integer.parseInt(urlVariables.get("size")), Sort.unsorted());

    Page<UnidadMedida> pageResponse = new PageImpl<UnidadMedida>(response, pageable, response.size());

    BDDMockito.given(unidadMedidaService.findAllLike(filter, pageable)).willReturn(pageResponse);

    mockMvc
        .perform(MockMvcRequestBuilders
            .get(url, urlVariables.get("size"), urlVariables.get("page"), urlVariables.get("filtroAbreviatura"))
            .contentType(MediaType.APPLICATION_JSON))
        .andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isOk())
        .andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.jsonPath("$").hasJsonPath())
        .andExpect(MockMvcResultMatchers.jsonPath("$.content", Matchers.hasSize(1)))
        .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].id").value(response.get(0).getId()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].abreviatura").value(response.get(0).getAbreviatura()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].descripcion").value(response.get(0).getDescripcion()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.pageable.pageSize").value(urlVariables.get("size")))
        .andExpect(MockMvcResultMatchers.jsonPath("$.pageable.pageNumber").value(urlVariables.get("page")))
        .andExpect(MockMvcResultMatchers.jsonPath("$.totalElements").value(response.size()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.first").value("true"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.last").value("true"));
  }

  @Test
  public void getOne_WithId_ReturnsUnidadMedida() throws Exception {

    UnidadMedida response = new UnidadMedida(1L, "UM1", "UnidadMedida1");
    final String url = new StringBuilder(ConstantesCat.UNIDAD_MEDIDA_CONTROLLER_BASE_PATH)//
        .append(ConstantesCat.PATH_PARAMETER_ID)//
        .toString();

    BDDMockito.given(unidadMedidaService.findById(response.getId())).willReturn(response);

    mockMvc.perform(MockMvcRequestBuilders.get(url, response.getId()).contentType(MediaType.APPLICATION_JSON))
        .andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("id").value(response.getId()))
        .andExpect(MockMvcResultMatchers.jsonPath("abreviatura").value(response.getAbreviatura()))
        .andExpect(MockMvcResultMatchers.jsonPath("descripcion").value(response.getDescripcion()));
  }

  @Test
  public void getOne_WithId_NotFound_Returns404() throws Exception {

    UnidadMedida response = new UnidadMedida(1L, "UM1", "UnidadMedida1");
    final String url = new StringBuilder(ConstantesCat.UNIDAD_MEDIDA_CONTROLLER_BASE_PATH)//
        .append(ConstantesCat.PATH_PARAMETER_ID)//
        .toString();

    BDDMockito.given(unidadMedidaService.findById(ArgumentMatchers.anyLong())).will((InvocationOnMock invocation) -> {
      throw new UnidadMedidaNotFoundException((Long) invocation.getArgument(0));
    });
    mockMvc.perform(MockMvcRequestBuilders.get(url, response.getId()).contentType(MediaType.APPLICATION_JSON))
        .andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isNotFound());
  }
}
