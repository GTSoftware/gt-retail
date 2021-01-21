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
package ar.com.gtsoftware.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * Representa las formas de pago del negocio
 *
 * @author Rodrigo Tato <rotatomel@gmail.com>
 */
@Entity
@Table(name = "negocio_formas_pago")
@Getter
@Setter
public class NegocioFormasPago extends BaseEntity {

  @Id
  @GeneratedValue(
      strategy = GenerationType.SEQUENCE,
      generator = "ventas_formas_pago_id_forma_pago")
  @SequenceGenerator(
      allocationSize = 1,
      initialValue = 1,
      name = "ventas_formas_pago_id_forma_pago",
      sequenceName = "ventas_formas_pago_id_forma_pago_seq")
  @Basic(optional = false)
  @Column(name = "id_forma_pago", nullable = false, updatable = false)
  private Long id;

  @Basic(optional = false)
  @NotNull
  @Size(min = 1, max = 60)
  @Column(name = "nombre_forma_pago")
  private String nombreFormaPago;

  @Size(max = 10)
  @Column(name = "nombre_corto")
  private String nombreCorto;

  @Basic(optional = false)
  @NotNull
  @Column(name = "venta")
  private boolean venta;

  @Basic(optional = false)
  @NotNull
  @Column(name = "compra")
  private boolean compra;

  @Basic(optional = false)
  @NotNull
  @Column(name = "requiere_plan")
  private boolean requierePlan;

  @Basic(optional = false)
  @NotNull
  @Column(name = "requiere_valores")
  private boolean requiereValores;
}
