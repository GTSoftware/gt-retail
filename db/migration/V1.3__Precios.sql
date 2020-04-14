drop table productos_porcentajes;


alter table productos add column fecha_ultima_modificacion timestamp without time zone NOT NULL default current_timestamp;
alter table productos add column observaciones varchar(2048);
alter table productos add column costo_final numeric(19,4) not null default 0;


create table productos_tipos_porcentajes(
id_tipo_porcentaje serial primary key,
version integer DEFAULT 0 NOT NULL,
nombre_tipo varchar(50) not null,
porcentaje boolean DEFAULT true NOT NULL);
comment on table productos_tipos_porcentajes is 'Los tipos de porcentaje que se pueden aplicar';

insert  into productos_tipos_porcentajes (version,nombre_tipo,porcentaje) values (0,'BONIF (-) / RECARGOS (+)',true);
insert  into productos_tipos_porcentajes (version,nombre_tipo,porcentaje) values (0,'FLETE',true);
insert  into productos_tipos_porcentajes (version,nombre_tipo,porcentaje) values (0,'INGR. BRUTOS',true);
insert  into productos_tipos_porcentajes (version,nombre_tipo,porcentaje) values (0,'OTROS',true);

create table productos_porcentajes(
    id_producto_porcentaje serial primary key,
    id_producto integer not null references productos (id_producto) on delete cascade ,
    id_tipo_porcentaje integer not null references productos_tipos_porcentajes (id_tipo_porcentaje),
    valor numeric(19,4) not null,
    fecha_modificacion timestamp not null DEFAULT current_timestamp
);

comment on table productos_porcentajes is 'Valor de cada tipo de porcentaje por producto';

