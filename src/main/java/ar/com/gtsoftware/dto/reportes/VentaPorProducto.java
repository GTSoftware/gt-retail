/*
 * Copyright 2019 GT Software.
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

package ar.com.gtsoftware.dto.reportes;

import java.io.Serializable;
import java.math.BigDecimal;
import lombok.*;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VentaPorProducto implements Serializable {

  @EqualsAndHashCode.Include private Long idProducto;
  private String codigoProducto;
  private String codigoFabrica;
  private String descripcionProducto;
  private String unidadVenta;
  private BigDecimal stockMinimo;
  private BigDecimal stockTotalActual;
  private BigDecimal cantidadVendida;
  private BigDecimal costoTotalVentas;
  private BigDecimal totalAPrecioVenta;
  private Integer cantidadComprobantes;
  private String proveedor;
}
