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
import javax.validation.constraints.Size;

/**
 * @author Rodrigo Tato mailto:rotatomel@gmail.com
 */
@Entity
@Table(name = "negocio_condiciones_operaciones")
@Getter
@Setter
public class NegocioCondicionesOperaciones extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ventas_condiciones_id_condicion_venta")
    @SequenceGenerator(allocationSize = 1, initialValue = 1, name = "ventas_condiciones_id_condicion_venta",
            sequenceName = "ventas_condiciones_id_condicion_venta_seq")
    @Basic(optional = false)
    @Column(name = "id_condicion", nullable = false, updatable = false)
    private Long id;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 60)
    @Column(name = "nombre_condicion")
    private String nombreCondicion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "activo")
    private boolean activo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "venta")
    private boolean venta;
    @Basic(optional = false)
    @NotNull
    @Column(name = "compra")
    private boolean compra;
    @Basic(optional = false)
    @NotNull
    @Column(name = "pago_total")
    private boolean pagoTotal;

    @Override
    public String toString() {
        return "ar.com.gtsoftware.model.NegocioCondicionesOperacionesDto[ idCondicion=" + id + " ]";
    }

}
