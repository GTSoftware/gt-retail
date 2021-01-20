package ar.com.gtsoftware.api.impl;

import ar.com.gtsoftware.api.FiscalBookController;
import ar.com.gtsoftware.api.request.FiscalBookRequest;
import ar.com.gtsoftware.dto.RegimenInformativoVentas;
import ar.com.gtsoftware.search.LibroIVASearchFilter;
import ar.com.gtsoftware.service.fiscal.RegimenInformativoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class FiscalBookControllerImpl implements FiscalBookController {
    private final RegimenInformativoService regimenInformativoService;

    @Override
    public void getInformativeRegime(@Valid FiscalBookRequest filter) {
        LibroIVASearchFilter sf = LibroIVASearchFilter.builder()
                .idPeriodo(filter.getFiscalPeriodId())
                .fechaDesde(filter.getFromDate())
                .fechaHasta(filter.getToDate())
                .build();

        final RegimenInformativoVentas regimenInformativoVentas = regimenInformativoService.getRegimenInformativoVentas(sf);

    }
}
