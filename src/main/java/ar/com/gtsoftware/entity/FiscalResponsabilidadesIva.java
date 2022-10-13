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

import java.util.List;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Rodrigo M. Tato Rothamel <rotatomel@gmail.com>
 */
@Entity
@Table(name = "fiscal_responsabilidades_iva")
@Getter
@Setter
public class FiscalResponsabilidadesIva extends BaseEntity {

  @Id
  @GeneratedValue(
      strategy = GenerationType.SEQUENCE,
      generator = "fiscal_responsabilidades_iva_id_resoponsabildiad_iva")
  @SequenceGenerator(
      allocationSize = 1,
      initialValue = 1,
      name = "fiscal_responsabilidades_iva_id_resoponsabildiad_iva",
      sequenceName = "fiscal_responsabilidades_iva_id_resoponsabildiad_iva_seq")
  @Basic(optional = false)
  @Column(name = "id_resoponsabildiad_iva", nullable = false, updatable = false)
  private Long id;

  @Basic(optional = false)
  @NotNull
  @Size(min = 1, max = 60)
  @Column(name = "nombre_responsabildiad")
  private String nombreResponsabildiad;

  @Basic(optional = true)
  @Column(name = "fiscal_codigo_responsable")
  private Integer fiscalCodigoResponsable;

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "idResponsabilidadIvaReceptor")
  private List<FiscalLetrasComprobantes> fiscalLetrasComprobantesList;

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "idResponsabilidadIvaEmisor")
  private List<FiscalLetrasComprobantes> fiscalLetrasComprobantesList1;

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "idResponsabilidadIva")
  private List<Personas> personasList;

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "idResponsabilidadIva")
  private List<FiscalLibroIvaVentas> fiscalLibroIvaVentasList;
}
