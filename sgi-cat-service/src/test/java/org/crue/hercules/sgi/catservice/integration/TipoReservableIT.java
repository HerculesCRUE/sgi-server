package org.crue.hercules.sgi.catservice.integration;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.assertj.core.api.Assertions;
import org.crue.hercules.sgi.catservice.enums.EstadoTipoReservableEnum;
import org.crue.hercules.sgi.catservice.model.Seccion;
import org.crue.hercules.sgi.catservice.model.Servicio;
import org.crue.hercules.sgi.catservice.model.TipoReservable;
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
 * Test de integracion de TipoReservable.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TipoReservableIT {

  @Autowired
  private TestRestTemplate restTemplate;

  @Sql
  @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:cleanup.sql")
  @Test
  public void getTipoReservables_ReturnsTipoReservableList() throws Exception {
    final ResponseEntity<List<TipoReservable>> response = restTemplate.exchange(
        ConstantesCat.TIPORESERVABLE_CONTROLLER_BASE_PATH + ConstantesCat.PATH_ALL, HttpMethod.GET, null,
        new ParameterizedTypeReference<List<TipoReservable>>() {
        });

    Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

    final List<TipoReservable> tipoReservables = response.getBody();

    Assertions.assertThat(tipoReservables.size()).isEqualTo(2);
    Assertions.assertThat(tipoReservables.stream().map(TipoReservable::getId).collect(Collectors.toList())
        .containsAll(Arrays.asList(1L, 2L))).isTrue();
    Assertions.assertThat(tipoReservables.stream().map(TipoReservable::getDescripcion).collect(Collectors.toList())
        .containsAll(Arrays.asList("TipoReservable1", "TipoReservable2"))).isTrue();
  }

  @Sql
  @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:cleanup.sql")
  @Test
  public void getTipoReservables_ReturnsTipoReservablePage() throws Exception {
    final ResponseEntity<RestResponsePage<TipoReservable>> response = restTemplate.exchange(
        ConstantesCat.TIPORESERVABLE_CONTROLLER_BASE_PATH + ConstantesCat.PATH_ALL + ConstantesCat.PATH_PAGE,
        HttpMethod.GET, null, new ParameterizedTypeReference<RestResponsePage<TipoReservable>>() {
        });

    Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

    final List<TipoReservable> tipoReservables = response.getBody().getContent();

    Assertions.assertThat(tipoReservables.size()).isEqualTo(2);
    Assertions.assertThat(tipoReservables.stream().map(TipoReservable::getId).collect(Collectors.toList())
        .containsAll(Arrays.asList(1L, 2L))).isTrue();
    Assertions.assertThat(tipoReservables.stream().map(TipoReservable::getDescripcion).collect(Collectors.toList())
        .containsAll(Arrays.asList("TipoReservable1", "TipoReservable2"))).isTrue();
  }

  @Sql
  @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:cleanup.sql")
  @Test
  public void getTipoReservables_ReturnsTipoReservablePageFiltered() throws Exception {
    final ResponseEntity<RestResponsePage<TipoReservable>> response = restTemplate.exchange(
        ConstantesCat.TIPORESERVABLE_CONTROLLER_BASE_PATH + ConstantesCat.PATH_ALL + ConstantesCat.PATH_PAGEFILTERED,
        HttpMethod.GET, null, new ParameterizedTypeReference<RestResponsePage<TipoReservable>>() {
        });

    Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

    final List<TipoReservable> tipoReservables = response.getBody().getContent();

    Assertions.assertThat(tipoReservables.size()).isEqualTo(2);
    Assertions.assertThat(tipoReservables.stream().map(TipoReservable::getId).collect(Collectors.toList())
        .containsAll(Arrays.asList(1L, 2L))).isTrue();
    Assertions.assertThat(tipoReservables.stream().map(TipoReservable::getDescripcion).collect(Collectors.toList())
        .containsAll(Arrays.asList("TipoReservable1", "TipoReservable2"))).isTrue();
  }

  @Sql
  @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:cleanup.sql")
  @Test
  public void getTipoReservable_WithId_ReturnsTipoReservable() throws Exception {
    final ResponseEntity<TipoReservable> response = restTemplate.getForEntity(
        ConstantesCat.TIPORESERVABLE_CONTROLLER_BASE_PATH + ConstantesCat.PATH_PARAMETER_ID, TipoReservable.class, 1L);

    Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

    final TipoReservable tipoReservable = response.getBody();

    Assertions.assertThat(tipoReservable.getId()).isEqualTo(1L);
    Assertions.assertThat(tipoReservable.getDescripcion()).isEqualTo("TipoReservable1");
  }

  @Sql
  @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:cleanup.sql")
  @Test
  public void replaceTipoReservable_ReturnsTipoReservable() throws Exception {
    final ResponseEntity<TipoReservable> responseBeforeReplace = restTemplate.getForEntity(
        ConstantesCat.TIPORESERVABLE_CONTROLLER_BASE_PATH + ConstantesCat.PATH_PARAMETER_ID, TipoReservable.class, 1L);
    Assertions.assertThat(responseBeforeReplace.getStatusCode()).isEqualTo(HttpStatus.OK);
    final TipoReservable tipoReservableBeforeReplace = responseBeforeReplace.getBody();
    // TipoReservable que queremos modificar
    final TipoReservable replaceTipoReservable = generarMockTipoReservable(1L, "TipoReservable123");

    final HttpEntity<TipoReservable> requestEntity = new HttpEntity<TipoReservable>(replaceTipoReservable,
        new HttpHeaders());
    final ResponseEntity<TipoReservable> responseAfterReplace = restTemplate.exchange(
        ConstantesCat.TIPORESERVABLE_CONTROLLER_BASE_PATH + ConstantesCat.PATH_PARAMETER_ID, HttpMethod.PUT,
        requestEntity, TipoReservable.class, 1L);
    Assertions.assertThat(responseAfterReplace.getStatusCode()).isEqualTo(HttpStatus.OK);
    // TipoReservable modificado
    final TipoReservable tipoReservableAfterReplace = responseAfterReplace.getBody();

    Assertions.assertThat(tipoReservableAfterReplace.getId()).isNotNull();
    // Se comprueba que la descripción del TipoReservable ha cambiado
    Assertions.assertThat(tipoReservableAfterReplace.getDescripcion())
        .isNotEqualTo(tipoReservableBeforeReplace.getDescripcion());
  }

  @Sql
  @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:cleanup.sql")
  @Test
  public void removeTipoReservable_DoNotGetTipoReservable() throws Exception {
    restTemplate.delete(ConstantesCat.TIPORESERVABLE_CONTROLLER_BASE_PATH + ConstantesCat.PATH_PARAMETER_ID, 1L);

    ResponseEntity<TipoReservable> response = restTemplate.getForEntity(
        ConstantesCat.TIPORESERVABLE_CONTROLLER_BASE_PATH + ConstantesCat.PATH_PARAMETER_ID, TipoReservable.class, 1L);

    Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
  }

  /**
   * Función que devuelve un objeto TipoReservable
   * 
   * @param id          id del tipoReservable
   * @param descripcion la descripción del tipo de reservable
   * @return el objeto tipo reservable
   */

  public TipoReservable generarMockTipoReservable(Long id, String descripcion) {
    Seccion seccion = new Seccion();
    seccion.setId(id);
    seccion.setDescripcion("Seccion" + id);
    seccion.setNombre("Secc" + id);

    Servicio servicio = new Servicio();
    servicio.setId(id);
    servicio.setAbreviatura("SV" + id);
    servicio.setNombre("Servicio" + id);
    servicio.setContacto("Contacto" + id);
    servicio.setSeccion(seccion);

    TipoReservable tipoReservable = new TipoReservable();
    tipoReservable.setId(id);
    tipoReservable.setDescripcion(descripcion);
    tipoReservable.setDiasAnteMax(2);
    tipoReservable.setDuracionMin(2);
    tipoReservable.setEstado(EstadoTipoReservableEnum.ALTA);
    tipoReservable.setHorasAnteMin(3);
    tipoReservable.setHorasAnteAnular(4);
    tipoReservable.setDiasVistaMaxCalen(3);
    tipoReservable.setReservaMulti(false);
    tipoReservable.setServicio(servicio);

    return tipoReservable;
  }
}