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
package ar.com.gtsoftware.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * Detalle del plan de pago
 *
 * @author Rodrigo M. Tato Rothamel mailto:rotatomel@gmail.com
 */
@Entity
@Table(name = "negocio_planes_pago_detalle")
@Getter
@Setter
public class NegocioPlanesPagoDetalle extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "negocio_planes_pago_detalle_id_detalle_plan")
    @SequenceGenerator(allocationSize = 1, initialValue = 1, name = "negocio_planes_pago_detalle_id_detalle_plan",
            sequenceName = "negocio_planes_pago_detalle_id_detalle_plan_seq")
    @Basic(optional = false)
    @Column(name = "id_detalle_plan", nullable = false, updatable = false)
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "id_plan", referencedColumnName = "id_plan")
    private NegocioPlanesPago idPlan;

    @Column(name = "activo")
    private boolean activo;

    @Column(name = "cuotas")
    private int cuotas;

    @Column(name = "coeficiente_interes")
    @DecimalMax(value = "999999999999999.9999")
    @DecimalMin(value = "0")
    private BigDecimal coeficienteInteres;

}
