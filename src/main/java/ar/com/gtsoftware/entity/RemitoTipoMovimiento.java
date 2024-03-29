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
package ar.com.gtsoftware.entity;

import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * @author fede
 */
@Entity
@Table(name = "remitos_movimientos_tipos")
@Getter
@Setter
public class RemitoTipoMovimiento extends BaseEntity {

  @Id
  @Basic(optional = false)
  @Column(name = "id_tipo_movimiento", nullable = false, updatable = false)
  private Long id;

  @Column(name = "nombre_tipo")
  private String nombreTipo;
}
