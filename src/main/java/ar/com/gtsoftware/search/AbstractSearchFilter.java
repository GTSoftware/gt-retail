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
package ar.com.gtsoftware.search;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase abstracta que representa una agrupación de criterios de filtrado
 *
 * @author Rodrigo Tato <rotatomel@gmail.com>
 * @version 1.0.0
 * @since 1.0.0
 */
public abstract class AbstractSearchFilter implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<SortField> sortFields;

    private String namedEntityGraph;

    /**
     * Devuelve true si existe al menos un filtro de búsqueda establecido.
     *
     * @return true si hay algún filtro para buscar entidades.
     */
    @JsonIgnore
    public boolean hasFilter() {
        return false;
    }

    /**
     * Devuelve true si existen atributos de ordenamientos para las entidades.
     *
     * @return true si hay atributos de ordenamiento.
     */
    public boolean hasOrderFields() {
        return CollectionUtils.isNotEmpty(sortFields);
    }

    /**
     * Devuelve true si existen entity graph
     *
     * @return
     */
    public boolean hasNamedEntityGraph() {
        return StringUtils.isNotEmpty(namedEntityGraph);
    }

    /**
     * Retorna el listado de atributos de ordenamiento.
     *
     * @return
     */
    public List<SortField> getSortFields() {
        return sortFields;
    }

    /**
     * Agrega un nuevo campo de ordenamiento
     *
     * @param sortField
     */
    public void addSortField(SortField sortField) {
        if (sortFields == null) {
            sortFields = new ArrayList<>();
        }
        sortFields.add(sortField);
    }

    /**
     * Método conveniente que evita tener que instanciar un SortField
     *
     * @param field
     * @param ascending
     */
    public void addSortField(String field, boolean ascending) {
        addSortField(new SortField(field, ascending));
    }

    /**
     * Setea en nulo la lista de campos de ordenamiento
     */
    public void clearSortFields() {
        sortFields = null;
    }

    /**
     * Retorna el nombre del Entity graph
     *
     * @return
     */
    public String getNamedEntityGraph() {
        return namedEntityGraph;
    }

    /**
     * Establece el nombre del entity graph
     *
     * @param namedEntityGraph
     */
    public void setNamedEntityGraph(String namedEntityGraph) {
        this.namedEntityGraph = namedEntityGraph;
    }

}
