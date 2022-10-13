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
package ar.com.gtsoftware.entity;

import java.math.BigDecimal;
import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * @author fede
 */
@Entity
@Table(name = "remitos_detalle")
@Getter
@Setter
public class RemitoDetalle extends BaseEntity {

  @Id
  @GeneratedValue(
      strategy = GenerationType.SEQUENCE,
      generator = "remitos_detalle_id_remito_detalle")
  @SequenceGenerator(
      allocationSize = 1,
      initialValue = 1,
      name = "remitos_detalle_id_remito_detalle",
      sequenceName = "remitos_detalle_id_remito_detalle_seq")
  @Basic(optional = false)
  @Column(name = "id_remito_detalle", nullable = false, updatable = false)
  private Long id;

  @JoinColumn(name = "id_remito", referencedColumnName = "id_remito")
  @ManyToOne
  private Remito remitoCabecera;

  @JoinColumn(name = "id_producto", referencedColumnName = "id_producto")
  @ManyToOne
  private Productos idProducto;

  @Column(name = "cantidad")
  private BigDecimal cantidad;

  @Transient private int nroLinea;
}
