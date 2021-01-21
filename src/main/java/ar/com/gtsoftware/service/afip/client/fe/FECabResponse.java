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
 * Clase Java para FECabResponse complex type.
 *
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta
 * clase.
 *
 * <pre>
 * &lt;complexType name="FECabResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Cuit" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="PtoVta" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="CbteTipo" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="FchProceso" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="CantReg" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="Resultado" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Reproceso" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
    name = "FECabResponse",
    propOrder = {"cuit", "ptoVta", "cbteTipo", "fchProceso", "cantReg", "resultado", "reproceso"})
@XmlSeeAlso({FECAECabResponse.class, FECAEACabResponse.class})
@Generated(
    value = "com.sun.tools.internal.xjc.Driver",
    date = "2020-05-28T09:17:55-03:00",
    comments = "JAXB RI v2.2.8-b130911.1802")
public class FECabResponse {

  @XmlElement(name = "Cuit")
  @Generated(
      value = "com.sun.tools.internal.xjc.Driver",
      date = "2020-05-28T09:17:55-03:00",
      comments = "JAXB RI v2.2.8-b130911.1802")
  protected long cuit;

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

  @XmlElement(name = "FchProceso")
  @Generated(
      value = "com.sun.tools.internal.xjc.Driver",
      date = "2020-05-28T09:17:55-03:00",
      comments = "JAXB RI v2.2.8-b130911.1802")
  protected String fchProceso;

  @XmlElement(name = "CantReg")
  @Generated(
      value = "com.sun.tools.internal.xjc.Driver",
      date = "2020-05-28T09:17:55-03:00",
      comments = "JAXB RI v2.2.8-b130911.1802")
  protected int cantReg;

  @XmlElement(name = "Resultado")
  @Generated(
      value = "com.sun.tools.internal.xjc.Driver",
      date = "2020-05-28T09:17:55-03:00",
      comments = "JAXB RI v2.2.8-b130911.1802")
  protected String resultado;

  @XmlElement(name = "Reproceso")
  @Generated(
      value = "com.sun.tools.internal.xjc.Driver",
      date = "2020-05-28T09:17:55-03:00",
      comments = "JAXB RI v2.2.8-b130911.1802")
  protected String reproceso;

  /** Obtiene el valor de la propiedad cuit. */
  @Generated(
      value = "com.sun.tools.internal.xjc.Driver",
      date = "2020-05-28T09:17:55-03:00",
      comments = "JAXB RI v2.2.8-b130911.1802")
  public long getCuit() {
    return cuit;
  }

  /** Define el valor de la propiedad cuit. */
  @Generated(
      value = "com.sun.tools.internal.xjc.Driver",
      date = "2020-05-28T09:17:55-03:00",
      comments = "JAXB RI v2.2.8-b130911.1802")
  public void setCuit(long value) {
    this.cuit = value;
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

  /** Obtiene el valor de la propiedad cantReg. */
  @Generated(
      value = "com.sun.tools.internal.xjc.Driver",
      date = "2020-05-28T09:17:55-03:00",
      comments = "JAXB RI v2.2.8-b130911.1802")
  public int getCantReg() {
    return cantReg;
  }

  /** Define el valor de la propiedad cantReg. */
  @Generated(
      value = "com.sun.tools.internal.xjc.Driver",
      date = "2020-05-28T09:17:55-03:00",
      comments = "JAXB RI v2.2.8-b130911.1802")
  public void setCantReg(int value) {
    this.cantReg = value;
  }

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
   * Obtiene el valor de la propiedad reproceso.
   *
   * @return possible object is {@link String }
   */
  @Generated(
      value = "com.sun.tools.internal.xjc.Driver",
      date = "2020-05-28T09:17:55-03:00",
      comments = "JAXB RI v2.2.8-b130911.1802")
  public String getReproceso() {
    return reproceso;
  }

  /**
   * Define el valor de la propiedad reproceso.
   *
   * @param value allowed object is {@link String }
   */
  @Generated(
      value = "com.sun.tools.internal.xjc.Driver",
      date = "2020-05-28T09:17:55-03:00",
      comments = "JAXB RI v2.2.8-b130911.1802")
  public void setReproceso(String value) {
    this.reproceso = value;
  }
}
