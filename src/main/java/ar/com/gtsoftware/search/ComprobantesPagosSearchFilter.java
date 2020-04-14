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
package ar.com.gtsoftware.search;

/**
 * SearchFilter para Arqueos de caja
 *
 * @author Rodrigo M. Tato Rothamel
 */
public class ComprobantesPagosSearchFilter extends AbstractSearchFilter {

    private static final long serialVersionUID = 1L;

    private Long idComprobante;
    private Boolean conSaldo;

    public ComprobantesPagosSearchFilter() {
    }

    public ComprobantesPagosSearchFilter(Long idComprobante, Boolean conSaldo) {
        this.idComprobante = idComprobante;
        this.conSaldo = conSaldo;
    }

    @Override
    public boolean hasFilter() {
        return idComprobante != null || conSaldo != null;
    }

    public Long getIdComprobante() {
        return idComprobante;
    }

    public void setIdComprobante(Long idComprobante) {
        this.idComprobante = idComprobante;
    }

    public Boolean getConSaldo() {
        return conSaldo;
    }

    public void setConSaldo(Boolean conSaldo) {
        this.conSaldo = conSaldo;
    }

}
