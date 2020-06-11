package org.crue.hercules.sgi.catservice.repository.specification;

import java.util.Collection;

import org.crue.hercules.sgi.catservice.filter.TipoFungibleFilter;
import org.crue.hercules.sgi.catservice.model.Servicio_;
import org.crue.hercules.sgi.catservice.model.TipoFungible;
import org.crue.hercules.sgi.catservice.model.TipoFungible_;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.CollectionUtils;

/**
 * TipoFungible specifications
 */
public class TipoFungibleSpecifications {
  /**
   * Devuelve las especificaciones que cumplen el filtro
   * 
   * @param filter {@link TipoFungibleFilter}
   * @return La lista de especificaciones que cumplen el filtro
   */
  public static Specification<TipoFungible> byTipoFungibleFilter(TipoFungibleFilter filter) {
    Specification<TipoFungible> spec = Specification.where(TipoFungibleSpecifications.byId(filter.getId()))
        .and(TipoFungibleSpecifications.byIds(filter.getIds()))
        .and(TipoFungibleSpecifications.byNombre(filter.getNombre()))
        .and(TipoFungibleSpecifications.byServicioId(filter.getServicioId()));
    return spec;

  }

  /**
   * Para buscar por el identificador de TipoFungible
   * 
   * @param id de TipoFungible
   * @return especificación del id de TipoFungible
   */
  public static Specification<TipoFungible> byId(Long id) {
    return (root, query, cb) -> {
      if (id == null) {
        return cb.isTrue(cb.literal(true)); // always true = no filtering
      }
      return cb.equal(root.get(TipoFungible_.id), id);
    };
  }

  /**
   * Para buscar por varios identificadores de TipoFungible
   * 
   * @param ids de TipoFungible
   * @return especificación de los ids de TipoFungible
   */
  public static Specification<TipoFungible> byIds(Collection<Long> ids) {
    return (root, query, cb) -> {
      if (CollectionUtils.isEmpty(ids)) {
        return cb.isTrue(cb.literal(true)); // always true = no filtering
      }
      return root.get(TipoFungible_.id).in(ids);
    };
  }

  /**
   * Para buscar por nombre de tipoFungible
   * 
   * @param nombre de tipoFungible
   * @return especificación del código de tipoFungible
   */
  public static Specification<TipoFungible> byNombre(String nombre) {
    return (root, query, cb) -> {
      if (nombre == null) {
        return cb.isTrue(cb.literal(true)); // always true = no filtering
      }
      return cb.like(cb.lower(root.get(TipoFungible_.nombre)), "%" + nombre.toLowerCase() + "%");

    };

  }

  /**
   * Para buscar por servicio de tipoFungible
   * 
   * @param servicioId de Servicio
   * @return especificación del tipoFungible
   */
  public static Specification<TipoFungible> byServicioId(Long servicioId) {
    return (root, query, cb) -> {
      if (servicioId == null) {
        return cb.isTrue(cb.literal(true)); // always true = no filtering
      }
      return cb.equal(root.get(TipoFungible_.servicio).get(Servicio_.id), servicioId);

    };

  }
}