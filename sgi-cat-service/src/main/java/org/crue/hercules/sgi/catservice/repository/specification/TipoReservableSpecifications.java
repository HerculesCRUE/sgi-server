package org.crue.hercules.sgi.catservice.repository.specification;

import java.util.Collection;

import org.crue.hercules.sgi.catservice.enums.EstadoTipoReservableEnum;
import org.crue.hercules.sgi.catservice.filter.TipoReservableFilter;
import org.crue.hercules.sgi.catservice.model.Servicio;
import org.crue.hercules.sgi.catservice.model.Servicio_;
import org.crue.hercules.sgi.catservice.model.TipoReservable;
import org.crue.hercules.sgi.catservice.model.TipoReservable_;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.CollectionUtils;

/**
 * TipoReservable specifications
 */
public class TipoReservableSpecifications {
  /**
   * Devuelve las especificaciones que cumplen el filtro
   * 
   * @param filter {@link TipoReservableFilter}
   * @return La lista de especificaciones que cumplen el filtro
   */
  public static Specification<TipoReservable> byTipoReservableFilter(TipoReservableFilter filter) {
    Specification<TipoReservable> spec = Specification.where(TipoReservableSpecifications.byId(filter.getId()))
        .and(TipoReservableSpecifications.byIds(filter.getIds()))
        .and(TipoReservableSpecifications.byDescripcion(filter.getDescripcion()))
        .and(TipoReservableSpecifications.byDuracionMinima(filter.getDuracionMin()))
        .and(TipoReservableSpecifications.byDiasAntelacionMaximo(filter.getDiasAnteMax()))
        .and(TipoReservableSpecifications.byReservaMultiple(filter.getReservaMulti()))
        .and(TipoReservableSpecifications.byDiasVistaMaximaCalendario(filter.getDiasVistaMaxCalen()))
        .and(TipoReservableSpecifications.byHorasAntelacionMin(filter.getHorasAnteMin()))
        .and(TipoReservableSpecifications.byHorasAntelacionAnularUsuario(filter.getHorasAnteAnular()))
        .and(TipoReservableSpecifications.byEstado(filter.getEstado()))
        .and(TipoReservableSpecifications.byServicioId(filter.getServicioId()));

    return spec;

  }

  /**
   * Para buscar por el identificador de TipoReservable
   * 
   * @param id de TipoReservable
   * @return especificación del id de TipoReservable
   */
  public static Specification<TipoReservable> byId(Long id) {
    return (root, query, cb) -> {
      if (id == null) {
        return cb.isTrue(cb.literal(true)); // always true = no filtering
      }
      return cb.equal(root.get(TipoReservable_.id), id);
    };
  }

  /**
   * Para buscar por varios identificadores de TipoReservable
   * 
   * @param ids de TipoReservable
   * @return especificación de los ids de TipoReservable
   */
  public static Specification<TipoReservable> byIds(Collection<Long> ids) {
    return (root, query, cb) -> {
      if (CollectionUtils.isEmpty(ids)) {
        return cb.isTrue(cb.literal(true)); // always true = no filtering
      }
      return root.get(TipoReservable_.id).in(ids);
    };
  }

  /**
   * Para buscar por descripcion de TipoReservable
   * 
   * @param descripcion de TipoReservable
   * @return especificación de la descripcion de TipoReservable
   */
  public static Specification<TipoReservable> byDescripcion(String descripcion) {
    return (root, query, cb) -> {
      if (descripcion == null) {
        return cb.isTrue(cb.literal(true)); // always true = no filtering
      }
      return cb.like(cb.lower(root.get(TipoReservable_.descripcion)), "%" + descripcion.toLowerCase() + "%");

    };

  }

  /**
   * Para buscar por duración mínima del TipoReservable
   * 
   * @param duracionMin de TipoReservable
   * @return especificación de la duracionMinima de TipoReservable
   */
  public static Specification<TipoReservable> byDuracionMinima(Integer duracionMin) {
    return (root, query, cb) -> {
      if (duracionMin == null) {
        return cb.isTrue(cb.literal(true)); // always true = no filtering
      }
      return cb.equal(root.get(TipoReservable_.duracionMin), duracionMin);

    };

  }

  /**
   * Para buscar por días antelación máximo del TipoReservable
   * 
   * @param diasAnteMax de TipoReservable
   * @return especificación de los diasAntelacionMaximo de TipoReservable
   */
  public static Specification<TipoReservable> byDiasAntelacionMaximo(Integer diasAnteMax) {
    return (root, query, cb) -> {
      if (diasAnteMax == null) {
        return cb.isTrue(cb.literal(true)); // always true = no filtering
      }
      return cb.equal(root.get(TipoReservable_.diasAnteMax), diasAnteMax);

    };

  }

  /**
   * Para buscar por reserva múltiple del TipoReservable
   * 
   * @param reservaMulti de TipoReservable
   * @return especificación de los reservaMultiple de TipoReservable
   */
  public static Specification<TipoReservable> byReservaMultiple(Boolean reservaMulti) {
    return (root, query, cb) -> {
      if (reservaMulti == null) {
        return cb.isTrue(cb.literal(true)); // always true = no filtering
      }
      return cb.equal(root.get(TipoReservable_.reservaMulti), reservaMulti);

    };

  }

  /**
   * Para buscar por visita máxima calendario del TipoReservable
   * 
   * @param diasVistaMaxCalen de TipoReservable
   * @return especificación de los diasVistaMaxCalen de TipoReservable
   */
  public static Specification<TipoReservable> byDiasVistaMaximaCalendario(Integer diasVistaMaxCalen) {
    return (root, query, cb) -> {
      if (diasVistaMaxCalen == null) {
        return cb.isTrue(cb.literal(true)); // always true = no filtering
      }
      return cb.equal(root.get(TipoReservable_.diasVistaMaxCalen), diasVistaMaxCalen);

    };

  }

  /**
   * Para buscar por las horas antelación mínima del TipoReservable
   * 
   * @param horasAnteMin de TipoReservable
   * @return especificación de las horasAntelacionAnularUsuario de TipoReservable
   */
  public static Specification<TipoReservable> byHorasAntelacionMin(Integer horasAnteMin) {
    return (root, query, cb) -> {
      if (horasAnteMin == null) {
        return cb.isTrue(cb.literal(true)); // always true = no filtering
      }
      return cb.equal(root.get(TipoReservable_.horasAnteMin), horasAnteMin);

    };

  }

  /**
   * Para buscar por las horas antelación anular usuario del TipoReservable
   * 
   * @param horasAnteAnular de TipoReservable
   * @return especificación de las horasAntelacionAnularUsuario de TipoReservable
   */
  public static Specification<TipoReservable> byHorasAntelacionAnularUsuario(Integer horasAnteAnular) {
    return (root, query, cb) -> {
      if (horasAnteAnular == null) {
        return cb.isTrue(cb.literal(true)); // always true = no filtering
      }
      return cb.equal(root.get(TipoReservable_.horasAnteAnular), horasAnteAnular);

    };

  }

  /**
   * Para buscar por el estado del TipoReservable
   * 
   * @param estado de EstadoTipoReservableEnum
   * @return especificación del estado de TipoReservable
   */
  public static Specification<TipoReservable> byEstado(EstadoTipoReservableEnum estado) {
    return (root, query, cb) -> {
      if (estado == null) {
        return cb.isTrue(cb.literal(true)); // always true = no filtering
      }
      return cb.equal(root.get(TipoReservable_.estado), estado);

    };

  }

  /**
   * Para buscar por id de {@link Servicio}
   * 
   * @param servicioId de id {@link Servicio}
   * @return especificación del id de {@link Servicio}
   */
  public static Specification<TipoReservable> byServicioId(Long servicioId) {
    return (root, query, cb) -> {
      if (servicioId == null) {
        return cb.isTrue(cb.literal(true)); // always true = no filtering
      }
      return cb.equal(root.get(TipoReservable_.servicio).get(Servicio_.id), servicioId);

    };

  }

}
