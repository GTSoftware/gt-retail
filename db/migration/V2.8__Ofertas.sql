/*
 * Copyright 2017 GT Software.
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
/**
 * Author:  Rodrigo
 * Created: Nov 08, 2018
 */

create table ofertas (
  id_oferta serial primary key,
  texto_oferta varchar(90) not null,
  tipo_accion varchar(30) not null,
  descuento numeric(19,4) not null,
  vigencia_desde timestamp without time zone NOT NULL,
  vigencia_hasta timestamp without time zone NOT NULL,
  id_sucursal integer,
  version integer not null default 0,

   CONSTRAINT ofertas_id_sucursal_fk FOREIGN KEY (id_sucursal)
      REFERENCES sucursales (id_sucursal) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);

create table ofertas_condiciones (
  id_oferta_condicion serial primary key,
  id_oferta integer not null,
  operacion varchar(30) not null,
  campo varchar(30) not null,
  valor varchar (30) not null,
  version integer not null default 0,

   CONSTRAINT ofertas_condiciones_id_oferta_fk FOREIGN KEY (id_oferta)
      REFERENCES ofertas (id_oferta) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);



