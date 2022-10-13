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
@Table(name = "legal_tipos_documento")
@Getter
@Setter
public class LegalTiposDocumento extends BaseEntity {

  @Id
  @GeneratedValue(
      strategy = GenerationType.SEQUENCE,
      generator = "legal_tipos_documento_id_tipo_documento")
  @SequenceGenerator(
      allocationSize = 1,
      initialValue = 1,
      name = "legal_tipos_documento_id_tipo_documento",
      sequenceName = "legal_tipos_documento_id_tipo_documento_seq")
  @Basic(optional = false)
  @Column(name = "id_tipo_documento", nullable = false, updatable = false)
  private Long id;

  @Basic(optional = false)
  @NotNull
  @Size(min = 1, max = 100)
  @Column(name = "nombre_tipo_documento")
  private String nombreTipoDocumento;

  @Basic(optional = false)
  @NotNull
  @Column(name = "cantidad_caracteres_minimo")
  private int cantidadCaracteresMinimo;

  @Basic(optional = false)
  @NotNull
  @Column(name = "cantidad_caracteres_maximo")
  private int cantidadCaracteresMaximo;

  @Basic(optional = true)
  @Column(name = "fiscal_codigo_tipo_documento")
  private Integer fiscalCodigoTipoDocumento;

  @JoinColumn(
      name = "id_tipo_personeria",
      referencedColumnName = "id_tipo_personeria",
      columnDefinition = "int4")
  @ManyToOne(optional = false)
  private LegalTiposPersoneria idTipoPersoneria;

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "idTipoDocumento")
  private List<Personas> personasList;
}
