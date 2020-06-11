package org.crue.hercules.sgi.catservice.service;

import java.util.List;

import org.crue.hercules.sgi.catservice.exceptions.UnidadMedidaNotFoundException;
import org.crue.hercules.sgi.catservice.filter.UnidadMedidaFilter;
import org.crue.hercules.sgi.catservice.model.UnidadMedida;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface para gestionar {@link UnidadMedida}.
 */
public interface UnidadMedidaService {

  /**
   * Guardar UnidadMedida.
   *
   * @param unidadMedida la entidad a guardar.
   * @return la entidad persistida.
   */
  UnidadMedida create(UnidadMedida unidadMedida) throws IllegalArgumentException;

  /**
   * Actualizar UnidadMedida.
   *
   * @param unidadMedida la entidad a actualizar.
   * @return la entidad persistida.
   * @throws UnidadMedidaNotFoundException Si no existe ninguna unidad medida con
   *                                       ese id.
   * @throws IllegalArgumentException      Si la unidad medida no tiene id.
   */
  UnidadMedida update(UnidadMedida unidadMedida) throws UnidadMedidaNotFoundException, IllegalArgumentException;

  /**
   * Elimina UnidadMedida por "id".
   *
   * @param id el id de la entidad.
   * @throws UnidadMedidaNotFoundException Si no existe ninguna unidad medida con
   *                                       ese id.
   * @throws IllegalArgumentException      Si no hay id.
   *
   */
  void delete(Long id) throws UnidadMedidaNotFoundException;

  /**
   * Obtiene todas las entidades UnidadMedida .
   *
   * @return la lista de entidades.
   */
  List<UnidadMedida> findAll();

  /**
   * Obtiene todas las entidades UnidadMedida paginadas.
   *
   * @param pageable la información de la paginación.
   * @return la lista paginada de entidades.
   */
  Page<UnidadMedida> findAll(Pageable pageable);

  /**
   * Obtiene todas las entidades UnidadMedida filtradas y paginadas.
   *
   * @param filter   la información del filtro.
   * @param pageable la información de la paginación.
   * @return la lista filtrada y paginada de entidades.
   */
  Page<UnidadMedida> findAllLike(UnidadMedidaFilter filter, Pageable pageable);

  /**
   * Obtiene UnidadMedida por "id"
   *
   * @param id el id de la entidad.
   * @return la entidad.
   * @throws UnidadMedidaNotFoundException Si no existe ninguna unidad medida con
   *                                       ese id.
   */
  UnidadMedida findById(Long id) throws UnidadMedidaNotFoundException;

  /**
   * Obtiene UnidadMedida por "abreviatura".
   *
   * @param abreviatura el abreviatura de la entidad.
   * @return la entidad.
   * @throws UnidadMedidaNotFoundException Si no existe ninguna unidad medida con
   *                                       esa abreviatura.
   */
  UnidadMedida findByAbreviatura(String abreviatura);

}