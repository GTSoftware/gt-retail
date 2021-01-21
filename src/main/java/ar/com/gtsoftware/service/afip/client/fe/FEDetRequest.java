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
 * Clase Java para FEDetRequest complex type.
 *
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta
 * clase.
 *
 * <pre>
 * &lt;complexType name="FEDetRequest">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Concepto" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="DocTipo" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="DocNro" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="CbteDesde" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="CbteHasta" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="CbteFch" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ImpTotal" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *         &lt;element name="ImpTotConc" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *         &lt;element name="ImpNeto" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *         &lt;element name="ImpOpEx" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *         &lt;element name="ImpTrib" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *         &lt;element name="ImpIVA" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *         &lt;element name="FchServDesde" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="FchServHasta" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="FchVtoPago" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="MonId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="MonCotiz" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *         &lt;element name="CbtesAsoc" type="{http://ar.gov.afip.dif.FEV1/}ArrayOfCbteAsoc" minOccurs="0"/>
 *         &lt;element name="Tributos" type="{http://ar.gov.afip.dif.FEV1/}ArrayOfTributo" minOccurs="0"/>
 *         &lt;element name="Iva" type="{http://ar.gov.afip.dif.FEV1/}ArrayOfAlicIva" minOccurs="0"/>
 *         &lt;element name="Opcionales" type="{http://ar.gov.afip.dif.FEV1/}ArrayOfOpcional" minOccurs="0"/>
 *         &lt;element name="Compradores" type="{http://ar.gov.afip.dif.FEV1/}ArrayOfComprador" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
    name = "FEDetRequest",
    propOrder = {
      "concepto",
      "docTipo",
      "docNro",
      "cbteDesde",
      "cbteHasta",
      "cbteFch",
      "impTotal",
      "impTotConc",
      "impNeto",
      "impOpEx",
      "impTrib",
      "impIVA",
      "fchServDesde",
      "fchServHasta",
      "fchVtoPago",
      "monId",
      "monCotiz",
      "cbtesAsoc",
      "tributos",
      "iva",
      "opcionales",
      "compradores"
    })
@XmlSeeAlso({FECAEADetRequest.class, FECAEDetRequest.class})
@Generated(
    value = "com.sun.tools.internal.xjc.Driver",
    date = "2020-05-28T09:17:55-03:00",
    comments = "JAXB RI v2.2.8-b130911.1802")
public class FEDetRequest {

  @XmlElement(name = "Concepto")
  @Generated(
      value = "com.sun.tools.internal.xjc.Driver",
      date = "2020-05-28T09:17:55-03:00",
      comments = "JAXB RI v2.2.8-b130911.1802")
  protected int concepto;

  @XmlElement(name = "DocTipo")
  @Generated(
      value = "com.sun.tools.internal.xjc.Driver",
      date = "2020-05-28T09:17:55-03:00",
      comments = "JAXB RI v2.2.8-b130911.1802")
  protected int docTipo;

  @XmlElement(name = "DocNro")
  @Generated(
      value = "com.sun.tools.internal.xjc.Driver",
      date = "2020-05-28T09:17:55-03:00",
      comments = "JAXB RI v2.2.8-b130911.1802")
  protected long docNro;

  @XmlElement(name = "CbteDesde")
  @Generated(
      value = "com.sun.tools.internal.xjc.Driver",
      date = "2020-05-28T09:17:55-03:00",
      comments = "JAXB RI v2.2.8-b130911.1802")
  protected long cbteDesde;

  @XmlElement(name = "CbteHasta")
  @Generated(
      value = "com.sun.tools.internal.xjc.Driver",
      date = "2020-05-28T09:17:55-03:00",
      comments = "JAXB RI v2.2.8-b130911.1802")
  protected long cbteHasta;

  @XmlElement(name = "CbteFch")
  @Generated(
      value = "com.sun.tools.internal.xjc.Driver",
      date = "2020-05-28T09:17:55-03:00",
      comments = "JAXB RI v2.2.8-b130911.1802")
  protected String cbteFch;

  @XmlElement(name = "ImpTotal")
  @Generated(
      value = "com.sun.tools.internal.xjc.Driver",
      date = "2020-05-28T09:17:55-03:00",
      comments = "JAXB RI v2.2.8-b130911.1802")
  protected double impTotal;

  @XmlElement(name = "ImpTotConc")
  @Generated(
      value = "com.sun.tools.internal.xjc.Driver",
      date = "2020-05-28T09:17:55-03:00",
      comments = "JAXB RI v2.2.8-b130911.1802")
  protected double impTotConc;

  @XmlElement(name = "ImpNeto")
  @Generated(
      value = "com.sun.tools.internal.xjc.Driver",
      date = "2020-05-28T09:17:55-03:00",
      comments = "JAXB RI v2.2.8-b130911.1802")
  protected double impNeto;

  @XmlElement(name = "ImpOpEx")
  @Generated(
      value = "com.sun.tools.internal.xjc.Driver",
      date = "2020-05-28T09:17:55-03:00",
      comments = "JAXB RI v2.2.8-b130911.1802")
  protected double impOpEx;

  @XmlElement(name = "ImpTrib")
  @Generated(
      value = "com.sun.tools.internal.xjc.Driver",
      date = "2020-05-28T09:17:55-03:00",
      comments = "JAXB RI v2.2.8-b130911.1802")
  protected double impTrib;

  @XmlElement(name = "ImpIVA")
  @Generated(
      value = "com.sun.tools.internal.xjc.Driver",
      date = "2020-05-28T09:17:55-03:00",
      comments = "JAXB RI v2.2.8-b130911.1802")
  protected double impIVA;

  @XmlElement(name = "FchServDesde")
  @Generated(
      value = "com.sun.tools.internal.xjc.Driver",
      date = "2020-05-28T09:17:55-03:00",
      comments = "JAXB RI v2.2.8-b130911.1802")
  protected String fchServDesde;

  @XmlElement(name = "FchServHasta")
  @Generated(
      value = "com.sun.tools.internal.xjc.Driver",
      date = "2020-05-28T09:17:55-03:00",
      comments = "JAXB RI v2.2.8-b130911.1802")
  protected String fchServHasta;

  @XmlElement(name = "FchVtoPago")
  @Generated(
      value = "com.sun.tools.internal.xjc.Driver",
      date = "2020-05-28T09:17:55-03:00",
      comments = "JAXB RI v2.2.8-b130911.1802")
  protected String fchVtoPago;

  @XmlElement(name = "MonId")
  @Generated(
      value = "com.sun.tools.internal.xjc.Driver",
      date = "2020-05-28T09:17:55-03:00",
      comments = "JAXB RI v2.2.8-b130911.1802")
  protected String monId;

  @XmlElement(name = "MonCotiz")
  @Generated(
      value = "com.sun.tools.internal.xjc.Driver",
      date = "2020-05-28T09:17:55-03:00",
      comments = "JAXB RI v2.2.8-b130911.1802")
  protected double monCotiz;

  @XmlElement(name = "CbtesAsoc")
  @Generated(
      value = "com.sun.tools.internal.xjc.Driver",
      date = "2020-05-28T09:17:55-03:00",
      comments = "JAXB RI v2.2.8-b130911.1802")
  protected ArrayOfCbteAsoc cbtesAsoc;

  @XmlElement(name = "Tributos")
  @Generated(
      value = "com.sun.tools.internal.xjc.Driver",
      date = "2020-05-28T09:17:55-03:00",
      comments = "JAXB RI v2.2.8-b130911.1802")
  protected ArrayOfTributo tributos;

  @XmlElement(name = "Iva")
  @Generated(
      value = "com.sun.tools.internal.xjc.Driver",
      date = "2020-05-28T09:17:55-03:00",
      comments = "JAXB RI v2.2.8-b130911.1802")
  protected ArrayOfAlicIva iva;

  @XmlElement(name = "Opcionales")
  @Generated(
      value = "com.sun.tools.internal.xjc.Driver",
      date = "2020-05-28T09:17:55-03:00",
      comments = "JAXB RI v2.2.8-b130911.1802")
  protected ArrayOfOpcional opcionales;

  @XmlElement(name = "Compradores")
  @Generated(
      value = "com.sun.tools.internal.xjc.Driver",
      date = "2020-05-28T09:17:55-03:00",
      comments = "JAXB RI v2.2.8-b130911.1802")
  protected ArrayOfComprador compradores;

  /** Obtiene el valor de la propiedad concepto. */
  @Generated(
      value = "com.sun.tools.internal.xjc.Driver",
      date = "2020-05-28T09:17:55-03:00",
      comments = "JAXB RI v2.2.8-b130911.1802")
  public int getConcepto() {
    return concepto;
  }

  /** Define el valor de la propiedad concepto. */
  @Generated(
      value = "com.sun.tools.internal.xjc.Driver",
      date = "2020-05-28T09:17:55-03:00",
      comments = "JAXB RI v2.2.8-b130911.1802")
  public void setConcepto(int value) {
    this.concepto = value;
  }

  /** Obtiene el valor de la propiedad docTipo. */
  @Generated(
      value = "com.sun.tools.internal.xjc.Driver",
      date = "2020-05-28T09:17:55-03:00",
      comments = "JAXB RI v2.2.8-b130911.1802")
  public int getDocTipo() {
    return docTipo;
  }

  /** Define el valor de la propiedad docTipo. */
  @Generated(
      value = "com.sun.tools.internal.xjc.Driver",
      date = "2020-05-28T09:17:55-03:00",
      comments = "JAXB RI v2.2.8-b130911.1802")
  public void setDocTipo(int value) {
    this.docTipo = value;
  }

  /** Obtiene el valor de la propiedad docNro. */
  @Generated(
      value = "com.sun.tools.internal.xjc.Driver",
      date = "2020-05-28T09:17:55-03:00",
      comments = "JAXB RI v2.2.8-b130911.1802")
  public long getDocNro() {
    return docNro;
  }

  /** Define el valor de la propiedad docNro. */
  @Generated(
      value = "com.sun.tools.internal.xjc.Driver",
      date = "2020-05-28T09:17:55-03:00",
      comments = "JAXB RI v2.2.8-b130911.1802")
  public void setDocNro(long value) {
    this.docNro = value;
  }

  /** Obtiene el valor de la propiedad cbteDesde. */
  @Generated(
      value = "com.sun.tools.internal.xjc.Driver",
      date = "2020-05-28T09:17:55-03:00",
      comments = "JAXB RI v2.2.8-b130911.1802")
  public long getCbteDesde() {
    return cbteDesde;
  }

  /** Define el valor de la propiedad cbteDesde. */
  @Generated(
      value = "com.sun.tools.internal.xjc.Driver",
      date = "2020-05-28T09:17:55-03:00",
      comments = "JAXB RI v2.2.8-b130911.1802")
  public void setCbteDesde(long value) {
    this.cbteDesde = value;
  }

  /** Obtiene el valor de la propiedad cbteHasta. */
  @Generated(
      value = "com.sun.tools.internal.xjc.Driver",
      date = "2020-05-28T09:17:55-03:00",
      comments = "JAXB RI v2.2.8-b130911.1802")
  public long getCbteHasta() {
    return cbteHasta;
  }

  /** Define el valor de la propiedad cbteHasta. */
  @Generated(
      value = "com.sun.tools.internal.xjc.Driver",
      date = "2020-05-28T09:17:55-03:00",
      comments = "JAXB RI v2.2.8-b130911.1802")
  public void setCbteHasta(long value) {
    this.cbteHasta = value;
  }

  /**
   * Obtiene el valor de la propiedad cbteFch.
   *
   * @return possible object is {@link String }
   */
  @Generated(
      value = "com.sun.tools.internal.xjc.Driver",
      date = "2020-05-28T09:17:55-03:00",
      comments = "JAXB RI v2.2.8-b130911.1802")
  public String getCbteFch() {
    return cbteFch;
  }

  /**
   * Define el valor de la propiedad cbteFch.
   *
   * @param value allowed object is {@link String }
   */
  @Generated(
      value = "com.sun.tools.internal.xjc.Driver",
      date = "2020-05-28T09:17:55-03:00",
      comments = "JAXB RI v2.2.8-b130911.1802")
  public void setCbteFch(String value) {
    this.cbteFch = value;
  }

  /** Obtiene el valor de la propiedad impTotal. */
  @Generated(
      value = "com.sun.tools.internal.xjc.Driver",
      date = "2020-05-28T09:17:55-03:00",
      comments = "JAXB RI v2.2.8-b130911.1802")
  public double getImpTotal() {
    return impTotal;
  }

  /** Define el valor de la propiedad impTotal. */
  @Generated(
      value = "com.sun.tools.internal.xjc.Driver",
      date = "2020-05-28T09:17:55-03:00",
      comments = "JAXB RI v2.2.8-b130911.1802")
  public void setImpTotal(double value) {
    this.impTotal = value;
  }

  /** Obtiene el valor de la propiedad impTotConc. */
  @Generated(
      value = "com.sun.tools.internal.xjc.Driver",
      date = "2020-05-28T09:17:55-03:00",
      comments = "JAXB RI v2.2.8-b130911.1802")
  public double getImpTotConc() {
    return impTotConc;
  }

  /** Define el valor de la propiedad impTotConc. */
  @Generated(
      value = "com.sun.tools.internal.xjc.Driver",
      date = "2020-05-28T09:17:55-03:00",
      comments = "JAXB RI v2.2.8-b130911.1802")
  public void setImpTotConc(double value) {
    this.impTotConc = value;
  }

  /** Obtiene el valor de la propiedad impNeto. */
  @Generated(
      value = "com.sun.tools.internal.xjc.Driver",
      date = "2020-05-28T09:17:55-03:00",
      comments = "JAXB RI v2.2.8-b130911.1802")
  public double getImpNeto() {
    return impNeto;
  }

  /** Define el valor de la propiedad impNeto. */
  @Generated(
      value = "com.sun.tools.internal.xjc.Driver",
      date = "2020-05-28T09:17:55-03:00",
      comments = "JAXB RI v2.2.8-b130911.1802")
  public void setImpNeto(double value) {
    this.impNeto = value;
  }

  /** Obtiene el valor de la propiedad impOpEx. */
  @Generated(
      value = "com.sun.tools.internal.xjc.Driver",
      date = "2020-05-28T09:17:55-03:00",
      comments = "JAXB RI v2.2.8-b130911.1802")
  public double getImpOpEx() {
    return impOpEx;
  }

  /** Define el valor de la propiedad impOpEx. */
  @Generated(
      value = "com.sun.tools.internal.xjc.Driver",
      date = "2020-05-28T09:17:55-03:00",
      comments = "JAXB RI v2.2.8-b130911.1802")
  public void setImpOpEx(double value) {
    this.impOpEx = value;
  }

  /** Obtiene el valor de la propiedad impTrib. */
  @Generated(
      value = "com.sun.tools.internal.xjc.Driver",
      date = "2020-05-28T09:17:55-03:00",
      comments = "JAXB RI v2.2.8-b130911.1802")
  public double getImpTrib() {
    return impTrib;
  }

  /** Define el valor de la propiedad impTrib. */
  @Generated(
      value = "com.sun.tools.internal.xjc.Driver",
      date = "2020-05-28T09:17:55-03:00",
      comments = "JAXB RI v2.2.8-b130911.1802")
  public void setImpTrib(double value) {
    this.impTrib = value;
  }

  /** Obtiene el valor de la propiedad impIVA. */
  @Generated(
      value = "com.sun.tools.internal.xjc.Driver",
      date = "2020-05-28T09:17:55-03:00",
      comments = "JAXB RI v2.2.8-b130911.1802")
  public double getImpIVA() {
    return impIVA;
  }

  /** Define el valor de la propiedad impIVA. */
  @Generated(
      value = "com.sun.tools.internal.xjc.Driver",
      date = "2020-05-28T09:17:55-03:00",
      comments = "JAXB RI v2.2.8-b130911.1802")
  public void setImpIVA(double value) {
    this.impIVA = value;
  }

  /**
   * Obtiene el valor de la propiedad fchServDesde.
   *
   * @return possible object is {@link String }
   */
  @Generated(
      value = "com.sun.tools.internal.xjc.Driver",
      date = "2020-05-28T09:17:55-03:00",
      comments = "JAXB RI v2.2.8-b130911.1802")
  public String getFchServDesde() {
    return fchServDesde;
  }

  /**
   * Define el valor de la propiedad fchServDesde.
   *
   * @param value allowed object is {@link String }
   */
  @Generated(
      value = "com.sun.tools.internal.xjc.Driver",
      date = "2020-05-28T09:17:55-03:00",
      comments = "JAXB RI v2.2.8-b130911.1802")
  public void setFchServDesde(String value) {
    this.fchServDesde = value;
  }

  /**
   * Obtiene el valor de la propiedad fchServHasta.
   *
   * @return possible object is {@link String }
   */
  @Generated(
      value = "com.sun.tools.internal.xjc.Driver",
      date = "2020-05-28T09:17:55-03:00",
      comments = "JAXB RI v2.2.8-b130911.1802")
  public String getFchServHasta() {
    return fchServHasta;
  }

  /**
   * Define el valor de la propiedad fchServHasta.
   *
   * @param value allowed object is {@link String }
   */
  @Generated(
      value = "com.sun.tools.internal.xjc.Driver",
      date = "2020-05-28T09:17:55-03:00",
      comments = "JAXB RI v2.2.8-b130911.1802")
  public void setFchServHasta(String value) {
    this.fchServHasta = value;
  }

  /**
   * Obtiene el valor de la propiedad fchVtoPago.
   *
   * @return possible object is {@link String }
   */
  @Generated(
      value = "com.sun.tools.internal.xjc.Driver",
      date = "2020-05-28T09:17:55-03:00",
      comments = "JAXB RI v2.2.8-b130911.1802")
  public String getFchVtoPago() {
    return fchVtoPago;
  }

  /**
   * Define el valor de la propiedad fchVtoPago.
   *
   * @param value allowed object is {@link String }
   */
  @Generated(
      value = "com.sun.tools.internal.xjc.Driver",
      date = "2020-05-28T09:17:55-03:00",
      comments = "JAXB RI v2.2.8-b130911.1802")
  public void setFchVtoPago(String value) {
    this.fchVtoPago = value;
  }

  /**
   * Obtiene el valor de la propiedad monId.
   *
   * @return possible object is {@link String }
   */
  @Generated(
      value = "com.sun.tools.internal.xjc.Driver",
      date = "2020-05-28T09:17:55-03:00",
      comments = "JAXB RI v2.2.8-b130911.1802")
  public String getMonId() {
    return monId;
  }

  /**
   * Define el valor de la propiedad monId.
   *
   * @param value allowed object is {@link String }
   */
  @Generated(
      value = "com.sun.tools.internal.xjc.Driver",
      date = "2020-05-28T09:17:55-03:00",
      comments = "JAXB RI v2.2.8-b130911.1802")
  public void setMonId(String value) {
    this.monId = value;
  }

  /** Obtiene el valor de la propiedad monCotiz. */
  @Generated(
      value = "com.sun.tools.internal.xjc.Driver",
      date = "2020-05-28T09:17:55-03:00",
      comments = "JAXB RI v2.2.8-b130911.1802")
  public double getMonCotiz() {
    return monCotiz;
  }

  /** Define el valor de la propiedad monCotiz. */
  @Generated(
      value = "com.sun.tools.internal.xjc.Driver",
      date = "2020-05-28T09:17:55-03:00",
      comments = "JAXB RI v2.2.8-b130911.1802")
  public void setMonCotiz(double value) {
    this.monCotiz = value;
  }

  /**
   * Obtiene el valor de la propiedad cbtesAsoc.
   *
   * @return possible object is {@link ArrayOfCbteAsoc }
   */
  @Generated(
      value = "com.sun.tools.internal.xjc.Driver",
      date = "2020-05-28T09:17:55-03:00",
      comments = "JAXB RI v2.2.8-b130911.1802")
  public ArrayOfCbteAsoc getCbtesAsoc() {
    return cbtesAsoc;
  }

  /**
   * Define el valor de la propiedad cbtesAsoc.
   *
   * @param value allowed object is {@link ArrayOfCbteAsoc }
   */
  @Generated(
      value = "com.sun.tools.internal.xjc.Driver",
      date = "2020-05-28T09:17:55-03:00",
      comments = "JAXB RI v2.2.8-b130911.1802")
  public void setCbtesAsoc(ArrayOfCbteAsoc value) {
    this.cbtesAsoc = value;
  }

  /**
   * Obtiene el valor de la propiedad tributos.
   *
   * @return possible object is {@link ArrayOfTributo }
   */
  @Generated(
      value = "com.sun.tools.internal.xjc.Driver",
      date = "2020-05-28T09:17:55-03:00",
      comments = "JAXB RI v2.2.8-b130911.1802")
  public ArrayOfTributo getTributos() {
    return tributos;
  }

  /**
   * Define el valor de la propiedad tributos.
   *
   * @param value allowed object is {@link ArrayOfTributo }
   */
  @Generated(
      value = "com.sun.tools.internal.xjc.Driver",
      date = "2020-05-28T09:17:55-03:00",
      comments = "JAXB RI v2.2.8-b130911.1802")
  public void setTributos(ArrayOfTributo value) {
    this.tributos = value;
  }

  /**
   * Obtiene el valor de la propiedad iva.
   *
   * @return possible object is {@link ArrayOfAlicIva }
   */
  @Generated(
      value = "com.sun.tools.internal.xjc.Driver",
      date = "2020-05-28T09:17:55-03:00",
      comments = "JAXB RI v2.2.8-b130911.1802")
  public ArrayOfAlicIva getIva() {
    return iva;
  }

  /**
   * Define el valor de la propiedad iva.
   *
   * @param value allowed object is {@link ArrayOfAlicIva }
   */
  @Generated(
      value = "com.sun.tools.internal.xjc.Driver",
      date = "2020-05-28T09:17:55-03:00",
      comments = "JAXB RI v2.2.8-b130911.1802")
  public void setIva(ArrayOfAlicIva value) {
    this.iva = value;
  }

  /**
   * Obtiene el valor de la propiedad opcionales.
   *
   * @return possible object is {@link ArrayOfOpcional }
   */
  @Generated(
      value = "com.sun.tools.internal.xjc.Driver",
      date = "2020-05-28T09:17:55-03:00",
      comments = "JAXB RI v2.2.8-b130911.1802")
  public ArrayOfOpcional getOpcionales() {
    return opcionales;
  }

  /**
   * Define el valor de la propiedad opcionales.
   *
   * @param value allowed object is {@link ArrayOfOpcional }
   */
  @Generated(
      value = "com.sun.tools.internal.xjc.Driver",
      date = "2020-05-28T09:17:55-03:00",
      comments = "JAXB RI v2.2.8-b130911.1802")
  public void setOpcionales(ArrayOfOpcional value) {
    this.opcionales = value;
  }

  /**
   * Obtiene el valor de la propiedad compradores.
   *
   * @return possible object is {@link ArrayOfComprador }
   */
  @Generated(
      value = "com.sun.tools.internal.xjc.Driver",
      date = "2020-05-28T09:17:55-03:00",
      comments = "JAXB RI v2.2.8-b130911.1802")
  public ArrayOfComprador getCompradores() {
    return compradores;
  }

  /**
   * Define el valor de la propiedad compradores.
   *
   * @param value allowed object is {@link ArrayOfComprador }
   */
  @Generated(
      value = "com.sun.tools.internal.xjc.Driver",
      date = "2020-05-28T09:17:55-03:00",
      comments = "JAXB RI v2.2.8-b130911.1802")
  public void setCompradores(ArrayOfComprador value) {
    this.compradores = value;
  }
}
