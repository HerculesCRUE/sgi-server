package org.crue.hercules.sgi.catservice.repository;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.crue.hercules.sgi.catservice.enums.EstadoTipoReservableEnum;
import org.crue.hercules.sgi.catservice.filter.TipoReservableFilter;
import org.crue.hercules.sgi.catservice.model.TipoReservable;
import org.crue.hercules.sgi.catservice.repository.specification.TipoReservableSpecifications;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

/**
 * TipoReservableRepositoryTest
 */
@DataJpaTest
public class TipoReservableRepositoryTest {

  @Autowired
  private TestEntityManager entityManager;

  @Autowired
  private TipoReservableRepository repository;

  @Test
  public void create_ReturnTipoReservable() throws Exception {

    TipoReservable nuevoTipoReservable = generarMockTipoReservable(null, "TipoReservableNuevo");
    entityManager.persistFlushFind(repository.save(nuevoTipoReservable));

    Optional<TipoReservable> tipoReservableCreado = repository.findById(nuevoTipoReservable.getId());

    Assertions.assertThat(tipoReservableCreado).isPresent().containsInstanceOf(TipoReservable.class)
        .hasValueSatisfying(tipoReservable -> {
          Assertions.assertThat(tipoReservable.getId()).isEqualTo(nuevoTipoReservable.getId());
          Assertions.assertThat(tipoReservable.getDescripcion()).isEqualTo(nuevoTipoReservable.getDescripcion());
          Assertions.assertThat(tipoReservable.getDiasAnteMax()).isEqualTo(nuevoTipoReservable.getDiasAnteMax());
          Assertions.assertThat(tipoReservable.getDiasVistaMaxCalen())
              .isEqualTo(nuevoTipoReservable.getDiasVistaMaxCalen());
          Assertions.assertThat(tipoReservable.getDuracionMin()).isEqualTo(nuevoTipoReservable.getDuracionMin());
          Assertions.assertThat(tipoReservable.getEstado()).isEqualTo(nuevoTipoReservable.getEstado());
          Assertions.assertThat(tipoReservable.getHorasAnteAnular())
              .isEqualTo(nuevoTipoReservable.getHorasAnteAnular());
          Assertions.assertThat(tipoReservable.getHorasAnteMin()).isEqualTo(nuevoTipoReservable.getHorasAnteMin());
          Assertions.assertThat(tipoReservable.getReservaMulti()).isEqualTo(nuevoTipoReservable.getReservaMulti());
        });
  }

  @Test
  public void update_ReturnTipoReservable() throws Exception {

    TipoReservable tipoReservableGuardado = entityManager
        .persistFlushFind(generarMockTipoReservable(null, "TipoReservable1"));
    tipoReservableGuardado.setDescripcion("TipoReservableMod");
    repository.save(tipoReservableGuardado);

    Optional<TipoReservable> tipoReservableActualizado = repository.findById(tipoReservableGuardado.getId());

    Assertions.assertThat(tipoReservableActualizado).isPresent().containsInstanceOf(TipoReservable.class)
        .hasValueSatisfying(tipoReservable -> {
          Assertions.assertThat(tipoReservable.getId()).isEqualTo(tipoReservableGuardado.getId());
          Assertions.assertThat(tipoReservable.getDescripcion()).isEqualTo(tipoReservableGuardado.getDescripcion());
          Assertions.assertThat(tipoReservable.getDiasAnteMax()).isEqualTo(tipoReservableGuardado.getDiasAnteMax());
          Assertions.assertThat(tipoReservable.getDiasVistaMaxCalen())
              .isEqualTo(tipoReservableGuardado.getDiasVistaMaxCalen());
          Assertions.assertThat(tipoReservable.getDuracionMin()).isEqualTo(tipoReservableGuardado.getDuracionMin());
          Assertions.assertThat(tipoReservable.getEstado()).isEqualTo(tipoReservableGuardado.getEstado());
          Assertions.assertThat(tipoReservable.getHorasAnteAnular())
              .isEqualTo(tipoReservableGuardado.getHorasAnteAnular());
          Assertions.assertThat(tipoReservable.getHorasAnteMin()).isEqualTo(tipoReservableGuardado.getHorasAnteMin());
          Assertions.assertThat(tipoReservable.getReservaMulti()).isEqualTo(tipoReservableGuardado.getReservaMulti());
        });
  }

  @Test
  public void delete_WithId_DoNotGetTipoReservable() throws Exception {

    List<TipoReservable> tipoReservableList = new LinkedList<TipoReservable>();

    TipoReservable tipoReservableEliminar = entityManager
        .persistFlushFind(generarMockTipoReservable(null, "TipoReservable1"));
    tipoReservableList.add(entityManager.persistFlushFind(generarMockTipoReservable(null, "TipoReservable2")));

    repository.deleteById(tipoReservableEliminar.getId());
    List<TipoReservable> result = repository.findAll();

    Assertions.assertThat(tipoReservableList).isEqualTo(result);
  }

  @Test
  public void findByDescripcion_ReturnsTipoReservable() throws Exception {

    TipoReservable savedTipoReservable = entityManager
        .persistFlushFind(repository.save(generarMockTipoReservable(null, "TipoReservable1")));

    Optional<TipoReservable> tipoReservableBuscado = repository.findByDescripcion("TipoReservable1");

    Assertions.assertThat(tipoReservableBuscado).isPresent().containsInstanceOf(TipoReservable.class)
        .hasValueSatisfying(tipoReservable -> {
          Assertions.assertThat(tipoReservable.getId()).isEqualTo(savedTipoReservable.getId());
          Assertions.assertThat(tipoReservable.getDescripcion()).isEqualTo(savedTipoReservable.getDescripcion());
          Assertions.assertThat(tipoReservable.getDiasAnteMax()).isEqualTo(savedTipoReservable.getDiasAnteMax());
          Assertions.assertThat(tipoReservable.getDiasVistaMaxCalen())
              .isEqualTo(savedTipoReservable.getDiasVistaMaxCalen());
          Assertions.assertThat(tipoReservable.getDuracionMin()).isEqualTo(savedTipoReservable.getDuracionMin());
          Assertions.assertThat(tipoReservable.getEstado()).isEqualTo(savedTipoReservable.getEstado());
          Assertions.assertThat(tipoReservable.getHorasAnteAnular())
              .isEqualTo(savedTipoReservable.getHorasAnteAnular());
          Assertions.assertThat(tipoReservable.getHorasAnteMin()).isEqualTo(savedTipoReservable.getHorasAnteMin());
          Assertions.assertThat(tipoReservable.getReservaMulti()).isEqualTo(savedTipoReservable.getReservaMulti());
        });
  }

  @Test
  public void findByDescripcion_NotFound_ReturnsEmpty() throws Exception {

    entityManager.persistFlushFind(generarMockTipoReservable(null, "TipoReservable1"));

    Optional<TipoReservable> tipoReservable = repository.findByDescripcion("TipoReservable");

    Assertions.assertThat(tipoReservable).isEmpty();

  }

  @Test
  public void findAllFilter_ReturnsTipoReservableList() throws Exception {
    // given: Insertamos un TipoReservable
    TipoReservable tipoReservable = generarMockTipoReservable(null, "TipoReservable1");
    TipoReservable tipoReservableCreado = entityManager.persistFlushFind(tipoReservable);

    TipoReservableFilter filter = new TipoReservableFilter();
    filter.setDescripcion(tipoReservableCreado.getDescripcion());

    // when: Buscamos los tiposReservables por su descripción
    List<TipoReservable> tipoReservables = repository
        .findAll(TipoReservableSpecifications.byTipoReservableFilter(filter));

    // then: comprobamos los TipoReservables
    Assertions.assertThat(tipoReservables.size()).isEqualTo(1);
    Assertions.assertThat(tipoReservables.get(0).getId()).isEqualTo(tipoReservableCreado.getId());
    Assertions.assertThat(tipoReservables.get(0).getDescripcion()).isEqualTo(tipoReservableCreado.getDescripcion());
    Assertions.assertThat(tipoReservables.get(0).getDiasAnteMax()).isEqualTo(tipoReservableCreado.getDiasAnteMax());
    Assertions.assertThat(tipoReservables.get(0).getDiasVistaMaxCalen())
        .isEqualTo(tipoReservableCreado.getDiasVistaMaxCalen());
    Assertions.assertThat(tipoReservables.get(0).getDuracionMin()).isEqualTo(tipoReservableCreado.getDuracionMin());
    Assertions.assertThat(tipoReservables.get(0).getEstado()).isEqualTo(tipoReservableCreado.getEstado());
    Assertions.assertThat(tipoReservables.get(0).getHorasAnteAnular())
        .isEqualTo(tipoReservableCreado.getHorasAnteAnular());
    Assertions.assertThat(tipoReservables.get(0).getHorasAnteMin()).isEqualTo(tipoReservableCreado.getHorasAnteMin());
    Assertions.assertThat(tipoReservables.get(0).getReservaMulti()).isEqualTo(tipoReservableCreado.getReservaMulti());
  }

  @Test
  public void findAllFilter_NotFound_ReturnsEmpty() throws Exception {

    TipoReservableFilter filter = new TipoReservableFilter();
    filter.setDescripcion("TipoReservableFalso");

    // when: Buscamos los tiposReservables por su descripción
    List<TipoReservable> tipoReservables = repository
        .findAll(TipoReservableSpecifications.byTipoReservableFilter(filter));

    // then: comprobamos los TipoReservables
    Assertions.assertThat(tipoReservables).isEmpty();
    Assertions.assertThat(tipoReservables.size()).isEqualTo(0);

  }

  @Test
  public void findAllPageFilter_ReturnsTipoReservablePage() throws Exception {
    // given: Insertamos un TipoReservable
    TipoReservable tipoReservable = generarMockTipoReservable(null, "TipoReservable1");
    TipoReservable tipoReservableCreado = entityManager.persistFlushFind(tipoReservable);

    TipoReservableFilter filter = new TipoReservableFilter();
    filter.setReservaMulti(false);

    // when: Buscamos los tiposReservables con reserva_multi = false
    Page<TipoReservable> tipoReservablesPage = repository
        .findAll(TipoReservableSpecifications.byTipoReservableFilter(filter), PageRequest.of(0, 5));

    // then: comprobamos los TipoReservables
    Assertions.assertThat(tipoReservablesPage.getContent().size()).isEqualTo(1);
    Assertions.assertThat(tipoReservablesPage.getContent().get(0).getId()).isEqualTo(tipoReservableCreado.getId());
    Assertions.assertThat(tipoReservablesPage.getContent().get(0).getDescripcion())
        .isEqualTo(tipoReservableCreado.getDescripcion());
    Assertions.assertThat(tipoReservablesPage.getContent().get(0).getDiasAnteMax())
        .isEqualTo(tipoReservableCreado.getDiasAnteMax());
    Assertions.assertThat(tipoReservablesPage.getContent().get(0).getDiasVistaMaxCalen())
        .isEqualTo(tipoReservableCreado.getDiasVistaMaxCalen());
    Assertions.assertThat(tipoReservablesPage.getContent().get(0).getDuracionMin())
        .isEqualTo(tipoReservableCreado.getDuracionMin());
    Assertions.assertThat(tipoReservablesPage.getContent().get(0).getEstado())
        .isEqualTo(tipoReservableCreado.getEstado());
    Assertions.assertThat(tipoReservablesPage.getContent().get(0).getHorasAnteAnular())
        .isEqualTo(tipoReservableCreado.getHorasAnteAnular());
    Assertions.assertThat(tipoReservablesPage.getContent().get(0).getHorasAnteMin())
        .isEqualTo(tipoReservableCreado.getHorasAnteMin());
    Assertions.assertThat(tipoReservablesPage.getContent().get(0).getReservaMulti())
        .isEqualTo(tipoReservableCreado.getReservaMulti());
  }

  /**
   * Función que devuelve un objeto TipoReservable
   * 
   * @param id          id del tipoReservable
   * @param descripcion la descripción del tipo de reservable
   * @return el objeto tipo reservable
   */

  public TipoReservable generarMockTipoReservable(Long id, String descripcion) {
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

    return tipoReservable;
  }
}