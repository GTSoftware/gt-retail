INSERT INTO parametros (nombre_parametro, valor_parametro, descripcion_parametro) VALUES
('producto.descuento-recargo.codigo',	'0',	'El ID del producto que representa un descuento o un recargo');
INSERT INTO parametros (nombre_parametro, valor_parametro, descripcion_parametro) VALUES
('facturacion.impresion.texto.linea1',	'',	'Texto a imprimir en linea 1 de factura');
INSERT INTO parametros (nombre_parametro, valor_parametro, descripcion_parametro) VALUES
('facturacion.impresion.texto.linea2',	'',	'Texto a imprimir en linea 2 de factura');

alter table ventas_lineas
add column descripcion varchar(200) not null default 'NO DESC';
alter table ventas_lineas
add column item integer not null default 0;

update ventas_lineas set descripcion = (select pr.descripcion from productos pr where pr.id_producto = ventas_lineas.id_producto);
