//
// Este archivo ha sido generado por Eclipse Implementation of JAXB v2.3.4
// Visite https://eclipse-ee4j.github.io/jaxb-ri
// Todas las modificaciones realizadas en este archivo se perderán si se vuelve a compilar el
// esquema de origen.
// Generado el: 2021.06.08 a las 12:50:16 AM ART
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
 * Clase Java para ArrayOfOpcionalTipo complex type.
 *
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta
 * clase.
 *
 * <pre>
 * &lt;complexType name="ArrayOfOpcionalTipo"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="OpcionalTipo" type="{http://ar.gov.afip.dif.FEV1/}OpcionalTipo" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
    name = "ArrayOfOpcionalTipo",
    namespace = "http://ar.gov.afip.dif.FEV1/",
    propOrder = {"opcionalTipo"})
@Generated(
    value = "com.sun.tools.xjc.Driver",
    comments = "JAXB RI v2.3.4",
    date = "2021-06-08T00:50:15-03:00")
public class ArrayOfOpcionalTipo {

  @XmlElement(name = "OpcionalTipo", namespace = "http://ar.gov.afip.dif.FEV1/", nillable = true)
  @Generated(
      value = "com.sun.tools.xjc.Driver",
      comments = "JAXB RI v2.3.4",
      date = "2021-06-08T00:50:15-03:00")
  protected List<OpcionalTipo> opcionalTipo;

  /**
   * Gets the value of the opcionalTipo property.
   *
   * <p>This accessor method returns a reference to the live list, not a snapshot. Therefore any
   * modification you make to the returned list will be present inside the JAXB object. This is why
   * there is not a <CODE>set</CODE> method for the opcionalTipo property.
   *
   * <p>For example, to add a new item, do as follows:
   *
   * <pre>
   *    getOpcionalTipo().add(newItem);
   * </pre>
   *
   * <p>Objects of the following type(s) are allowed in the list {@link OpcionalTipo }
   */
  @Generated(
      value = "com.sun.tools.xjc.Driver",
      comments = "JAXB RI v2.3.4",
      date = "2021-06-08T00:50:15-03:00")
  public List<OpcionalTipo> getOpcionalTipo() {
    if (opcionalTipo == null) {
      opcionalTipo = new ArrayList<OpcionalTipo>();
    }
    return this.opcionalTipo;
  }
}
