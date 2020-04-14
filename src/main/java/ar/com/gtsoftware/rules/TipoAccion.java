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

@Getter
public enum TipoAccion {

    DESCUENTO_PORCENTAJE("Se aplicará el porcentaje sobre el sub-total del ítem.", "Porcentaje"),
    DESCUENTO_MONTO_FIJO("Se aplicará el monto fijo de descuento.", "Monto fijo");

    private final String descripcion;
    private final String nombre;

    TipoAccion(String descripcion, String nombre) {
        this.descripcion = descripcion;
        this.nombre = nombre;
    }
}
