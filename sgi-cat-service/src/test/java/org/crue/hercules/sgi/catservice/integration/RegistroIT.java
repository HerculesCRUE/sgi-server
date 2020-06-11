package org.crue.hercules.sgi.catservice.integration;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.assertj.core.api.Assertions;
import org.crue.hercules.sgi.catservice.model.Seccion;
import org.crue.hercules.sgi.catservice.model.Servicio;
import org.crue.hercules.sgi.catservice.enums.EstadoRegistroEnum;
import org.crue.hercules.sgi.catservice.model.Registro;
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
 * Test de integracion de Registro.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RegistroIT {

  @Autowired
  private TestRestTemplate restTemplate;

  @Sql
  @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:cleanup.sql")
  @Test
  public void getRegistros_ReturnsRegistroList() throws Exception {
    final ResponseEntity<List<Registro>> response = restTemplate.exchange(
        ConstantesCat.REGISTRO_CONTROLLER_BASE_PATH + ConstantesCat.PATH_ALL, HttpMethod.GET, null,
        new ParameterizedTypeReference<List<Registro>>() {
        });

    Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

    final List<Registro> registros = response.getBody();

    Assertions.assertThat(registros.size()).isEqualTo(2);
    Assertions
        .assertThat(
            registros.stream().map(Registro::getId).collect(Collectors.toList()).containsAll(Arrays.asList(1L, 2L)))
        .isTrue();
    Assertions.assertThat(registros.stream().map(Registro::getUsuarioRef).collect(Collectors.toList())
        .containsAll(Arrays.asList("user-998", "user-558"))).isTrue();
  }

  @Sql
  @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:cleanup.sql")
  @Test
  public void getRegistros_ReturnsRegistroPage() throws Exception {
    final ResponseEntity<RestResponsePage<Registro>> response = restTemplate.exchange(
        ConstantesCat.REGISTRO_CONTROLLER_BASE_PATH + ConstantesCat.PATH_ALL + ConstantesCat.PATH_PAGE, HttpMethod.GET,
        null, new ParameterizedTypeReference<RestResponsePage<Registro>>() {
        });

    Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

    final List<Registro> registros = response.getBody().getContent();

    Assertions.assertThat(registros.size()).isEqualTo(2);
    Assertions
        .assertThat(
            registros.stream().map(Registro::getId).collect(Collectors.toList()).containsAll(Arrays.asList(1L, 2L)))
        .isTrue();
    Assertions.assertThat(registros.stream().map(Registro::getUsuarioRef).collect(Collectors.toList())
        .containsAll(Arrays.asList("user-998", "user-558"))).isTrue();
  }

  @Sql
  @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:cleanup.sql")
  @Test
  public void getRegistros_ReturnsRegistroPageFiltered() throws Exception {
    final ResponseEntity<RestResponsePage<Registro>> response = restTemplate.exchange(
        ConstantesCat.REGISTRO_CONTROLLER_BASE_PATH + ConstantesCat.PATH_ALL + ConstantesCat.PATH_PAGEFILTERED,
        HttpMethod.GET, null, new ParameterizedTypeReference<RestResponsePage<Registro>>() {
        });

    Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

    final List<Registro> registros = response.getBody().getContent();

    Assertions.assertThat(registros.size()).isEqualTo(3);
    Assertions
        .assertThat(
            registros.stream().map(Registro::getId).collect(Collectors.toList()).containsAll(Arrays.asList(1L, 2L)))
        .isTrue();
    Assertions.assertThat(registros.stream().map(Registro::getUsuarioRef).collect(Collectors.toList())
        .containsAll(Arrays.asList("user-998", "user-558", "user-996"))).isTrue();
  }

  @Sql
  @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:cleanup.sql")
  @Test
  public void addRegistro_ReturnsRegistro() throws Exception {
    Seccion seccion = new Seccion();
    seccion.setId(1L);
    seccion.setNombre("Seccion 1");
    seccion.setDescripcion("Seccion");

    Servicio servicio = new Servicio();
    servicio.setId(1L);
    servicio.setNombre("Servicio 1");
    servicio.setAbreviatura("Serv1");
    servicio.setContacto("Nombre Apellido");
    servicio.setSeccion(seccion);

    Registro nuevoRegistro = new Registro(null, "user-998", servicio, EstadoRegistroEnum.ACTIVO, Boolean.TRUE,
        Boolean.TRUE, "registro 1");

    ResponseEntity<Registro> response = restTemplate.postForEntity(ConstantesCat.REGISTRO_CONTROLLER_BASE_PATH,
        nuevoRegistro, Registro.class);
    Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

    Registro registro = response.getBody();

    Assertions.assertThat(registro.getId()).isNotNull();
    Assertions.assertThat(registro.getUsuarioRef()).isEqualTo(nuevoRegistro.getUsuarioRef());
    Assertions.assertThat(registro.getEstado()).isEqualTo(nuevoRegistro.getEstado());
    Assertions.assertThat(registro.getEntregaPapel()).isEqualTo(nuevoRegistro.getEntregaPapel());
    Assertions.assertThat(registro.getAceptaCondiciones()).isEqualTo(nuevoRegistro.getAceptaCondiciones());
    Assertions.assertThat(registro.getObservaciones()).isEqualTo(nuevoRegistro.getObservaciones());
    Assertions.assertThat(registro.getServicio().getId()).isEqualTo(nuevoRegistro.getServicio().getId());
  }

  @Sql
  @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:cleanup.sql")
  @Test
  public void replaceRegistro_ReturnsRegistro() throws Exception {
    Seccion seccion = new Seccion();
    seccion.setId(1L);
    seccion.setNombre("Seccion 1");
    seccion.setDescripcion("Seccion");

    Servicio servicio = new Servicio();
    servicio.setId(1L);
    servicio.setNombre("Servicio 1");
    servicio.setAbreviatura("Serv1");
    servicio.setContacto("Nombre Apellido");
    servicio.setSeccion(seccion);

    Registro replaceRegistro = new Registro(1L, "user-558", servicio, EstadoRegistroEnum.ACTIVO, Boolean.TRUE,
        Boolean.TRUE, "registro 1");

    final HttpEntity<Registro> requestEntity = new HttpEntity<Registro>(replaceRegistro, new HttpHeaders());
    final ResponseEntity<Registro> response = restTemplate.exchange(
        ConstantesCat.REGISTRO_CONTROLLER_BASE_PATH + ConstantesCat.PATH_PARAMETER_ID, HttpMethod.PUT, requestEntity,
        Registro.class, 1L);

    Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

    final Registro registro = response.getBody();

    Assertions.assertThat(servicio.getId()).isNotNull();
    Assertions.assertThat(registro.getUsuarioRef()).isEqualTo(replaceRegistro.getUsuarioRef());
  }

  @Sql
  @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:cleanup.sql")
  @Test
  public void removeRegistro_DoNotGetRegistro() throws Exception {
    restTemplate.delete(ConstantesCat.REGISTRO_CONTROLLER_BASE_PATH + ConstantesCat.PATH_PARAMETER_ID, 1L);

    final ResponseEntity<Registro> response = restTemplate.getForEntity(
        ConstantesCat.REGISTRO_CONTROLLER_BASE_PATH + ConstantesCat.PATH_PARAMETER_ID, Registro.class, 1L);

    Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
  }

  @Sql
  @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:cleanup.sql")
  @Test
  public void getRegistro_WithId_ReturnsRegistro() throws Exception {
    final ResponseEntity<Registro> response = restTemplate.getForEntity(
        ConstantesCat.REGISTRO_CONTROLLER_BASE_PATH + ConstantesCat.PATH_PARAMETER_ID, Registro.class, 1L);

    Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

    final Registro registro = response.getBody();

    Assertions.assertThat(registro.getId()).isEqualTo(1L);
    Assertions.assertThat(registro.getUsuarioRef()).isEqualTo("user-998");
  }
}