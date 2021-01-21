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
package ar.com.gtsoftware.domain;

import java.time.LocalDateTime;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "remitos_recepciones")
@Getter
@Setter
public class RemitoRecepcion extends BaseEntity {

  @Id
  @GeneratedValue(
      strategy = GenerationType.SEQUENCE,
      generator = "remitos_recepciones_id_recepcion")
  @SequenceGenerator(
      allocationSize = 1,
      initialValue = 1,
      name = "remitos_recepciones_id_recepcion",
      sequenceName = "remitos_recepciones_id_recepcion_seq")
  @Basic(optional = false)
  @Column(name = "id_recepcion", nullable = false, updatable = false)
  private Long id;

  @JoinColumn(name = "id_remito", referencedColumnName = "id_remito")
  @NotNull
  @ManyToOne
  private Remito remito;

  @Column(name = "fecha")
  private LocalDateTime fecha;

  @JoinColumn(name = "id_usuario", referencedColumnName = "id_usuario")
  @ManyToOne
  @NotNull
  private Usuarios idUsuario;

  @JoinColumn(name = "id_persona", referencedColumnName = "id_persona")
  @ManyToOne
  private Personas idPersona;

  @JoinColumn(name = "id_deposito", referencedColumnName = "id_deposito")
  @ManyToOne
  private Depositos idDeposito;
}
