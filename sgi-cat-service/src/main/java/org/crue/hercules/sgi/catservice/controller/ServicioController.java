package org.crue.hercules.sgi.catservice.controller;

import java.util.List;

import javax.validation.Valid;

import org.crue.hercules.sgi.catservice.filter.ServicioFilter;
import org.crue.hercules.sgi.catservice.model.Servicio;
import org.crue.hercules.sgi.catservice.service.ServicioService;
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
 * ServicioController
 */
@RestController
@RequestMapping(ConstantesCat.SERVICIO_CONTROLLER_BASE_PATH)
@Slf4j
public class ServicioController {

  /** Servicio service */
  private ServicioService service;

  /**
   * Instancia un nuevo ServicioController.
   * 
   * @param service ServicioService
   */
  public ServicioController(ServicioService service) {
    log.debug("ServicioController(ServicioService service) - start");
    this.service = service;
    log.debug("ServicioController(ServicioService service) - end");
  }

  /**
   * Devuelve la lista completa de Servicio.
   * 
   * @return La lista con todos los Servicio.
   */
  @GetMapping(ConstantesCat.PATH_ALL)
  List<Servicio> all() {
    log.debug("all() - start");
    List<Servicio> returnValue = service.findAll();
    log.debug("all() - end");
    return returnValue;
  }

  /**
   * Devuelve todas las entidades Servicio paginadas.
   * 
   * @param pageable ServicioService
   * 
   * @return La lista con todos los servicios
   */
  @GetMapping(ConstantesCat.PATH_ALL + ConstantesCat.PATH_PAGE)
  Page<Servicio> all(@PageableDefault(value = 10, page = 0) Pageable pageable) {
    log.debug("all() - start");
    Page<Servicio> returnValue = service.findAll(pageable);
    log.debug("all() - end");
    return returnValue;
  }

  /**
   * Devuelve todas las entidades Servicio que cumplen el filtro paginadas
   * 
   * @return Lista de todas las entidades Servicio filtradas por página
   */
  @GetMapping(ConstantesCat.PATH_ALL + ConstantesCat.PATH_PAGEFILTERED)
  Page<Servicio> all(@ModelAttribute ServicioFilter filter, @PageableDefault(value = 10, page = 0) Pageable pageable) {
    log.debug("all(ServicioFilter filter) - start");
    Page<Servicio> returnValue = service.findAllLike(filter, pageable);
    log.debug("all(Servicio filter) - end");
    return returnValue;
  }

  /**
   * Crea nuevo Servicio
   * 
   * @param nuevoServicio Servicio que se quiere crear
   * @return Nuevo Servicio creado
   */
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  Servicio newServicio(@Valid @RequestBody Servicio nuevoServicio) {
    log.debug("newServicio(Servicio nuevoServicio) - start");
    Servicio returnValue = service.create(nuevoServicio);
    log.debug("newServicio(Servicio nuevoServicio) - end");
    return returnValue;
  }

  /**
   * Actualiza Servicio.
   * 
   * @param updatedServicio Servicio a actualizar
   * @param id              id Servicio a actualizar.
   * @return servicio actualizado.
   */
  @PutMapping(ConstantesCat.PATH_PARAMETER_ID)
  Servicio replaceServicio(@Valid @RequestBody Servicio updatedServicio, @PathVariable Long id) {
    log.debug("replaceServicio(Servicio updatedServicio, Long id) - start");
    updatedServicio.setId(id);
    Servicio returnValue = service.update(updatedServicio);
    log.debug("replaceServicio(Servicio updatedServicio, Long id) - end");
    return returnValue;
  }

  /**
   * Devuelve el servicio con el id indicado
   * 
   * @param id Identificador de Servicio
   * @return Servicio correspondiente al id
   */
  @GetMapping(ConstantesCat.PATH_PARAMETER_ID)
  private Servicio one(@PathVariable Long id) {
    log.debug("one(Long id) - start");
    Servicio returnValue = service.findById(id);
    log.debug("one(Long id) - end");
    return returnValue;
  }

  /**
   * Elimina Servicio con id indicado
   * 
   * @param id Identificador de Servicio
   */
  @DeleteMapping(ConstantesCat.PATH_PARAMETER_ID)
  void delete(@PathVariable Long id) {
    log.debug("deleteServicio(Long id) - start");
    service.delete(id);
    log.debug("deleteServicio(Long id) - end");
  }

}
