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
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/** @author rodrigo */
@Entity
@Table(name = "depositos")
@Getter
@Setter
public class Depositos extends BaseEntity {

  private static final String BUSINESS_STRING = "[%d] %s Suc: %s";

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "depositos_id_deposito")
  @SequenceGenerator(
      allocationSize = 1,
      initialValue = 1,
      name = "depositos_id_deposito",
      sequenceName = "depositos_id_deposito_seq")
  @Basic(optional = false)
  @Column(name = "id_deposito", nullable = false, updatable = false)
  private Long id;

  @Basic(optional = false)
  @NotNull
  @Size(min = 1, max = 60)
  @Column(name = "nombre_deposito")
  private String nombreDeposito;

  @Size(max = 500)
  @Column(name = "direccion")
  private String direccion;

  @Basic(optional = false)
  @NotNull
  @Column(name = "fecha_alta")
  @Temporal(TemporalType.TIMESTAMP)
  private Date fechaAlta;

  @Basic(optional = false)
  @NotNull
  @Column(name = "activo")
  private boolean activo;
  //    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idDepositoMovimiento")
  //    private List<StockMovimientos> stockMovimientosList;
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

  @JoinColumn(name = "id_sucursal", referencedColumnName = "id_sucursal", columnDefinition = "int4")
  @ManyToOne
  private Sucursales idSucursal;

  @PrePersist
  protected void onCreate() {
    fechaAlta = new Date();
  }

  public String getBusinessString() {
    return String.format(
        BUSINESS_STRING, this.getId(), nombreDeposito, idSucursal.getBusinessString());
  }
}
