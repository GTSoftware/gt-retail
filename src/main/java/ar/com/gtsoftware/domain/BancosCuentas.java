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
import java.util.Date;
import java.util.List;

/**
 * @author rodrigo
 */
@Entity
@Table(name = "bancos_cuentas")
@Getter
@Setter
public class BancosCuentas extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "bancos_cuentas_id_cuenta_banco")
    @SequenceGenerator(allocationSize = 1, initialValue = 1, name = "bancos_cuentas_id_cuenta_banco",
            sequenceName = "bancos_cuentas_id_cuenta_banco_seq")
    @Basic(optional = false)
    @Column(name = "id_cuenta_banco", nullable = false, updatable = false)
    private Long id;

    @Size(max = 200)
    @Column(name = "descripcion_cuenta")
    private String descripcionCuenta;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 60)
    @Column(name = "numero_cuenta")
    private String numeroCuenta;
    @Size(max = 10)
    @Column(name = "numero_sucursal")
    private String numeroSucursal;
    @Size(max = 22)
    @Column(name = "cbu")
    private String cbu;
    @Basic(optional = false)
    @NotNull
    @Column(name = "activo")
    private boolean activo;
    @Column(name = "fecha_apertura")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaApertura;
    @JoinColumn(name = "id_moneda", referencedColumnName = "id_moneda", columnDefinition = "int4")
    @ManyToOne(optional = false)
    private ContabilidadMonedas idMoneda;
    @JoinColumn(name = "id_tipo_cuenta_banco", referencedColumnName = "id_tipo_cuenta_banco", columnDefinition = "int4")
    @ManyToOne(optional = false)
    private BancosTiposCuenta idTipoCuentaBanco;
    @JoinColumn(name = "id_banco", referencedColumnName = "id_banco", columnDefinition = "int4")
    @ManyToOne(optional = false)
    private Bancos idBanco;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idCuentaBanco")
    private List<BancosCuentaCorriente> bancosCuentaCorrienteList;

}
