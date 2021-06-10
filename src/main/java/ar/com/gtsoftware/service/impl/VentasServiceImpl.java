/*
 * Copyright 2018 GT Software.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package ar.com.gtsoftware.service.impl;

import ar.com.gtsoftware.dao.*;
import ar.com.gtsoftware.dto.RegistroVentaDto;
import ar.com.gtsoftware.dto.domain.ComprobantesDto;
import ar.com.gtsoftware.entity.*;
import ar.com.gtsoftware.enums.NegocioTiposComprobanteEnum;
import ar.com.gtsoftware.enums.Parametros;
import ar.com.gtsoftware.mappers.ComprobantesMapper;
import ar.com.gtsoftware.mappers.helper.CycleAvoidingMappingContext;
import ar.com.gtsoftware.search.DepositosSearchFilter;
import ar.com.gtsoftware.search.FiscalLetrasComprobantesSearchFilter;
import ar.com.gtsoftware.service.ParametrosService;
import ar.com.gtsoftware.service.PersonasCuentaCorrienteService;
import ar.com.gtsoftware.service.VentasService;
import ar.com.gtsoftware.service.afip.QRCodeGenerator;
import ar.com.gtsoftware.utils.BusinessDateUtils;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class VentasServiceImpl implements VentasService {

  private static final long ID_ESTADO_ACEPTADA = 2L;
  private static final long ID_TIPO_MOV_VENTA = 2L;

  private final ComprobantesMapper mapper;

  private final ComprobantesFacade facade;

  private final FiscalLetrasComprobantesFacade letrasComprobantesFacade;
  private final ComprobantesEstadosFacade estadosFacade;
  private final FiscalResponsabilidadesIvaFacade responsabilidadesIvaFacade;
  private final RemitoTipoMovimientoFacade tipoMovimientoFacade;
  private final RemitoFacade remitoFacade;
  private final UsuariosFacade usuariosFacade;
  private final DepositosFacade depositosFacade;
  private final SucursalesFacade sucursalesFacade;
  private final PersonasFacade personasFacade;
  private final ProductosFacade productosFacade;
  private final NegocioTiposComprobanteFacade negocioTiposComprobanteFacade;
  private final NegocioCondicionesOperacionesFacade negocioCondicionesOperacionesFacade;
  private final NegocioFormasPagoFacade formasPagoFacade;
  private final NegocioPlanesPagoFacade planesPagoFacade;
  private final NegocioPlanesPagoDetalleFacade planesPagoDetalleFacade;
  private final BusinessDateUtils dateUtils;
  private final ParametrosService parametrosService;
  private final QRCodeGenerator qrCodeGenerator;

  private final PersonasCuentaCorrienteService cuentaCorrienteBean;

  @Override
  @Transactional
  public RegistroVentaDto guardarVenta(
      ComprobantesDto comprobantesDto, boolean generarRemitoSalida) {
    Comprobantes comprobante =
        mapper.dtoToEntity(comprobantesDto, new CycleAvoidingMappingContext());
    completeInformation(comprobante);

    boolean isPresupuesto =
        NegocioTiposComprobanteEnum.PRESUPUESTO
            .getId()
            .equals(comprobante.getTipoComprobante().getId());
    if (isPresupuesto) {
      comprobante.setSaldo(BigDecimal.ZERO);
    } else {
      comprobante.setSaldo(comprobante.getTotal());
    }
    comprobante.setIdEstadoComprobante(estadosFacade.find(ID_ESTADO_ACEPTADA));
    comprobante.setFechaComprobante(dateUtils.getCurrentDateTime());

    FiscalLetrasComprobantesSearchFilter lsf =
        FiscalLetrasComprobantesSearchFilter.builder()
            .idRespIvaReceptor(comprobante.getIdPersona().getIdResponsabilidadIva().getId())
            .idRespIvaEmisor(responsabilidadesIvaFacade.find(2L).getId())
            .build();
    FiscalLetrasComprobantes letra = letrasComprobantesFacade.findFirstBySearchFilter(lsf);
    comprobante.setLetra(letra.getLetraComprobante());

    facade.createOrEdit(comprobante);
    RegistroVentaDto registro = new RegistroVentaDto();
    registro.setIdComprobante(comprobante.getId());
    if (generarRemitoSalida && !isPresupuesto) {
      long idRemito = generarRemitoComprobante(comprobante);

      registro.setIdRemito(idRemito);
    }
    if (!isPresupuesto) {
      String descMovimiento =
          String.format(
              "%s Nro: %d",
              comprobante.getTipoComprobante().getNombreComprobante(), registro.getIdComprobante());
      cuentaCorrienteBean.registrarMovimientoCuenta(
          comprobante.getIdPersona(), comprobante.getTotalConSigno(), descMovimiento);
    }

    return registro;
  }

  private void completeInformation(Comprobantes comprobante) {
    comprobante.setIdSucursal(sucursalesFacade.find(comprobante.getIdSucursal().getId()));
    comprobante.setIdPersona(personasFacade.find(comprobante.getIdPersona().getId()));
    comprobante.setIdUsuario(usuariosFacade.find(comprobante.getIdUsuario().getId()));
    comprobante.setTipoComprobante(
        negocioTiposComprobanteFacade.find(comprobante.getTipoComprobante().getId()));
    comprobante.setIdCondicionComprobante(
        negocioCondicionesOperacionesFacade.find(comprobante.getIdCondicionComprobante().getId()));

    BigDecimal saleTotal = completeProductsInformation(comprobante);
    comprobante.setTotal(saleTotal);

    completePaymentInformation(comprobante);
  }

  private void completePaymentInformation(Comprobantes comprobante) {
    for (ComprobantesPagos pago : comprobante.getPagosList()) {
      pago.setIdComprobante(comprobante);
      pago.setIdFormaPago(formasPagoFacade.find(pago.getIdFormaPago().getId()));
      pago.setMontoPagado(BigDecimal.ZERO);
      if (pago.getIdFormaPago().isRequierePlan() && pago.getIdPlan() == null) {
        throw new RuntimeException(
            "Se require un plan para la forma de pago: "
                + pago.getIdFormaPago().getNombreFormaPago());
      } else if (pago.getIdFormaPago().isRequierePlan()) {
        pago.setIdPlan(planesPagoFacade.find(pago.getIdPlan().getId()));
        pago.setIdDetallePlan(planesPagoDetalleFacade.find(pago.getIdDetallePlan().getId()));
      }
    }
  }

  private BigDecimal completeProductsInformation(Comprobantes comprobante) {
    BigDecimal total = BigDecimal.ZERO;
    int item = 1;

    for (ComprobantesLineas linea : comprobante.getComprobantesLineasList()) {
      linea.setIdComprobante(comprobante);
      Productos producto = productosFacade.find(linea.getIdProducto().getId());

      linea.setIdProducto(producto);
      linea.setCantidadEntregada(BigDecimal.ZERO);
      if (!producto.getIdTipoProveeduria().isControlStock()) {
        linea.setCantidadEntregada(linea.getCantidad());
      }
      linea.setCostoBrutoUnitario(producto.getCostoAdquisicionNeto());
      linea.setCostoNetoUnitario(producto.getCostoFinal());
      linea.setSubTotal(
          linea
              .getCantidad()
              .multiply(linea.getPrecioUnitario())
              .setScale(2, RoundingMode.HALF_UP));
      linea.setItem(item++);

      total = total.add(linea.getSubTotal());
    }
    return total.setScale(2, RoundingMode.HALF_UP);
  }

  @Override
  public ComprobantesDto obtenerComprobante(Long id) {
    Comprobantes comprobante = facade.find(id);
    final ComprobantesDto comprobantesDto =
        mapper.entityToDto(comprobante, new CycleAvoidingMappingContext());
    if (comprobante != null && comprobante.getIdRegistro() != null) {

      final Long cuitEmpresa = parametrosService.getLongParam(Parametros.EMPRESA_CUIT);

      comprobantesDto.setCodigoBarrasFactura(
          qrCodeGenerator.generarCodigo(comprobante.getIdRegistro(), cuitEmpresa));
    }

    return comprobantesDto;
  }

  // TODO esto deberìa ir en un bean responsable de manejar el stock :S
  private long generarRemitoComprobante(Comprobantes venta) {

    Remito rem = new Remito();
    List<RemitoDetalle> remitoDetalle = new ArrayList<>(venta.getComprobantesLineasList().size());
    int nroLineaRem = 0;
    for (ComprobantesLineas vl : venta.getComprobantesLineasList()) {

      Productos product = vl.getIdProducto();
      if (product.getIdTipoProveeduria().isControlStock()) {

        // Armo las lineas de remitos
        RemitoDetalle rd = new RemitoDetalle();
        rd.setCantidad(vl.getCantidad());
        rd.setIdProducto(product);
        rd.setNroLinea(nroLineaRem++);
        rd.setRemitoCabecera(rem);

        remitoDetalle.add(rd);
      }
    }
    if (CollectionUtils.isEmpty(remitoDetalle)) {
      return 0;
    }
    LocalDateTime fechaRemito = venta.getFechaComprobante();

    rem.setDetalleList(remitoDetalle);
    rem.setFechaAlta(fechaRemito);
    rem.setFechaCierre(fechaRemito);
    RemitoRecepcion recepcion = new RemitoRecepcion();
    recepcion.setFecha(fechaRemito);
    recepcion.setRemito(rem);
    recepcion.setIdUsuario(venta.getIdUsuario());

    // Verifico el origen y destino del remitoDtoCabecera a generar
    Depositos deposito =
        depositosFacade.findFirstBySearchFilter(
            DepositosSearchFilter.builder()
                .activo(true)
                .idSucursal(venta.getIdSucursal().getId())
                .build());
    if (venta.getTipoComprobante().getSigno().signum() > 0) {
      rem.setIsDestinoInterno(false);
      rem.setIsOrigenInterno(true);
      rem.setIdDestinoPrevistoExterno(venta.getIdPersona());
      // Tomo el primer depósito por defecto, esto debería venir de la UI
      rem.setIdOrigenInterno(deposito);
      recepcion.setIdPersona(venta.getIdPersona());
    } else {
      rem.setIsDestinoInterno(true);
      rem.setIsOrigenInterno(false);
      rem.setIdOrigenExterno(venta.getIdPersona());
      // Tomo el primer depósito por defecto, esto debería venir de la UI
      rem.setIdDestinoPrevistoInterno(deposito);
      recepcion.setIdDeposito(rem.getIdDestinoPrevistoInterno());
    }
    rem.setObservaciones(
        String.format("Remito generado automáticamente por comprobante nro: %d", venta.getId()));
    // TODO: Seteado en Venta por defecto pero debería ir el que corresponda.
    rem.setRemitoTipoMovimiento(tipoMovimientoFacade.find(ID_TIPO_MOV_VENTA));
    rem.setRemitoRecepcionesList(Collections.singletonList(recepcion));
    rem.setIdUsuario(venta.getIdUsuario());

    remitoFacade.create(rem);

    return rem.getId();
  }
}
