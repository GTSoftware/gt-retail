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
package ar.com.gtsoftware.entity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/** @author rodrigo */
@Entity
@Table(name = "proveedores_ordenes_compra")
@Getter
@Setter
public class ProveedoresOrdenesCompra extends BaseEntity {

  @Id
  @GeneratedValue(
      strategy = GenerationType.SEQUENCE,
      generator = "proveedores_ordenes_compra_id_orden_compra")
  @SequenceGenerator(
      allocationSize = 1,
      initialValue = 1,
      name = "proveedores_ordenes_compra_id_orden_compra",
      sequenceName = "proveedores_ordenes_compra_id_orden_compra_seq")
  @Basic(optional = false)
  @Column(name = "id_orden_compra", nullable = false, updatable = false)
  private Long id;

  @Basic(optional = false)
  @NotNull
  @Column(name = "fecha_alta")
  @Temporal(TemporalType.TIMESTAMP)
  private Date fechaAlta;

  @Basic(optional = false)
  @NotNull
  @Column(name = "fecha_modificacion")
  @Temporal(TemporalType.TIMESTAMP)
  private Date fechaModificacion;

  @Basic(optional = false)
  @NotNull
  @Column(name = "fecha_estimada_recepcion")
  @Temporal(TemporalType.DATE)
  private Date fechaEstimadaRecepcion;
  // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these
  // annotations to enforce field validation
  @Basic(optional = false)
  @NotNull
  @Column(name = "total_orden_compra")
  private BigDecimal total;

  @Basic(optional = false)
  @NotNull
  @Column(name = "monto_iva_total")
  private BigDecimal totalIVA;

  @Size(max = 1024)
  @Column(name = "observaciones")
  private String observaciones;

  @NotNull
  @JoinColumn(name = "id_usuario", referencedColumnName = "id_usuario", columnDefinition = "int4")
  @ManyToOne(optional = false)
  private Usuarios idUsuario;

  @NotNull
  @JoinColumn(
      name = "id_estado_orden_compra",
      referencedColumnName = "id_estado_orden_compra",
      columnDefinition = "int4")
  @ManyToOne(optional = false)
  private ProveedoresOrdenesCompraEstados idEstadoOrdenCompra;

  @NotNull
  @JoinColumn(name = "id_proveedor", referencedColumnName = "id_persona", columnDefinition = "int4")
  @ManyToOne(optional = false)
  private Personas idProveedor;

  @JoinColumn(
      name = "id_transporte",
      referencedColumnName = "id_persona",
      columnDefinition = "int4")
  @ManyToOne(optional = false)
  private Personas idTransporte;

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "idOrdenCompra", orphanRemoval = true)
  private List<ProveedoresOrdenesCompraLineas> proveedoresOrdenesCompraLineasList;

  @PreUpdate
  private void preUpdate() {
    this.fechaModificacion = new Date();
  }

  @PrePersist
  private void prePersist() {
    Date today = new Date();
    this.fechaAlta = today;
    this.fechaModificacion = today;
  }
}
