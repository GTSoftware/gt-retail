/*
 * Copyright 2018 GT Software.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package ar.com.gtsoftware.domain;

import ar.com.gtsoftware.rules.TipoAccion;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "ofertas")
@Getter
@Setter
public class Ofertas extends BaseEntity implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ofertas_id_oferta")
  @SequenceGenerator(
      allocationSize = 1,
      initialValue = 1,
      name = "ofertas_id_oferta",
      sequenceName = "ofertas_id_oferta_seq")
  @Basic(optional = false)
  @Column(name = "id_oferta", nullable = false, updatable = false)
  private Long id;

  @NotNull
  @Column(name = "texto_oferta")
  @Size(max = 90)
  private String textoOferta;

  @Enumerated(EnumType.STRING)
  @Column(name = "tipo_accion")
  @NotNull
  private TipoAccion tipoAccion;

  @Column(name = "descuento")
  @NotNull
  private BigDecimal descuento;

  @NotNull
  @Column(name = "vigencia_desde")
  @Temporal(TemporalType.TIMESTAMP)
  private Date vigenciaDesde;

  @NotNull
  @Column(name = "vigencia_hasta")
  @Temporal(TemporalType.TIMESTAMP)
  private Date vigenciaHasta;

  @JoinColumn(name = "id_sucursal", referencedColumnName = "id_sucursal")
  @ManyToOne
  private Sucursales idSucursal;

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "idOferta", orphanRemoval = true)
  private List<OfertasCondiciones> condiciones;
}
