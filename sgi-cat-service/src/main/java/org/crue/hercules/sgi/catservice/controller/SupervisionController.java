package org.crue.hercules.sgi.catservice.controller;

import java.util.List;

import javax.validation.Valid;

import org.crue.hercules.sgi.catservice.filter.SupervisionFilter;
import org.crue.hercules.sgi.catservice.model.Supervision;
import org.crue.hercules.sgi.catservice.service.SupervisionService;
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
@RequestMapping(ConstantesCat.SUPERVISION_CONTROLLER_BASE_PATH)
@Slf4j
public class SupervisionController {
  /** Supervision service */
  private SupervisionService service;

  /**
   * Instancia un nuevo SupervisionController.
   * 
   * @param service SupervisionService
   */
  public SupervisionController(SupervisionService service) {
    log.debug("SupervisionController(SupervisionService service) - start");
    this.service = service;
    log.debug("SupervisionController(SupervisionService service) - end");
  }

  /**
   * Devuelve la lista completa de Supervision.
   * 
   * @return La lista con todos los Supervision.
   */
  @GetMapping(ConstantesCat.PATH_ALL)
  List<Supervision> all() {
    log.debug("all() - start");
    List<Supervision> returnValue = service.findAll();
    log.debug("all() - end");
    return returnValue;
  }

  /**
   * Devuelve todas las entidades Supervision paginadas.
   * 
   * @param pageable SupervisionService
   * 
   * @return La lista con todos las supervision
   */
  @GetMapping(ConstantesCat.PATH_ALL + ConstantesCat.PATH_PAGE)
  Page<Supervision> all(@PageableDefault(value = 10, page = 0) Pageable pageable) {
    log.debug("all(Pageable pageable) - start");
    Page<Supervision> returnValue = service.findAll(pageable);
    log.debug("all(Pageable pageable) - end");
    return returnValue;
  }

  /**
   * Devuelve todas las entidades Supervision que cumplen el filtro paginadas
   * 
   * @return Lista de todas las entidades Supervision filtradas por página
   */
  @GetMapping(ConstantesCat.PATH_ALL + ConstantesCat.PATH_PAGEFILTERED)
  Page<Supervision> all(@ModelAttribute SupervisionFilter filter,
      @PageableDefault(value = 10, page = 0) Pageable pageable) {
    log.debug("all(SupervisionFilter filter, Pageable pageable) - start");
    Page<Supervision> listSupervision = service.findAllLike(filter, pageable);
    log.debug("all(Supervision filter, Pageable pageable) - end");
    return listSupervision;
  }

  /**
   * Crea nuevo Supervision
   * 
   * @param nuevaSupervision Supervision que se quiere crear
   * @return Nuevo Supervision creado
   */
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  Supervision newSupervision(@Valid @RequestBody Supervision nuevaSupervision) {
    log.debug("newSupervision(Supervision nuevaSupervision) - start");
    Supervision returnValue = service.create(nuevaSupervision);
    log.debug("newSupervision(Supervision nuevaSupervision) - end");
    return returnValue;
  }

  /**
   * Actualiza Supervision.
   * 
   * @param updatedSupervision Supervision a actualizar
   * @param id                 id Supervision a actualizar.
   * @return Supervision actualizado.
   */
  @PutMapping(ConstantesCat.PATH_PARAMETER_ID)
  Supervision replaceSupervision(@Valid @RequestBody Supervision updatedSupervision, @PathVariable Long id) {
    log.debug("replaceSupervision(Supervision updatedSupervision, Long id) - start");
    updatedSupervision.setId(id);
    Supervision returnValue = service.update(updatedSupervision);
    log.debug("replaceSupervision(Supervision updatedSupervision, Long id) - end");
    return returnValue;
  }

  /**
   * Devuelve el Supervision con el id indicado
   * 
   * @param id Identificador de Supervision
   * @return Supervision correspondiente al id
   */
  @GetMapping(ConstantesCat.PATH_PARAMETER_ID)
  private Supervision one(@PathVariable Long id) {
    log.debug("one(Long id) - start");
    Supervision returnValue = service.findById(id);
    log.debug("one(Long id) - end");
    return returnValue;
  }

  /**
   * Elimina Supervision con id indicado
   * 
   * @param id Identificador de Supervision
   */
  @DeleteMapping(ConstantesCat.PATH_PARAMETER_ID)
  void delete(@PathVariable Long id) {
    log.debug("deleteSupervision(Long id) - start");
    service.delete(id);
    log.debug("deleteSupervision(Long id) - end");
  }
}