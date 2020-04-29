package ar.com.gtsoftware.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;

@Component
@RequiredArgsConstructor
public class BusinessDateUtils {

    private final Clock clock;

    public LocalDateTime getCurrentDateTime() {
        return LocalDateTime.now(clock);
    }

    public LocalDateTime getStartDateTimeOfCurrentMonth() {
        return this.getCurrentDateTime().with(TemporalAdjusters.firstDayOfMonth()).with(LocalTime.MIDNIGHT);
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
