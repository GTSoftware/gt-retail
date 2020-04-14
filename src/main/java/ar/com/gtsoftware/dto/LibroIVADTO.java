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
 * Clase que almacena la información necesaria para un libro de IVA Ventas
 *
 * @author Rodrigo Tato <rotatomel@gmail.com>
 * @since 1.0.0
 * @version 1.0.0
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LibroIVADTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Date fechaDesde;
    private Date fechaHasta;
    private final Date fechaGeneracion = new Date();

    private List<RegistroIVADTO> facturasList;
    private BigDecimal importeTotal;
    private BigDecimal importeTotalIVA;
    private List<ImportesResponsabilidad> totalesIVAResponsabilidad;
    private List<ImportesAlicuotasIVA> totalesAlicuota;


    /**
     * Devuelve la fecha desde
     *
     * @return fechaDesde
     */
    public Date getFechaDesde() {
        return fechaDesde;
    }

    /**
     * Establece la fecha desde
     *
     * @param fechaDesde
     */
    public void setFechaDesde(Date fechaDesde) {
        this.fechaDesde = fechaDesde;
    }

    /**
     * Devuelve la fecha hasta
     *
     * @return fechaHasta
     */
    public Date getFechaHasta() {
        return fechaHasta;
    }

    /**
     * Estable la fecha hasta
     *
     * @param fechaHasta
     */
    public void setFechaHasta(Date fechaHasta) {
        this.fechaHasta = fechaHasta;
    }

    /**
     * Devuelve la fecha de generación del libro
     *
     * @return
     */
    public Date getFechaGeneracion() {
        return fechaGeneracion;
    }


    /**
     * Devuelve la lista de facturas
     *
     * @return facturasList
     */
    public List<RegistroIVADTO> getFacturasList() {
        return facturasList;
    }

    /**
     * Establece la lista de facturas
     *
     * @param facturasList
     */
    public void setFacturasList(List<RegistroIVADTO> facturasList) {
        this.facturasList = facturasList;
    }

    /**
     * Devuelve el importe total de todas las facturas del libro
     *
     * @return
     */
    public BigDecimal getImporteTotal() {
        return importeTotal;
    }

    /**
     * Establece el importe total de todas las facturas del libro
     *
     * @param importeTotal
     */
    public void setImporteTotal(BigDecimal importeTotal) {
        this.importeTotal = importeTotal;
    }

    /**
     * Devuelve el importe total de IVA del libro
     *
     * @return importeTotalIVA
     */
    public BigDecimal getImporteTotalIVA() {
        return importeTotalIVA;
    }

    /**
     * Establece el importe total de IVA del libro
     *
     * @param importeTotalIVA
     */
    public void setImporteTotalIVA(BigDecimal importeTotalIVA) {
        this.importeTotalIVA = importeTotalIVA;
    }

    /**
     * Devuelve los totales por responsabilidad de IVA
     *
     * @return
     */
    public List<ImportesResponsabilidad> getTotalesIVAResponsabilidad() {
        return totalesIVAResponsabilidad;
    }

    /**
     * Establece los totales por responsabilidad de IVA
     *
     * @param totalesIVAResponsabilidad
     */
    public void setTotalesIVAResponsabilidad(List<ImportesResponsabilidad> totalesIVAResponsabilidad) {
        this.totalesIVAResponsabilidad = totalesIVAResponsabilidad;
    }

    /**
     * Devuelve el total de iva por alícuota
     *
     * @return
     */
    public List<ImportesAlicuotasIVA> getTotalesAlicuota() {
        return totalesAlicuota;
    }

    /**
     * Establece el total de iva por alícuota
     *
     * @param totalesAlicuota
     */
    public void setTotalesAlicuota(List<ImportesAlicuotasIVA> totalesAlicuota) {
        this.totalesAlicuota = totalesAlicuota;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 23 * hash + Objects.hashCode(this.fechaGeneracion);
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
        final LibroIVADTO other = (LibroIVADTO) obj;
        if (!Objects.equals(this.fechaGeneracion, other.fechaGeneracion)) {
            return false;
        }
        return true;
    }

}
