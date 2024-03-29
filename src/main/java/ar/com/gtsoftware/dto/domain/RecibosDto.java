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
import java.util.Date;
import java.util.List;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.*;

/**
 * @author Rodrigo M. Tato Rothamel mailto:rotatomel@gmail.com
 */
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecibosDto {

  private static final long serialVersionUID = 1L;

  @EqualsAndHashCode.Include private Long id;
  @NotNull private Date fechaRecibo;

  @NotNull private PersonasDto idPersona;

  @NotNull private UsuariosDto idUsuario;

  @NotNull private BigDecimal montoTotal;

  @Size(max = 255)
  private String observaciones;

  @NotNull private CajasDto idCaja;

  private List<RecibosDetalleDto> recibosDetalles;
  private Integer version;
}
