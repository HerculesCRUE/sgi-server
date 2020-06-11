package org.crue.hercules.sgi.catservice.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.crue.hercules.sgi.catservice.enums.EstadoRegistroEnum;
import org.crue.hercules.sgi.catservice.exceptions.RegistroNotFoundException;
import org.crue.hercules.sgi.catservice.filter.RegistroFilter;
import org.crue.hercules.sgi.catservice.model.Seccion;
import org.crue.hercules.sgi.catservice.model.Servicio;
import org.crue.hercules.sgi.catservice.model.Registro;
import org.crue.hercules.sgi.catservice.repository.RegistroRepository;
import org.crue.hercules.sgi.catservice.service.impl.RegistroServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

/**
 * RegistroServiceTest
 */
@ExtendWith(MockitoExtension.class)
public class RegistroServiceTest {

  @Mock
  private RegistroRepository registroRepository;

  private RegistroService registroServicio;

  @BeforeEach
  public void setUp() throws Exception {
    registroServicio = new RegistroServiceImpl(registroRepository);
  }

  @Test
  public void create_ReturnsRegistro() {
    // given: Un nuevo registro
    Seccion seccion = new Seccion(1L, "Seccion 1", "Seccion create test");
    Servicio servicio = new Servicio(1L, "Registro 1", "Serv1", "Nombre Apellidos", seccion);

    Registro nuevoRegistro = new Registro(null, "user-998", servicio, EstadoRegistroEnum.ACTIVO, Boolean.TRUE,
        Boolean.FALSE, "nuevo registro");
    BDDMockito.given(registroRepository.save(nuevoRegistro)).will((InvocationOnMock invocation) -> {
      Registro registroCreado = invocation.getArgument(0);
      registroCreado.setId(1L);
      return registroCreado;
    });
    // when: Creamos el registro
    Registro registroCreado = registroServicio.create(nuevoRegistro);

    // then: La servicio se crea correctamente.
    Assertions.assertThat(registroCreado.getId()).isEqualTo(1L);
    Assertions.assertThat(registroCreado.getUsuarioRef()).isEqualTo("user-998");
    Assertions.assertThat(registroCreado.getEstado()).isEqualTo(EstadoRegistroEnum.ACTIVO);
    Assertions.assertThat(registroCreado.getEntregaPapel()).isTrue();
    Assertions.assertThat(registroCreado.getAceptaCondiciones()).isFalse();
    Assertions.assertThat(registroCreado.getServicio().getId()).isEqualTo(1L);
    Assertions.assertThat(registroCreado.getObservaciones()).isEqualTo("nuevo registro");
  }

  @Test
  public void create_RegistroWithId_ThrowsIllegalArgumentException() {
    // given: Un nuevo registro que ya tiene id
    Seccion seccion = new Seccion(1L, "Seccion 1", "Seccion create test");
    Servicio servicio = new Servicio(1L, "Registro 1", "Serv1", "Nombre Apellidos", seccion);

    Registro nuevoRegistro = new Registro(1L, "user-998", servicio, EstadoRegistroEnum.ACTIVO, Boolean.TRUE,
        Boolean.FALSE, "nuevo registro");

    // when: Creamos el servicio
    // then: Lanza una excepcion porque el servicio ya tiene id
    Assertions.assertThatThrownBy(() -> registroServicio.create(nuevoRegistro))
        .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  public void update_ReturnsRegistro() {
    // given: Una nueva registro con el usuario ref y aceptar condiciones
    // actualizado
    Seccion seccion = new Seccion(1L, "Seccion 1", "Seccion create test");
    Servicio servicio = new Servicio(1L, "Registro 1", "Serv1", "Nombre Apellidos", seccion);

    Registro registroActualizado = new Registro(1L, "user-555", servicio, EstadoRegistroEnum.ACTIVO, Boolean.TRUE,
        Boolean.TRUE, "nuevo registro");
    Registro registro = new Registro(1L, "user-998", servicio, EstadoRegistroEnum.ACTIVO, Boolean.TRUE, Boolean.FALSE,
        "nuevo registro");

    BDDMockito.given(registroRepository.findById(1L)).willReturn(Optional.of(registro));
    BDDMockito.given(registroRepository.save(registro)).willReturn(registroActualizado);

    // when: Actualizamos el registro
    Registro registroActualizadoBD = registroServicio.update(registro);

    // then: El registro se actualiza correctamente.
    Assertions.assertThat(registroActualizadoBD.getId()).isEqualTo(1L);
    Assertions.assertThat(registroActualizadoBD.getUsuarioRef()).isEqualTo("user-555");
    Assertions.assertThat(registroActualizadoBD.getAceptaCondiciones()).isTrue();
    Assertions.assertThat(registroActualizadoBD.getEntregaPapel()).isTrue();
    Assertions.assertThat(registroActualizadoBD.getObservaciones()).isEqualTo("nuevo registro");
    Assertions.assertThat(registroActualizadoBD.getServicio().getId()).isEqualTo(1L);
  }

  @Test
  public void update_ThrowsRegistroNotFoundException() {
    // given: Un nuevo servicio a actualizar
    Seccion seccion = new Seccion(1L, "Seccion 1", "Seccion create test");
    Servicio servicio = new Servicio(1L, "Registro 1", "Serv1", "Nombre Apellidos1", seccion);

    Registro registro = new Registro(1L, "user-555", servicio, EstadoRegistroEnum.ACTIVO, Boolean.TRUE, Boolean.TRUE,
        "nuevo registro");

    // when: actualizamos el servicio
    // then: Lanza una excepcion porque el servicio no existe
    Assertions.assertThatThrownBy(() -> registroServicio.update(registro))
        .isInstanceOf(RegistroNotFoundException.class);

  }

  @Test
  public void find_WithId_ReturnsRegistro() {

    // given: Dado un servicio con id 1
    Seccion seccion = new Seccion(1L, "Seccion 1", "Seccion create test");
    Servicio servicio = new Servicio(1L, "Registro 1", "Serv1", "Nombre Apellidos", seccion);

    Registro registro = new Registro(1L, "user-555", servicio, EstadoRegistroEnum.ACTIVO, Boolean.TRUE, Boolean.TRUE,
        "registro 1 user-555");

    BDDMockito.given(registroRepository.findById(1L)).willReturn(Optional.of(registro));

    Registro registroFind = registroServicio.findById(1L);

    Assertions.assertThat(registroFind.getId()).isEqualTo(1L);
    Assertions.assertThat(registroFind.getUsuarioRef()).isEqualTo("user-555");
    Assertions.assertThat(registroFind.getAceptaCondiciones()).isTrue();
    Assertions.assertThat(registroFind.getEntregaPapel()).isTrue();
    Assertions.assertThat(registroFind.getObservaciones()).isEqualTo("registro 1 user-555");
    Assertions.assertThat(registroFind.getServicio().getId()).isEqualTo(1L);

  }

  @Test
  public void find_NotFound_ThrowsRegistroNotFoundException() throws Exception {
    BDDMockito.given(registroRepository.findById(1L)).willReturn(Optional.empty());

    Assertions.assertThatThrownBy(() -> registroServicio.findById(1L)).isInstanceOf(RegistroNotFoundException.class);
  }

  @Test
  public void findAll_ReturnRegistroList() {
    // given: dos registros
    Seccion seccion = new Seccion(1L, "Seccion 1", "Seccion create test");
    Servicio servicio = new Servicio(1L, "Registro 1", "Serv1", "Nombre Apellidos", seccion);

    Registro registro = new Registro(1L, "user-555", servicio, EstadoRegistroEnum.ACTIVO, Boolean.TRUE, Boolean.TRUE,
        "registro 1 user-555");
    Registro registro2 = new Registro(2L, "user-458", servicio, EstadoRegistroEnum.ACTIVO, Boolean.TRUE, Boolean.TRUE,
        "registro 2 user-458");

    List<Registro> servicioResponseList = new ArrayList<Registro>();
    servicioResponseList.add(registro);
    servicioResponseList.add(registro2);
    Page<Registro> pagedResponse = new PageImpl<Registro>(servicioResponseList);
    BDDMockito.given(registroRepository.findAll(PageRequest.of(0, 5, Sort.unsorted()))).willReturn(pagedResponse);

    // when: Buscamos los registros por id
    Pageable pageable = PageRequest.of(0, 5, Sort.unsorted());

    Page<Registro> serviciosEncontradas = registroServicio.findAll(pageable);

    // then: Recuperamos los registros
    Assertions.assertThat(serviciosEncontradas.getContent().size()).isEqualTo(2);
    Assertions.assertThat(serviciosEncontradas.getContent().containsAll(Arrays.asList(registro, registro2)));

  }

  @Test
  public void findAll_ReturnEmptyList() {
    // given: no hay registros

    Page<Registro> pagedResponse = new PageImpl<Registro>(Collections.emptyList());
    BDDMockito.given(registroRepository.findAll(PageRequest.of(0, 5, Sort.unsorted()))).willReturn(pagedResponse);

    // when: Buscamos los registros
    Pageable pageable = PageRequest.of(0, 5, Sort.unsorted());

    Page<Registro> registrosEncontrados = registroServicio.findAll(pageable);

    // then: Recuperamos los registros
    Assertions.assertThat(registrosEncontrados.getContent().isEmpty());
  }

  @Test
  public void findAllLike_ReturnRegistroList() {
    // given: dos registros
    Seccion seccion = new Seccion(1L, "Seccion 1", "Seccion create test");
    Servicio servicio = new Servicio(1L, "Registro 1", "Serv1", "Nombre Apellidos", seccion);

    Registro registro = new Registro(1L, "user-555", servicio, EstadoRegistroEnum.ACTIVO, Boolean.TRUE, Boolean.TRUE,
        "registro 1 user-555");
    Registro registro2 = new Registro(2L, "user-458", servicio, EstadoRegistroEnum.ACTIVO, Boolean.TRUE, Boolean.TRUE,
        "registro 2 user-458");

    List<Registro> registroResponseList = new ArrayList<Registro>();
    registroResponseList.add(registro);
    registroResponseList.add(registro2);
    Page<Registro> pagedResponse = new PageImpl<Registro>(registroResponseList);

    BDDMockito.given(
        registroRepository.findAll(ArgumentMatchers.<Specification<Registro>>any(), ArgumentMatchers.<Pageable>any()))
        .willReturn(pagedResponse);

    // when: Buscamos los registros por estado del registro
    Pageable pageable = PageRequest.of(0, 5, Sort.unsorted());

    RegistroFilter filter = new RegistroFilter();
    filter.setEstadoRegistro(EstadoRegistroEnum.ACTIVO);
    Page<Registro> registrosEncontrados = registroServicio.findAllLike(filter, pageable);

    // then: Recuperamos los registros
    Assertions.assertThat(registrosEncontrados.getContent().size()).isEqualTo(2);
    Assertions.assertThat(registrosEncontrados.getContent().containsAll(Arrays.asList(registro, registro2)));

  }

  @Test
  public void findAllLike_ReturnEmptyList() {
    // given: no hay registros

    Page<Registro> pagedResponse = new PageImpl<Registro>(Collections.emptyList());
    BDDMockito.given(
        registroRepository.findAll(ArgumentMatchers.<Specification<Registro>>any(), ArgumentMatchers.<Pageable>any()))
        .willReturn(pagedResponse);

    // when: Buscamos los registros por usuario ref
    Pageable pageable = PageRequest.of(0, 5, Sort.unsorted());

    RegistroFilter filter = new RegistroFilter();
    filter.setUsuarioRef("user");
    Page<Registro> registrosEncontrados = registroServicio.findAllLike(filter, pageable);

    // then: Recuperamos las unidades medida
    Assertions.assertThat(registrosEncontrados.getContent().isEmpty());
  }

  @Test
  public void findAll_WithoutPageable_ReturnRegistroList() {
    // given: dos registros

    Seccion seccion = new Seccion(1L, "Seccion 1", "Seccion create test");
    Servicio servicio = new Servicio(1L, "Registro 1", "Serv1", "Nombre Apellidos", seccion);

    Registro registro = new Registro(1L, "user-555", servicio, EstadoRegistroEnum.ACTIVO, Boolean.TRUE, Boolean.TRUE,
        "registro 1 user-555");
    Registro registro2 = new Registro(2L, "user-458", servicio, EstadoRegistroEnum.ACTIVO, Boolean.TRUE, Boolean.TRUE,
        "registro 2 user-458");

    List<Registro> servicioResponseList = new ArrayList<Registro>();
    servicioResponseList.add(registro);
    servicioResponseList.add(registro2);

    BDDMockito.given(registroRepository.findAll()).willReturn(servicioResponseList);

    // when: Buscamos todos los registros
    List<Registro> registrosEncontrados = registroServicio.findAll();

    // then: Recuperamos los registros
    Assertions.assertThat(registrosEncontrados.size()).isEqualTo(2);
    Assertions.assertThat(registrosEncontrados.containsAll(Arrays.asList(registro, registro2)));

  }

  @Test
  public void findAllLike_WithoutPageable_ReturnEmptyList() {
    // given: no hay registros
    BDDMockito.given(registroRepository.findAll()).willReturn(Collections.emptyList());

    List<Registro> registrosEncontrados = registroServicio.findAll();

    // then: Recuperamos los registros
    Assertions.assertThat(registrosEncontrados.isEmpty());
  }

  @Test
  public void delete_Success() {
    registroServicio.delete(ArgumentMatchers.<Long>any());
  }
}