package org.crue.hercules.sgi.catservice.repository;

import java.util.Optional;

import org.crue.hercules.sgi.catservice.model.UnidadMedida;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository para {@link UnidadMedida}.
 */

@Repository
public interface UnidadMedidaRepository
    extends JpaRepository<UnidadMedida, Long>, JpaSpecificationExecutor<UnidadMedida> {

  /**
   * Devuelve una unidad de medida por abreviatura
   * 
   * @param abreviatura abreviatura de la unidad medida
   * @return {@link UnidadMedida}
   */
  Optional<UnidadMedida> findByAbreviatura(String abreviatura);

}