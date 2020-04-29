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
import java.time.LocalDateTime;
import java.util.List;

/**
 * Clase que representa las ventas que se realizan
 *
 * @author Rodrigo Tato <rotatomel@gmail.com>
 */
@Entity
@Table(name = "comprobantes")
@NamedEntityGraphs({
        @NamedEntityGraph(name = "lineas", attributeNodes = {
                @NamedAttributeNode("comprobantesLineasList")})
        ,
        @NamedEntityGraph(name = "pagos", attributeNodes = {
                @NamedAttributeNode("pagosList")})
        ,
        @NamedEntityGraph(name = "todo", attributeNodes = {
                @NamedAttributeNode("pagosList")
                ,
                @NamedAttributeNode("comprobantesLineasList")})})
@Getter
@Setter
public class Comprobantes extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ventas_id_venta")
    @SequenceGenerator(allocationSize = 1, initialValue = 1, name = "ventas_id_venta",
            sequenceName = "ventas_id_venta_seq")
    @Basic(optional = false)
    @Column(name = "id_comprobante", nullable = false, updatable = false)
    private Long id;

    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_comprobante")
    //@Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime fechaComprobante;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "total")
    private BigDecimal total;
    @Basic(optional = false)
    @NotNull
    @Column(name = "saldo")
    private BigDecimal saldo;
    @Size(max = 1024)
    @Column(name = "observaciones")
    private String observaciones;
    @Size(max = 100)
    @Column(name = "remitente")
    private String remitente;
    @Size(max = 100)
    @Column(name = "nroremito")
    private String nroRemito;
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

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idComprobante", orphanRemoval = true)
    private List<ComprobantesLineas> comprobantesLineasList;
    @JoinColumn(name = "id_usuario", referencedColumnName = "id_usuario")
    @ManyToOne(optional = false)
    private Usuarios idUsuario;
    @JoinColumn(name = "id_sucursal", referencedColumnName = "id_sucursal")
    @ManyToOne(optional = false)
    private Sucursales idSucursal;
    @JoinColumn(name = "id_persona", referencedColumnName = "id_persona")
    @ManyToOne(optional = false)
    private Personas idPersona;
    @JoinColumn(name = "id_condicion_comprobante", referencedColumnName = "id_condicion")
    @ManyToOne(optional = false)
    private NegocioCondicionesOperaciones idCondicionComprobante;
    @JoinColumn(name = "id_estado", referencedColumnName = "id_estado")
    @ManyToOne(optional = false)
    private ComprobantesEstados idEstadoComprobante;
    @JoinColumn(name = "id_registro_iva", referencedColumnName = "id_registro")
    @ManyToOne
    private FiscalLibroIvaVentas idRegistro;

    @Transient
    private BigDecimal totalConSigno;
    @Transient
    private BigDecimal saldoConSigno;
    @OneToMany(mappedBy = "idComprobante", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ComprobantesPagos> pagosList;

    public BigDecimal getTotalConSigno() {
        if (totalConSigno == null) {
            totalConSigno = total.multiply(tipoComprobante.getSigno());
        }

        return totalConSigno;
    }

    public BigDecimal getSaldoConSigno() {
        if (saldoConSigno == null) {
            saldoConSigno = saldo.multiply(tipoComprobante.getSigno());
        }
        return saldoConSigno;
    }

}
