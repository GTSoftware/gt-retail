package ar.com.gtsoftware.service.impl;

import ar.com.gtsoftware.api.response.PaymentMethod;
import ar.com.gtsoftware.api.response.PaymentPlan;
import ar.com.gtsoftware.api.response.PlanDetail;
import ar.com.gtsoftware.dto.domain.NegocioFormasPagoDto;
import ar.com.gtsoftware.dto.domain.NegocioPlanesPagoDetalleDto;
import ar.com.gtsoftware.dto.domain.NegocioPlanesPagoDto;
import ar.com.gtsoftware.search.FormasPagoSearchFilter;
import ar.com.gtsoftware.search.PlanesPagoSearchFilter;
import ar.com.gtsoftware.service.NegocioFormasPagoService;
import ar.com.gtsoftware.service.NegocioPlanesPagoService;
import ar.com.gtsoftware.service.NoExtraCostPaymentMethodsService;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NoExtraCostPaymentMethodsServiceImpl implements NoExtraCostPaymentMethodsService {

  private final NegocioFormasPagoService formasPagoService;
  private final NegocioPlanesPagoService planesPagoService;

  @Override
  public List<PaymentMethod> getNoExtraCostPaymentMethods() {
    final List<NegocioFormasPagoDto> formasPago =
        formasPagoService.findAllBySearchFilter(
            FormasPagoSearchFilter.builder().disponibleVenta(true).build());
    List<PaymentMethod> paymentMethods = new ArrayList<>(formasPago.size());
    for (NegocioFormasPagoDto formaPagoDto : formasPago) {
      paymentMethods.add(
          PaymentMethod.builder()
              .paymentMethodId(formaPagoDto.getId())
              .paymentMethodName(formaPagoDto.getNombreFormaPago())
              .paymentMethodShortName(formaPagoDto.getNombreCorto())
              .paymentPlans(getPaymentPlans(formaPagoDto))
              .build());
    }

    return paymentMethods;
  }

  private List<PaymentPlan> getPaymentPlans(NegocioFormasPagoDto formaPagoDto) {
    final List<NegocioPlanesPagoDto> planesPago =
        planesPagoService.findAllBySearchFilter(
            PlanesPagoSearchFilter.builder()
                .activo(true)
                .idFormaPago(formaPagoDto.getId())
                .build());
    List<PaymentPlan> paymentPlans = new ArrayList<>(planesPago.size());
    for (NegocioPlanesPagoDto planPagoDto : planesPago) {
      final List<PlanDetail> planDetails = getPlanDetails(planPagoDto);
      if (CollectionUtils.isNotEmpty(planDetails)) {
        paymentPlans.add(
            PaymentPlan.builder()
                .paymentPlanId(planPagoDto.getId())
                .paymentPlanName(planPagoDto.getNombre())
                .paymentPlanDetails(planDetails)
                .build());
      }
    }

    return paymentPlans;
  }

  private List<PlanDetail> getPlanDetails(NegocioPlanesPagoDto planPagoDto) {
    final List<NegocioPlanesPagoDetalleDto> detallePlan =
        planPagoDto.getNegocioPlanesPagoDetalles().stream()
            .filter(
                detalle ->
                    detalle.isActivo()
                        && detalle.getCoeficienteInteres().subtract(BigDecimal.ONE).signum() == 0)
            .collect(Collectors.toUnmodifiableList());

    List<PlanDetail> planDetails = new ArrayList<>(detallePlan.size());
    for (NegocioPlanesPagoDetalleDto detalle : detallePlan) {
      planDetails.add(
          PlanDetail.builder()
              .detailId(detalle.getId())
              .payments(detalle.getCuotas())
              .rate(BigDecimal.ONE)
              .build());
    }

    return planDetails;
  }
}
