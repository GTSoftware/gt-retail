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
 * Representa a los coeficientes de descuentos o recargos comerciales que aplican al total de la orden de compra
 *
 * @author Rodrigo M. Tato Rothamel
 */
@Entity
@Table(name = "proveedores_ordenes_compra_porcentajes")
@Getter
@Setter
public class ProveedoresOrdenesCompraPorcentajes extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "proveedores_ordenes_compra_porce_id_orden_compra_porcentaje")
    @SequenceGenerator(allocationSize = 1, initialValue = 1, name = "proveedores_ordenes_compra_porce_id_orden_compra_porcentaje",
            sequenceName = "proveedores_ordenes_compra_porce_id_orden_compra_porcentaje_seq")
    @Basic(optional = false)
    @Column(name = "id_orden_compra_porcentaje", nullable = false, updatable = false)
    private Long id;

    @NotNull
    @JoinColumn(name = "id_orden_compra", referencedColumnName = "id_orden_compra", columnDefinition = "int4")
    @ManyToOne(optional = false)
    private ProveedoresOrdenesCompra idOrdenCompra;

    @NotNull
    @JoinColumn(name = "id_tipo_porcentaje", referencedColumnName = "id_tipo_porcentaje", columnDefinition = "int4")
    @ManyToOne(optional = false)
    private ProductosTiposPorcentajes idTipoPorcentaje;

    @Basic(optional = false)
    @NotNull
    @Column(name = "valor")
    private BigDecimal valor;

}
