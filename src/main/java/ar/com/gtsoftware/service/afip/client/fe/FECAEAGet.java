//
// Este archivo ha sido generado por la arquitectura JavaTM para la implantación de la referencia de enlace (JAXB) XML v2.2.8-b130911.1802 
// Visite <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Todas las modificaciones realizadas en este archivo se perderán si se vuelve a compilar el esquema de origen. 
// Generado el: 2020.05.28 a las 09:17:55 PM ART 
//


package ar.com.gtsoftware.service.afip.client.fe;

import javax.annotation.Generated;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para FECAEAGet complex type.
 *
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 *
 * <pre>
 * &lt;complexType name="FECAEAGet">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="CAEA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Periodo" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="Orden" type="{http://www.w3.org/2001/XMLSchema}short"/>
 *         &lt;element name="FchVigDesde" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="FchVigHasta" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="FchTopeInf" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="FchProceso" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Observaciones" type="{http://ar.gov.afip.dif.FEV1/}ArrayOfObs" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "FECAEAGet", propOrder = {
        "caea",
        "periodo",
        "orden",
        "fchVigDesde",
        "fchVigHasta",
        "fchTopeInf",
        "fchProceso",
        "observaciones"
})
@Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2020-05-28T09:17:55-03:00", comments = "JAXB RI v2.2.8-b130911.1802")
public class FECAEAGet {

    @XmlElement(name = "CAEA")
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2020-05-28T09:17:55-03:00", comments = "JAXB RI v2.2.8-b130911.1802")
    protected String caea;
    @XmlElement(name = "Periodo")
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2020-05-28T09:17:55-03:00", comments = "JAXB RI v2.2.8-b130911.1802")
    protected int periodo;
    @XmlElement(name = "Orden")
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2020-05-28T09:17:55-03:00", comments = "JAXB RI v2.2.8-b130911.1802")
    protected short orden;
    @XmlElement(name = "FchVigDesde")
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2020-05-28T09:17:55-03:00", comments = "JAXB RI v2.2.8-b130911.1802")
    protected String fchVigDesde;
    @XmlElement(name = "FchVigHasta")
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2020-05-28T09:17:55-03:00", comments = "JAXB RI v2.2.8-b130911.1802")
    protected String fchVigHasta;
    @XmlElement(name = "FchTopeInf")
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2020-05-28T09:17:55-03:00", comments = "JAXB RI v2.2.8-b130911.1802")
    protected String fchTopeInf;
    @XmlElement(name = "FchProceso")
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2020-05-28T09:17:55-03:00", comments = "JAXB RI v2.2.8-b130911.1802")
    protected String fchProceso;
    @XmlElement(name = "Observaciones")
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2020-05-28T09:17:55-03:00", comments = "JAXB RI v2.2.8-b130911.1802")
    protected ArrayOfObs observaciones;

    /**
     * Obtiene el valor de la propiedad caea.
     *
     * @return possible object is
     * {@link String }
     */
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2020-05-28T09:17:55-03:00", comments = "JAXB RI v2.2.8-b130911.1802")
    public String getCAEA() {
        return caea;
    }

    /**
     * Define el valor de la propiedad caea.
     *
     * @param value allowed object is
     *              {@link String }
     */
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2020-05-28T09:17:55-03:00", comments = "JAXB RI v2.2.8-b130911.1802")
    public void setCAEA(String value) {
        this.caea = value;
    }

    /**
     * Obtiene el valor de la propiedad periodo.
     */
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2020-05-28T09:17:55-03:00", comments = "JAXB RI v2.2.8-b130911.1802")
    public int getPeriodo() {
        return periodo;
    }

    /**
     * Define el valor de la propiedad periodo.
     */
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2020-05-28T09:17:55-03:00", comments = "JAXB RI v2.2.8-b130911.1802")
    public void setPeriodo(int value) {
        this.periodo = value;
    }

    /**
     * Obtiene el valor de la propiedad orden.
     */
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2020-05-28T09:17:55-03:00", comments = "JAXB RI v2.2.8-b130911.1802")
    public short getOrden() {
        return orden;
    }

    /**
     * Define el valor de la propiedad orden.
     */
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2020-05-28T09:17:55-03:00", comments = "JAXB RI v2.2.8-b130911.1802")
    public void setOrden(short value) {
        this.orden = value;
    }

    /**
     * Obtiene el valor de la propiedad fchVigDesde.
     *
     * @return possible object is
     * {@link String }
     */
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2020-05-28T09:17:55-03:00", comments = "JAXB RI v2.2.8-b130911.1802")
    public String getFchVigDesde() {
        return fchVigDesde;
    }

    /**
     * Define el valor de la propiedad fchVigDesde.
     *
     * @param value allowed object is
     *              {@link String }
     */
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2020-05-28T09:17:55-03:00", comments = "JAXB RI v2.2.8-b130911.1802")
    public void setFchVigDesde(String value) {
        this.fchVigDesde = value;
    }

    /**
     * Obtiene el valor de la propiedad fchVigHasta.
     *
     * @return possible object is
     * {@link String }
     */
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2020-05-28T09:17:55-03:00", comments = "JAXB RI v2.2.8-b130911.1802")
    public String getFchVigHasta() {
        return fchVigHasta;
    }

    /**
     * Define el valor de la propiedad fchVigHasta.
     *
     * @param value allowed object is
     *              {@link String }
     */
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2020-05-28T09:17:55-03:00", comments = "JAXB RI v2.2.8-b130911.1802")
    public void setFchVigHasta(String value) {
        this.fchVigHasta = value;
    }

    /**
     * Obtiene el valor de la propiedad fchTopeInf.
     *
     * @return possible object is
     * {@link String }
     */
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2020-05-28T09:17:55-03:00", comments = "JAXB RI v2.2.8-b130911.1802")
    public String getFchTopeInf() {
        return fchTopeInf;
    }

    /**
     * Define el valor de la propiedad fchTopeInf.
     *
     * @param value allowed object is
     *              {@link String }
     */
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2020-05-28T09:17:55-03:00", comments = "JAXB RI v2.2.8-b130911.1802")
    public void setFchTopeInf(String value) {
        this.fchTopeInf = value;
    }

    /**
     * Obtiene el valor de la propiedad fchProceso.
     *
     * @return possible object is
     * {@link String }
     */
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2020-05-28T09:17:55-03:00", comments = "JAXB RI v2.2.8-b130911.1802")
    public String getFchProceso() {
        return fchProceso;
    }

    /**
     * Define el valor de la propiedad fchProceso.
     *
     * @param value allowed object is
     *              {@link String }
     */
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2020-05-28T09:17:55-03:00", comments = "JAXB RI v2.2.8-b130911.1802")
    public void setFchProceso(String value) {
        this.fchProceso = value;
    }

    /**
     * Obtiene el valor de la propiedad observaciones.
     *
     * @return possible object is
     * {@link ArrayOfObs }
     */
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2020-05-28T09:17:55-03:00", comments = "JAXB RI v2.2.8-b130911.1802")
    public ArrayOfObs getObservaciones() {
        return observaciones;
    }

    /**
     * Define el valor de la propiedad observaciones.
     *
     * @param value allowed object is
     *              {@link ArrayOfObs }
     */
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2020-05-28T09:17:55-03:00", comments = "JAXB RI v2.2.8-b130911.1802")
    public void setObservaciones(ArrayOfObs value) {
        this.observaciones = value;
    }

}
