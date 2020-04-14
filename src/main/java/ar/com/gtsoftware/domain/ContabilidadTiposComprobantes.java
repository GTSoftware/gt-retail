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
import java.util.List;

/**
 * @author rodrigo
 */
@Entity
@Table(name = "contabilidad_tipos_comprobantes")
@Getter
@Setter
public class ContabilidadTiposComprobantes extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "contabilidad_tipos_comprobantes_id_tipo_comprobante")
    @SequenceGenerator(allocationSize = 1, initialValue = 1, name = "contabilidad_tipos_comprobantes_id_tipo_comprobante",
            sequenceName = "contabilidad_tipos_comprobantes_id_tipo_comprobante_seq")
    @Basic(optional = false)
    @Column(name = "id_tipo_comprobante", nullable = false, updatable = false)
    private Long id;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "nombre_tipo")
    private String nombreTipo;
    @Size(max = 2000)
    @Column(name = "descripcion_tipo")
    private String descripcionTipo;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idTipoComprobante")
    private List<ContabilidadRegistroContable> contabilidadRegistroContableList;

}
