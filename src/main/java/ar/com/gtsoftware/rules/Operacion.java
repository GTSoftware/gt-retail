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

import lombok.Getter;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static java.util.Arrays.asList;

@Getter
public enum Operacion {

    IGUAL("Igual a", "==", asList(
            BigDecimal.class,
            Long.class,
            Date.class)),

    DISTINTO("Distinto a", "!=", asList(
            BigDecimal.class,
            Long.class,
            Date.class)),

    MAYOR("Mayor a", ">", asList(
            BigDecimal.class,
            Long.class,
            Date.class)),

    MAYOR_IGUAL("Mayor o igual a", ">=", asList(
            BigDecimal.class,
            Long.class,
            Date.class)),

    MENOR("Menor a", "<", asList(
            BigDecimal.class,
            Long.class,
            Date.class)),

    MENOR_IGUAL("Menor o igual a", "<=", asList(
            BigDecimal.class,
            Long.class,
            Date.class)),

    MULTIPLO("Multiplo", "%", asList(
            BigDecimal.class,
            Long.class)),

    CONTIENE("Contiene a", "?", Collections.singletonList(String.class));

    private final String nombre, operador;
    private final List<Class> tiposSoportados;

    Operacion(String nombre, String operador, List<Class> tiposSoportados) {
        this.nombre = nombre;
        this.operador = operador;
        this.tiposSoportados = tiposSoportados;
    }

    public boolean soportaTipo(Class tipo) {
        return tiposSoportados.stream().anyMatch(x -> x.getName().equalsIgnoreCase(tipo.getName()));
    }

}
