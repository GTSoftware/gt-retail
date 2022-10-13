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

/**
 * @author rodrigo
 */
@Entity
@Table(name = "legal_generos")
@Getter
@Setter
public class LegalGeneros extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "legal_generos_id_genero")
  @SequenceGenerator(
      allocationSize = 1,
      initialValue = 1,
      name = "legal_generos_id_genero",
      sequenceName = "legal_generos_id_genero_seq")
  @Basic(optional = false)
  @Column(name = "id_genero", nullable = false, updatable = false)
  private Long id;

  @Basic(optional = false)
  @NotNull
  @Size(min = 1, max = 30)
  @Column(name = "nombre_genero")
  private String nombreGenero;

  @Basic(optional = false)
  @NotNull
  @Size(min = 1, max = 1)
  @Column(name = "simbolo")
  private String simbolo;

  @JoinColumn(
      name = "id_tipo_personeria",
      referencedColumnName = "id_tipo_personeria",
      columnDefinition = "int4")
  @ManyToOne(optional = false)
  private LegalTiposPersoneria idTipoPersoneria;

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "idGenero")
  private List<Personas> personasList;
}
