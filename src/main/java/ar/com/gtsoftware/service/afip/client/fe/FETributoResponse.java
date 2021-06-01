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
 * Clase Java para FETributoResponse complex type.
 *
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta
 * clase.
 *
 * <pre>
 * &lt;complexType name="FETributoResponse"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="ResultGet" type="{http://ar.gov.afip.dif.FEV1/}ArrayOfTributoTipo" minOccurs="0"/&gt;
 *         &lt;element name="Errors" type="{http://ar.gov.afip.dif.FEV1/}ArrayOfErr" minOccurs="0"/&gt;
 *         &lt;element name="Events" type="{http://ar.gov.afip.dif.FEV1/}ArrayOfEvt" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
    name = "FETributoResponse",
    propOrder = {"resultGet", "errors", "events"})
@Generated(
    value = "com.sun.tools.xjc.Driver",
    comments = "JAXB RI v2.3.4",
    date = "2021-04-20T01:12:11-03:00")
public class FETributoResponse {

  @XmlElement(name = "ResultGet")
  @Generated(
      value = "com.sun.tools.xjc.Driver",
      comments = "JAXB RI v2.3.4",
      date = "2021-04-20T01:12:11-03:00")
  protected ArrayOfTributoTipo resultGet;

  @XmlElement(name = "Errors")
  @Generated(
      value = "com.sun.tools.xjc.Driver",
      comments = "JAXB RI v2.3.4",
      date = "2021-04-20T01:12:11-03:00")
  protected ArrayOfErr errors;

  @XmlElement(name = "Events")
  @Generated(
      value = "com.sun.tools.xjc.Driver",
      comments = "JAXB RI v2.3.4",
      date = "2021-04-20T01:12:11-03:00")
  protected ArrayOfEvt events;

  /**
   * Obtiene el valor de la propiedad resultGet.
   *
   * @return possible object is {@link ArrayOfTributoTipo }
   */
  @Generated(
      value = "com.sun.tools.xjc.Driver",
      comments = "JAXB RI v2.3.4",
      date = "2021-04-20T01:12:11-03:00")
  public ArrayOfTributoTipo getResultGet() {
    return resultGet;
  }

  /**
   * Define el valor de la propiedad resultGet.
   *
   * @param value allowed object is {@link ArrayOfTributoTipo }
   */
  @Generated(
      value = "com.sun.tools.xjc.Driver",
      comments = "JAXB RI v2.3.4",
      date = "2021-04-20T01:12:11-03:00")
  public void setResultGet(ArrayOfTributoTipo value) {
    this.resultGet = value;
  }

  /**
   * Obtiene el valor de la propiedad errors.
   *
   * @return possible object is {@link ArrayOfErr }
   */
  @Generated(
      value = "com.sun.tools.xjc.Driver",
      comments = "JAXB RI v2.3.4",
      date = "2021-04-20T01:12:11-03:00")
  public ArrayOfErr getErrors() {
    return errors;
  }

  /**
   * Define el valor de la propiedad errors.
   *
   * @param value allowed object is {@link ArrayOfErr }
   */
  @Generated(
      value = "com.sun.tools.xjc.Driver",
      comments = "JAXB RI v2.3.4",
      date = "2021-04-20T01:12:11-03:00")
  public void setErrors(ArrayOfErr value) {
    this.errors = value;
  }

  /**
   * Obtiene el valor de la propiedad events.
   *
   * @return possible object is {@link ArrayOfEvt }
   */
  @Generated(
      value = "com.sun.tools.xjc.Driver",
      comments = "JAXB RI v2.3.4",
      date = "2021-04-20T01:12:11-03:00")
  public ArrayOfEvt getEvents() {
    return events;
  }

  /**
   * Define el valor de la propiedad events.
   *
   * @param value allowed object is {@link ArrayOfEvt }
   */
  @Generated(
      value = "com.sun.tools.xjc.Driver",
      comments = "JAXB RI v2.3.4",
      date = "2021-04-20T01:12:11-03:00")
  public void setEvents(ArrayOfEvt value) {
    this.events = value;
  }
}
