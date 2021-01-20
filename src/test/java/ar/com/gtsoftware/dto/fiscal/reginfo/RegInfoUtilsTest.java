package ar.com.gtsoftware.dto.fiscal.reginfo;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RegInfoUtilsTest {

    @ParameterizedTest
    @MethodSource("tiposCambioProvider")
    void shouldFormatTipoCambio(BigDecimal tipoCambio, String expected) {
        final String result = RegInfoUtils.formatTipoCambio(tipoCambio);

        assertEquals(expected, result);
    }

    @ParameterizedTest
    @MethodSource("numberProvider")
    void shouldFormatNumber(BigDecimal number, String expected) {
        final String result = RegInfoUtils.formatNumber(number);

        assertEquals(expected, result);
    }

    private static Stream<Arguments> tiposCambioProvider() {
        return Stream.of(
                Arguments.of(BigDecimal.ONE, "0001000000"),
                Arguments.of(BigDecimal.valueOf(0.85), "0000850000"),
                Arguments.of(BigDecimal.valueOf(9999), "9999000000")
        );
    }

    private static Stream<Arguments> numberProvider() {
        return Stream.of(
                Arguments.of(BigDecimal.ONE, "000000000000100"),
                Arguments.of(BigDecimal.valueOf(0.85), "000000000000085"),
                Arguments.of(BigDecimal.valueOf(9999), "000000000999900"),
                Arguments.of(BigDecimal.valueOf(9999.99), "000000000999999")
        );
    }

}