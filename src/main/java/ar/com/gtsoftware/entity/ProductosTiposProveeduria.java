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

/**
 * @author rodrigo
 */
@Entity
@Table(name = "productos_tipos_proveeduria")
@Getter
@Setter
public class ProductosTiposProveeduria extends BaseEntity {

  @Id
  @GeneratedValue(
      strategy = GenerationType.SEQUENCE,
      generator = "productos_tipos_proveeduria_id_tipo_proveeduria")
  @SequenceGenerator(
      allocationSize = 1,
      initialValue = 1,
      name = "productos_tipos_proveeduria_id_tipo_proveeduria",
      sequenceName = "productos_tipos_proveeduria_id_tipo_proveeduria_seq")
  @Basic(optional = false)
  @Column(name = "id_tipo_proveeduria", nullable = false, updatable = false)
  private Long id;

  @Size(max = 60)
  @Column(name = "nombre_tipo_proveeduria")
  private String nombreTipoProveeduria;

  @Basic(optional = false)
  @NotNull
  @Column(name = "puede_comprarse")
  private boolean puedeComprarse;

  @Basic(optional = false)
  @NotNull
  @Column(name = "puede_venderse")
  private boolean puedeVenderse;

  @Basic(optional = false)
  @NotNull
  @Column(name = "control_stock")
  private boolean controlStock;

  @Basic(optional = false)
  @NotNull
  @Column(name = "cambiar_precio_venta")
  private boolean cambiarPrecioVenta;

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "idTipoProveeduria")
  private List<Productos> productosList;
}
