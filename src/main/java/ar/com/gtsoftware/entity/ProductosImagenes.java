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

import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * @author rodrigo
 */
@Entity
@Table(name = "productos_imagenes")
@Getter
@Setter
public class ProductosImagenes extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "productos_imagenes_id_imagen")
  @SequenceGenerator(
      allocationSize = 1,
      initialValue = 1,
      name = "productos_imagenes_id_imagen",
      sequenceName = "productos_imagenes_id_imagen_seq")
  @Basic(optional = false)
  @Column(name = "id_imagen", nullable = false, updatable = false)
  private Long id;

  @Basic(optional = false)
  @NotNull
  @Column(name = "fecha_alta")
  @Temporal(TemporalType.TIMESTAMP)
  private Date fechaAlta;

  @Size(max = 200)
  @Column(name = "descripcion")
  private String descripcion;

  @Basic(optional = false)
  @NotNull
  @Lob
  @Column(name = "imagen", columnDefinition = "bytea")
  private byte[] imagen;

  @JoinColumn(name = "id_usuario", referencedColumnName = "id_usuario", columnDefinition = "int4")
  @ManyToOne(optional = false)
  private Usuarios idUsuario;

  @JoinColumn(name = "id_producto", referencedColumnName = "id_producto", columnDefinition = "int4")
  @ManyToOne(optional = false)
  private Productos idProducto;
}
