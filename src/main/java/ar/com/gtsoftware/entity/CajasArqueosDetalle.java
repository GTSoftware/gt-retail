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

import java.math.BigDecimal;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * Detalle del arqueo.
 *
 * @author Rodrigo M. Tato Rothamel mailto:rotatomel@gmail.com
 */
@Entity
@Table(name = "caja_arqueos_detalle")
@Getter
@Setter
public class CajasArqueosDetalle extends BaseEntity {

  @Id
  @GeneratedValue(
      strategy = GenerationType.SEQUENCE,
      generator = "caja_arqueos_detalle_id_arqueo_detalle")
  @SequenceGenerator(
      allocationSize = 1,
      initialValue = 1,
      name = "caja_arqueos_detalle_id_arqueo_detalle",
      sequenceName = "caja_arqueos_detalle_id_arqueo_detalle_seq")
  @Basic(optional = false)
  @Column(name = "id_arqueo_detalle", nullable = false, updatable = false)
  private Long id;

  @NotNull
  @ManyToOne
  @JoinColumn(name = "id_arqueo", referencedColumnName = "id_arqueo")
  private CajasArqueos idArqueo;

  @ManyToOne
  @JoinColumn(name = "id_forma_pago", referencedColumnName = "id_forma_pago")
  private NegocioFormasPago idFormaPago;

  @NotNull
  @Column(name = "monto_sistema")
  private BigDecimal montoSistema;

  @NotNull
  @Column(name = "monto_declarado")
  private BigDecimal montoDeclarado;

  @NotNull
  @Column(name = "diferencia")
  private BigDecimal diferencia;

  @Column(name = "descargo")
  @Size(max = 200)
  private String descargo;
}
