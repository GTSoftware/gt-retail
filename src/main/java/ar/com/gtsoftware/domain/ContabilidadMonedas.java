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

import java.util.List;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/** @author rodrigo */
@Entity
@Table(name = "contabilidad_monedas")
@Getter
@Setter
public class ContabilidadMonedas extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "contabilidad_monedas_id_moneda")
  @SequenceGenerator(
      allocationSize = 1,
      initialValue = 1,
      name = "contabilidad_monedas_id_moneda",
      sequenceName = "contabilidad_monedas_id_moneda_seq")
  @Basic(optional = false)
  @Column(name = "id_moneda", nullable = false, updatable = false)
  private Long id;

  @Basic(optional = false)
  @NotNull
  @Size(min = 1, max = 100)
  @Column(name = "nombre_moneda")
  private String nombreMoneda;

  @Size(max = 10)
  @Column(name = "nombre_corto_moneda")
  private String nombreCortoMoneda;

  @Size(max = 5)
  @Column(name = "simbolo_moneda")
  private String simboloMoneda;

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "idMoneda")
  private List<BancosCuentas> bancosCuentasList;
}
