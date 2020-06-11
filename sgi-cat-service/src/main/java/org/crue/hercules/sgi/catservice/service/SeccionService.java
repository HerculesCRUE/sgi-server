package org.crue.hercules.sgi.catservice.service;

import java.util.List;

import org.crue.hercules.sgi.catservice.exceptions.SeccionNotFoundException;
import org.crue.hercules.sgi.catservice.filter.SeccionFilter;
import org.crue.hercules.sgi.catservice.model.Seccion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface para gestionar {@link Seccion}.
 */
public interface SeccionService {

  /**
   * Guardar {@link Seccion}.
   *
   * @param seccion la entidad a guardar.
   * @return la entidad persistida.
   */
  Seccion create(Seccion seccion);

  /**
   * Actualizar {@link Seccion}.
   *
   * @param seccion la entidad a actualizar.
   * @return la entidad persistida.
   */
  Seccion update(Seccion seccion);

  /**
   * Obtener todas las entidades {@link Seccion}.
   *
   * @param pageable la información de la paginación.
   * @return la lista de entidades.
   */
  Page<Seccion> findAll(Pageable pageable);

  /**
   * Obtener todas las entidades {@link Seccion} filtradas.
   *
   * @param filter   la información del filtro.
   * @param pageable la información de la paginación.
   * @return la página de lista de entidades.
   */
  Page<Seccion> findAllLike(SeccionFilter filter, Pageable pageable);

  /**
   * Obtener las entidades {@link Seccion} .
   *
   * @return la lista de entidades {@link Seccion}.
   */
  List<Seccion> findAll();

  /**
   * Obtiene {@link Seccion} por id.
   *
   * @param id el id de la entidad.
   * @return la entidad.
   */
  Seccion findById(Long id);

  /**
   * Elimina un {@link Seccion} por id.
   *
   * @param id el id de la entidad.
   */
  void delete(Long id) throws SeccionNotFoundException;

}
