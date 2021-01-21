/*
 * Copyright 2018 GT Software.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ar.com.gtsoftware.search;

import java.time.LocalDateTime;
import java.util.List;
import lombok.*;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

/** @author Rodrigo Tato <rotatomel@gmail.com> */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ComprobantesProveedorSearchFilter extends AbstractSearchFilter {

  private static final long serialVersionUID = 1L;

  private Long idComprobante;
  private LocalDateTime fechaComprobanteDesde;
  private LocalDateTime fechaComprobanteHasta;
  private Boolean anulada;
  private Long idUsuario;
  private Long idProveedor;
  private Long idSucursal;
  private Boolean registradaEnLibroIVA;
  private String numeroFactura;
  private List<Long> idTiposComprobanteList;

  @Override
  public boolean hasFilter() {
    return idComprobante != null
        || hasFechasFilter()
        || anulada != null
        || idUsuario != null
        || idSucursal != null
        || idProveedor != null
        || registradaEnLibroIVA != null
        || StringUtils.isNotEmpty(numeroFactura)
        || hasTiposComprobanteFilter();
  }

  public boolean hasTiposComprobanteFilter() {
    return CollectionUtils.isNotEmpty(idTiposComprobanteList);
  }

  public boolean hasFechasFilter() {
    return fechaComprobanteDesde != null && fechaComprobanteHasta != null;
  }
}
