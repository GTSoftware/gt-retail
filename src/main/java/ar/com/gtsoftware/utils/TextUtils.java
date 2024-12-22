package ar.com.gtsoftware.utils;

import org.apache.commons.lang3.StringUtils;

public class TextUtils {

  private TextUtils() {}

  public static String upperCaseTrim(String text) {
    return StringUtils.upperCase(StringUtils.trim(text));
  }
}
