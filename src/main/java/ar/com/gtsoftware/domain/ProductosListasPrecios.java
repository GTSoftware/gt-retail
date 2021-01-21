/*
 * Copyright 2015 GT Software.
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

import java.util.List;
import javax.persistence.*;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/** @author Rodrigo M. Tato Rothamel <rotatomel@gmail.com> */
@Entity
@Table(name = "productos_listas_precios")
@Getter
@Setter
public class ProductosListasPrecios extends BaseEntity {

  @Id
  @Basic(optional = false)
  @Column(name = "id_lista_precio", nullable = false, updatable = false)
  private Long id;

  @Basic(optional = false)
  @Column(name = "nombre_lista")
  @Size(max = 50)
  private String nombreLista;

  @Basic(optional = false)
  @Column(name = "activa")
  private boolean activa;

  @ManyToMany(mappedBy = "listasPrecioHabilitadas")
  private List<NegocioPlanesPago> planesPagoAsociados;
}
