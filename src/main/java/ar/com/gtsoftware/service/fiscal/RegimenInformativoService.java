package ar.com.gtsoftware.service.fiscal;

import ar.com.gtsoftware.dto.fiscal.reginfo.RegimenInformativoCompras;
import ar.com.gtsoftware.dto.fiscal.reginfo.RegimenInformativoVentas;
import ar.com.gtsoftware.search.LibroIVASearchFilter;

public interface RegimenInformativoService {
    RegimenInformativoVentas getRegimenInformativoVentas(LibroIVASearchFilter filter);

    RegimenInformativoCompras getRegimenInformativoCompras(LibroIVASearchFilter filter);
}
