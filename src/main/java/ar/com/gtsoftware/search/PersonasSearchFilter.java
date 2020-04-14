/*
 * Copyright 2014 GT Software.
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

import lombok.*;
import org.apache.commons.lang3.StringUtils;

/**
 * @author rodrigo
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PersonasSearchFilter extends AbstractSearchFilter {


    private String txt;
    private Integer idPersona;
    private String razonSocial;
    private String apellidos;
    private String nombres;
    private String nombreFantasia;
    private Long idTipoDocumento;
    private String documento;
    private Boolean activo;
    private Boolean cliente;
    private Boolean proveedor;

    @Override
    public boolean hasFilter() {
        return StringUtils.isNotEmpty(txt)
                || idPersona != null
                || StringUtils.isNotEmpty(razonSocial)
                || StringUtils.isNotEmpty(apellidos)
                || StringUtils.isNotEmpty(nombres)
                || StringUtils.isNotEmpty(nombreFantasia)
                || idTipoDocumento != null
                || documento != null
                || activo != null
                || cliente != null
                || proveedor != null;
    }

}
