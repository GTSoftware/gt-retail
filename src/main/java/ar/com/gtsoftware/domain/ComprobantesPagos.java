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

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * Los planes de pago elegidos para cada comprobante
 *
 * @author Rodrigo M. Tato Rothamel mailto:rotatomel@gmail.com
 */
@Entity
@Table(name = "comprobantes_pagos")
@Getter
@Setter
public class ComprobantesPagos extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "comprobantes_pagos_id_pago")
  @SequenceGenerator(
      allocationSize = 1,
      initialValue = 1,
      name = "comprobantes_pagos_id_pago",
      sequenceName = "comprobantes_pagos_id_pago_seq")
  @Basic(optional = false)
  @Column(name = "id_pago", nullable = false, updatable = false)
  private Long id;

  @NotNull
  @ManyToOne
  @JoinColumn(name = "id_comprobante", referencedColumnName = "id_comprobante")
  private Comprobantes idComprobante;

  @NotNull
  @ManyToOne
  @JoinColumn(name = "id_forma_pago", referencedColumnName = "id_forma_pago")
  private NegocioFormasPago idFormaPago;

  @ManyToOne
  @JoinColumn(name = "id_plan", referencedColumnName = "id_plan")
  private NegocioPlanesPago idPlan;

  @ManyToOne
  @JoinColumn(name = "id_detalle_plan", referencedColumnName = "id_detalle_plan")
  private NegocioPlanesPagoDetalle idDetallePlan;

  @NotNull
  @Column(name = "monto_pago")
  private BigDecimal montoPago;

  @NotNull
  @Column(name = "monto_pagado")
  private BigDecimal montoPagado;

  @Column(name = "fecha_pago")
  @Temporal(javax.persistence.TemporalType.TIMESTAMP)
  private Date fechaPago;

  @Column(name = "fecha_ultimo_pago")
  @Temporal(javax.persistence.TemporalType.TIMESTAMP)
  private Date fechaUltimoPago;

  /**
   * Retorna el total con signo del pago
   *
   * @return
   */
  public BigDecimal getMontoPagoConSigno() {
    return idComprobante.getTipoComprobante().getSigno().multiply(montoPago);
  }

  /**
   * Retorna el monto pagado con signo del pago
   *
   * @return
   */
  public BigDecimal getMontoPagadoConSigno() {
    return idComprobante.getTipoComprobante().getSigno().multiply(montoPagado);
  }
}
