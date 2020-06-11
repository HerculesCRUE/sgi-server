package org.crue.hercules.sgi.catservice.service;

import java.util.List;

import org.crue.hercules.sgi.catservice.exceptions.RegistroNotFoundException;
import org.crue.hercules.sgi.catservice.filter.RegistroFilter;
import org.crue.hercules.sgi.catservice.model.Registro;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface para gestionar {@link Registro}.
 */
public interface RegistroService {

  /**
   * Guardar {@link Registro}.
   *
   * @param servicio la entidad a guardar.
   * @return la entidad persistida.
   */
  Registro create(Registro servicio);

  /**
   * Actualizar {@link Registro}.
   *
   * @param servicio la entidad a actualizar.
   * @return la entidad persistida.
   */
  Registro update(Registro servicio);

  /**
   * Obtener todas las entidades {@link Registro}.
   *
   * @param pageable la información de la paginación.
   * @return la lista de entidades.
   */
  Page<Registro> findAll(Pageable pageable);

  /**
   * Obtener todas las entidades {@link Registro} filtradas.
   *
   * @param filter   la información del filtro.
   * @param pageable la información de la paginación.
   * @return la página de lista de entidades.
   */
  Page<Registro> findAllLike(RegistroFilter filter, Pageable pageable);

  /**
   * Obtener las entidades {@link Registro} .
   *
   * @return la lista de entidades {@link Registro}.
   */
  List<Registro> findAll();

  /**
   * Obtiene {@link Registro} por id.
   *
   * @param id el id de la entidad.
   * @return la entidad.
   */
  Registro findById(Long id);

  /**
   * Elimina un {@link Registro} por id.
   *
   * @param id el id de la entidad.
   */
  void delete(Long id) throws RegistroNotFoundException;

}
