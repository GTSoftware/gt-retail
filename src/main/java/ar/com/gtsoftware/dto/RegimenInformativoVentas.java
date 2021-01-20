package ar.com.gtsoftware.dto;

import ar.com.gtsoftware.dto.fiscal.reginfo.RegInfoCvVentasAlicuotas;
import ar.com.gtsoftware.dto.fiscal.reginfo.ReginfoCvCabecera;
import ar.com.gtsoftware.dto.fiscal.reginfo.ReginfoCvVentasCbte;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Builder
@Getter
public class RegimenInformativoVentas {
    private final ReginfoCvCabecera cabecera;
    private final List<ReginfoCvVentasCbte> comprobantes;
    private final List<RegInfoCvVentasAlicuotas> alicuotas;
}
