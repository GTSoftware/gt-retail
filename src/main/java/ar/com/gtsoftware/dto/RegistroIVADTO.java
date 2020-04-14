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
package ar.com.gtsoftware.dto;

import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * Clase que almacena la información de un registro de IVA de manera resumida
 *
 * @author Rodrigo Tato <rotatomel@gmail.com>
 * @version 1.0.0
 * @since 1.0.0
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegistroIVADTO implements Serializable {

    private static final long serialVersionUID = 3L;

    private Long idFactura;
    private Date fechaFactura;
    private String numeroFactura;
    private String tipoDocumento;
    private String tipoComprobante;
    private String documentoCliente;
    private String razonSocialCliente;
    private String categoriaIVACliente;
    private BigDecimal netoGravado;
    private BigDecimal noGravado;
    private BigDecimal totalIva;
    private BigDecimal otrosTributos;
    private BigDecimal exento;
    private BigDecimal percepcionIva;
    private BigDecimal percepcionIngresosBrutos;
    private List<ImportesAlicuotasIVA> totalAlicuota;
    private BigDecimal totalFactura;

    /**
     * Devuelve el identificador de la factura
     *
     * @return idFactura
     */
    public Long getIdFactura() {
        return idFactura;
    }

    /**
     * Establece el identificador de la factura
     *
     * @param idFactura
     */
    public void setIdFactura(Long idFactura) {
        this.idFactura = idFactura;
    }

    /**
     * Devuelve la fecha de factura
     *
     * @return
     */
    public Date getFechaFactura() {
        return fechaFactura;
    }

    /**
     * Establece la fecha de la factura
     *
     * @param fechaFactura
     */
    public void setFechaFactura(Date fechaFactura) {
        this.fechaFactura = fechaFactura;
    }

    /**
     * Devuelve el número de factura con el formato: Letra PuntoVenta-Numero Ej: B 0001-00000000
     *
     * @return numeroFactura
     */
    public String getNumeroFactura() {
        return numeroFactura;
    }

    /**
     * Establece el número de factura. Debe estar en el formato Letra PuntoVenta-Numero
     *
     * @param numeroFactura
     */
    public void setNumeroFactura(String numeroFactura) {
        this.numeroFactura = numeroFactura;
    }

    /**
     * Devuelve el total de neto gravado
     *
     * @return netoGrabado
     */
    public BigDecimal getNetoGravado() {
        return netoGravado;
    }

    /**
     * Establece el total de neto gravado
     *
     * @param netoGravado
     */
    public void setNetoGravado(BigDecimal netoGravado) {
        this.netoGravado = netoGravado;
    }

    /**
     * Devuelve el monto no gravado
     *
     * @return noGravado
     */
    public BigDecimal getNoGravado() {
        return noGravado;
    }

    /**
     * Establece el monto no gravado
     *
     * @param noGravado
     */
    public void setNoGravado(BigDecimal noGravado) {
        this.noGravado = noGravado;
    }

    /**
     * Devuelve el total de IVA
     *
     * @return totalIVA
     */
    public BigDecimal getTotalIva() {
        return totalIva;
    }

    /**
     * Establece el total de IVA
     *
     * @param totalIva
     */
    public void setTotalIva(BigDecimal totalIva) {
        this.totalIva = totalIva;
    }

    /**
     * Devuelve el total de IVA por cada alícuota de la venta
     *
     * @return totalAlicuota
     */
    public List<ImportesAlicuotasIVA> getTotalAlicuota() {
        return totalAlicuota;
    }

    /**
     * Establece el total de IVA por cada alicuota de la venta
     *
     * @param totalAlicuota
     */
    public void setTotalAlicuota(List<ImportesAlicuotasIVA> totalAlicuota) {
        this.totalAlicuota = totalAlicuota;
    }

    /**
     * Devuelve el total de la factura
     *
     * @return
     */
    public BigDecimal getTotalFactura() {
        return totalFactura;
    }

    /**
     * Establece el total de la factura
     *
     * @param totalFactura
     */
    public void setTotalFactura(BigDecimal totalFactura) {
        this.totalFactura = totalFactura;
    }

    /**
     * Devuelve el número de documento del cliente
     *
     * @return
     */
    public String getDocumentoCliente() {
        return documentoCliente;
    }

    /**
     * Establece el número de documento del cliente
     *
     * @param documentoCliente
     */
    public void setDocumentoCliente(String documentoCliente) {
        this.documentoCliente = documentoCliente;
    }

    /**
     * Devuelve la razón social del cliente
     *
     * @return
     */
    public String getRazonSocialCliente() {
        return razonSocialCliente;
    }

    /**
     * Establece la razón social del cliente
     *
     * @param razonSocialCliente
     */
    public void setRazonSocialCliente(String razonSocialCliente) {
        this.razonSocialCliente = razonSocialCliente;
    }

    /**
     * Total de otros tributos asociados
     *
     * @return
     */
    public BigDecimal getOtrosTributos() {
        return otrosTributos;
    }

    /**
     * Total de otros tributos asociados
     *
     * @param otrosTributos
     */
    public void setOtrosTributos(BigDecimal otrosTributos) {
        this.otrosTributos = otrosTributos;
    }

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public String getTipoComprobante() {
        return tipoComprobante;
    }

    public void setTipoComprobante(String tipoComprobante) {
        this.tipoComprobante = tipoComprobante;
    }

    public String getCategoriaIVACliente() {
        return categoriaIVACliente;
    }

    public void setCategoriaIVACliente(String categoriaIVACliente) {
        this.categoriaIVACliente = categoriaIVACliente;
    }

    public BigDecimal getExento() {
        return exento;
    }

    public void setExento(BigDecimal exento) {
        this.exento = exento;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 23 * hash + Objects.hashCode(this.idFactura);
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
        final RegistroIVADTO other = (RegistroIVADTO) obj;
        if (!Objects.equals(this.idFactura, other.idFactura)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "FacturaDTO{" + "idFactura=" + idFactura + ", numeroFactura=" + numeroFactura + '}';
    }

}
