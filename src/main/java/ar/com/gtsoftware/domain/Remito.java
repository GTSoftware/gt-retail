/*
 * Copyright 2016 GT Software.
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
import java.util.List;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "remitos")
@Getter
@Setter
public class Remito extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "remitos_id_remito")
  @SequenceGenerator(
      allocationSize = 1,
      initialValue = 1,
      name = "remitos_id_remito",
      sequenceName = "remitos_id_remito_seq")
  @Basic(optional = false)
  @Column(name = "id_remito", nullable = false, updatable = false)
  private Long id;

  @Column(name = "fecha_alta")
  private LocalDateTime fechaAlta;

  @JoinColumn(name = "id_usuario", referencedColumnName = "id_usuario")
  @ManyToOne
  @NotNull
  private Usuarios idUsuario;

  @Column(name = "observaciones")
  private String observaciones;

  @Column(name = "origen_is_interno")
  @NotNull
  private Boolean isOrigenInterno;

  @JoinColumn(name = "id_origen_externo", referencedColumnName = "id_persona")
  @ManyToOne
  private Personas idOrigenExterno;

  @JoinColumn(name = "id_origen_interno", referencedColumnName = "id_deposito")
  @ManyToOne
  private Depositos idOrigenInterno;

  @Column(name = "destino_is_interno")
  @NotNull
  private Boolean isDestinoInterno;

  @JoinColumn(name = "destino_previsto_externo", referencedColumnName = "id_persona")
  @ManyToOne
  private Personas idDestinoPrevistoExterno;

  @JoinColumn(name = "destino_previsto_interno", referencedColumnName = "id_deposito")
  @ManyToOne
  private Depositos idDestinoPrevistoInterno;

  @Column(name = "fecha_cierre")
  private LocalDateTime fechaCierre;

  @NotNull
  @JoinColumn(name = "id_tipo_movimiento", referencedColumnName = "id_tipo_movimiento")
  @ManyToOne
  private RemitoTipoMovimiento remitoTipoMovimiento;

  @OneToMany(mappedBy = "remitoCabecera", cascade = CascadeType.ALL)
  private List<RemitoDetalle> detalleList;

  @OneToMany(mappedBy = "remito", cascade = CascadeType.ALL)
  private List<RemitoRecepcion> remitoRecepcionesList;
}
