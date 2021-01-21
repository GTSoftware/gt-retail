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

import java.util.Date;
import java.util.List;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/** @author rodrigo */
@Entity
@Table(name = "contabilidad_registro_contable")
@Getter
@Setter
public class ContabilidadRegistroContable extends BaseEntity {

  @Id
  @GeneratedValue(
      strategy = GenerationType.SEQUENCE,
      generator = "contabilidad_registro_contable_id_registro")
  @SequenceGenerator(
      allocationSize = 1,
      initialValue = 1,
      name = "contabilidad_registro_contable_id_registro",
      sequenceName = "contabilidad_registro_contable_id_registro_seq")
  @Basic(optional = false)
  @Column(name = "id_registro", nullable = false, updatable = false)
  private Long id;

  @Basic(optional = false)
  @NotNull
  @Column(name = "fecha_proceso")
  @Temporal(TemporalType.TIMESTAMP)
  private Date fechaProceso;

  @Size(max = 100)
  @Column(name = "usuario")
  private String usuario;

  @Size(max = 10)
  @Column(name = "letra_comprobante")
  private String letraComprobante;

  @Size(max = 10)
  @Column(name = "punto_venta_comprobante")
  private String puntoVentaComprobante;

  @Size(max = 100)
  @Column(name = "numero_comprobante")
  private String numeroComprobante;

  @Size(max = 11)
  @Column(name = "cuit_emisor_comprobante")
  private String cuitEmisorComprobante;

  @Size(max = 11)
  @Column(name = "cuit_receptor_comprobante")
  private String cuitReceptorComprobante;

  @Column(name = "fecha_comprobante")
  @Temporal(TemporalType.TIMESTAMP)
  private Date fechaComprobante;

  @Column(name = "fecha_vencimiento")
  @Temporal(TemporalType.TIMESTAMP)
  private Date fechaVencimiento;

  @Size(max = 2000)
  @Column(name = "concepto_comprobante")
  private String conceptoComprobante;

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "idRegistroContable")
  private List<ContabilidadRegistroContableLineas> contabilidadRegistroContableLineasList;

  @JoinColumn(
      name = "id_periodo_fiscal",
      referencedColumnName = "id_periodo_fiscal",
      columnDefinition = "int4")
  @ManyToOne
  private FiscalPeriodosFiscales idPeriodoFiscal;

  @JoinColumn(
      name = "id_tipo_operacion",
      referencedColumnName = "id_tipo_operacion",
      columnDefinition = "int4")
  @ManyToOne
  private ContabilidadTiposOperacion idTipoOperacion;

  @JoinColumn(
      name = "id_tipo_comprobante",
      referencedColumnName = "id_tipo_comprobante",
      columnDefinition = "int4")
  @ManyToOne(optional = false)
  private ContabilidadTiposComprobantes idTipoComprobante;

  @JoinColumn(
      name = "id_periodo_contable",
      referencedColumnName = "id_periodo_contable",
      columnDefinition = "int4")
  @ManyToOne
  private ContabilidadPeriodosContables idPeriodoContable;

  @JoinColumn(name = "id_libro", referencedColumnName = "id_libro", columnDefinition = "int4")
  @ManyToOne(optional = false)
  private ContabilidadLibros idLibro;

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "idRegistroContable")
  private List<FiscalLibroIvaVentas> fiscalLibroIvaVentasList;

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "idRegistroContable")
  private List<PersonasCuentaCorriente> personasCuentaCorrienteList;
}
