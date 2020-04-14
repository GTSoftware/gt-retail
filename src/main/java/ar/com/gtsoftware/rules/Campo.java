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

@Getter
public enum Campo {

    DESCRIPCION("La descripcion del producto",
            String.class,
            "descripcion"),

    ID_PRODUCTO("El id del producto",
            Long.class,
            "idProducto.id"),

    ID_PROVEEDOR("El id del proveedor habitual",
            Long.class,
            "idProducto.idProveedorHabitual.id"),

    NOMBRE_RUBRO("El nombre del rubro",
            String.class,
            "idProducto.idRubro.nombreRubro"),

    ID_RUBRO("El id del rubro",
            Long.class,
            "idProducto.idRubro.id"),

    NOMBRE_SUB_RUBRO("El nombre del sub-rubro",
            String.class,
            "idProducto.idSubRubro.nombreSubRubro"),

    ID_SUB_RUBRO("El id del sub-rubro",
            Long.class,
            "idProducto.idSubRubro.id"),

    ID_MARCA("El id de marca",
            Long.class,
            "idProducto.idMarca.id"),

    NOMBRE_MARCA("El nombre de marca",
            String.class,
            "idProducto.idMarca.nombreMarca"),

    CANTIDAD("La cantidad de venta",
            BigDecimal.class,
            "cantidad"),

    PRECIO_UNITARIO("El precio unitario del producto",
            BigDecimal.class,
            "precioUnitario");

    private final String descripcionCampo;
    private final Class clase;
    private final String ruta;


    Campo(String descripcionCampo, Class clase, String ruta) {
        this.descripcionCampo = descripcionCampo;
        this.clase = clase;
        this.ruta = ruta;
    }

}
