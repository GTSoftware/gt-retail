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
 * Clase Java para Opcional complex type.
 *
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta
 * clase.
 *
 * <pre>
 * &lt;complexType name="Opcional"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="Id" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="Valor" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
    name = "Opcional",
    namespace = "http://ar.gov.afip.dif.FEV1/",
    propOrder = {"id", "valor"})
@Generated(
    value = "com.sun.tools.xjc.Driver",
    comments = "JAXB RI v2.3.4",
    date = "2021-06-08T00:50:15-03:00")
public class Opcional {

  @XmlElement(name = "Id", namespace = "http://ar.gov.afip.dif.FEV1/")
  @Generated(
      value = "com.sun.tools.xjc.Driver",
      comments = "JAXB RI v2.3.4",
      date = "2021-06-08T00:50:15-03:00")
  protected String id;

  @XmlElement(name = "Valor", namespace = "http://ar.gov.afip.dif.FEV1/")
  @Generated(
      value = "com.sun.tools.xjc.Driver",
      comments = "JAXB RI v2.3.4",
      date = "2021-06-08T00:50:15-03:00")
  protected String valor;

  /**
   * Obtiene el valor de la propiedad id.
   *
   * @return possible object is {@link String }
   */
  @Generated(
      value = "com.sun.tools.xjc.Driver",
      comments = "JAXB RI v2.3.4",
      date = "2021-06-08T00:50:15-03:00")
  public String getId() {
    return id;
  }

  /**
   * Define el valor de la propiedad id.
   *
   * @param value allowed object is {@link String }
   */
  @Generated(
      value = "com.sun.tools.xjc.Driver",
      comments = "JAXB RI v2.3.4",
      date = "2021-06-08T00:50:15-03:00")
  public void setId(String value) {
    this.id = value;
  }

  /**
   * Obtiene el valor de la propiedad valor.
   *
   * @return possible object is {@link String }
   */
  @Generated(
      value = "com.sun.tools.xjc.Driver",
      comments = "JAXB RI v2.3.4",
      date = "2021-06-08T00:50:15-03:00")
  public String getValor() {
    return valor;
  }

  /**
   * Define el valor de la propiedad valor.
   *
   * @param value allowed object is {@link String }
   */
  @Generated(
      value = "com.sun.tools.xjc.Driver",
      comments = "JAXB RI v2.3.4",
      date = "2021-06-08T00:50:15-03:00")
  public void setValor(String value) {
    this.valor = value;
  }
}
