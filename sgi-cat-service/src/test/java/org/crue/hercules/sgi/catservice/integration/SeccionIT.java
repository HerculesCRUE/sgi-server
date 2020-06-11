package org.crue.hercules.sgi.catservice.integration;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.assertj.core.api.Assertions;
import org.crue.hercules.sgi.catservice.model.Seccion;
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
 * Test de integracion de Seccion.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SeccionIT {

  @Autowired
  private TestRestTemplate restTemplate;

  @Sql
  @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:cleanup.sql")
  @Test
  public void getSecciones_ReturnsSeccionList() throws Exception {
    final ResponseEntity<List<Seccion>> response = restTemplate.exchange(
        ConstantesCat.SECCION_CONTROLLER_BASE_PATH + ConstantesCat.PATH_ALL, HttpMethod.GET, null,
        new ParameterizedTypeReference<List<Seccion>>() {
        });

    Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

    final List<Seccion> secciones = response.getBody();

    Assertions.assertThat(secciones.size()).isEqualTo(2);
    Assertions
        .assertThat(
            secciones.stream().map(Seccion::getId).collect(Collectors.toList()).containsAll(Arrays.asList(1L, 2L)))
        .isTrue();
    Assertions.assertThat(secciones.stream().map(Seccion::getNombre).collect(Collectors.toList())
        .containsAll(Arrays.asList("Seccion 1", "Seccion 2"))).isTrue();
  }

  @Sql
  @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:cleanup.sql")
  @Test
  public void getSecciones_ReturnsSeccionPage() throws Exception {
    final ResponseEntity<RestResponsePage<Seccion>> response = restTemplate.exchange(
        ConstantesCat.SECCION_CONTROLLER_BASE_PATH + ConstantesCat.PATH_ALL + ConstantesCat.PATH_PAGE, HttpMethod.GET,
        null, new ParameterizedTypeReference<RestResponsePage<Seccion>>() {
        });

    Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

    final List<Seccion> secciones = response.getBody().getContent();

    Assertions.assertThat(secciones.size()).isEqualTo(2);
    Assertions
        .assertThat(
            secciones.stream().map(Seccion::getId).collect(Collectors.toList()).containsAll(Arrays.asList(1L, 2L)))
        .isTrue();
    Assertions.assertThat(secciones.stream().map(Seccion::getNombre).collect(Collectors.toList())
        .containsAll(Arrays.asList("Seccion 1", "Seccion 2"))).isTrue();
  }

  @Sql
  @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:cleanup.sql")
  @Test
  public void getSecciones_ReturnsSeccionPageFiltered() throws Exception {
    final ResponseEntity<RestResponsePage<Seccion>> response = restTemplate.exchange(
        ConstantesCat.SECCION_CONTROLLER_BASE_PATH + ConstantesCat.PATH_ALL + ConstantesCat.PATH_PAGEFILTERED,
        HttpMethod.GET, null, new ParameterizedTypeReference<RestResponsePage<Seccion>>() {
        });

    Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

    final List<Seccion> secciones = response.getBody().getContent();

    Assertions.assertThat(secciones.size()).isEqualTo(3);
    Assertions
        .assertThat(
            secciones.stream().map(Seccion::getId).collect(Collectors.toList()).containsAll(Arrays.asList(1L, 2L)))
        .isTrue();
    Assertions.assertThat(secciones.stream().map(Seccion::getNombre).collect(Collectors.toList())
        .containsAll(Arrays.asList("Seccion 1", "Seccion 2", "Seccion 3"))).isTrue();
  }

  @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:cleanup.sql")
  @Test
  public void addSeccion_ReturnsSeccion() throws Exception {
    Seccion nuevaSeccion = new Seccion();
    nuevaSeccion.setNombre("Seccion 2");
    nuevaSeccion.setDescripcion("Nueva Seccion");

    ResponseEntity<Seccion> response = restTemplate.postForEntity(ConstantesCat.SECCION_CONTROLLER_BASE_PATH,
        nuevaSeccion, Seccion.class);
    Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

    Seccion seccion = response.getBody();

    Assertions.assertThat(seccion.getId()).isNotNull();
    Assertions.assertThat(seccion.getNombre()).isEqualTo(nuevaSeccion.getNombre());
    Assertions.assertThat(seccion.getDescripcion()).isEqualTo(nuevaSeccion.getDescripcion());
  }

  @Sql
  @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:cleanup.sql")
  @Test
  public void replaceSeccion_ReturnsSeccion() throws Exception {
    Seccion replaceSeccion = new Seccion();
    replaceSeccion.setId(1L);
    replaceSeccion.setNombre("Seccion replace");
    replaceSeccion.setDescripcion("Replace Seccion");

    final HttpEntity<Seccion> requestEntity = new HttpEntity<Seccion>(replaceSeccion, new HttpHeaders());
    final ResponseEntity<Seccion> response = restTemplate.exchange(
        ConstantesCat.SECCION_CONTROLLER_BASE_PATH + ConstantesCat.PATH_PARAMETER_ID, HttpMethod.PUT, requestEntity,
        Seccion.class, 1L);

    Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

    final Seccion seccion = response.getBody();

    Assertions.assertThat(seccion.getId()).isNotNull();
    Assertions.assertThat(seccion.getNombre()).isEqualTo(replaceSeccion.getNombre());
  }

  @Sql
  @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:cleanup.sql")
  @Test
  public void removeSeccion_DoNotGetSeccion() throws Exception {
    restTemplate.delete(ConstantesCat.SECCION_CONTROLLER_BASE_PATH + ConstantesCat.PATH_PARAMETER_ID, 1L);

    final ResponseEntity<Seccion> response = restTemplate
        .getForEntity(ConstantesCat.SECCION_CONTROLLER_BASE_PATH + ConstantesCat.PATH_PARAMETER_ID, Seccion.class, 1L);

    Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
  }

  @Sql
  @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:cleanup.sql")
  @Test
  public void getSeccion_WithId_ReturnsSeccion() throws Exception {
    final ResponseEntity<Seccion> response = restTemplate
        .getForEntity(ConstantesCat.SECCION_CONTROLLER_BASE_PATH + ConstantesCat.PATH_PARAMETER_ID, Seccion.class, 1L);

    Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

    final Seccion unidadMedida = response.getBody();

    Assertions.assertThat(unidadMedida.getId()).isEqualTo(1L);
    Assertions.assertThat(unidadMedida.getNombre()).isEqualTo("Seccion 1");
  }
}