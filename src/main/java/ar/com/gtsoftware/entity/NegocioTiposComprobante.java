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
import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * Representa a los tipos de comprobante soportados por el sistema
 *
 * @author Rodrigo M. Tato Rothamel mailto:rotatomel@gmail.com
 */
@Entity
@Table(name = "negocio_tipos_comprobante")
@Getter
@Setter
public class NegocioTiposComprobante extends BaseEntity {

  @Id
  @Basic(optional = false)
  @Column(name = "id_negocio_tipo_comprobante", nullable = false, updatable = false)
  private Long id;

  @Basic(optional = false)
  @NotNull
  @Size(min = 1, max = 30)
  @Column(name = "nombre_comprobante")
  private String nombreComprobante;

  @Max(1)
  @Min(-1)
  @NotNull
  @Column(name = "signo")
  private BigDecimal signo;

  @Column(name = "activo")
  private boolean activo;
}
