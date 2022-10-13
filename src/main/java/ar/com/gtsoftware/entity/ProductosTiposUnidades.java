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

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Rodrigo Tato <rotatomel@gmail.com>
 */
@Entity
@Table(name = "productos_tipos_unidades")
@Getter
@Setter
public class ProductosTiposUnidades extends BaseEntity {

  @Id
  @GeneratedValue(
      strategy = GenerationType.SEQUENCE,
      generator = "productos_tipos_unidades_id_tipo_unidad")
  @SequenceGenerator(
      allocationSize = 1,
      initialValue = 1,
      name = "productos_tipos_unidades_id_tipo_unidad",
      sequenceName = "productos_tipos_unidades_id_tipo_unidad_seq")
  @Basic(optional = false)
  @Column(name = "id_tipo_unidad", nullable = false, updatable = false)
  private Long id;

  @Basic(optional = false)
  @NotNull
  @Size(min = 1, max = 60)
  @Column(name = "nombre_unidad")
  private String nombreUnidad;

  @Basic(optional = false)
  @NotNull
  @Column(name = "cantidad_entera")
  private boolean cantidadEntera;
}
