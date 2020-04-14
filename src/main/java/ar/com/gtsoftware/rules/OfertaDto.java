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

package ar.com.gtsoftware.rules;


import ar.com.gtsoftware.dto.domain.SucursalesDto;
import lombok.*;
import org.apache.commons.collections4.CollectionUtils;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OfertaDto {

    @EqualsAndHashCode.Include
    private Long id;

    @Size(max = 90)
    @NotNull
    private String textoOferta;

    private List<Condicion> condiciones;

    @NotNull
    private TipoAccion tipoAccion;

    @NotNull
    private BigDecimal descuento;

    private SucursalesDto idSucursal;

    @NotNull
    private Date vigenciaDesde;

    @NotNull
    private Date vigenciaHasta;

    private int version;


}
