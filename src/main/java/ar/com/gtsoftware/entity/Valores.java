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
package ar.com.gtsoftware.entity;

import java.math.BigDecimal;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * Son los documentos que representan cupones o cheques.
 *
 * @author Rodrigo M. Tato Rothamel mailto:rotatomel@gmail.com
 */
@Entity
@Table(name = "valores")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "tipo_valor")
@Getter
@Setter
public class Valores extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "valores_id_valor")
  @SequenceGenerator(
      allocationSize = 1,
      initialValue = 1,
      name = "valores_id_valor",
      sequenceName = "valores_id_valor_seq")
  @Basic(optional = false)
  @Column(name = "id_valor", nullable = false, updatable = false)
  private Long id;

  @NotNull
  @Column(name = "monto")
  private BigDecimal monto;

  @OneToOne(mappedBy = "idValor")
  private RecibosDetalle reciboDetalle;
}
