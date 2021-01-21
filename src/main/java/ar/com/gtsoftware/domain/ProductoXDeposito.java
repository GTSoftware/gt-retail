/*
 * Copyright 2017 GT Software.
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

import java.math.BigDecimal;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * Reperesenta a las existencias de stock de un determinado producto en un depósito determinado
 *
 * @author fede
 */
@Entity
@Table(name = "productos_x_depositos")
@Getter
@Setter
public class ProductoXDeposito extends BaseEntity {

  @Id
  @GeneratedValue(
      strategy = GenerationType.SEQUENCE,
      generator = "productos_x_depositos_id_producto_x_deposito")
  @SequenceGenerator(
      allocationSize = 1,
      initialValue = 1,
      name = "productos_x_depositos_id_producto_x_deposito",
      sequenceName = "productos_x_depositos_id_producto_x_deposito_seq")
  @Basic(optional = false)
  @Column(name = "id_producto_x_deposito", nullable = false, updatable = false)
  private Long id;

  @JoinColumn(
      name = "id_producto",
      referencedColumnName = "id_producto",
      insertable = false,
      updatable = false)
  @NotNull
  @ManyToOne
  private Productos producto;

  @JoinColumn(
      name = "id_deposito",
      referencedColumnName = "id_deposito",
      insertable = false,
      updatable = false)
  @NotNull
  @ManyToOne
  private Depositos deposito;

  @NotNull
  @Column(name = "stock")
  private BigDecimal stock;
}
