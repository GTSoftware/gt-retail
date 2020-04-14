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

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author Rodrigo M. Tato Rothamel <rotatomel@gmail.com>
 */
@Entity
@Table(name = "productos_marcas")
@Getter
@Setter
public class ProductosMarcas extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "productos_marcas_id_marca")
    @SequenceGenerator(allocationSize = 1, initialValue = 1, name = "productos_marcas_id_marca",
            sequenceName = "productos_marcas_id_marca_seq")
    @Basic(optional = false)
    @Column(name = "id_marca", nullable = false, updatable = false)
    private Long id;

    @NotNull
    @Column(name = "nombre_marca", length = 100)
    @Size(min = 1, max = 100)
    private String nombreMarca;

}
