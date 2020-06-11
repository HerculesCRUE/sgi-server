package org.crue.hercules.sgi.catservice.service;

import org.crue.hercules.sgi.catservice.model.TipoFungible;

import java.util.List;

import org.crue.hercules.sgi.catservice.exceptions.TipoFungibleNotFoundException;
import org.crue.hercules.sgi.catservice.filter.TipoFungibleFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TipoFungibleService {
  /**
   * Guardar TipoFungible.
   *
   * @param tipoFungible la entidad a guardar.
   * @return la entidad persistida.
   */
  TipoFungible create(TipoFungible tipoFungible);

  /**
   * Actualizar TipoFungible.
   *
   * @param tipoFungible la entidad a actualizar.
   * @return la entidad persistida.
   */
  TipoFungible update(TipoFungible tipoFungible);

  /**
   * Obtener todas las entidades TipoFungible.
   *
   * @param pageable la información de la paginación.
   * @return la lista de entidades.
   */
  Page<TipoFungible> findAll(Pageable pageable);

  /**
   * Obtener todas las entidades TipoFungible filtradas.
   *
   * @param filter   la información del filtro.
   * @param pageable la información de la paginación.
   * @return la página de lista de entidades.
   */
  Page<TipoFungible> findAllLike(TipoFungibleFilter filter, Pageable pageable);

  /**
   * Obtener las entidades TipoFungible .
   *
   * @return la lista de entidades TipoFungible.
   */
  List<TipoFungible> findAll();

  /**
   * Obtiene TipoFungible por "id".
   *
   * @param id el id de la entidad.
   * @return la entidad.
   */
  TipoFungible findById(Long id);

  /**
   * Elimina el TipoFungible por "id".
   *
   * @param id el id de la entidad.
   */
  void delete(Long id) throws TipoFungibleNotFoundException;
}