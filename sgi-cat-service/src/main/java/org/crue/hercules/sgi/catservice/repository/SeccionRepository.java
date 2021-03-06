package org.crue.hercules.sgi.catservice.repository;

import org.crue.hercules.sgi.catservice.model.Seccion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository para {@link Seccion}.
 */

@Repository
public interface SeccionRepository extends JpaRepository<Seccion, Long>, JpaSpecificationExecutor<Seccion> {

}