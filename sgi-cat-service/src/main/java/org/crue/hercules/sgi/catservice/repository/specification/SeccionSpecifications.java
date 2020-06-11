package org.crue.hercules.sgi.catservice.repository.specification;

import java.util.Collection;

import org.crue.hercules.sgi.catservice.filter.SeccionFilter;
import org.crue.hercules.sgi.catservice.model.Seccion_;
import org.crue.hercules.sgi.catservice.model.Seccion;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.CollectionUtils;

/**
 * Seccion specifications
 */
public class SeccionSpecifications {
  /**
   * Devuelve las especificaciones que cumplen el filtro
   * 
   * @param filter {@link SeccionFilter}
   * @return La lista de especificaciones que cumplen el filtro
   */
  public static Specification<Seccion> bySeccionFilter(SeccionFilter filter) {
    Specification<Seccion> spec = Specification.where(SeccionSpecifications.byId(filter.getId()))
        .and(SeccionSpecifications.byIds(filter.getIds())).and(SeccionSpecifications.byNombre(filter.getNombre()))
        .and(SeccionSpecifications.byDescripcion(filter.getDescripcion()));
    return spec;

  }

  /**
   * Para buscar por el identificador de {@link Seccion}
   * 
   * @param id de {@link Seccion}
   * @return especificación del id de {@link Seccion}
   */
  public static Specification<Seccion> byId(Long id) {
    return (root, query, cb) -> {
      if (id == null) {
        return cb.isTrue(cb.literal(true)); // always true = no filtering
      }
      return cb.equal(root.get(Seccion_.id), id);
    };
  }

  /**
   * Para buscar por varios identificadores de {@link Seccion}
   * 
   * @param ids de {@link Seccion}
   * @return especificación de los ids de {@link Seccion}
   */
  public static Specification<Seccion> byIds(Collection<Long> ids) {
    return (root, query, cb) -> {
      if (CollectionUtils.isEmpty(ids)) {
        return cb.isTrue(cb.literal(true)); // always true = no filtering
      }
      return root.get(Seccion_.id).in(ids);
    };
  }

  /**
   * Para buscar por nombre de {@link Seccion}
   * 
   * @param nombre de {@link Seccion}
   * @return especificación del nombre de {@link Seccion}
   */
  public static Specification<Seccion> byNombre(String nombre) {
    return (root, query, cb) -> {
      if (nombre == null) {
        return cb.isTrue(cb.literal(true)); // always true = no filtering
      }
      return cb.like(cb.lower(root.get(Seccion_.nombre)), "%" + nombre.toLowerCase() + "%");

    };

  }

  /**
   * Para buscar por descripcion de {@link Seccion}
   * 
   * @param descripcion de {@link Seccion}
   * @return especificación del descripcion de {@link Seccion}
   */
  public static Specification<Seccion> byDescripcion(String descripcion) {
    return (root, query, cb) -> {
      if (descripcion == null) {
        return cb.isTrue(cb.literal(true)); // always true = no filtering
      }
      return cb.like(cb.lower(root.get(Seccion_.descripcion)), "%" + descripcion.toLowerCase() + "%");

    };

  }

}
