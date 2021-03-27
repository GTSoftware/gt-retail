/*
 * Copyright 2019 GT Software.
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
import lombok.NoArgsConstructor;
import lombok.Setter;

/** @author rodrigo */
@Entity
@Table(name = "personas_telefonos")
@Getter
@Setter
@NoArgsConstructor
public class PersonasTelefonos extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "personas_telefonos_id_telefono")
  @SequenceGenerator(
      allocationSize = 1,
      initialValue = 1,
      name = "personas_telefonos_id_telefono",
      sequenceName = "personas_telefonos_id_telefono_seq")
  @Basic(optional = false)
  @Column(name = "id_telefono", nullable = false, updatable = false)
  private Long id;

  @Basic(optional = false)
  @NotNull
  @Size(min = 1, max = 50)
  @Column(name = "numero")
  // @Pattern(regexp =
  // "(?<=\\s|:)\\(?(?:(0?[1-3]\\d{1,2})\\)?(?:\\s|-)?)?((?:\\d[\\d-]{5}|15[\\s\\d-]{7})\\d+)",
  // message = "Número de teléfono no válido")
  private String numero;

  @Size(max = 100)
  @Column(name = "referencia")
  private String referencia;

  @JoinColumn(name = "id_persona", referencedColumnName = "id_persona", columnDefinition = "int4")
  @ManyToOne(optional = false)
  private Personas idPersona;
}
