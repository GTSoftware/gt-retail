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
 * Created: Jul 14, 2017
 */

--Crear las funciones para actualizar las existencias de la tabla productos_x_depositos

CREATE OR REPLACE FUNCTION actualizar_existencia(
    iddepo integer,
    idprod integer,
    cantidad numeric(19,2),
    signo integer)
  RETURNS integer AS
$BODY$
DECLARE
--   iddepo alias for $1;
--   idprod alias for $2;
--   cantidad alias for $3;
--   signo alias for $4;
  existencia record;
  rta int4;
BEGIN
  -- Se recibe iddepo e idprod y se fija si existe un registro con esas claves en la tabla productos_x_depositos.
  -- Si no existe crea el registro con la cantidad pasada como parametro y mult. por el signo.
  -- Si existe, entonces suma o resta (depende del signo(1 o -1)) la cantidad recibida.
  rta = 0;
  IF (iddepo IS NOT NULL) THEN
    SELECT * INTO existencia FROM productos_x_depositos WHERE id_deposito = iddepo AND id_producto = idprod;
    IF (NOT FOUND) THEN
      INSERT INTO productos_x_depositos (id_producto,id_deposito,stock) VALUES (idprod,iddepo,(signo * COALESCE(cantidad,0)));
    ELSE
      UPDATE productos_x_depositos SET stock = stock + (signo * COALESCE(cantidad,0)), version=version + 1 WHERE existencia.id_producto_x_deposito = productos_x_depositos.id_producto_x_deposito
      AND productos_x_depositos.version = existencia.version;
       IF (NOT FOUND) THEN
        RAISE EXCEPTION 'Los datos fueron modificados mientras mientras se estaba haciendo la actualizacion de stock';
       END IF;
    END IF;
  END IF;
  return rta;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE;

  --Funcion para aplicar los movimientos de stock

CREATE OR REPLACE FUNCTION aplicar_movimiento(
    idRemito integer,
    idDepoOrigen integer,
    idDepoDestino integer,
    idSalida integer,
    signo integer)
  RETURNS integer AS
$BODY$
DECLARE
 -- idRemito alias for $1;
 -- idDepoOrigen alias for $2;
 -- idDepoDestino alias for $3;
 -- idSalida alias for $4;
 -- signo alias for $5;
  arts record;
  rta int4;
  aux int4;
BEGIN
  /*Esta func. originalmente fue pensada para ser llamada ante cambios de tablas involucradas con remitos y que no sean
  la tabla remitos_detalle.*/
  rta = 0;
  -- Quitar la existencia del origen
  IF (((idDepoDestino IS NOT NULL) OR (idSalida IS NOT NULL)) AND (idDepoOrigen IS NOT NULL)) THEN
   FOR arts IN SELECT * FROM remitos_detalle WHERE id_remito = idRemito LOOP
     rta = actualizar_existencia(idDepoOrigen, arts.id_producto, arts.cantidad, (-1 * signo));
   END LOOP;
  END IF;
  -- Agregar la existencia al destino
  IF (idDepoDestino IS NOT NULL) THEN
   FOR arts IN SELECT * FROM remitos_detalle WHERE id_remito = idRemito LOOP
     rta = actualizar_existencia(idDepoDestino, arts.id_producto, arts.cantidad, signo);
   END LOOP;
  END IF;
  -- Actualizar situación de los lotes referenciados en los detalles del remito
--  FOR arts IN SELECT * FROM remitos_detalles WHERE id_remito = idRemito AND id_lote IS NOT NULL LOOP
--    aux = lote_actualizar_datos(arts.id_lote);
--  END LOOP;
  return rta;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE;

  --Funcion de trigger para ser llamada ante el agregado o borrado de una recepción

  CREATE OR REPLACE FUNCTION actualizar_existencias_remitos_recep()
  RETURNS trigger AS
$BODY$
DECLARE
   ult_recep RECORD;
   idDepoOrigen int4;
   aux int4;
   idremito int4;
   signo int4;
 BEGIN
   IF (TG_OP = 'INSERT') THEN
    idremito = NEW.id_remito;
   ELSE
    idremito = OLD.id_remito;
   END IF;
   IF (TG_WHEN = 'BEFORE') THEN
     signo = -1; --Volver hacia atras la existencia que habia antes.
   ELSE
     signo = 1; --Aplicar la existencia que hay ahora.
   END IF;
   --Buscar destino
   SELECT id_deposito,id_persona INTO ult_recep FROM remitos_recepciones WHERE id_remito = idremito order by id_recepcion desc limit 1;
   --Buscar origen
   SELECT id_origen_interno INTO idDepoOrigen FROM remitos WHERE id_remito = idremito;
   --Mover existencias
   aux = aplicar_movimiento(idremito,idDepoOrigen,ult_recep.id_deposito,ult_recep.id_persona,signo);
   --Terminar
   IF (TG_OP = 'INSERT' OR TG_OP = 'UPDATE') THEN
     RETURN NEW;
   ELSE
     RETURN OLD;
   END IF;
 END;$BODY$
  LANGUAGE plpgsql VOLATILE;


--Creación de los triggers
--Quizas se pueda resolver dentro de un solo método y llamando a la función en el after pero estoy cansado como para pensar ahora

--Lo llamo luego de la actualización para que aplique los cambios en el stock
  CREATE TRIGGER t_remitos_recep_existencias_after
  AFTER INSERT OR UPDATE OR DELETE
  ON remitos_recepciones
  FOR EACH ROW
  EXECUTE PROCEDURE actualizar_existencias_remitos_recep();

  --Lo llamo antes de la actualización para que vuelva atrás los cambios realizados por la recepción
  CREATE TRIGGER t_remitos_recep_existencias_before
  BEFORE INSERT OR UPDATE OR DELETE
  ON remitos_recepciones
  FOR EACH ROW
  EXECUTE PROCEDURE actualizar_existencias_remitos_recep();

--Quito esta columna por quedar obsoleta
ALTER TABLE productos DROP COLUMN stock_actual;
