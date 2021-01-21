/*
 * Copyright 2016 GT Software.
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

import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/** @author Rodrigo M. Tato Rothamel mailto:rotatomel@gmail.com */
@Entity
@Table(name = "afip_auth_services")
@XmlRootElement
public class AFIPAuthServices extends GTEntity<String> {

  private static final long serialVersionUID = 1L;

  @Id
  @Basic(optional = false)
  @NotNull
  @Size(min = 1, max = 30)
  @Column(name = "nombre_servicio")
  private String nombreServicio;

  @Size(max = 1024)
  @Column(name = "token")
  private String token;

  @Size(max = 255)
  @Column(name = "sign")
  private String sign;

  @Column(name = "fecha_expiracion")
  private LocalDateTime fechaExpiracion;

  public AFIPAuthServices() {}

  public String getNombreServicio() {
    return nombreServicio;
  }

  public void setNombreServicio(String nombreServicio) {
    this.nombreServicio = nombreServicio;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public String getSign() {
    return sign;
  }

  public void setSign(String sign) {
    this.sign = sign;
  }

  public LocalDateTime getFechaExpiracion() {
    return fechaExpiracion;
  }

  public void setFechaExpiracion(LocalDateTime fechaExpiracion) {
    this.fechaExpiracion = fechaExpiracion;
  }

  @Override
  public boolean isNew() {
    return nombreServicio == null;
  }

  @Override
  public String getId() {
    return nombreServicio;
  }

  @Override
  public String calculateId(String id) {
    return id;
  }

  @Override
  public String getStringId() {
    return nombreServicio;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 19 * hash + Objects.hashCode(this.nombreServicio);
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
    final AFIPAuthServices other = (AFIPAuthServices) obj;
    return Objects.equals(this.nombreServicio, other.nombreServicio);
  }
}
