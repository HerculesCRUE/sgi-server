package org.crue.hercules.sgi.catservice.service;

import java.util.List;

import org.crue.hercules.sgi.catservice.exceptions.ServicioNotFoundException;
import org.crue.hercules.sgi.catservice.filter.ServicioFilter;
import org.crue.hercules.sgi.catservice.model.Servicio;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface para gestionar {@link Servicio}.
 */
public interface ServicioService {

  /**
   * Guardar {@link Servicio}.
   *
   * @param servicio la entidad a guardar.
   * @return la entidad persistida.
   */
  Servicio create(Servicio servicio);

  /**
   * Actualizar {@link Servicio}.
   *
   * @param servicio la entidad a actualizar.
   * @return la entidad persistida.
   */
  Servicio update(Servicio servicio);

  /**
   * Obtener todas las entidades {@link Servicio}.
   *
   * @param pageable la información de la paginación.
   * @return la lista de entidades.
   */
  Page<Servicio> findAll(Pageable pageable);

  /**
   * Obtener todas las entidades {@link Servicio} filtradas.
   *
   * @param filter   la información del filtro.
   * @param pageable la información de la paginación.
   * @return la página de lista de entidades.
   */
  Page<Servicio> findAllLike(ServicioFilter filter, Pageable pageable);

  /**
   * Obtener las entidades {@link Servicio} .
   *
   * @return la lista de entidades {@link Servicio}.
   */
  List<Servicio> findAll();

  /**
   * Obtiene {@link Servicio} por id.
   *
   * @param id el id de la entidad.
   * @return la entidad.
   */
  Servicio findById(Long id);

  /**
   * Elimina un {@link Servicio} por id.
   *
   * @param id el id de la entidad.
   */
  void delete(Long id) throws ServicioNotFoundException;

}
