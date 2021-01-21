package ar.com.gtsoftware.dto.fiscal.reginfo;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class RegimenInformativoCompras {
  private final List<RegInfoCvComprasCbte> comprobantes;
  private final List<RegInfoCvComprasAlicuotas> alicuotas;
}
