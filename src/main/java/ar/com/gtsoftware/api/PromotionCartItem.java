package ar.com.gtsoftware.api;

import ar.com.gtsoftware.dto.domain.ComprobantesLineasDto;
import ar.com.gtsoftware.rules.TipoAccion;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
public class PromotionCartItem {

    private TipoAccion tipoAccion;
    private BigDecimal descuento;
    private String nombreOferta;
    @Setter
    private ComprobantesLineasDto linea;

    public void applyDiscount(TipoAccion tipoAccion, BigDecimal descuento, String nombreOferta) {
        this.tipoAccion = tipoAccion;
        this.descuento = descuento;
        this.nombreOferta = nombreOferta;
    }

    public boolean isDiscountApplicable() {
        return tipoAccion != null && descuento != null;
    }
}
