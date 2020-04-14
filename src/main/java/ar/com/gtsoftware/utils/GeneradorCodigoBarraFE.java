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
package ar.com.gtsoftware.utils;

import ar.com.gtsoftware.domain.FiscalLibroIvaVentas;

import java.text.SimpleDateFormat;

/**
 * @author Rodrigo M. Tato Rothamel mailto:rotatomel@gmail.com
 */
public class GeneradorCodigoBarraFE {

    private static final SimpleDateFormat AMD = new SimpleDateFormat("yyyyMMdd");

    public static String calcularCodigoBarras(FiscalLibroIvaVentas registro, String cuitEmpresa) {
        StringBuilder result = new StringBuilder();

        result.append(cuitEmpresa)
                .append(registro.getCodigoTipoComprobante().getCodigoTipoComprobante())
                .append(registro.getPuntoVentaFactura())
                .append(registro.getCae())
                .append(AMD.format(registro.getFechaVencimientoCae()));
        int digito = calcularDigitoVerificador(result.toString());

        result.append(String.valueOf(digito));
        return result.toString();
    }

    private static int calcularDigitoVerificador(String s) {
        int indice, i;
        int n = s.length();
        int sumaPares = 0, sumaImpares = 0;
        for (indice = 0, i = 0; i < n; i++) {
            char ch = s.charAt(i);
            if (Character.isSpaceChar(ch)) {
                continue;
            }
            int x = ch - '0';
            // La primer posición es impar, porque cuentan desde 1.
            if ((indice % 2) == 1) {
                sumaPares += x;
            } else {
                sumaImpares += x;
            }
            indice++;
        }
        return (10 - ((sumaImpares * 3 + sumaPares) % 10)) % 10;
    }
}
