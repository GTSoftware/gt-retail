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
import lombok.*;

/**
 * Clase que almacena la información de las facturas del libro de iva compras
 *
 * @author Rodrigo Tato <rotatomel@gmail.com>
 */
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FiscalLibroIvaComprasDto {

  private static final long serialVersionUID = 1L;

  @EqualsAndHashCode.Include private Long id;
  private LocalDateTime fechaFactura;
  private String documento;
  private String letraFactura;
  private String puntoVentaFactura;
  private String numeroFactura;
  private boolean anulada;
  private BigDecimal totalFactura;

  private FiscalTiposComprobanteDto codigoTipoComprobante;

  private List<FiscalLibroIvaComprasLineasDto> fiscalLibroIvaComprasLineasList;
  private PersonasDto idPersona;
  private FiscalResponsabilidadesIvaDto idResponsabilidadIva;
  private FiscalPeriodosFiscalesDto idPeriodoFiscal;

  private Long cae;
  private LocalDateTime fechaVencimientoCae;

  private BigDecimal importeNetoNoGravado;
  private BigDecimal importeExento;
  private BigDecimal importeNetoGravado;
  private BigDecimal importeTributos;
  private BigDecimal importeIva;
  private BigDecimal importePercepcionIva;
  private BigDecimal importePercepcionIngresosBrutos;

  private Integer version;
}
