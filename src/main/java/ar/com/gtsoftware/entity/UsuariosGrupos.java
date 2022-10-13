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
@Table(name = "usuarios_grupos")
@Getter
@Setter
public class UsuariosGrupos extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "usuarios_grupos_id_grupo")
  @SequenceGenerator(
      allocationSize = 1,
      initialValue = 1,
      name = "usuarios_grupos_id_grupo",
      sequenceName = "usuarios_grupos_id_grupo_seq")
  @Basic(optional = false)
  @Column(name = "id_grupo", nullable = false, updatable = false)
  private Long id;

  @Basic(optional = false)
  @NotNull
  @Size(min = 1, max = 100)
  @Column(name = "nombre_grupo")
  private String nombreGrupo;

  @ManyToMany(
      mappedBy = "usuariosGruposList",
      cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
  private List<Usuarios> usuariosList;

  @JoinTable(
      name = "privilegios_gruposx",
      joinColumns = {
        @JoinColumn(name = "id_grupo", referencedColumnName = "id_grupo", columnDefinition = "int4")
      },
      inverseJoinColumns = {
        @JoinColumn(
            name = "id_privilegio",
            referencedColumnName = "id_privilegio",
            columnDefinition = "int4")
      })
  @ManyToMany
  private List<Privilegios> privilegiosList;
}
