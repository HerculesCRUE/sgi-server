package org.crue.hercules.sgi.catservice.service.impl;

import java.util.List;

import org.crue.hercules.sgi.catservice.exceptions.ServicioNotFoundException;
import org.crue.hercules.sgi.catservice.filter.ServicioFilter;
import org.crue.hercules.sgi.catservice.model.Servicio;
import org.crue.hercules.sgi.catservice.repository.ServicioRepository;
import org.crue.hercules.sgi.catservice.repository.specification.ServicioSpecifications;
import org.crue.hercules.sgi.catservice.service.ServicioService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import lombok.extern.slf4j.Slf4j;

/**
 * Service Implementation para gestion {@link Servicio}.
 */
@Service
@Slf4j
@Transactional
public class ServicioServiceImpl implements ServicioService {

  /** Servicio repository. */
  private final ServicioRepository servicioRepository;

  public ServicioServiceImpl(ServicioRepository servicioRepository) {
    this.servicioRepository = servicioRepository;
  }

  /**
   * Guardar {@link Servicio}.
   *
   * @param servicio la entidad a guardar.
   * @return la entidad persistida.
   */
  @Override
  public Servicio create(Servicio servicio) {
    log.debug("Petición a create Servicio : {} - start", servicio);

    Assert.isNull(servicio.getId(), "Servicio id tiene que ser null para crear un nuevo servicio.");

    log.debug("Petición a create Servicio : {} - end", servicio);
    return servicioRepository.save(servicio);
  }

  /**
   * Actualizar {@link Servicio}.
   *
   * @param servicioActualizar la entidad a actualizar.
   * @return la entidad persistida.
   */
  @Override
  public Servicio update(Servicio servicioActualizar) {
    log.debug("Petición a update Servicio : {} - start", servicioActualizar);

    Assert.notNull(servicioActualizar.getId(), "Servicio id no puede ser null para actualizar un servicio");

    return servicioRepository.findById(servicioActualizar.getId()).map(servicio -> {
      servicio.setNombre(servicioActualizar.getNombre());
      servicio.setAbreviatura(servicioActualizar.getAbreviatura());
      servicio.setContacto(servicioActualizar.getContacto());
      servicio.setSeccion(servicioActualizar.getSeccion());

      Servicio returnValue = servicioRepository.save(servicio);
      log.debug("Petición a update Servicio : {} - end", servicioActualizar);
      return returnValue;
    }).orElseThrow(() -> new ServicioNotFoundException(servicioActualizar.getId()));
  }

  /**
   * Obtener todas las entidades {@link Servicio}.
   *
   * @param pageable la información de la paginación.
   * @return la lista de entidades.
   */

  @Override
  @Transactional(readOnly = true)
  public Page<Servicio> findAll(Pageable pageable) {
    log.debug("Petición a get all Servicio {} - start", pageable);
    return servicioRepository.findAll(pageable);
  }

  /**
   * Obtener todas las entidades {@link Servicio} filtradas.
   *
   * @param filter   la información del filtro.
   * @param pageable la información de la paginación.
   * @return la página de lista de entidades.
   */
  @Override
  @Transactional(readOnly = true)
  public Page<Servicio> findAllLike(ServicioFilter filter, Pageable pageable) {
    log.debug("Petición a get all (filter, pageable) Servicio filtro {}, pageable{} - start", filter, pageable);
    final Page<Servicio> listservicio = servicioRepository.findAll(ServicioSpecifications.byServicioFilter(filter),
        pageable);
    log.debug("Petición a get all (filter, pageable) Servicio filtro {}, pageable{} - end", filter, pageable);
    return listservicio;
  }

  /**
   * Obtener las entidades {@link Servicio} .
   *
   * @return la lista de entidades {@link Servicio}.
   */
  @Override
  @Transactional(readOnly = true)
  public List<Servicio> findAll() {
    log.debug("Petición a get all Servicio - start");
    return servicioRepository.findAll();
  }

  /**
   * Obtiene {@link Servicio} por id.
   *
   * @param id el id de la entidad.
   * @return la entidad.
   */
  @Override
  @Transactional(readOnly = true)
  public Servicio findById(Long id) {
    log.debug("Petición a get Servicio : {}  - start", id);
    final Servicio servicio = servicioRepository.findById(id).orElseThrow(() -> new ServicioNotFoundException(id));
    log.debug("Petición a get Servicio : {}  - end", id);
    return servicio;
  }

  /**
   * Elimina un {@link Servicio} por id.
   *
   * @param id el id de la entidad.
   */
  @Override
  public void delete(Long id) throws ServicioNotFoundException {
    log.debug("Petición a delete Servicio : {}  - start", id);
    servicioRepository.deleteById(id);

  }

}
