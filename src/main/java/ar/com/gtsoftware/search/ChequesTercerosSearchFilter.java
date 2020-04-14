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

import java.util.Date;

/**
 * Search filter para cheques de terceros
 *
 * @author Rodrigo Tato mailto:rotatomel@gmail.com
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChequesTercerosSearchFilter extends AbstractSearchFilter {

    private static final long serialVersionUID = 1L;

    private String cuitOriginante;
    private String nombreOriginante;
    private Long idBanco;
    private Date fechaOrigenDesde;
    private Date fechaOrigenHasta;
    private Boolean noVencidos;
    private Boolean noCobrados;


    @Override
    public boolean hasFilter() {
        return hasFilterBanco()
                || hasFilterCuitOriginante()
                || hasFilterFechaOrigen()
                || hasFilterNoCobrados()
                || hasFilterNoVencidos()
                || hasFilterNombreOriginante();
    }

    public boolean hasFilterCuitOriginante() {
        return StringUtils.isNotEmpty(cuitOriginante);
    }

    public boolean hasFilterNombreOriginante() {
        return StringUtils.isNotEmpty(nombreOriginante);
    }

    public boolean hasFilterBanco() {
        return idBanco != null;
    }

    public boolean hasFilterFechaOrigen() {
        return fechaOrigenDesde != null && fechaOrigenHasta != null && (fechaOrigenHasta.compareTo(fechaOrigenDesde) >= 0);
    }

    public boolean hasFilterNoVencidos() {
        return noVencidos != null;
    }

    public boolean hasFilterNoCobrados() {
        return noCobrados != null;
    }


}
