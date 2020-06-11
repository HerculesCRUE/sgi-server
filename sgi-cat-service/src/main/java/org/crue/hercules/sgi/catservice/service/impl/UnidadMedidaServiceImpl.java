package org.crue.hercules.sgi.catservice.service.impl;

import java.util.List;

import org.crue.hercules.sgi.catservice.exceptions.UnidadMedidaNotFoundException;
import org.crue.hercules.sgi.catservice.filter.UnidadMedidaFilter;
import org.crue.hercules.sgi.catservice.model.UnidadMedida;
import org.crue.hercules.sgi.catservice.repository.UnidadMedidaRepository;
import org.crue.hercules.sgi.catservice.repository.specification.UnidadMedidaSpecifications;
import org.crue.hercules.sgi.catservice.service.UnidadMedidaService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import lombok.extern.slf4j.Slf4j;

/**
 * Service Implementation para gestion {@link UnidadMedida}.
 */
@Service
@Slf4j
@Transactional
public class UnidadMedidaServiceImpl implements UnidadMedidaService {

  private final UnidadMedidaRepository unidadMedidaRepository;

  public UnidadMedidaServiceImpl(UnidadMedidaRepository unidadMedidaRepository) {
    this.unidadMedidaRepository = unidadMedidaRepository;
  }

  /**
   * Guarda UnidadMedida.
   *
   * @param unidadmedida la entidad a guardar.
   * @return la entidad persistida.
   * @throws IllegalArgumentException Si la unidad medida tiene id.
   */
  @Override
  public UnidadMedida create(UnidadMedida unidadmedida) {
    log.debug("create(UnidadMedida unidadmedida) - start");
    Assert.isNull(unidadmedida.getId(), "UnidadMedida id tiene que ser null para crear una nueva unidadMedida");
    log.debug("create(UnidadMedida unidadmedida) - end");
    return unidadMedidaRepository.save(unidadmedida);
  }

  /**
   * Actualiza UnidadMedida.
   * 
   * @param unidadMedidaActualizar la entidad a actualizar.
   * @return la entidad persistida.
   * @throws UnidadMedidaNotFoundException Si no existe ninguna unidad medida con
   *                                       ese id.
   * @throws IllegalArgumentException      Si la unidad medida no tiene id.
   */
  public UnidadMedida update(final UnidadMedida unidadMedidaActualizar) {
    log.debug("update(UnidadMedida unidadMedidaActualizar) - start");

    Assert.notNull(unidadMedidaActualizar.getId(),
        "UnidadMedida id no puede ser null para actualizar una unidadMedida");

    return unidadMedidaRepository.findById(unidadMedidaActualizar.getId()).map(unidadMedida -> {
      unidadMedida.setAbreviatura(unidadMedidaActualizar.getAbreviatura());
      unidadMedida.setDescripcion(unidadMedidaActualizar.getDescripcion());

      UnidadMedida returnValue = unidadMedidaRepository.save(unidadMedida);
      log.debug("update(UnidadMedida unidadMedidaActualizar) - end");
      return returnValue;
    }).orElseThrow(() -> new UnidadMedidaNotFoundException(unidadMedidaActualizar.getId()));
  }

  /**
   * Elimina UnidadMedida por id.
   *
   * @param id el id de la entidad.
   * @throws UnidadMedidaNotFoundException Si no existe ninguna unidad medida con
   *                                       ese id.
   * @throws IllegalArgumentException      Si no hay id.
   */
  @Override
  public void delete(Long id) {
    log.debug("delete(Long id) - start");
    unidadMedidaRepository.deleteById(id);
    log.debug("delete(Long id) - end");
  }

  /**
   * Obtiene todas las entidades UnidadMedida.
   *
   * 
   * @return el listado de entidades.
   */
  @Override
  @Transactional(readOnly = true)
  public List<UnidadMedida> findAll() {
    log.debug("findAll() - start");
    final List<UnidadMedida> listUnidadMedida = unidadMedidaRepository.findAll();
    log.debug("findAll() - end");
    return listUnidadMedida;
  }

  /**
   * Obtiene todas las entidades UnidadMedida paginadas.
   *
   * @param pageable la información de paginación.
   * @return la lista paginada de entidades.
   */
  @Override
  @Transactional(readOnly = true)
  public Page<UnidadMedida> findAll(Pageable pageable) {
    log.debug("findAll(Pageable pageable) - start");
    final Page<UnidadMedida> listUnidadMedida = unidadMedidaRepository.findAll(pageable);
    log.debug("findAll(Pageable pageable) - end");
    return listUnidadMedida;
  }

  /**
   * Obtiene todas las entidades UnidadMedida filtradas y paginadas.
   * 
   * @param filter   la información del filtro.
   * @param pageable la información de la paginación.
   * @return la lista filtrada y paginada de entidades.
   */
  @Override
  public Page<UnidadMedida> findAllLike(UnidadMedidaFilter filter, Pageable pageable) {
    log.debug("findAllLike(UnidadMedidaFilter filter, Pageable pageable) - start");
    final Page<UnidadMedida> listUnidadMedida = unidadMedidaRepository
        .findAll(UnidadMedidaSpecifications.byUnidadMedidaFilter(filter), pageable);
    log.debug("findAllLike(UnidadMedidaFilter filter, Pageable pageable) - end");
    return listUnidadMedida;
  }

  /**
   * Obtiene UnidadMedida por id.
   *
   * @param id el id de la entidad.
   * @return la entidad.
   * @throws UnidadMedidaNotFoundException Si no existe ninguna unidad medida con
   *                                       ese id.
   */
  @Override
  @Transactional(readOnly = true)
  public UnidadMedida findById(final Long id) throws UnidadMedidaNotFoundException {
    log.debug("findById(final Long id) - start");
    final UnidadMedida unidadMedida = unidadMedidaRepository.findById(id)
        .orElseThrow(() -> new UnidadMedidaNotFoundException(id));
    log.debug("findById(final Long id) - end");
    return unidadMedida;
  }

  /**
   * Obtiene la UnidadMedida por abreviatura.
   * 
   * @param abreviatura la abreviatura de la entidad
   * @return la entidad
   * @throws UnidadMedidaNotFoundException Si no existe ninguna unidad medida con
   *                                       esa abreviatura.
   */
  @Override
  @Transactional(readOnly = true)
  public UnidadMedida findByAbreviatura(String abreviatura) {
    log.debug("findByAbreviatura(String abreviatura) - start");
    final UnidadMedida unidadMedida = unidadMedidaRepository.findByAbreviatura(abreviatura)
        .orElseThrow(() -> new UnidadMedidaNotFoundException(abreviatura));
    log.debug("findByAbreviatura(String abreviatura) - end");
    return unidadMedida;
  }

}
