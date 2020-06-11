package org.crue.hercules.sgi.catservice.integration;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.assertj.core.api.Assertions;
import org.crue.hercules.sgi.catservice.model.UnidadMedida;
import org.crue.hercules.sgi.catservice.util.ConstantesCat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;

/**
 * Test de integracion de UnidadMedida.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UnidadMedidaIT {

  @Autowired
  private TestRestTemplate restTemplate;

  @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:cleanup.sql")
  @Test
  public void addUnidadMedida_ReturnsUnidadMedida() throws Exception {

    final UnidadMedida newUnidadMedida = new UnidadMedida();
    newUnidadMedida.setAbreviatura("UM1");
    newUnidadMedida.setDescripcion("UnidadMedida1");

    final String url = new StringBuilder(ConstantesCat.UNIDAD_MEDIDA_CONTROLLER_BASE_PATH).toString();

    final ResponseEntity<UnidadMedida> response = restTemplate.postForEntity(url, newUnidadMedida, UnidadMedida.class);

    Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

    final UnidadMedida unidadMedida = response.getBody();

    Assertions.assertThat(unidadMedida.getId()).isNotNull();
    Assertions.assertThat(unidadMedida.getAbreviatura()).isEqualTo(newUnidadMedida.getAbreviatura());
    Assertions.assertThat(unidadMedida.getDescripcion()).isEqualTo(newUnidadMedida.getDescripcion());
  }

  @Sql
  @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:cleanup.sql")
  @Test
  public void replaceUnidadMedida_ReturnsUnidadMedida() throws Exception {

    final UnidadMedida updatedUnidadMedida = new UnidadMedida();
    updatedUnidadMedida.setAbreviatura("UM01");
    updatedUnidadMedida.setDescripcion("UnidadMedida01");

    final String url = new StringBuilder(ConstantesCat.UNIDAD_MEDIDA_CONTROLLER_BASE_PATH)//
        .append(ConstantesCat.PATH_PARAMETER_ID)//
        .toString();

    restTemplate.put(url, updatedUnidadMedida, 1L);

    final ResponseEntity<UnidadMedida> response = restTemplate.getForEntity(url, UnidadMedida.class, 1L);

    Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

    final UnidadMedida unidadMedida = response.getBody();

    Assertions.assertThat(unidadMedida.getId()).isNotNull();
    Assertions.assertThat(unidadMedida.getAbreviatura()).isEqualTo(updatedUnidadMedida.getAbreviatura());
    Assertions.assertThat(unidadMedida.getDescripcion()).isEqualTo(updatedUnidadMedida.getDescripcion());
  }

  @Sql
  @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:cleanup.sql")
  @Test
  public void removeUnidadMedida_DoNotGetUnidadMedida() throws Exception {

    final String url = new StringBuilder(ConstantesCat.UNIDAD_MEDIDA_CONTROLLER_BASE_PATH)//
        .append(ConstantesCat.PATH_PARAMETER_ID)//
        .toString();

    restTemplate.delete(url, 1L);

    final ResponseEntity<UnidadMedida> response = restTemplate.getForEntity(url, UnidadMedida.class, 1L);

    Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
  }

  @Sql
  @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:cleanup.sql")
  @Test
  public void getUnidadMedidas_ReturnsUnidaMedidaList() throws Exception {

    final String url = new StringBuilder(ConstantesCat.UNIDAD_MEDIDA_CONTROLLER_BASE_PATH)//
        .append(ConstantesCat.PATH_ALL)//
        .toString();

    final ResponseEntity<List<UnidadMedida>> response = restTemplate.exchange(url, HttpMethod.GET, null,
        new ParameterizedTypeReference<List<UnidadMedida>>() {
        });

    Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

    final List<UnidadMedida> unidadMedidas = response.getBody();

    Assertions.assertThat(unidadMedidas.size()).isEqualTo(2);
    Assertions.assertThat(
        unidadMedidas.stream().map(UnidadMedida::getId).collect(Collectors.toList()).containsAll(Arrays.asList(1L, 2L)))
        .isTrue();
    Assertions.assertThat(unidadMedidas.stream().map(UnidadMedida::getAbreviatura).collect(Collectors.toList())
        .containsAll(Arrays.asList("UM1", "UM2"))).isTrue();
  }

  @Sql
  @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:cleanup.sql")
  @Test
  public void getUnidadMedidasPage_ReturnsPage() throws Exception {

    final String url = new StringBuilder(ConstantesCat.UNIDAD_MEDIDA_CONTROLLER_BASE_PATH)//
        .append(ConstantesCat.PATH_ALL)//
        .append(ConstantesCat.PATH_PAGE)//
        .append(ConstantesCat.PATH_PARAMETERS_PAGE)//
        .toString();

    final HashMap<String, String> urlVariables = new HashMap<>();
    urlVariables.put("size", "2");
    urlVariables.put("page", "2");

    final ResponseEntity<RestResponsePage<UnidadMedida>> response = restTemplate.exchange(url, HttpMethod.GET, null,
        new ParameterizedTypeReference<RestResponsePage<UnidadMedida>>() {
        }, urlVariables);

    Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

    final List<UnidadMedida> unidadMedidas = response.getBody().getContent();

    Assertions.assertThat(unidadMedidas.size()).isEqualTo(1);
    Assertions.assertThat(unidadMedidas.stream().map(UnidadMedida::getId).collect(Collectors.toList()).contains(5L))
        .isTrue();
    Assertions
        .assertThat(
            unidadMedidas.stream().map(UnidadMedida::getAbreviatura).collect(Collectors.toList()).contains("UM5"))
        .isTrue();
    Assertions.assertThat(response.getBody().getTotalElements()).isEqualTo(5);
    Assertions.assertThat(response.getBody().getTotalPages()).isEqualTo(3);
    Assertions.assertThat(response.getBody().getPageable().getPageSize()).isEqualTo(2);
    Assertions.assertThat(response.getBody().getPageable().getPageNumber()).isEqualTo(2);
    Assertions.assertThat(response.getBody().getPageable().hasPrevious()).isTrue();
  }

  @Sql
  @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:cleanup.sql")
  @Test
  public void getUnidadMedidasFilteredPage_ReturnsPage() throws Exception {

    final String url = new StringBuilder(ConstantesCat.UNIDAD_MEDIDA_CONTROLLER_BASE_PATH)//
        .append(ConstantesCat.PATH_ALL)//
        .append(ConstantesCat.PATH_PAGEFILTERED)//
        .append(ConstantesCat.PATH_PARAMETERS_PAGE)//
        .append("&abreviatura={filtroAbreviatura}")//
        .toString();

    final HashMap<String, String> urlVariables = new HashMap<>();
    urlVariables.put("size", "2");
    urlVariables.put("page", "1");
    urlVariables.put("filtroAbreviatura", "UM0");

    final ResponseEntity<RestResponsePage<UnidadMedida>> response = restTemplate.exchange(url, HttpMethod.GET, null,
        new ParameterizedTypeReference<RestResponsePage<UnidadMedida>>() {
        }, urlVariables);

    Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

    final List<UnidadMedida> unidadMedidas = response.getBody().getContent();

    Assertions.assertThat(unidadMedidas.size()).isEqualTo(1);
    Assertions.assertThat(unidadMedidas.stream().map(UnidadMedida::getId).collect(Collectors.toList()).contains(3L))
        .isTrue();
    Assertions
        .assertThat(
            unidadMedidas.stream().map(UnidadMedida::getAbreviatura).collect(Collectors.toList()).contains("UM03"))
        .isTrue();
    Assertions.assertThat(response.getBody().getTotalElements()).isEqualTo(3);
    Assertions.assertThat(response.getBody().getTotalPages()).isEqualTo(2);
    Assertions.assertThat(response.getBody().getPageable().getPageSize()).isEqualTo(2);
    Assertions.assertThat(response.getBody().getPageable().getPageNumber()).isEqualTo(1);
    Assertions.assertThat(response.getBody().getPageable().hasPrevious()).isTrue();
  }

  @Sql
  @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:cleanup.sql")
  @Test
  public void getUnidadMedida_WithId_ReturnsUnidadMedida() throws Exception {

    final String url = new StringBuilder(ConstantesCat.UNIDAD_MEDIDA_CONTROLLER_BASE_PATH)//
        .append(ConstantesCat.PATH_PARAMETER_ID)//
        .toString();

    ResponseEntity<UnidadMedida> response = restTemplate.getForEntity(url, UnidadMedida.class, 1L);

    Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

    final UnidadMedida unidadMedida = response.getBody();

    Assertions.assertThat(unidadMedida.getId()).isEqualTo(1L);
    Assertions.assertThat(unidadMedida.getAbreviatura()).isEqualTo("UM1");

    response = restTemplate.getForEntity(url, UnidadMedida.class, 3L);
    Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
  }

}