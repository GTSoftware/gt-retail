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

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import javax.validation.constraints.Pattern;
import lombok.*;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductosDto {

  @EqualsAndHashCode.Include private Long id;

  @Pattern(
      regexp = "([A-Z]|[0-9]|/|-|_|\\.\\S)+",
      message = "El código no puede tener caracteres extraños ni espacios")
  private String codigoPropio;

  private String descripcion;
  private String observaciones;
  private LocalDateTime fechaAlta;
  private LocalDateTime fechaUltimaModificacion;
  private boolean activo;
  private BigDecimal costoAdquisicionNeto;

  private BigDecimal costoFinal;

  private int annosAmortizacion;
  private BigDecimal unidadesCompraUnidadesVenta;
  private ProductosTiposUnidadesDto idTipoUnidadVenta;
  private ProductosTiposUnidadesDto idTipoUnidadCompra;
  private ProductosTiposProveeduriaDto idTipoProveeduria;
  private ProductosSubRubrosDto idSubRubro;
  private ProductosRubrosDto idRubro;

  private PersonasDto idProveedorHabitual;

  private FiscalAlicuotasIvaDto idAlicuotaIva;
  private ProductosMarcasDto idMarca;

  private List<ProductosPreciosDto> precios;

  private List<ProductosPorcentajesDto> porcentajes;

  private String ubicacion;

  private String codigoFabricante;

  private BigDecimal stockMinimo;

  private BigDecimal precioVenta;

  private BigDecimal stockActual;

  private BigDecimal stockActualEnSucursal;

  private Integer version;
}
