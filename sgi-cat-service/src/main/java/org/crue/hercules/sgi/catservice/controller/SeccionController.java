package org.crue.hercules.sgi.catservice.controller;

import java.util.List;

import javax.validation.Valid;

import org.crue.hercules.sgi.catservice.filter.SeccionFilter;
import org.crue.hercules.sgi.catservice.model.Seccion;
import org.crue.hercules.sgi.catservice.service.SeccionService;
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
 * SeccionController
 */
@RestController
@RequestMapping(ConstantesCat.SECCION_CONTROLLER_BASE_PATH)
@Slf4j
public class SeccionController {

  /** Seccion service */
  private SeccionService service;

  /**
   * Instancia un nuevo SeccionController.
   * 
   * @param service SeccionService
   */
  public SeccionController(SeccionService service) {
    log.debug("SeccionController(SeccionService service) - start");
    this.service = service;
    log.debug("SeccionController(SeccionService service) - end");
  }

  /**
   * Devuelve la lista completa de Seccion.
   * 
   * @return La lista con todos los Seccion.
   */
  @GetMapping(ConstantesCat.PATH_ALL)
  List<Seccion> all() {
    log.debug("all() - start");
    List<Seccion> returnValue = service.findAll();
    log.debug("all() - end");
    return returnValue;
  }

  /**
   * Devuelve todas las entidades Seccion paginadas.
   * 
   * @param pageable SeccionService
   * 
   * @return La lista con todos los servicios
   */
  @GetMapping(ConstantesCat.PATH_ALL + ConstantesCat.PATH_PAGE)
  Page<Seccion> all(@PageableDefault(value = 10, page = 0) Pageable pageable) {
    log.debug("all(Pageable pageable) - start");
    Page<Seccion> returnValue = service.findAll(pageable);
    log.debug("all(Pageable pageable) - end");
    return returnValue;
  }

  /**
   * Devuelve todas las entidades Seccion que cumplen el filtro paginadas
   * 
   * @return Lista de todas las entidades Seccion filtradas por página
   */
  @GetMapping(ConstantesCat.PATH_ALL + ConstantesCat.PATH_PAGEFILTERED)
  Page<Seccion> all(@ModelAttribute SeccionFilter filter, @PageableDefault(value = 10, page = 0) Pageable pageable) {
    log.debug("all(SeccionFilter filter) - start");
    Page<Seccion> listSecciones = service.findAllLike(filter, pageable);
    log.debug("all(SeccionFilter filter) - end");
    return listSecciones;
  }

  /**
   * Crea nuevo Seccion
   * 
   * @param nuevoSeccion Seccion que se quiere crear
   * @return Nuevo Seccion creado
   */
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  Seccion newSeccion(@Valid @RequestBody Seccion nuevoSeccion) {
    log.debug("newSeccion(Seccion nuevoSeccion) - start");
    Seccion returnValue = service.create(nuevoSeccion);
    log.debug("newSeccion(Seccion nuevoSeccion) - end");
    return returnValue;
  }

  /**
   * Actualiza Seccion.
   * 
   * @param updatedSeccion Seccion a actualizar
   * @param id             id Seccion a actualizar.
   * @return seccion actualizada
   */
  @PutMapping(ConstantesCat.PATH_PARAMETER_ID)
  Seccion replaceSeccion(@Valid @RequestBody Seccion updatedSeccion, @PathVariable Long id) {
    log.debug("replaceSeccion(Seccion updatedSeccion, Long id) - start");
    updatedSeccion.setId(id);
    Seccion returnValue = service.update(updatedSeccion);
    log.debug("replaceSeccion(Seccion updatedSeccion, Long id) - end");
    return returnValue;
  }

  /**
   * Devuelve la seccion con el id indicado.
   * 
   * @param id Identificador de Seccion
   * @return Seccion correspondiente al id
   */
  @GetMapping(ConstantesCat.PATH_PARAMETER_ID)
  private Seccion one(@PathVariable Long id) {
    log.debug("one(Long id) - start");
    Seccion returnValue = service.findById(id);
    log.debug("one(Long id) - end");
    return returnValue;
  }

  /**
   * Elimina Seccion con id indicado.
   * 
   * @param id Identificador de Seccion
   */
  @DeleteMapping(ConstantesCat.PATH_PARAMETER_ID)
  void delete(@PathVariable Long id) {
    log.debug("deleteSeccion(Long id) - start");
    service.delete(id);
    log.debug("deleteSeccion(Long id) - end");
  }

}
