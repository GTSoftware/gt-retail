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
 *         &lt;element name="FEParamGetTiposPaisesResult" type="{http://ar.gov.afip.dif.FEV1/}FEPaisResponse" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
    name = "",
    propOrder = {"feParamGetTiposPaisesResult"})
@XmlRootElement(name = "FEParamGetTiposPaisesResponse")
@Generated(
    value = "com.sun.tools.xjc.Driver",
    comments = "JAXB RI v2.3.4",
    date = "2021-04-20T01:12:11-03:00")
public class FEParamGetTiposPaisesResponse {

  @XmlElement(name = "FEParamGetTiposPaisesResult")
  @Generated(
      value = "com.sun.tools.xjc.Driver",
      comments = "JAXB RI v2.3.4",
      date = "2021-04-20T01:12:11-03:00")
  protected FEPaisResponse feParamGetTiposPaisesResult;

  /**
   * Obtiene el valor de la propiedad feParamGetTiposPaisesResult.
   *
   * @return possible object is {@link FEPaisResponse }
   */
  @Generated(
      value = "com.sun.tools.xjc.Driver",
      comments = "JAXB RI v2.3.4",
      date = "2021-04-20T01:12:11-03:00")
  public FEPaisResponse getFEParamGetTiposPaisesResult() {
    return feParamGetTiposPaisesResult;
  }

  /**
   * Define el valor de la propiedad feParamGetTiposPaisesResult.
   *
   * @param value allowed object is {@link FEPaisResponse }
   */
  @Generated(
      value = "com.sun.tools.xjc.Driver",
      comments = "JAXB RI v2.3.4",
      date = "2021-04-20T01:12:11-03:00")
  public void setFEParamGetTiposPaisesResult(FEPaisResponse value) {
    this.feParamGetTiposPaisesResult = value;
  }
}
