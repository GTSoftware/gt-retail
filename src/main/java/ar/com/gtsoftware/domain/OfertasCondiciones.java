/*
 * Copyright 2018 GT Software.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package ar.com.gtsoftware.domain;

import ar.com.gtsoftware.rules.Campo;
import ar.com.gtsoftware.rules.Operacion;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;


@Entity
@Table(name = "ofertas_condiciones")
@Getter
@Setter
public class OfertasCondiciones extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ofertas_condiciones_id_oferta_condicion")
    @SequenceGenerator(allocationSize = 1, initialValue = 1, name = "ofertas_condiciones_id_oferta_condicion",
            sequenceName = "ofertas_condiciones_id_oferta_condicion_seq")
    @Basic(optional = false)
    @Column(name = "id_oferta_condicion", nullable = false, updatable = false)
    private Long id;

    @JoinColumn(name = "id_oferta", referencedColumnName = "id_oferta")
    @ManyToOne(optional = false)
    @NotNull
    private Ofertas idOferta;

    @Column(name = "operacion")
    @Enumerated(EnumType.STRING)
    @NotNull
    private Operacion operacion;

    @Column(name = "campo")
    @Enumerated(EnumType.STRING)
    @NotNull
    private Campo campo;

    @Column(name = "valor")
    @NotNull
    private String valor;

}
