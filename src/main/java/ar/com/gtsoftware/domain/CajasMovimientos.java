/*
 * Copyright 2016 GT Software.
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
import java.math.BigDecimal;
import java.util.Date;

/**
 * Movimientos de cajas
 *
 * @author Rodrigo M. Tato Rothamel mailto:rotatomel@gmail.com
 */
@Entity
@Table(name = "cajas_movimientos")
@Getter
@Setter
public class CajasMovimientos extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cajas_movimientos_id_movimiento_caja")
    @SequenceGenerator(allocationSize = 1, initialValue = 1, name = "cajas_movimientos_id_movimiento_caja",
            sequenceName = "cajas_movimientos_id_movimiento_caja_seq")
    @Basic(optional = false)
    @Column(name = "id_movimiento_caja", nullable = false, updatable = false)
    private Long id;

    @NotNull
    @JoinColumn(name = "id_caja", referencedColumnName = "id_caja")
    @ManyToOne
    private Cajas idCaja;

    @NotNull
    @Column(name = "fecha_movimiento")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date fechaMovimiento;

    @NotNull
    @Column(name = "monto_movimiento")
    private BigDecimal montoMovimiento;

    @Column(name = "descripcion")
    @Size(max = 255)
    private String descripcion;

}
