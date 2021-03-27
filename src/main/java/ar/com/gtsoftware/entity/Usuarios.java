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

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "usuarios")
@Getter
@Setter
@NamedEntityGraph(
    name = "rolesUsuarios",
    attributeNodes = @NamedAttributeNode("usuariosGruposList"))
public class Usuarios extends BaseEntity implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "usuarios_id_usuario")
  @SequenceGenerator(
      allocationSize = 1,
      initialValue = 1,
      name = "usuarios_id_usuario",
      sequenceName = "usuarios_id_usuario_seq")
  @Basic(optional = false)
  @Column(name = "id_usuario", nullable = false, updatable = false)
  private Long id;

  @Basic(optional = false)
  @NotNull
  @Size(min = 1, max = 100)
  @Column(name = "nombre_usuario")
  private String nombreUsuario;

  @Basic(optional = false)
  @NotNull
  @Size(min = 1, max = 60)
  @Column(name = "login")
  private String login;

  @Basic(optional = false)
  @NotNull
  @Size(min = 1, max = 64)
  @Column(name = "password")
  private String password;

  @Basic(optional = false)
  @NotNull
  @Column(name = "fecha_alta")
  @Temporal(TemporalType.TIMESTAMP)
  private Date fechaAlta;

  @ManyToMany(
      cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
  @JoinTable(
      name = "usuarios_gruposx",
      joinColumns = {@JoinColumn(name = "id_usuario", referencedColumnName = "id_usuario")},
      inverseJoinColumns = {@JoinColumn(name = "id_grupo", referencedColumnName = "id_grupo")})
  private List<UsuariosGrupos> usuariosGruposList;

  @JoinColumn(name = "id_sucursal", referencedColumnName = "id_sucursal", columnDefinition = "int4")
  @ManyToOne
  private Sucursales idSucursal;
}
