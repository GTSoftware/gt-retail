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

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

/**
 * @author Rodrigo Tato <rotatomel@gmail.com>
 */
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PersonasDto {

    private static final long serialVersionUID = 1L;

    @EqualsAndHashCode.Include
    private Long id;

    private static final String BUSINESS_STRING = "[%d] %s - %s: %s";

    @Pattern(regexp = "^$|^[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\."
            + "[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@"
            + "(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?",
            message = "Debe ser un mail válido Ej: alguien@host.com.")
    private String email;
    @NotNull
    @Size(min = 1, max = 200)
    private String razonSocial;
    @Size(max = 60)
    private String apellidos;
    @Size(max = 60)
    private String nombres;
    @Size(max = 200)
    private String nombreFantasia;
    @Size(max = 100)
    private String calle;
    @Size(max = 50)
    private String altura;
    @Size(max = 3)
    private String piso;
    @Size(max = 5)
    private String depto;
    @NotNull
    @Size(min = 1, max = 13)
    private String documento;
    @NotNull
    private Date fechaAlta;

    private boolean activo;

    private boolean cliente;

    private boolean proveedor;
    private UbicacionProvinciasDto idProvincia;
    private UbicacionPaisesDto idPais;
    private UbicacionLocalidadesDto idLocalidad;
    private LegalTiposPersoneriaDto idTipoPersoneria;
    private LegalTiposDocumentoDto idTipoDocumento;
    private LegalGenerosDto idGenero;
    private FiscalResponsabilidadesIvaDto idResponsabilidadIva;
    private List<PersonasTelefonosDto> personasTelefonosList;

    private SucursalesDto idSucursal;
    private Integer version;

    @Override
    public String toString() {
        return String.format(BUSINESS_STRING, id, razonSocial,
                idTipoDocumento.getNombreTipoDocumento(), documento);
    }


}
