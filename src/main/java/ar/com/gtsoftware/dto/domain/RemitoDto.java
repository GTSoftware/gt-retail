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

import java.time.LocalDateTime;
import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.*;
import org.apache.commons.lang3.StringUtils;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RemitoDto {

  private static final long serialVersionUID = 1L;

  @EqualsAndHashCode.Include private Long id;
  private LocalDateTime fechaAlta;

  @NotNull private UsuariosDto idUsuario;

  private String observaciones;

  @NotNull private Boolean isOrigenInterno;

  private PersonasDto idOrigenExterno;

  private DepositosDto idOrigenInterno;

  @NotNull private Boolean isDestinoInterno;

  private PersonasDto idDestinoPrevistoExterno;

  private DepositosDto idDestinoPrevistoInterno;

  private LocalDateTime fechaCierre;

  @NotNull private RemitoTipoMovimientoDto remitoTipoMovimiento;

  private List<RemitoDetalleDto> detalleList;

  private List<RemitoRecepcionDto> remitoRecepcionesList;
  private Integer version;

  /**
   * Devuelve la representación en Stirng del origen del remitoDtoCabecera.
   *
   * @return un String que representa el origen
   */
  public String getNombreOrigen() {
    if (isOrigenInterno) {
      return String.format("INTERNO: %s", idOrigenInterno.toString());
    }
    return String.format("EXTERNO: %s", idOrigenExterno.toString());
  }

  /**
   * Devuelve la representación en Stirng del destino del remitoDtoCabecera.
   *
   * @return un String que representa el destino
   */
  public String getNombreDestino() {
    if (isDestinoInterno) {
      return String.format("INTERNO: %s", idDestinoPrevistoInterno.toString());
    }
    return String.format(
        "EXTERNO: %s Dirección: %s %s Piso: %s Depto: %s (%s) %s - %s",
        idDestinoPrevistoExterno.toString(),
        StringUtils.defaultString(idDestinoPrevistoExterno.getCalle()),
        StringUtils.defaultString(idDestinoPrevistoExterno.getAltura()),
        StringUtils.defaultString(idDestinoPrevistoExterno.getPiso(), "-"),
        StringUtils.defaultString(idDestinoPrevistoExterno.getDepto(), "-"),
        idDestinoPrevistoExterno.getIdLocalidad().getCodigoPostal(),
        idDestinoPrevistoExterno.getIdLocalidad().getNombreLocalidad(),
        idDestinoPrevistoExterno.getIdProvincia().getNombreProvincia());
  }
}
