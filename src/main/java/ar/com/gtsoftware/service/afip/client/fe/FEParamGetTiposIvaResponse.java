//
// Este archivo ha sido generado por la arquitectura JavaTM para la implantación de la referencia de
// enlace (JAXB) XML v2.2.8-b130911.1802
// Visite <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a>
// Todas las modificaciones realizadas en este archivo se perderán si se vuelve a compilar el
// esquema de origen.
// Generado el: 2020.05.28 a las 09:17:55 PM ART
//

package ar.com.gtsoftware.service.afip.client.fe;

import javax.annotation.Generated;
import javax.xml.bind.annotation.*;

/**
 * Clase Java para anonymous complex type.
 *
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta
 * clase.
 *
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="FEParamGetTiposIvaResult" type="{http://ar.gov.afip.dif.FEV1/}IvaTipoResponse" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
    name = "",
    propOrder = {"feParamGetTiposIvaResult"})
@XmlRootElement(name = "FEParamGetTiposIvaResponse")
@Generated(
    value = "com.sun.tools.internal.xjc.Driver",
    date = "2020-05-28T09:17:55-03:00",
    comments = "JAXB RI v2.2.8-b130911.1802")
public class FEParamGetTiposIvaResponse {

  @XmlElement(name = "FEParamGetTiposIvaResult")
  @Generated(
      value = "com.sun.tools.internal.xjc.Driver",
      date = "2020-05-28T09:17:55-03:00",
      comments = "JAXB RI v2.2.8-b130911.1802")
  protected IvaTipoResponse feParamGetTiposIvaResult;

  /**
   * Obtiene el valor de la propiedad feParamGetTiposIvaResult.
   *
   * @return possible object is {@link IvaTipoResponse }
   */
  @Generated(
      value = "com.sun.tools.internal.xjc.Driver",
      date = "2020-05-28T09:17:55-03:00",
      comments = "JAXB RI v2.2.8-b130911.1802")
  public IvaTipoResponse getFEParamGetTiposIvaResult() {
    return feParamGetTiposIvaResult;
  }

  /**
   * Define el valor de la propiedad feParamGetTiposIvaResult.
   *
   * @param value allowed object is {@link IvaTipoResponse }
   */
  @Generated(
      value = "com.sun.tools.internal.xjc.Driver",
      date = "2020-05-28T09:17:55-03:00",
      comments = "JAXB RI v2.2.8-b130911.1802")
  public void setFEParamGetTiposIvaResult(IvaTipoResponse value) {
    this.feParamGetTiposIvaResult = value;
  }
}
