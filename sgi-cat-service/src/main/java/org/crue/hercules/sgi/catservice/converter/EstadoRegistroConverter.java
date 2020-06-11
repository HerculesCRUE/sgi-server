
package org.crue.hercules.sgi.catservice.converter;

import java.util.stream.Stream;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import org.crue.hercules.sgi.catservice.enums.EstadoRegistroEnum;

@Converter(autoApply = true)
public class EstadoRegistroConverter implements AttributeConverter<EstadoRegistroEnum, String> {

  /**
   * Convierte el enumerado a string
   * 
   * @param estado {@link EstadoRegistroEnum}
   * @return el valor del enumerado en string
   */
  @Override
  public String convertToDatabaseColumn(EstadoRegistroEnum estado) {
    if (estado == null) {
      return null;
    }
    return estado.getValue();
  }

  /**
   * Convierte el string en el enumerado
   * 
   * @param estado el string
   * @return EstadoRegistroEnum
   */
  @Override
  public EstadoRegistroEnum convertToEntityAttribute(String estado) {
    if (estado == null) {
      return null;
    }
    return Stream.of(EstadoRegistroEnum.values()).filter(c -> c.getValue().equals(estado)).findFirst()
        .orElseThrow(IllegalArgumentException::new);

  }
}