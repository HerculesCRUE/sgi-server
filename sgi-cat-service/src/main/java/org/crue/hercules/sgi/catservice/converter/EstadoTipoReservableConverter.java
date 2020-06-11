
package org.crue.hercules.sgi.catservice.converter;

import java.util.stream.Stream;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import org.crue.hercules.sgi.catservice.enums.EstadoTipoReservableEnum;

@Converter(autoApply = true)
public class EstadoTipoReservableConverter implements AttributeConverter<EstadoTipoReservableEnum, String> {

  /**
   * Convierte el enumerado a string
   * 
   * @param estado {@link EstadoTipoReservableEnum}
   * @return el valor del enumerado en string
   */
  @Override
  public String convertToDatabaseColumn(EstadoTipoReservableEnum estado) {
    if (estado == null) {
      return null;
    }
    return estado.getValue();
  }

  /**
   * Convierte el string en el enumerado
   * 
   * @param estado el string
   * @return EstadoTipoReservableEnum
   */
  @Override
  public EstadoTipoReservableEnum convertToEntityAttribute(String estado) {
    if (estado == null) {
      return null;
    }
    return Stream.of(EstadoTipoReservableEnum.values()).filter(c -> c.getValue().equals(estado)).findFirst()
        .orElseThrow(IllegalArgumentException::new);

  }
}