package ar.com.gtsoftware.dto.fiscal.reginfo;

import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.format.DateTimeFormatter;

class RegInfoUtils {

    static final char NUMBER_PAD = '0';
    static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");

    static String formatNumber(BigDecimal number) {
        final String numberString = number.setScale(2, RoundingMode.HALF_UP).toString().replaceAll("\\.", StringUtils.EMPTY);

        return StringUtils.leftPad(numberString, 15, NUMBER_PAD);
    }

    static String formatTipoCambio(BigDecimal number) {
        final String numberString = number.setScale(6, RoundingMode.HALF_UP).toString().replaceAll("\\.", StringUtils.EMPTY);
        return StringUtils.leftPad(numberString, 10, NUMBER_PAD);
    }

    static <T extends Number> String numberPad(T number, int size) {
        return StringUtils.leftPad(String.valueOf(number), size, NUMBER_PAD);
    }
}
