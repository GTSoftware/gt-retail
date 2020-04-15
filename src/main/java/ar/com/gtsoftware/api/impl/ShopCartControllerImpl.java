package ar.com.gtsoftware.api.impl;

import ar.com.gtsoftware.api.PromotionCartItem;
import ar.com.gtsoftware.api.ShopCartController;
import ar.com.gtsoftware.api.exception.ProductNotFoundException;
import ar.com.gtsoftware.api.request.AddCartItemRequest;
import ar.com.gtsoftware.api.request.SaleItem;
import ar.com.gtsoftware.api.request.SalePayment;
import ar.com.gtsoftware.api.request.SaleRequest;
import ar.com.gtsoftware.api.response.CartItemResponse;
import ar.com.gtsoftware.api.response.CreatedSaleResponse;
import ar.com.gtsoftware.api.response.DiscountItem;
import ar.com.gtsoftware.auth.JwtUserDetails;
import ar.com.gtsoftware.dto.RegistroVentaDto;
import ar.com.gtsoftware.dto.domain.*;
import ar.com.gtsoftware.enums.Parametros;
import ar.com.gtsoftware.rules.helper.OfertasHelper;
import ar.com.gtsoftware.search.*;
import ar.com.gtsoftware.service.*;
import ar.com.gtsoftware.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
class ShopCartControllerImpl implements ShopCartController {

    private static final BigDecimal CIEN = new BigDecimal(100);
    private static final Long DEFAULT_TIPO_COMPROBANTE_ID = 1L;
    private final ProductosService productosService;
    private final PersonasService personasService;
    private final SecurityUtils securityUtils;
    private final ParametrosService parametrosService;
    private final NegocioTiposComprobanteService negocioTiposComprobanteService;
    private final NegocioCondicionesOperacionesService negocioCondicionesOperacionesService;
    private final NegocioFormasPagoService negocioFormasPagoService;
    private final NegocioPlanesPagoService negocioPlanesPagoService;
    private final VentasService ventasService;
    private final OfertasHelper ofertasHelper;
    private final Logger logger = LoggerFactory.getLogger(ShopCartControllerImpl.class);

    @Override
    public CartItemResponse addProduct(AddCartItemRequest addCartItemRequest) {

        Long idListaPrecio = parametrosService.getLongParam(Parametros.ID_LISTA_VENTA);

        ProductosSearchFilter psf = ProductosSearchFilter.builder()
                .activo(true)
                .codigoPropio(addCartItemRequest.getProductCode())
                .idProducto(addCartItemRequest.getProductId())
                .idListaPrecio(idListaPrecio)
                .idSucursal(securityUtils.getUserDetails().getSucursalId())
                .build();

        ProductosDto productosDto = productosService.findFirstBySearchFilter(psf);

        if (productosDto != null) {
            BigDecimal fixedCantidad = fixCantidad(productosDto, addCartItemRequest);
            PromotionCartItem promotionCartItem = applyPromotions(productosDto, fixedCantidad);

            return mapToItemResponse(promotionCartItem);
        }

        throw new ProductNotFoundException();
    }

    @Override
    public PersonasDto getDefaultCustomer() {
        Long idClienteDefecto = parametrosService.getLongParam(Parametros.ID_CLIENTE_DEFECTO);
        return personasService.find(idClienteDefecto);
    }

    @Override
    public List<PersonasDto> searchCustomers(String query) {
        PersonasSearchFilter filter = PersonasSearchFilter.builder()
                .activo(true)
                .cliente(true)
                .txt(query)
                .build();
        filter.addSortField("razonSocial", true);

        return personasService.findBySearchFilter(filter, 0, 15);
    }

    @Override
    public List<NegocioTiposComprobanteDto> getSaleTypes() {
        NegocioTiposComprobanteSearchFilter tipoCompSf = NegocioTiposComprobanteSearchFilter.builder()
                .activo(true).build();
        List<NegocioTiposComprobanteDto> tiposComprobanteDtos = negocioTiposComprobanteService.findAllBySearchFilter(tipoCompSf);

        for (NegocioTiposComprobanteDto tipo : tiposComprobanteDtos) {
            if (DEFAULT_TIPO_COMPROBANTE_ID.equals(tipo.getId())) {
                tipo.setPorDefecto(true);
                break;
            }
        }

        return tiposComprobanteDtos;
    }

    @Override
    public List<NegocioCondicionesOperacionesDto> getSaleConditions() {
        final Long defaultSaleConditionId = parametrosService.getLongParam(Parametros.ID_CONDICION_VENTA_DEFECTO);
        List<NegocioCondicionesOperacionesDto> condiciones = negocioCondicionesOperacionesService.findAll();

        for (NegocioCondicionesOperacionesDto condicion : condiciones) {
            if (defaultSaleConditionId.equals(condicion.getId())) {
                condicion.setPorDefecto(true);
                break;
            }
        }

        return condiciones;
    }

    @Override
    public List<NegocioFormasPagoDto> getPaymentMethods() {
        final FormasPagoSearchFilter formasPagoSearchFilter = FormasPagoSearchFilter.builder()
                .disponibleVenta(true).build();
        final Long defaultPaymentMethodId = parametrosService.getLongParam(Parametros.ID_FORMA_PAGO_DEFECTO);
        List<NegocioFormasPagoDto> paymentMethods = negocioFormasPagoService.findAllBySearchFilter(formasPagoSearchFilter);

        for (NegocioFormasPagoDto method : paymentMethods) {
            if (defaultPaymentMethodId.equals(method.getId())) {
                method.setPorDefecto(true);
                break;
            }
        }

        return paymentMethods;
    }

    @Override
    public List<NegocioPlanesPagoDto> getPaymentPlans(Long paymentPlanId) {
        final PlanesPagoSearchFilter sf = PlanesPagoSearchFilter.builder()
                .activo(true)
                .idFormaPago(paymentPlanId)
                .build();

        return negocioPlanesPagoService.findAllBySearchFilter(sf);
    }

    @Override
    public CreatedSaleResponse saveSale(SaleRequest saleRequest) {
        JwtUserDetails userDetails = securityUtils.getUserDetails();

        ComprobantesDto comprobante = ComprobantesDto.builder()
                .idCondicionComprobante(saleRequest.getSaleCondition())
                .idPersona(saleRequest.getCustomerInfo())
                .nroRemito(saleRequest.getRemito())
                .observaciones(saleRequest.getObservaciones())
                .remitente(saleRequest.getRemitente())
                .tipoComprobante(saleRequest.getSaleType())
                .comprobantesLineasList(transformSaleItems(saleRequest.getProducts()))
                .pagosList(transformPayments(saleRequest.getPayments()))
                .idSucursal(SucursalesDto.builder().id(userDetails.getSucursalId()).build())
                .idUsuario(UsuariosDto.builder().id(userDetails.getId()).build())
                .build();

        RegistroVentaDto registroVentaDto = ventasService.guardarVenta(comprobante, true);

        return CreatedSaleResponse.builder()
                .deliveryNoteId(registroVentaDto.getIdRemito())
                .saleId(registroVentaDto.getIdComprobante())
                .build();
    }

    private List<ComprobantesPagosDto> transformPayments(List<SalePayment> payments) {
        List<ComprobantesPagosDto> pagos = new ArrayList<>(payments.size());
        for (SalePayment payment : payments) {
            ComprobantesPagosDto pago = ComprobantesPagosDto.builder()
                    .idFormaPago(payment.getIdFormaPago())
                    .montoPago(payment.getMontoPago())
                    .idPlan(payment.getIdPlan())
                    .idDetallePlan(payment.getIdDetallePlan())
                    .build();

            pagos.add(pago);
        }

        return pagos;
    }

    private List<ComprobantesLineasDto> transformSaleItems(List<SaleItem> products) {
        List<ComprobantesLineasDto> lineas = new ArrayList<>(products.size());
        for (SaleItem item : products) {
            ProductosDto producto = null;
            if (item.getId() != null) {
                producto = ProductosDto.builder()
                        .id(item.getId())
                        .codigoPropio(item.getCodigoPropio())
                        .build();
            } else {
                Long idProdRedondeoParam = parametrosService.getLongParam(Parametros.ID_PRODUCTO_REDONDEO);
                producto = productosService.find(idProdRedondeoParam);
            }
            ComprobantesLineasDto linea = ComprobantesLineasDto.builder()
                    .cantidad(item.getCantidad())
                    .descripcion(item.getDescripcion())
                    .precioUnitario(item.getPrecioVentaUnitario())
                    .idProducto(producto)
                    .build();

            lineas.add(linea);
        }

        return lineas;
    }

    private BigDecimal fixCantidad(ProductosDto productosDto, AddCartItemRequest addCartItemRequest) {
        BigDecimal fixedCantidad = BigDecimal.ONE;
        BigDecimal requestCantidad = addCartItemRequest.getCantidad().setScale(2, RoundingMode.HALF_UP);

        if (productosDto.getIdTipoUnidadVenta().isCantidadEntera()) {
            BigDecimal integerPart = requestCantidad.setScale(0, RoundingMode.DOWN);
            if (integerPart.signum() != 0) {
                fixedCantidad = integerPart;
            }
        }

        return fixedCantidad;
    }

    private PromotionCartItem applyPromotions(ProductosDto productosDto, BigDecimal cantidad) {

        ComprobantesLineasDto linea = crearLinea(productosDto, cantidad);

        PromotionCartItem promotionCartItem = new PromotionCartItem();
        promotionCartItem.setLinea(linea);

        ofertasHelper.ejecutarReglasOferta(promotionCartItem);

        return promotionCartItem;
    }

    private ComprobantesLineasDto crearLinea(ProductosDto prod, BigDecimal cantidad) {
        ComprobantesLineasDto linea = new ComprobantesLineasDto();

        linea.setCantidad(cantidad);
        linea.setDescripcion(prod.getDescripcion());
        linea.setIdProducto(prod);
        linea.setPrecioUnitario(prod.getPrecioVenta());
        linea.setSubTotal(linea.getCantidad().multiply(linea.getIdProducto().getPrecioVenta()));

        return linea;
    }

    private CartItemResponse mapToItemResponse(PromotionCartItem promotionCartItem) {
        ComprobantesLineasDto linea = promotionCartItem.getLinea();
        ProductosDto producto = linea.getIdProducto();

        return CartItemResponse.builder()
                .id(producto.getId())
                .codigoPropio(producto.getCodigoPropio())
                .descripcion(producto.getDescripcion())
                .precioVentaUnitario(producto.getPrecioVenta())
                .stockActual(producto.getStockActual())
                .stockActualEnSucursal(producto.getStockActualEnSucursal())
                .cantidadEntera(producto.getIdTipoUnidadVenta().isCantidadEntera())
                .unidadVenta(producto.getIdTipoUnidadVenta().getNombreUnidad())
                .cantidad(linea.getCantidad())
                .subTotal(linea.getSubTotal())
                .discountItem(buildDiscountItem(promotionCartItem))
                .build();
    }

    private DiscountItem buildDiscountItem(PromotionCartItem promotionCartItem) {
        DiscountItem discountItem = null;
        if (promotionCartItem.isDiscountApplicable()) {
            ComprobantesLineasDto linea = promotionCartItem.getLinea();
            Long idProdRedondeoParam = parametrosService.getLongParam(Parametros.ID_PRODUCTO_REDONDEO);
            BigDecimal discountAmount = calculateDiscountAmount(promotionCartItem);

            discountItem = DiscountItem.builder()
                    .cantidad(BigDecimal.ONE)
                    .descripcion(StringUtils.left("[" + linea.getIdProducto().getId() + "] " + promotionCartItem.getNombreOferta(), 90))
                    .codigoPropio("DESC.")
                    .id(idProdRedondeoParam)
                    .unidadVenta("UN.")
                    .precioVentaUnitario(discountAmount)
                    .subTotal(discountAmount)
                    .build();
        }

        return discountItem;
    }


    private BigDecimal calculateDiscountAmount(PromotionCartItem promotionCartItem) {
        BigDecimal discountAmount = BigDecimal.ZERO;

        switch (promotionCartItem.getTipoAccion()) {
            case DESCUENTO_MONTO_FIJO:

                discountAmount = promotionCartItem.getDescuento();
                break;
            case DESCUENTO_PORCENTAJE:

                discountAmount = promotionCartItem.getLinea().getSubTotal()
                        .multiply(promotionCartItem.getDescuento().divide(CIEN))
                        .setScale(2, RoundingMode.HALF_UP);
                break;
        }

        return discountAmount.negate();
    }
}
