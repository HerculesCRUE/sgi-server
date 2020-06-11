package org.crue.hercules.sgi.catservice.service.impl;

import java.util.List;

import org.crue.hercules.sgi.catservice.exceptions.RegistroNotFoundException;
import org.crue.hercules.sgi.catservice.filter.RegistroFilter;
import org.crue.hercules.sgi.catservice.model.Registro;
import org.crue.hercules.sgi.catservice.repository.RegistroRepository;
import org.crue.hercules.sgi.catservice.repository.specification.RegistroSpecifications;
import org.crue.hercules.sgi.catservice.service.RegistroService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import lombok.extern.slf4j.Slf4j;

/**
 * Service Implementation para gestion {@link Registro}.
 */
@Service
@Slf4j
@Transactional
public class RegistroServiceImpl implements RegistroService {

  /** Registro repository. */
  private final RegistroRepository registroRepository;

  public RegistroServiceImpl(RegistroRepository registroRepository) {
    this.registroRepository = registroRepository;
  }

  /**
   * Guardar {@link Registro}.
   *
   * @param registro la entidad a guardar.
   * @return la entidad persistida.
   */
  @Override
  public Registro create(Registro registro) {
    log.debug("Petición a create Registro : {} - start", registro);

    Assert.isNull(registro.getId(), "Registro id tiene que ser null para crear un nuevo registro.");

    log.debug("Petición a create Registro : {} - end", registro);
    return registroRepository.save(registro);
  }

  /**
   * Actualizar {@link Registro}.
   *
   * @param registroActualizar la entidad a actualizar.
   * @return la entidad persistida.
   */
  @Override
  public Registro update(Registro registroActualizar) {
    log.debug("Petición a update Registro : {} - start", registroActualizar);

    Assert.notNull(registroActualizar.getId(), "Registro id no puede ser null para actualizar un registro");

    return registroRepository.findById(registroActualizar.getId()).map(registro -> {
      registro.setUsuarioRef(registroActualizar.getUsuarioRef());
      registro.setServicio(registroActualizar.getServicio());
      registro.setEstado(registroActualizar.getEstado());
      registro.setEntregaPapel(registroActualizar.getEntregaPapel());
      registro.setAceptaCondiciones(registroActualizar.getAceptaCondiciones());
      registro.setObservaciones(registroActualizar.getObservaciones());

      Registro returnValue = registroRepository.save(registro);
      log.debug("Petición a update Registro : {} - end", registroActualizar);
      return returnValue;
    }).orElseThrow(() -> new RegistroNotFoundException(registroActualizar.getId()));
  }

  /**
   * Obtener todas las entidades {@link Registro}.
   *
   * @param pageable la información de la paginación.
   * @return la lista de entidades.
   */

  @Override
  @Transactional(readOnly = true)
  public Page<Registro> findAll(Pageable pageable) {
    log.debug("Petición a get all Registro {} - start", pageable);
    return registroRepository.findAll(pageable);
  }

  /**
   * Obtener todas las entidades {@link Registro} filtradas.
   *
   * @param filter   la información del filtro.
   * @param pageable la información de la paginación.
   * @return la página de lista de entidades.
   */
  @Override
  @Transactional(readOnly = true)
  public Page<Registro> findAllLike(RegistroFilter filter, Pageable pageable) {
    log.debug("Petición a get all (filter, pageable) Registro filtro {}, pageable{} - start", filter, pageable);
    final Page<Registro> listregistro = registroRepository.findAll(RegistroSpecifications.byRegistroFilter(filter),
        pageable);
    log.debug("Petición a get all (filter, pageable) Registro filtro {}, pageable{} - end", filter, pageable);
    return listregistro;
  }

  /**
   * Obtener las entidades {@link Registro} .
   *
   * @return la lista de entidades {@link Registro}.
   */
  @Override
  @Transactional(readOnly = true)
  public List<Registro> findAll() {
    log.debug("Petición a get all Registro - start");
    return registroRepository.findAll();
  }

  /**
   * Obtiene {@link Registro} por id.
   *
   * @param id el id de la entidad.
   * @return la entidad.
   */
  @Override
  @Transactional(readOnly = true)
  public Registro findById(Long id) {
    log.debug("Petición a get Registro : {}  - start", id);
    final Registro registro = registroRepository.findById(id).orElseThrow(() -> new RegistroNotFoundException(id));
    log.debug("Petición a get Registro : {}  - end", id);
    return registro;
  }

  /**
   * Elimina un {@link Registro} por id.
   *
   * @param id el id de la entidad.
   */
  @Override
  public void delete(Long id) throws RegistroNotFoundException {
    log.debug("Petición a delete Registro : {}  - start", id);
    registroRepository.deleteById(id);

  }

}
