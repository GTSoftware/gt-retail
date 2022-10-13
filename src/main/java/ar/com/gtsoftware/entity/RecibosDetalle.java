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
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Rodrigo M. Tato Rothamel mailto:rotatomel@gmail.com
 */
@Entity
@Table(name = "recibos_detalle")
@Getter
@Setter
public class RecibosDetalle extends BaseEntity {

  @Id
  @GeneratedValue(
      strategy = GenerationType.SEQUENCE,
      generator = "recibos_detalle_id_detalle_recibo")
  @SequenceGenerator(
      allocationSize = 1,
      initialValue = 1,
      name = "recibos_detalle_id_detalle_recibo",
      sequenceName = "recibos_detalle_id_detalle_recibo_seq")
  @Basic(optional = false)
  @Column(name = "id_detalle_recibo", nullable = false, updatable = false)
  private Long id;

  @NotNull
  @ManyToOne
  @JoinColumn(name = "id_recibo", referencedColumnName = "id_recibo")
  private Recibos idRecibo;

  @ManyToOne
  @JoinColumn(name = "id_comprobante_pago", referencedColumnName = "id_pago")
  private ComprobantesPagos idPagoComprobante;

  @NotNull
  @Column(name = "monto_pagado")
  private BigDecimal montoPagado;

  @NotNull
  @Column(name = "monto_pagado_con_signo")
  private BigDecimal montoPagadoConSigno;

  @NotNull
  @Column(name = "redondeo")
  private BigDecimal redondeo;

  @NotNull
  @ManyToOne
  @JoinColumn(name = "id_forma_pago", referencedColumnName = "id_forma_pago")
  private NegocioFormasPago idFormaPago;

  @ManyToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "id_valor", referencedColumnName = "id_valor")
  @OneToOne
  private Valores idValor;
}
