package org.crue.hercules.sgi.catservice.controller;

import java.util.List;

import javax.validation.Valid;

import org.crue.hercules.sgi.catservice.filter.RegistroFilter;
import org.crue.hercules.sgi.catservice.model.Registro;
import org.crue.hercules.sgi.catservice.service.RegistroService;
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
 * RegistroController
 */
@RestController
@RequestMapping(ConstantesCat.REGISTRO_CONTROLLER_BASE_PATH)
@Slf4j
public class RegistroController {

  /** Registro service */
  private RegistroService service;

  /**
   * Instancia un nuevo RegistroController.
   * 
   * @param service RegistroService
   */
  public RegistroController(RegistroService service) {
    log.debug("RegistroController(RegistroService service) - start");
    this.service = service;
    log.debug("RegistroController(RegistroService service) - end");
  }

  /**
   * Devuelve la lista completa de {@link Registro}.
   * 
   * @return La lista con todos los {@link Registro}.
   */
  @GetMapping(ConstantesCat.PATH_ALL)
  List<Registro> all() {
    log.debug("all() - start");
    List<Registro> returnValue = service.findAll();
    log.debug("all() - end");
    return returnValue;
  }

  /**
   * Devuelve todas las entidades {@link Registro} paginadas.
   * 
   * @param pageable información de la paginación.
   * 
   * @return La lista con todos los {@link Registro}.
   */
  @GetMapping(ConstantesCat.PATH_ALL + ConstantesCat.PATH_PAGE)
  Page<Registro> all(@PageableDefault(value = 10, page = 0) Pageable pageable) {
    log.debug("all() - start");
    Page<Registro> returnValue = service.findAll(pageable);
    log.debug("all() - end");
    return returnValue;
  }

  /**
   * Devuelve todas las entidades {@link Registro} que cumplen el filtro paginadas
   * 
   * @return Lista de todas las entidades {@link Registro} filtradas por página
   */
  @GetMapping(ConstantesCat.PATH_ALL + ConstantesCat.PATH_PAGEFILTERED)
  Page<Registro> all(@ModelAttribute RegistroFilter filter, @PageableDefault(value = 10, page = 0) Pageable pageable) {
    log.debug("all(RegistroFilter filter) - start");
    Page<Registro> returnValue = service.findAllLike(filter, pageable);
    log.debug("all(Registro filter) - end");
    return returnValue;
  }

  /**
   * Crea nuevo {@link Registro}
   * 
   * @param nuevoRegistro {@link Registro} que se quiere crear
   * @return Nuevo {@link Registro} creado
   */
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  Registro newRegistro(@Valid @RequestBody Registro nuevoRegistro) {
    log.debug("newRegistro(Registro nuevoRegistro) - start");
    Registro returnValue = service.create(nuevoRegistro);
    log.debug("newRegistro(Registro nuevoRegistro) - end");
    return returnValue;
  }

  /**
   * Actualiza {@link Registro}.
   * 
   * @param updatedRegistro {@link Registro} a actualizar
   * @param id              id {@link Registro} a actualizar.
   * @return registro actualizado.
   */
  @PutMapping(ConstantesCat.PATH_PARAMETER_ID)
  Registro replaceRegistro(@Valid @RequestBody Registro updatedRegistro, @PathVariable Long id) {
    log.debug("replaceRegistro(Registro updatedRegistro, Long id) - start");
    updatedRegistro.setId(id);
    Registro returnValue = service.update(updatedRegistro);
    log.debug("replaceRegistro(Registro updatedRegistro, Long id) - end");
    return returnValue;
  }

  /**
   * Devuelve el {@link Registro} con el id indicado
   * 
   * @param id Identificador de {@link Registro}
   * @return {@link Registro} correspondiente al id
   */
  @GetMapping(ConstantesCat.PATH_PARAMETER_ID)
  private Registro one(@PathVariable Long id) {
    log.debug("one(Long id) - start");
    Registro returnValue = service.findById(id);
    log.debug("one(Long id) - end");
    return returnValue;
  }

  /**
   * Elimina {@link Registro} con id indicado
   * 
   * @param id Identificador de {@link Registro}
   */
  @DeleteMapping(ConstantesCat.PATH_PARAMETER_ID)
  void delete(@PathVariable Long id) {
    log.debug("deleteRegistro(Long id) - start");
    service.delete(id);
    log.debug("deleteRegistro(Long id) - end");
  }

}
