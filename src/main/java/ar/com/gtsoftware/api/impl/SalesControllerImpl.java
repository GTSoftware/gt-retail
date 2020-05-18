package ar.com.gtsoftware.api.impl;

import ar.com.gtsoftware.api.SalesController;
import ar.com.gtsoftware.api.request.PaginatedSearchRequest;
import ar.com.gtsoftware.api.request.SaleSearchResult;
import ar.com.gtsoftware.api.response.PaginatedResponse;
import ar.com.gtsoftware.api.transformer.fromDomain.SaleSearchResultTransformer;
import ar.com.gtsoftware.auth.Roles;
import ar.com.gtsoftware.dto.domain.ComprobantesDto;
import ar.com.gtsoftware.search.ComprobantesSearchFilter;
import ar.com.gtsoftware.service.ComprobantesService;
import ar.com.gtsoftware.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class SalesControllerImpl implements SalesController {

    private final ComprobantesService comprobantesService;
    private final SaleSearchResultTransformer saleSearchResultTransformer;
    private final SecurityUtils securityUtils;

    @Override
    public PaginatedResponse<SaleSearchResult> findBySearchFilter(@Valid PaginatedSearchRequest<ComprobantesSearchFilter> searchRequest) {
        final ComprobantesSearchFilter searchFilter = searchRequest.getSearchFilter();
        searchFilter.addSortField("fechaComprobante", false);
        if (securityUtils.userHasRole(Roles.VENDEDORES) && !securityUtils.userHasRole(Roles.ADMINISTRADORES)) {
            searchFilter.setIdUsuario(securityUtils.getUserDetails().getId());
        }

        final int count = comprobantesService.countBySearchFilter(searchFilter);
        final PaginatedResponse<SaleSearchResult> response = PaginatedResponse.<SaleSearchResult>builder().totalResults(count).build();

        if (count > 0) {
            final List<ComprobantesDto> comprobantes = comprobantesService.findBySearchFilter(searchFilter,
                    searchRequest.getFirstResult(),
                    searchRequest.getMaxResults());
            response.setData(saleSearchResultTransformer.transformSales(comprobantes));
        }

        return response;
    }
}
