package ar.com.gtsoftware.dto.reportes;

import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class VentaMensual implements Serializable {
    private Integer anio;
    private Integer mes;
    private BigDecimal total;
}
