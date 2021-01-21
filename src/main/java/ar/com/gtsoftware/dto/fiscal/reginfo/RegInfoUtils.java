package ar.com.gtsoftware.dto.fiscal.reginfo;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import org.apache.commons.lang3.StringUtils;

class RegInfoUtils {

  static final char NUMBER_PAD = '0';
  static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");
  private static final int NUMBER_SIZE = 15;
  private static final int TIPO_CAMBIO_SIZE = 10;

  static String formatNumber(final BigDecimal number) {
    final BigDecimal numberToProcess = getNumberToProcess(number);
    final String numberString =
        numberToProcess
            .setScale(2, RoundingMode.HALF_UP)
            .toString()
            .replaceAll("\\.", StringUtils.EMPTY);

    return StringUtils.leftPad(numberString, NUMBER_SIZE, NUMBER_PAD);
  }

  static String formatTipoCambio(BigDecimal number) {
    final BigDecimal numberToProcess = getNumberToProcess(number);

    final String numberString =
        numberToProcess
            .setScale(6, RoundingMode.HALF_UP)
            .toString()
            .replaceAll("\\.", StringUtils.EMPTY);
    return StringUtils.leftPad(numberString, TIPO_CAMBIO_SIZE, NUMBER_PAD);
  }

  private static BigDecimal getNumberToProcess(BigDecimal number) {
    return Objects.requireNonNullElse(number, BigDecimal.ZERO);
  }

  static <T extends Number> String numberPad(T number, int size) {
    return StringUtils.leftPad(String.valueOf(number), size, NUMBER_PAD);
  }
}
