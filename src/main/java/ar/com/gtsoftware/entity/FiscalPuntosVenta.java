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

import ar.com.gtsoftware.enums.TiposPuntosVenta;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlTransient;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

/**
 * Representa a los puntos de venta fiscales asignados a cada sucursal
 *
 * @author Rodrigo M. Tato Rothamel mailto:rotatomel@gmail.com
 */
@Entity
@Table(name = "fiscal_puntos_venta")
@Getter
@Setter
public class FiscalPuntosVenta extends GTEntity<Integer> {

  @Id
  @Basic(optional = false)
  @Column(name = "nro_punto_venta")
  private Integer nroPuntoVenta;

  @Column(name = "activo")
  @Basic(optional = false)
  private boolean activo;

  @Column(name = "descripcion")
  @Size(min = 1, max = 100)
  @NotNull
  private String descripcion;

  @NotNull
  @Column(name = "tipo")
  @Enumerated(EnumType.STRING)
  private TiposPuntosVenta tipo;

  @ManyToOne
  @JoinColumn(name = "id_sucursal", referencedColumnName = "id_sucursal")
  private Sucursales sucursal;

  @Override
  public Integer getId() {
    return nroPuntoVenta;
  }

  @Override
  public Integer calculateId(String id) {
    if (StringUtils.isNotEmpty(id)) {
      return Integer.parseInt(id);
    }
    return null;
  }

  @Override
  @XmlTransient
  public String getStringId() {
    return String.valueOf(nroPuntoVenta);
  }

  @Override
  @XmlTransient
  public boolean isNew() {
    return nroPuntoVenta == null;
  }

  @XmlTransient
  public String getBusinessString() {
    return String.format("%s - %s", StringUtils.leftPad(getStringId(), 4, '0'), tipo);
  }
}
