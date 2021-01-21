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
package ar.com.gtsoftware.domain;

import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/** @author rodrigo */
@Entity
@Table(name = "personas_imagenes")
@Getter
@Setter
public class PersonasImagenes extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "personas_imagenes_id_imagen")
  @SequenceGenerator(
      allocationSize = 1,
      initialValue = 1,
      name = "personas_imagenes_id_imagen",
      sequenceName = "personas_imagenes_id_imagen_seq")
  @Basic(optional = false)
  @Column(name = "id_imagen", nullable = false, updatable = false)
  private Long id;

  @Basic(optional = false)
  @NotNull
  @Column(name = "fecha_alta")
  @Temporal(TemporalType.TIMESTAMP)
  private Date fechaAlta;

  @Size(max = 1024)
  @Column(name = "observaciones")
  private String observaciones;

  @JoinColumn(
      name = "id_tipo_imagen",
      referencedColumnName = "id_tipo_imagen",
      columnDefinition = "int4")
  @ManyToOne(optional = false)
  private PersonasTiposImagenes idTipoImagen;

  @JoinColumn(name = "id_persona", referencedColumnName = "id_persona", columnDefinition = "int4")
  @ManyToOne(optional = false)
  private Personas idPersona;
}
