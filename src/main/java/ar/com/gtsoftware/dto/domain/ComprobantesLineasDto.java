/*
 * Copyright 2018 GT Software.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package ar.com.gtsoftware.dto.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import lombok.*;
import org.apache.commons.lang3.StringUtils;

/**
 * @author Rodrigo Tato <rotatomel@gmail.com>
 */
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ComprobantesLineasDto implements Serializable {

  @EqualsAndHashCode.Include private Long id;
  private BigDecimal precioUnitario;
  private BigDecimal cantidad;
  private BigDecimal subTotal;
  private BigDecimal costoNetoUnitario;
  private BigDecimal costoBrutoUnitario;
  private BigDecimal cantidadEntregada;
  private ComprobantesDto idComprobante;
  private ProductosDto idProducto;
  private String descripcion;
  private Integer version;
  private Integer item;

  /**
   * Retorna la descripción imprimible o mostrable hasta los 90 caracteres.
   *
   * @return
   */
  public String getDescripcionLinea() {
    return StringUtils.left(
        String.format("[%s] %s", idProducto.getCodigoPropio(), descripcion), 90);
  }

  public BigDecimal getIva() {
    return this.idProducto.getIdAlicuotaIva().getValorAlicuota();
  }
}
