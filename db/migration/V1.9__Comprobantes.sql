create table negocio_tipos_comprobante(
    id_negocio_tipo_comprobante integer primary key,
    nombre_comprobante varchar(30) not null,
    signo numeric(1,0) not null check (signo = 1 OR signo =-1),
    activo boolean not null,
    version integer not null default 0
);

comment on table negocio_tipos_comprobante is 'Los tipos de comprobante soportados por el sistema (Facturas, Notas de Crédito, etc)';

insert into negocio_tipos_comprobante values (1,'FACTURA',1,true,0);
insert into negocio_tipos_comprobante values (2,'NOTA DE CREDITO',-1,true,0);
insert into negocio_tipos_comprobante values (3,'NOTA DE DEBITO',1,true,0);
insert into negocio_tipos_comprobante values (4,'PRESUPUESTO',1,true,0);

alter table fiscal_tipos_comprobante add column letra character(1);
alter table fiscal_tipos_comprobante add column id_negocio_tipo_comprobante integer
references negocio_tipos_comprobante(id_negocio_tipo_comprobante);

update fiscal_tipos_comprobante set letra = 'B' where codigo_tipo_comprobante IN ('006','007','008');
update fiscal_tipos_comprobante set letra = 'A' where codigo_tipo_comprobante IN ('001','002','003');
update fiscal_tipos_comprobante set id_negocio_tipo_comprobante = 1 where codigo_tipo_comprobante IN ('001','006');
update fiscal_tipos_comprobante set id_negocio_tipo_comprobante = 2 where codigo_tipo_comprobante IN ('003','008');
update fiscal_tipos_comprobante set id_negocio_tipo_comprobante = 3 where codigo_tipo_comprobante IN ('002','007');

alter table ventas rename to comprobantes;

alter table comprobantes rename column id_venta to id_comprobante;
alter table comprobantes rename column fecha_venta to fecha_comprobante;
alter table comprobantes rename column id_condicion_venta to id_condicion_comprobante;

alter table comprobantes add column letra character(1);
alter table comprobantes add column id_negocio_tipo_comprobante integer not null default 1 references negocio_tipos_comprobante(id_negocio_tipo_comprobante);

DROP TABLE ventas_remitos_lineas;
DROP TABLE ventas_remitos;

alter table ventas_lineas rename to comprobantes_lineas;

alter table comprobantes_lineas rename column id_linea_venta to id_linea_comprobante;
alter table comprobantes_lineas rename column id_venta to id_comprobante;
alter table comprobantes_lineas rename column precio_venta_unitario to precio_unitario;
alter table comprobantes_lineas drop column id_linea_venta_referencia;


alter table fiscal_libro_iva_ventas rename column id_factura to id_registro;

alter table fiscal_libro_iva_ventas_lineas rename column id_factura to id_registro;

drop table ventas_cargos_fijos;

alter table ventas_estados rename to comprobantes_estados;

alter table ventas_estados_historico rename to comprobantes_estados_historico;

alter table comprobantes_estados_historico rename column id_venta to id_comprobante;

alter table ventas_pagos rename to comprobantes_pagos;

alter table comprobantes_pagos rename column id_pago_venta to id_pago_comprobante;

alter table ventas_pagos_lineas rename to comprobantes_pagos_lineas;

alter table comprobantes_pagos_lineas rename column id_pago_venta to id_pago_comprobante;

alter table comprobantes_pagos_lineas rename column id_venta to id_comprobante;
