package org.crue.hercules.sgi.catservice.controller;

import java.util.List;

import javax.validation.Valid;

import org.crue.hercules.sgi.catservice.filter.TipoFungibleFilter;
import org.crue.hercules.sgi.catservice.model.TipoFungible;
import org.crue.hercules.sgi.catservice.service.TipoFungibleService;
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
 * TipoFungibleController
 */
@RestController
@RequestMapping(ConstantesCat.TIPO_FUNGIBLE_CONTROLLER_BASE_PATH)
@Slf4j
public class TipoFungibleController {

  /** TipoFungible service */
  private TipoFungibleService service;

  /**
   * Instancia un nuevo TipoFungibleController.
   * 
   * @param service TipoFungibleService
   */
  public TipoFungibleController(TipoFungibleService service) {
    log.debug("TipoFungibleController(TipoFungibleService service) - start");
    this.service = service;
    log.debug("TipoFungibleController(TipoFungibleService service) - end");
  }

  /**
   * Devuelve la lista completa de TipoFungible.
   * 
   * @return La lista con todos los TipoFungible.
   */
  @GetMapping(ConstantesCat.PATH_ALL)
  List<TipoFungible> all() {
    log.debug("all() - start");
    List<TipoFungible> returnValue = service.findAll();
    log.debug("all() - end");
    return returnValue;
  }

  /**
   * Devuelve todas las entidades TipoFungible paginadas.
   * 
   * @param pageable TipoFungibleService
   * 
   * @return La lista con todos los tipoFungible
   */
  @GetMapping(ConstantesCat.PATH_ALL + ConstantesCat.PATH_PAGE)
  Page<TipoFungible> all(@PageableDefault(value = 10, page = 0) Pageable pageable) {
    log.debug("all(Pageable pageable) - start");
    Page<TipoFungible> returnValue = service.findAll(pageable);
    log.debug("all(Pageable pageable) - end");
    return returnValue;
  }

  /**
   * Devuelve todas las entidades TipoFungible que cumplen el filtro paginadas
   * 
   * @return Lista de todas las entidades TipoFungible filtradas por página
   */
  @GetMapping(ConstantesCat.PATH_ALL + ConstantesCat.PATH_PAGEFILTERED)
  Page<TipoFungible> all(@ModelAttribute TipoFungibleFilter filter,
      @PageableDefault(value = 10, page = 0) Pageable pageable) {
    log.debug("all(TipoFungibleFilter filter, Pageable pageable) - start");
    Page<TipoFungible> listTipoFungible = service.findAllLike(filter, pageable);
    log.debug("all(TipoFungible filter, Pageable pageable) - end");
    return listTipoFungible;
  }

  /**
   * Crea nuevo TipoFungible
   * 
   * @param nuevoTipoFungible TipoFungible que se quiere crear
   * @return Nuevo TipoFungible creado
   */
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  TipoFungible newTipoFungible(@Valid @RequestBody TipoFungible nuevoTipoFungible) {
    log.debug("newTipoFungible(TipoFungible nuevoTipoFungible) - start");
    TipoFungible returnValue = service.create(nuevoTipoFungible);
    log.debug("newTipoFungible(TipoFungible nuevoTipoFungible) - end");
    return returnValue;
  }

  /**
   * Actualiza TipoFungible.
   * 
   * @param updatedTipoFungible TipoFungible a actualizar
   * @param id                  id TipoFungible a actualizar.
   * @return TipoFungible actualizado.
   */
  @PutMapping(ConstantesCat.PATH_PARAMETER_ID)
  TipoFungible replaceTipoFungible(@Valid @RequestBody TipoFungible updatedTipoFungible, @PathVariable Long id) {
    log.debug("replaceTipoFungible(TipoFungible updatedTipoFungible, Long id) - start");
    updatedTipoFungible.setId(id);
    TipoFungible returnValue = service.update(updatedTipoFungible);
    log.debug("replaceTipoFungible(TipoFungible updatedTipoFungible, Long id) - end");
    return returnValue;
  }

  /**
   * Devuelve el TipoFungible con el id indicado
   * 
   * @param id Identificador de TipoFungible
   * @return TipoFungible correspondiente al id
   */
  @GetMapping(ConstantesCat.PATH_PARAMETER_ID)
  private TipoFungible one(@PathVariable Long id) {
    log.debug("one(Long id) - start");
    TipoFungible returnValue = service.findById(id);
    log.debug("one(Long id) - end");
    return returnValue;
  }

  /**
   * Elimina TipoFungible con id indicado
   * 
   * @param id Identificador de TipoFungible
   */
  @DeleteMapping(ConstantesCat.PATH_PARAMETER_ID)
  void delete(@PathVariable Long id) {
    log.debug("deleteTipoFungible(Long id) - start");
    service.delete(id);
    log.debug("deleteTipoFungible(Long id) - end");
  }
}