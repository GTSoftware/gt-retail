package ar.com.gtsoftware.dto.fiscal.reginfo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Builder
@Getter
public class RegimenInformativoVentas {
    private final RegInfoCvCabecera cabecera;
    private final List<RegInfoCvVentasCbte> comprobantes;
    private final List<RegInfoCvVentasAlicuotas> alicuotas;
}
