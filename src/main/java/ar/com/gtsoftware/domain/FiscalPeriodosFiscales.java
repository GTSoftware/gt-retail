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
import java.time.LocalDateTime;

/**
 * @author rodrigo
 */
@Entity
@Table(name = "fiscal_periodos_fiscales")
@Getter
@Setter
public class FiscalPeriodosFiscales extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "fiscal_periodos_fiscales_id_periodo_fiscal")
    @SequenceGenerator(allocationSize = 1, initialValue = 1, name = "fiscal_periodos_fiscales_id_periodo_fiscal",
            sequenceName = "fiscal_periodos_fiscales_id_periodo_fiscal_seq")
    @Basic(optional = false)
    @Column(name = "id_periodo_fiscal", nullable = false, updatable = false)
    private Long id;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "nombre_periodo")
    private String nombrePeriodo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_inicio_periodo")
    private LocalDateTime fechaInicioPeriodo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_fin_periodo")
    private LocalDateTime fechaFinPeriodo;
    @Basic(optional = false)
    @Column(name = "periodo_cerrado")
    private boolean periodoCerrado;

}
