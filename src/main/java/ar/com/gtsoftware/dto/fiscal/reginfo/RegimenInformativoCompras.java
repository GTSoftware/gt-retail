package ar.com.gtsoftware.dto.fiscal.reginfo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Builder
@Getter
public class RegimenInformativoCompras {
    private final List<RegInfoCvComprasCbte> comprobantes;
    private final List<RegInfoCvComprasAlicuotas> alicuotas;
}
