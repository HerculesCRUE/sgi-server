package org.crue.hercules.sgi.catservice.repository.specification;

import java.util.Collection;

import org.crue.hercules.sgi.catservice.enums.EstadoRegistroEnum;
import org.crue.hercules.sgi.catservice.filter.RegistroFilter;
import org.crue.hercules.sgi.catservice.model.Servicio;
import org.crue.hercules.sgi.catservice.model.Servicio_;
import org.crue.hercules.sgi.catservice.model.Registro;
import org.crue.hercules.sgi.catservice.model.Registro_;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.CollectionUtils;

/**
 * Registro specifications
 */
public class RegistroSpecifications {
  /**
   * Devuelve las especificaciones que cumplen el filtro
   * 
   * @param filter {@link RegistroFilter}
   * @return La lista de especificaciones que cumplen el filtro
   */
  public static Specification<Registro> byRegistroFilter(RegistroFilter filter) {
    Specification<Registro> spec = Specification.where(RegistroSpecifications.byId(filter.getId()))
        .and(RegistroSpecifications.byIds(filter.getIds()))
        .and(RegistroSpecifications.byUsuarioRef(filter.getUsuarioRef()))
        .and(RegistroSpecifications.byServicioId(filter.getServicioId()))
        .and(RegistroSpecifications.byEstado(filter.getEstadoRegistro()))
        .and(RegistroSpecifications.byEntregaPapel(filter.getEntregaPapel()))
        .and(RegistroSpecifications.byAceptaCondiciones(filter.getAceptaCondiciones()))
        .and(RegistroSpecifications.byObservaciones(filter.getObservaciones()));
    return spec;

  }

  /**
   * Para buscar por el identificador de {@link Registro}
   * 
   * @param id de {@link Registro}
   * @return especificación del id de {@link Registro}
   */
  public static Specification<Registro> byId(Long id) {
    return (root, query, cb) -> {
      if (id == null) {
        return cb.isTrue(cb.literal(true)); // always true = no filtering
      }
      return cb.equal(root.get(Registro_.id), id);
    };
  }

  /**
   * Para buscar por varios identificadores de {@link Registro}
   * 
   * @param ids de {@link Registro}
   * @return especificación de los ids de {@link Registro}
   */
  public static Specification<Registro> byIds(Collection<Long> ids) {
    return (root, query, cb) -> {
      if (CollectionUtils.isEmpty(ids)) {
        return cb.isTrue(cb.literal(true)); // always true = no filtering
      }
      return root.get(Registro_.id).in(ids);
    };
  }

  /**
   * Para buscar por referencia de usuairo de {@link Registro}
   * 
   * @param usuarioRef de {@link Registro}
   * @return especificación de la referencia de usuario de {@link Registro}
   */
  public static Specification<Registro> byUsuarioRef(String usuarioRef) {
    return (root, query, cb) -> {
      if (usuarioRef == null) {
        return cb.isTrue(cb.literal(true)); // always true = no filtering
      }
      return cb.like(cb.lower(root.get(Registro_.usuarioRef)), "%" + usuarioRef.toLowerCase() + "%");

    };

  }

  /**
   * Para buscar por contacto de {@link Registro}.
   * 
   * @param servicioId identificador de {@link Servicio} de {@link Registro}
   * @return especificación identificador de {@link Servicio} de {@link Registro}
   */
  public static Specification<Registro> byServicioId(String servicioId) {
    return (root, query, cb) -> {
      if (servicioId == null) {
        return cb.isTrue(cb.literal(true)); // always true = no filtering
      }
      return cb.equal(root.get(Registro_.servicio).get(Servicio_.id), servicioId);

    };

  }

  /**
   * Para buscar por estado de registro de {@link Registro}.
   * 
   * @param estadoRegistro de {@link Registro}
   * @return especificación del estado de registro de {@link Registro}
   */
  public static Specification<Registro> byEstado(EstadoRegistroEnum estadoRegistro) {
    return (root, query, cb) -> {
      if (estadoRegistro == null) {
        return cb.isTrue(cb.literal(true)); // always true = no filtering
      }
      return cb.equal(root.get(Registro_.estado), estadoRegistro);

    };

  }

  /**
   * Para buscar por indicador entrega papel de {@link Registro}.
   * 
   * @param entregaPapel de {@link Registro}
   * @return especificación del indicador entrega papel de {@link Registro}
   */
  public static Specification<Registro> byEntregaPapel(Boolean entregaPapel) {
    return (root, query, cb) -> {
      if (entregaPapel == null) {
        return cb.isTrue(cb.literal(true)); // always true = no filtering
      }
      return cb.equal(root.get(Registro_.entregaPapel), entregaPapel);

    };

  }

  /**
   * Para buscar por indicador acepta condiciones de {@link Registro}.
   * 
   * @param aceptaCondiciones de {@link Registro}
   * @return especificación del indicador acepta condiciones de {@link Registro}
   */
  public static Specification<Registro> byAceptaCondiciones(Boolean aceptaCondiciones) {
    return (root, query, cb) -> {
      if (aceptaCondiciones == null) {
        return cb.isTrue(cb.literal(true)); // always true = no filtering
      }
      return cb.equal(root.get(Registro_.aceptaCondiciones), aceptaCondiciones);

    };

  }

  /**
   * Para buscar por observaciones de {@link Registro}.
   * 
   * @param observaciones de {@link Registro}
   * @return especificación de observaciones de {@link Registro}
   */
  public static Specification<Registro> byObservaciones(String observaciones) {
    return (root, query, cb) -> {
      if (observaciones == null) {
        return cb.isTrue(cb.literal(true)); // always true = no filtering
      }
      return cb.like(cb.lower(root.get(Registro_.observaciones)), "%" + observaciones.toLowerCase() + "%");

    };

  }

}
