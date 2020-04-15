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
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author rodrigo
 */
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProveedoresOrdenesCompraDto {

    private static final long serialVersionUID = 1L;

    @EqualsAndHashCode.Include
    private Long id;
    private Date fechaAlta;
    @NotNull
    private Date fechaModificacion;
    @NotNull
    private Date fechaEstimadaRecepcion;
    @NotNull
    private BigDecimal total;
    @NotNull
    private BigDecimal totalIVA;
    @Size(max = 1024)
    private String observaciones;

    @NotNull
    private UsuariosDto idUsuario;
    @NotNull
    private ProveedoresOrdenesCompraEstadosDto idEstadoOrdenCompra;

    @NotNull
    private PersonasDto idProveedor;
    private PersonasDto idTransporte;

    private List<ProveedoresOrdenesCompraLineasDto> proveedoresOrdenesCompraLineasList;
    private Integer version;


    public void addPurchaseItem(ProveedoresOrdenesCompraLineasDto item) {
        if (proveedoresOrdenesCompraLineasList == null) {
            proveedoresOrdenesCompraLineasList = new ArrayList<>();
        }
        proveedoresOrdenesCompraLineasList.add(item);
    }
}
