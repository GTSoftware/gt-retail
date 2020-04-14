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
package ar.com.gtsoftware.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @author rodrigo
 */
@Entity
@Table(name = "proveedores_ordenes_compra_lineas")
@Getter
@Setter
public class ProveedoresOrdenesCompraLineas extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "proveedores_ordenes_compra_lineas_id_orden_compra_linea")
    @SequenceGenerator(allocationSize = 1, initialValue = 1, name = "proveedores_ordenes_compra_lineas_id_orden_compra_linea",
            sequenceName = "proveedores_ordenes_compra_lineas_id_orden_compra_linea_seq")
    @Basic(optional = false)
    @Column(name = "id_orden_compra_linea", nullable = false, updatable = false)
    private Long id;

    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "precio_compra_unitario")
    private BigDecimal precioCompraUnitario;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cantidad_pedida")
    private BigDecimal cantidadPedida;
    @Basic(optional = false)
    @NotNull
    @Column(name = "sub_total")
    private BigDecimal subTotal;
    @NotNull
    @Column(name = "cantidad_recibida")
    private BigDecimal cantidadRecibida;
    @NotNull
    @JoinColumn(name = "id_orden_compra", referencedColumnName = "id_orden_compra", columnDefinition = "int4")
    @ManyToOne(optional = false)
    private ProveedoresOrdenesCompra idOrdenCompra;

    @NotNull
    @JoinColumn(name = "id_producto", referencedColumnName = "id_producto", columnDefinition = "int4")
    @ManyToOne(optional = false)
    private Productos idProducto;
    @NotNull
    @JoinColumn(name = "id_tipo_unidad", referencedColumnName = "id_tipo_unidad", columnDefinition = "int4")
    @ManyToOne(optional = false)
    private ProductosTiposUnidades idTipoUnidad;

}
