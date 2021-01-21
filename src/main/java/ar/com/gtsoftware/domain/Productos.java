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

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "productos", uniqueConstraints = @UniqueConstraint(columnNames = {"codigo_propio"}))
@NamedEntityGraph(
    name = "precios",
    attributeNodes = {@NamedAttributeNode("porcentajes"), @NamedAttributeNode("precios")})
@Getter
@Setter
public class Productos extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "productos_id_producto")
  @SequenceGenerator(
      allocationSize = 1,
      initialValue = 1,
      name = "productos_id_producto",
      sequenceName = "productos_id_producto_seq")
  @Basic(optional = false)
  @Column(name = "id_producto", nullable = false, updatable = false)
  private Long id;

  @Size(max = 100)
  @Column(name = "codigo_propio")
  private String codigoPropio;

  @Size(max = 200)
  @Column(name = "descripcion")
  private String descripcion;

  @Size(max = 2048)
  @Column(name = "observaciones")
  private String observaciones;

  @Basic(optional = false)
  @NotNull
  @Column(name = "fecha_alta")
  private LocalDateTime fechaAlta;

  @Basic(optional = false)
  @NotNull
  @Column(name = "fecha_ultima_modificacion")
  private LocalDateTime fechaUltimaModificacion;

  @Basic(optional = false)
  @NotNull
  @Column(name = "activo")
  private boolean activo;
  // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these
  // annotations to enforce field validation
  @Basic(optional = false)
  @NotNull
  @Column(name = "costo_adquisicion_neto", scale = 4, precision = 19)
  private BigDecimal costoAdquisicionNeto;

  @Basic(optional = false)
  @NotNull
  @Column(name = "costo_final", scale = 4, precision = 19)
  private BigDecimal costoFinal;

  @Basic(optional = false)
  @NotNull
  @Column(name = "annos_amortizacion")
  private int annosAmortizacion;

  @Basic(optional = false)
  @NotNull
  @Column(name = "unidades_compra_unidades_venta")
  @Min(0)
  private BigDecimal unidadesCompraUnidadesVenta;

  @JoinColumn(
      name = "id_tipo_unidad_venta",
      referencedColumnName = "id_tipo_unidad",
      columnDefinition = "int4")
  @ManyToOne(optional = false)
  // @JoinFetch
  private ProductosTiposUnidades idTipoUnidadVenta;

  @JoinColumn(
      name = "id_tipo_unidad_compra",
      referencedColumnName = "id_tipo_unidad",
      columnDefinition = "int4")
  @ManyToOne(optional = false)
  // @JoinFetch
  private ProductosTiposUnidades idTipoUnidadCompra;

  @JoinColumn(
      name = "id_tipo_proveeduria",
      referencedColumnName = "id_tipo_proveeduria",
      columnDefinition = "int4")
  @ManyToOne(optional = false)
  // @JoinFetch
  private ProductosTiposProveeduria idTipoProveeduria;

  @JoinColumn(
      name = "id_sub_rubro",
      referencedColumnName = "id_sub_rubro",
      columnDefinition = "int4")
  @ManyToOne(optional = false)
  // @JoinFetch
  private ProductosSubRubros idSubRubro;

  @JoinColumn(name = "id_rubro", referencedColumnName = "id_rubro", columnDefinition = "int4")
  @ManyToOne(optional = false)
  //    @JoinFetch
  private ProductosRubros idRubro;

  @JoinColumn(
      name = "id_proveedor_habitual",
      referencedColumnName = "id_persona",
      columnDefinition = "int4")
  @ManyToOne
  private Personas idProveedorHabitual;

  @JoinColumn(
      name = "id_alicuota_iva",
      referencedColumnName = "id_alicuota_iva",
      columnDefinition = "int4")
  @ManyToOne(optional = false)
  //  @JoinFetch
  private FiscalAlicuotasIva idAlicuotaIva;

  @JoinColumn(name = "id_marca", referencedColumnName = "id_marca", columnDefinition = "int4")
  @ManyToOne(optional = false)
  // @JoinFetch
  private ProductosMarcas idMarca;

  @OneToMany(orphanRemoval = true, mappedBy = "idProducto", cascade = CascadeType.ALL)
  //    @OrderBy(value = "idListaPrecios")
  private List<ProductosPrecios> precios;

  @OneToMany(mappedBy = "idProducto", orphanRemoval = true, cascade = CascadeType.ALL)
  private List<ProductosPorcentajes> porcentajes;

  @Size(max = 60)
  @Column(name = "ubicacion")
  private String ubicacion;

  @Size(max = 60)
  @Column(name = "codigo_fabricante")
  private String codigoFabricante;

  @Basic(optional = false)
  @NotNull
  @Column(name = "stock_minimo", scale = 2, precision = 19)
  private BigDecimal stockMinimo;
}
