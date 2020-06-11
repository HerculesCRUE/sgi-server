package org.crue.hercules.sgi.catservice.repository.specification;

import java.util.Collection;

import org.crue.hercules.sgi.catservice.filter.SupervisionFilter;
import org.crue.hercules.sgi.catservice.model.Servicio_;
import org.crue.hercules.sgi.catservice.model.Supervision;
import org.crue.hercules.sgi.catservice.model.Supervision_;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.CollectionUtils;

/**
 * Supervision specifications
 */
public class SupervisionSpecifications {
  /**
   * Devuelve las especificaciones que cumplen el filtro
   * 
   * @param filter {@link SupervisionFilter}
   * @return La lista de especificaciones que cumplen el filtro
   */
  public static Specification<Supervision> bySupervisionFilter(SupervisionFilter filter) {
    Specification<Supervision> spec = Specification.where(SupervisionSpecifications.byId(filter.getId()))
        .and(SupervisionSpecifications.byIds(filter.getIds()))
        .and(SupervisionSpecifications.byUsuarioRef(filter.getUsuarioRef()))
        .and(SupervisionSpecifications.byServicioId(filter.getServicioId()));
    return spec;

  }

  /**
   * Para buscar por el identificador de Supervision
   * 
   * @param id de Supervision
   * @return especificación del id de Supervision
   */
  public static Specification<Supervision> byId(Long id) {
    return (root, query, cb) -> {
      if (id == null) {
        return cb.isTrue(cb.literal(true)); // always true = no filtering
      }
      return cb.equal(root.get(Supervision_.id), id);
    };
  }

  /**
   * Para buscar por varios identificadores de Supervision
   * 
   * @param ids de Supervision
   * @return especificación de los ids de Supervision
   */
  public static Specification<Supervision> byIds(Collection<Long> ids) {
    return (root, query, cb) -> {
      if (CollectionUtils.isEmpty(ids)) {
        return cb.isTrue(cb.literal(true)); // always true = no filtering
      }
      return root.get(Supervision_.id).in(ids);
    };
  }

  /**
   * Para buscar por nombre de Supervision
   * 
   * @param usuarioRef de Supervision
   * @return especificación del código de Supervision
   */
  public static Specification<Supervision> byUsuarioRef(String usuarioRef) {
    return (root, query, cb) -> {
      if (usuarioRef == null) {
        return cb.isTrue(cb.literal(true)); // always true = no filtering
      }
      return cb.like(cb.lower(root.get(Supervision_.usuarioRef)), "%" + usuarioRef.toLowerCase() + "%");

    };

  }

  /**
   * Para buscar por servicio de Supervision
   * 
   * @param servicioId de Servicio
   * @return especificación del Supervision
   */
  public static Specification<Supervision> byServicioId(Long servicioId) {
    return (root, query, cb) -> {
      if (servicioId == null) {
        return cb.isTrue(cb.literal(true)); // always true = no filtering
      }
      return cb.equal(root.get(Supervision_.servicio).get(Servicio_.id), servicioId);

    };

  }
}