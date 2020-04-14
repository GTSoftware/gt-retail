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

package ar.com.gtsoftware.rules;


import lombok.*;

import javax.validation.constraints.NotNull;

import static ar.com.gtsoftware.rules.Operacion.CONTIENE;
import static ar.com.gtsoftware.rules.Operacion.MULTIPLO;
import static org.apache.commons.lang3.StringUtils.SPACE;
import static org.apache.commons.lang3.StringUtils.isEmpty;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Condicion {

    @EqualsAndHashCode.Include
    private Long id;

    @NotNull
    private Operacion operacion;

    @NotNull
    private Campo campo;

    @NotNull
    private String valor;

    @NotNull
    private OfertaDto idOferta;

    @EqualsAndHashCode.Include
    private transient int nroItem;

    private int version;

    public String buildExpression() throws CondicionIlegalException {
        validarCondicion();

        if (MULTIPLO == operacion) {
            return "(" + campo.getRuta() + buildOperador();
        }

        return campo.getRuta() + buildOperador();

    }

    private void validarCondicion() throws CondicionIlegalException {
        if (operacion == null || campo == null || isEmpty(valor)) {
            throw new CondicionIlegalException("Algunos de los elementos de la condición es nulo!");
        }
        if (!operacion.soportaTipo(campo.getClase())) {
            throw new CondicionIlegalException("El campo:" + campo + " no soporta la operación: " + operacion);
        }

    }

    private String buildOperador() {
        if (CONTIENE == operacion) {
            return ".toUpperCase().contains(\"" + valor.toUpperCase() + "\")";
        }
        if (MULTIPLO == operacion) {
            return SPACE + operacion.getOperador() + SPACE + valor + ") == 0";
        }
        return SPACE + operacion.getOperador() + SPACE + valor;

    }

}
