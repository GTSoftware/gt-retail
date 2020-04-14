/*
 * Copyright 2015 GT Software.
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

import lombok.Getter;
import lombok.Setter;

import javax.persistence.MappedSuperclass;
import javax.persistence.Version;
import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.Objects;

/**
 * Superclase para todas las entidades del sistema
 *
 * @author Rodrigo M. Tato Rothamel mailto:rotatomel@gmail.com
 * @param <T> la clase de la clave primaria
 */
@MappedSuperclass
@Getter
@Setter
public abstract class GTEntity<T extends Serializable> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Version
    @XmlTransient
    private Integer version;

    /**
     * Determina si la entidad es nueva o no
     *
     * @return
     */
    @XmlTransient
    public abstract boolean isNew();

    /**
     * Retorna el objeto que es la clave de la entidad
     *
     * @return
     */
    public abstract T getId();

    /**
     * Retorna un objeto clave primaria a partir de la representación en String del Id
     *
     * @param id
     * @return
     */
    public abstract T calculateId(String id);

    /**
     * Retorna la representación en String del ID de la clase que puede ser vuelta a convertir en ID
     *
     * @return
     */
    @XmlTransient
    public abstract String getStringId();

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + Objects.hashCode(this.getId());
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof GTEntity)) {
            return false;
        }
        GTEntity other = (GTEntity) obj;
        return (getId() != null || other.getId() == null)
                && (getId() == null || this.getId().equals(other.getId()));
    }

    @Override
    public String toString() {
        return String.format("%s [id=%s]", this.getClass().getName(), getId());
    }
}
