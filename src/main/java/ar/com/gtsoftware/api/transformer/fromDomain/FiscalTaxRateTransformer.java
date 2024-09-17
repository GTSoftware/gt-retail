package ar.com.gtsoftware.api.transformer.fromDomain;

import ar.com.gtsoftware.api.response.FiscalTaxRate;
import ar.com.gtsoftware.api.transformer.Transformer;
import ar.com.gtsoftware.dto.domain.FiscalAlicuotasIvaDto;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import org.springframework.stereotype.Component;

@Component
public class FiscalTaxRateTransformer implements Transformer<FiscalAlicuotasIvaDto, FiscalTaxRate> {

  @Override
  public FiscalTaxRate transform(FiscalAlicuotasIvaDto alicuota) {
    Objects.requireNonNull(alicuota);

    return FiscalTaxRate.builder()
        .rate(alicuota.getValorAlicuota())
        .taxRateId(alicuota.getId())
        .taxRateName(alicuota.getNombreAlicuotaIva())
        .displayName(
            String.format(DISPLAY_NAME_FMT, alicuota.getId(), alicuota.getNombreAlicuotaIva()))
            .taxable(alicuota.isGravarIva())
        .build();
  }

  @Override
  public List<FiscalTaxRate> transform(List<FiscalAlicuotasIvaDto> alicuotas) {
    Objects.requireNonNull(alicuotas);

    List<FiscalTaxRate> fiscalRates = new LinkedList<>();

    for (FiscalAlicuotasIvaDto alicuota : alicuotas) {
      fiscalRates.add(transform(alicuota));
    }

    return fiscalRates;
  }
}
