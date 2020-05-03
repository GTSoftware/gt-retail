package ar.com.gtsoftware.api.impl;

import ar.com.gtsoftware.api.ProductsController;
import ar.com.gtsoftware.api.request.PaginatedSearchRequest;
import ar.com.gtsoftware.api.response.PaginatedResponse;
import ar.com.gtsoftware.api.response.ProductSearchResult;
import ar.com.gtsoftware.dto.domain.ProductosDto;
import ar.com.gtsoftware.enums.Parametros;
import ar.com.gtsoftware.search.ProductosSearchFilter;
import ar.com.gtsoftware.service.ParametrosService;
import ar.com.gtsoftware.service.ProductosService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ProductsControllerImpl implements ProductsController {

    private final ProductosService productosService;
    private final ParametrosService parametrosService;

    @Override
    public PaginatedResponse<ProductSearchResult> findBySearchFilter(@Valid PaginatedSearchRequest<ProductosSearchFilter> searchRequest) {
        final ProductosSearchFilter searchFilter = searchRequest.getSearchFilter();
        if (searchFilter.getIdListaPrecio() == null) {
            searchFilter.setIdListaPrecio(parametrosService.getLongParam(Parametros.ID_LISTA_VENTA));
        }

        final int count = productosService.countBySearchFilter(searchFilter);
        final PaginatedResponse<ProductSearchResult> response = PaginatedResponse.<ProductSearchResult>builder().totalResults(count).build();

        if (count > 0) {
            final List<ProductosDto> productos = productosService.findBySearchFilter(searchFilter,
                    searchRequest.getFirstResult(),
                    searchRequest.getMaxResults());
            response.setData(transformProducts(productos));
        }

        return response;
    }

    private List<ProductSearchResult> transformProducts(List<ProductosDto> productos) {
        List<ProductSearchResult> productSearchResults = new ArrayList<>(productos.size());

        for (ProductosDto dto : productos) {
            final ProductSearchResult productSearchResult = ProductSearchResult.builder()
                    .codigoFabricante(dto.getCodigoFabricante())
                    .codigoPropio(dto.getCodigoPropio())
                    .descripcion(dto.getDescripcion())
                    .fechaUltimaModificacion(dto.getFechaUltimaModificacion())
                    .id(dto.getId())
                    .marca(dto.getIdMarca())
                    .rubro(dto.getIdRubro())
                    .subRubro(dto.getIdSubRubro())
                    .tipoProveeduria(dto.getIdTipoProveeduria())
                    .tipoUnidadCompra(dto.getIdTipoUnidadCompra())
                    .tipoUnidadVenta(dto.getIdTipoUnidadVenta())
                    .observaciones(dto.getObservaciones())
                    .precioVenta(dto.getPrecioVenta())
                    .stockActual(dto.getStockActual())
                    .stockActualEnSucursal(dto.getStockActualEnSucursal())
                    .build();

            productSearchResults.add(productSearchResult);
        }

        return productSearchResults;
    }

}
