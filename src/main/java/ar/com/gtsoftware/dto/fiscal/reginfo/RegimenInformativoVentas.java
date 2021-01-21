package ar.com.gtsoftware.dto.fiscal.reginfo;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class RegimenInformativoVentas {
  private final RegInfoCvCabecera cabecera;
  private final List<RegInfoCvVentasCbte> comprobantes;
  private final List<RegInfoCvVentasAlicuotas> alicuotas;
}
