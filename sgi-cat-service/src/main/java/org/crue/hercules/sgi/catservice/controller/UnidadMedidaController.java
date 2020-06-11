package org.crue.hercules.sgi.catservice.controller;

import java.util.List;

import javax.validation.Valid;

import org.crue.hercules.sgi.catservice.filter.UnidadMedidaFilter;
import org.crue.hercules.sgi.catservice.model.UnidadMedida;
import org.crue.hercules.sgi.catservice.service.UnidadMedidaService;
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
 * UnidadMedidaController
 */
@RestController
@RequestMapping("/unidadmedidas")
@Slf4j
public class UnidadMedidaController {

  /** UnidadMedida service */
  private UnidadMedidaService service;

  /**
   * Instancia un nuevo UnidadMedidaController.
   * 
   * @param service UnidadMedidaService.
   */
  public UnidadMedidaController(UnidadMedidaService service) {
    log.debug("UnidadMedidaController(UnidadMedidaService service) - start");
    this.service = service;
    log.debug("UnidadMedidaController(UnidadMedidaService service) - end");
  }

  /**
   * Crea UnidadMedida.
   * 
   * @param nuevoUnidadMedida UnidadMedida que se quiere crear.
   * @return Nuevo UnidadMedida creado.
   * @throws IllegalArgumentException Si la unidad medida tiene id.
   */
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  UnidadMedida newUnidadMedida(@Valid @RequestBody UnidadMedida nuevoUnidadMedida) {
    log.debug("newUnidadMedida(UnidadMedida nuevoUnidadMedida) - start");
    UnidadMedida returnValue = service.create(nuevoUnidadMedida);
    log.debug("newUnidadMedida(UnidadMedida nuevoUnidadMedida) - end");
    return returnValue;
  }

  /**
   * Actualiza UnidadMedida.
   * 
   * @param updatedUnidadMedida UnidadMedida a actualizar.
   * @param id                  Identificador de UnidadMedida.
   * @return unidadMedida actualizado.
   * @throws UnidadMedidaNotFoundException Si no existe ninguna unidad medida con
   *                                       ese id.
   * @throws IllegalArgumentException      Si la unidad medida no tiene id.
   */
  @PutMapping(ConstantesCat.PATH_PARAMETER_ID)
  UnidadMedida replaceUnidadMedida(@Valid @RequestBody UnidadMedida updatedUnidadMedida, @PathVariable Long id) {
    log.debug("replaceUnidadMedida(UnidadMedida updatedUnidadMedida, Long id) - start");
    updatedUnidadMedida.setId(id);
    UnidadMedida returnValue = service.update(updatedUnidadMedida);
    log.debug("replaceUnidadMedida(UnidadMedida updatedUnidadMedida, Long id) - end");
    return returnValue;
  }

  /**
   * Elimina UnidadMedida por id.
   * 
   * @param id Identificador de UnidadMedida.
   * @throws UnidadMedidaNotFoundException Si no existe ninguna unidad medida con
   *                                       ese id.
   * @throws IllegalArgumentException      Si la unidad medida no tiene id.
   */
  @DeleteMapping(ConstantesCat.PATH_PARAMETER_ID)
  void delete(@PathVariable Long id) {
    log.debug("deleteUnidadMedida(Long id) - start");
    service.delete(id);
    log.debug("deleteUnidadMedida(Long id) - end");
  }

  /**
   * Obtiene la lista completa de UnidadMedida.
   * 
   * @return La lista con todos los UnidadMedida.
   */
  @GetMapping(ConstantesCat.PATH_ALL)
  List<UnidadMedida> all() {
    log.debug("all() - start");
    List<UnidadMedida> returnValue = service.findAll();
    log.debug("all() - end");
    return returnValue;
  }

  /**
   * Obtiene todas las entidades UnidadMedida paginadas.
   * 
   * @param pageable UnidadMedidaService.
   * 
   * @return La lista con todos los unidadMedidas.
   */
  @GetMapping(ConstantesCat.PATH_ALL + ConstantesCat.PATH_PAGE)
  Page<UnidadMedida> all(@PageableDefault(value = 10, page = 0) Pageable pageable) {
    log.debug("all(Pageable pageable) - start");
    Page<UnidadMedida> returnValue = service.findAll(pageable);
    log.debug("all(Pageable pageable) - end");
    return returnValue;
  }

  /**
   * Obtiene todas las entidades UnidadMedida que cumplen el filtro paginadas.
   * 
   * @return Lista de todas las entidades UnidadMedida filtradas por página.
   */
  @GetMapping(ConstantesCat.PATH_ALL + ConstantesCat.PATH_PAGEFILTERED)
  Page<UnidadMedida> all(@ModelAttribute UnidadMedidaFilter filter,
      @PageableDefault(value = 10, page = 0) Pageable pageable) {
    log.debug("all(UnidadMedidaFilter filter, Pageable pageable) - start");
    Page<UnidadMedida> listUnidadMedida = service.findAllLike(filter, pageable);
    log.debug("all(UnidadMedida filter, Pageable pageable) - end");
    return listUnidadMedida;
  }

  /**
   * Obtiene unidadMedida por id.
   * 
   * @param id Identificador de UnidadMedida.
   * @return UnidadMedida correspondiente al id.
   * @throws UnidadMedidaNotFoundException Si no existe ninguna unidad medida con
   *                                       ese id.
   */
  @GetMapping(ConstantesCat.PATH_PARAMETER_ID)
  private UnidadMedida one(@PathVariable Long id) {
    log.debug("one(Long id) - start");
    UnidadMedida returnValue = service.findById(id);
    log.debug("one(Long id) - end");
    return returnValue;
  }

}
