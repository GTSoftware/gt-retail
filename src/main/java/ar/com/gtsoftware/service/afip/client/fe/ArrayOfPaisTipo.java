//
// Este archivo ha sido generado por Eclipse Implementation of JAXB v2.3.4
// Visite https://eclipse-ee4j.github.io/jaxb-ri
// Todas las modificaciones realizadas en este archivo se perderán si se vuelve a compilar el
// esquema de origen.
// Generado el: 2021.04.20 a las 01:12:11 AM ART
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
 * Clase Java para ArrayOfPaisTipo complex type.
 *
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta
 * clase.
 *
 * <pre>
 * &lt;complexType name="ArrayOfPaisTipo"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="PaisTipo" type="{http://ar.gov.afip.dif.FEV1/}PaisTipo" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
    name = "ArrayOfPaisTipo",
    propOrder = {"paisTipo"})
@Generated(
    value = "com.sun.tools.xjc.Driver",
    comments = "JAXB RI v2.3.4",
    date = "2021-04-20T01:12:11-03:00")
public class ArrayOfPaisTipo {

  @XmlElement(name = "PaisTipo", nillable = true)
  @Generated(
      value = "com.sun.tools.xjc.Driver",
      comments = "JAXB RI v2.3.4",
      date = "2021-04-20T01:12:11-03:00")
  protected List<PaisTipo> paisTipo;

  /**
   * Gets the value of the paisTipo property.
   *
   * <p>This accessor method returns a reference to the live list, not a snapshot. Therefore any
   * modification you make to the returned list will be present inside the JAXB object. This is why
   * there is not a <CODE>set</CODE> method for the paisTipo property.
   *
   * <p>For example, to add a new item, do as follows:
   *
   * <pre>
   *    getPaisTipo().add(newItem);
   * </pre>
   *
   * <p>Objects of the following type(s) are allowed in the list {@link PaisTipo }
   */
  @Generated(
      value = "com.sun.tools.xjc.Driver",
      comments = "JAXB RI v2.3.4",
      date = "2021-04-20T01:12:11-03:00")
  public List<PaisTipo> getPaisTipo() {
    if (paisTipo == null) {
      paisTipo = new ArrayList<PaisTipo>();
    }
    return this.paisTipo;
  }
}
