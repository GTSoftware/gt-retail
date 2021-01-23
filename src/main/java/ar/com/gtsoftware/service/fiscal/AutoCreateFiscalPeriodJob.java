package ar.com.gtsoftware.service.fiscal;

import ar.com.gtsoftware.dto.domain.FiscalPeriodosFiscalesDto;
import ar.com.gtsoftware.search.FiscalPeriodosFiscalesSearchFilter;
import ar.com.gtsoftware.service.FiscalPeriodosFiscalesService;
import ar.com.gtsoftware.utils.BusinessDateUtils;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class AutoCreateFiscalPeriodJob {

  private final FiscalPeriodosFiscalesService periodosFiscalesService;
  private final BusinessDateUtils dateUtils;
  private final DateTimeFormatter FISCAL_PERIOD_NAME_FORMATTER =
      DateTimeFormatter.ofPattern("yyyy-MM");
  private final Logger logger = LogManager.getLogger(AutoCreateFiscalPeriodJob.class);

  @Scheduled(cron = "${fiscal.period.autocreation.cron}") // Every month on the 1st at 2am
  @Transactional
  public void createFiscalPeriodForActualMonth() {
    logger.info("Fiscal period auto-creation triggered.");
    final FiscalPeriodosFiscalesDto existingFiscalPeriod = getExistingFiscalPeriod();
    if (Objects.isNull(existingFiscalPeriod)) {
      createFiscalPeriod();
    } else {
      logger.info("Fiscal period '{}' already exists.", existingFiscalPeriod.getNombrePeriodo());
    }
  }

  private FiscalPeriodosFiscalesDto getExistingFiscalPeriod() {
    final FiscalPeriodosFiscalesSearchFilter sf =
        FiscalPeriodosFiscalesSearchFilter.builder()
            .fechaActual(dateUtils.getCurrentDateTime())
            .build();
    return periodosFiscalesService.findFirstBySearchFilter(sf);
  }

  private void createFiscalPeriod() {
    final String periodName = FISCAL_PERIOD_NAME_FORMATTER.format(dateUtils.getCurrentDate());
    logger.info("About to create fiscal period: {}", periodName);
    FiscalPeriodosFiscalesDto fiscalPeriod =
        FiscalPeriodosFiscalesDto.builder()
            .fechaInicioPeriodo(dateUtils.getStartDateTimeOfCurrentMonth())
            .fechaFinPeriodo(dateUtils.getEndDateTimeOfCurrentMonth().minus(1, ChronoUnit.SECONDS))
            .nombrePeriodo(periodName)
            .build();
    final FiscalPeriodosFiscalesDto createdPeriod =
        periodosFiscalesService.createOrEdit(fiscalPeriod);
    logger.info("Successfully created fiscal period with ID: {}", createdPeriod.getId());
  }
}
