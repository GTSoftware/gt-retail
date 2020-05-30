//
// Este archivo ha sido generado por la arquitectura JavaTM para la implantación de la referencia de enlace (JAXB) XML v2.2.8-b130911.1802 
// Visite <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Todas las modificaciones realizadas en este archivo se perderán si se vuelve a compilar el esquema de origen. 
// Generado el: 2020.05.28 a las 09:17:46 PM ART 
//


package ar.com.gtsoftware.service.afip.client.login;

import javax.annotation.Generated;
import javax.xml.bind.annotation.*;


/**
 * <p>Clase Java para anonymous complex type.
 *
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 *
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="in0" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "in0"
})
@XmlRootElement(name = "loginCms", namespace = "http://wsaa.view.sua.dvadac.desein.afip.gov")
@Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2020-05-28T09:17:46-03:00", comments = "JAXB RI v2.2.8-b130911.1802")
public class LoginCms {

    @XmlElement(namespace = "http://wsaa.view.sua.dvadac.desein.afip.gov", required = true)
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2020-05-28T09:17:46-03:00", comments = "JAXB RI v2.2.8-b130911.1802")
    protected String in0;

    /**
     * Obtiene el valor de la propiedad in0.
     *
     * @return possible object is
     * {@link String }
     */
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2020-05-28T09:17:46-03:00", comments = "JAXB RI v2.2.8-b130911.1802")
    public String getIn0() {
        return in0;
    }

    /**
     * Define el valor de la propiedad in0.
     *
     * @param value allowed object is
     *              {@link String }
     */
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2020-05-28T09:17:46-03:00", comments = "JAXB RI v2.2.8-b130911.1802")
    public void setIn0(String value) {
        this.in0 = value;
    }

}
