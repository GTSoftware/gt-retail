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

/**
 *
 * @author rodrigo
 */
@Entity
@Table(name = "fiscal_letras_comprobantes")
@Getter
@Setter
public class FiscalLetrasComprobantes extends GTEntity<FiscalLetrasComprobantesPK> {

    @EmbeddedId
    protected FiscalLetrasComprobantesPK fiscalLetrasComprobantesPK;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "letra_comprobante")
    private String letraComprobante;
    @JoinColumn(name = "id_resoponsabildiad_iva_receptor", referencedColumnName = "id_resoponsabildiad_iva", insertable = false, updatable = false, columnDefinition = "int4")
    @ManyToOne(optional = false)
    private FiscalResponsabilidadesIva idResponsabilidadIvaReceptor;
    @JoinColumn(name = "id_resoponsabildiad_iva_emisor", referencedColumnName = "id_resoponsabildiad_iva", insertable = false, updatable = false, columnDefinition = "int4")
    @ManyToOne(optional = false)
    private FiscalResponsabilidadesIva idResponsabilidadIvaEmisor;

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (fiscalLetrasComprobantesPK != null ? fiscalLetrasComprobantesPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FiscalLetrasComprobantes)) {
            return false;
        }
        FiscalLetrasComprobantes other = (FiscalLetrasComprobantes) object;
        return (this.fiscalLetrasComprobantesPK != null || other.fiscalLetrasComprobantesPK == null) && (this.fiscalLetrasComprobantesPK == null || this.fiscalLetrasComprobantesPK.equals(other.fiscalLetrasComprobantesPK));
    }

    @Override
    public String toString() {
        return "ar.com.gtsoftware.model.FiscalLetrasComprobantes[ fiscalLetrasComprobantesPK=" + fiscalLetrasComprobantesPK + " ]";
    }

    @Override
    public boolean isNew() {
        return fiscalLetrasComprobantesPK == null;
    }

    @Override
    public FiscalLetrasComprobantesPK getId() {
        return fiscalLetrasComprobantesPK;
    }

    @Override
    public FiscalLetrasComprobantesPK calculateId(String id) {
        if (id != null) {
            String[] pkStr = id.split("-");
            if (pkStr.length == 2) {
                return new FiscalLetrasComprobantesPK(Integer.parseInt(pkStr[0]), Integer.parseInt(pkStr[1]));
            }
        }
        return null;
    }

    @Override
    public String getStringId() {
        if (fiscalLetrasComprobantesPK != null) {
            return fiscalLetrasComprobantesPK.toString();
        }
        return null;
    }

}
