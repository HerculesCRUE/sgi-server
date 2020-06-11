package org.crue.hercules.sgi.catservice.service.impl;

import java.util.List;

import org.crue.hercules.sgi.catservice.exceptions.SeccionNotFoundException;
import org.crue.hercules.sgi.catservice.filter.SeccionFilter;
import org.crue.hercules.sgi.catservice.model.Seccion;
import org.crue.hercules.sgi.catservice.repository.SeccionRepository;
import org.crue.hercules.sgi.catservice.repository.specification.SeccionSpecifications;
import org.crue.hercules.sgi.catservice.service.SeccionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import lombok.extern.slf4j.Slf4j;

/**
 * Service Implementation para gestion {@link Seccion}.
 */
@Service
@Slf4j
@Transactional
public class SeccionServiceImpl implements SeccionService {

  /** Seccion repository. */
  private final SeccionRepository seccionRepository;

  public SeccionServiceImpl(SeccionRepository seccionRepository) {
    this.seccionRepository = seccionRepository;
  }

  /**
   * Guardar {@link Seccion}.
   *
   * @param seccion la entidad a guardar.
   * @return la entidad persistida.
   */
  @Override
  public Seccion create(Seccion seccion) {
    log.debug("Petición a create Seccion : {} - start", seccion);

    Assert.isNull(seccion.getId(), "Seccion id tiene que ser null para crear un nuevo seccion.");

    log.debug("Petición a create Seccion : {} - end", seccion);
    return seccionRepository.save(seccion);
  }

  /**
   * Actualizar {@link Seccion}.
   *
   * @param seccionActualizar la entidad a actualizar.
   * @return la entidad persistida.
   */
  @Override
  public Seccion update(Seccion seccionActualizar) {
    log.debug("Petición a update Seccion : {} - start", seccionActualizar);

    Assert.notNull(seccionActualizar.getId(), "Seccion id no puede ser null para actualizar un seccion");

    return seccionRepository.findById(seccionActualizar.getId()).map(seccion -> {
      seccion.setNombre(seccionActualizar.getNombre());
      seccion.setDescripcion(seccionActualizar.getDescripcion());

      Seccion returnValue = seccionRepository.save(seccion);
      log.debug("Petición a update Seccion : {} - end", seccionActualizar);
      return returnValue;
    }).orElseThrow(() -> new SeccionNotFoundException(seccionActualizar.getId()));
  }

  /**
   * Obtener todas las entidades {@link Seccion}.
   *
   * @param pageable la información de la paginación.
   * @return la lista de entidades.
   */

  @Override
  @Transactional(readOnly = true)
  public Page<Seccion> findAll(Pageable pageable) {
    log.debug("Petición a get all Seccion {} - start", pageable);
    return seccionRepository.findAll(pageable);
  }

  /**
   * Obtener todas las entidades {@link Seccion} filtradas.
   *
   * @param filter   la información del filtro.
   * @param pageable la información de la paginación.
   * @return la página de lista de entidades.
   */
  @Override
  @Transactional(readOnly = true)
  public Page<Seccion> findAllLike(SeccionFilter filter, Pageable pageable) {
    log.debug("Petición a get all (filter, pageable) Seccion filtro {}, pageable{} - start", filter, pageable);
    final Page<Seccion> listseccion = seccionRepository.findAll(SeccionSpecifications.bySeccionFilter(filter),
        pageable);
    log.debug("Petición a get all (filter, pageable) Seccion filtro {}, pageable{} - end", filter, pageable);
    return listseccion;
  }

  /**
   * Obtener las entidades {@link Seccion} .
   *
   * @return la lista de entidades {@link Seccion}.
   */
  @Override
  @Transactional(readOnly = true)
  public List<Seccion> findAll() {
    log.debug("Petición a get all Seccion - start");
    return seccionRepository.findAll();
  }

  /**
   * Obtiene {@link Seccion} por id.
   *
   * @param id el id de la entidad.
   * @return la entidad.
   */
  @Override
  @Transactional(readOnly = true)
  public Seccion findById(Long id) {
    log.debug("Petición a get Seccion : {}  - start", id);
    final Seccion seccion = seccionRepository.findById(id).orElseThrow(() -> new SeccionNotFoundException(id));
    log.debug("Petición a get Seccion : {}  - end", id);
    return seccion;
  }

  /**
   * Elimina un {@link Seccion} por id.
   *
   * @param id el id de la entidad.
   */
  @Override
  public void delete(Long id) throws SeccionNotFoundException {
    log.debug("Petición a delete Seccion : {}  - start", id);
    seccionRepository.deleteById(id);

  }

}
