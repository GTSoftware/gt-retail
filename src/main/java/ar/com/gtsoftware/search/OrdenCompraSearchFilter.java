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

import lombok.*;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

/**
 * SearchFilter para Órdenes de Compra
 *
 * @author Rodrigo Tato mailto:rotatomel@gmail.com
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrdenCompraSearchFilter extends AbstractSearchFilter {

    private Date fechaAltaDesde;
    private Date fechaAltaHasta;
    private Long idProveedor;
    private Long idProducto;
    private String txt;
    private Long idEstadoOrdenCompra;

    @Override
    public boolean hasFilter() {
        return hasEntreFechasAltaFilter()
                || idProveedor != null
                || idProducto != null
                || idEstadoOrdenCompra != null
                || hasTxtFilter();
    }

    /**
     * Retorna true si ambos filtros de búsqueda por fecha de alta de orden de compra están establecidos.
     *
     * @return
     */
    public boolean hasEntreFechasAltaFilter() {
        return fechaAltaDesde != null && fechaAltaHasta != null;
    }


    /**
     * Retorna true si hay algún texto de búsqueda.
     *
     * @return
     */
    public boolean hasTxtFilter() {
        return StringUtils.isNotEmpty(txt);
    }

}
