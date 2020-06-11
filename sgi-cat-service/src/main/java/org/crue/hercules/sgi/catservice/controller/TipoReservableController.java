package org.crue.hercules.sgi.catservice.controller;

import java.util.List;

import javax.validation.Valid;

import org.crue.hercules.sgi.catservice.filter.TipoReservableFilter;
import org.crue.hercules.sgi.catservice.model.TipoReservable;
import org.crue.hercules.sgi.catservice.service.TipoReservableService;
import org.crue.hercules.sgi.catservice.util.ConstantesCat;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

/**
 * TipoReservableController
 */
@RestController
@RequestMapping(ConstantesCat.TIPORESERVABLE_CONTROLLER_BASE_PATH)
@Slf4j
public class TipoReservableController {

  /** TipoReservable service */
  private final TipoReservableService service;

  /**
   * Instancia un nuevo TipoReservableController.
   * 
   * @param service TipoReservableService
   */
  public TipoReservableController(TipoReservableService service) {
    log.debug("TipoReservableController(TipoReservableService service) - start");
    this.service = service;
    log.debug("TipoReservableController(TipoReservableService service) - end");
  }

  /**
   * Devuelve la lista completa de TipoReservable.
   * 
   * @return La lista con todos los TipoReservable.
   */
  @GetMapping(ConstantesCat.PATH_ALL)
  List<TipoReservable> all() {
    log.debug("TipoReservable all() - start");
    List<TipoReservable> returnValue = service.findAll();
    log.debug("TipoReservable all() - end");
    return returnValue;
  }

  /**
   * Devuelve todas las entidades TipoReservable paginadas.
   * 
   * @param pageable TipoReservableService
   * 
   * @return La lista con todos los TipoReservable
   */
  @GetMapping(ConstantesCat.PATH_ALL + ConstantesCat.PATH_PAGE)
  Page<TipoReservable> all(@PageableDefault(value = 10, page = 0) Pageable pageable) {
    log.debug("TipoReservable all(Pageable pageable) - start");
    Page<TipoReservable> returnValue = service.findAll(pageable);
    log.debug("TipoReservable all(Pageable pageable) - end");
    return returnValue;
  }

  /**
   * Devuelve todas las entidades TipoReservable que cumplen el filtro paginadas
   * 
   * @return Lista de todas las entidades TipoReservable filtradas por página
   */
  @GetMapping(ConstantesCat.PATH_ALL + ConstantesCat.PATH_PAGEFILTERED)
  Page<TipoReservable> all(@ModelAttribute TipoReservableFilter filter,
      @PageableDefault(value = 10, page = 0) Pageable pageable) {
    log.debug("all(TipoReservableFilter filter, Pageable pageable) - start");
    Page<TipoReservable> listTipoReservable = service.findAllLike(filter, pageable);
    log.debug("all(TipoReservable filter, Pageable pageable) - end");
    return listTipoReservable;
  }

  /**
   * Crea nuevo TipoReservable
   * 
   * @param nuevoTipoReservable TipoReservable que se quiere crear
   * @return Nuevo TipoReservable creado
   */
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  TipoReservable newTipoReservable(@Valid @RequestBody TipoReservable nuevoTipoReservable) {
    log.debug("newTipoReservable(TipoReservable nuevoTipoReservable) - start");
    TipoReservable returnValue = service.create(nuevoTipoReservable);
    log.debug("newTipoReservable(TipoReservable nuevoTipoReservable) - end");
    return returnValue;
  }

  /**
   * Actualiza TipoReservable.
   * 
   * @param updatedTipoReservable TipoReservable a actualizar
   * @param id                    id TipoReservable a actualizar.
   * @return TipoReservable actualizado.
   */
  @PutMapping(ConstantesCat.PATH_PARAMETER_ID)
  TipoReservable replaceTipoReservable(@Valid @RequestBody TipoReservable updatedTipoReservable,
      @PathVariable Long id) {
    log.debug("replaceTipoReservable(TipoReservable updatedTipoReservable, Long id) - start");
    updatedTipoReservable.setId(id);
    TipoReservable returnValue = service.update(updatedTipoReservable);
    log.debug("replaceTipoReservable(TipoReservable updatedTipoReservable, Long id) - end");
    return returnValue;
  }

  /**
   * Devuelve el TipoReservable con el id indicado
   * 
   * @param id Identificador de TipoReservable
   * @return TipoReservable correspondiente al id
   */
  @GetMapping(ConstantesCat.PATH_PARAMETER_ID)
  TipoReservable one(@PathVariable Long id) {
    log.debug("TipoReservable one(Long id) - start");
    TipoReservable returnValue = service.findById(id);
    log.debug("TipoReservable one(Long id) - end");
    return returnValue;
  }

  /**
   * Elimina TipoReservable con id indicado
   * 
   * @param id Identificador de TipoReservable
   */
  @DeleteMapping(ConstantesCat.PATH_PARAMETER_ID)
  void delete(@PathVariable Long id) {
    log.debug("deleteTipoReservable(Long id) - start");
    service.delete(id);
    log.debug("deleteTipoReservable(Long id) - end");
  }

}
