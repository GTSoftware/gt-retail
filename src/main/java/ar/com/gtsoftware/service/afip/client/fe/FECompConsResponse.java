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
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Clase Java para FECompConsResponse complex type.
 *
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta
 * clase.
 *
 * <pre>
 * &lt;complexType name="FECompConsResponse">
 *   &lt;complexContent>
 *     &lt;extension base="{http://ar.gov.afip.dif.FEV1/}FECAEDetRequest">
 *       &lt;sequence>
 *         &lt;element name="Resultado" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="CodAutorizacion" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="EmisionTipo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="FchVto" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="FchProceso" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Observaciones" type="{http://ar.gov.afip.dif.FEV1/}ArrayOfObs" minOccurs="0"/>
 *         &lt;element name="PtoVta" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="CbteTipo" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
    name = "FECompConsResponse",
    propOrder = {
      "resultado",
      "codAutorizacion",
      "emisionTipo",
      "fchVto",
      "fchProceso",
      "observaciones",
      "ptoVta",
      "cbteTipo"
    })
@Generated(
    value = "com.sun.tools.internal.xjc.Driver",
    date = "2020-05-28T09:17:55-03:00",
    comments = "JAXB RI v2.2.8-b130911.1802")
public class FECompConsResponse extends FECAEDetRequest {

  @XmlElement(name = "Resultado")
  @Generated(
      value = "com.sun.tools.internal.xjc.Driver",
      date = "2020-05-28T09:17:55-03:00",
      comments = "JAXB RI v2.2.8-b130911.1802")
  protected String resultado;

  @XmlElement(name = "CodAutorizacion")
  @Generated(
      value = "com.sun.tools.internal.xjc.Driver",
      date = "2020-05-28T09:17:55-03:00",
      comments = "JAXB RI v2.2.8-b130911.1802")
  protected String codAutorizacion;

  @XmlElement(name = "EmisionTipo")
  @Generated(
      value = "com.sun.tools.internal.xjc.Driver",
      date = "2020-05-28T09:17:55-03:00",
      comments = "JAXB RI v2.2.8-b130911.1802")
  protected String emisionTipo;

  @XmlElement(name = "FchVto")
  @Generated(
      value = "com.sun.tools.internal.xjc.Driver",
      date = "2020-05-28T09:17:55-03:00",
      comments = "JAXB RI v2.2.8-b130911.1802")
  protected String fchVto;

  @XmlElement(name = "FchProceso")
  @Generated(
      value = "com.sun.tools.internal.xjc.Driver",
      date = "2020-05-28T09:17:55-03:00",
      comments = "JAXB RI v2.2.8-b130911.1802")
  protected String fchProceso;

  @XmlElement(name = "Observaciones")
  @Generated(
      value = "com.sun.tools.internal.xjc.Driver",
      date = "2020-05-28T09:17:55-03:00",
      comments = "JAXB RI v2.2.8-b130911.1802")
  protected ArrayOfObs observaciones;

  @XmlElement(name = "PtoVta")
  @Generated(
      value = "com.sun.tools.internal.xjc.Driver",
      date = "2020-05-28T09:17:55-03:00",
      comments = "JAXB RI v2.2.8-b130911.1802")
  protected int ptoVta;

  @XmlElement(name = "CbteTipo")
  @Generated(
      value = "com.sun.tools.internal.xjc.Driver",
      date = "2020-05-28T09:17:55-03:00",
      comments = "JAXB RI v2.2.8-b130911.1802")
  protected int cbteTipo;

  /**
   * Obtiene el valor de la propiedad resultado.
   *
   * @return possible object is {@link String }
   */
  @Generated(
      value = "com.sun.tools.internal.xjc.Driver",
      date = "2020-05-28T09:17:55-03:00",
      comments = "JAXB RI v2.2.8-b130911.1802")
  public String getResultado() {
    return resultado;
  }

  /**
   * Define el valor de la propiedad resultado.
   *
   * @param value allowed object is {@link String }
   */
  @Generated(
      value = "com.sun.tools.internal.xjc.Driver",
      date = "2020-05-28T09:17:55-03:00",
      comments = "JAXB RI v2.2.8-b130911.1802")
  public void setResultado(String value) {
    this.resultado = value;
  }

  /**
   * Obtiene el valor de la propiedad codAutorizacion.
   *
   * @return possible object is {@link String }
   */
  @Generated(
      value = "com.sun.tools.internal.xjc.Driver",
      date = "2020-05-28T09:17:55-03:00",
      comments = "JAXB RI v2.2.8-b130911.1802")
  public String getCodAutorizacion() {
    return codAutorizacion;
  }

  /**
   * Define el valor de la propiedad codAutorizacion.
   *
   * @param value allowed object is {@link String }
   */
  @Generated(
      value = "com.sun.tools.internal.xjc.Driver",
      date = "2020-05-28T09:17:55-03:00",
      comments = "JAXB RI v2.2.8-b130911.1802")
  public void setCodAutorizacion(String value) {
    this.codAutorizacion = value;
  }

  /**
   * Obtiene el valor de la propiedad emisionTipo.
   *
   * @return possible object is {@link String }
   */
  @Generated(
      value = "com.sun.tools.internal.xjc.Driver",
      date = "2020-05-28T09:17:55-03:00",
      comments = "JAXB RI v2.2.8-b130911.1802")
  public String getEmisionTipo() {
    return emisionTipo;
  }

  /**
   * Define el valor de la propiedad emisionTipo.
   *
   * @param value allowed object is {@link String }
   */
  @Generated(
      value = "com.sun.tools.internal.xjc.Driver",
      date = "2020-05-28T09:17:55-03:00",
      comments = "JAXB RI v2.2.8-b130911.1802")
  public void setEmisionTipo(String value) {
    this.emisionTipo = value;
  }

  /**
   * Obtiene el valor de la propiedad fchVto.
   *
   * @return possible object is {@link String }
   */
  @Generated(
      value = "com.sun.tools.internal.xjc.Driver",
      date = "2020-05-28T09:17:55-03:00",
      comments = "JAXB RI v2.2.8-b130911.1802")
  public String getFchVto() {
    return fchVto;
  }

  /**
   * Define el valor de la propiedad fchVto.
   *
   * @param value allowed object is {@link String }
   */
  @Generated(
      value = "com.sun.tools.internal.xjc.Driver",
      date = "2020-05-28T09:17:55-03:00",
      comments = "JAXB RI v2.2.8-b130911.1802")
  public void setFchVto(String value) {
    this.fchVto = value;
  }

  /**
   * Obtiene el valor de la propiedad fchProceso.
   *
   * @return possible object is {@link String }
   */
  @Generated(
      value = "com.sun.tools.internal.xjc.Driver",
      date = "2020-05-28T09:17:55-03:00",
      comments = "JAXB RI v2.2.8-b130911.1802")
  public String getFchProceso() {
    return fchProceso;
  }

  /**
   * Define el valor de la propiedad fchProceso.
   *
   * @param value allowed object is {@link String }
   */
  @Generated(
      value = "com.sun.tools.internal.xjc.Driver",
      date = "2020-05-28T09:17:55-03:00",
      comments = "JAXB RI v2.2.8-b130911.1802")
  public void setFchProceso(String value) {
    this.fchProceso = value;
  }

  /**
   * Obtiene el valor de la propiedad observaciones.
   *
   * @return possible object is {@link ArrayOfObs }
   */
  @Generated(
      value = "com.sun.tools.internal.xjc.Driver",
      date = "2020-05-28T09:17:55-03:00",
      comments = "JAXB RI v2.2.8-b130911.1802")
  public ArrayOfObs getObservaciones() {
    return observaciones;
  }

  /**
   * Define el valor de la propiedad observaciones.
   *
   * @param value allowed object is {@link ArrayOfObs }
   */
  @Generated(
      value = "com.sun.tools.internal.xjc.Driver",
      date = "2020-05-28T09:17:55-03:00",
      comments = "JAXB RI v2.2.8-b130911.1802")
  public void setObservaciones(ArrayOfObs value) {
    this.observaciones = value;
  }

  /** Obtiene el valor de la propiedad ptoVta. */
  @Generated(
      value = "com.sun.tools.internal.xjc.Driver",
      date = "2020-05-28T09:17:55-03:00",
      comments = "JAXB RI v2.2.8-b130911.1802")
  public int getPtoVta() {
    return ptoVta;
  }

  /** Define el valor de la propiedad ptoVta. */
  @Generated(
      value = "com.sun.tools.internal.xjc.Driver",
      date = "2020-05-28T09:17:55-03:00",
      comments = "JAXB RI v2.2.8-b130911.1802")
  public void setPtoVta(int value) {
    this.ptoVta = value;
  }

  /** Obtiene el valor de la propiedad cbteTipo. */
  @Generated(
      value = "com.sun.tools.internal.xjc.Driver",
      date = "2020-05-28T09:17:55-03:00",
      comments = "JAXB RI v2.2.8-b130911.1802")
  public int getCbteTipo() {
    return cbteTipo;
  }

  /** Define el valor de la propiedad cbteTipo. */
  @Generated(
      value = "com.sun.tools.internal.xjc.Driver",
      date = "2020-05-28T09:17:55-03:00",
      comments = "JAXB RI v2.2.8-b130911.1802")
  public void setCbteTipo(int value) {
    this.cbteTipo = value;
  }
}
