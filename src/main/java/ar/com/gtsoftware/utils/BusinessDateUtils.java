package ar.com.gtsoftware.utils;

import java.time.*;
import java.time.temporal.TemporalAdjusters;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BusinessDateUtils {

  private final Clock clock;

  public LocalDateTime getCurrentDateTime() {
    return LocalDateTime.now(clock);
  }

  public ZonedDateTime getCurrentZonedDateTime() {
    return ZonedDateTime.now(clock);
  }

  public LocalDateTime getStartDateTimeOfCurrentMonth() {
    return this.getCurrentDateTime()
        .with(TemporalAdjusters.firstDayOfMonth())
        .with(LocalTime.MIDNIGHT);
  }

  public LocalDateTime getEndDateTimeOfCurrentMonth() {
    return this.getCurrentDateTime().with(TemporalAdjusters.lastDayOfMonth()).with(LocalTime.MAX);
  }

  public LocalDate getCurrentDate() {
    return LocalDate.now(clock);
  }

  public LocalDate getStartDateOfCurrentMonth() {
    return this.getCurrentDate().withDayOfMonth(1);
  }

  public LocalDate getEndDateOfCurrentMonth() {
    LocalDate today = getCurrentDate();
    return today.withDayOfMonth(today.lengthOfMonth());
  }
}
