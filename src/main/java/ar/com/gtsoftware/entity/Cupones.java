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

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

/**
 * CuponesDto de tarjetas de crédito o débito
 *
 * @author Rodrigo M. Tato Rothamel mailto:rotatomel@gmail.com
 */
@Entity
@Table(name = "cupones")
@Getter
@Setter
public class Cupones extends Valores {

  @NotNull
  @Column(name = "nro_cupon")
  private Integer nroCupon;

  @Column(name = "codigo_autorizacion")
  private Integer codigoAutorizacion;

  @Column(name = "nro_lote")
  private Integer nroLote;

  @NotNull
  @Column(name = "fecha_origen")
  @Temporal(jakarta.persistence.TemporalType.TIMESTAMP)
  private LocalDateTime fechaOrigen;

  @Column(name = "fecha_presentacion")
  @Temporal(jakarta.persistence.TemporalType.TIMESTAMP)
  private LocalDateTime fechaPresentacion;

  @Column(name = "fecha_acreditacion")
  @Temporal(jakarta.persistence.TemporalType.TIMESTAMP)
  private LocalDateTime fechaAcreditacion;

  @NotNull
  @Column(name = "cant_cuotas")
  private Integer cantCuotas;

  @Column(name = "notas")
  @Size(max = 255)
  private String notas;

  @Column(name = "coeficiente")
  private BigDecimal coeficiente;
}
