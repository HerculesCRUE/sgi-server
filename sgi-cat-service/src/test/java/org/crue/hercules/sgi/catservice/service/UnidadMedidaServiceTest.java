package org.crue.hercules.sgi.catservice.service;

import static org.mockito.ArgumentMatchers.eq;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.crue.hercules.sgi.catservice.exceptions.UnidadMedidaNotFoundException;
import org.crue.hercules.sgi.catservice.filter.UnidadMedidaFilter;
import org.crue.hercules.sgi.catservice.model.UnidadMedida;
import org.crue.hercules.sgi.catservice.repository.UnidadMedidaRepository;
import org.crue.hercules.sgi.catservice.service.impl.UnidadMedidaServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

/**
 * UnidadMedidaServiceTest
 */
@ExtendWith(MockitoExtension.class)
public class UnidadMedidaServiceTest {

  @Mock
  private UnidadMedidaRepository unidadMedidaRepository;

  private UnidadMedidaService unidadMedidaService;

  @BeforeEach
  public void setUp() throws Exception {
    unidadMedidaService = new UnidadMedidaServiceImpl(unidadMedidaRepository);
  }

  @Test
  public void create_ReturnsUnidadMedida() {

    UnidadMedida response = new UnidadMedida(1L, "UM1", "UnidadMedida1");

    BDDMockito.given(unidadMedidaRepository.findById(response.getId())).willReturn(Optional.empty());

    Assertions.assertThatThrownBy(() -> unidadMedidaService.findById(response.getId()))
        .isInstanceOf(UnidadMedidaNotFoundException.class);

    Assertions.assertThatThrownBy(() -> unidadMedidaService.create(response))
        .isInstanceOf(IllegalArgumentException.class);

    response.setId(null);
    BDDMockito.given(unidadMedidaRepository.save(response)).willReturn(response);
    UnidadMedida result = unidadMedidaService.create(response);

    Assertions.assertThat(result).isEqualTo(response);
  }

  @Test
  public void update_ReturnsUnidadMedida() {

    UnidadMedida response = new UnidadMedida(1L, "UM01", "UnidadMedida01");

    BDDMockito.given(unidadMedidaRepository.save(response)).willReturn(response);

    Assertions.assertThatThrownBy(() -> unidadMedidaService.findById(response.getId()))
        .isInstanceOf(UnidadMedidaNotFoundException.class);

    BDDMockito.given(unidadMedidaRepository.findById(response.getId())).willReturn(Optional.of(response));

    UnidadMedida result = unidadMedidaService.update(response);
    Assertions.assertThat(result).isEqualTo(response);

    response.setId(null);
    Assertions.assertThatThrownBy(() -> unidadMedidaService.update(response))
        .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  public void delete_WhithId_DoNotGetUnidadMedida() {

    UnidadMedida response = new UnidadMedida(1L, "UM1", "UnidadMedida1");

    BDDMockito.given(unidadMedidaRepository.findById(response.getId())).willReturn(Optional.of(response));

    UnidadMedida result = unidadMedidaService.findById(response.getId());
    Assertions.assertThat(result).isEqualTo(response);

    BDDMockito.doNothing().when(unidadMedidaRepository).deleteById(response.getId());
    BDDMockito.given(unidadMedidaRepository.findById(response.getId())).willReturn(Optional.empty());

    unidadMedidaService.delete(1L);

    Assertions.assertThatThrownBy(() -> unidadMedidaService.findById(response.getId()))
        .isInstanceOf(UnidadMedidaNotFoundException.class);

    BDDMockito.doThrow(IllegalArgumentException.class).when(unidadMedidaRepository).deleteById(null);
    Assertions.assertThatThrownBy(() -> unidadMedidaService.delete(null)).isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  public void findAll_ReturnUnidadMedidaList() {

    List<UnidadMedida> response = new LinkedList<UnidadMedida>();
    response.add(new UnidadMedida(1L, "UM1", "UnidadMedida1"));
    response.add(new UnidadMedida(2L, "UM2", "UnidadMedida2"));

    BDDMockito.given(unidadMedidaRepository.findAll()).willReturn(response);

    List<UnidadMedida> result = unidadMedidaService.findAll();

    Assertions.assertThat(result).isEqualTo(response);
  }

  @Test
  public void findAll_ReturnUnidadMedidaPage() {

    List<UnidadMedida> response = new LinkedList<UnidadMedida>();
    response.add(new UnidadMedida(3L, "UM3", "UnidadMedida3"));
    Page<UnidadMedida> pageResponse = new PageImpl<UnidadMedida>(response);

    BDDMockito.given(unidadMedidaRepository.findAll(PageRequest.of(1, 2, Sort.unsorted()))).willReturn(pageResponse);

    Page<UnidadMedida> result = unidadMedidaService.findAll(PageRequest.of(1, 2, Sort.unsorted()));

    Assertions.assertThat(result).isEqualTo(pageResponse);
  }

  @Test
  public void findAllFilter_ReturnUnidadMedidaPage() {

    List<UnidadMedida> response = new LinkedList<UnidadMedida>();
    response.add(new UnidadMedida(1L, "UM1", "UnidadMedida1"));
    response.add(new UnidadMedida(2L, "UM2", "UnidadMedida2"));
    response.add(new UnidadMedida(3L, "UM3", "UnidadMedida3"));
    UnidadMedidaFilter filter = new UnidadMedidaFilter();
    filter.setAbreviatura("UM");
    Page<UnidadMedida> pageResponse = new PageImpl<UnidadMedida>(response);

    BDDMockito.given(unidadMedidaRepository.findAll(ArgumentMatchers.<Specification<UnidadMedida>>any(),
        eq(PageRequest.of(1, 2, Sort.unsorted())))).willReturn(pageResponse);

    Page<UnidadMedida> result = unidadMedidaService.findAllLike(filter, PageRequest.of(1, 2, Sort.unsorted()));

    Assertions.assertThat(result).isEqualTo(pageResponse);
  }

  @Test
  public void find_WithId_ReturnsUnidadMedida() {

    UnidadMedida response = new UnidadMedida(1L, "UM1", "UnidadMedida1");

    BDDMockito.given(unidadMedidaRepository.findById(response.getId())).willReturn(Optional.of(response));

    UnidadMedida result = unidadMedidaService.findById(response.getId());

    Assertions.assertThat(result).isEqualTo(response);
  }

  @Test
  public void find_WithAbreviatura_ReturnsUnidadMedida() {

    UnidadMedida response = new UnidadMedida(1L, "UM1", "UnidadMedida1");

    BDDMockito.given(unidadMedidaRepository.findByAbreviatura(response.getAbreviatura()))
        .willReturn(Optional.of(response));

    UnidadMedida result = unidadMedidaService.findByAbreviatura(response.getAbreviatura());

    Assertions.assertThat(result).isEqualTo(response);
  }

  @Test
  public void find_NotFound_ThrowsUnidadMedidaNotFoundException() throws Exception {

    BDDMockito.given(unidadMedidaRepository.findById(1L)).willReturn(Optional.empty());

    Assertions.assertThatThrownBy(() -> unidadMedidaService.findById(1L))
        .isInstanceOf(UnidadMedidaNotFoundException.class);
  }

}