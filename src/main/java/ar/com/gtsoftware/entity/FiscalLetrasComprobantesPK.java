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
package ar.com.gtsoftware.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/** @author rodrigo */
@Embeddable
@Getter
@Setter
public class FiscalLetrasComprobantesPK implements Serializable {

  private static final long serialVersionUID = 1L;

  @Basic(optional = false)
  @NotNull
  @Column(name = "id_resoponsabildiad_iva_emisor")
  private int idResoponsabildiadIvaEmisor;

  @Basic(optional = false)
  @NotNull
  @Column(name = "id_resoponsabildiad_iva_receptor")
  private int idResoponsabildiadIvaReceptor;

  public FiscalLetrasComprobantesPK() {}

  public FiscalLetrasComprobantesPK(
      int idResoponsabildiadIvaEmisor, int idResoponsabildiadIvaReceptor) {
    this.idResoponsabildiadIvaEmisor = idResoponsabildiadIvaEmisor;
    this.idResoponsabildiadIvaReceptor = idResoponsabildiadIvaReceptor;
  }

  @Override
  public int hashCode() {
    int hash = 0;
    hash += idResoponsabildiadIvaEmisor;
    hash += idResoponsabildiadIvaReceptor;
    return hash;
  }

  @Override
  public boolean equals(Object object) {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if (!(object instanceof FiscalLetrasComprobantesPK)) {
      return false;
    }
    FiscalLetrasComprobantesPK other = (FiscalLetrasComprobantesPK) object;
    if (this.idResoponsabildiadIvaEmisor != other.idResoponsabildiadIvaEmisor) {
      return false;
    }
    return this.idResoponsabildiadIvaReceptor == other.idResoponsabildiadIvaReceptor;
  }

  @Override
  public String toString() {
    return String.format("%s-%s", idResoponsabildiadIvaEmisor, idResoponsabildiadIvaReceptor);
  }
}
