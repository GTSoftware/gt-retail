package ar.com.gtsoftware.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BusinessDateUtilsTest {

  private static final LocalDateTime LOCAL_DATE_TIME = LocalDateTime.of(2020, 2, 13, 13, 13, 4);
  private static final ZonedDateTime ZONED_DATE_TIME =
      ZonedDateTime.of(2020, 2, 13, 13, 13, 4, 0, ZoneId.of("Europe/Amsterdam"));
  private static final LocalDate LOCAL_DATE = LocalDate.of(2020, 2, 13);

  private BusinessDateUtils businessDateUtils;

  @BeforeEach
  void setUp() {

    Clock fixedClock = Clock.fixed(ZONED_DATE_TIME.toInstant(), ZoneId.of("Europe/Amsterdam"));

    businessDateUtils = new BusinessDateUtils(fixedClock);
  }

  @Test
  public void shouldGetCurrentDateTime() {
    assertEquals(LOCAL_DATE_TIME, businessDateUtils.getCurrentDateTime());
  }

  @Test
  public void shouldGetStartDateTimeOfCurrentMonth() {
    assertEquals(
        LocalDateTime.of(2020, 2, 1, 0, 0, 0), businessDateUtils.getStartDateTimeOfCurrentMonth());
  }

  @Test
  public void shouldGetEndDateTimeOfCurrentMonth() {
    assertEquals(
        LocalDateTime.of(2020, 2, 29, 23, 59, 59, 999999999),
        businessDateUtils.getEndDateTimeOfCurrentMonth());
  }

  @Test
  public void shouldGetCurrentDate() {
    assertEquals(LOCAL_DATE, businessDateUtils.getCurrentDate());
  }

  @Test
  public void shouldGetStartDateOfCurrentMonth() {
    assertEquals(LocalDate.of(2020, 2, 1), businessDateUtils.getStartDateOfCurrentMonth());
  }

  @Test
  public void shouldGetEndDateOfCurrentMonth() {
    assertEquals(LocalDate.of(2020, 2, 29), businessDateUtils.getEndDateOfCurrentMonth());
  }

  @Test
  public void shouldGetCurrentZonedDateTime() {
    assertEquals(ZONED_DATE_TIME, businessDateUtils.getCurrentZonedDateTime());
  }
}
