//
// Este archivo ha sido generado por la arquitectura JavaTM para la implantación de la referencia de
// enlace (JAXB) XML v2.2.8-b130911.1802
// Visite <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a>
// Todas las modificaciones realizadas en este archivo se perderán si se vuelve a compilar el
// esquema de origen.
// Generado el: 2020.05.28 a las 09:17:55 PM ART
//

package ar.com.gtsoftware.service.afip.client.fe;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Clase Java para ArrayOfFECAEADetResponse complex type.
 *
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta
 * clase.
 *
 * <pre>
 * &lt;complexType name="ArrayOfFECAEADetResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="FECAEADetResponse" type="{http://ar.gov.afip.dif.FEV1/}FECAEADetResponse" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
    name = "ArrayOfFECAEADetResponse",
    propOrder = {"fecaeaDetResponse"})
@Generated(
    value = "com.sun.tools.internal.xjc.Driver",
    date = "2020-05-28T09:17:55-03:00",
    comments = "JAXB RI v2.2.8-b130911.1802")
public class ArrayOfFECAEADetResponse {

  @XmlElement(name = "FECAEADetResponse", nillable = true)
  @Generated(
      value = "com.sun.tools.internal.xjc.Driver",
      date = "2020-05-28T09:17:55-03:00",
      comments = "JAXB RI v2.2.8-b130911.1802")
  protected List<FECAEADetResponse> fecaeaDetResponse;

  /**
   * Gets the value of the fecaeaDetResponse property.
   *
   * <p>This accessor method returns a reference to the live list, not a snapshot. Therefore any
   * modification you make to the returned list will be present inside the JAXB object. This is why
   * there is not a <CODE>set</CODE> method for the fecaeaDetResponse property.
   *
   * <p>For example, to add a new item, do as follows:
   *
   * <pre>
   *    getFECAEADetResponse().add(newItem);
   * </pre>
   *
   * <p>Objects of the following type(s) are allowed in the list {@link FECAEADetResponse }
   */
  @Generated(
      value = "com.sun.tools.internal.xjc.Driver",
      date = "2020-05-28T09:17:55-03:00",
      comments = "JAXB RI v2.2.8-b130911.1802")
  public List<FECAEADetResponse> getFECAEADetResponse() {
    if (fecaeaDetResponse == null) {
      fecaeaDetResponse = new ArrayList<FECAEADetResponse>();
    }
    return this.fecaeaDetResponse;
  }
}
