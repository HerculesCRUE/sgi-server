package org.crue.hercules.sgi.catservice.repository.specification;

import java.util.Collection;

import org.crue.hercules.sgi.catservice.filter.ServicioFilter;
import org.crue.hercules.sgi.catservice.model.Seccion;
import org.crue.hercules.sgi.catservice.model.Seccion_;
import org.crue.hercules.sgi.catservice.model.Servicio;
import org.crue.hercules.sgi.catservice.model.Servicio_;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.CollectionUtils;

/**
 * Servicio specifications
 */
public class ServicioSpecifications {
  /**
   * Devuelve las especificaciones que cumplen el filtro
   * 
   * @param filter {@link ServicioFilter}
   * @return La lista de especificaciones que cumplen el filtro
   */
  public static Specification<Servicio> byServicioFilter(ServicioFilter filter) {
    Specification<Servicio> spec = Specification.where(ServicioSpecifications.byId(filter.getId()))
        .and(ServicioSpecifications.byIds(filter.getIds())).and(ServicioSpecifications.byNombre(filter.getNombre()))
        .and(ServicioSpecifications.byAbreviatura(filter.getAbreviatura()))
        .and(ServicioSpecifications.byContacto(filter.getContacto()))
        .and(ServicioSpecifications.byIdSeccion(filter.getIdSeccion()));
    return spec;

  }

  /**
   * Para buscar por el identificador de {@link Servicio}
   * 
   * @param id de {@link Servicio}
   * @return especificación del id de {@link Servicio}
   */
  public static Specification<Servicio> byId(Long id) {
    return (root, query, cb) -> {
      if (id == null) {
        return cb.isTrue(cb.literal(true)); // always true = no filtering
      }
      return cb.equal(root.get(Servicio_.id), id);
    };
  }

  /**
   * Para buscar por varios identificadores de {@link Servicio}
   * 
   * @param ids de {@link Servicio}
   * @return especificación de los ids de {@link Servicio}
   */
  public static Specification<Servicio> byIds(Collection<Long> ids) {
    return (root, query, cb) -> {
      if (CollectionUtils.isEmpty(ids)) {
        return cb.isTrue(cb.literal(true)); // always true = no filtering
      }
      return root.get(Servicio_.id).in(ids);
    };
  }

  /**
   * Para buscar por abreviatura de {@link Servicio}
   * 
   * @param abreviatura de {@link Servicio}
   * @return especificación de la abreviatura de {@link Servicio}
   */
  public static Specification<Servicio> byAbreviatura(String abreviatura) {
    return (root, query, cb) -> {
      if (abreviatura == null) {
        return cb.isTrue(cb.literal(true)); // always true = no filtering
      }
      return cb.like(cb.lower(root.get(Servicio_.abreviatura)), "%" + abreviatura.toLowerCase() + "%");

    };

  }

  /**
   * Para buscar por contacto de {@link Servicio}.
   * 
   * @param contacto de {@link Servicio}
   * @return especificación del contacto de {@link Servicio}
   */
  public static Specification<Servicio> byContacto(String contacto) {
    return (root, query, cb) -> {
      if (contacto == null) {
        return cb.isTrue(cb.literal(true)); // always true = no filtering
      }
      return cb.like(cb.lower(root.get(Servicio_.contacto)), "%" + contacto.toLowerCase() + "%");

    };

  }

  /**
   * Para buscar por id de {@link Seccion} de {@link Servicio}.
   * 
   * @param idSeccion de {@link Seccion}
   * @return especificación del id de {@link Seccion} de {@link Servicio}
   */
  public static Specification<Servicio> byIdSeccion(Long idSeccion) {
    return (root, query, cb) -> {
      if (idSeccion == null) {
        return cb.isTrue(cb.literal(true)); // always true = no filtering
      }
      return cb.equal(root.get(Servicio_.seccion).get(Seccion_.id), idSeccion);

    };

  }

  /**
   * Para buscar por el nombre de {@link Servicio}.
   * 
   * @param nombre de {@link Servicio}
   * @return especificación del nombre de {@link Servicio}
   */
  public static Specification<Servicio> byNombre(String nombre) {
    return (root, query, cb) -> {
      if (nombre == null) {
        return cb.isTrue(cb.literal(true)); // always true = no filtering
      }
      return cb.like(cb.lower(root.get(Servicio_.nombre)), "%" + nombre.toLowerCase() + "%");

    };

  }

}
