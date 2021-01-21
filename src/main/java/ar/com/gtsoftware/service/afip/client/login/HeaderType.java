//
// Este archivo ha sido generado por la arquitectura JavaTM para la implantación de la referencia de
// enlace (JAXB) XML v2.2.8-b130911.1802
// Visite <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a>
// Todas las modificaciones realizadas en este archivo se perderán si se vuelve a compilar el
// esquema de origen.
// Generado el: 2020.05.28 a las 09:17:46 PM ART
//

package ar.com.gtsoftware.service.afip.client.login;

import javax.annotation.Generated;
import javax.xml.bind.annotation.*;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 * Clase Java para headerType complex type.
 *
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta
 * clase.
 *
 * <pre>
 * &lt;complexType name="headerType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="source" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="destination" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="uniqueId" type="{http://www.w3.org/2001/XMLSchema}unsignedInt"/>
 *         &lt;element name="generationTime" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="expirationTime" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
    name = "headerType",
    propOrder = {"source", "destination", "uniqueId", "generationTime", "expirationTime"})
@Generated(
    value = "com.sun.tools.internal.xjc.Driver",
    date = "2020-05-28T09:17:46-03:00",
    comments = "JAXB RI v2.2.8-b130911.1802")
public class HeaderType {

  @XmlElement(required = true)
  @Generated(
      value = "com.sun.tools.internal.xjc.Driver",
      date = "2020-05-28T09:17:46-03:00",
      comments = "JAXB RI v2.2.8-b130911.1802")
  protected String source;

  @XmlElement(required = true)
  @Generated(
      value = "com.sun.tools.internal.xjc.Driver",
      date = "2020-05-28T09:17:46-03:00",
      comments = "JAXB RI v2.2.8-b130911.1802")
  protected String destination;

  @XmlSchemaType(name = "unsignedInt")
  @Generated(
      value = "com.sun.tools.internal.xjc.Driver",
      date = "2020-05-28T09:17:46-03:00",
      comments = "JAXB RI v2.2.8-b130911.1802")
  protected long uniqueId;

  @XmlElement(required = true)
  @XmlSchemaType(name = "dateTime")
  @Generated(
      value = "com.sun.tools.internal.xjc.Driver",
      date = "2020-05-28T09:17:46-03:00",
      comments = "JAXB RI v2.2.8-b130911.1802")
  protected XMLGregorianCalendar generationTime;

  @XmlElement(required = true)
  @XmlSchemaType(name = "dateTime")
  @Generated(
      value = "com.sun.tools.internal.xjc.Driver",
      date = "2020-05-28T09:17:46-03:00",
      comments = "JAXB RI v2.2.8-b130911.1802")
  protected XMLGregorianCalendar expirationTime;

  /**
   * Obtiene el valor de la propiedad source.
   *
   * @return possible object is {@link String }
   */
  @Generated(
      value = "com.sun.tools.internal.xjc.Driver",
      date = "2020-05-28T09:17:46-03:00",
      comments = "JAXB RI v2.2.8-b130911.1802")
  public String getSource() {
    return source;
  }

  /**
   * Define el valor de la propiedad source.
   *
   * @param value allowed object is {@link String }
   */
  @Generated(
      value = "com.sun.tools.internal.xjc.Driver",
      date = "2020-05-28T09:17:46-03:00",
      comments = "JAXB RI v2.2.8-b130911.1802")
  public void setSource(String value) {
    this.source = value;
  }

  /**
   * Obtiene el valor de la propiedad destination.
   *
   * @return possible object is {@link String }
   */
  @Generated(
      value = "com.sun.tools.internal.xjc.Driver",
      date = "2020-05-28T09:17:46-03:00",
      comments = "JAXB RI v2.2.8-b130911.1802")
  public String getDestination() {
    return destination;
  }

  /**
   * Define el valor de la propiedad destination.
   *
   * @param value allowed object is {@link String }
   */
  @Generated(
      value = "com.sun.tools.internal.xjc.Driver",
      date = "2020-05-28T09:17:46-03:00",
      comments = "JAXB RI v2.2.8-b130911.1802")
  public void setDestination(String value) {
    this.destination = value;
  }

  /** Obtiene el valor de la propiedad uniqueId. */
  @Generated(
      value = "com.sun.tools.internal.xjc.Driver",
      date = "2020-05-28T09:17:46-03:00",
      comments = "JAXB RI v2.2.8-b130911.1802")
  public long getUniqueId() {
    return uniqueId;
  }

  /** Define el valor de la propiedad uniqueId. */
  @Generated(
      value = "com.sun.tools.internal.xjc.Driver",
      date = "2020-05-28T09:17:46-03:00",
      comments = "JAXB RI v2.2.8-b130911.1802")
  public void setUniqueId(long value) {
    this.uniqueId = value;
  }

  /**
   * Obtiene el valor de la propiedad generationTime.
   *
   * @return possible object is {@link XMLGregorianCalendar }
   */
  @Generated(
      value = "com.sun.tools.internal.xjc.Driver",
      date = "2020-05-28T09:17:46-03:00",
      comments = "JAXB RI v2.2.8-b130911.1802")
  public XMLGregorianCalendar getGenerationTime() {
    return generationTime;
  }

  /**
   * Define el valor de la propiedad generationTime.
   *
   * @param value allowed object is {@link XMLGregorianCalendar }
   */
  @Generated(
      value = "com.sun.tools.internal.xjc.Driver",
      date = "2020-05-28T09:17:46-03:00",
      comments = "JAXB RI v2.2.8-b130911.1802")
  public void setGenerationTime(XMLGregorianCalendar value) {
    this.generationTime = value;
  }

  /**
   * Obtiene el valor de la propiedad expirationTime.
   *
   * @return possible object is {@link XMLGregorianCalendar }
   */
  @Generated(
      value = "com.sun.tools.internal.xjc.Driver",
      date = "2020-05-28T09:17:46-03:00",
      comments = "JAXB RI v2.2.8-b130911.1802")
  public XMLGregorianCalendar getExpirationTime() {
    return expirationTime;
  }

  /**
   * Define el valor de la propiedad expirationTime.
   *
   * @param value allowed object is {@link XMLGregorianCalendar }
   */
  @Generated(
      value = "com.sun.tools.internal.xjc.Driver",
      date = "2020-05-28T09:17:46-03:00",
      comments = "JAXB RI v2.2.8-b130911.1802")
  public void setExpirationTime(XMLGregorianCalendar value) {
    this.expirationTime = value;
  }
}
