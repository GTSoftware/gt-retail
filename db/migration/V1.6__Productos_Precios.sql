

--El precio de venta estará definido por la lista de precios que se utilice así como la utilidad
create table productos_listas_precios
(id_lista_precio integer primary key,
version integer not null default 0,
nombre_lista varchar(50) not null,
activa boolean not null);

insert into productos_listas_precios (id_lista_precio,version,nombre_lista,activa) values (1,0,'CONTADO',true);


CREATE SEQUENCE productos_listas_precios_id_lista_precio_seq
    START WITH 2
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE productos_listas_precios_id_lista_precio_seq OWNED BY productos_listas_precios.id_lista_precio;

create table productos_precios (
id_producto integer not null references productos(id_producto) on update no action on delete no action,
id_lista_precio integer not null references productos_listas_precios(id_lista_precio) on update no action on delete no action,
utilidad numeric(8,4) not null,
neto numeric(19,4) not null,
precio numeric(19,4) not null,
version integer not null default 0,
fecha_modificacion timestamp not null DEFAULT current_timestamp,
PRIMARY KEY (id_producto, id_lista_precio));

--Establecer los precios antiguos en la lista de precios CONTADO

insert into productos_precios (id_producto,id_lista_precio,utilidad,neto,precio,version) (select prod.id_producto,1,1,1,precio_venta,0 from productos prod);


alter table productos
drop column precio_venta;

alter table productos
drop column utilidad;

--El stock sale de cada depósito
alter table productos
drop column stock_total;

alter table productos_porcentajes add column
version integer not null default 0;

alter table usuarios_grupos add column
version integer not null default 0;

alter table productos alter column costo_adquisicion_neto type numeric(19,4);

alter table fiscal_tipos_comprobante add column version integer not null default 0;

alter table parametros add column version integer not null default 0;

alter table productos
drop column ubicacion;

alter table productos
add column unidades_compra_unidades_venta numeric(19,2) not null default 1;