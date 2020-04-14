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
 * Created: Dec 28, 2018
 */

create table cajas_transferencias(
  id_transferencia serial primary key,
  fecha_transferencia timestamp not null,
  id_caja_origen integer not null references cajas(id_caja),
  id_caja_destino integer not null references cajas(id_caja),
  observaciones varchar(90),
  id_forma_pago integer not null references negocio_formas_pago(id_forma_pago),
  monto numeric(19,2) not null,
  version integer not null default 0
);

comment on table cajas_transferencias is 'Transferencias de valores entre cajas';


alter table recibos_detalle
 add column monto_pagado_con_signo numeric (19,2);

update recibos_detalle
set monto_pagado_con_signo = monto_pagado * (select ntc.signo from negocio_tipos_comprobante ntc
      join comprobantes_pagos cp on cp.id_pago = recibos_detalle.id_comprobante_pago
      join comprobantes c2 on cp.id_comprobante = c2.id_comprobante
      where ntc.id_negocio_tipo_comprobante = c2.id_negocio_tipo_comprobante );

alter table recibos_detalle
alter column monto_pagado_con_signo set not null;


alter table recibos_detalle
  add column redondeo numeric (19,2) not null default 0;

comment on column recibos_detalle.redondeo is 'El monto que se suma o resta al valor correspondiente al pago en concepto de redondeo. Solo el tipo de pago EFECTIVO deberia completar este campo.';

