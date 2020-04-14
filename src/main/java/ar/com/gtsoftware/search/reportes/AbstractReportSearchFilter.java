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

package ar.com.gtsoftware.search.reportes;

import java.io.Serializable;

/**
 * Search filter del que deben heredar los search filter para reportes
 */
public class AbstractReportSearchFilter implements Serializable {

    private static final long serialVersionUID = 1L;

    private int pageSize = 30;
    private int firstRow = 0;

    public boolean hasFilter() {
        return false;
    }

    public final int getPageSize() {
        return pageSize;
    }

    public final void setPageSize(int pageSize) {
        if (pageSize <= 0) {
            throw new IllegalArgumentException("Page size must be greater than or equal to zero.");
        }
        this.pageSize = pageSize;
    }

    public final int getFirstRow() {
        return firstRow;
    }

    public final void setFirstRow(int firstRow) {
        if (firstRow < 0) {
            throw new IllegalArgumentException("First row must be greater than zero.");
        }
        this.firstRow = firstRow;
    }
}
