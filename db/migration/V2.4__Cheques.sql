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
 * Created: Sep 30, 2017
 */


alter table bancos drop column id_pais;
alter table bancos drop column id_provincia ;
alter table bancos drop column id_localidad ;
alter table bancos drop column direccion ;
alter table bancos drop column telefono_fijo ;
alter table bancos drop column celular ;
alter table bancos drop column cuit ;
alter table bancos drop column id_responsabilidad_iva ;
alter table bancos drop column fecha_alta;
alter table bancos drop column activo ;

alter table bancos add column id_persona integer references personas(id_persona);


create table cheques_terceros(
    id_valor integer primary key references valores(id_valor),
    nro_cheque varchar(30) not null,
    fecha_origen date not null,
    notas varchar(255),
    cuit_originante varchar(11) not null,
    razon_social_originante varchar(200) not null,
    fecha_vencimiento date not null,
    id_banco integer not null references bancos(id_banco),
    fecha_cobro timestamp without time zone
);



--Insert de bancos de Argentina
insert into bancos (razon_social) values ('BACS BANCO DE CREDITO Y SECURITIZACION S');
insert into bancos (razon_social) values ('BANCO BICA S.A.');
insert into bancos (razon_social) values ('BANCO BRADESCO ARGENTINA S.A.');
insert into bancos (razon_social) values ('BANCO CETELEM ARGENTINA S.A.');
insert into bancos (razon_social) values ('BANCO CMF S.A.');
insert into bancos (razon_social) values ('BANCO COINAG S.A.');
insert into bancos (razon_social) values ('BANCO COLUMBIA S.A.');
insert into bancos (razon_social) values ('BANCO COMAFI SOCIEDAD ANONIMA');
insert into bancos (razon_social) values ('BANCO CREDICOOP COOPERATIVO LIMITADO');
insert into bancos (razon_social) values ('BANCO DE COMERCIO S.A.');
insert into bancos (razon_social) values ('BANCO DE CORRIENTES S.A.');
insert into bancos (razon_social) values ('BANCO DE FORMOSA S.A.');
insert into bancos (razon_social) values ('BANCO DE GALICIA Y BUENOS AIRES S.A.');
insert into bancos (razon_social) values ('BANCO DE INVERSION Y COMERCIO EXTERIOR S');
insert into bancos (razon_social) values ('BANCO DE LA CIUDAD DE BUENOS AIRES');
insert into bancos (razon_social) values ('BANCO DE LA NACION ARGENTINA');
insert into bancos (razon_social) values ('BANCO DE LA PAMPA SOCIEDAD DE ECONOMÍA M');
insert into bancos (razon_social) values ('BANCO DE LA PROVINCIA DE BUENOS AIRES');
insert into bancos (razon_social) values ('BANCO DE LA PROVINCIA DE CORDOBA S.A.');
insert into bancos (razon_social) values ('BANCO DE SAN JUAN S.A.');
insert into bancos (razon_social) values ('BANCO DE SANTA CRUZ S.A.');
insert into bancos (razon_social) values ('BANCO DE SANTIAGO DEL ESTERO S.A.');
insert into bancos (razon_social) values ('BANCO DE SERVICIOS FINANCIEROS S.A.');
insert into bancos (razon_social) values ('BANCO DE SERVICIOS Y TRANSACCIONES S.A.');
insert into bancos (razon_social) values ('BANCO DE VALORES S.A.');
insert into bancos (razon_social) values ('BANCO DEL CHUBUT S.A.');
insert into bancos (razon_social) values ('BANCO DEL SOL S.A.');
insert into bancos (razon_social) values ('BANCO DEL TUCUMAN S.A.');
insert into bancos (razon_social) values ('BANCO FINANSUR S.A.');
insert into bancos (razon_social) values ('BANCO HIPOTECARIO S.A.');
insert into bancos (razon_social) values ('BANCO INDUSTRIAL S.A.');
insert into bancos (razon_social) values ('BANCO INTERFINANZAS S.A.');
insert into bancos (razon_social) values ('BANCO ITAU ARGENTINA S.A.');
insert into bancos (razon_social) values ('BANCO JULIO SOCIEDAD ANONIMA');
insert into bancos (razon_social) values ('BANCO MACRO S.A.');
insert into bancos (razon_social) values ('BANCO MARIVA S.A.');
insert into bancos (razon_social) values ('BANCO MASVENTAS S.A.');
insert into bancos (razon_social) values ('BANCO MERIDIAN S.A.');
insert into bancos (razon_social) values ('BANCO MUNICIPAL DE ROSARIO');
insert into bancos (razon_social) values ('BANCO PATAGONIA S.A.');
insert into bancos (razon_social) values ('BANCO PIANO S.A.');
insert into bancos (razon_social) values ('BANCO PROVINCIA DE TIERRA DEL FUEGO');
insert into bancos (razon_social) values ('BANCO PROVINCIA DEL NEUQUÉN SOCIEDAD ANÓ');
insert into bancos (razon_social) values ('BANCO RIOJA SOCIEDAD ANONIMA UNIPERSONAL');
insert into bancos (razon_social) values ('BANCO ROELA S.A.');
insert into bancos (razon_social) values ('BANCO SAENZ S.A.');
insert into bancos (razon_social) values ('BANCO SANTANDER RIO S.A.');
insert into bancos (razon_social) values ('BANCO SUPERVIELLE S.A.');
insert into bancos (razon_social) values ('BANCO VOII S.A.');
insert into bancos (razon_social) values ('BBVA BANCO FRANCES S.A.');
insert into bancos (razon_social) values ('NUEVO BANCO DE ENTRE RÍOS S.A.');
insert into bancos (razon_social) values ('NUEVO BANCO DE SANTA FE SOCIEDAD ANONIMA');
insert into bancos (razon_social) values ('NUEVO BANCO DEL CHACO S.A.');


