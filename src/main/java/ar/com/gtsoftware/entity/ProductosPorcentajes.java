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
import java.time.LocalDateTime;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/** @author Rodrigo Tato <rotatomel@gmail.com> */
@Entity
@Table(name = "productos_porcentajes")
@Getter
@Setter
public class ProductosPorcentajes extends BaseEntity {

  @Id
  @GeneratedValue(
      strategy = GenerationType.SEQUENCE,
      generator = "productos_porcentajes_id_producto_porcentaje")
  @SequenceGenerator(
      allocationSize = 1,
      initialValue = 1,
      name = "productos_porcentajes_id_producto_porcentaje",
      sequenceName = "productos_porcentajes_id_producto_porcentaje_seq")
  @Basic(optional = false)
  @Column(name = "id_producto_porcentaje", nullable = false, updatable = false)
  private Long id;

  @Basic(optional = false)
  @NotNull
  @Column(name = "fecha_modificacion")
  private LocalDateTime fechaModificacion;

  @NotNull
  @Column(name = "valor", nullable = false, precision = 19, scale = 4)
  private BigDecimal valor;

  @JoinColumn(
      name = "id_tipo_porcentaje",
      referencedColumnName = "id_tipo_porcentaje",
      columnDefinition = "int4")
  @ManyToOne(optional = false)
  private ProductosTiposPorcentajes idTipoPorcentaje;

  @JoinColumn(name = "id_producto", referencedColumnName = "id_producto", columnDefinition = "int4")
  @ManyToOne(optional = false)
  private Productos idProducto;
}
