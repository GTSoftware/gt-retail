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
package ar.com.gtsoftware.entity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/** @author Rodrigo M. Tato Rothamel mailto:rotatomel@gmail.com */
@Entity
@Table(name = "recibos")
@Getter
@Setter
public class Recibos extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "recibos_id_recibo")
  @SequenceGenerator(
      allocationSize = 1,
      initialValue = 1,
      name = "recibos_id_recibo",
      sequenceName = "recibos_id_recibo_seq")
  @Basic(optional = false)
  @Column(name = "id_recibo", nullable = false, updatable = false)
  private Long id;

  @NotNull
  @Column(name = "fecha_recibo")
  @Temporal(javax.persistence.TemporalType.TIMESTAMP)
  private Date fechaRecibo;

  @NotNull
  @ManyToOne
  @JoinColumn(name = "id_persona", referencedColumnName = "id_persona")
  private Personas idPersona;

  @NotNull
  @ManyToOne
  @JoinColumn(name = "id_usuario", referencedColumnName = "id_usuario")
  private Usuarios idUsuario;

  @NotNull
  @Column(name = "monto_total")
  private BigDecimal montoTotal;

  @Column(name = "observaciones")
  @Size(max = 255)
  private String observaciones;

  @NotNull
  @ManyToOne
  @JoinColumn(name = "id_caja", referencedColumnName = "id_caja")
  private Cajas idCaja;

  @OneToMany(mappedBy = "idRecibo", orphanRemoval = true, cascade = CascadeType.ALL)
  private List<RecibosDetalle> recibosDetalles;
}
