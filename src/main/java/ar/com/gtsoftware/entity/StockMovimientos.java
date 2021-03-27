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
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/** @author rodrigo */
@Entity
@Table(name = "stock_movimientos")
@Getter
@Setter
public class StockMovimientos extends BaseEntity {

  @Id
  @GeneratedValue(
      strategy = GenerationType.SEQUENCE,
      generator = "stock_movimientos_id_movimiento_stock")
  @SequenceGenerator(
      allocationSize = 1,
      initialValue = 1,
      name = "stock_movimientos_id_movimiento_stock",
      sequenceName = "stock_movimientos_id_movimiento_stock_seq")
  @Basic(optional = false)
  @Column(name = "id_movimiento_stock", nullable = false, updatable = false)
  private Long id;

  @Basic(optional = false)
  @NotNull
  @Column(name = "fecha_movimiento")
  @Temporal(TemporalType.TIMESTAMP)
  private Date fechaMovimiento;
  // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these
  // annotations to enforce field validation
  @Basic(optional = false)
  @NotNull
  @Column(name = "cantidad_anterior")
  private BigDecimal cantidadAnterior;

  @Basic(optional = false)
  @NotNull
  @Column(name = "cantidad_movimiento")
  private BigDecimal cantidadMovimiento;

  @Basic(optional = false)
  @NotNull
  @Column(name = "cantidad_actual")
  private BigDecimal cantidadActual;

  @Basic(optional = false)
  @NotNull
  @Column(name = "id_tipo_movimiento")
  private int idTipoMovimiento;

  @Size(max = 255)
  @Column(name = "observaciones_movimiento")
  private String observacionesMovimiento;

  @Basic(optional = false)
  @NotNull
  @Column(name = "costo_total_movimiento")
  private BigDecimal costoTotalMovimiento;

  @JoinColumn(name = "id_usuario", referencedColumnName = "id_usuario", columnDefinition = "int4")
  @ManyToOne(optional = false)
  private Usuarios idUsuario;

  @JoinColumn(name = "id_producto", referencedColumnName = "id_producto", columnDefinition = "int4")
  @ManyToOne(optional = false)
  private Productos idProducto;

  @JoinColumn(
      name = "id_deposito_movimiento",
      referencedColumnName = "id_deposito",
      columnDefinition = "int4")
  @ManyToOne(optional = false)
  private Depositos idDepositoMovimiento;
}
