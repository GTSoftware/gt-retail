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

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * @author rodrigo
 */
@Entity
@Table(name = "bancos_cuenta_corriente")
@Getter
@Setter
public class BancosCuentaCorriente extends BaseEntity {

  @Id
  @GeneratedValue(
      strategy = GenerationType.SEQUENCE,
      generator = "bancos_cuenta_corriente_id_movimiento")
  @SequenceGenerator(
      allocationSize = 1,
      initialValue = 1,
      name = "bancos_cuenta_corriente_id_movimiento",
      sequenceName = "bancos_cuenta_corriente_id_movimiento_seq")
  @Basic(optional = false)
  @Column(name = "id_movimiento", nullable = false, updatable = false)
  private Long id;

  @Basic(optional = false)
  @NotNull
  @Column(name = "fecha_movimiento")
  @Temporal(TemporalType.TIMESTAMP)
  private Date fechaMovimiento;
  // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these
  // annotations to enforce field validation
  @Basic(optional = false)
  @NotNull
  @Column(name = "importe_movimiento")
  private BigDecimal importeMovimiento;

  @Basic(optional = false)
  @NotNull
  @Size(min = 1, max = 200)
  @Column(name = "descripcion_movimiento")
  private String descripcionMovimiento;

  @Basic(optional = false)
  @NotNull
  @Column(name = "id_registro_contable")
  private int idRegistroContable;

  @JoinColumn(
      name = "id_cuenta_banco",
      referencedColumnName = "id_cuenta_banco",
      columnDefinition = "int4")
  @ManyToOne(optional = false)
  private BancosCuentas idCuentaBanco;
}
