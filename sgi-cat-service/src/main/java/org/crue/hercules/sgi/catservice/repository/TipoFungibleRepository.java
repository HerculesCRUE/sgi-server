package org.crue.hercules.sgi.catservice.repository;

import org.crue.hercules.sgi.catservice.model.TipoFungible;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository para {@link TipoFungible}.
 */

@Repository
public interface TipoFungibleRepository
    extends JpaRepository<TipoFungible, Long>, JpaSpecificationExecutor<TipoFungible> {

}