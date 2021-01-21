/*
 * Copyright 2018 GT Software.
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

import java.math.BigDecimal;
import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * Clase que almacena la información de el detalle de la factura para el libro de iva compras
 *
 * @author Rodrigo Tato <rotatomel@gmail.com>
 */
@Entity
@Table(name = "fiscal_libro_iva_compras_lineas")
@Getter
@Setter
public class FiscalLibroIvaComprasLineas extends BaseEntity {

  @Id
  @GeneratedValue(
      strategy = GenerationType.SEQUENCE,
      generator = "fiscal_libro_iva_compras_lineas_id_linea_libro")
  @SequenceGenerator(
      allocationSize = 1,
      initialValue = 1,
      name = "fiscal_libro_iva_compras_lineas_id_linea_libro",
      sequenceName = "fiscal_libro_iva_compras_lineas_id_linea_libro_seq")
  @Basic(optional = false)
  @Column(name = "id_linea_libro", nullable = false, updatable = false)
  private Long id;

  // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these
  // annotations to enforce field validation
  @Column(name = "neto_gravado")
  private BigDecimal netoGravado;

  @Column(name = "no_gravado")
  private BigDecimal noGravado;

  @Column(name = "importe_iva")
  private BigDecimal importeIva;

  @JoinColumn(name = "id_registro", referencedColumnName = "id_registro")
  @ManyToOne(optional = false)
  private FiscalLibroIvaCompras idRegistro;

  @JoinColumn(name = "id_alicuota_iva", referencedColumnName = "id_alicuota_iva")
  @ManyToOne(optional = false)
  private FiscalAlicuotasIva idAlicuotaIva;

  @Override
  public String toString() {
    return "ar.com.gtsoftware.model.FiscalLibroIvaComprasLineasDto[ idLineaLibro=" + id + " ]";
  }
}
