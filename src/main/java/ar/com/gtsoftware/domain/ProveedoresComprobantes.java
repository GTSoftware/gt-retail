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
import java.math.BigDecimal;
import java.util.Date;

/**
 * Clase que representa las compras que se realizan
 *
 * @author Rodrigo Tato <rotatomel@gmail.com>
 */
@Entity
@Table(name = "proveedores_comprobantes")
@Getter
@Setter
public class ProveedoresComprobantes extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "proveedores_comprobantes_id_comprobante")
    @SequenceGenerator(allocationSize = 1, initialValue = 1, name = "proveedores_comprobantes_id_comprobante",
            sequenceName = "proveedores_comprobantes_id_comprobante_seq")
    @Basic(optional = false)
    @Column(name = "id_comprobante", nullable = false, updatable = false)
    private Long id;

    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_comprobante")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaComprobante;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "total")
    private BigDecimal total;
    @Basic(optional = false)

    @Size(max = 1024)
    @Column(name = "observaciones")
    private String observaciones;
    @Basic(optional = false)
    @NotNull
    @Column(name = "anulada")
    private boolean anulada;

    @Size(max = 1)
    @Column(name = "letra")
    private String letra;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "id_negocio_tipo_comprobante", referencedColumnName = "id_negocio_tipo_comprobante")
    private NegocioTiposComprobante tipoComprobante;

    @JoinColumn(name = "id_usuario", referencedColumnName = "id_usuario")
    @ManyToOne(optional = false)
    private Usuarios idUsuario;
    @JoinColumn(name = "id_sucursal", referencedColumnName = "id_sucursal")
    @ManyToOne(optional = false)
    private Sucursales idSucursal;
    @JoinColumn(name = "id_proveedor", referencedColumnName = "id_persona")
    @ManyToOne(optional = false)
    private Personas idProveedor;

    @JoinColumn(name = "id_registro_iva", referencedColumnName = "id_registro")
    @ManyToOne
    private FiscalLibroIvaCompras idRegistro;

    @Transient
    private BigDecimal totalConSigno;

    public BigDecimal getTotalConSigno() {
        if (total != null && tipoComprobante != null) {
            if (totalConSigno == null) {
                totalConSigno = total.multiply(tipoComprobante.getSigno());
            }
        }
        return totalConSigno;
    }

}
