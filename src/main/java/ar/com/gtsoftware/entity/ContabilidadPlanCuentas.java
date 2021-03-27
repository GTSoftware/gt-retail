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

import java.util.List;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/** @author rodrigo */
@Entity
@Table(name = "contabilidad_plan_cuentas")
@Getter
@Setter
public class ContabilidadPlanCuentas extends BaseEntity {

  @Id
  @GeneratedValue(
      strategy = GenerationType.SEQUENCE,
      generator = "contabilidad_plan_cuentas_id_cuenta")
  @SequenceGenerator(
      allocationSize = 1,
      initialValue = 1,
      name = "contabilidad_plan_cuentas_id_cuenta",
      sequenceName = "contabilidad_plan_cuentas_id_cuenta_seq")
  @Basic(optional = false)
  @Column(name = "id_cuenta", nullable = false, updatable = false)
  private Long id;

  @Basic(optional = false)
  @NotNull
  @Size(min = 1, max = 200)
  @Column(name = "nombre_cuenta")
  private String nombreCuenta;

  @Size(max = 100)
  @Column(name = "numero_cuenta")
  private String numeroCuenta;

  @Size(max = 2000)
  @Column(name = "descripcion_cuenta")
  private String descripcionCuenta;

  @Basic(optional = false)
  @NotNull
  @Column(name = "cuenta_rubro")
  private boolean cuentaRubro;

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "idCuenta")
  private List<ContabilidadRegistroContableLineas> contabilidadRegistroContableLineasList;

  @JoinColumn(
      name = "id_tipo_cuenta",
      referencedColumnName = "id_tipo_cuenta",
      columnDefinition = "int4")
  @ManyToOne(optional = false)
  private ContabilidadTiposCuenta idTipoCuenta;

  @OneToMany(mappedBy = "idCuentaPadre")
  private List<ContabilidadPlanCuentas> contabilidadPlanCuentasList;

  @JoinColumn(
      name = "id_cuenta_padre",
      referencedColumnName = "id_cuenta",
      columnDefinition = "int4")
  @ManyToOne
  private ContabilidadPlanCuentas idCuentaPadre;
}
