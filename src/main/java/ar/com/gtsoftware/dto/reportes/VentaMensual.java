package ar.com.gtsoftware.dto.reportes;

import java.io.Serializable;
import java.math.BigDecimal;
import lombok.*;

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
