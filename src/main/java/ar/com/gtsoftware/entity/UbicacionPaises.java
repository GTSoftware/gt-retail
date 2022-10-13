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
@Table(name = "ubicacion_paises")
@Getter
@Setter
public class UbicacionPaises extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ubicacion_paises_id_pais")
  @SequenceGenerator(
      allocationSize = 1,
      initialValue = 1,
      name = "ubicacion_paises_id_pais",
      sequenceName = "ubicacion_paises_id_pais_seq")
  @Basic(optional = false)
  @Column(name = "id_pais", nullable = false, updatable = false)
  private Long id;

  @Basic(optional = false)
  @NotNull
  @Size(min = 1, max = 60)
  @Column(name = "nombre_pais")
  private String nombrePais;

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "idPais")
  private List<UbicacionProvincias> ubicacionProvinciasList;
}
