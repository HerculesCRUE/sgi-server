package org.crue.hercules.sgi.catservice.repository.specification;

import java.util.Collection;

import org.crue.hercules.sgi.catservice.filter.UnidadMedidaFilter;
import org.crue.hercules.sgi.catservice.model.UnidadMedida;
import org.crue.hercules.sgi.catservice.model.UnidadMedida_;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.CollectionUtils;

/**
 * UnidadMedida specifications
 */
public class UnidadMedidaSpecifications {
  /**
   * Devuelve las especificaciones que cumplen el filtro
   * 
   * @param filter {@link UnidadMedidaFilter}
   * @return La lista de especificaciones que cumplen el filtro
   */
  public static Specification<UnidadMedida> byUnidadMedidaFilter(UnidadMedidaFilter filter) {
    Specification<UnidadMedida> spec = Specification.where(UnidadMedidaSpecifications.byId(filter.getId()))
        .and(UnidadMedidaSpecifications.byIds(filter.getIds()))
        .and(UnidadMedidaSpecifications.byAbreviatura(filter.getAbreviatura()))
        .and(UnidadMedidaSpecifications.byDescripcion(filter.getDescripcion()));
    return spec;

  }

  /**
   * Para buscar por el identificador de UnidadMedida
   * 
   * @param id de UnidadMedida
   * @return especificación del id de UnidadMedida
   */
  public static Specification<UnidadMedida> byId(Long id) {
    return (root, query, cb) -> {
      if (id == null) {
        return cb.isTrue(cb.literal(true)); // always true = no filtering
      }
      return cb.equal(root.get(UnidadMedida_.id), id);
    };
  }

  /**
   * Para buscar por varios identificadores de UnidadMedida
   * 
   * @param ids de UnidadMedida
   * @return especificación de los ids de UnidadMedida
   */
  public static Specification<UnidadMedida> byIds(Collection<Long> ids) {
    return (root, query, cb) -> {
      if (CollectionUtils.isEmpty(ids)) {
        return cb.isTrue(cb.literal(true)); // always true = no filtering
      }
      return root.get(UnidadMedida_.id).in(ids);
    };
  }

  /**
   * Para buscar por código de unidadMedida
   * 
   * @param abreviatura de unidadMedida
   * @return especificación del código de unidadMedida
   */
  public static Specification<UnidadMedida> byAbreviatura(String abreviatura) {
    return (root, query, cb) -> {
      if (abreviatura == null) {
        return cb.isTrue(cb.literal(true)); // always true = no filtering
      }
      return cb.like(cb.lower(root.get(UnidadMedida_.abreviatura)), "%" + abreviatura.toLowerCase() + "%");

    };

  }

  /**
   * Para buscar por descripcion de unidadMedida
   * 
   * @param descripcion de unidadMedida
   * @return especificación del descripcion de unidadMedida
   */
  public static Specification<UnidadMedida> byDescripcion(String descripcion) {
    return (root, query, cb) -> {
      if (descripcion == null) {
        return cb.isTrue(cb.literal(true)); // always true = no filtering
      }
      return cb.like(cb.lower(root.get(UnidadMedida_.descripcion)), "%" + descripcion.toLowerCase() + "%");

    };

  }

}
