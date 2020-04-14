ALTER TABLE legal_tipos_documento
add column fiscal_codigo_tipo_documento integer;

update legal_tipos_documento set fiscal_codigo_tipo_documento = 96
where id_tipo_documento = 1;
update legal_tipos_documento set fiscal_codigo_tipo_documento = 80
where id_tipo_documento = 2;

alter table fiscal_alicuotas_iva
add column fiscal_codigo_alicuota integer;

--IVA 21%
update fiscal_alicuotas_iva set fiscal_codigo_alicuota = 5
where id_alicuota_iva = 1;
--IVA 10.5%
update fiscal_alicuotas_iva set fiscal_codigo_alicuota = 4
where id_alicuota_iva = 2;
--IVA 27%
update fiscal_alicuotas_iva set fiscal_codigo_alicuota = 6
where id_alicuota_iva = 3;
--IVA exento
update fiscal_alicuotas_iva set fiscal_codigo_alicuota = 2
where id_alicuota_iva = 4;
--IVA no gravado
insert into fiscal_alicuotas_iva (id_alicuota_iva , nombre_alicuota_iva , valor_alicuota , gravar_iva , activo , version,fiscal_codigo_alicuota)
values(5,'NO GRAVADO',0,false,true,0,1);
--IVA 0%
insert into fiscal_alicuotas_iva (id_alicuota_iva , nombre_alicuota_iva , valor_alicuota , gravar_iva , activo , version,fiscal_codigo_alicuota)
values(6,'0%',0,true,true,0,3);
--IVA 5%
insert into fiscal_alicuotas_iva (id_alicuota_iva , nombre_alicuota_iva , valor_alicuota , gravar_iva , activo , version,fiscal_codigo_alicuota)
values(7,'5%',5,true,true,0,8);
--IVA 2.5%
insert into fiscal_alicuotas_iva (id_alicuota_iva , nombre_alicuota_iva , valor_alicuota , gravar_iva , activo , version,fiscal_codigo_alicuota)
values(8,'2.5%',2.5,true,true,0,9);

alter table fiscal_responsabilidades_iva
add column fiscal_codigo_responsable integer;

--Consumidor final
update fiscal_responsabilidades_iva
set fiscal_codigo_responsable = 5
where id_resoponsabildiad_iva = 1;
--Responsable inscripto
update fiscal_responsabilidades_iva
set fiscal_codigo_responsable = 1
where id_resoponsabildiad_iva = 2;
--Responsable monotributo
update fiscal_responsabilidades_iva
set fiscal_codigo_responsable = 6
where id_resoponsabildiad_iva = 3;
--Exento
update fiscal_responsabilidades_iva
set fiscal_codigo_responsable = 4
where id_resoponsabildiad_iva = 4;

create table fiscal_monedas(
    codigo_moneda char(3) primary key,
    nombre_moneda varchar(50) not null);

--Data for table fiscal_monedas
INSERT INTO fiscal_monedas (codigo_moneda,nombre_moneda) VALUES('000','OTRAS MONEDAS');
INSERT INTO fiscal_monedas (codigo_moneda,nombre_moneda) VALUES('PES','PESOS');
INSERT INTO fiscal_monedas (codigo_moneda,nombre_moneda) VALUES('DOL','Dólar ESTADOUNIDENSE');
INSERT INTO fiscal_monedas (codigo_moneda,nombre_moneda) VALUES('002','Dólar EEUU LIBRE');
INSERT INTO fiscal_monedas (codigo_moneda,nombre_moneda) VALUES('003','FRANCOS FRANCESES');
INSERT INTO fiscal_monedas (codigo_moneda,nombre_moneda) VALUES('004','LIRAS ITALIANAS');
INSERT INTO fiscal_monedas (codigo_moneda,nombre_moneda) VALUES('005','PESETAS');
INSERT INTO fiscal_monedas (codigo_moneda,nombre_moneda) VALUES('006','MARCOS ALEMANES');
INSERT INTO fiscal_monedas (codigo_moneda,nombre_moneda) VALUES('007','FLORINES HOLANDESES');
INSERT INTO fiscal_monedas (codigo_moneda,nombre_moneda) VALUES('008','FRANCOS BELGAS');
INSERT INTO fiscal_monedas (codigo_moneda,nombre_moneda) VALUES('009','FRANCOS SUIZOS');
INSERT INTO fiscal_monedas (codigo_moneda,nombre_moneda) VALUES('010','PESOS MEJICANOS');
INSERT INTO fiscal_monedas (codigo_moneda,nombre_moneda) VALUES('011','PESOS URUGUAYOS');
INSERT INTO fiscal_monedas (codigo_moneda,nombre_moneda) VALUES('012','REAL');
INSERT INTO fiscal_monedas (codigo_moneda,nombre_moneda) VALUES('013','ESCUDOS PORTUGUESES');
INSERT INTO fiscal_monedas (codigo_moneda,nombre_moneda) VALUES('014','CORONAS DANESAS');
INSERT INTO fiscal_monedas (codigo_moneda,nombre_moneda) VALUES('015','CORONAS NORUEGAS');
INSERT INTO fiscal_monedas (codigo_moneda,nombre_moneda) VALUES('016','CORONAS SUECAS');
INSERT INTO fiscal_monedas (codigo_moneda,nombre_moneda) VALUES('017','CHELINES AUTRIACOS');
INSERT INTO fiscal_monedas (codigo_moneda,nombre_moneda) VALUES('018','Dólar CANADIENSE');
INSERT INTO fiscal_monedas (codigo_moneda,nombre_moneda) VALUES('019','YENS');
INSERT INTO fiscal_monedas (codigo_moneda,nombre_moneda) VALUES('021','LIBRA ESTERLINA');
INSERT INTO fiscal_monedas (codigo_moneda,nombre_moneda) VALUES('022','MARCOS FINLANDESES');
INSERT INTO fiscal_monedas (codigo_moneda,nombre_moneda) VALUES('023','BOLIVAR (VENEZOLANO)');
INSERT INTO fiscal_monedas (codigo_moneda,nombre_moneda) VALUES('024','CORONA CHECA');
INSERT INTO fiscal_monedas (codigo_moneda,nombre_moneda) VALUES('025','DINAR (YUGOSLAVO)');
INSERT INTO fiscal_monedas (codigo_moneda,nombre_moneda) VALUES('026','Dólar AUSTRALIANO');
INSERT INTO fiscal_monedas (codigo_moneda,nombre_moneda) VALUES('027','DRACMA (GRIEGO)');
INSERT INTO fiscal_monedas (codigo_moneda,nombre_moneda) VALUES('028','FLORIN (ANTILLAS HOLA');
INSERT INTO fiscal_monedas (codigo_moneda,nombre_moneda) VALUES('029','GUARANI');
INSERT INTO fiscal_monedas (codigo_moneda,nombre_moneda) VALUES('030','SHEKEL (ISRAEL)');
INSERT INTO fiscal_monedas (codigo_moneda,nombre_moneda) VALUES('031','PESO BOLIVIANO');
INSERT INTO fiscal_monedas (codigo_moneda,nombre_moneda) VALUES('032','PESO COLOMBIANO');
INSERT INTO fiscal_monedas (codigo_moneda,nombre_moneda) VALUES('033','PESO CHILENO');
INSERT INTO fiscal_monedas (codigo_moneda,nombre_moneda) VALUES('034','RAND (SUDAFRICANO)');
INSERT INTO fiscal_monedas (codigo_moneda,nombre_moneda) VALUES('035','NUEVO SOL PERUANO');
INSERT INTO fiscal_monedas (codigo_moneda,nombre_moneda) VALUES('036','SUCRE (ECUATORIANO)');
INSERT INTO fiscal_monedas (codigo_moneda,nombre_moneda) VALUES('040','LEI RUMANOS');
INSERT INTO fiscal_monedas (codigo_moneda,nombre_moneda) VALUES('041','DERECHOS ESPECIALES DE GIRO');
INSERT INTO fiscal_monedas (codigo_moneda,nombre_moneda) VALUES('042','PESOS DOMINICANOS');
INSERT INTO fiscal_monedas (codigo_moneda,nombre_moneda) VALUES('043','BALBOAS PANAMEÑAS');
INSERT INTO fiscal_monedas (codigo_moneda,nombre_moneda) VALUES('044','CORDOBAS NICARAGÛENSES');
INSERT INTO fiscal_monedas (codigo_moneda,nombre_moneda) VALUES('045','DIRHAM MARROQUÍES');
INSERT INTO fiscal_monedas (codigo_moneda,nombre_moneda) VALUES('046','LIBRAS EGIPCIAS');
INSERT INTO fiscal_monedas (codigo_moneda,nombre_moneda) VALUES('047','RIYALS SAUDITAS');
INSERT INTO fiscal_monedas (codigo_moneda,nombre_moneda) VALUES('048','BRANCOS BELGAS FINANCIERAS');
INSERT INTO fiscal_monedas (codigo_moneda,nombre_moneda) VALUES('049','GRAMOS DE ORO FINO');
INSERT INTO fiscal_monedas (codigo_moneda,nombre_moneda) VALUES('050','LIBRAS IRLANDESAS');
INSERT INTO fiscal_monedas (codigo_moneda,nombre_moneda) VALUES('051','Dólar DE HONG KONG');
INSERT INTO fiscal_monedas (codigo_moneda,nombre_moneda) VALUES('052','Dólar DE SINGAPUR');
INSERT INTO fiscal_monedas (codigo_moneda,nombre_moneda) VALUES('053','Dólar DE JAMAICA');
INSERT INTO fiscal_monedas (codigo_moneda,nombre_moneda) VALUES('054','Dólar DE TAIWAN');
INSERT INTO fiscal_monedas (codigo_moneda,nombre_moneda) VALUES('055','QUETZAL (GUATEMALTECOS)');
INSERT INTO fiscal_monedas (codigo_moneda,nombre_moneda) VALUES('056','FORINT (HUNGRIA)');
INSERT INTO fiscal_monedas (codigo_moneda,nombre_moneda) VALUES('057','BAHT (TAILANDIA)');
INSERT INTO fiscal_monedas (codigo_moneda,nombre_moneda) VALUES('058','ECU');
INSERT INTO fiscal_monedas (codigo_moneda,nombre_moneda) VALUES('059','DINAR KUWAITI');
INSERT INTO fiscal_monedas (codigo_moneda,nombre_moneda) VALUES('060','EURO');
INSERT INTO fiscal_monedas (codigo_moneda,nombre_moneda) VALUES('061','ZLTYS POLACOS');
INSERT INTO fiscal_monedas (codigo_moneda,nombre_moneda) VALUES('062','RUPIAS HINDÚES');
INSERT INTO fiscal_monedas (codigo_moneda,nombre_moneda) VALUES('063','LEMPIRAS HONDUREÑAS');
INSERT INTO fiscal_monedas (codigo_moneda,nombre_moneda) VALUES('064','YUAN (Rep. Pop. China)');

--Tipos de comprobante
create table fiscal_tipos_comprobante (
codigo_tipo_comprobante char(3) primary key,
denominacion_comprobante varchar(100) not null);

--Data for table fiscal_tipos_comprobante
INSERT INTO fiscal_tipos_comprobante (codigo_tipo_comprobante,denominacion_comprobante) VALUES ('001','FACTURAS A');
INSERT INTO fiscal_tipos_comprobante (codigo_tipo_comprobante,denominacion_comprobante) VALUES ('002','NOTAS DE DEBITO A');
INSERT INTO fiscal_tipos_comprobante (codigo_tipo_comprobante,denominacion_comprobante) VALUES ('003','NOTAS DE CREDITO A');
INSERT INTO fiscal_tipos_comprobante (codigo_tipo_comprobante,denominacion_comprobante) VALUES ('004','RECIBOS A');
INSERT INTO fiscal_tipos_comprobante (codigo_tipo_comprobante,denominacion_comprobante) VALUES ('005','NOTAS DE VENTA AL CONTADO A');
INSERT INTO fiscal_tipos_comprobante (codigo_tipo_comprobante,denominacion_comprobante) VALUES ('006','FACTURAS B');
INSERT INTO fiscal_tipos_comprobante (codigo_tipo_comprobante,denominacion_comprobante) VALUES ('007','NOTAS DE DEBITO B');
INSERT INTO fiscal_tipos_comprobante (codigo_tipo_comprobante,denominacion_comprobante) VALUES ('008','NOTAS DE CREDITO B');
INSERT INTO fiscal_tipos_comprobante (codigo_tipo_comprobante,denominacion_comprobante) VALUES ('009','RECIBOS B');
INSERT INTO fiscal_tipos_comprobante (codigo_tipo_comprobante,denominacion_comprobante) VALUES ('010','NOTAS DE VENTA AL CONTADO B');
INSERT INTO fiscal_tipos_comprobante (codigo_tipo_comprobante,denominacion_comprobante) VALUES ('011','FACTURAS C');
INSERT INTO fiscal_tipos_comprobante (codigo_tipo_comprobante,denominacion_comprobante) VALUES ('012','NOTAS DE DEBITO C');
INSERT INTO fiscal_tipos_comprobante (codigo_tipo_comprobante,denominacion_comprobante) VALUES ('013','NOTAS DE CREDITO C');
INSERT INTO fiscal_tipos_comprobante (codigo_tipo_comprobante,denominacion_comprobante) VALUES ('014','DOCUMENTO ADUANERO');
INSERT INTO fiscal_tipos_comprobante (codigo_tipo_comprobante,denominacion_comprobante) VALUES ('015','RECIBOS C');
INSERT INTO fiscal_tipos_comprobante (codigo_tipo_comprobante,denominacion_comprobante) VALUES ('016','NOTAS DE VENTA AL CONTADO C');
INSERT INTO fiscal_tipos_comprobante (codigo_tipo_comprobante,denominacion_comprobante) VALUES ('019','FACTURAS DE EXPORTACION');
INSERT INTO fiscal_tipos_comprobante (codigo_tipo_comprobante,denominacion_comprobante) VALUES ('020','NOTAS DE DEBITO POR OPERACIONES CON EL EXTERIOR');
INSERT INTO fiscal_tipos_comprobante (codigo_tipo_comprobante,denominacion_comprobante) VALUES ('021','NOTAS DE CREDITO POR OPERACIONES CON EL EXTERIOR');
INSERT INTO fiscal_tipos_comprobante (codigo_tipo_comprobante,denominacion_comprobante) VALUES ('022','FACTURAS - PERMISO EXPORTACION SIMPLIFICADO - DTO. 855/97');
INSERT INTO fiscal_tipos_comprobante (codigo_tipo_comprobante,denominacion_comprobante) VALUES ('030','COMPROBANTES DE COMPRA DE BIENES USADOS');
INSERT INTO fiscal_tipos_comprobante (codigo_tipo_comprobante,denominacion_comprobante) VALUES ('031','MANDATO - CONSIGNACION');
INSERT INTO fiscal_tipos_comprobante (codigo_tipo_comprobante,denominacion_comprobante) VALUES ('032','COMPROBANTES PARA RECICLAR MATERIALES');
INSERT INTO fiscal_tipos_comprobante (codigo_tipo_comprobante,denominacion_comprobante) VALUES ('034','COMPROBANTES A DEL APARTADO A  INCISO F  R G  N  1415');
INSERT INTO fiscal_tipos_comprobante (codigo_tipo_comprobante,denominacion_comprobante) VALUES ('035','COMPROBANTES B DEL ANEXO I, APARTADO A, INC. F), RG N° 1415');
INSERT INTO fiscal_tipos_comprobante (codigo_tipo_comprobante,denominacion_comprobante) VALUES ('036','COMPROBANTES C DEL Anexo I, Apartado A, INC.F), R.G. N° 1415');
INSERT INTO fiscal_tipos_comprobante (codigo_tipo_comprobante,denominacion_comprobante) VALUES ('037','NOTAS DE DEBITO O DOCUMENTO EQUIVALENTE QUE CUMPLAN CON LA R.G. N° 1415');
INSERT INTO fiscal_tipos_comprobante (codigo_tipo_comprobante,denominacion_comprobante) VALUES ('038','NOTAS DE CREDITO O DOCMENTO EQUIVALENTE QUE CUMPLAN CON LA R.G. N° 1415');
INSERT INTO fiscal_tipos_comprobante (codigo_tipo_comprobante,denominacion_comprobante) VALUES ('039','OTROS COMPROBANTES A QUE CUMPLEN CON LA R G  1415');
INSERT INTO fiscal_tipos_comprobante (codigo_tipo_comprobante,denominacion_comprobante) VALUES ('040','OTROS COMPROBANTES B QUE CUMPLAN CON LA R.G. N° 1415');
INSERT INTO fiscal_tipos_comprobante (codigo_tipo_comprobante,denominacion_comprobante) VALUES ('041','OTROS COMPROBANTES C QUE CUMPLAN CON LA R.G. N° 1415');
INSERT INTO fiscal_tipos_comprobante (codigo_tipo_comprobante,denominacion_comprobante) VALUES ('050','RECIBO FACTURA A  REGIMEN DE FACTURA DE CREDITO ');
INSERT INTO fiscal_tipos_comprobante (codigo_tipo_comprobante,denominacion_comprobante) VALUES ('051','FACTURAS M');
INSERT INTO fiscal_tipos_comprobante (codigo_tipo_comprobante,denominacion_comprobante) VALUES ('052','NOTAS DE DEBITO M');
INSERT INTO fiscal_tipos_comprobante (codigo_tipo_comprobante,denominacion_comprobante) VALUES ('053','NOTAS DE CREDITO M');
INSERT INTO fiscal_tipos_comprobante (codigo_tipo_comprobante,denominacion_comprobante) VALUES ('054','RECIBOS M');
INSERT INTO fiscal_tipos_comprobante (codigo_tipo_comprobante,denominacion_comprobante) VALUES ('055','NOTAS DE VENTA AL CONTADO M');
INSERT INTO fiscal_tipos_comprobante (codigo_tipo_comprobante,denominacion_comprobante) VALUES ('056','COMPROBANTES M DEL ANEXO I  APARTADO A  INC F   R G  N  1415');
INSERT INTO fiscal_tipos_comprobante (codigo_tipo_comprobante,denominacion_comprobante) VALUES ('057','OTROS COMPROBANTES M QUE CUMPLAN CON LA R G  N  1415');
INSERT INTO fiscal_tipos_comprobante (codigo_tipo_comprobante,denominacion_comprobante) VALUES ('058','CUENTAS DE VENTA Y LIQUIDO PRODUCTO M');
INSERT INTO fiscal_tipos_comprobante (codigo_tipo_comprobante,denominacion_comprobante) VALUES ('059','LIQUIDACIONES M');
INSERT INTO fiscal_tipos_comprobante (codigo_tipo_comprobante,denominacion_comprobante) VALUES ('060','CUENTAS DE VENTA Y LIQUIDO PRODUCTO A');
INSERT INTO fiscal_tipos_comprobante (codigo_tipo_comprobante,denominacion_comprobante) VALUES ('061','CUENTAS DE VENTA Y LIQUIDO PRODUCTO B');
INSERT INTO fiscal_tipos_comprobante (codigo_tipo_comprobante,denominacion_comprobante) VALUES ('063','LIQUIDACIONES A');
INSERT INTO fiscal_tipos_comprobante (codigo_tipo_comprobante,denominacion_comprobante) VALUES ('064','LIQUIDACIONES B');
INSERT INTO fiscal_tipos_comprobante (codigo_tipo_comprobante,denominacion_comprobante) VALUES ('065','NOTAS DE CREDITO DE COMPROBANTES CON COD. 34, 39, 58, 59, 60, 63, 96, 97 ');
INSERT INTO fiscal_tipos_comprobante (codigo_tipo_comprobante,denominacion_comprobante) VALUES ('066','DESPACHO DE IMPORTACION');
INSERT INTO fiscal_tipos_comprobante (codigo_tipo_comprobante,denominacion_comprobante) VALUES ('067','IMPORTACION DE SERVICIOS');
INSERT INTO fiscal_tipos_comprobante (codigo_tipo_comprobante,denominacion_comprobante) VALUES ('068','LIQUIDACION C');
INSERT INTO fiscal_tipos_comprobante (codigo_tipo_comprobante,denominacion_comprobante) VALUES ('070','RECIBOS FACTURA DE CREDITO');
INSERT INTO fiscal_tipos_comprobante (codigo_tipo_comprobante,denominacion_comprobante) VALUES ('071','CREDITO FISCAL POR CONTRIBUCIONES PATRONALES');
INSERT INTO fiscal_tipos_comprobante (codigo_tipo_comprobante,denominacion_comprobante) VALUES ('073','FORMULARIO 1116 RT');
INSERT INTO fiscal_tipos_comprobante (codigo_tipo_comprobante,denominacion_comprobante) VALUES ('074','CARTA DE PORTE PARA EL TRANSPORTE AUTOMOTOR PARA GRANOS');
INSERT INTO fiscal_tipos_comprobante (codigo_tipo_comprobante,denominacion_comprobante) VALUES ('075','CARTA DE PORTE PARA EL TRANSPORTE FERROVIARIO PARA GRANOS');
INSERT INTO fiscal_tipos_comprobante (codigo_tipo_comprobante,denominacion_comprobante) VALUES ('077','');
INSERT INTO fiscal_tipos_comprobante (codigo_tipo_comprobante,denominacion_comprobante) VALUES ('078','');
INSERT INTO fiscal_tipos_comprobante (codigo_tipo_comprobante,denominacion_comprobante) VALUES ('079','');
INSERT INTO fiscal_tipos_comprobante (codigo_tipo_comprobante,denominacion_comprobante) VALUES ('080','COMPROBANTE DIARIO DE CIERRE (ZETA)');
INSERT INTO fiscal_tipos_comprobante (codigo_tipo_comprobante,denominacion_comprobante) VALUES ('081','TIQUE FACTURA A   CONTROLADORES FISCALES');
INSERT INTO fiscal_tipos_comprobante (codigo_tipo_comprobante,denominacion_comprobante) VALUES ('082','TIQUE - FACTURA B');
INSERT INTO fiscal_tipos_comprobante (codigo_tipo_comprobante,denominacion_comprobante) VALUES ('083','TIQUE');
INSERT INTO fiscal_tipos_comprobante (codigo_tipo_comprobante,denominacion_comprobante) VALUES ('084','COMPROBANTE   FACTURA DE SERVICIOS PUBLICOS   INTERESES FINANCIEROS');
INSERT INTO fiscal_tipos_comprobante (codigo_tipo_comprobante,denominacion_comprobante) VALUES ('085','NOTA DE CREDITO   SERVICIOS PUBLICOS   NOTA DE CREDITO CONTROLADORES FISCALES');
INSERT INTO fiscal_tipos_comprobante (codigo_tipo_comprobante,denominacion_comprobante) VALUES ('086','NOTA DE DEBITO   SERVICIOS PUBLICOS');
INSERT INTO fiscal_tipos_comprobante (codigo_tipo_comprobante,denominacion_comprobante) VALUES ('087','OTROS COMPROBANTES - SERVICIOS DEL EXTERIOR');
INSERT INTO fiscal_tipos_comprobante (codigo_tipo_comprobante,denominacion_comprobante) VALUES ('088','OTROS COMPROBANTES - DOCUMENTOS EXCEPTUADOS / REMITO ELECTRONICO ');
INSERT INTO fiscal_tipos_comprobante (codigo_tipo_comprobante,denominacion_comprobante) VALUES ('089','OTROS COMPROBANTES - DOCUMENTOS EXCEPTUADOS - NOTAS DE DEBITO / RESUMEN DE DATOS');
INSERT INTO fiscal_tipos_comprobante (codigo_tipo_comprobante,denominacion_comprobante) VALUES ('090','OTROS COMPROBANTES - DOCUMENTOS EXCEPTUADOS - NOTAS DE CREDITO');
INSERT INTO fiscal_tipos_comprobante (codigo_tipo_comprobante,denominacion_comprobante) VALUES ('091','REMITOS R');
INSERT INTO fiscal_tipos_comprobante (codigo_tipo_comprobante,denominacion_comprobante) VALUES ('092','AJUSTES CONTABLES QUE INCREMENTAN EL DEBITO FISCAL');
INSERT INTO fiscal_tipos_comprobante (codigo_tipo_comprobante,denominacion_comprobante) VALUES ('093','AJUSTES CONTABLES QUE DISMINUYEN EL DEBITO FISCAL');
INSERT INTO fiscal_tipos_comprobante (codigo_tipo_comprobante,denominacion_comprobante) VALUES ('094','AJUSTES CONTABLES QUE INCREMENTAN EL CREDITO FISCAL');
INSERT INTO fiscal_tipos_comprobante (codigo_tipo_comprobante,denominacion_comprobante) VALUES ('095','AJUSTES CONTABLES QUE DISMINUYEN EL CREDITO FISCAL');
INSERT INTO fiscal_tipos_comprobante (codigo_tipo_comprobante,denominacion_comprobante) VALUES ('096','FORMULARIO 1116 B');
INSERT INTO fiscal_tipos_comprobante (codigo_tipo_comprobante,denominacion_comprobante) VALUES ('097','FORMULARIO 1116 C');
INSERT INTO fiscal_tipos_comprobante (codigo_tipo_comprobante,denominacion_comprobante) VALUES ('099','OTROS COMP  QUE NO CUMPLEN CON LA R G  3419 Y SUS MODIF ');
INSERT INTO fiscal_tipos_comprobante (codigo_tipo_comprobante,denominacion_comprobante) VALUES ('101','AJUSTE ANUAL PROVENIENTE DE LA  D J  DEL IVA  POSITIVO ');
INSERT INTO fiscal_tipos_comprobante (codigo_tipo_comprobante,denominacion_comprobante) VALUES ('102','AJUSTE ANUAL PROVENIENTE DE LA  D J  DEL IVA  NEGATIVO ');
INSERT INTO fiscal_tipos_comprobante (codigo_tipo_comprobante,denominacion_comprobante) VALUES ('103','NOTA DE ASIGNACION');
INSERT INTO fiscal_tipos_comprobante (codigo_tipo_comprobante,denominacion_comprobante) VALUES ('104','NOTA DE CREDITO DE ASIGNACION');

ALTER TABLE fiscal_libro_iva_ventas
add column codigo_tipo_comprobante char(3)
not null default '006';

update fiscal_libro_iva_ventas set codigo_tipo_comprobante='001'
where letra_factura ='A';

--Clave foranea a tabla de tipos de comprobantes fiscales
alter table fiscal_libro_iva_ventas
add constraint fk_fiscal_libro_iva_ventas_codigo_tipo_comprobante foreign key
(codigo_tipo_comprobante) references fiscal_tipos_comprobante(codigo_tipo_comprobante)
on update no action
on delete no action;
