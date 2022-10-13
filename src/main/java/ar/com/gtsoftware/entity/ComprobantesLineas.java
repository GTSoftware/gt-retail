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
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Rodrigo Tato <rotatomel@gmail.com>
 */
@Entity
@Table(name = "comprobantes_lineas")
@Getter
@Setter
public class ComprobantesLineas extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ventas_lineas_id_linea_venta")
  @SequenceGenerator(
      allocationSize = 1,
      initialValue = 1,
      name = "ventas_lineas_id_linea_venta",
      sequenceName = "ventas_lineas_id_linea_venta_seq")
  @Basic(optional = false)
  @Column(name = "id_linea_comprobante", nullable = false, updatable = false)
  private Long id;

  // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these
  // annotations to enforce field validation
  @Basic(optional = false)
  @NotNull
  @Column(name = "precio_unitario")
  private BigDecimal precioUnitario;

  @Basic(optional = false)
  @NotNull
  @Column(name = "cantidad")
  private BigDecimal cantidad;

  @Basic(optional = false)
  @NotNull
  @Column(name = "sub_total")
  private BigDecimal subTotal;

  @Basic(optional = false)
  @NotNull
  @Column(name = "costo_neto_unitario")
  private BigDecimal costoNetoUnitario;

  @Basic(optional = false)
  @NotNull
  @Column(name = "costo_bruto_unitario")
  private BigDecimal costoBrutoUnitario;

  @Basic(optional = false)
  @NotNull
  @Column(name = "cantidad_entregada")
  private BigDecimal cantidadEntregada;

  @JoinColumn(name = "id_comprobante", referencedColumnName = "id_comprobante")
  @ManyToOne(optional = false)
  private Comprobantes idComprobante;

  @JoinColumn(name = "id_producto", referencedColumnName = "id_producto")
  @ManyToOne(optional = false)
  private Productos idProducto;

  @Basic(optional = false)
  @NotNull
  @Column(name = "descripcion")
  @Size(max = 200)
  private String descripcion;

  @Basic(optional = false)
  @NotNull
  @Column(name = "item")
  private Integer item;

  public BigDecimal getIva() {
    return this.idProducto.getIdAlicuotaIva().getValorAlicuota();
  }
}
