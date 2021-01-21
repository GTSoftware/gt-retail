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

/** @author Rodrigo Tato <rotatomel@gmail.com> */
@Entity
@Table(name = "productos_tipos_porcentajes")
@Getter
@Setter
public class ProductosTiposPorcentajes extends BaseEntity {

  @Id
  @GeneratedValue(
      strategy = GenerationType.SEQUENCE,
      generator = "productos_tipos_porcentajes_id_tipo_porcentaje")
  @SequenceGenerator(
      allocationSize = 1,
      initialValue = 1,
      name = "productos_tipos_porcentajes_id_tipo_porcentaje",
      sequenceName = "productos_tipos_porcentajes_id_tipo_porcentaje_seq")
  @Basic(optional = false)
  @Column(name = "id_tipo_porcentaje", nullable = false, updatable = false)
  private Long id;

  @Basic(optional = false)
  @NotNull
  @Size(min = 1, max = 50)
  @Column(name = "nombre_tipo")
  private String nombreTipo;

  @Basic(optional = false)
  @NotNull
  @Column(name = "porcentaje")
  private boolean porcentaje;
}
