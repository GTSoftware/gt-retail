/*
 * Copyright 2018 GT Software.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package ar.com.gtsoftware.dto.domain;

import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Clase que representa las compras que se realizan
 *
 * @author Rodrigo Tato <rotatomel@gmail.com>
 */
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProveedoresComprobantesDto {

    private static final long serialVersionUID = 1L;

    @EqualsAndHashCode.Include
    private Long id;

    private Date fechaComprobante;
    @NotNull
    private BigDecimal total;

    @Size(max = 1024)
    private String observaciones;
    private boolean anulada;

    @Size(max = 1)
    private String letra;
    @NotNull
    private NegocioTiposComprobanteDto tipoComprobante;

    private UsuariosDto idUsuario;
    private SucursalesDto idSucursal;
    private PersonasDto idProveedor;


    private FiscalLibroIvaComprasDto idRegistro;

    private BigDecimal totalConSigno;
    private Integer version;


    public BigDecimal getTotalConSigno() {
        if (totalConSigno == null) {
            totalConSigno = total.multiply(tipoComprobante.getSigno());
        }
        return totalConSigno;
    }

    @Override
    public String toString() {
        if (idRegistro != null) {
            return String.format("[%d] %s %s %s-%s", getId(), tipoComprobante.getNombreComprobante(), letra,
                    idRegistro.getPuntoVentaFactura(), idRegistro.getNumeroFactura());
        }
        return String.format("[%d] %s", getId(), tipoComprobante.getNombreComprobante());
    }


}
