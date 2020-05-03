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

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DepositosDto {

    private static final long serialVersionUID = 1L;
    private static final String BUSINESS_STRING = "[%d] %s Suc: %s";

    @EqualsAndHashCode.Include
    private Long id;
    private String nombreDeposito;
    private String direccion;
    private Date fechaAlta;
    private boolean activo;
    private UbicacionProvinciasDto idProvincia;
    private UbicacionPaisesDto idPais;
    private UbicacionLocalidadesDto idLocalidad;
    private SucursalesDto idSucursal;
    private Integer version;

    @Override
    public String toString() {
        return String.format(BUSINESS_STRING, id, nombreDeposito, idSucursal.toString());
    }

}
