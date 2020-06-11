package org.crue.hercules.sgi.catservice.integration;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.assertj.core.api.Assertions;
import org.crue.hercules.sgi.catservice.model.Seccion;
import org.crue.hercules.sgi.catservice.model.Servicio;
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
 * Test de integracion de Servicio.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ServicioIT {

  @Autowired
  private TestRestTemplate restTemplate;

  @Sql
  @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:cleanup.sql")
  @Test
  public void getServicios_ReturnsServicioList() throws Exception {
    final ResponseEntity<List<Servicio>> response = restTemplate.exchange(
        ConstantesCat.SERVICIO_CONTROLLER_BASE_PATH + ConstantesCat.PATH_ALL, HttpMethod.GET, null,
        new ParameterizedTypeReference<List<Servicio>>() {
        });

    Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

    final List<Servicio> secciones = response.getBody();

    Assertions.assertThat(secciones.size()).isEqualTo(2);
    Assertions
        .assertThat(
            secciones.stream().map(Servicio::getId).collect(Collectors.toList()).containsAll(Arrays.asList(1L, 2L)))
        .isTrue();
    Assertions.assertThat(secciones.stream().map(Servicio::getNombre).collect(Collectors.toList())
        .containsAll(Arrays.asList("Servicio 1", "Servicio 2"))).isTrue();
  }

  @Sql
  @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:cleanup.sql")
  @Test
  public void getServicios_ReturnsServicioPage() throws Exception {
    final ResponseEntity<RestResponsePage<Servicio>> response = restTemplate.exchange(
        ConstantesCat.SERVICIO_CONTROLLER_BASE_PATH + ConstantesCat.PATH_ALL + ConstantesCat.PATH_PAGE, HttpMethod.GET,
        null, new ParameterizedTypeReference<RestResponsePage<Servicio>>() {
        });

    Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

    final List<Servicio> secciones = response.getBody().getContent();

    Assertions.assertThat(secciones.size()).isEqualTo(2);
    Assertions
        .assertThat(
            secciones.stream().map(Servicio::getId).collect(Collectors.toList()).containsAll(Arrays.asList(1L, 2L)))
        .isTrue();
    Assertions.assertThat(secciones.stream().map(Servicio::getNombre).collect(Collectors.toList())
        .containsAll(Arrays.asList("Servicio 1", "Servicio 2"))).isTrue();
  }

  @Sql
  @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:cleanup.sql")
  @Test
  public void getServicios_ReturnsServicioPageFiltered() throws Exception {
    final ResponseEntity<RestResponsePage<Servicio>> response = restTemplate.exchange(
        ConstantesCat.SERVICIO_CONTROLLER_BASE_PATH + ConstantesCat.PATH_ALL + ConstantesCat.PATH_PAGEFILTERED,
        HttpMethod.GET, null, new ParameterizedTypeReference<RestResponsePage<Servicio>>() {
        });

    Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

    final List<Servicio> secciones = response.getBody().getContent();

    Assertions.assertThat(secciones.size()).isEqualTo(3);
    Assertions
        .assertThat(
            secciones.stream().map(Servicio::getId).collect(Collectors.toList()).containsAll(Arrays.asList(1L, 2L)))
        .isTrue();
    Assertions.assertThat(secciones.stream().map(Servicio::getNombre).collect(Collectors.toList())
        .containsAll(Arrays.asList("Servicio 1", "Servicio 2", "Servicio 3"))).isTrue();
  }

  @Sql
  @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:cleanup.sql")
  @Test
  public void addServicio_ReturnsServicio() throws Exception {
    Seccion seccion = new Seccion();
    seccion.setId(1L);
    seccion.setNombre("Seccion 1");
    seccion.setDescripcion("Seccion");

    Servicio nuevaServicio = new Servicio();
    nuevaServicio.setNombre("Servicio 2");
    nuevaServicio.setAbreviatura("Serv2");
    nuevaServicio.setContacto("Nombre Apellido");
    nuevaServicio.setSeccion(seccion);

    ResponseEntity<Servicio> response = restTemplate.postForEntity(ConstantesCat.SERVICIO_CONTROLLER_BASE_PATH,
        nuevaServicio, Servicio.class);
    Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

    Servicio servicio = response.getBody();

    Assertions.assertThat(servicio.getId()).isNotNull();
    Assertions.assertThat(servicio.getNombre()).isEqualTo(nuevaServicio.getNombre());
    Assertions.assertThat(servicio.getAbreviatura()).isEqualTo(nuevaServicio.getAbreviatura());
    Assertions.assertThat(servicio.getContacto()).isEqualTo(nuevaServicio.getContacto());
    Assertions.assertThat(servicio.getSeccion().getId()).isEqualTo(nuevaServicio.getSeccion().getId());
  }

  @Sql
  @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:cleanup.sql")
  @Test
  public void replaceServicio_ReturnsServicio() throws Exception {
    Seccion seccion = new Seccion();
    seccion.setId(1L);
    seccion.setNombre("Seccion 1");
    seccion.setDescripcion("Seccion");

    Servicio replaceServicio = new Servicio();
    replaceServicio.setNombre("Replace Servicio 1");
    replaceServicio.setAbreviatura("Serv1");
    replaceServicio.setContacto("Nombre Apellido");
    replaceServicio.setSeccion(seccion);

    final HttpEntity<Servicio> requestEntity = new HttpEntity<Servicio>(replaceServicio, new HttpHeaders());
    final ResponseEntity<Servicio> response = restTemplate.exchange(
        ConstantesCat.SERVICIO_CONTROLLER_BASE_PATH + ConstantesCat.PATH_PARAMETER_ID, HttpMethod.PUT, requestEntity,
        Servicio.class, 1L);

    Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

    final Servicio servicio = response.getBody();

    Assertions.assertThat(servicio.getId()).isNotNull();
    Assertions.assertThat(servicio.getNombre()).isEqualTo(replaceServicio.getNombre());
  }

  @Sql
  @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:cleanup.sql")
  @Test
  public void removeServicio_DoNotGetServicio() throws Exception {
    restTemplate.delete(ConstantesCat.SERVICIO_CONTROLLER_BASE_PATH + ConstantesCat.PATH_PARAMETER_ID, 1L);

    final ResponseEntity<Servicio> response = restTemplate.getForEntity(
        ConstantesCat.SERVICIO_CONTROLLER_BASE_PATH + ConstantesCat.PATH_PARAMETER_ID, Servicio.class, 1L);

    Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
  }

  @Sql
  @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:cleanup.sql")
  @Test
  public void getServicio_WithId_ReturnsServicio() throws Exception {
    final ResponseEntity<Servicio> response = restTemplate.getForEntity(
        ConstantesCat.SERVICIO_CONTROLLER_BASE_PATH + ConstantesCat.PATH_PARAMETER_ID, Servicio.class, 1L);

    Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

    final Servicio unidadMedida = response.getBody();

    Assertions.assertThat(unidadMedida.getId()).isEqualTo(1L);
    Assertions.assertThat(unidadMedida.getNombre()).isEqualTo("Servicio 1");
  }
}