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

import ar.com.gtsoftware.enums.TiposPuntosVenta;
import lombok.*;

/**
 * Representa a los puntos de venta fiscales asignados a cada sucursal
 *
 * @author Rodrigo M. Tato Rothamel mailto:rotatomel@gmail.com
 */
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FiscalPuntosVentaDto {

  @EqualsAndHashCode.Include private Integer nroPuntoVenta;

  private boolean activo;

  private String descripcion;

  private TiposPuntosVenta tipo;

  private SucursalesDto sucursal;
  private Integer version;

  //
  //    @XmlTransient
  //    public String getBusinessString() {
  //        return String.format("%s - %s", StringUtils.leftPad(String.valueOf(nroPuntoVenta), 4,
  // '0'), tipo);
  //    }

}
