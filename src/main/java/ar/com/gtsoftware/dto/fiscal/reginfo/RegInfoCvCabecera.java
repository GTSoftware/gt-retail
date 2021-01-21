package ar.com.gtsoftware.dto.fiscal.reginfo;

import static ar.com.gtsoftware.dto.fiscal.reginfo.RegInfoUtils.formatNumber;
import static ar.com.gtsoftware.dto.fiscal.reginfo.RegInfoUtils.numberPad;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Builder
@Getter
@Setter
public class RegInfoCvCabecera {

  private String cuitInformante;

  private Integer anioPeriodo;

  private Integer mesPeriodo;

  private Integer secuencia;

  private String sinMovimiento;

  private String prorratearCreditoFiscalComputable;

  private String creditoFiscalComputableGlobalOPorComprobante;

  private BigDecimal importeCreditoFiscalComputableGlobal;

  private BigDecimal importeCreditoFiscalComputableConAsignacionDirecta;

  private BigDecimal importeCreditoFiscalComputableDeterminadoPorProrrateo;

  private BigDecimal importeCreditoFiscalNoComputableGlobal;

  private BigDecimal importeCreditoFiscalContribSegSocyOtros;

  private BigDecimal importeCreditoFiscalComputableContribSegSocyOtros;

  @Override
  public String toString() {
    return cuitInformante
        + anioPeriodo
        + numberPad(mesPeriodo, 2)
        + numberPad(secuencia, 2)
        + sinMovimiento
        + prorratearCreditoFiscalComputable
        + creditoFiscalComputableGlobalOPorComprobante
        + formatNumber(importeCreditoFiscalComputableGlobal)
        + formatNumber(importeCreditoFiscalComputableConAsignacionDirecta)
        + formatNumber(importeCreditoFiscalComputableDeterminadoPorProrrateo)
        + formatNumber(importeCreditoFiscalNoComputableGlobal)
        + formatNumber(importeCreditoFiscalContribSegSocyOtros)
        + formatNumber(importeCreditoFiscalComputableContribSegSocyOtros);
  }
}
