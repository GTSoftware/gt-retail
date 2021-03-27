/*
 * Copyright 2016 GT Software.
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
 * Los planes de pago para cada tipo de pago
 *
 * @author Rodrigo M. Tato Rothamel mailto:rotatomel@gmail.com
 */
@Entity
@Table(name = "negocio_planes_pago")
@Getter
@Setter
public class NegocioPlanesPago extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "negocio_planes_pago_id_plan")
  @SequenceGenerator(
      allocationSize = 1,
      initialValue = 1,
      name = "negocio_planes_pago_id_plan",
      sequenceName = "negocio_planes_pago_id_plan_seq")
  @Basic(optional = false)
  @Column(name = "id_plan", nullable = false, updatable = false)
  private Long id;

  @JoinColumn(name = "id_forma_pago", referencedColumnName = "id_forma_pago")
  @ManyToOne
  @NotNull
  private NegocioFormasPago idFormaPago;

  @Column(name = "nombre")
  @Size(max = 60)
  @NotNull
  private String nombre;

  @Column(name = "fecha_activo_desde")
  @Temporal(javax.persistence.TemporalType.TIMESTAMP)
  @NotNull
  private Date fechaActivoDesde;

  @Column(name = "fecha_activo_hasta")
  @Temporal(javax.persistence.TemporalType.TIMESTAMP)
  @NotNull
  private Date fechaActivoHasta;

  @ManyToMany
  @JoinTable(
      name = "negocio_planes_pago_listas",
      joinColumns = @JoinColumn(name = "id_plan", referencedColumnName = "id_plan"),
      inverseJoinColumns =
          @JoinColumn(name = "id_lista_precio", referencedColumnName = "id_lista_precio"))
  private List<ProductosListasPrecios> listasPrecioHabilitadas;

  @OneToMany(mappedBy = "idPlan", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<NegocioPlanesPagoDetalle> negocioPlanesPagoDetalles;
}
