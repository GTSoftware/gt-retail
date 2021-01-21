/*
 * Copyright 2015 GT Software.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ar.com.gtsoftware.service.impl;

import ar.com.gtsoftware.dao.*;
import ar.com.gtsoftware.domain.*;
import ar.com.gtsoftware.dto.PagoValorDTO;
import ar.com.gtsoftware.dto.PreparedPaymentDto;
import ar.com.gtsoftware.dto.SaleToPayDto;
import ar.com.gtsoftware.dto.domain.CajasDto;
import ar.com.gtsoftware.dto.domain.RecibosDto;
import ar.com.gtsoftware.mappers.*;
import ar.com.gtsoftware.mappers.helper.CycleAvoidingMappingContext;
import ar.com.gtsoftware.service.PaymentsService;
import ar.com.gtsoftware.service.PersonasCuentaCorrienteService;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentsServiceImpl implements PaymentsService {

  private static final BigDecimal ROUND_AMOUNT = new BigDecimal(5);
  private final RecibosFacade recibosFacade;
  private final ComprobantesFacade comprobantesFacade;
  private final CajasMovimientosFacade cajasMovimientosFacade;
  private final CajasFacade cajasFacade;
  private final ComprobantesPagosFacade compPagosFacade;
  private final PersonasCuentaCorrienteService cuentaCorrienteService;
  private final UsuariosFacade usuariosFacade;
  private final ComprobantesPagosMapper comprobantesPagosMapper;
  private final CuponesMapper cuponesMapper;
  private final ChequesTercerosMapper chequesTercerosMapper;
  private final RecibosMapper recibosMapper;
  private final PersonasMapper personasMapper;
  private final ComprobantesMapper comprobantesMapper;

  @Override
  public RecibosDto cobrarComprobantes(CajasDto cajasDto, List<PagoValorDTO> pagos) {
    if (CollectionUtils.isEmpty(pagos)) {
      throw new IllegalArgumentException("La lista de pagos no puede estar vacía!");
    }
    Date fecha = new Date();
    Recibos recibo = new Recibos();
    recibo.setFechaRecibo(fecha);
    Cajas caja = cajasFacade.find(cajasDto.getId());
    recibo.setIdCaja(caja);
    Comprobantes comprobante =
        comprobantesFacade.find(pagos.get(0).getPago().getIdComprobante().getId());
    BigDecimal montoTotal = BigDecimal.ZERO;
    BigDecimal montoTotalConRedondeo = BigDecimal.ZERO;

    recibo.setIdPersona(comprobante.getIdPersona());
    recibo.setIdUsuario(usuariosFacade.find(cajasDto.getIdUsuario().getId()));
    Set<Comprobantes> comprobantesToEdit = new HashSet<>();
    List<RecibosDetalle> recibosDetalleList = new ArrayList<>(pagos.size());

    for (PagoValorDTO pv : pagos) {

      ComprobantesPagos compPago;
      if (pv.getPago().getId() == null) {
        compPago =
            comprobantesPagosMapper.dtoToEntity(pv.getPago(), new CycleAvoidingMappingContext());
      } else {
        compPago = compPagosFacade.find(pv.getPago().getId());
      }

      Comprobantes comp = comprobantesFacade.find(compPago.getIdComprobante().getId());

      RecibosDetalle reciboDet = new RecibosDetalle();
      reciboDet.setIdRecibo(recibo);
      reciboDet.setIdFormaPago(compPago.getIdFormaPago());
      reciboDet.setRedondeo(BigDecimal.ZERO);

      reciboDet.setMontoPagado(compPago.getMontoPago());
      reciboDet.setMontoPagadoConSigno(compPago.getMontoPagoConSigno());

      if (comp.getIdCondicionComprobante().getId() == 2) {
        reciboDet.setMontoPagado(pv.getMontoRealPagado());
        reciboDet.setMontoPagadoConSigno(pv.getMontoRealPagadoConSigno());

        montoTotal = montoTotal.add(pv.getMontoRealPagadoConSigno());

        compPago.setMontoPagado(pv.getMontoRealPagado());
        compPago.setMontoPago(compPago.getMontoPagado());
      }

      if (comp.getIdCondicionComprobante().getId() == 1) {
        reciboDet.setMontoPagado(pv.getPago().getMontoPago());
        reciboDet.setMontoPagadoConSigno(pv.getPago().getMontoPagoConSigno());

        BigDecimal redondeo =
            pv.getMontoRealPagadoConSigno().subtract(compPago.getMontoPagoConSigno());
        reciboDet.setRedondeo(redondeo);

        montoTotal = montoTotal.add(pv.getPago().getMontoPagoConSigno());
        montoTotalConRedondeo = montoTotalConRedondeo.add(redondeo);

        compPago.setMontoPagado(compPago.getMontoPago());
      }

      if (pv.getCupon() != null) {
        Cupones cupon = cuponesMapper.dtoToEntity(pv.getCupon(), new CycleAvoidingMappingContext());
        cupon.setFechaOrigen(fecha);
        reciboDet.setIdValor(cupon);
      }

      if (pv.getCheque() != null) {
        ChequesTerceros cheque =
            chequesTercerosMapper.dtoToEntity(pv.getCheque(), new CycleAvoidingMappingContext());
        reciboDet.setIdValor(cheque);
      }

      compPago.setFechaPago(fecha);
      compPago.setFechaUltimoPago(fecha);

      if (compPago.isNew()) {
        compPagosFacade.create(compPago);
      }
      reciboDet.setIdPagoComprobante(compPago);

      recibosDetalleList.add(reciboDet);

      comprobantesToEdit.add(comp);

      BigDecimal saldo = comp.getSaldo();
      saldo = saldo.subtract(compPago.getMontoPagado());
      comp.setSaldo(saldo);
    }

    recibo.setMontoTotal(montoTotal);
    montoTotalConRedondeo = montoTotalConRedondeo.add(montoTotal);

    recibo.setRecibosDetalles(recibosDetalleList);

    recibosFacade.create(recibo);

    for (Comprobantes c : comprobantesToEdit) {
      comprobantesFacade.edit(c);
    }

    String descMovimiento =
        generarMovimientoCaja(fecha, recibo.getId(), caja, comprobante, montoTotalConRedondeo);

    generarMovimientoCuenta(recibo.getMontoTotal(), comprobante, descMovimiento);

    return recibosMapper.entityToDto(recibo, new CycleAvoidingMappingContext());
  }

  @Override
  public PreparedPaymentDto prepareToPay(List<Long> saleIds) {
    List<Comprobantes> sales = new ArrayList<>(saleIds.size());

    saleIds.forEach(saleId -> sales.add(comprobantesFacade.find(saleId)));
    validateSales(sales);
    final PreparedPaymentDto preparedPaymentDto =
        PreparedPaymentDto.builder()
            .customer(
                personasMapper.entityToDto(
                    sales.get(0).getIdPersona(), new CycleAvoidingMappingContext()))
            .build();

    preparedPaymentDto.setSalesToPay(buildSalesToPay(sales));

    return preparedPaymentDto;
  }

  private void validateSales(List<Comprobantes> sales) {
    final Long customerId = sales.get(0).getIdPersona().getId();
    final Optional<Comprobantes> saleWithDifferentCustomer =
        sales.stream().filter(s -> !s.getIdPersona().getId().equals(customerId)).findAny();
    if (saleWithDifferentCustomer.isPresent()) {
      throw new RuntimeException("The selected sales should be for the same customer");
    }
    final Optional<Comprobantes> saleWithNoRemainingAmount =
        sales.stream().filter(s -> s.getSaldo().signum() == 0).findAny();
    if (saleWithNoRemainingAmount.isPresent()) {
      throw new RuntimeException(
          String.format(
              "The sale with Id: %d should have remaining amount",
              saleWithNoRemainingAmount.get().getId()));
    }
  }

  private List<SaleToPayDto> buildSalesToPay(List<Comprobantes> sales) {
    List<SaleToPayDto> salesToPay = new ArrayList<>();
    for (Comprobantes sale : sales) {
      BigDecimal remainingAmount = sale.getSaldo();
      for (ComprobantesPagos pago : sale.getPagosList()) {
        if (!pago.getMontoPago().equals(pago.getMontoPagado())) {
          final BigDecimal totalPayment = pago.getMontoPago().subtract(pago.getMontoPagado());
          remainingAmount = remainingAmount.subtract(totalPayment);
          final SaleToPayDto saleToPay = buildSaleToPay(sale, pago, totalPayment);

          if (!pago.getIdFormaPago().isRequiereValores()) {
            saleToPay.setEditableAmount(true);
            BigDecimal truncatedPaymentAmount = totalPayment.setScale(0, RoundingMode.HALF_UP);
            saleToPay.setMinAllowedPayment(
                truncatedPaymentAmount.subtract(ROUND_AMOUNT).max(BigDecimal.ZERO));
            saleToPay.setMaxAllowedPayment(truncatedPaymentAmount.add(ROUND_AMOUNT));
          }
          salesToPay.add(saleToPay);
        }
      }
      if (remainingAmount.signum() > 0) {
        final SaleToPayDto saleToPay = buildUndefinedPayment(sale, remainingAmount);

        salesToPay.add(saleToPay);
      }
    }

    return salesToPay;
  }

  private SaleToPayDto buildUndefinedPayment(Comprobantes sale, BigDecimal remainingAmount) {
    return SaleToPayDto.builder()
        .sale(comprobantesMapper.entityToDto(sale, new CycleAvoidingMappingContext()))
        .payment(null)
        .totalPayment(remainingAmount)
        .maxAllowedPayment(remainingAmount)
        .minAllowedPayment(BigDecimal.ZERO)
        .editableAmount(true)
        .build();
  }

  private SaleToPayDto buildSaleToPay(
      Comprobantes sale, ComprobantesPagos pago, BigDecimal totalPayment) {
    return SaleToPayDto.builder()
        .sale(comprobantesMapper.entityToDto(sale, new CycleAvoidingMappingContext()))
        .totalPayment(totalPayment)
        .payment(comprobantesPagosMapper.entityToDto(pago, new CycleAvoidingMappingContext()))
        .editableAmount(false)
        .minAllowedPayment(totalPayment)
        .maxAllowedPayment(totalPayment)
        .build();
  }

  private void generarMovimientoCuenta(
      BigDecimal monto, Comprobantes comprobante, String descMovimiento) {
    cuentaCorrienteService.registrarMovimientoCuenta(
        comprobante.getIdPersona(), monto.negate(), descMovimiento);
  }

  private String generarMovimientoCaja(
      Date fecha,
      Long idRecibo,
      Cajas caja,
      Comprobantes comprobante,
      BigDecimal montoTotalConRedondeo) {
    CajasMovimientos movimiento = new CajasMovimientos();
    movimiento.setFechaMovimiento(fecha);
    String descMovimiento =
        String.format(
            "Cobro de comprobantes del cliente %s - Recibo: %d",
            comprobante.getIdPersona().getBusinessString(), idRecibo);
    movimiento.setDescripcion(descMovimiento);
    movimiento.setIdCaja(caja);
    movimiento.setMontoMovimiento(montoTotalConRedondeo);
    cajasMovimientosFacade.create(movimiento);
    return descMovimiento;
  }
}
