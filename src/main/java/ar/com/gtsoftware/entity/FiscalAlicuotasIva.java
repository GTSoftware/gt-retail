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

import java.math.BigDecimal;
import java.util.List;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/** @author Rodrigo M. Tato Rothamel <rotatomel@gmail.com> */
@Entity
@Table(name = "fiscal_alicuotas_iva")
@Getter
@Setter
public class FiscalAlicuotasIva extends BaseEntity {

  @Id
  @GeneratedValue(
      strategy = GenerationType.SEQUENCE,
      generator = "fiscal_alicuotas_iva_id_alicuota_iva")
  @SequenceGenerator(
      allocationSize = 1,
      initialValue = 1,
      name = "fiscal_alicuotas_iva_id_alicuota_iva",
      sequenceName = "fiscal_alicuotas_iva_id_alicuota_iva_seq")
  @Basic(optional = false)
  @Column(name = "id_alicuota_iva", nullable = false, updatable = false)
  private Long id;

  @Basic(optional = false)
  @NotNull
  @Size(min = 1, max = 60)
  @Column(name = "nombre_alicuota_iva")
  private String nombreAlicuotaIva;
  // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these
  // annotations to enforce field validation
  @Basic(optional = false)
  @NotNull
  @Column(name = "valor_alicuota")
  private BigDecimal valorAlicuota;

  @Basic(optional = false)
  @NotNull
  @Column(name = "gravar_iva")
  private boolean gravarIva;

  @Basic(optional = false)
  @NotNull
  @Column(name = "activo")
  private boolean activo;

  @Basic(optional = true)
  @Column(name = "fiscal_codigo_alicuota")
  private Integer fiscalCodigoAlicuota;

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "idAlicuotaIva")
  private List<Productos> productosList;

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "idAlicuotaIva")
  private List<FiscalLibroIvaVentasLineas> fiscalLibroIvaVentasLineasList;
}
