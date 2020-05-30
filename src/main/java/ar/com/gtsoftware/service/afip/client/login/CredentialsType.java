//
// Este archivo ha sido generado por la arquitectura JavaTM para la implantación de la referencia de enlace (JAXB) XML v2.2.8-b130911.1802 
// Visite <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Todas las modificaciones realizadas en este archivo se perderán si se vuelve a compilar el esquema de origen. 
// Generado el: 2020.05.28 a las 09:17:46 PM ART 
//


package ar.com.gtsoftware.service.afip.client.login;

import javax.annotation.Generated;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para credentialsType complex type.
 *
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 *
 * <pre>
 * &lt;complexType name="credentialsType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="token" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="sign" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "credentialsType", propOrder = {
        "token",
        "sign"
})
@Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2020-05-28T09:17:46-03:00", comments = "JAXB RI v2.2.8-b130911.1802")
public class CredentialsType {

    @XmlElement(required = true)
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2020-05-28T09:17:46-03:00", comments = "JAXB RI v2.2.8-b130911.1802")
    protected String token;
    @XmlElement(required = true)
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2020-05-28T09:17:46-03:00", comments = "JAXB RI v2.2.8-b130911.1802")
    protected String sign;

    /**
     * Obtiene el valor de la propiedad token.
     *
     * @return possible object is
     * {@link String }
     */
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2020-05-28T09:17:46-03:00", comments = "JAXB RI v2.2.8-b130911.1802")
    public String getToken() {
        return token;
    }

    /**
     * Define el valor de la propiedad token.
     *
     * @param value allowed object is
     *              {@link String }
     */
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2020-05-28T09:17:46-03:00", comments = "JAXB RI v2.2.8-b130911.1802")
    public void setToken(String value) {
        this.token = value;
    }

    /**
     * Obtiene el valor de la propiedad sign.
     *
     * @return possible object is
     * {@link String }
     */
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2020-05-28T09:17:46-03:00", comments = "JAXB RI v2.2.8-b130911.1802")
    public String getSign() {
        return sign;
    }

    /**
     * Define el valor de la propiedad sign.
     *
     * @param value allowed object is
     *              {@link String }
     */
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2020-05-28T09:17:46-03:00", comments = "JAXB RI v2.2.8-b130911.1802")
    public void setSign(String value) {
        this.sign = value;
    }

}
