package ar.com.gtsoftware.service.fiscal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

import ar.com.gtsoftware.dto.domain.FiscalPeriodosFiscalesDto;
import ar.com.gtsoftware.search.FiscalPeriodosFiscalesSearchFilter;
import ar.com.gtsoftware.service.FiscalPeriodosFiscalesService;
import ar.com.gtsoftware.utils.BusinessDateUtils;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;

class AutoCreateFiscalPeriodJobTest {

  private static final LocalDateTime START_DATE = LocalDateTime.of(2020, 1, 1, 0, 0);
  private static final LocalDateTime END_DATE = LocalDateTime.of(2020, 1, 31, 23, 59);
  private static final LocalDate ACTUAL_DATE = LocalDate.of(2020, 1, 1);
  private static final LocalDateTime ACTUAL_DATETIME = LocalDateTime.of(2020, 1, 1, 2, 2);

  private AutoCreateFiscalPeriodJob autoCreateFiscalPeriodJob;

  @Mock private BusinessDateUtils dateUtilsMock;
  @Mock private FiscalPeriodosFiscalesService periodosFiscalesServiceMock;
  @Captor private ArgumentCaptor<FiscalPeriodosFiscalesDto> dtoArgumentCaptor;
  @Captor private ArgumentCaptor<FiscalPeriodosFiscalesSearchFilter> filterArgumentCaptor;

  @BeforeEach
  void setUp() {
    initMocks(this);
    when(dateUtilsMock.getStartDateTimeOfCurrentMonth()).thenReturn(START_DATE);
    when(dateUtilsMock.getEndDateTimeOfCurrentMonth()).thenReturn(END_DATE);
    when(dateUtilsMock.getCurrentDate()).thenReturn(ACTUAL_DATE);
    when(dateUtilsMock.getCurrentDateTime()).thenReturn(ACTUAL_DATETIME);

    autoCreateFiscalPeriodJob =
        new AutoCreateFiscalPeriodJob(periodosFiscalesServiceMock, dateUtilsMock);
  }

  @Test
  void shouldCreateFiscalPeriodForActualMonthWhenNotAlreadyExists() {
    when(periodosFiscalesServiceMock.findFirstBySearchFilter(
            any(FiscalPeriodosFiscalesSearchFilter.class)))
        .thenReturn(null);
    when(periodosFiscalesServiceMock.createOrEdit(any(FiscalPeriodosFiscalesDto.class)))
        .thenReturn(FiscalPeriodosFiscalesDto.builder().id(1L).build());

    autoCreateFiscalPeriodJob.createFiscalPeriodForActualMonth();

    verify(periodosFiscalesServiceMock).findFirstBySearchFilter(filterArgumentCaptor.capture());
    verify(periodosFiscalesServiceMock).createOrEdit(dtoArgumentCaptor.capture());

    final FiscalPeriodosFiscalesSearchFilter sf = filterArgumentCaptor.getValue();
    assertEquals(ACTUAL_DATETIME, sf.getFechaActual());

    final FiscalPeriodosFiscalesDto fiscalPeriod = dtoArgumentCaptor.getValue();

    assertEquals("2020-01", fiscalPeriod.getNombrePeriodo());
    assertEquals(START_DATE, fiscalPeriod.getFechaInicioPeriodo());
    assertEquals(END_DATE.minus(1, ChronoUnit.SECONDS), fiscalPeriod.getFechaFinPeriodo());
  }

  @Test
  void shouldNotCreateFiscalPeriodForActualMonthWhenAlreadyExists() {
    when(periodosFiscalesServiceMock.findFirstBySearchFilter(
            any(FiscalPeriodosFiscalesSearchFilter.class)))
        .thenReturn(FiscalPeriodosFiscalesDto.builder().id(1L).nombrePeriodo("TEST").build());

    autoCreateFiscalPeriodJob.createFiscalPeriodForActualMonth();

    verify(periodosFiscalesServiceMock).findFirstBySearchFilter(filterArgumentCaptor.capture());
    verifyNoMoreInteractions(periodosFiscalesServiceMock);

    final FiscalPeriodosFiscalesSearchFilter sf = filterArgumentCaptor.getValue();
    assertEquals(ACTUAL_DATETIME, sf.getFechaActual());
  }
}
