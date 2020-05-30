package ar.com.gtsoftware.api.impl;

import ar.com.gtsoftware.api.InvoiceController;
import ar.com.gtsoftware.api.exception.PointOfSaleNotFoundException;
import ar.com.gtsoftware.api.exception.SaleNotFoundException;
import ar.com.gtsoftware.api.request.InvoiceRequest;
import ar.com.gtsoftware.api.response.InvoiceResponse;
import ar.com.gtsoftware.api.response.PointOfSale;
import ar.com.gtsoftware.dto.domain.ComprobantesDto;
import ar.com.gtsoftware.dto.domain.FiscalLibroIvaVentasDto;
import ar.com.gtsoftware.dto.domain.FiscalPuntosVentaDto;
import ar.com.gtsoftware.enums.TiposPuntosVenta;
import ar.com.gtsoftware.search.FiscalPuntosVentaSearchFilter;
import ar.com.gtsoftware.service.ComprobantesService;
import ar.com.gtsoftware.service.FacturacionVentasService;
import ar.com.gtsoftware.service.FiscalPuntosVentaService;
import ar.com.gtsoftware.service.exceptions.ServiceException;
import ar.com.gtsoftware.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequiredArgsConstructor

public class InvoiceControllerImpl implements InvoiceController {

    private final SecurityUtils securityUtils;
    private final FiscalPuntosVentaService puntosVentaService;
    private final ComprobantesService comprobantesService;
    private final FacturacionVentasService facturacionService;

    @Override
    public List<PointOfSale> getPointsOfSale() {
        FiscalPuntosVentaSearchFilter sf = FiscalPuntosVentaSearchFilter.builder()
                .activo(true)
                .idSucursal(securityUtils.getUserDetails().getSucursalId())
                .tiposPuntoVenta(Arrays.asList(TiposPuntosVenta.ELECTRONICO, TiposPuntosVenta.MANUAL))
                .build();

        final List<FiscalPuntosVentaDto> puntosVenta = puntosVentaService.findAllBySearchFilter(sf);

        return transformPointsOfSale(puntosVenta);
    }

    private List<PointOfSale> transformPointsOfSale(List<FiscalPuntosVentaDto> puntosVenta) {
        List<PointOfSale> pointsOfSale = new ArrayList<>(puntosVenta.size());

        for (FiscalPuntosVentaDto fiscalPuntosVentaDto : puntosVenta) {
            pointsOfSale.add(
                    PointOfSale.builder()
                            .posNumber(fiscalPuntosVentaDto.getNroPuntoVenta())
                            .posType(fiscalPuntosVentaDto.getTipo().name())
                            .byDefault(TiposPuntosVenta.ELECTRONICO == fiscalPuntosVentaDto.getTipo())
                            .displayName(String.format("[%s-%s] %s",
                                    fiscalPuntosVentaDto.getNroPuntoVenta(),
                                    fiscalPuntosVentaDto.getTipo().name(),
                                    fiscalPuntosVentaDto.getDescripcion()))
                            .build()
            );
        }

        return pointsOfSale;
    }

    @Override
    public InvoiceResponse invoiceSale(@Valid InvoiceRequest request) {
        final ComprobantesDto comprobantesDto = comprobantesService.find(request.getSaleId());
        final FiscalPuntosVentaDto puntosVentaDto = puntosVentaService.find(request.getPointOfSale());

        if (comprobantesDto == null) {
            throw new SaleNotFoundException();
        }
        if (puntosVentaDto == null) {
            throw new PointOfSaleNotFoundException();
        }

        try {
            facturacionService.registrarFacturaVenta(comprobantesDto.getId(), puntosVentaDto, 0, null);
        } catch (ServiceException e) {
            throw new RuntimeException(e);
        }

        final ComprobantesDto comprobanteFacturado = comprobantesService.find(request.getSaleId());
        final FiscalLibroIvaVentasDto idRegistro = comprobanteFacturado.getIdRegistro();

        return InvoiceResponse.builder()
                .invoiceNumber(String.format("%s %s-%s",
                        idRegistro.getLetraFactura(),
                        idRegistro.getPuntoVentaFactura(),
                        idRegistro.getNumeroFactura()))
                .build();
    }
}
