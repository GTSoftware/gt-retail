//
// Este archivo ha sido generado por Eclipse Implementation of JAXB v2.3.4
// Visite https://eclipse-ee4j.github.io/jaxb-ri
// Todas las modificaciones realizadas en este archivo se perderán si se vuelve a compilar el
// esquema de origen.
// Generado el: 2021.06.08 a las 12:50:16 AM ART
//

package ar.com.gtsoftware.service.afip.client.fe;

import javax.annotation.Generated;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Clase Java para anonymous complex type.
 *
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta
 * clase.
 *
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="FECompUltimoAutorizadoResult" type="{http://ar.gov.afip.dif.FEV1/}FERecuperaLastCbteResponse" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
    name = "",
    propOrder = {"feCompUltimoAutorizadoResult"})
@XmlRootElement(name = "FECompUltimoAutorizadoResponse", namespace = "http://ar.gov.afip.dif.FEV1/")
@Generated(
    value = "com.sun.tools.xjc.Driver",
    comments = "JAXB RI v2.3.4",
    date = "2021-06-08T00:50:15-03:00")
public class FECompUltimoAutorizadoResponse {

  @XmlElement(name = "FECompUltimoAutorizadoResult", namespace = "http://ar.gov.afip.dif.FEV1/")
  @Generated(
      value = "com.sun.tools.xjc.Driver",
      comments = "JAXB RI v2.3.4",
      date = "2021-06-08T00:50:15-03:00")
  protected FERecuperaLastCbteResponse feCompUltimoAutorizadoResult;

  /**
   * Obtiene el valor de la propiedad feCompUltimoAutorizadoResult.
   *
   * @return possible object is {@link FERecuperaLastCbteResponse }
   */
  @Generated(
      value = "com.sun.tools.xjc.Driver",
      comments = "JAXB RI v2.3.4",
      date = "2021-06-08T00:50:15-03:00")
  public FERecuperaLastCbteResponse getFECompUltimoAutorizadoResult() {
    return feCompUltimoAutorizadoResult;
  }

  /**
   * Define el valor de la propiedad feCompUltimoAutorizadoResult.
   *
   * @param value allowed object is {@link FERecuperaLastCbteResponse }
   */
  @Generated(
      value = "com.sun.tools.xjc.Driver",
      comments = "JAXB RI v2.3.4",
      date = "2021-06-08T00:50:15-03:00")
  public void setFECompUltimoAutorizadoResult(FERecuperaLastCbteResponse value) {
    this.feCompUltimoAutorizadoResult = value;
  }
}
