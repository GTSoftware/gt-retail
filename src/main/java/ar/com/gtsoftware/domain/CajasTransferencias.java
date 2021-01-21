/*
 * Copyright 2018 GT Software.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package ar.com.gtsoftware.domain;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * Transferencia de valores entre cajas
 *
 * @author Rodrigo M. Tato Rothamel mailto:rotatomel@gmail.com
 */
@Entity
@Table(name = "cajas_transferencias")
@Getter
@Setter
public class CajasTransferencias extends BaseEntity {

  @Id
  @GeneratedValue(
      strategy = GenerationType.SEQUENCE,
      generator = "cajas_transferencias_id_transferencia")
  @SequenceGenerator(
      allocationSize = 1,
      initialValue = 1,
      name = "cajas_transferencias_id_transferencia",
      sequenceName = "cajas_transferencias_id_transferencia_seq")
  @Basic(optional = false)
  @Column(name = "id_transferencia", nullable = false, updatable = false)
  private Long id;

  @NotNull
  @JoinColumn(name = "id_caja_origen", referencedColumnName = "id_caja")
  @ManyToOne
  private Cajas idCajaOrigen;

  @NotNull
  @JoinColumn(name = "id_caja_destino", referencedColumnName = "id_caja")
  @ManyToOne
  private Cajas idCajaDestino;

  @NotNull
  @Column(name = "fecha_transferencia")
  @Temporal(javax.persistence.TemporalType.TIMESTAMP)
  private Date fechaTransferencia;

  @NotNull
  @Column(name = "monto")
  private BigDecimal monto;

  @Column(name = "observaciones")
  @Size(max = 90)
  private String observaciones;

  @NotNull
  @JoinColumn(name = "id_forma_pago", referencedColumnName = "id_forma_pago")
  @ManyToOne
  private NegocioFormasPago idFormaPago;
}
