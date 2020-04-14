/*
 * Copyright 2019 GT Software.
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

package ar.com.gtsoftware.dto.reportes;


import java.io.Serializable;
import java.util.List;

/**
 * Clase abstracta de la que heredan los reportes
 */
public abstract class GenericReport<T extends Serializable> implements Serializable {

    private final int totalRows;

    private List<T> pageRows;

    public GenericReport(int totalRows) {
        this.totalRows = totalRows;
    }

    public List<T> getPageRows() {
        return pageRows;
    }

    public void setPageRows(List<T> pageRows) {
        this.pageRows = pageRows;
    }

    public int getTotalRows() {
        return totalRows;
    }
}
