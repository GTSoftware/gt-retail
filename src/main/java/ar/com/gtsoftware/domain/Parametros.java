/*
 * Copyright 2014 GT Software.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ar.com.gtsoftware.domain;

import java.util.Objects;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/** @author rodrigo */
@Entity
@Table(name = "parametros")
@XmlRootElement
public class Parametros extends GTEntity<String> {

  private static final long serialVersionUID = 1L;

  @Id
  @Basic(optional = false)
  @NotNull
  @Size(min = 1, max = 255)
  @Column(name = "nombre_parametro")
  private String nombreParametro;

  @Size(max = 255)
  @Column(name = "valor_parametro")
  private String valorParametro;

  @Basic(optional = false)
  @NotNull
  @Size(min = 1, max = 1024)
  @Column(name = "descripcion_parametro")
  private String descripcionParametro;

  public Parametros() {}

  public Parametros(String nombreParametro) {
    this.nombreParametro = nombreParametro;
  }

  public Parametros(String nombreParametro, String descripcionParametro) {
    this.nombreParametro = nombreParametro;
    this.descripcionParametro = descripcionParametro;
  }

  public String getNombreParametro() {
    return nombreParametro;
  }

  public void setNombreParametro(String nombreParametro) {
    this.nombreParametro = nombreParametro;
  }

  public String getValorParametro() {
    return valorParametro;
  }

  public void setValorParametro(String valorParametro) {
    this.valorParametro = valorParametro;
  }

  public String getDescripcionParametro() {
    return descripcionParametro;
  }

  public void setDescripcionParametro(String descripcionParametro) {
    this.descripcionParametro = descripcionParametro;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 83 * hash + Objects.hashCode(this.nombreParametro);
    return hash;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final Parametros other = (Parametros) obj;
    return Objects.equals(this.nombreParametro, other.nombreParametro);
  }

  @Override
  public String toString() {
    return String.format("%s=%s", nombreParametro, valorParametro);
  }

  @Override
  public boolean isNew() {
    return nombreParametro == null;
  }

  @Override
  public String getId() {
    return nombreParametro;
  }

  @Override
  public String calculateId(String id) {
    return id;
  }

  @Override
  public String getStringId() {
    return nombreParametro;
  }
}
