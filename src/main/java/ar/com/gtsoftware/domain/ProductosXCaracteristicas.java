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

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/** @author rodrigo */
@Entity
@Table(name = "productos_x_caracteristicas")
@Getter
@Setter
public class ProductosXCaracteristicas extends BaseEntity {

  @Id
  @GeneratedValue(
      strategy = GenerationType.SEQUENCE,
      generator = "productos_x_caracteristicas_id_caracteristica_x_producto")
  @SequenceGenerator(
      allocationSize = 1,
      initialValue = 1,
      name = "productos_x_caracteristicas_id_caracteristica_x_producto",
      sequenceName = "productos_x_caracteristicas_id_caracteristica_x_producto_seq")
  @Basic(optional = false)
  @Column(name = "id_caracteristica_x_producto", nullable = false, updatable = false)
  private Long id;

  @Basic(optional = false)
  @NotNull
  @Size(min = 1, max = 255)
  @Column(name = "valor_caracteristica")
  private String valorCaracteristica;

  @JoinColumn(
      name = "id_caracteristica",
      referencedColumnName = "id_caracteristica",
      columnDefinition = "int4")
  @ManyToOne(optional = false)
  private ProductosCaracteristicas idCaracteristica;

  @JoinColumn(name = "id_producto", referencedColumnName = "id_producto", columnDefinition = "int4")
  @ManyToOne(optional = false)
  private Productos idProducto;
}
