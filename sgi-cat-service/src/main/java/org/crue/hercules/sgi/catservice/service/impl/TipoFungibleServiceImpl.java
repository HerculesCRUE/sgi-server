package org.crue.hercules.sgi.catservice.service.impl;

import java.util.List;

import org.crue.hercules.sgi.catservice.exceptions.TipoFungibleNotFoundException;
import org.crue.hercules.sgi.catservice.filter.TipoFungibleFilter;
import org.crue.hercules.sgi.catservice.model.TipoFungible;
import org.crue.hercules.sgi.catservice.repository.TipoFungibleRepository;
import org.crue.hercules.sgi.catservice.repository.specification.TipoFungibleSpecifications;
import org.crue.hercules.sgi.catservice.service.TipoFungibleService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import lombok.extern.slf4j.Slf4j;

/**
 * Service Implementation para gestion {@link TipoFungible}.
 */
@Service
@Slf4j
@Transactional
public class TipoFungibleServiceImpl implements TipoFungibleService {
  private final TipoFungibleRepository tipoFungibleRepository;

  public TipoFungibleServiceImpl(TipoFungibleRepository tipoFungibleRepository) {
    this.tipoFungibleRepository = tipoFungibleRepository;
  }

  /**
   * Guarda la entidad TipoFungible.
   *
   * @param tipoFungible la entidad a guardar.
   * @return la entidad persistida.
   */
  @Override
  public TipoFungible create(TipoFungible tipoFungible) {
    log.debug("Petición a create TipoFungible : {} - start", tipoFungible);
    Assert.isNull(tipoFungible.getId(), "TipoFungible id tiene que ser null para crear un nuevo tipoFungible");

    return tipoFungibleRepository.save(tipoFungible);
  }

  /**
   * Obtiene todas las entidades TipoFungible paginadas.
   *
   * @param pageable la información de paginación.
   * @return el listado de entidades.
   */
  @Override
  @Transactional(readOnly = true)
  public Page<TipoFungible> findAll(Pageable pageable) {
    log.debug("Petición a get all TipoFungible - start");
    return tipoFungibleRepository.findAll(pageable);
  }

  /**
   * Obtiene todas las entidades TipoFungible.
   *
   * 
   * @return el listado de entidades.
   */
  @Override
  @Transactional(readOnly = true)
  public List<TipoFungible> findAll() {
    log.debug("Petición a get all TipoFungible - start");
    return tipoFungibleRepository.findAll();
  }

  /**
   * Optiene una entidad TipoFungible por id.
   *
   * @param id el id de la entidad.
   * @return la entidad.
   * @throws TipoFungibleNotFoundException Si no existe ningún tipo fungible con
   *                                       ese id.
   */
  @Override
  @Transactional(readOnly = true)
  public TipoFungible findById(final Long id) throws TipoFungibleNotFoundException {
    log.debug("Petición a get TipoFungible : {}  - start", id);
    final TipoFungible tipoFungible = tipoFungibleRepository.findById(id)
        .orElseThrow(() -> new TipoFungibleNotFoundException(id));
    return tipoFungible;
  }

  /**
   * Elimina una entidad TipoFungible por id.
   *
   * @param id el id de la entidad.
   */
  @Override
  public void delete(Long id) {
    log.debug("Petición a delete TipoFungible : {}  - start", id);
    tipoFungibleRepository.deleteById(id);
  }

  /**
   * Actualiza los datos del TipoFungible.
   * 
   * @param tipoFungibleActualizar TipoFungible con los datos actualizados.
   * @return El Tipo Fungible actualizado.
   * @throws TipoFungibleNotFoundException Si no existe ningún tipo fungible con
   *                                       ese id.
   * @throws IllegalArgumentException      Si el tipo fungible no tiene id.
   */
  public TipoFungible update(final TipoFungible tipoFungibleActualizar) {
    log.debug("update(TipoFungible tipoFungibleActualizar) - start");

    Assert.notNull(tipoFungibleActualizar.getId(),
        "TipoFungible id no puede ser null para actualizar un tipo fungible");

    return tipoFungibleRepository.findById(tipoFungibleActualizar.getId()).map(tipoFungible -> {
      tipoFungible.setNombre(tipoFungibleActualizar.getNombre());
      tipoFungible.setServicio(tipoFungibleActualizar.getServicio());

      TipoFungible returnValue = tipoFungibleRepository.save(tipoFungible);
      log.debug("update(TipoFungible tipoFungibleActualizar) - end");
      return returnValue;
    }).orElseThrow(() -> new TipoFungibleNotFoundException(tipoFungibleActualizar.getId()));
  }

  /**
   * Devuelve todas los TipoFungible que cumplan con los datos del filtro
   * 
   * @param filter   filtro.
   * @param pageable informacion de la pagina.
   * @return lista de TipoFungible
   */
  @Override
  @Transactional(readOnly = true)
  public Page<TipoFungible> findAllLike(TipoFungibleFilter filter, Pageable pageable) {
    log.debug("Petición a get all TipoFungibles - start");
    final Page<TipoFungible> listTipoFungible = tipoFungibleRepository
        .findAll(TipoFungibleSpecifications.byTipoFungibleFilter(filter), pageable);
    return listTipoFungible;
  }
}