package ar.com.gtsoftware.api.response;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Builder
@Getter
@Setter
public class PlanDetail {
    private Long detailId;
    private int payments;
    private BigDecimal rate;
}
