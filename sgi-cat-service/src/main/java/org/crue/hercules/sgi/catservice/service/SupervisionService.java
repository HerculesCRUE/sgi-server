package org.crue.hercules.sgi.catservice.service;

import org.crue.hercules.sgi.catservice.model.Supervision;
import java.util.List;

import org.crue.hercules.sgi.catservice.exceptions.SupervisionNotFoundException;
import org.crue.hercules.sgi.catservice.filter.SupervisionFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SupervisionService {
  /**
   * Guardar Supervision.
   *
   * @param supervision la entidad a guardar.
   * @return la entidad persistida.
   */
  Supervision create(Supervision supervision);

  /**
   * Actualizar Supervision.
   *
   * @param supervision la entidad a actualizar.
   * @return la entidad persistida.
   */
  Supervision update(Supervision supervision);

  /**
   * Obtener todas las entidades Supervision.
   *
   * @param pageable la información de la paginación.
   * @return la lista de entidades.
   */
  Page<Supervision> findAll(Pageable pageable);

  /**
   * Obtener todas las entidades Supervision filtradas.
   *
   * @param filter   la información del filtro.
   * @param pageable la información de la paginación.
   * @return la página de lista de entidades.
   */
  Page<Supervision> findAllLike(SupervisionFilter filter, Pageable pageable);

  /**
   * Obtener las entidades Supervision .
   *
   * @return la lista de entidades Supervision.
   */
  List<Supervision> findAll();

  /**
   * Obtiene Supervision por "id".
   *
   * @param id el id de la entidad.
   * @return la entidad.
   */
  Supervision findById(Long id);

  /**
   * Elimina el Supervision por "id".
   *
   * @param id el id de la entidad.
   */
  void delete(Long id) throws SupervisionNotFoundException;
}