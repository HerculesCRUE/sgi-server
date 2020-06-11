package org.crue.hercules.sgi.catservice.repository;

import org.crue.hercules.sgi.catservice.model.Supervision;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository para {@link Supervision}.
 */
@Repository
public interface SupervisionRepository extends JpaRepository<Supervision, Long>, JpaSpecificationExecutor<Supervision> {

}