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

import java.util.Date;
import java.util.List;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * @author rodrigo
 */
@Entity
@Table(name = "contabilidad_periodos_contables")
@Getter
@Setter
public class ContabilidadPeriodosContables extends BaseEntity {

  @Id
  @GeneratedValue(
      strategy = GenerationType.SEQUENCE,
      generator = "contabilidad_periodos_contables_id_periodo_contable")
  @SequenceGenerator(
      allocationSize = 1,
      initialValue = 1,
      name = "contabilidad_periodos_contables_id_periodo_contable",
      sequenceName = "contabilidad_periodos_contables_id_periodo_contable_seq")
  @Basic(optional = false)
  @Column(name = "id_periodo_contable", nullable = false, updatable = false)
  private Long id;

  @Basic(optional = false)
  @NotNull
  @Size(min = 1, max = 100)
  @Column(name = "nombre_periodo")
  private String nombrePeriodo;

  @Basic(optional = false)
  @NotNull
  @Column(name = "fecha_inicio_periodo")
  @Temporal(TemporalType.TIMESTAMP)
  private Date fechaInicioPeriodo;

  @Basic(optional = false)
  @NotNull
  @Column(name = "fecha_fin_periodo")
  @Temporal(TemporalType.TIMESTAMP)
  private Date fechaFinPeriodo;

  @OneToMany(mappedBy = "idPeriodoContable")
  private List<ContabilidadRegistroContable> contabilidadRegistroContableList;
}
