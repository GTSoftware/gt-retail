/*
 * Copyright 2015 GT Software.
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
package ar.com.gtsoftware.search;

import java.io.Serializable;

/**
 * Clase para mantener la información del tipo de orden de cada campo
 *
 * @author Rodrigo Tato <rotatomel@gmail.com>
 */
public class SortField implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String fieldName;
    private boolean ascending;

    public SortField(String fieldName, boolean ascending) {
        this.fieldName = fieldName;
        this.ascending = ascending;
    }

    public SortField() {
    }

    /**
     * Retorna el nombre del campo por el cual ordenar
     *
     * @return
     */
    public String getFieldName() {
        return fieldName;
    }

    /**
     * Establece el nombre del campo por el cual se va a ordenar
     *
     * @param fieldName
     */
    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    /**
     * Determina si es en orden ascendente o descendente. true es ascendente,
     * false descendente
     *
     * @return
     */
    public boolean isAscending() {
        return ascending;
    }

    /**
     * Establece si es en orden ascendente o descendente
     *
     * @param ascending
     */
    public void setAscending(boolean ascending) {
        this.ascending = ascending;
    }

}
