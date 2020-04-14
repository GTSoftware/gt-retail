/*
 * Copyright 2017 GT Software.
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
package ar.com.gtsoftware.dto;

import ar.com.gtsoftware.dto.domain.ChequesTercerosDto;
import ar.com.gtsoftware.dto.domain.ComprobantesPagosDto;
import ar.com.gtsoftware.dto.domain.CuponesDto;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Relaciona un pago de comprobante junto con el valor (de ser necesario) requerido para la forma de pago.
 *
 * @author Rodrigo M. Tato Rothamel mailto:rotatomel@gmail.com
 */
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class PagoValorDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @EqualsAndHashCode.Include
    private final int item;
    private ComprobantesPagosDto pago;
    private CuponesDto cupon;
    private ChequesTercerosDto cheque;
    @Builder.Default
    private boolean montoEditable = false;
    private BigDecimal montoMaximo;
    private BigDecimal montoRealPagado;
    private BigDecimal montoMinimoConRedondeo;
    private BigDecimal montoMaximoConRedondeo;

    public BigDecimal getMontoRealPagadoConSigno() {
        return pago.getIdComprobante().getTipoComprobante().getSigno().multiply(montoRealPagado);
    }

}
