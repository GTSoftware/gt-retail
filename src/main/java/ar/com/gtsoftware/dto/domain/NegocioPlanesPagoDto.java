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

import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NegocioPlanesPagoDto {

    @EqualsAndHashCode.Include
    private Long id;


    private String nombre;

    private Date fechaActivoDesde;
    private Date fechaActivoHasta;
    private Integer version;

    private List<NegocioPlanesPagoDetalleDto> negocioPlanesPagoDetalles;


    @Override
    public String toString() {
        return "[" + id + "] " + nombre;
    }
}
