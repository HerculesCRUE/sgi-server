package org.crue.hercules.sgi.catservice.service.impl;

import java.util.List;

import org.crue.hercules.sgi.catservice.exceptions.TipoReservableNotFoundException;
import org.crue.hercules.sgi.catservice.filter.TipoReservableFilter;
import org.crue.hercules.sgi.catservice.model.TipoReservable;
import org.crue.hercules.sgi.catservice.repository.TipoReservableRepository;
import org.crue.hercules.sgi.catservice.repository.specification.TipoReservableSpecifications;
import org.crue.hercules.sgi.catservice.service.TipoReservableService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import lombok.extern.slf4j.Slf4j;

/**
 * Service Implementation para gestion {@link TipoReservable}.
 */
@Service
@Slf4j
@Transactional
public class TipoReservableServiceImpl implements TipoReservableService {

  private final TipoReservableRepository tipoReservableRepository;

  public TipoReservableServiceImpl(TipoReservableRepository tipoReservableRepository) {
    this.tipoReservableRepository = tipoReservableRepository;
  }

  /**
   * Guarda la entidad TipoReservable.
   *
   * @param tipoReservable la entidad a guardar.
   * @return la entidad persistida.
   */
  @Override
  public TipoReservable create(TipoReservable tipoReservable) {
    log.debug("create(TipoReservable tipoReservable) - start", tipoReservable);
    Assert.isNull(tipoReservable.getId(), "TipoReservable id tiene que ser null para crear un nuevo tipoReservable");

    return tipoReservableRepository.save(tipoReservable);
  }

  /**
   * Obtiene todas las entidades TipoReservable.
   *
   * @param pageable la información de paginación.
   * @return el listado de entidades.
   */
  @Override
  @Transactional(readOnly = true)
  public Page<TipoReservable> findAll(Pageable pageable) {
    log.debug("TipoReservable findAll(Pageable pageable) - start");
    return tipoReservableRepository.findAll(pageable);
  }

  /**
   * Obtiene todas las entidades TipoReservable.
   *
   * 
   * @return el listado de entidades.
   */
  @Override
  @Transactional(readOnly = true)
  public List<TipoReservable> findAll() {
    log.debug("TipoReservable findAll() - start");
    return tipoReservableRepository.findAll();
  }

  /**
   * Optiene una entidad TipoReservable por id.
   *
   * @param id el id de la entidad.
   * @return la entidad.
   * @throws TipoReservableNotFoundException Si no existe ninguna unidad medida
   *                                         con ese id.
   */
  @Override
  @Transactional(readOnly = true)
  public TipoReservable findById(final Long id) throws TipoReservableNotFoundException {
    log.debug("TipoReservable findById(final Long id) - start", id);
    final TipoReservable tipoReservable = tipoReservableRepository.findById(id)
        .orElseThrow(() -> new TipoReservableNotFoundException(id));
    return tipoReservable;
  }

  /**
   * Elimina una entidad TipoReservable por id.
   *
   * @param id el id de la entidad.
   */
  @Override
  public void delete(Long id) {
    log.debug("TipoReservable delete(Long id) - start", id);
    tipoReservableRepository.deleteById(id);
  }

  /**
   * Actualiza los datos de tipoReservable.
   * 
   * @param tipoReservableActualizar TipoReservable con los datos actualizados.
   * @return La unidad medida actualizada.
   * @throws TipoReservableNotFoundException Si no existe ninguna unidad medida
   *                                         con ese id.
   * @throws IllegalArgumentException        Si la unidad medida no tiene id.
   */
  public TipoReservable update(final TipoReservable tipoReservableActualizar) {
    log.debug("update(TipoReservable tipoReservableActualizar) - start");

    Assert.notNull(tipoReservableActualizar.getId(),
        "TipoReservable id no puede ser null para actualizar un tipoReservable");

    return tipoReservableRepository.findById(tipoReservableActualizar.getId()).map(tipoReservable -> {
      tipoReservable.setDescripcion(tipoReservableActualizar.getDescripcion());
      tipoReservable.setDiasAnteMax(tipoReservableActualizar.getDiasAnteMax());
      tipoReservable.setDiasVistaMaxCalen(tipoReservableActualizar.getDiasVistaMaxCalen());
      tipoReservable.setDuracionMin(tipoReservableActualizar.getDuracionMin());
      tipoReservable.setEstado(tipoReservableActualizar.getEstado());
      tipoReservable.setHorasAnteMin(tipoReservableActualizar.getHorasAnteMin());
      tipoReservable.setHorasAnteAnular(tipoReservableActualizar.getHorasAnteAnular());
      tipoReservable.setReservaMulti(tipoReservableActualizar.getReservaMulti());
      tipoReservable.setServicio(tipoReservableActualizar.getServicio());

      TipoReservable returnValue = tipoReservableRepository.save(tipoReservable);
      log.debug("update(TipoReservable tipoReservableActualizar) - end");
      return returnValue;
    }).orElseThrow(() -> new TipoReservableNotFoundException(tipoReservableActualizar.getId()));
  }

  /**
   * Devuelve todas los TipoReservable que cumplan con los datos del filtro
   * 
   * @param filter   filtro.
   * @param pageable informacion de la pagina.
   * @return lista de TipoReservable
   */
  @Override
  @Transactional(readOnly = true)
  public Page<TipoReservable> findAllLike(TipoReservableFilter filter, Pageable pageable) {
    log.debug("findAllLike(TipoReservableFilter filter, Pageable pageable) - start");
    final Page<TipoReservable> listTipoReservable = tipoReservableRepository
        .findAll(TipoReservableSpecifications.byTipoReservableFilter(filter), pageable);
    return listTipoReservable;
  }

}
