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

import lombok.*;
import org.apache.commons.lang3.time.DateUtils;

import java.util.Calendar;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReporteVentasSearchFilter extends AbstractReportSearchFilter {

    private Date fechaDesde, fechaHasta;
    private Long idSucursal;

    @Override
    public boolean hasFilter() {
        return idSucursal != null
                || hasFechasFilter();
    }

    public boolean hasFechasFilter() {
        return (fechaDesde != null && fechaHasta != null);
    }

    public void setDefaultDatesValues() {
        Date today = new Date();
        fechaDesde = DateUtils.truncate(today, Calendar.MONTH);
        fechaHasta = DateUtils.addMonths(DateUtils.truncate(today, Calendar.MONTH), 1);
    }
}
