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
import java.util.Date;
import java.util.List;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * Arqueos de caja realizadados por los cajeros. Delimitan un cierre y apertura de caja.
 *
 * @author Rodrigo M. Tato Rothamel mailto:rotatomel@gmail.com
 */
@Entity
@Table(name = "caja_arqueos")
@Getter
@Setter
public class CajasArqueos extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "caja_arqueos_id_arqueo")
  @SequenceGenerator(
      allocationSize = 1,
      initialValue = 1,
      name = "caja_arqueos_id_arqueo",
      sequenceName = "caja_arqueos_id_arqueo_seq")
  @Basic(optional = false)
  @Column(name = "id_arqueo", nullable = false, updatable = false)
  private Long id;

  @NotNull
  @ManyToOne
  @JoinColumn(name = "id_usuario_arqueo", referencedColumnName = "id_usuario")
  private Usuarios idUsuario;

  @ManyToOne
  @JoinColumn(name = "id_usuario_control", referencedColumnName = "id_usuario")
  private Usuarios idUsuarioControl;

  @NotNull
  @ManyToOne
  @JoinColumn(name = "id_sucursal", referencedColumnName = "id_sucursal")
  private Sucursales idSucursal;

  @NotNull
  @ManyToOne
  @JoinColumn(name = "id_caja", referencedColumnName = "id_caja")
  private Cajas idCaja;

  @NotNull
  @Column(name = "fecha_arqueo")
  @Temporal(javax.persistence.TemporalType.TIMESTAMP)
  private Date fechaArqueo;

  @Column(name = "fecha_control")
  @Temporal(javax.persistence.TemporalType.TIMESTAMP)
  private Date fechaControl;

  @NotNull
  @Column(name = "saldo_inicial")
  private BigDecimal saldoInicial;

  @NotNull
  @Column(name = "saldo_final")
  private BigDecimal saldoFinal;

  @Column(name = "observaciones_control")
  @Size(max = 500)
  private String obvervacionesControl;

  @OneToMany(mappedBy = "idArqueo", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<CajasArqueosDetalle> detalleArqueo;
}
