package org.crue.hercules.sgi.catservice.service;

import java.util.List;

import org.crue.hercules.sgi.catservice.exceptions.TipoReservableNotFoundException;
import org.crue.hercules.sgi.catservice.filter.TipoReservableFilter;
import org.crue.hercules.sgi.catservice.model.TipoReservable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface para gestionar {@link TipoReservable}.
 */
public interface TipoReservableService {

  /**
   * Guardar TipoReservable.
   *
   * @param tipoReservable la entidad a guardar.
   * @return la entidad persistida.
   */
  TipoReservable create(TipoReservable tipoReservable);

  /**
   * Actualizar TipoReservable.
   *
   * @param tipoReservable la entidad a actualizar.
   * @return la entidad persistida.
   */
  TipoReservable update(TipoReservable tipoReservable);

  /**
   * Obtener todas las entidades TipoReservable.
   *
   * @param pageable la información de la paginación.
   * @return la lista de entidades.
   */
  Page<TipoReservable> findAll(Pageable pageable);

  /**
   * Obtener todas las entidades TipoReservable filtradas.
   *
   * @param filter   la información del filtro.
   * @param pageable la información de la paginación.
   * @return la página de lista de entidades.
   */
  Page<TipoReservable> findAllLike(TipoReservableFilter filter, Pageable pageable);

  /**
   * Obtener las entidades TipoReservable .
   *
   * @return la lista de entidades TipoReservable.
   */
  List<TipoReservable> findAll();

  /**
   * Obtiene la entidad TipoReservable a partir del "id"
   *
   * @param id el id de la entidad.
   * @return la entidad.
   */
  TipoReservable findById(Long id);

  /**
   * Elimina TipoReservable por "id".
   *
   * @param id el id de la entidad.
   */
  void delete(Long id) throws TipoReservableNotFoundException;

}
