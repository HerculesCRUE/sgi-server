package org.crue.hercules.sgi.catservice.integration;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.assertj.core.api.Assertions;
import org.crue.hercules.sgi.catservice.model.Seccion;
import org.crue.hercules.sgi.catservice.model.Servicio;
import org.crue.hercules.sgi.catservice.model.TipoFungible;
import org.crue.hercules.sgi.catservice.util.ConstantesCat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;

/**
 * Test de integracion de TipoFungible.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TipoFungibleIT {

  @Autowired
  private TestRestTemplate restTemplate;

  @Sql
  @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:cleanup.sql")
  @Test
  public void getTipoFungible_ReturnsTipoFungibleList() throws Exception {
    final ResponseEntity<List<TipoFungible>> response = restTemplate.exchange(
        ConstantesCat.TIPO_FUNGIBLE_CONTROLLER_BASE_PATH + ConstantesCat.PATH_ALL, HttpMethod.GET, null,
        new ParameterizedTypeReference<List<TipoFungible>>() {
        });

    Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

    final List<TipoFungible> tipoFungible = response.getBody();

    Assertions.assertThat(tipoFungible.size()).isEqualTo(2);
    Assertions.assertThat(
        tipoFungible.stream().map(TipoFungible::getId).collect(Collectors.toList()).containsAll(Arrays.asList(1L, 2L)))
        .isTrue();
    Assertions.assertThat(tipoFungible.stream().map(TipoFungible::getNombre).collect(Collectors.toList())
        .containsAll(Arrays.asList("TipoFungible1", "TipoFungible1"))).isTrue();
  }

  @Sql
  @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:cleanup.sql")
  @Test
  public void getTipoFungible_ReturnsTipoFungiblePage() throws Exception {
    final ResponseEntity<RestResponsePage<TipoFungible>> response = restTemplate.exchange(
        ConstantesCat.TIPO_FUNGIBLE_CONTROLLER_BASE_PATH + ConstantesCat.PATH_ALL + ConstantesCat.PATH_PAGE,
        HttpMethod.GET, null, new ParameterizedTypeReference<RestResponsePage<TipoFungible>>() {
        });

    Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

    final List<TipoFungible> tipoFungible = response.getBody().getContent();

    Assertions.assertThat(tipoFungible.size()).isEqualTo(2);
    Assertions.assertThat(
        tipoFungible.stream().map(TipoFungible::getId).collect(Collectors.toList()).containsAll(Arrays.asList(1L, 2L)))
        .isTrue();
    Assertions.assertThat(tipoFungible.stream().map(TipoFungible::getNombre).collect(Collectors.toList())
        .containsAll(Arrays.asList("TipoFungible1", "TipoFungible2"))).isTrue();
  }

  @Sql
  @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:cleanup.sql")
  @Test
  public void getTipoFungible_ReturnsTipoFungiblePageFiltered() throws Exception {
    final String url = new StringBuilder(ConstantesCat.TIPO_FUNGIBLE_CONTROLLER_BASE_PATH)
        .append(ConstantesCat.PATH_ALL).append(ConstantesCat.PATH_PAGEFILTERED)
        .append(ConstantesCat.PATH_PARAMETERS_PAGE).append("&nombre={filtroNombre}").toString();

    final HashMap<String, String> urlVariables = new HashMap<>();
    urlVariables.put("size", "2");
    urlVariables.put("page", "1");
    urlVariables.put("filtroNombre", "TipoFungible1");

    final ResponseEntity<RestResponsePage<TipoFungible>> response = restTemplate.exchange(url, HttpMethod.GET, null,
        new ParameterizedTypeReference<RestResponsePage<TipoFungible>>() {
        }, urlVariables);

    Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

    final List<TipoFungible> tipoFungible = response.getBody().getContent();

    Assertions.assertThat(tipoFungible.size()).isEqualTo(1);
    Assertions.assertThat(tipoFungible.stream().map(TipoFungible::getId).collect(Collectors.toList()).contains(3L))
        .isTrue();
    Assertions
        .assertThat(
            tipoFungible.stream().map(TipoFungible::getNombre).collect(Collectors.toList()).contains("TipoFungible1"))
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
  public void getTipoFungible_WithId_ReturnsTipoFungible() throws Exception {
    final ResponseEntity<TipoFungible> response = restTemplate.getForEntity(
        ConstantesCat.TIPO_FUNGIBLE_CONTROLLER_BASE_PATH + ConstantesCat.PATH_PARAMETER_ID, TipoFungible.class, 1L);

    Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

    final TipoFungible tipoFungible = response.getBody();

    Assertions.assertThat(tipoFungible.getId()).isEqualTo(1L);
    Assertions.assertThat(tipoFungible.getNombre()).isEqualTo("TipoFungible1");
  }

  @Sql
  @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:cleanup.sql")
  @Test
  public void addTipoFungible_ReturnsTipoFungible() throws Exception {

    Seccion seccion = new Seccion();
    seccion.setId(1L);
    seccion.setNombre("Seccion 1");
    seccion.setDescripcion("Seccion 1");

    Servicio servicio = new Servicio();
    servicio.setId(1L);
    servicio.setNombre("Servicio 1");
    servicio.setAbreviatura("Serv1");
    servicio.setContacto("Nombre Apellido");
    servicio.setSeccion(seccion);

    TipoFungible nuevoTipoFungible = new TipoFungible();
    nuevoTipoFungible.setNombre("TipoFungible1");
    nuevoTipoFungible.setServicio(servicio);

    ResponseEntity<TipoFungible> response = restTemplate.postForEntity(ConstantesCat.TIPO_FUNGIBLE_CONTROLLER_BASE_PATH,
        nuevoTipoFungible, TipoFungible.class);

    Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

    TipoFungible tipoFungible = response.getBody();

    Assertions.assertThat(tipoFungible.getId()).isNotNull();
    Assertions.assertThat(tipoFungible.getNombre()).isEqualTo(nuevoTipoFungible.getNombre());
    Assertions.assertThat(tipoFungible.getServicio().getId()).isEqualTo(nuevoTipoFungible.getServicio().getId());
  }

  @Sql
  @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:cleanup.sql")
  @Test
  public void removeTipoFungible_DoNotGetTipoFungible() throws Exception {

    restTemplate.delete(ConstantesCat.TIPO_FUNGIBLE_CONTROLLER_BASE_PATH + ConstantesCat.PATH_PARAMETER_ID, 1L);
    ResponseEntity<TipoFungible> response = restTemplate.getForEntity(
        ConstantesCat.TIPO_FUNGIBLE_CONTROLLER_BASE_PATH + ConstantesCat.PATH_PARAMETER_ID, TipoFungible.class, 1l);

    Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

  }

  @Sql
  @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:cleanup.sql")
  @Test
  public void replaceTipoFungible_ReturnsTipoFungible() throws Exception {

    Seccion seccion = new Seccion(1L, "Seccion 1", "Seccion create test");
    Servicio servicio = new Servicio(1L, "Servicio 1", "Serv1", "Nombre Apellidos", seccion);

    TipoFungible replaceTipoFungible = new TipoFungible();
    replaceTipoFungible.setNombre("TipoFungible2");
    replaceTipoFungible.setServicio(servicio);

    final HttpEntity<TipoFungible> requestEntity = new HttpEntity<TipoFungible>(replaceTipoFungible, new HttpHeaders());

    final ResponseEntity<TipoFungible> response = restTemplate.exchange(

        ConstantesCat.TIPO_FUNGIBLE_CONTROLLER_BASE_PATH + ConstantesCat.PATH_PARAMETER_ID, HttpMethod.PUT,
        requestEntity, TipoFungible.class, 1L);

    Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

    final TipoFungible tipoFungible = response.getBody();

    Assertions.assertThat(tipoFungible.getId()).isNotNull();
    Assertions.assertThat(tipoFungible.getNombre()).isEqualTo(replaceTipoFungible.getNombre());
    Assertions.assertThat(tipoFungible.getServicio()).isEqualTo(replaceTipoFungible.getServicio());
  }

}