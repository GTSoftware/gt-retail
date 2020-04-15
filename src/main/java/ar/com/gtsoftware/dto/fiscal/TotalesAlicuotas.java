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
package ar.com.gtsoftware.dto.fiscal;

import ar.com.gtsoftware.dto.domain.FiscalAlicuotasIvaDto;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder
public class TotalesAlicuotas implements Serializable {

    private static final long serialVersionUID = 1L;

    @EqualsAndHashCode.Include
    private FiscalAlicuotasIvaDto alicuota;
    @Builder.Default
    private BigDecimal importeIva = BigDecimal.ZERO;
    @Builder.Default
    private BigDecimal noGravado = BigDecimal.ZERO;
    @Builder.Default
    private BigDecimal netoGravado = BigDecimal.ZERO;
    @Builder.Default
    private BigDecimal importeExento = BigDecimal.ZERO;

    /**
     * Suma los totales de this con los recibidos de subTotal siempre que correspondan a la misma alícuota
     *
     * @param subTotal
     */
    public void add(TotalesAlicuotas subTotal) {
        if (!this.equals(subTotal)) {
            throw new IllegalArgumentException("No se pueden sumar alícuotas diferentes!");
        }
        this.importeExento = this.importeExento.add(subTotal.getImporteExento());
        this.importeIva = this.importeIva.add(subTotal.getImporteIva());
        this.netoGravado = this.netoGravado.add(subTotal.getNetoGravado());
        this.noGravado = this.noGravado.add(subTotal.getNoGravado());
    }
}
