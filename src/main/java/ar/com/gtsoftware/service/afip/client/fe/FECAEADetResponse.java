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
 * <p>Clase Java para FECAEADetResponse complex type.
 *
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 *
 * <pre>
 * &lt;complexType name="FECAEADetResponse">
 *   &lt;complexContent>
 *     &lt;extension base="{http://ar.gov.afip.dif.FEV1/}FEDetResponse">
 *       &lt;sequence>
 *         &lt;element name="CAEA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "FECAEADetResponse", propOrder = {
        "caea"
})
@Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2020-05-28T09:17:55-03:00", comments = "JAXB RI v2.2.8-b130911.1802")
public class FECAEADetResponse
        extends FEDetResponse {

    @XmlElement(name = "CAEA")
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2020-05-28T09:17:55-03:00", comments = "JAXB RI v2.2.8-b130911.1802")
    protected String caea;

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

}