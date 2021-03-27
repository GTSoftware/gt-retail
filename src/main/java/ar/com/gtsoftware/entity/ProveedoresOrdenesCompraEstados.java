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

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * Representa a los posibles estados por los que puede pasar una Orden de Compra
 *
 * @author Rodrigo M. Tato Rothamel
 */
@Entity
@Table(name = "proveedores_ordenes_compra_estados")
@Getter
@Setter
public class ProveedoresOrdenesCompraEstados extends BaseEntity {

  @Id
  @Basic(optional = false)
  @Column(name = "id_estado_orden_compra", nullable = false, updatable = false)
  private Long id;

  @Basic(optional = false)
  @NotNull
  @Size(min = 1, max = 50)
  @Column(name = "nombre_estado_orden_compra")
  private String nombreEstado;
}
