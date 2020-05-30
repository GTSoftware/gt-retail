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
 * <p>Clase Java para FECAEAResponse complex type.
 *
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 *
 * <pre>
 * &lt;complexType name="FECAEAResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="FeCabResp" type="{http://ar.gov.afip.dif.FEV1/}FECAEACabResponse" minOccurs="0"/>
 *         &lt;element name="FeDetResp" type="{http://ar.gov.afip.dif.FEV1/}ArrayOfFECAEADetResponse" minOccurs="0"/>
 *         &lt;element name="Events" type="{http://ar.gov.afip.dif.FEV1/}ArrayOfEvt" minOccurs="0"/>
 *         &lt;element name="Errors" type="{http://ar.gov.afip.dif.FEV1/}ArrayOfErr" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "FECAEAResponse", propOrder = {
        "feCabResp",
        "feDetResp",
        "events",
        "errors"
})
@Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2020-05-28T09:17:55-03:00", comments = "JAXB RI v2.2.8-b130911.1802")
public class FECAEAResponse {

    @XmlElement(name = "FeCabResp")
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2020-05-28T09:17:55-03:00", comments = "JAXB RI v2.2.8-b130911.1802")
    protected FECAEACabResponse feCabResp;
    @XmlElement(name = "FeDetResp")
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2020-05-28T09:17:55-03:00", comments = "JAXB RI v2.2.8-b130911.1802")
    protected ArrayOfFECAEADetResponse feDetResp;
    @XmlElement(name = "Events")
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2020-05-28T09:17:55-03:00", comments = "JAXB RI v2.2.8-b130911.1802")
    protected ArrayOfEvt events;
    @XmlElement(name = "Errors")
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2020-05-28T09:17:55-03:00", comments = "JAXB RI v2.2.8-b130911.1802")
    protected ArrayOfErr errors;

    /**
     * Obtiene el valor de la propiedad feCabResp.
     *
     * @return possible object is
     * {@link FECAEACabResponse }
     */
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2020-05-28T09:17:55-03:00", comments = "JAXB RI v2.2.8-b130911.1802")
    public FECAEACabResponse getFeCabResp() {
        return feCabResp;
    }

    /**
     * Define el valor de la propiedad feCabResp.
     *
     * @param value allowed object is
     *              {@link FECAEACabResponse }
     */
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2020-05-28T09:17:55-03:00", comments = "JAXB RI v2.2.8-b130911.1802")
    public void setFeCabResp(FECAEACabResponse value) {
        this.feCabResp = value;
    }

    /**
     * Obtiene el valor de la propiedad feDetResp.
     *
     * @return possible object is
     * {@link ArrayOfFECAEADetResponse }
     */
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2020-05-28T09:17:55-03:00", comments = "JAXB RI v2.2.8-b130911.1802")
    public ArrayOfFECAEADetResponse getFeDetResp() {
        return feDetResp;
    }

    /**
     * Define el valor de la propiedad feDetResp.
     *
     * @param value allowed object is
     *              {@link ArrayOfFECAEADetResponse }
     */
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2020-05-28T09:17:55-03:00", comments = "JAXB RI v2.2.8-b130911.1802")
    public void setFeDetResp(ArrayOfFECAEADetResponse value) {
        this.feDetResp = value;
    }

    /**
     * Obtiene el valor de la propiedad events.
     *
     * @return possible object is
     * {@link ArrayOfEvt }
     */
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2020-05-28T09:17:55-03:00", comments = "JAXB RI v2.2.8-b130911.1802")
    public ArrayOfEvt getEvents() {
        return events;
    }

    /**
     * Define el valor de la propiedad events.
     *
     * @param value allowed object is
     *              {@link ArrayOfEvt }
     */
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2020-05-28T09:17:55-03:00", comments = "JAXB RI v2.2.8-b130911.1802")
    public void setEvents(ArrayOfEvt value) {
        this.events = value;
    }

    /**
     * Obtiene el valor de la propiedad errors.
     *
     * @return possible object is
     * {@link ArrayOfErr }
     */
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2020-05-28T09:17:55-03:00", comments = "JAXB RI v2.2.8-b130911.1802")
    public ArrayOfErr getErrors() {
        return errors;
    }

    /**
     * Define el valor de la propiedad errors.
     *
     * @param value allowed object is
     *              {@link ArrayOfErr }
     */
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2020-05-28T09:17:55-03:00", comments = "JAXB RI v2.2.8-b130911.1802")
    public void setErrors(ArrayOfErr value) {
        this.errors = value;
    }

}
