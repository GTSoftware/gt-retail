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
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;

/**
 * Clase Java para FEDetRequest complex type.
 *
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta
 * clase.
 *
 * <pre>
 * &lt;complexType name="FEDetRequest"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="Concepto" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="DocTipo" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="DocNro" type="{http://www.w3.org/2001/XMLSchema}long"/&gt;
 *         &lt;element name="CbteDesde" type="{http://www.w3.org/2001/XMLSchema}long"/&gt;
 *         &lt;element name="CbteHasta" type="{http://www.w3.org/2001/XMLSchema}long"/&gt;
 *         &lt;element name="CbteFch" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="ImpTotal" type="{http://www.w3.org/2001/XMLSchema}double"/&gt;
 *         &lt;element name="ImpTotConc" type="{http://www.w3.org/2001/XMLSchema}double"/&gt;
 *         &lt;element name="ImpNeto" type="{http://www.w3.org/2001/XMLSchema}double"/&gt;
 *         &lt;element name="ImpOpEx" type="{http://www.w3.org/2001/XMLSchema}double"/&gt;
 *         &lt;element name="ImpTrib" type="{http://www.w3.org/2001/XMLSchema}double"/&gt;
 *         &lt;element name="ImpIVA" type="{http://www.w3.org/2001/XMLSchema}double"/&gt;
 *         &lt;element name="FchServDesde" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="FchServHasta" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="FchVtoPago" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="MonId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="MonCotiz" type="{http://www.w3.org/2001/XMLSchema}double"/&gt;
 *         &lt;element name="CbtesAsoc" type="{http://ar.gov.afip.dif.FEV1/}ArrayOfCbteAsoc" minOccurs="0"/&gt;
 *         &lt;element name="Tributos" type="{http://ar.gov.afip.dif.FEV1/}ArrayOfTributo" minOccurs="0"/&gt;
 *         &lt;element name="Iva" type="{http://ar.gov.afip.dif.FEV1/}ArrayOfAlicIva" minOccurs="0"/&gt;
 *         &lt;element name="Opcionales" type="{http://ar.gov.afip.dif.FEV1/}ArrayOfOpcional" minOccurs="0"/&gt;
 *         &lt;element name="Compradores" type="{http://ar.gov.afip.dif.FEV1/}ArrayOfComprador" minOccurs="0"/&gt;
 *         &lt;element name="PeriodoAsoc" type="{http://ar.gov.afip.dif.FEV1/}Periodo" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
    name = "FEDetRequest",
    namespace = "http://ar.gov.afip.dif.FEV1/",
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
      "compradores",
      "periodoAsoc"
    })
@XmlSeeAlso({FECAEDetRequest.class, FECAEADetRequest.class})
@Generated(
    value = "com.sun.tools.xjc.Driver",
    comments = "JAXB RI v2.3.4",
    date = "2021-06-08T00:50:15-03:00")
public class FEDetRequest {

  @XmlElement(name = "Concepto", namespace = "http://ar.gov.afip.dif.FEV1/")
  @Generated(
      value = "com.sun.tools.xjc.Driver",
      comments = "JAXB RI v2.3.4",
      date = "2021-06-08T00:50:15-03:00")
  protected int concepto;

  @XmlElement(name = "DocTipo", namespace = "http://ar.gov.afip.dif.FEV1/")
  @Generated(
      value = "com.sun.tools.xjc.Driver",
      comments = "JAXB RI v2.3.4",
      date = "2021-06-08T00:50:15-03:00")
  protected int docTipo;

  @XmlElement(name = "DocNro", namespace = "http://ar.gov.afip.dif.FEV1/")
  @Generated(
      value = "com.sun.tools.xjc.Driver",
      comments = "JAXB RI v2.3.4",
      date = "2021-06-08T00:50:15-03:00")
  protected long docNro;

  @XmlElement(name = "CbteDesde", namespace = "http://ar.gov.afip.dif.FEV1/")
  @Generated(
      value = "com.sun.tools.xjc.Driver",
      comments = "JAXB RI v2.3.4",
      date = "2021-06-08T00:50:15-03:00")
  protected long cbteDesde;

  @XmlElement(name = "CbteHasta", namespace = "http://ar.gov.afip.dif.FEV1/")
  @Generated(
      value = "com.sun.tools.xjc.Driver",
      comments = "JAXB RI v2.3.4",
      date = "2021-06-08T00:50:15-03:00")
  protected long cbteHasta;

  @XmlElement(name = "CbteFch", namespace = "http://ar.gov.afip.dif.FEV1/")
  @Generated(
      value = "com.sun.tools.xjc.Driver",
      comments = "JAXB RI v2.3.4",
      date = "2021-06-08T00:50:15-03:00")
  protected String cbteFch;

  @XmlElement(name = "ImpTotal", namespace = "http://ar.gov.afip.dif.FEV1/")
  @Generated(
      value = "com.sun.tools.xjc.Driver",
      comments = "JAXB RI v2.3.4",
      date = "2021-06-08T00:50:15-03:00")
  protected double impTotal;

  @XmlElement(name = "ImpTotConc", namespace = "http://ar.gov.afip.dif.FEV1/")
  @Generated(
      value = "com.sun.tools.xjc.Driver",
      comments = "JAXB RI v2.3.4",
      date = "2021-06-08T00:50:15-03:00")
  protected double impTotConc;

  @XmlElement(name = "ImpNeto", namespace = "http://ar.gov.afip.dif.FEV1/")
  @Generated(
      value = "com.sun.tools.xjc.Driver",
      comments = "JAXB RI v2.3.4",
      date = "2021-06-08T00:50:15-03:00")
  protected double impNeto;

  @XmlElement(name = "ImpOpEx", namespace = "http://ar.gov.afip.dif.FEV1/")
  @Generated(
      value = "com.sun.tools.xjc.Driver",
      comments = "JAXB RI v2.3.4",
      date = "2021-06-08T00:50:15-03:00")
  protected double impOpEx;

  @XmlElement(name = "ImpTrib", namespace = "http://ar.gov.afip.dif.FEV1/")
  @Generated(
      value = "com.sun.tools.xjc.Driver",
      comments = "JAXB RI v2.3.4",
      date = "2021-06-08T00:50:15-03:00")
  protected double impTrib;

  @XmlElement(name = "ImpIVA", namespace = "http://ar.gov.afip.dif.FEV1/")
  @Generated(
      value = "com.sun.tools.xjc.Driver",
      comments = "JAXB RI v2.3.4",
      date = "2021-06-08T00:50:15-03:00")
  protected double impIVA;

  @XmlElement(name = "FchServDesde", namespace = "http://ar.gov.afip.dif.FEV1/")
  @Generated(
      value = "com.sun.tools.xjc.Driver",
      comments = "JAXB RI v2.3.4",
      date = "2021-06-08T00:50:15-03:00")
  protected String fchServDesde;

  @XmlElement(name = "FchServHasta", namespace = "http://ar.gov.afip.dif.FEV1/")
  @Generated(
      value = "com.sun.tools.xjc.Driver",
      comments = "JAXB RI v2.3.4",
      date = "2021-06-08T00:50:15-03:00")
  protected String fchServHasta;

  @XmlElement(name = "FchVtoPago", namespace = "http://ar.gov.afip.dif.FEV1/")
  @Generated(
      value = "com.sun.tools.xjc.Driver",
      comments = "JAXB RI v2.3.4",
      date = "2021-06-08T00:50:15-03:00")
  protected String fchVtoPago;

  @XmlElement(name = "MonId", namespace = "http://ar.gov.afip.dif.FEV1/")
  @Generated(
      value = "com.sun.tools.xjc.Driver",
      comments = "JAXB RI v2.3.4",
      date = "2021-06-08T00:50:15-03:00")
  protected String monId;

  @XmlElement(name = "MonCotiz", namespace = "http://ar.gov.afip.dif.FEV1/")
  @Generated(
      value = "com.sun.tools.xjc.Driver",
      comments = "JAXB RI v2.3.4",
      date = "2021-06-08T00:50:15-03:00")
  protected double monCotiz;

  @XmlElement(name = "CbtesAsoc", namespace = "http://ar.gov.afip.dif.FEV1/")
  @Generated(
      value = "com.sun.tools.xjc.Driver",
      comments = "JAXB RI v2.3.4",
      date = "2021-06-08T00:50:15-03:00")
  protected ArrayOfCbteAsoc cbtesAsoc;

  @XmlElement(name = "Tributos", namespace = "http://ar.gov.afip.dif.FEV1/")
  @Generated(
      value = "com.sun.tools.xjc.Driver",
      comments = "JAXB RI v2.3.4",
      date = "2021-06-08T00:50:15-03:00")
  protected ArrayOfTributo tributos;

  @XmlElement(name = "Iva", namespace = "http://ar.gov.afip.dif.FEV1/")
  @Generated(
      value = "com.sun.tools.xjc.Driver",
      comments = "JAXB RI v2.3.4",
      date = "2021-06-08T00:50:15-03:00")
  protected ArrayOfAlicIva iva;

  @XmlElement(name = "Opcionales", namespace = "http://ar.gov.afip.dif.FEV1/")
  @Generated(
      value = "com.sun.tools.xjc.Driver",
      comments = "JAXB RI v2.3.4",
      date = "2021-06-08T00:50:15-03:00")
  protected ArrayOfOpcional opcionales;

  @XmlElement(name = "Compradores", namespace = "http://ar.gov.afip.dif.FEV1/")
  @Generated(
      value = "com.sun.tools.xjc.Driver",
      comments = "JAXB RI v2.3.4",
      date = "2021-06-08T00:50:15-03:00")
  protected ArrayOfComprador compradores;

  @XmlElement(name = "PeriodoAsoc", namespace = "http://ar.gov.afip.dif.FEV1/")
  @Generated(
      value = "com.sun.tools.xjc.Driver",
      comments = "JAXB RI v2.3.4",
      date = "2021-06-08T00:50:15-03:00")
  protected Periodo periodoAsoc;

  /** Obtiene el valor de la propiedad concepto. */
  @Generated(
      value = "com.sun.tools.xjc.Driver",
      comments = "JAXB RI v2.3.4",
      date = "2021-06-08T00:50:15-03:00")
  public int getConcepto() {
    return concepto;
  }

  /** Define el valor de la propiedad concepto. */
  @Generated(
      value = "com.sun.tools.xjc.Driver",
      comments = "JAXB RI v2.3.4",
      date = "2021-06-08T00:50:15-03:00")
  public void setConcepto(int value) {
    this.concepto = value;
  }

  /** Obtiene el valor de la propiedad docTipo. */
  @Generated(
      value = "com.sun.tools.xjc.Driver",
      comments = "JAXB RI v2.3.4",
      date = "2021-06-08T00:50:15-03:00")
  public int getDocTipo() {
    return docTipo;
  }

  /** Define el valor de la propiedad docTipo. */
  @Generated(
      value = "com.sun.tools.xjc.Driver",
      comments = "JAXB RI v2.3.4",
      date = "2021-06-08T00:50:15-03:00")
  public void setDocTipo(int value) {
    this.docTipo = value;
  }

  /** Obtiene el valor de la propiedad docNro. */
  @Generated(
      value = "com.sun.tools.xjc.Driver",
      comments = "JAXB RI v2.3.4",
      date = "2021-06-08T00:50:15-03:00")
  public long getDocNro() {
    return docNro;
  }

  /** Define el valor de la propiedad docNro. */
  @Generated(
      value = "com.sun.tools.xjc.Driver",
      comments = "JAXB RI v2.3.4",
      date = "2021-06-08T00:50:15-03:00")
  public void setDocNro(long value) {
    this.docNro = value;
  }

  /** Obtiene el valor de la propiedad cbteDesde. */
  @Generated(
      value = "com.sun.tools.xjc.Driver",
      comments = "JAXB RI v2.3.4",
      date = "2021-06-08T00:50:15-03:00")
  public long getCbteDesde() {
    return cbteDesde;
  }

  /** Define el valor de la propiedad cbteDesde. */
  @Generated(
      value = "com.sun.tools.xjc.Driver",
      comments = "JAXB RI v2.3.4",
      date = "2021-06-08T00:50:15-03:00")
  public void setCbteDesde(long value) {
    this.cbteDesde = value;
  }

  /** Obtiene el valor de la propiedad cbteHasta. */
  @Generated(
      value = "com.sun.tools.xjc.Driver",
      comments = "JAXB RI v2.3.4",
      date = "2021-06-08T00:50:15-03:00")
  public long getCbteHasta() {
    return cbteHasta;
  }

  /** Define el valor de la propiedad cbteHasta. */
  @Generated(
      value = "com.sun.tools.xjc.Driver",
      comments = "JAXB RI v2.3.4",
      date = "2021-06-08T00:50:15-03:00")
  public void setCbteHasta(long value) {
    this.cbteHasta = value;
  }

  /**
   * Obtiene el valor de la propiedad cbteFch.
   *
   * @return possible object is {@link String }
   */
  @Generated(
      value = "com.sun.tools.xjc.Driver",
      comments = "JAXB RI v2.3.4",
      date = "2021-06-08T00:50:15-03:00")
  public String getCbteFch() {
    return cbteFch;
  }

  /**
   * Define el valor de la propiedad cbteFch.
   *
   * @param value allowed object is {@link String }
   */
  @Generated(
      value = "com.sun.tools.xjc.Driver",
      comments = "JAXB RI v2.3.4",
      date = "2021-06-08T00:50:15-03:00")
  public void setCbteFch(String value) {
    this.cbteFch = value;
  }

  /** Obtiene el valor de la propiedad impTotal. */
  @Generated(
      value = "com.sun.tools.xjc.Driver",
      comments = "JAXB RI v2.3.4",
      date = "2021-06-08T00:50:15-03:00")
  public double getImpTotal() {
    return impTotal;
  }

  /** Define el valor de la propiedad impTotal. */
  @Generated(
      value = "com.sun.tools.xjc.Driver",
      comments = "JAXB RI v2.3.4",
      date = "2021-06-08T00:50:15-03:00")
  public void setImpTotal(double value) {
    this.impTotal = value;
  }

  /** Obtiene el valor de la propiedad impTotConc. */
  @Generated(
      value = "com.sun.tools.xjc.Driver",
      comments = "JAXB RI v2.3.4",
      date = "2021-06-08T00:50:15-03:00")
  public double getImpTotConc() {
    return impTotConc;
  }

  /** Define el valor de la propiedad impTotConc. */
  @Generated(
      value = "com.sun.tools.xjc.Driver",
      comments = "JAXB RI v2.3.4",
      date = "2021-06-08T00:50:15-03:00")
  public void setImpTotConc(double value) {
    this.impTotConc = value;
  }

  /** Obtiene el valor de la propiedad impNeto. */
  @Generated(
      value = "com.sun.tools.xjc.Driver",
      comments = "JAXB RI v2.3.4",
      date = "2021-06-08T00:50:15-03:00")
  public double getImpNeto() {
    return impNeto;
  }

  /** Define el valor de la propiedad impNeto. */
  @Generated(
      value = "com.sun.tools.xjc.Driver",
      comments = "JAXB RI v2.3.4",
      date = "2021-06-08T00:50:15-03:00")
  public void setImpNeto(double value) {
    this.impNeto = value;
  }

  /** Obtiene el valor de la propiedad impOpEx. */
  @Generated(
      value = "com.sun.tools.xjc.Driver",
      comments = "JAXB RI v2.3.4",
      date = "2021-06-08T00:50:15-03:00")
  public double getImpOpEx() {
    return impOpEx;
  }

  /** Define el valor de la propiedad impOpEx. */
  @Generated(
      value = "com.sun.tools.xjc.Driver",
      comments = "JAXB RI v2.3.4",
      date = "2021-06-08T00:50:15-03:00")
  public void setImpOpEx(double value) {
    this.impOpEx = value;
  }

  /** Obtiene el valor de la propiedad impTrib. */
  @Generated(
      value = "com.sun.tools.xjc.Driver",
      comments = "JAXB RI v2.3.4",
      date = "2021-06-08T00:50:15-03:00")
  public double getImpTrib() {
    return impTrib;
  }

  /** Define el valor de la propiedad impTrib. */
  @Generated(
      value = "com.sun.tools.xjc.Driver",
      comments = "JAXB RI v2.3.4",
      date = "2021-06-08T00:50:15-03:00")
  public void setImpTrib(double value) {
    this.impTrib = value;
  }

  /** Obtiene el valor de la propiedad impIVA. */
  @Generated(
      value = "com.sun.tools.xjc.Driver",
      comments = "JAXB RI v2.3.4",
      date = "2021-06-08T00:50:15-03:00")
  public double getImpIVA() {
    return impIVA;
  }

  /** Define el valor de la propiedad impIVA. */
  @Generated(
      value = "com.sun.tools.xjc.Driver",
      comments = "JAXB RI v2.3.4",
      date = "2021-06-08T00:50:15-03:00")
  public void setImpIVA(double value) {
    this.impIVA = value;
  }

  /**
   * Obtiene el valor de la propiedad fchServDesde.
   *
   * @return possible object is {@link String }
   */
  @Generated(
      value = "com.sun.tools.xjc.Driver",
      comments = "JAXB RI v2.3.4",
      date = "2021-06-08T00:50:15-03:00")
  public String getFchServDesde() {
    return fchServDesde;
  }

  /**
   * Define el valor de la propiedad fchServDesde.
   *
   * @param value allowed object is {@link String }
   */
  @Generated(
      value = "com.sun.tools.xjc.Driver",
      comments = "JAXB RI v2.3.4",
      date = "2021-06-08T00:50:15-03:00")
  public void setFchServDesde(String value) {
    this.fchServDesde = value;
  }

  /**
   * Obtiene el valor de la propiedad fchServHasta.
   *
   * @return possible object is {@link String }
   */
  @Generated(
      value = "com.sun.tools.xjc.Driver",
      comments = "JAXB RI v2.3.4",
      date = "2021-06-08T00:50:15-03:00")
  public String getFchServHasta() {
    return fchServHasta;
  }

  /**
   * Define el valor de la propiedad fchServHasta.
   *
   * @param value allowed object is {@link String }
   */
  @Generated(
      value = "com.sun.tools.xjc.Driver",
      comments = "JAXB RI v2.3.4",
      date = "2021-06-08T00:50:15-03:00")
  public void setFchServHasta(String value) {
    this.fchServHasta = value;
  }

  /**
   * Obtiene el valor de la propiedad fchVtoPago.
   *
   * @return possible object is {@link String }
   */
  @Generated(
      value = "com.sun.tools.xjc.Driver",
      comments = "JAXB RI v2.3.4",
      date = "2021-06-08T00:50:15-03:00")
  public String getFchVtoPago() {
    return fchVtoPago;
  }

  /**
   * Define el valor de la propiedad fchVtoPago.
   *
   * @param value allowed object is {@link String }
   */
  @Generated(
      value = "com.sun.tools.xjc.Driver",
      comments = "JAXB RI v2.3.4",
      date = "2021-06-08T00:50:15-03:00")
  public void setFchVtoPago(String value) {
    this.fchVtoPago = value;
  }

  /**
   * Obtiene el valor de la propiedad monId.
   *
   * @return possible object is {@link String }
   */
  @Generated(
      value = "com.sun.tools.xjc.Driver",
      comments = "JAXB RI v2.3.4",
      date = "2021-06-08T00:50:15-03:00")
  public String getMonId() {
    return monId;
  }

  /**
   * Define el valor de la propiedad monId.
   *
   * @param value allowed object is {@link String }
   */
  @Generated(
      value = "com.sun.tools.xjc.Driver",
      comments = "JAXB RI v2.3.4",
      date = "2021-06-08T00:50:15-03:00")
  public void setMonId(String value) {
    this.monId = value;
  }

  /** Obtiene el valor de la propiedad monCotiz. */
  @Generated(
      value = "com.sun.tools.xjc.Driver",
      comments = "JAXB RI v2.3.4",
      date = "2021-06-08T00:50:15-03:00")
  public double getMonCotiz() {
    return monCotiz;
  }

  /** Define el valor de la propiedad monCotiz. */
  @Generated(
      value = "com.sun.tools.xjc.Driver",
      comments = "JAXB RI v2.3.4",
      date = "2021-06-08T00:50:15-03:00")
  public void setMonCotiz(double value) {
    this.monCotiz = value;
  }

  /**
   * Obtiene el valor de la propiedad cbtesAsoc.
   *
   * @return possible object is {@link ArrayOfCbteAsoc }
   */
  @Generated(
      value = "com.sun.tools.xjc.Driver",
      comments = "JAXB RI v2.3.4",
      date = "2021-06-08T00:50:15-03:00")
  public ArrayOfCbteAsoc getCbtesAsoc() {
    return cbtesAsoc;
  }

  /**
   * Define el valor de la propiedad cbtesAsoc.
   *
   * @param value allowed object is {@link ArrayOfCbteAsoc }
   */
  @Generated(
      value = "com.sun.tools.xjc.Driver",
      comments = "JAXB RI v2.3.4",
      date = "2021-06-08T00:50:15-03:00")
  public void setCbtesAsoc(ArrayOfCbteAsoc value) {
    this.cbtesAsoc = value;
  }

  /**
   * Obtiene el valor de la propiedad tributos.
   *
   * @return possible object is {@link ArrayOfTributo }
   */
  @Generated(
      value = "com.sun.tools.xjc.Driver",
      comments = "JAXB RI v2.3.4",
      date = "2021-06-08T00:50:15-03:00")
  public ArrayOfTributo getTributos() {
    return tributos;
  }

  /**
   * Define el valor de la propiedad tributos.
   *
   * @param value allowed object is {@link ArrayOfTributo }
   */
  @Generated(
      value = "com.sun.tools.xjc.Driver",
      comments = "JAXB RI v2.3.4",
      date = "2021-06-08T00:50:15-03:00")
  public void setTributos(ArrayOfTributo value) {
    this.tributos = value;
  }

  /**
   * Obtiene el valor de la propiedad iva.
   *
   * @return possible object is {@link ArrayOfAlicIva }
   */
  @Generated(
      value = "com.sun.tools.xjc.Driver",
      comments = "JAXB RI v2.3.4",
      date = "2021-06-08T00:50:15-03:00")
  public ArrayOfAlicIva getIva() {
    return iva;
  }

  /**
   * Define el valor de la propiedad iva.
   *
   * @param value allowed object is {@link ArrayOfAlicIva }
   */
  @Generated(
      value = "com.sun.tools.xjc.Driver",
      comments = "JAXB RI v2.3.4",
      date = "2021-06-08T00:50:15-03:00")
  public void setIva(ArrayOfAlicIva value) {
    this.iva = value;
  }

  /**
   * Obtiene el valor de la propiedad opcionales.
   *
   * @return possible object is {@link ArrayOfOpcional }
   */
  @Generated(
      value = "com.sun.tools.xjc.Driver",
      comments = "JAXB RI v2.3.4",
      date = "2021-06-08T00:50:15-03:00")
  public ArrayOfOpcional getOpcionales() {
    return opcionales;
  }

  /**
   * Define el valor de la propiedad opcionales.
   *
   * @param value allowed object is {@link ArrayOfOpcional }
   */
  @Generated(
      value = "com.sun.tools.xjc.Driver",
      comments = "JAXB RI v2.3.4",
      date = "2021-06-08T00:50:15-03:00")
  public void setOpcionales(ArrayOfOpcional value) {
    this.opcionales = value;
  }

  /**
   * Obtiene el valor de la propiedad compradores.
   *
   * @return possible object is {@link ArrayOfComprador }
   */
  @Generated(
      value = "com.sun.tools.xjc.Driver",
      comments = "JAXB RI v2.3.4",
      date = "2021-06-08T00:50:15-03:00")
  public ArrayOfComprador getCompradores() {
    return compradores;
  }

  /**
   * Define el valor de la propiedad compradores.
   *
   * @param value allowed object is {@link ArrayOfComprador }
   */
  @Generated(
      value = "com.sun.tools.xjc.Driver",
      comments = "JAXB RI v2.3.4",
      date = "2021-06-08T00:50:15-03:00")
  public void setCompradores(ArrayOfComprador value) {
    this.compradores = value;
  }

  /**
   * Obtiene el valor de la propiedad periodoAsoc.
   *
   * @return possible object is {@link Periodo }
   */
  @Generated(
      value = "com.sun.tools.xjc.Driver",
      comments = "JAXB RI v2.3.4",
      date = "2021-06-08T00:50:15-03:00")
  public Periodo getPeriodoAsoc() {
    return periodoAsoc;
  }

  /**
   * Define el valor de la propiedad periodoAsoc.
   *
   * @param value allowed object is {@link Periodo }
   */
  @Generated(
      value = "com.sun.tools.xjc.Driver",
      comments = "JAXB RI v2.3.4",
      date = "2021-06-08T00:50:15-03:00")
  public void setPeriodoAsoc(Periodo value) {
    this.periodoAsoc = value;
  }
}
