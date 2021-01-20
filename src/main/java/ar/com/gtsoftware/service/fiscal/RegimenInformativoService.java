package ar.com.gtsoftware.service.fiscal;

import ar.com.gtsoftware.dto.RegimenInformativoVentas;
import ar.com.gtsoftware.search.LibroIVASearchFilter;

public interface RegimenInformativoService {
    RegimenInformativoVentas getRegimenInformativoVentas(LibroIVASearchFilter filter);
}
