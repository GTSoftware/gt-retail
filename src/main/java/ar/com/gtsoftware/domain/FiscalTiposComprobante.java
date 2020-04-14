/*
 * Copyright 2014 GT Software.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ar.com.gtsoftware.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;

/**
 * Clase que mapea a la tabla fiscal_tipos_comprobante para regímenes informativos
 *
 * @author Rodrigo M. Tato Rothamel <rotatomel@gmail.com>
 */
@Entity
@Table(name = "fiscal_tipos_comprobante")
@Getter
@Setter
public class FiscalTiposComprobante extends GTEntity<String> {

    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 3, max = 3)
    @Column(name = "codigo_tipo_comprobante")
    private String codigoTipoComprobante;
    @Size(max = 100)
    @Column(name = "denominacion_comprobante")
    private String denominacionComprobante;

    @Size(max = 1)
    @Column(name = "letra")
    private String letra;

    @JoinColumn(name = "id_negocio_tipo_comprobante", referencedColumnName = "id_negocio_tipo_comprobante")
    @ManyToOne
    private NegocioTiposComprobante tipoComprobante;

    @Override
    public boolean isNew() {
        return codigoTipoComprobante == null;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 67 * hash + Objects.hashCode(this.codigoTipoComprobante);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final FiscalTiposComprobante other = (FiscalTiposComprobante) obj;
        return Objects.equals(this.codigoTipoComprobante, other.codigoTipoComprobante);
    }

    @Override
    public String toString() {
        return "FiscalTiposComprobanteDto{" + "codigoTipoComprobante=" + codigoTipoComprobante + ", denominacionComprobante=" + denominacionComprobante + '}';
    }

    @Override
    public String getId() {
        return codigoTipoComprobante;
    }

    @Override
    public String calculateId(String id) {
        return id;
    }

    @Override
    public String getStringId() {
        return codigoTipoComprobante;
    }

}
