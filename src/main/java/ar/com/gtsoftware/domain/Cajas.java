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
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author Rodrigo M. Tato Rothamel mailto:rotatomel@gmail.com
 */
@Entity
@Table(name = "cajas")
@Getter
@Setter
public class Cajas extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cajas_id_caja")
    @SequenceGenerator(allocationSize = 1, initialValue = 1, name = "cajas_id_caja",
            sequenceName = "cajas_id_caja_seq")
    @Basic(optional = false)
    @Column(name = "id_caja", nullable = false, updatable = false)
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "id_usuario", referencedColumnName = "id_usuario")
    private Usuarios idUsuario;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "id_sucursal", referencedColumnName = "id_sucursal")
    private Sucursales idSucursal;

    @NotNull
    @Column(name = "fecha_apertura")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date fechaApertura;

    @Column(name = "fecha_cierre")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date fechaCierre;

    @NotNull
    @Column(name = "saldo_inicial")
    private BigDecimal saldoInicial;

    @OneToMany(mappedBy = "idCaja")
    private List<CajasMovimientos> cajasMovimientoss;

    @OneToMany(mappedBy = "idCaja")
    private List<Recibos> recibosList;

}
