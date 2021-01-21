package ar.com.gtsoftware.api.response;

import java.math.BigDecimal;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class PlanDetail {
  private Long detailId;
  private int payments;
  private BigDecimal rate;
}
