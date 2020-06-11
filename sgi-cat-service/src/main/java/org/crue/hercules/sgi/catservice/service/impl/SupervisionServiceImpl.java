package org.crue.hercules.sgi.catservice.service.impl;

import java.util.List;

import org.crue.hercules.sgi.catservice.exceptions.SupervisionNotFoundException;
import org.crue.hercules.sgi.catservice.filter.SupervisionFilter;
import org.crue.hercules.sgi.catservice.model.Supervision;
import org.crue.hercules.sgi.catservice.repository.SupervisionRepository;
import org.crue.hercules.sgi.catservice.repository.specification.SupervisionSpecifications;
import org.crue.hercules.sgi.catservice.service.SupervisionService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import lombok.extern.slf4j.Slf4j;

/**
 * Service Implementation para gestion {@link Supervision}.
 */
@Service
@Slf4j
@Transactional
public class SupervisionServiceImpl implements SupervisionService {
  private final SupervisionRepository supervisionRepository;

  public SupervisionServiceImpl(SupervisionRepository supervisionRepository) {
    this.supervisionRepository = supervisionRepository;
  }

  /**
   * Guarda la entidad Supervision.
   *
   * @param supervision la entidad a guardar.
   * @return la entidad persistida.
   */
  @Override
  public Supervision create(Supervision supervision) {
    log.debug("Petición a create Supervision : {} - start", supervision);
    Assert.isNull(supervision.getId(), "Supervision id tiene que ser null para crear un nuevo supervision");

    return supervisionRepository.save(supervision);
  }

  /**
   * Obtiene todas las entidades Supervision paginadas.
   *
   * @param pageable la información de paginación.
   * @return el listado de entidades.
   */
  @Override
  @Transactional(readOnly = true)
  public Page<Supervision> findAll(Pageable pageable) {
    log.debug("Petición a get all Supervision - start");
    return supervisionRepository.findAll(pageable);
  }

  /**
   * Obtiene todas las entidades Supervision.
   *
   * 
   * @return el listado de entidades.
   */
  @Override
  @Transactional(readOnly = true)
  public List<Supervision> findAll() {
    log.debug("Petición a get all Supervision - start");
    return supervisionRepository.findAll();
  }

  /**
   * Optiene una entidad Supervision por id.
   *
   * @param id el id de la entidad.
   * @return la entidad.
   * @throws SupervisionNotFoundException Si no existe ninguna supervision con ese
   *                                      id.
   */
  @Override
  @Transactional(readOnly = true)
  public Supervision findById(final Long id) throws SupervisionNotFoundException {
    log.debug("Petición a get Supervision : {}  - start", id);
    final Supervision supervision = supervisionRepository.findById(id)
        .orElseThrow(() -> new SupervisionNotFoundException(id));
    return supervision;
  }

  /**
   * Elimina una entidad Supervision por id.
   *
   * @param id el id de la entidad.
   */
  @Override
  public void delete(Long id) {
    log.debug("Petición a delete Supervision : {}  - start", id);
    supervisionRepository.deleteById(id);
  }

  /**
   * Actualiza los datos de Supervision.
   * 
   * @param supervisionActualizar Supervision con los datos actualizados.
   * @return La Supervision actualizada.
   * @throws SupervisionNotFoundException Si no existe ninguna supervision con ese
   *                                      id.
   * @throws IllegalArgumentException     Si supervision no tiene id.
   */
  public Supervision update(final Supervision supervisionActualizar) {
    log.debug("update(Supervision supervisionActualizar) - start");

    Assert.notNull(supervisionActualizar.getId(), "Supervision id no puede ser null para actualizar una supervision");

    return supervisionRepository.findById(supervisionActualizar.getId()).map(supervision -> {
      supervision.setUsuarioRef(supervisionActualizar.getUsuarioRef());
      supervision.setServicio(supervisionActualizar.getServicio());

      Supervision returnValue = supervisionRepository.save(supervision);
      log.debug("update(Supervision supervisionActualizar) - end");
      return returnValue;
    }).orElseThrow(() -> new SupervisionNotFoundException(supervisionActualizar.getId()));
  }

  /**
   * Devuelve todas las Supervision que cumplan con los datos del filtro
   * 
   * @param filter   filtro.
   * @param pageable informacion de la pagina.
   * @return lista de Supervision
   */
  @Override
  @Transactional(readOnly = true)
  public Page<Supervision> findAllLike(SupervisionFilter filter, Pageable pageable) {
    log.debug("Petición a get all Supervision - start");
    final Page<Supervision> listSupervision = supervisionRepository
        .findAll(SupervisionSpecifications.bySupervisionFilter(filter), pageable);
    return listSupervision;
  }
}