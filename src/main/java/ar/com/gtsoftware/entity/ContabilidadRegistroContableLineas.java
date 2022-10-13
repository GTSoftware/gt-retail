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
@Table(name = "contabilidad_registro_contable_lineas")
@Getter
@Setter
public class ContabilidadRegistroContableLineas extends BaseEntity {

  @Id
  @GeneratedValue(
      strategy = GenerationType.SEQUENCE,
      generator = "contabilidad_registro_contable_lineas_id_linea_registro")
  @SequenceGenerator(
      allocationSize = 1,
      initialValue = 1,
      name = "contabilidad_registro_contable_lineas_id_linea_registro",
      sequenceName = "contabilidad_registro_contable_lineas_id_linea_registro_seq")
  @Basic(optional = false)
  @Column(name = "id_linea_registro", nullable = false, updatable = false)
  private Long id;

  @Size(max = 1024)
  @Column(name = "descripcion_linea")
  private String descripcionLinea;
  // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these
  // annotations to enforce field validation
  @Basic(optional = false)
  @NotNull
  @Column(name = "cantidad")
  private BigDecimal cantidad;

  @Size(max = 20)
  @Column(name = "unidad_medida")
  private String unidadMedida;

  @Basic(optional = false)
  @NotNull
  @Column(name = "fecha_vencimiento")
  @Temporal(TemporalType.TIMESTAMP)
  private Date fechaVencimiento;

  @Basic(optional = false)
  @NotNull
  @Column(name = "importe_debe")
  private BigDecimal importeDebe;

  @Basic(optional = false)
  @NotNull
  @Column(name = "importe_haber")
  private BigDecimal importeHaber;

  @JoinColumn(
      name = "id_registro_contable",
      referencedColumnName = "id_registro",
      columnDefinition = "int4")
  @ManyToOne(optional = false)
  private ContabilidadRegistroContable idRegistroContable;

  @JoinColumn(name = "id_cuenta", referencedColumnName = "id_cuenta", columnDefinition = "int4")
  @ManyToOne(optional = false)
  private ContabilidadPlanCuentas idCuenta;
}
