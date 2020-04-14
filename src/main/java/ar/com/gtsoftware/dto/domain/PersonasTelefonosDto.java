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

package ar.com.gtsoftware.dto.domain;

import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PersonasTelefonosDto {

    @EqualsAndHashCode.Include
    private Long id;
    @Pattern(regexp = "0\\d{2,4}\\s\\d{6,9}",
            message = "Número de teléfono no válido, debe tener el formato: 0123 123456789")
    @NotNull
    @Size(min = 1, max = 50)
    private String numero;
    @Size(max = 100)
    private String referencia;
//    @NotNull
//    private PersonasDto idPersona;
    private Integer version;
}
