--Elimino tablas viejas de pagos que no se utilizaban
DROP TABLE IF EXISTS recibos_detalle;
DROP TABLE IF EXISTS recibos;
DROP TABLE IF EXISTS comprobantes_pagos_lineas;

DROP TABLE IF EXISTS comprobantes_pagos;

DROP TABLE IF EXISTS cajas_movimientos;

DROP TABLE IF EXISTS cajas_categorias_movimientos;
DROP TABLE IF EXISTS cajas;
DROP TABLE IF EXISTS negocio_planes_pago_listas;
DROP TABLE IF EXISTS negocio_planes_pago_detalle;
drop table if exists negocio_planes_pago;
drop table if exists cupones;
drop table if exists valores;



--Tabla para manejar la generación de ID en vez de el secuencial
-- create table numeraciones (
--     tabla varchar(255) primary key,
--     id integer not null default 0
-- );

-- comment on table numeraciones is 'Generador de ID para las tablas';

alter table negocio_formas_pago add column IF NOT EXISTS requiere_plan boolean not null default false;
alter table negocio_formas_pago add column IF NOT EXISTS requiere_valores boolean not null default false;

comment on column negocio_formas_pago.requiere_plan is 'Si requiere que tenga al menos un plan asociado para poder ser utilizado';
comment on column negocio_formas_pago.requiere_valores is 'Si la forma de pago requiere el ingreso de valores que la representen';

--Planes de pago, asocian una forma de pago con un plan
create table negocio_planes_pago (
    id_plan serial primary key,
    id_forma_pago integer not null references negocio_formas_pago(id_forma_pago),
    nombre varchar(60) not null,
    fecha_activo_desde timestamp not null,
    fecha_activo_hasta timestamp not null,
    version integer not null default 0
);

comment on table negocio_planes_pago is 'Los planes de pago para cada tipo de pago';
-- insert into numeraciones values ('negocio_planes_pago',0);

--Las listas de precio habilitadas para cada plan
create table negocio_planes_pago_listas (
    id_plan integer not null references negocio_planes_pago(id_plan),
    id_lista_precio integer not null references productos_listas_precios(id_lista_precio),
    CONSTRAINT negocio_planes_pago_listas_pkey PRIMARY KEY (id_plan,id_lista_precio)
);

comment on table negocio_planes_pago_listas is 'Las listas de precio habilitadas para cada plan';

--Detalle del plan de pago
create table negocio_planes_pago_detalle (
    id_detalle_plan serial primary key,
    id_plan integer not null references negocio_planes_pago(id_plan),
    activo boolean not null,
    cuotas integer not null,
    coeficiente_interes numeric (19,4),
    version integer not null default 0
);

comment on table negocio_planes_pago_detalle is 'Detalle del plan de pago';
-- insert into numeraciones values ('negocio_planes_pago_detalle',0);

--Plan de pagos de cada comprobante
create table comprobantes_pagos (
    id_pago serial primary key,
    id_comprobante integer not null references comprobantes(id_comprobante),
    id_forma_pago integer not null references negocio_formas_pago(id_forma_pago),
    id_plan integer references negocio_planes_pago (id_plan),
    id_detalle_plan integer references negocio_planes_pago_detalle (id_detalle_plan),
    monto_pago numeric(19,2) not null,
    monto_pagado numeric(19,2) not null, --El monto pagado hasta el momento
    fecha_pago timestamp, --La fecha en la que se pagó en su totalidad
    fecha_ultimo_pago timestamp, --La fecha en la que se paǵo por última vez
    version integer not null default 0
);

comment on table comprobantes_pagos is 'Los planes de pago elegidos para cada comprobante';
-- insert into numeraciones values ('comprobantes_pagos',0);

create table cajas (
    id_caja serial primary key,
    id_usuario integer not null references usuarios(id_usuario),
    id_sucursal integer not null references sucursales(id_sucursal),
    fecha_apertura timestamp not null,
    fecha_cierre timestamp,
    saldo_inicial numeric(19,2) not null,
    version integer not null default 0
);

comment on table cajas is 'Las cajas asociadas a los usuarios';
-- insert into numeraciones values ('cajas',0);

create table cajas_movimientos (
    id_movimiento_caja serial primary key,
    id_caja integer not null references cajas(id_caja),
    fecha_movimiento timestamp not null,
    monto_movimiento numeric(19,2) not null,
    descripcion varchar(255),
    version integer not null default 0
);

comment on table cajas_movimientos is 'Movimientos de cajas';
-- insert into numeraciones values ('cajas_movimientos',0);

--Los valores son los documentos que representan cupones o cheques.
create table valores (
    id_valor serial primary key,
    version integer not null default 0,
    monto numeric(19,2) not null,
    tipo_valor varchar(45) not null --Columna para poder discriminar el tipo de valor por JPA
);

comment on table valores is 'Son los documentos que representan cupones o cheques.';
-- insert into numeraciones values ('valores',0);

create table cupones (
    id_valor integer primary key references valores(id_valor),
    nro_cupon integer not null,
    codigo_autorizacion integer,
    nro_lote integer,
    fecha_origen timestamp not null,
    fecha_presentacion timestamp,
    fecha_acreditacion timestamp,
    cant_cuotas integer not null,
    notas varchar(255), --Por el momento se puede poner acá la marca de la tarjeta
    --Marca de tarjeta y banco
    coeficiente numeric(19,4)
);

comment on table cupones is 'Cupones de tarjetas de crédito o débito';


create table recibos (
    id_recibo serial primary key,
    fecha_recibo timestamp not null,
    id_persona integer not null references personas (id_persona),
    id_usuario integer not null references usuarios(id_usuario),
    monto_total numeric(19,2),
    observaciones varchar(255),
    id_caja integer not null references cajas(id_caja),
    version integer not null default 0
);

comment on table recibos is 'Recibos de pago de los clientes';
-- insert into numeraciones values ('recibos',0);

create table recibos_detalle (
    id_detalle_recibo serial primary key,
    id_recibo integer not null references recibos (id_recibo),
    id_comprobante_pago integer references comprobantes_pagos (id_pago),
    monto_pagado numeric(19,2) not null,
    id_forma_pago integer not null references negocio_formas_pago(id_forma_pago),
    id_valor integer references valores(id_valor),
    version integer not null default 0
);

comment on table recibos_detalle is 'Detalle de los valores y comprobantes asociados al recibo';
-- insert into numeraciones values ('recibos_detalle',0);

DROP TABLE IF EXISTS comprobantes_estados_historico;
--Arreglos para generación de índices por medio de tabla de numeraciones
-- DROP SEQUENCE bancos_cuenta_corriente_id_movimiento_seq CASCADE; INSERT INTO numeraciones VALUES ('bancos_cuenta_corriente',COALESCE((SELECT MAX(id_movimiento)+1 FROM bancos_cuenta_corriente),1));
-- DROP SEQUENCE bancos_cuentas_id_cuenta_banco_seq CASCADE; INSERT INTO numeraciones VALUES ('bancos_cuentas',COALESCE((SELECT MAX(id_cuenta_banco)+1 FROM bancos_cuentas),1));
-- DROP SEQUENCE bancos_id_banco_seq CASCADE; INSERT INTO numeraciones VALUES ('bancos',COALESCE((SELECT MAX(id_banco)+1 FROM bancos),1));
-- DROP SEQUENCE bancos_tipos_cuenta_id_tipo_cuenta_banco_seq CASCADE; INSERT INTO numeraciones VALUES ('bancos_tipos_cuenta',COALESCE((SELECT MAX(id_tipo_cuenta_banco)+1 FROM bancos_tipos_cuenta),1));
-- DROP SEQUENCE contabilidad_libros_id_libro_seq CASCADE; INSERT INTO numeraciones VALUES ('contabilidad_libros',COALESCE((SELECT MAX(id_libro)+1 FROM contabilidad_libros),1));
-- DROP SEQUENCE contabilidad_monedas_id_moneda_seq CASCADE; INSERT INTO numeraciones VALUES ('contabilidad_monedas',COALESCE((SELECT MAX(id_moneda)+1 FROM contabilidad_monedas),1));
-- DROP SEQUENCE contabilidad_periodos_contables_id_periodo_contable_seq CASCADE; INSERT INTO numeraciones VALUES ('contabilidad_periodos_contables',COALESCE((SELECT MAX(id_periodo_contable)+1 FROM contabilidad_periodos_contables),1));
-- DROP SEQUENCE contabilidad_plan_cuentas_id_cuenta_seq CASCADE; INSERT INTO numeraciones VALUES ('contabilidad_plan_cuentas',COALESCE((SELECT MAX(id_cuenta)+1 FROM contabilidad_plan_cuentas),1));
-- DROP SEQUENCE contabilidad_registro_contable_id_registro_seq CASCADE; INSERT INTO numeraciones VALUES ('contabilidad_registro_contable',COALESCE((SELECT MAX(id_registro)+1 FROM contabilidad_registro_contable),1));
-- DROP SEQUENCE contabilidad_registro_contable_lineas_id_linea_registro_seq CASCADE; INSERT INTO numeraciones VALUES ('contabilidad_registro_contable_lineas',COALESCE((SELECT MAX(id_linea_registro)+1 FROM contabilidad_registro_contable_lineas),1));
-- DROP SEQUENCE contabilidad_tipos_comprobantes_id_tipo_comprobante_seq CASCADE; INSERT INTO numeraciones VALUES ('contabilidad_tipos_comprobantes',COALESCE((SELECT MAX(id_tipo_comprobante)+1 FROM contabilidad_tipos_comprobantes),1));
-- DROP SEQUENCE contabilidad_tipos_cuenta_id_tipo_cuenta_seq CASCADE; INSERT INTO numeraciones VALUES ('contabilidad_tipos_cuenta',COALESCE((SELECT MAX(id_tipo_cuenta)+1 FROM contabilidad_tipos_cuenta),1));
-- DROP SEQUENCE contabilidad_tipos_operacion_id_tipo_operacion_seq CASCADE; INSERT INTO numeraciones VALUES ('contabilidad_tipos_operacion',COALESCE((SELECT MAX(id_tipo_operacion)+1 FROM contabilidad_tipos_operacion),1));
-- DROP SEQUENCE depositos_id_deposito_seq CASCADE; INSERT INTO numeraciones VALUES ('depositos',COALESCE((SELECT MAX(id_deposito)+1 FROM depositos),1));
-- DROP SEQUENCE fiscal_alicuotas_iva_id_alicuota_iva_seq CASCADE; INSERT INTO numeraciones VALUES ('fiscal_alicuotas_iva',COALESCE((SELECT MAX(id_alicuota_iva)+1 FROM fiscal_alicuotas_iva),1));
-- DROP SEQUENCE fiscal_libro_iva_ventas_id_factura_seq CASCADE; INSERT INTO numeraciones VALUES ('fiscal_libro_iva_ventas',COALESCE((SELECT MAX(id_registro)+1 FROM fiscal_libro_iva_ventas),1));
-- DROP SEQUENCE fiscal_libro_iva_ventas_lineas_id_linea_libro_seq CASCADE; INSERT INTO numeraciones VALUES ('fiscal_libro_iva_ventas_lineas',COALESCE((SELECT MAX(id_linea_libro)+1 FROM fiscal_libro_iva_ventas_lineas),1));
-- DROP SEQUENCE fiscal_periodos_fiscales_id_periodo_fiscal_seq CASCADE; INSERT INTO numeraciones VALUES ('fiscal_periodos_fiscales',COALESCE((SELECT MAX(id_periodo_fiscal)+1 FROM fiscal_periodos_fiscales),1));
-- DROP SEQUENCE fiscal_responsabilidades_iva_id_resoponsabildiad_iva_seq CASCADE; INSERT INTO numeraciones VALUES ('fiscal_responsabilidades_iva',COALESCE((SELECT MAX(id_resoponsabildiad_iva)+1 FROM fiscal_responsabilidades_iva),1));
-- DROP SEQUENCE legal_generos_id_genero_seq CASCADE; INSERT INTO numeraciones VALUES ('legal_generos',COALESCE((SELECT MAX(id_genero)+1 FROM legal_generos),1));
-- DROP SEQUENCE legal_tipos_documento_id_tipo_documento_seq CASCADE; INSERT INTO numeraciones VALUES ('legal_tipos_documento',COALESCE((SELECT MAX(id_tipo_documento)+1 FROM legal_tipos_documento),1));
-- DROP SEQUENCE legal_tipos_personeria_id_tipo_personeria_seq CASCADE; INSERT INTO numeraciones VALUES ('legal_tipos_personeria',COALESCE((SELECT MAX(id_tipo_personeria)+1 FROM legal_tipos_personeria),1));
-- DROP SEQUENCE personas_cuenta_corriente_id_movimiento_seq CASCADE; INSERT INTO numeraciones VALUES ('personas_cuenta_corriente',COALESCE((SELECT MAX(id_movimiento)+1 FROM personas_cuenta_corriente),1));
-- DROP SEQUENCE personas_id_persona_seq CASCADE; INSERT INTO numeraciones VALUES ('personas',COALESCE((SELECT MAX(id_persona)+1 FROM personas),1));
-- DROP SEQUENCE personas_imagenes_id_imagen_seq CASCADE; INSERT INTO numeraciones VALUES ('personas_imagenes',COALESCE((SELECT MAX(id_imagen)+1 FROM personas_imagenes),1));
-- DROP SEQUENCE personas_telefonos_id_telefono_seq CASCADE; INSERT INTO numeraciones VALUES ('personas_telefonos',COALESCE((SELECT MAX(id_telefono)+1 FROM personas_telefonos),1));
-- DROP SEQUENCE personas_tipos_imagenes_id_tipo_imagen_seq CASCADE; INSERT INTO numeraciones VALUES ('personas_tipos_imagenes',COALESCE((SELECT MAX(id_tipo_imagen)+1 FROM personas_tipos_imagenes),1));
-- DROP SEQUENCE privilegios_id_privilegio_seq CASCADE; INSERT INTO numeraciones VALUES ('privilegios',COALESCE((SELECT MAX(id_privilegio)+1 FROM privilegios),1));
-- DROP SEQUENCE productos_caracteristicas_id_caracteristica_seq CASCADE; INSERT INTO numeraciones VALUES ('productos_caracteristicas',COALESCE((SELECT MAX(id_caracteristica)+1 FROM productos_caracteristicas),1));
-- DROP SEQUENCE productos_id_producto_seq CASCADE; INSERT INTO numeraciones VALUES ('productos',COALESCE((SELECT MAX(id_producto)+1 FROM productos),1));
-- DROP SEQUENCE productos_imagenes_id_imagen_seq CASCADE; INSERT INTO numeraciones VALUES ('productos_imagenes',COALESCE((SELECT MAX(id_imagen)+1 FROM productos_imagenes),1));
-- DROP SEQUENCE productos_listas_precios_id_lista_precio_seq CASCADE; INSERT INTO numeraciones VALUES ('productos_listas_precios',COALESCE((SELECT MAX(id_lista_precio)+1 FROM productos_listas_precios),1));
-- DROP SEQUENCE productos_marcas_id_marca_seq CASCADE; INSERT INTO numeraciones VALUES ('productos_marcas',COALESCE((SELECT MAX(id_marca)+1 FROM productos_marcas),1));
-- DROP SEQUENCE productos_porcentajes_id_producto_porcentaje_seq CASCADE; INSERT INTO numeraciones VALUES ('productos_porcentajes',COALESCE((SELECT MAX(id_producto_porcentaje)+1 FROM productos_porcentajes),1));
-- DROP SEQUENCE productos_rubros_id_rubro_seq CASCADE; INSERT INTO numeraciones VALUES ('productos_rubros',COALESCE((SELECT MAX(id_rubro)+1 FROM productos_rubros),1));
-- DROP SEQUENCE productos_sub_rubros_id_sub_rubro_seq CASCADE; INSERT INTO numeraciones VALUES ('productos_sub_rubros',COALESCE((SELECT MAX(id_sub_rubro)+1 FROM productos_sub_rubros),1));
-- DROP SEQUENCE productos_tipos_porcentajes_id_tipo_porcentaje_seq CASCADE; INSERT INTO numeraciones VALUES ('productos_tipos_porcentajes',COALESCE((SELECT MAX(id_tipo_porcentaje)+1 FROM productos_tipos_porcentajes),1));
-- DROP SEQUENCE productos_tipos_proveeduria_id_tipo_proveeduria_seq CASCADE; INSERT INTO numeraciones VALUES ('productos_tipos_proveeduria',COALESCE((SELECT MAX(id_tipo_proveeduria)+1 FROM productos_tipos_proveeduria),1));
-- DROP SEQUENCE productos_tipos_unidades_id_tipo_unidad_seq CASCADE; INSERT INTO numeraciones VALUES ('productos_tipos_unidades',COALESCE((SELECT MAX(id_tipo_unidad)+1 FROM productos_tipos_unidades),1));
-- DROP SEQUENCE productos_x_caracteristicas_id_caracteristica_x_producto_seq CASCADE; INSERT INTO numeraciones VALUES ('productos_x_caracteristicas',COALESCE((SELECT MAX(id_caracteristica_x_producto)+1 FROM productos_x_caracteristicas),1));
-- DROP SEQUENCE proveedores_ordenes_compra_id_orden_compra_seq CASCADE; INSERT INTO numeraciones VALUES ('proveedores_ordenes_compra',COALESCE((SELECT MAX(id_orden_compra)+1 FROM proveedores_ordenes_compra),1));
-- DROP SEQUENCE proveedores_ordenes_compra_lineas_id_linea_seq CASCADE; INSERT INTO numeraciones VALUES ('proveedores_ordenes_compra_lineas',COALESCE((SELECT MAX(id_linea)+1 FROM proveedores_ordenes_compra_lineas),1));
-- DROP SEQUENCE stock_movimientos_id_movimiento_stock_seq CASCADE; INSERT INTO numeraciones VALUES ('stock_movimientos',COALESCE((SELECT MAX(id_movimiento_stock)+1 FROM stock_movimientos),1));
-- DROP SEQUENCE stock_movimientos_tipos_id_tipo_movimiento_seq CASCADE; INSERT INTO numeraciones VALUES ('stock_movimientos_tipos',COALESCE((SELECT MAX(id_tipo_movimiento)+1 FROM stock_movimientos_tipos),1));
-- DROP SEQUENCE sucursales_id_sucursal_seq CASCADE; INSERT INTO numeraciones VALUES ('sucursales',COALESCE((SELECT MAX(id_sucursal)+1 FROM sucursales),1));
-- DROP SEQUENCE ubicacion_localidades_id_localidad_seq CASCADE; INSERT INTO numeraciones VALUES ('ubicacion_localidades',COALESCE((SELECT MAX(id_localidad)+1 FROM ubicacion_localidades),1));
-- DROP SEQUENCE ubicacion_paises_id_pais_seq CASCADE; INSERT INTO numeraciones VALUES ('ubicacion_paises',COALESCE((SELECT MAX(id_pais)+1 FROM ubicacion_paises),1));
-- DROP SEQUENCE ubicacion_provincias_id_provincia_seq CASCADE; INSERT INTO numeraciones VALUES ('ubicacion_provincias',COALESCE((SELECT MAX(id_provincia)+1 FROM ubicacion_provincias),1));
-- DROP SEQUENCE usuarios_grupos_id_grupo_seq CASCADE; INSERT INTO numeraciones VALUES ('usuarios_grupos',COALESCE((SELECT MAX(id_grupo)+1 FROM usuarios_grupos),1));
-- DROP SEQUENCE usuarios_id_usuario_seq CASCADE; INSERT INTO numeraciones VALUES ('usuarios',COALESCE((SELECT MAX(id_usuario)+1 FROM usuarios),1));
-- DROP SEQUENCE ventas_condiciones_id_condicion_venta_seq CASCADE; INSERT INTO numeraciones VALUES ('negocio_condiciones_operaciones',COALESCE((SELECT MAX(id_condicion)+1 FROM negocio_condiciones_operaciones),1));
-- DROP SEQUENCE ventas_formas_pago_id_forma_pago_seq CASCADE; INSERT INTO numeraciones VALUES ('negocio_formas_pago',COALESCE((SELECT MAX(id_forma_pago)+1 FROM negocio_formas_pago),1));
-- DROP SEQUENCE ventas_id_venta_seq CASCADE; INSERT INTO numeraciones VALUES ('comprobantes',COALESCE((SELECT MAX(id_comprobante)+1 FROM comprobantes),1));
-- DROP SEQUENCE ventas_lineas_id_linea_venta_seq CASCADE; INSERT INTO numeraciones VALUES ('comprobantes_lineas',COALESCE((SELECT MAX(id_linea_comprobante)+1 FROM comprobantes_lineas),1));


alter table productos_precios drop constraint productos_precios_pkey;
alter table productos_precios drop constraint IF EXISTS productos_precios_unique;

alter table productos_precios add column IF NOT EXISTS productos_precios_id serial primary key;

alter table productos_precios add constraint productos_precios_unique unique (id_producto, id_lista_precio);

--Arqueos de caja
create table caja_arqueos(
    id_arqueo serial primary key,
    fecha_arqueo timestamp not null,
    id_caja integer not null references cajas(id_caja),
    id_usuario_arqueo integer not null references usuarios(id_usuario),
    version integer not null default 0,
    id_usuario_control integer references usuarios(id_usuario),
    fecha_control timestamp,
    saldo_inicial numeric(19,2) not null,
    saldo_final numeric(19,2) not null,
    id_sucursal integer not null references sucursales(id_sucursal),
    observaciones_control varchar(500)
);

comment on table caja_arqueos is 'Arqueos de caja realizadados por los cajeros. Delimitan un cierre y apertura de caja.';
comment on column caja_arqueos.id_usuario_arqueo is 'El usuario que realiza el arqueo.';
comment on column caja_arqueos.id_usuario_control is 'El usuario que controló el arqueo.';
comment on column caja_arqueos.fecha_control is 'La fecha de control.';
comment on column caja_arqueos.observaciones_control is 'Notas aclaratorias dejadas por el usuario de control.';

CREATE INDEX fk_caja_arqueos_id_caja_fkey_idx ON caja_arqueos (id_caja);
CREATE INDEX fk_caja_arqueos_id_usuario_arqueo_fkey_idx ON caja_arqueos (id_usuario_arqueo);
CREATE INDEX fk_caja_arqueos_id_usuario_control_fkey_idx ON caja_arqueos (id_usuario_control);
CREATE INDEX fk_caja_arqueos_id_sucursal_fkey_idx ON caja_arqueos (id_sucursal);

create table caja_arqueos_detalle(
    id_arqueo_detalle serial primary key,
    id_arqueo integer not null references caja_arqueos(id_arqueo),
    version integer not null default 0,
    id_forma_pago integer not null references negocio_formas_pago(id_forma_pago),
    monto_sistema numeric(19,2) not null,
    monto_declarado numeric(19,2) not null,
    diferencia numeric(19,2) not null,
    descargo varchar(200)
);

comment on table caja_arqueos_detalle is 'Detalle del arqueo.';
comment on column caja_arqueos_detalle.monto_sistema is 'El monto calculado por el sistema para la forma de pago.';
comment on column caja_arqueos_detalle.monto_declarado is 'El monto declarado (fisico) por el usuario para la forma de pago.';
comment on column caja_arqueos_detalle.diferencia is 'La diferencia con el físico.';
comment on column caja_arqueos_detalle.descargo is 'La explicación del usuario por la diferencia.';

CREATE INDEX fk_caja_arqueos_detalle_id_arqueo_fkey_idx ON caja_arqueos_detalle (id_arqueo);
CREATE INDEX fk_caja_arqueos_detalle_id_forma_pago_fkey_idx ON caja_arqueos_detalle (id_forma_pago);


