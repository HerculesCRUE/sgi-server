package org.crue.hercules.sgi.catservice.integration;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.assertj.core.api.Assertions;
import org.crue.hercules.sgi.catservice.model.Seccion;
import org.crue.hercules.sgi.catservice.model.Servicio;
import org.crue.hercules.sgi.catservice.model.Supervision;
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
 * Test de integracion de Supervision.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SupervisionIT {
  @Autowired
  private TestRestTemplate restTemplate;

  @Sql
  @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:cleanup.sql")
  @Test
  public void getSupervision_ReturnsSupervisionList() throws Exception {
    final ResponseEntity<List<Supervision>> response = restTemplate.exchange(
        ConstantesCat.SUPERVISION_CONTROLLER_BASE_PATH + ConstantesCat.PATH_ALL, HttpMethod.GET, null,
        new ParameterizedTypeReference<List<Supervision>>() {
        });

    Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

    final List<Supervision> supervision = response.getBody();

    Assertions.assertThat(supervision.size()).isEqualTo(2);
    Assertions.assertThat(
        supervision.stream().map(Supervision::getId).collect(Collectors.toList()).containsAll(Arrays.asList(1L, 2L)))
        .isTrue();
    Assertions.assertThat(supervision.stream().map(Supervision::getUsuarioRef).collect(Collectors.toList())
        .containsAll(Arrays.asList("UsuarioRef1", "UsuarioRef2"))).isTrue();
  }

  @Sql
  @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:cleanup.sql")
  @Test
  public void getSupervision_ReturnsSupervisionPage() throws Exception {
    final ResponseEntity<RestResponsePage<Supervision>> response = restTemplate.exchange(
        ConstantesCat.SUPERVISION_CONTROLLER_BASE_PATH + ConstantesCat.PATH_ALL + ConstantesCat.PATH_PAGE,
        HttpMethod.GET, null, new ParameterizedTypeReference<RestResponsePage<Supervision>>() {
        });

    Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

    final List<Supervision> supervision = response.getBody().getContent();

    Assertions.assertThat(supervision.size()).isEqualTo(2);
    Assertions.assertThat(
        supervision.stream().map(Supervision::getId).collect(Collectors.toList()).containsAll(Arrays.asList(1L, 2L)))
        .isTrue();
    Assertions.assertThat(supervision.stream().map(Supervision::getUsuarioRef).collect(Collectors.toList())
        .containsAll(Arrays.asList("UsuarioRef1", "UsuarioRef2"))).isTrue();
  }

  @Sql
  @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:cleanup.sql")
  @Test
  public void getSupervision_ReturnsSupervisionPageFiltered() throws Exception {
    final String url = new StringBuilder(ConstantesCat.SUPERVISION_CONTROLLER_BASE_PATH).append(ConstantesCat.PATH_ALL)
        .append(ConstantesCat.PATH_PAGEFILTERED).append(ConstantesCat.PATH_PARAMETERS_PAGE)
        .append("&usuarioRef={filtroUsuarioRef}").toString();

    final HashMap<String, String> urlVariables = new HashMap<>();
    urlVariables.put("size", "2");
    urlVariables.put("page", "1");
    urlVariables.put("filtroUsuarioRef", "UsuarioRef1");

    final ResponseEntity<RestResponsePage<Supervision>> response = restTemplate.exchange(url, HttpMethod.GET, null,
        new ParameterizedTypeReference<RestResponsePage<Supervision>>() {
        }, urlVariables);

    Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

    final List<Supervision> supervision = response.getBody().getContent();

    Assertions.assertThat(supervision.size()).isEqualTo(1);
    Assertions.assertThat(supervision.stream().map(Supervision::getId).collect(Collectors.toList()).contains(3L))
        .isTrue();
    Assertions
        .assertThat(
            supervision.stream().map(Supervision::getUsuarioRef).collect(Collectors.toList()).contains("UsuarioRef1"))
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
  public void getSupervision_WithId_ReturnsSupervision() throws Exception {
    final ResponseEntity<Supervision> response = restTemplate.getForEntity(
        ConstantesCat.SUPERVISION_CONTROLLER_BASE_PATH + ConstantesCat.PATH_PARAMETER_ID, Supervision.class, 1L);

    Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

    final Supervision supervision = response.getBody();

    Assertions.assertThat(supervision.getId()).isEqualTo(1L);
    Assertions.assertThat(supervision.getUsuarioRef()).isEqualTo("UsuarioRef1");
  }

  @Sql
  @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:cleanup.sql")
  @Test
  public void addSupervision_ReturnsSupervision() throws Exception {

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

    Supervision nuevaSupervision = new Supervision();
    nuevaSupervision.setUsuarioRef("UsuarioRef1");
    nuevaSupervision.setServicio(servicio);

    ResponseEntity<Supervision> response = restTemplate.postForEntity(ConstantesCat.SUPERVISION_CONTROLLER_BASE_PATH,
        nuevaSupervision, Supervision.class);

    Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

    Supervision supervision = response.getBody();

    Assertions.assertThat(supervision.getId()).isNotNull();
    Assertions.assertThat(supervision.getUsuarioRef()).isEqualTo(nuevaSupervision.getUsuarioRef());
    Assertions.assertThat(supervision.getServicio().getId()).isEqualTo(nuevaSupervision.getServicio().getId());
  }

  @Sql
  @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:cleanup.sql")
  @Test
  public void removeSupervision_DoNotGetSupervision() throws Exception {

    restTemplate.delete(ConstantesCat.SUPERVISION_CONTROLLER_BASE_PATH + ConstantesCat.PATH_PARAMETER_ID, 1L);
    ResponseEntity<Supervision> response = restTemplate.getForEntity(
        ConstantesCat.SUPERVISION_CONTROLLER_BASE_PATH + ConstantesCat.PATH_PARAMETER_ID, Supervision.class, 1l);

    Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

  }

  @Sql
  @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:cleanup.sql")
  @Test
  public void replaceSupervision_ReturnsSupervision() throws Exception {

    Seccion seccion = new Seccion(1L, "Seccion 1", "Seccion create test");
    Servicio servicio = new Servicio(1L, "Servicio 1", "Serv1", "Nombre Apellidos", seccion);

    Supervision replaceSupervision = new Supervision();
    replaceSupervision.setUsuarioRef("UsuarioRef2");
    replaceSupervision.setServicio(servicio);

    final HttpEntity<Supervision> requestEntity = new HttpEntity<Supervision>(replaceSupervision, new HttpHeaders());

    final ResponseEntity<Supervision> response = restTemplate.exchange(
        ConstantesCat.SUPERVISION_CONTROLLER_BASE_PATH + ConstantesCat.PATH_PARAMETER_ID, HttpMethod.PUT, requestEntity,
        Supervision.class, 1L);

    Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

    final Supervision supervision = response.getBody();

    Assertions.assertThat(supervision.getId()).isNotNull();
    Assertions.assertThat(supervision.getUsuarioRef()).isEqualTo(replaceSupervision.getUsuarioRef());
    Assertions.assertThat(supervision.getServicio().getId()).isEqualTo(replaceSupervision.getServicio().getId());
  }
}