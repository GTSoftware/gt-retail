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

/** @author Rodrigo Tato <rotatomel@gmail.com> */
@Entity
@Table(name = "sucursales")
@Getter
@Setter
@NamedEntityGraph(name = "depositos", attributeNodes = @NamedAttributeNode("depositosList"))
public class Sucursales extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sucursales_id_sucursal")
  @SequenceGenerator(
      allocationSize = 1,
      initialValue = 1,
      name = "sucursales_id_sucursal",
      sequenceName = "sucursales_id_sucursal_seq")
  @Basic(optional = false)
  @Column(name = "id_sucursal", nullable = false, updatable = false)
  private Long id;

  @Basic(optional = false)
  @NotNull
  @Size(min = 1, max = 100)
  @Column(name = "nombre_sucursal")
  private String nombreSucursal;

  @Size(max = 500)
  @Column(name = "direccion")
  private String direccion;

  @Size(max = 20)
  @Column(name = "telefono_fijo")
  private String telefonoFijo;

  @Basic(optional = false)
  @NotNull
  @Column(name = "fecha_alta")
  @Temporal(TemporalType.TIMESTAMP)
  private Date fechaAlta;

  @Basic(optional = false)
  @NotNull
  @Column(name = "activo")
  private boolean activo;

  @JoinColumn(
      name = "id_provincia",
      referencedColumnName = "id_provincia",
      columnDefinition = "int4")
  @ManyToOne(optional = false)
  private UbicacionProvincias idProvincia;

  @JoinColumn(name = "id_pais", referencedColumnName = "id_pais", columnDefinition = "int4")
  @ManyToOne(optional = false)
  private UbicacionPaises idPais;

  @JoinColumn(
      name = "id_localidad",
      referencedColumnName = "id_localidad",
      columnDefinition = "int4")
  @ManyToOne(optional = false)
  private UbicacionLocalidades idLocalidad;

  @OneToMany(mappedBy = "idSucursal")
  private List<Depositos> depositosList;

  public String getBusinessString() {
    return String.format("[%d] %s", getId(), nombreSucursal);
  }
}
