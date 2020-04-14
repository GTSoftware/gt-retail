--Tabla de marcas
create table  productos_marcas(
id_marca serial primary key,
version integer  default 0  not null,
nombre_marca  varchar(100) not null);

comment on table productos_marcas is 'Marcas de productos';

insert  into productos_marcas(id_marca,nombre_marca) values (1,'SIN MARCA');

alter table productos add column id_marca integer not null
default 1;

alter table productos add constraint
productos_marcas_fkey foreign key (id_marca) references productos_marcas(id_marca);

