//
// Este archivo ha sido generado por Eclipse Implementation of JAXB v2.3.4
// Visite https://eclipse-ee4j.github.io/jaxb-ri
// Todas las modificaciones realizadas en este archivo se perderán si se vuelve a compilar el
// esquema de origen.
// Generado el: 2021.04.20 a las 01:12:11 AM ART
//

package ar.com.gtsoftware.service.afip.client.fe;

import javax.annotation.Generated;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Clase Java para FECAEADetRequest complex type.
 *
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta
 * clase.
 *
 * <pre>
 * &lt;complexType name="FECAEADetRequest"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://ar.gov.afip.dif.FEV1/}FEDetRequest"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="CAEA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="CbteFchHsGen" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
    name = "FECAEADetRequest",
    propOrder = {"caea", "cbteFchHsGen"})
@Generated(
    value = "com.sun.tools.xjc.Driver",
    comments = "JAXB RI v2.3.4",
    date = "2021-04-20T01:12:11-03:00")
public class FECAEADetRequest extends FEDetRequest {

  @XmlElement(name = "CAEA")
  @Generated(
      value = "com.sun.tools.xjc.Driver",
      comments = "JAXB RI v2.3.4",
      date = "2021-04-20T01:12:11-03:00")
  protected String caea;

  @XmlElement(name = "CbteFchHsGen")
  @Generated(
      value = "com.sun.tools.xjc.Driver",
      comments = "JAXB RI v2.3.4",
      date = "2021-04-20T01:12:11-03:00")
  protected String cbteFchHsGen;

  /**
   * Obtiene el valor de la propiedad caea.
   *
   * @return possible object is {@link String }
   */
  @Generated(
      value = "com.sun.tools.xjc.Driver",
      comments = "JAXB RI v2.3.4",
      date = "2021-04-20T01:12:11-03:00")
  public String getCAEA() {
    return caea;
  }

  /**
   * Define el valor de la propiedad caea.
   *
   * @param value allowed object is {@link String }
   */
  @Generated(
      value = "com.sun.tools.xjc.Driver",
      comments = "JAXB RI v2.3.4",
      date = "2021-04-20T01:12:11-03:00")
  public void setCAEA(String value) {
    this.caea = value;
  }

  /**
   * Obtiene el valor de la propiedad cbteFchHsGen.
   *
   * @return possible object is {@link String }
   */
  @Generated(
      value = "com.sun.tools.xjc.Driver",
      comments = "JAXB RI v2.3.4",
      date = "2021-04-20T01:12:11-03:00")
  public String getCbteFchHsGen() {
    return cbteFchHsGen;
  }

  /**
   * Define el valor de la propiedad cbteFchHsGen.
   *
   * @param value allowed object is {@link String }
   */
  @Generated(
      value = "com.sun.tools.xjc.Driver",
      comments = "JAXB RI v2.3.4",
      date = "2021-04-20T01:12:11-03:00")
  public void setCbteFchHsGen(String value) {
    this.cbteFchHsGen = value;
  }
}
