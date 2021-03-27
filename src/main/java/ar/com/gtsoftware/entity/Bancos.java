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
 * Clase que almacena la información de los bancos
 *
 * @author Rodrigo Tato <rotatomel@gmail.com>
 * @version 2.0.0
 * @since 1.0.0
 */
@Entity
@Table(name = "bancos")
@Getter
@Setter
public class Bancos extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "bancos_id_banco")
  @SequenceGenerator(
      allocationSize = 1,
      initialValue = 1,
      name = "bancos_id_banco",
      sequenceName = "bancos_id_banco_seq")
  @Basic(optional = false)
  @Column(name = "id_banco", nullable = false, updatable = false)
  private Long id;

  @Basic(optional = false)
  @NotNull
  @Size(min = 1, max = 200)
  @Column(name = "razon_social")
  private String razonSocial;

  @Size(max = 1024)
  @Column(name = "observaciones")
  private String observaciones;

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "idBanco")
  private List<BancosCuentas> bancosCuentasList;

  @JoinColumn(name = "id_persona", referencedColumnName = "id_persona")
  @ManyToOne(optional = false)
  private Personas idPersona;
}
