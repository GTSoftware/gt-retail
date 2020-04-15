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
import ar.com.gtsoftware.dto.domain.CajasDto;
import ar.com.gtsoftware.dto.domain.RecibosDto;
import ar.com.gtsoftware.mappers.*;
import ar.com.gtsoftware.mappers.helper.CycleAvoidingMappingContext;
import ar.com.gtsoftware.service.CobranzaService;
import ar.com.gtsoftware.service.PersonasCuentaCorrienteService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
@RequiredArgsConstructor
public class CobranzaServiceImpl implements CobranzaService {

    private final RecibosFacade recibosFacade;
    private final ComprobantesFacade comprobantesFacade;
    private final CajasMovimientosFacade cajasMovimientosFacade;
    private final CajasFacade cajasFacade;

    private final ComprobantesPagosFacade compPagosFacade;


    private final PersonasCuentaCorrienteService cuentaCorrienteBean;
    private final ComprobantesPagosFacade pagosFacade;
    private final UsuariosFacade usuariosFacade;

    private final ComprobantesPagosMapper comprobantesPagosMapper;
    private final CuponesMapper cuponesMapper;
    private final ChequesTercerosMapper chequesTercerosMapper;
    private final RecibosMapper recibosMapper;
    private final PersonasMapper personasMapper;


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
        Comprobantes comprobante = comprobantesFacade.find(pagos.get(0).getPago().getIdComprobante().getId());
        BigDecimal montoTotal = BigDecimal.ZERO;
        BigDecimal montoTotalConRedondeo = BigDecimal.ZERO;

        recibo.setIdPersona(comprobante.getIdPersona());
        recibo.setIdUsuario(usuariosFacade.find(cajasDto.getIdUsuario().getId()));
        Set<Comprobantes> comprobantesToEdit = new HashSet<>();
        List<RecibosDetalle> recibosDetalleList = new ArrayList<>(pagos.size());

        for (PagoValorDTO pv : pagos) {

            ComprobantesPagos compPago;
            if (pv.getPago().getId() == null) {
                compPago = comprobantesPagosMapper.dtoToEntity(pv.getPago(),
                        new CycleAvoidingMappingContext());
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

                BigDecimal redondeo = pv.getMontoRealPagadoConSigno().subtract(compPago.getMontoPagoConSigno());
                reciboDet.setRedondeo(redondeo);

                montoTotal = montoTotal.add(pv.getPago().getMontoPagoConSigno());
                montoTotalConRedondeo = montoTotalConRedondeo.add(redondeo);

                compPago.setMontoPagado(compPago.getMontoPago());

            }


            if (pv.getCupon() != null) {
                Cupones cupon = cuponesMapper.dtoToEntity(pv.getCupon(),
                        new CycleAvoidingMappingContext());
                cupon.setFechaOrigen(fecha);
                reciboDet.setIdValor(cupon);
            }

            if (pv.getCheque() != null) {
                ChequesTerceros cheque = chequesTercerosMapper.dtoToEntity(pv.getCheque(),
                        new CycleAvoidingMappingContext());
                reciboDet.setIdValor(cheque);
            }

            compPago.setFechaPago(fecha);
            compPago.setFechaUltimoPago(fecha);

            if (compPago.isNew()) {
                pagosFacade.create(compPago);
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

        String descMovimiento = generarMovimientoCaja(fecha, recibo.getId(), caja, comprobante, montoTotalConRedondeo);

        generarMovimientoCuenta(recibo.getMontoTotal(), comprobante, descMovimiento);

        return recibosMapper.entityToDto(recibo, new CycleAvoidingMappingContext());
    }

    private void generarMovimientoCuenta(BigDecimal monto, Comprobantes comprobante, String descMovimiento) {
        cuentaCorrienteBean.registrarMovimientoCuenta(
                personasMapper.entityToDto(comprobante.getIdPersona(),
                        new CycleAvoidingMappingContext()),
                monto.negate(),
                descMovimiento);
    }

    private String generarMovimientoCaja(Date fecha, Long idRecibo, Cajas caja, Comprobantes comprobante,
                                         BigDecimal montoTotalConRedondeo) {
        CajasMovimientos movimiento = new CajasMovimientos();
        movimiento.setFechaMovimiento(fecha);
        String descMovimiento = String.format("Cobro de comprobantes del cliente %s - Recibo: %d",
                comprobante.getIdPersona().getBusinessString(), idRecibo);
        movimiento.setDescripcion(descMovimiento);
        movimiento.setIdCaja(caja);
        movimiento.setMontoMovimiento(montoTotalConRedondeo);
        cajasMovimientosFacade.create(movimiento);
        return descMovimiento;
    }

}
