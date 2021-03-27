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

import java.util.List;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/** @author rodrigo */
@Entity
@Table(name = "contabilidad_libros")
@Getter
@Setter
public class ContabilidadLibros extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "contabilidad_libros_id_libro")
  @SequenceGenerator(
      allocationSize = 1,
      initialValue = 1,
      name = "contabilidad_libros_id_libro",
      sequenceName = "contabilidad_libros_id_libro_seq")
  @Basic(optional = false)
  @Column(name = "id_libro", nullable = false, updatable = false)
  private Long id;

  @Basic(optional = false)
  @NotNull
  @Size(min = 1, max = 100)
  @Column(name = "nombre_libro")
  private String nombreLibro;

  @Size(max = 255)
  @Column(name = "descripcion_libro")
  private String descripcionLibro;

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "idLibro")
  private List<ContabilidadRegistroContable> contabilidadRegistroContableList;
}
