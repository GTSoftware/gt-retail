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
import javax.xml.bind.annotation.XmlType;

/**
 * Clase Java para FECompConsultaReq complex type.
 *
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta
 * clase.
 *
 * <pre>
 * &lt;complexType name="FECompConsultaReq"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="CbteTipo" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="CbteNro" type="{http://www.w3.org/2001/XMLSchema}long"/&gt;
 *         &lt;element name="PtoVta" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
    name = "FECompConsultaReq",
    namespace = "http://ar.gov.afip.dif.FEV1/",
    propOrder = {"cbteTipo", "cbteNro", "ptoVta"})
@Generated(
    value = "com.sun.tools.xjc.Driver",
    comments = "JAXB RI v2.3.4",
    date = "2021-06-08T00:50:15-03:00")
public class FECompConsultaReq {

  @XmlElement(name = "CbteTipo", namespace = "http://ar.gov.afip.dif.FEV1/")
  @Generated(
      value = "com.sun.tools.xjc.Driver",
      comments = "JAXB RI v2.3.4",
      date = "2021-06-08T00:50:15-03:00")
  protected int cbteTipo;

  @XmlElement(name = "CbteNro", namespace = "http://ar.gov.afip.dif.FEV1/")
  @Generated(
      value = "com.sun.tools.xjc.Driver",
      comments = "JAXB RI v2.3.4",
      date = "2021-06-08T00:50:15-03:00")
  protected long cbteNro;

  @XmlElement(name = "PtoVta", namespace = "http://ar.gov.afip.dif.FEV1/")
  @Generated(
      value = "com.sun.tools.xjc.Driver",
      comments = "JAXB RI v2.3.4",
      date = "2021-06-08T00:50:15-03:00")
  protected int ptoVta;

  /** Obtiene el valor de la propiedad cbteTipo. */
  @Generated(
      value = "com.sun.tools.xjc.Driver",
      comments = "JAXB RI v2.3.4",
      date = "2021-06-08T00:50:15-03:00")
  public int getCbteTipo() {
    return cbteTipo;
  }

  /** Define el valor de la propiedad cbteTipo. */
  @Generated(
      value = "com.sun.tools.xjc.Driver",
      comments = "JAXB RI v2.3.4",
      date = "2021-06-08T00:50:15-03:00")
  public void setCbteTipo(int value) {
    this.cbteTipo = value;
  }

  /** Obtiene el valor de la propiedad cbteNro. */
  @Generated(
      value = "com.sun.tools.xjc.Driver",
      comments = "JAXB RI v2.3.4",
      date = "2021-06-08T00:50:15-03:00")
  public long getCbteNro() {
    return cbteNro;
  }

  /** Define el valor de la propiedad cbteNro. */
  @Generated(
      value = "com.sun.tools.xjc.Driver",
      comments = "JAXB RI v2.3.4",
      date = "2021-06-08T00:50:15-03:00")
  public void setCbteNro(long value) {
    this.cbteNro = value;
  }

  /** Obtiene el valor de la propiedad ptoVta. */
  @Generated(
      value = "com.sun.tools.xjc.Driver",
      comments = "JAXB RI v2.3.4",
      date = "2021-06-08T00:50:15-03:00")
  public int getPtoVta() {
    return ptoVta;
  }

  /** Define el valor de la propiedad ptoVta. */
  @Generated(
      value = "com.sun.tools.xjc.Driver",
      comments = "JAXB RI v2.3.4",
      date = "2021-06-08T00:50:15-03:00")
  public void setPtoVta(int value) {
    this.ptoVta = value;
  }
}
