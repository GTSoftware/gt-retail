package ar.com.gtsoftware.dto;

import ar.com.gtsoftware.dto.domain.PersonasDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Builder
@Getter
@Setter
public class PreparedPaymentDto {
    private PersonasDto customer;
    private List<SaleToPayDto> salesToPay;
}
