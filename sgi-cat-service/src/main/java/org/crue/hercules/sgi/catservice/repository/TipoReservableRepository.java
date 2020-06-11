package org.crue.hercules.sgi.catservice.repository;

import java.util.Optional;

import org.crue.hercules.sgi.catservice.model.TipoReservable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository para {@link TipoReservable}.
 */

@Repository
public interface TipoReservableRepository
    extends JpaRepository<TipoReservable, Long>, JpaSpecificationExecutor<TipoReservable> {

  /**
   * Devuelve un tipo de reservable por su descripción
   * 
   * @param descripcion descripción del tipo de reservable
   * @return {@link TipoReservable}
   */
  Optional<TipoReservable> findByDescripcion(String descripcion);

}