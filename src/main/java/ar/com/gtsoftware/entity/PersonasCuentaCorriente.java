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
import java.time.LocalDateTime;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/** @author rodrigo */
@Entity
@Table(name = "personas_cuenta_corriente")
@Getter
@Setter
public class PersonasCuentaCorriente extends BaseEntity {

  @Id
  @GeneratedValue(
      strategy = GenerationType.SEQUENCE,
      generator = "personas_cuenta_corriente_id_movimiento")
  @SequenceGenerator(
      allocationSize = 1,
      initialValue = 1,
      name = "personas_cuenta_corriente_id_movimiento",
      sequenceName = "personas_cuenta_corriente_id_movimiento_seq")
  @Basic(optional = false)
  @Column(name = "id_movimiento", nullable = false, updatable = false)
  private Long id;

  @Basic(optional = false)
  @NotNull
  @Column(name = "fecha_movimiento")
  private LocalDateTime fechaMovimiento;
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

  @JoinColumn(name = "id_persona", referencedColumnName = "id_persona", columnDefinition = "int4")
  @ManyToOne(optional = false)
  private Personas idPersona;

  @JoinColumn(
      name = "id_registro_contable",
      referencedColumnName = "id_registro",
      columnDefinition = "int4")
  @ManyToOne(optional = true)
  private ContabilidadRegistroContable idRegistroContable;
}
