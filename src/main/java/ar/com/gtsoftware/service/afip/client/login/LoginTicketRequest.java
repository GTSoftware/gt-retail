//
// Este archivo ha sido generado por la arquitectura JavaTM para la implantación de la referencia de enlace (JAXB) XML v2.2.8-b130911.1802 
// Visite <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Todas las modificaciones realizadas en este archivo se perderán si se vuelve a compilar el esquema de origen. 
// Generado el: 2020.05.28 a las 09:17:46 PM ART 
//


package ar.com.gtsoftware.service.afip.client.login;

import javax.annotation.Generated;
import javax.xml.bind.annotation.*;
import java.math.BigDecimal;


/**
 * <p>Clase Java para loginTicketRequest complex type.
 *
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 *
 * <pre>
 * &lt;complexType name="loginTicketRequest">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="header" type="{}headerType"/>
 *         &lt;element name="service" type="{}serviceType"/>
 *       &lt;/sequence>
 *       &lt;attribute name="version" type="{http://www.w3.org/2001/XMLSchema}decimal" default="1.0" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "loginTicketRequest", propOrder = {
        "header",
        "service"
})
@Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2020-05-28T09:17:46-03:00", comments = "JAXB RI v2.2.8-b130911.1802")
public class LoginTicketRequest {

    @XmlElement(required = true)
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2020-05-28T09:17:46-03:00", comments = "JAXB RI v2.2.8-b130911.1802")
    protected HeaderType header;
    @XmlElement(required = true)
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2020-05-28T09:17:46-03:00", comments = "JAXB RI v2.2.8-b130911.1802")
    protected String service;
    @XmlAttribute(name = "version")
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2020-05-28T09:17:46-03:00", comments = "JAXB RI v2.2.8-b130911.1802")
    protected BigDecimal version;

    /**
     * Obtiene el valor de la propiedad header.
     *
     * @return possible object is
     * {@link HeaderType }
     */
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2020-05-28T09:17:46-03:00", comments = "JAXB RI v2.2.8-b130911.1802")
    public HeaderType getHeader() {
        return header;
    }

    /**
     * Define el valor de la propiedad header.
     *
     * @param value allowed object is
     *              {@link HeaderType }
     */
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2020-05-28T09:17:46-03:00", comments = "JAXB RI v2.2.8-b130911.1802")
    public void setHeader(HeaderType value) {
        this.header = value;
    }

    /**
     * Obtiene el valor de la propiedad service.
     *
     * @return possible object is
     * {@link String }
     */
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2020-05-28T09:17:46-03:00", comments = "JAXB RI v2.2.8-b130911.1802")
    public String getService() {
        return service;
    }

    /**
     * Define el valor de la propiedad service.
     *
     * @param value allowed object is
     *              {@link String }
     */
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2020-05-28T09:17:46-03:00", comments = "JAXB RI v2.2.8-b130911.1802")
    public void setService(String value) {
        this.service = value;
    }

    /**
     * Obtiene el valor de la propiedad version.
     *
     * @return possible object is
     * {@link BigDecimal }
     */
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2020-05-28T09:17:46-03:00", comments = "JAXB RI v2.2.8-b130911.1802")
    public BigDecimal getVersion() {
        if (version == null) {
            return new BigDecimal("1.0");
        } else {
            return version;
        }
    }

    /**
     * Define el valor de la propiedad version.
     *
     * @param value allowed object is
     *              {@link BigDecimal }
     */
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2020-05-28T09:17:46-03:00", comments = "JAXB RI v2.2.8-b130911.1802")
    public void setVersion(BigDecimal value) {
        this.version = value;
    }

}
