/*
 * Copyright 2014 GTSoftware
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

import org.apache.commons.lang3.StringUtils;

/**
 * Esta clase provee la funcionalidad de convertir un numero representado en digitos a una representacion en letras.
 * Mejorado para leer centavos
 *
 * @version 1.0
 */
public abstract class NumberToLetterConverter {

    private static final String[] UNIDADES = {"", "UN ", "DOS ", "TRES ",
        "CUATRO ", "CINCO ", "SEIS ", "SIETE ", "OCHO ", "NUEVE ", "DIEZ ",
        "ONCE ", "DOCE ", "TRECE ", "CATORCE ", "QUINCE ", "DIECISEIS",
        "DIECISIETE", "DIECIOCHO", "DIECINUEVE", "VEINTE"};
    private static final String[] DECENAS = {"VEINTI", "TREINTA ", "CUARENTA ",
        "CINCUENTA ", "SESENTA ", "SETENTA ", "OCHENTA ", "NOVENTA ",
        "CIEN "};
    private static final String[] CENTENAS = {"CIENTO ", "DOSCIENTOS ",
        "TRESCIENTOS ", "CUATROCIENTOS ", "QUINIENTOS ", "SEISCIENTOS ",
        "SETECIENTOS ", "OCHOCIENTOS ", "NOVECIENTOS "};

    private static final String MILLONES = "MILLONES ";
    private static final String UN_MILLON = "UN MILLON ";
    private static final String PESOS = " PESOS";
    private static final String UN_CENTAVO = "UN CENTAVO";
    private static final String CENTAVOS = "CENTAVOS";
    private static final String CON = " CON ";
    private static final String CERO = "CERO";
    private static final String UN = "UN";
    private static final String MIL = "MIL ";
    private static final String CIEN = "CIEN";
    private static final String Y = "Y ";

    /**
     * Convierte un numero en representacion numerica a uno en representacion de texto. El numero es valido si esta
     * entre 0 y 999.999.999
     * <p>
     * Creation date 3/05/2006 - 05:37:47 PM
     *
     * @param number Numero a convertir
     * @return Numero convertido a texto
     * @throws NumberFormatException Si el numero esta fuera del rango
     * @since 1.0
     */
    public static String convertNumberToLetter(double number)
            throws NumberFormatException {
        StringBuilder converted = new StringBuilder();

        // Validamos que sea un numero legal
        if (number < 0) {
            throw new IllegalArgumentException("No puedo convertir números negativos");
        }
        double doubleNumber = Math.rint(number * 100) / 100;
        if (doubleNumber > 9999999) {
            throw new IllegalArgumentException(
                    "El número es mayor que 9.999.999, no es posible convertirlo");
        }

        String splitNumber[] = String.valueOf(doubleNumber).replace('.', '#').split("#");

        // Descompone el trio de millones - ¡SGT!
        int millon = Integer.parseInt(String.valueOf(getDigitAt(splitNumber[0],
                8))
                .concat(String.valueOf(getDigitAt(splitNumber[0], 7)))
                .concat(String.valueOf(getDigitAt(splitNumber[0], 6))));
        if (millon == 1) {
            converted.append(UN_MILLON);
        }
        if (millon > 1) {
            converted.append(convertNumber(String.valueOf(millon)))
                    .append(MILLONES);
        }

        // Descompone el trio de miles - ¡SGT!
        int miles = Integer.parseInt(String.valueOf(getDigitAt(splitNumber[0],
                5))
                .concat(String.valueOf(getDigitAt(splitNumber[0], 4)))
                .concat(String.valueOf(getDigitAt(splitNumber[0], 3))));
        if (miles == 1) {
            converted.append(MIL);
        }
        if (miles > 1) {
            converted.append(convertNumber(String.valueOf(miles)))
                    .append(MIL);
        }

        // Descompone el ultimo trio de unidades - ¡SGT!
        int cientos = Integer.parseInt(String.valueOf(getDigitAt(
                splitNumber[0], 2))
                .concat(String.valueOf(getDigitAt(splitNumber[0], 1)))
                .concat(String.valueOf(getDigitAt(splitNumber[0], 0))));
        if (cientos == 1) {
            converted.append(UN);
        }

        if (millon + miles + cientos == 0) {
            converted.append(CERO);
        }
        if (cientos == 100) {
            converted.append(CIEN);
        } else if (cientos > 1) {
            converted.append(convertNumber(String.valueOf(cientos)));
        }

        if (converted.toString().endsWith(MILLONES) || converted.toString().endsWith(UN_MILLON)) {
            converted.append("DE");
        }

        if (converted.toString().equals(MIL)) {
            converted.insert(0, UN + StringUtils.SPACE);
        }
        converted.append(PESOS);

        // Descompone los centavos
        int centavos = Integer.parseInt(String.valueOf(getDigitAt(
                splitNumber[1], 2))
                .concat(String.valueOf(getDigitAt(splitNumber[1], 1)))
                .concat(String.valueOf(getDigitAt(splitNumber[1], 0))));
        if (centavos == 1) {
            converted.append(CON).append(UN_CENTAVO);
        }
        if (centavos > 1) {
            converted.append(CON)
                    .append(convertNumber(String.valueOf(centavos)))
                    .append(CENTAVOS);
        }

        return converted.toString().trim().replaceAll("\\s{2,}", " ");
    }

    /**
     * Convierte los trios de numeros que componen las unidades, las decenas y las centenas del numero.
     * <p>
     * Creation date 3/05/2006 - 05:33:40 PM
     *
     * @param number Numero a convetir en digitos
     * @return Numero convertido en letras
     * @since 1.0
     */
    private static String convertNumber(String number) {
        if (number.length() > 3) {
            throw new IllegalArgumentException(
                    "La longitud máxima debe ser 3 dígitos");
        }

        StringBuilder output = new StringBuilder();
        if (getDigitAt(number, 2) != 0) {
            output.append(CENTENAS[getDigitAt(number, 2) - 1]);
        }

        int k = Integer.parseInt(String.valueOf(getDigitAt(number, 1)).concat(
                String.valueOf(getDigitAt(number, 0))));

        if (k <= 20) {
            output.append(UNIDADES[k]);
        } else if (k > 30 && getDigitAt(number, 0) != 0) {
            output.append(DECENAS[getDigitAt(number, 1) - 2])
                    .append(Y)
                    .append(
                            UNIDADES[getDigitAt(number, 0)]);
        } else {
            output.append(DECENAS[getDigitAt(number, 1) - 2])
                    .append(
                            UNIDADES[getDigitAt(number, 0)]);
        }

        // Caso especial con el 100
        if (getDigitAt(number, 2) == 1 && k == 0) {
            output.append(CIEN);
        }

        return output.toString();
    }

    /**
     * Retorna el digito numerico en la posicion indicada de derecha a izquierda
     * <p>
     * Creation date 3/05/2006 - 05:26:03 PM
     *
     * @param origin Cadena en la cual se busca el digito
     * @param position Posicion de derecha a izquierda a retornar
     * @return Digito ubicado en la posicion indicada
     * @since 1.0
     */
    private static int getDigitAt(String origin, int position) {
        if (origin.length() > position && position >= 0) {
            return origin.charAt(origin.length() - position - 1) - 48;
        }
        return 0;
    }
}
