/*
 * Copyright 2015 GT Software.
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
import java.time.LocalDateTime;

/**
 * @author Rodrigo M. Tato Rothamel <rotatomel@gmail.com>
 */
@Entity
@Table(name = "productos_precios", uniqueConstraints = @UniqueConstraint(columnNames = {"id_producto", "id_lista_precio"}))
@Getter
@Setter
public class ProductosPrecios extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "productos_precios_productos_precios_id")
    @SequenceGenerator(allocationSize = 1, initialValue = 1, name = "productos_precios_productos_precios_id",
            sequenceName = "productos_precios_productos_precios_id_seq")
    @Basic(optional = false)
    @Column(name = "productos_precios_id", nullable = false, updatable = false)
    private Long id;

    @JoinColumn(name = "id_producto", referencedColumnName = "id_producto")
    @ManyToOne(optional = false)
    private Productos idProducto;

    @JoinColumn(name = "id_lista_precio", referencedColumnName = "id_lista_precio")
    @ManyToOne(optional = false)
    private ProductosListasPrecios idListaPrecios;

    @Basic(optional = false)
    @Column(name = "utilidad")
    private BigDecimal utilidad;

    @Basic(optional = false)
    @Column(name = "precio")
    private BigDecimal precio;

    @Basic(optional = false)
    @Column(name = "neto")
    private BigDecimal neto;

    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_modificacion")
    private LocalDateTime fechaModificacion;

}
