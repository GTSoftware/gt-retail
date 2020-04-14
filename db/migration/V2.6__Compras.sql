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
 * Created: Dec 02, 2017
 */

drop table if exists proveedores_ordenes_compra_lineas;
drop table if exists proveedores_ordenes_compra;

create table proveedores_ordenes_compra_estados (
    id_estado_orden_compra integer primary key,
    nombre_estado_orden_compra varchar(50) not null,
    version integer NOT NULL DEFAULT 0
);


INSERT INTO proveedores_ordenes_compra_estados (id_estado_orden_compra, nombre_estado_orden_compra, version)
VALUES (1, 'DISEÑO', 0);
INSERT INTO proveedores_ordenes_compra_estados (id_estado_orden_compra, nombre_estado_orden_compra, version)
VALUES (2, 'PENDIENTE DE RECEPCIÓN', 0);
INSERT INTO proveedores_ordenes_compra_estados (id_estado_orden_compra, nombre_estado_orden_compra, version)
VALUES (3, 'RECEPCIÓN PARCIAL', 0);
INSERT INTO proveedores_ordenes_compra_estados (id_estado_orden_compra, nombre_estado_orden_compra, version)
VALUES (4, 'RECEPCIÓN TOTAL', 0);
INSERT INTO proveedores_ordenes_compra_estados (id_estado_orden_compra, nombre_estado_orden_compra, version)
VALUES (5, 'ANULADA', 0);

comment on table proveedores_ordenes_compra_estados is 'Los posibles estados de las órdenes de compra';

create table proveedores_ordenes_compra (
    id_orden_compra serial primary key,
    id_proveedor integer not null, --El proveedor al cual se le realiza el pedido
    fecha_alta timestamp without time zone NOT NULL,
    fecha_modificacion timestamp without time zone not null,
    id_usuario integer NOT NULL, --El usuario que carga la OC
    id_transporte integer, --El proveedor que sería la empresa de transportes que se encarga de traer la mercadería.
    id_estado_orden_compra integer not null,
    observaciones text,
    fecha_estimada_recepcion date,
    version integer not null default 0,
    monto_iva_total numeric(19,2) not null,
    total_orden_compra numeric(19,2) not null,

    CONSTRAINT proveedores_ordenes_compra_id_proveedor_fk FOREIGN KEY (id_proveedor)
      REFERENCES personas (id_persona) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
    CONSTRAINT proveedores_ordenes_compra_id_transporte_fk FOREIGN KEY (id_transporte)
      REFERENCES personas (id_persona) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
    CONSTRAINT proveedores_ordenes_compra_id_estado_orden_compra_fk FOREIGN KEY (id_estado_orden_compra)
      REFERENCES proveedores_ordenes_compra_estados (id_estado_orden_compra) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
    CONSTRAINT proveedores_ordenes_compra_id_usuario_fk FOREIGN KEY (id_usuario)
      REFERENCES usuarios (id_usuario) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);


create table proveedores_ordenes_compra_porcentajes (
    id_orden_compra_porcentaje serial primary key,
    id_orden_compra integer not null,
    id_tipo_porcentaje integer not null,
    valor numeric(19,4) not null,
    version integer not null default 0,

     CONSTRAINT proveedores_ordenes_compra_porcentajes_id_orden_compra_fk FOREIGN KEY (id_orden_compra)
      REFERENCES proveedores_ordenes_compra (id_orden_compra) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
     CONSTRAINT proveedores_ordenes_compra_porcentajes_id_tipo_porcentaje_fk FOREIGN KEY (id_tipo_porcentaje)
      REFERENCES productos_tipos_porcentajes(id_tipo_porcentaje) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);
comment on table proveedores_ordenes_compra_porcentajes is 'Los porcentajes que afectan a la orden de compra en general';


create table proveedores_ordenes_compra_lineas (
    id_orden_compra_linea serial primary key,
    id_orden_compra integer not null,
    id_producto integer not null,
    cantidad_pedida numeric(19,2) not null,
    id_tipo_unidad integer not null,
    cantidad_recibida numeric(19,2) not null,
    precio_compra_unitario numeric(19,4) not null,
    sub_total numeric(19,4) not null,
    version integer not null default 0,
    CONSTRAINT proveedores_ordenes_compra_lineas_id_orden_compra_fk FOREIGN KEY (id_orden_compra)
      REFERENCES proveedores_ordenes_compra (id_orden_compra) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
    CONSTRAINT proveedores_ordenes_compra_lineas_id_producto_fk FOREIGN KEY (id_producto)
      REFERENCES productos (id_producto) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
    CONSTRAINT proveedores_ordenes_compra_lineas_id_tipo_unidad_fk FOREIGN KEY (id_tipo_unidad)
      REFERENCES productos_tipos_unidades(id_tipo_unidad) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION

);

create table proveedores_ordenes_compra_lineas_porcentajes(
    id_orden_compra_linea_porcentaje serial primary key,
    id_orden_compra_linea integer not null,
    id_tipo_porcentaje integer not null,
    valor numeric(19,4) not null,
    version integer not null default 0,

     CONSTRAINT proveedores_ordenes_compra_lineas_porcentajes_id_orden_compra_linea_fk FOREIGN KEY (id_orden_compra_linea)
      REFERENCES proveedores_ordenes_compra_lineas (id_orden_compra_linea) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
     CONSTRAINT proveedores_ordenes_compra_lineas_porcentajes_id_tipo_porcentaje_fk FOREIGN KEY (id_tipo_porcentaje)
      REFERENCES productos_tipos_porcentajes(id_tipo_porcentaje) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);

comment on table proveedores_ordenes_compra_lineas_porcentajes is 'Los porcentajes que afectan al precio unitario de cada línea de la orden de compra';


/**IVA Compras*/
drop table if exists proveedores_comprobantes;
drop table if exists fiscal_libro_iva_compras_lineas;
drop table if exists fiscal_libro_iva_compras;

create table fiscal_libro_iva_compras
(
	id_registro serial not null
		constraint fiscal_libro_iva_compras_pkey
			primary key,
	fecha_factura timestamp not null,
	id_responsabilidad_iva integer not null
		constraint fiscal_libro_iva_compras_id_responsabilidad_iva_fkey
			references fiscal_responsabilidades_iva,
	documento varchar(20) not null,
	letra_factura varchar(2) not null,
	punto_venta_factura varchar(5) not null,
	numero_factura varchar(9) not null,
	total_factura numeric(19,2) not null,
	id_periodo_fiscal integer not null
		constraint fiscal_libro_iva_compras_id_periodo_fiscal_fkey
			references fiscal_periodos_fiscales,
	id_persona integer not null
		constraint fiscal_libro_iva_compras_id_persona_fkey
			references personas,
	id_registro_contable integer
		constraint fk_id_registro_contable
			references contabilidad_registro_contable,
	anulada boolean default false not null,
	version integer default 0 not null,
	codigo_tipo_comprobante char(3) default '001'::bpchar not null
		constraint fk_fiscal_libro_iva_ventas_codigo_tipo_comprobante
			references fiscal_tipos_comprobante,
	cae bigint,
	fecha_vencimiento_cae date,
	importe_neto_no_gravado numeric(19,2) default 0 not null,
	importe_exento numeric(19,2) default 0 not null,
	importe_neto_gravado numeric(19,2) default 0 not null,
	importe_tributos numeric(19,2) default 0 not null,
	importe_iva numeric(19,2) default 0 not null,
	importe_percepcion_iva numeric(19,2) default 0 not null,
	importe_percepcion_ingresos_brutos numeric(19,2) default 0 not null
)
;

create index fk_fiscal_libro_iva_compras_id_periodo_fiscal_fkey_idx
	on fiscal_libro_iva_compras (id_periodo_fiscal)
;

create index fk_fiscal_libro_iva_compras_id_persona_fkey_idx
	on fiscal_libro_iva_compras (id_persona)
;

create index fk_fiscal_libro_iva_compras_id_responsabilidad_iva_fkey_idx
	on fiscal_libro_iva_compras (id_responsabilidad_iva)
;

create index fk_fiscal_libro_iva_compras_codigo_tipo_comprobante_idx
	on fiscal_libro_iva_compras (codigo_tipo_comprobante)
;

create index fk_fiscal_libro_iva_compras_id_registro_contable_idx
	on fiscal_libro_iva_compras (id_registro_contable)
;

create table fiscal_libro_iva_compras_lineas
(
	id_linea_libro serial not null
		constraint fiscal_libro_iva_compras_lineas_pkey
			primary key,
	id_registro integer not null
		constraint fiscal_libro_iva_compras_lineas_id_factura_fkey
			references fiscal_libro_iva_compras,
	id_alicuota_iva integer not null
		constraint fiscal_libro_iva_compras_lineas_id_alicuota_iva_fkey
			references fiscal_alicuotas_iva,
	neto_gravado numeric(19,2),
	no_gravado numeric(19,2),
	importe_iva numeric(19,2),
	version integer default 0 not null
)
;

create index fk_fiscal_libro_iva_compras_lineas_id_alicuota_iva_fkey_idx
	on fiscal_libro_iva_compras_lineas (id_alicuota_iva)
;

create index fk_fiscal_libro_iva_compras_lineas_id_factura_fkey_idx
	on fiscal_libro_iva_compras_lineas (id_registro)
;

/*Comprbantes de proveedores*/

create table proveedores_comprobantes
(
	id_comprobante serial not null
			primary key,
	fecha_comprobante timestamp not null,
	id_usuario integer not null
			references usuarios,
	id_sucursal integer not null
			references sucursales,
	total numeric(19,2) not null,
	id_registro_iva integer
			references fiscal_libro_iva_compras,
	observaciones varchar(1024),
	anulada boolean default false not null,
	id_proveedor integer not null
			references personas,
	version integer not null,
	letra char,
	id_negocio_tipo_comprobante integer default 1 not null
			references negocio_tipos_comprobante
);

create index fk_proveedores_comprobantes_id_negocio_tipo_comprobante_fkey_idx
	on proveedores_comprobantes (id_negocio_tipo_comprobante)
;

create index fk_proveedores_comprobantes_id_proveedor_fkey_idx
	on proveedores_comprobantes (id_proveedor)
;

create index fk_proveedores_comprobantes_id_registro_iva_fkey_idx
	on proveedores_comprobantes (id_registro_iva)
;

create index fk_proveedores_comprobantes_id_sucursal_fkey_idx
	on proveedores_comprobantes (id_sucursal)
;

create index fk_proveedores_comprobantes_id_usuario_fkey_idx
	on proveedores_comprobantes (id_usuario)
;

