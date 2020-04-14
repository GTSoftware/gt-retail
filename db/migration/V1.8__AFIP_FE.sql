create table afip_auth_services(
nombre_servicio varchar(30)  primary key,
token varchar(1024),
sign varchar(255),
fecha_expiracion timestamp,
version integer not null default 0);

comment on table afip_auth_services is 'Parámetros de autorización para cada servicio web de AFIP';

--Parámetros para la configuración
insert into parametros values ('afip.wsaa.endpoint','https://wsaahomo.afip.gov.ar/ws/services/LoginCms','La URL para acceder al servicio de autorización para servicios web de AFIP',0);

insert into parametros values ('afip.wsaa.cert.path','/home/rodrigo/certs/afip/alias.p12','La ruta al certificado en formato pkcs12',0);
insert into parametros values ('afip.wsaa.cert.password','soloio','La clave para descifrar el certificado',0);
insert into parametros values ('afip.wsaa.dn','cn=wsaahomo,o=afip,c=ar,serialNumber=CUIT 33693450239','El DN para acceder al servicio',0);


create table fiscal_puntos_venta(
    nro_punto_venta int primary key,
    activo boolean not null,
    tipo varchar(30) not null,
    descripcion varchar(100) not null,
    id_sucursal integer not null references sucursales(id_sucursal)
    on update no action on delete no action,
    version integer not null default 0
);
comment on table fiscal_puntos_venta is 'Puntos de venta y su tipo para realizar facturas';

alter table fiscal_libro_iva_ventas add column cae bigint;
alter table fiscal_libro_iva_ventas add column fecha_vencimiento_cae date;

insert into parametros values ('afip.wsfe.endpoint','https://wswhomo.afip.gov.ar/wsfev1/service.asmx','El link para el servicio de web de factura electrónica',0);

alter table fiscal_libro_iva_ventas add column importe_neto_no_gravado numeric(19,2) not null default 0;
alter table fiscal_libro_iva_ventas add column importe_exento numeric(19,2) not null default 0;
alter table fiscal_libro_iva_ventas add column importe_neto_gravado numeric(19,2) not null default 0;
alter table fiscal_libro_iva_ventas add column importe_tributos numeric(19,2) not null default 0;
alter table fiscal_libro_iva_ventas add column importe_iva numeric(19,2) not null default 0;
alter table fiscal_libro_iva_ventas alter column total_factura set not null;


insert into parametros values ('empresa.fechainicio','01/01/2000','La fecha de inicio de actividades de la empresa',0);

