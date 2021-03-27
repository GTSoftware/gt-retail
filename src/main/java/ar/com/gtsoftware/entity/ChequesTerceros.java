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

import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * Cheques de terceros recibidos
 *
 * @author Rodrigo M. Tato Rothamel mailto:rotatomel@gmail.com
 */
@Entity
@Table(name = "cheques_terceros")
@Getter
@Setter
public class ChequesTerceros extends Valores {

  @NotNull
  @Column(name = "nro_cheque")
  @Size(max = 30, min = 1)
  private String nroCheque;

  @NotNull
  @Column(name = "cuit_originante")
  @Size(max = 11, min = 1)
  private String cuitOriginante;

  @NotNull
  @Column(name = "razon_social_originante")
  @Size(max = 200, min = 1)
  private String razonSocialOriginante;

  @NotNull
  @Column(name = "fecha_origen")
  @Temporal(javax.persistence.TemporalType.DATE)
  private Date fechaOrigen;

  @Column(name = "fecha_vencimiento")
  @Temporal(javax.persistence.TemporalType.DATE)
  private Date fechaVencimiento;

  @Column(name = "fecha_cobro")
  @Temporal(javax.persistence.TemporalType.TIMESTAMP)
  private Date fechaCobro;

  @NotNull
  @ManyToOne
  @JoinColumn(name = "id_banco", referencedColumnName = "id_banco")
  private Bancos idBanco;

  @Column(name = "notas")
  @Size(max = 255)
  private String notas;
}
