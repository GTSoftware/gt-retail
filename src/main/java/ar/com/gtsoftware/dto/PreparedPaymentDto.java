package ar.com.gtsoftware.dto;

import ar.com.gtsoftware.dto.domain.PersonasDto;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class PreparedPaymentDto {
  private PersonasDto customer;
  private List<SaleToPayDto> salesToPay;
}
