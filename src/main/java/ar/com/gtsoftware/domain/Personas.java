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

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Rodrigo Tato <rotatomel@gmail.com>
 */
@Entity
@Table(name = "personas")
@Getter
@Setter
public class Personas extends BaseEntity {

    private static final String BUSINESS_STRING = "[%d] %s - %s: %s";
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "personas_id_persona")
    @SequenceGenerator(allocationSize = 1, initialValue = 1, name = "personas_id_persona",
            sequenceName = "personas_id_persona_seq")
    @Basic(optional = false)
    @Column(name = "id_persona", nullable = false, updatable = false)
    private Long id;

    @Column(name = "email", length = 100)
    @Pattern(regexp = "^$|^[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\."
            + "[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@"
            + "(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?")
    private String email;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "razon_social")
    private String razonSocial;
    @Size(max = 60)
    @Column(name = "apellidos")
    private String apellidos;
    @Size(max = 60)
    @Column(name = "nombres")
    private String nombres;
    @Size(max = 200)
    @Column(name = "nombre_fantasia")
    private String nombreFantasia;
    @Size(max = 100)
    @Column(name = "calle")
    private String calle;
    @Size(max = 50)
    @Column(name = "altura")
    private String altura;
    @Size(max = 3)
    @Column(name = "piso")
    private String piso;
    @Size(max = 5)
    @Column(name = "depto")
    private String depto;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 13)
    @Column(name = "documento")
    private String documento;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_alta")
    //@Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime fechaAlta;
    @Basic(optional = false)
    @NotNull
    @Column(name = "activo")
    private boolean activo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cliente")
    private boolean cliente;
    @Basic(optional = false)
    @NotNull
    @Column(name = "proveedor")
    private boolean proveedor;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idPersona")
    private List<PersonasImagenes> personasImagenesList;
    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH}, mappedBy = "idProveedor")
    private List<ProveedoresOrdenesCompra> proveedoresOrdenesCompraList;
    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH}, mappedBy = "idPersona")
    private List<Comprobantes> ventasList;
    @JoinColumn(name = "id_provincia", referencedColumnName = "id_provincia", columnDefinition = "int4")
    @ManyToOne(optional = false)
    private UbicacionProvincias idProvincia;
    @JoinColumn(name = "id_pais", referencedColumnName = "id_pais", columnDefinition = "int4")
    @ManyToOne(optional = false)
    private UbicacionPaises idPais;
    @JoinColumn(name = "id_localidad", referencedColumnName = "id_localidad", columnDefinition = "int4")
    @ManyToOne(optional = false)
    private UbicacionLocalidades idLocalidad;
    @JoinColumn(name = "id_tipo_personeria", referencedColumnName = "id_tipo_personeria", columnDefinition = "int4")
    @ManyToOne(optional = false)
    private LegalTiposPersoneria idTipoPersoneria;
    @JoinColumn(name = "id_tipo_documento", referencedColumnName = "id_tipo_documento", columnDefinition = "int4")
    @ManyToOne(optional = false)
    private LegalTiposDocumento idTipoDocumento;
    @JoinColumn(name = "id_genero", referencedColumnName = "id_genero", columnDefinition = "int4")
    @ManyToOne(optional = false)
    private LegalGeneros idGenero;
    @JoinColumn(name = "id_responsabilidad_iva", referencedColumnName = "id_resoponsabildiad_iva", columnDefinition = "int4")
    @ManyToOne(optional = false)
    private FiscalResponsabilidadesIva idResponsabilidadIva;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idPersona", orphanRemoval = true)
    private List<PersonasTelefonos> personasTelefonosList;
    @JoinColumn(name = "id_sucursal", referencedColumnName = "id_sucursal", columnDefinition = "int4")
    @ManyToOne(optional = false)
    private Sucursales idSucursal;

    public String getBusinessString() {
        return String.format(BUSINESS_STRING, id, razonSocial, idTipoDocumento.getNombreTipoDocumento(), documento);
    }
}
