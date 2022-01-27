--
-- PostgreSQL database dump
--

-- Dumped from database version 13.4
-- Dumped by pg_dump version 14.1 (Debian 14.1-1.pgdg100+1)

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- Name: actualizar_existencia(integer, integer, numeric, integer); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION public.actualizar_existencia(iddepo integer, idprod integer, cantidad numeric, signo integer) RETURNS integer
    LANGUAGE plpgsql
    AS $_$
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
$_$;


--
-- Name: actualizar_existencias_remitos_recep(); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION public.actualizar_existencias_remitos_recep() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
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
END;$$;


--
-- Name: aplicar_movimiento(integer, integer, integer, integer, integer); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION public.aplicar_movimiento(idremito integer, iddepoorigen integer, iddepodestino integer, idsalida integer, signo integer) RETURNS integer
    LANGUAGE plpgsql
    AS $_$
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
$_$;


SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: afip_auth_services; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.afip_auth_services (
                                           nombre_servicio character varying(30) NOT NULL,
                                           token character varying(1024),
                                           sign character varying(255),
                                           fecha_expiracion timestamp without time zone,
                                           version integer DEFAULT 0 NOT NULL
);


--
-- Name: TABLE afip_auth_services; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.afip_auth_services IS 'Parámetros de autorización para cada servicio web de AFIP';


--
-- Name: bancos; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.bancos (
                               id_banco integer NOT NULL,
                               razon_social character varying(200) NOT NULL,
                               observaciones character varying(1024),
                               version integer DEFAULT 0 NOT NULL,
                               id_persona integer
);


--
-- Name: bancos_cuenta_corriente; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.bancos_cuenta_corriente (
                                                id_movimiento integer NOT NULL,
                                                id_cuenta_banco integer NOT NULL,
                                                fecha_movimiento timestamp without time zone NOT NULL,
                                                importe_movimiento numeric(19,2) NOT NULL,
                                                descripcion_movimiento character varying(200) NOT NULL,
                                                id_registro_contable integer NOT NULL,
                                                version integer DEFAULT 0 NOT NULL
);


--
-- Name: bancos_cuenta_corriente_id_movimiento_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.bancos_cuenta_corriente_id_movimiento_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: bancos_cuenta_corriente_id_movimiento_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.bancos_cuenta_corriente_id_movimiento_seq OWNED BY public.bancos_cuenta_corriente.id_movimiento;


--
-- Name: bancos_cuentas; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.bancos_cuentas (
                                       id_cuenta_banco integer NOT NULL,
                                       id_banco integer NOT NULL,
                                       descripcion_cuenta character varying(200),
                                       numero_cuenta character varying(60) NOT NULL,
                                       numero_sucursal character varying(10),
                                       cbu character varying(22),
                                       activo boolean NOT NULL,
                                       fecha_apertura timestamp without time zone,
                                       id_moneda integer NOT NULL,
                                       id_tipo_cuenta_banco integer NOT NULL,
                                       version integer DEFAULT 0 NOT NULL
);


--
-- Name: bancos_cuentas_id_cuenta_banco_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.bancos_cuentas_id_cuenta_banco_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: bancos_cuentas_id_cuenta_banco_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.bancos_cuentas_id_cuenta_banco_seq OWNED BY public.bancos_cuentas.id_cuenta_banco;


--
-- Name: bancos_id_banco_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.bancos_id_banco_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: bancos_id_banco_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.bancos_id_banco_seq OWNED BY public.bancos.id_banco;


--
-- Name: bancos_tipos_cuenta; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.bancos_tipos_cuenta (
                                            id_tipo_cuenta_banco integer NOT NULL,
                                            nombre_tipo_cuenta character varying(60) NOT NULL,
                                            version integer DEFAULT 0 NOT NULL
);


--
-- Name: bancos_tipos_cuenta_id_tipo_cuenta_banco_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.bancos_tipos_cuenta_id_tipo_cuenta_banco_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: bancos_tipos_cuenta_id_tipo_cuenta_banco_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.bancos_tipos_cuenta_id_tipo_cuenta_banco_seq OWNED BY public.bancos_tipos_cuenta.id_tipo_cuenta_banco;


--
-- Name: caja_arqueos; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.caja_arqueos (
                                     id_arqueo integer NOT NULL,
                                     fecha_arqueo timestamp without time zone NOT NULL,
                                     id_caja integer NOT NULL,
                                     id_usuario_arqueo integer NOT NULL,
                                     version integer DEFAULT 0 NOT NULL,
                                     id_usuario_control integer,
                                     fecha_control timestamp without time zone,
                                     saldo_inicial numeric(19,2) NOT NULL,
                                     saldo_final numeric(19,2) NOT NULL,
                                     id_sucursal integer NOT NULL,
                                     observaciones_control character varying(500)
);


--
-- Name: TABLE caja_arqueos; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.caja_arqueos IS 'Arqueos de caja realizadados por los cajeros. Delimitan un cierre y apertura de caja.';


--
-- Name: COLUMN caja_arqueos.id_usuario_arqueo; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.caja_arqueos.id_usuario_arqueo IS 'El usuario que realiza el arqueo.';


--
-- Name: COLUMN caja_arqueos.id_usuario_control; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.caja_arqueos.id_usuario_control IS 'El usuario que controló el arqueo.';


--
-- Name: COLUMN caja_arqueos.fecha_control; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.caja_arqueos.fecha_control IS 'La fecha de control.';


--
-- Name: COLUMN caja_arqueos.observaciones_control; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.caja_arqueos.observaciones_control IS 'Notas aclaratorias dejadas por el usuario de control.';


--
-- Name: caja_arqueos_detalle; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.caja_arqueos_detalle (
                                             id_arqueo_detalle integer NOT NULL,
                                             id_arqueo integer NOT NULL,
                                             version integer DEFAULT 0 NOT NULL,
                                             id_forma_pago integer NOT NULL,
                                             monto_sistema numeric(19,2) NOT NULL,
                                             monto_declarado numeric(19,2) NOT NULL,
                                             diferencia numeric(19,2) NOT NULL,
                                             descargo character varying(200)
);


--
-- Name: TABLE caja_arqueos_detalle; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.caja_arqueos_detalle IS 'Detalle del arqueo.';


--
-- Name: COLUMN caja_arqueos_detalle.monto_sistema; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.caja_arqueos_detalle.monto_sistema IS 'El monto calculado por el sistema para la forma de pago.';


--
-- Name: COLUMN caja_arqueos_detalle.monto_declarado; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.caja_arqueos_detalle.monto_declarado IS 'El monto declarado (fisico) por el usuario para la forma de pago.';


--
-- Name: COLUMN caja_arqueos_detalle.diferencia; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.caja_arqueos_detalle.diferencia IS 'La diferencia con el físico.';


--
-- Name: COLUMN caja_arqueos_detalle.descargo; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.caja_arqueos_detalle.descargo IS 'La explicación del usuario por la diferencia.';


--
-- Name: caja_arqueos_detalle_id_arqueo_detalle_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.caja_arqueos_detalle_id_arqueo_detalle_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: caja_arqueos_detalle_id_arqueo_detalle_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.caja_arqueos_detalle_id_arqueo_detalle_seq OWNED BY public.caja_arqueos_detalle.id_arqueo_detalle;


--
-- Name: caja_arqueos_id_arqueo_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.caja_arqueos_id_arqueo_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: caja_arqueos_id_arqueo_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.caja_arqueos_id_arqueo_seq OWNED BY public.caja_arqueos.id_arqueo;


--
-- Name: cajas; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.cajas (
                              id_caja integer NOT NULL,
                              id_usuario integer NOT NULL,
                              id_sucursal integer NOT NULL,
                              fecha_apertura timestamp without time zone NOT NULL,
                              fecha_cierre timestamp without time zone,
                              saldo_inicial numeric(19,2) NOT NULL,
                              version integer DEFAULT 0 NOT NULL
);


--
-- Name: TABLE cajas; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.cajas IS 'Las cajas asociadas a los usuarios';


--
-- Name: cajas_id_caja_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.cajas_id_caja_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: cajas_id_caja_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.cajas_id_caja_seq OWNED BY public.cajas.id_caja;


--
-- Name: cajas_movimientos; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.cajas_movimientos (
                                          id_movimiento_caja integer NOT NULL,
                                          id_caja integer NOT NULL,
                                          fecha_movimiento timestamp without time zone NOT NULL,
                                          monto_movimiento numeric(19,2) NOT NULL,
                                          descripcion character varying(255),
                                          version integer DEFAULT 0 NOT NULL
);


--
-- Name: TABLE cajas_movimientos; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.cajas_movimientos IS 'Movimientos de cajas';


--
-- Name: cajas_movimientos_id_movimiento_caja_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.cajas_movimientos_id_movimiento_caja_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: cajas_movimientos_id_movimiento_caja_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.cajas_movimientos_id_movimiento_caja_seq OWNED BY public.cajas_movimientos.id_movimiento_caja;


--
-- Name: cajas_transferencias; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.cajas_transferencias (
                                             id_transferencia integer NOT NULL,
                                             fecha_transferencia timestamp without time zone NOT NULL,
                                             id_caja_origen integer NOT NULL,
                                             id_caja_destino integer NOT NULL,
                                             observaciones character varying(90),
                                             id_forma_pago integer NOT NULL,
                                             monto numeric(19,2) NOT NULL,
                                             version integer DEFAULT 0 NOT NULL
);


--
-- Name: TABLE cajas_transferencias; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.cajas_transferencias IS 'Transferencias de valores entre cajas';


--
-- Name: cajas_transferencias_id_transferencia_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.cajas_transferencias_id_transferencia_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: cajas_transferencias_id_transferencia_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.cajas_transferencias_id_transferencia_seq OWNED BY public.cajas_transferencias.id_transferencia;


--
-- Name: cheques_terceros; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.cheques_terceros (
                                         id_valor integer NOT NULL,
                                         nro_cheque character varying(30) NOT NULL,
                                         fecha_origen date NOT NULL,
                                         notas character varying(255),
                                         cuit_originante character varying(11) NOT NULL,
                                         razon_social_originante character varying(200) NOT NULL,
                                         fecha_vencimiento date NOT NULL,
                                         id_banco integer NOT NULL,
                                         fecha_cobro timestamp without time zone
);


--
-- Name: comprobantes; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.comprobantes (
                                     id_comprobante integer NOT NULL,
                                     fecha_comprobante timestamp without time zone NOT NULL,
                                     id_usuario integer NOT NULL,
                                     id_sucursal integer NOT NULL,
                                     total numeric(19,2) NOT NULL,
                                     id_condicion_comprobante integer NOT NULL,
                                     saldo numeric(19,2) NOT NULL,
                                     id_registro_iva integer,
                                     observaciones character varying(1024),
                                     anulada boolean DEFAULT false NOT NULL,
                                     id_persona integer NOT NULL,
                                     id_estado integer NOT NULL,
                                     version integer NOT NULL,
                                     remitente character varying(100),
                                     nroremito character varying(100),
                                     letra character(1),
                                     id_negocio_tipo_comprobante integer DEFAULT 1 NOT NULL
);


--
-- Name: comprobantes_estados; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.comprobantes_estados (
                                             id_estado integer NOT NULL,
                                             nombre_estado character varying(20) NOT NULL,
                                             version integer DEFAULT 0 NOT NULL
);


--
-- Name: comprobantes_lineas; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.comprobantes_lineas (
                                            id_linea_comprobante integer NOT NULL,
                                            id_comprobante integer NOT NULL,
                                            id_producto integer NOT NULL,
                                            precio_unitario numeric(19,4) NOT NULL,
                                            cantidad numeric(19,2) NOT NULL,
                                            sub_total numeric(19,2) NOT NULL,
                                            costo_neto_unitario numeric(19,4) NOT NULL,
                                            costo_bruto_unitario numeric(19,4) NOT NULL,
                                            cantidad_entregada numeric(19,2) NOT NULL,
                                            version integer DEFAULT 0 NOT NULL,
                                            descripcion character varying(200) DEFAULT 'NO DESC'::character varying NOT NULL,
                                            item integer DEFAULT 0 NOT NULL
);


--
-- Name: comprobantes_pagos; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.comprobantes_pagos (
                                           id_pago integer NOT NULL,
                                           id_comprobante integer NOT NULL,
                                           id_forma_pago integer NOT NULL,
                                           id_plan integer,
                                           id_detalle_plan integer,
                                           monto_pago numeric(19,2) NOT NULL,
                                           monto_pagado numeric(19,2) NOT NULL,
                                           fecha_pago timestamp without time zone,
                                           fecha_ultimo_pago timestamp without time zone,
                                           version integer DEFAULT 0 NOT NULL
);


--
-- Name: TABLE comprobantes_pagos; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.comprobantes_pagos IS 'Los planes de pago elegidos para cada comprobante';


--
-- Name: comprobantes_pagos_id_pago_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.comprobantes_pagos_id_pago_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: comprobantes_pagos_id_pago_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.comprobantes_pagos_id_pago_seq OWNED BY public.comprobantes_pagos.id_pago;


--
-- Name: contabilidad_libros; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.contabilidad_libros (
                                            id_libro integer NOT NULL,
                                            nombre_libro character varying(100) NOT NULL,
                                            descripcion_libro character varying(255),
                                            version integer DEFAULT 0 NOT NULL
);


--
-- Name: contabilidad_libros_id_libro_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.contabilidad_libros_id_libro_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: contabilidad_libros_id_libro_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.contabilidad_libros_id_libro_seq OWNED BY public.contabilidad_libros.id_libro;


--
-- Name: contabilidad_monedas; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.contabilidad_monedas (
                                             id_moneda integer NOT NULL,
                                             nombre_moneda character varying(100) NOT NULL,
                                             nombre_corto_moneda character varying(10),
                                             simbolo_moneda character varying(5),
                                             version integer DEFAULT 0 NOT NULL
);


--
-- Name: contabilidad_monedas_id_moneda_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.contabilidad_monedas_id_moneda_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: contabilidad_monedas_id_moneda_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.contabilidad_monedas_id_moneda_seq OWNED BY public.contabilidad_monedas.id_moneda;


--
-- Name: contabilidad_periodos_contables; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.contabilidad_periodos_contables (
                                                        id_periodo_contable integer NOT NULL,
                                                        nombre_periodo character varying(100) NOT NULL,
                                                        fecha_inicio_periodo timestamp without time zone NOT NULL,
                                                        fecha_fin_periodo timestamp without time zone NOT NULL,
                                                        version integer DEFAULT 0 NOT NULL
);


--
-- Name: contabilidad_periodos_contables_id_periodo_contable_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.contabilidad_periodos_contables_id_periodo_contable_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: contabilidad_periodos_contables_id_periodo_contable_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.contabilidad_periodos_contables_id_periodo_contable_seq OWNED BY public.contabilidad_periodos_contables.id_periodo_contable;


--
-- Name: contabilidad_plan_cuentas; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.contabilidad_plan_cuentas (
                                                  id_cuenta integer NOT NULL,
                                                  nombre_cuenta character varying(200) NOT NULL,
                                                  numero_cuenta character varying(100),
                                                  id_cuenta_padre integer,
                                                  descripcion_cuenta character varying(2000),
                                                  cuenta_rubro boolean NOT NULL,
                                                  id_tipo_cuenta integer DEFAULT 1 NOT NULL,
                                                  version integer DEFAULT 0 NOT NULL
);


--
-- Name: contabilidad_plan_cuentas_id_cuenta_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.contabilidad_plan_cuentas_id_cuenta_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: contabilidad_plan_cuentas_id_cuenta_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.contabilidad_plan_cuentas_id_cuenta_seq OWNED BY public.contabilidad_plan_cuentas.id_cuenta;


--
-- Name: contabilidad_registro_contable; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.contabilidad_registro_contable (
                                                       id_registro integer NOT NULL,
                                                       id_libro integer NOT NULL,
                                                       fecha_proceso timestamp without time zone NOT NULL,
                                                       usuario character varying(100),
                                                       id_tipo_comprobante integer NOT NULL,
                                                       letra_comprobante character varying(10),
                                                       punto_venta_comprobante character varying(10),
                                                       numero_comprobante character varying(100),
                                                       cuit_emisor_comprobante character varying(11),
                                                       cuit_receptor_comprobante character varying(11),
                                                       fecha_comprobante timestamp without time zone,
                                                       fecha_vencimiento timestamp without time zone,
                                                       id_periodo_contable integer,
                                                       id_periodo_fiscal integer,
                                                       concepto_comprobante character varying(2000),
                                                       id_tipo_operacion integer,
                                                       version integer DEFAULT 0 NOT NULL
);


--
-- Name: contabilidad_registro_contable_id_registro_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.contabilidad_registro_contable_id_registro_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: contabilidad_registro_contable_id_registro_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.contabilidad_registro_contable_id_registro_seq OWNED BY public.contabilidad_registro_contable.id_registro;


--
-- Name: contabilidad_registro_contable_lineas; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.contabilidad_registro_contable_lineas (
                                                              id_linea_registro integer NOT NULL,
                                                              id_registro_contable integer NOT NULL,
                                                              id_cuenta integer NOT NULL,
                                                              descripcion_linea character varying(1024),
                                                              cantidad numeric(19,2) NOT NULL,
                                                              unidad_medida character varying(20),
                                                              fecha_vencimiento timestamp without time zone NOT NULL,
                                                              importe_debe numeric(19,2) NOT NULL,
                                                              importe_haber numeric(19,2) NOT NULL,
                                                              version integer DEFAULT 0 NOT NULL
);


--
-- Name: contabilidad_registro_contable_lineas_id_linea_registro_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.contabilidad_registro_contable_lineas_id_linea_registro_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: contabilidad_registro_contable_lineas_id_linea_registro_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.contabilidad_registro_contable_lineas_id_linea_registro_seq OWNED BY public.contabilidad_registro_contable_lineas.id_linea_registro;


--
-- Name: contabilidad_tipos_comprobantes; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.contabilidad_tipos_comprobantes (
                                                        id_tipo_comprobante integer NOT NULL,
                                                        nombre_tipo character varying(100) NOT NULL,
                                                        descripcion_tipo character varying(2000),
                                                        version integer DEFAULT 0 NOT NULL
);


--
-- Name: contabilidad_tipos_comprobantes_id_tipo_comprobante_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.contabilidad_tipos_comprobantes_id_tipo_comprobante_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: contabilidad_tipos_comprobantes_id_tipo_comprobante_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.contabilidad_tipos_comprobantes_id_tipo_comprobante_seq OWNED BY public.contabilidad_tipos_comprobantes.id_tipo_comprobante;


--
-- Name: contabilidad_tipos_cuenta; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.contabilidad_tipos_cuenta (
                                                  id_tipo_cuenta integer NOT NULL,
                                                  nombre_tipo character varying(60) NOT NULL,
                                                  descripcion_tipo character varying(255),
                                                  version integer DEFAULT 0 NOT NULL
);


--
-- Name: contabilidad_tipos_cuenta_id_tipo_cuenta_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.contabilidad_tipos_cuenta_id_tipo_cuenta_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: contabilidad_tipos_cuenta_id_tipo_cuenta_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.contabilidad_tipos_cuenta_id_tipo_cuenta_seq OWNED BY public.contabilidad_tipos_cuenta.id_tipo_cuenta;


--
-- Name: contabilidad_tipos_operacion; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.contabilidad_tipos_operacion (
                                                     id_tipo_operacion integer NOT NULL,
                                                     nombre_tipo_operacion character varying(100) NOT NULL,
                                                     descripcion_tipo_operacion character varying(2000),
                                                     version integer DEFAULT 0 NOT NULL
);


--
-- Name: contabilidad_tipos_operacion_id_tipo_operacion_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.contabilidad_tipos_operacion_id_tipo_operacion_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: contabilidad_tipos_operacion_id_tipo_operacion_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.contabilidad_tipos_operacion_id_tipo_operacion_seq OWNED BY public.contabilidad_tipos_operacion.id_tipo_operacion;


--
-- Name: cupones; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.cupones (
                                id_valor integer NOT NULL,
                                nro_cupon integer NOT NULL,
                                codigo_autorizacion integer,
                                nro_lote integer,
                                fecha_origen timestamp without time zone NOT NULL,
                                fecha_presentacion timestamp without time zone,
                                fecha_acreditacion timestamp without time zone,
                                cant_cuotas integer NOT NULL,
                                notas character varying(255),
                                coeficiente numeric(19,4)
);


--
-- Name: TABLE cupones; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.cupones IS 'Cupones de tarjetas de crédito o débito';


--
-- Name: depositos; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.depositos (
                                  id_deposito integer NOT NULL,
                                  nombre_deposito character varying(60) NOT NULL,
                                  id_sucursal integer,
                                  id_pais integer NOT NULL,
                                  id_provincia integer NOT NULL,
                                  id_localidad integer NOT NULL,
                                  direccion character varying(500),
                                  fecha_alta timestamp without time zone NOT NULL,
                                  activo boolean DEFAULT true NOT NULL,
                                  version integer DEFAULT 0 NOT NULL
);


--
-- Name: depositos_id_deposito_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.depositos_id_deposito_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: depositos_id_deposito_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.depositos_id_deposito_seq OWNED BY public.depositos.id_deposito;


--
-- Name: fiscal_alicuotas_iva; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.fiscal_alicuotas_iva (
                                             id_alicuota_iva integer NOT NULL,
                                             nombre_alicuota_iva character varying(60) NOT NULL,
                                             valor_alicuota numeric(19,2) NOT NULL,
                                             gravar_iva boolean DEFAULT true NOT NULL,
                                             activo boolean DEFAULT true NOT NULL,
                                             version integer DEFAULT 0 NOT NULL,
                                             fiscal_codigo_alicuota integer
);


--
-- Name: fiscal_alicuotas_iva_id_alicuota_iva_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.fiscal_alicuotas_iva_id_alicuota_iva_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: fiscal_alicuotas_iva_id_alicuota_iva_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.fiscal_alicuotas_iva_id_alicuota_iva_seq OWNED BY public.fiscal_alicuotas_iva.id_alicuota_iva;


--
-- Name: fiscal_letras_comprobantes; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.fiscal_letras_comprobantes (
                                                   id_resoponsabildiad_iva_emisor integer NOT NULL,
                                                   id_resoponsabildiad_iva_receptor integer NOT NULL,
                                                   letra_comprobante character varying(1) NOT NULL,
                                                   version integer DEFAULT 0 NOT NULL
);


--
-- Name: fiscal_libro_iva_compras; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.fiscal_libro_iva_compras (
                                                 id_registro integer NOT NULL,
                                                 fecha_factura timestamp without time zone NOT NULL,
                                                 id_responsabilidad_iva integer NOT NULL,
                                                 documento character varying(20) NOT NULL,
                                                 letra_factura character varying(2) NOT NULL,
                                                 punto_venta_factura character varying(5) NOT NULL,
                                                 numero_factura character varying(9) NOT NULL,
                                                 total_factura numeric(19,2) NOT NULL,
                                                 id_periodo_fiscal integer NOT NULL,
                                                 id_persona integer NOT NULL,
                                                 id_registro_contable integer,
                                                 anulada boolean DEFAULT false NOT NULL,
                                                 version integer DEFAULT 0 NOT NULL,
                                                 codigo_tipo_comprobante character(3) DEFAULT '001'::bpchar NOT NULL,
                                                 cae bigint,
                                                 fecha_vencimiento_cae date,
                                                 importe_neto_no_gravado numeric(19,2) DEFAULT 0 NOT NULL,
                                                 importe_exento numeric(19,2) DEFAULT 0 NOT NULL,
                                                 importe_neto_gravado numeric(19,2) DEFAULT 0 NOT NULL,
                                                 importe_tributos numeric(19,2) DEFAULT 0 NOT NULL,
                                                 importe_iva numeric(19,2) DEFAULT 0 NOT NULL,
                                                 importe_percepcion_iva numeric(19,2) DEFAULT 0 NOT NULL,
                                                 importe_percepcion_ingresos_brutos numeric(19,2) DEFAULT 0 NOT NULL
);


--
-- Name: fiscal_libro_iva_compras_id_registro_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.fiscal_libro_iva_compras_id_registro_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: fiscal_libro_iva_compras_id_registro_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.fiscal_libro_iva_compras_id_registro_seq OWNED BY public.fiscal_libro_iva_compras.id_registro;


--
-- Name: fiscal_libro_iva_compras_lineas; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.fiscal_libro_iva_compras_lineas (
                                                        id_linea_libro integer NOT NULL,
                                                        id_registro integer NOT NULL,
                                                        id_alicuota_iva integer NOT NULL,
                                                        neto_gravado numeric(19,2),
                                                        no_gravado numeric(19,2),
                                                        importe_iva numeric(19,2),
                                                        version integer DEFAULT 0 NOT NULL
);


--
-- Name: fiscal_libro_iva_compras_lineas_id_linea_libro_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.fiscal_libro_iva_compras_lineas_id_linea_libro_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: fiscal_libro_iva_compras_lineas_id_linea_libro_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.fiscal_libro_iva_compras_lineas_id_linea_libro_seq OWNED BY public.fiscal_libro_iva_compras_lineas.id_linea_libro;


--
-- Name: fiscal_libro_iva_ventas; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.fiscal_libro_iva_ventas (
                                                id_registro integer NOT NULL,
                                                fecha_factura timestamp without time zone NOT NULL,
                                                id_responsabilidad_iva integer NOT NULL,
                                                documento character varying(20) NOT NULL,
                                                letra_factura character varying(2) NOT NULL,
                                                punto_venta_factura character varying(4) NOT NULL,
                                                numero_factura character varying(8) NOT NULL,
                                                total_factura numeric(19,2) NOT NULL,
                                                id_periodo_fiscal integer NOT NULL,
                                                id_persona integer NOT NULL,
                                                id_registro_contable integer,
                                                anulada boolean DEFAULT false NOT NULL,
                                                version integer DEFAULT 0 NOT NULL,
                                                codigo_tipo_comprobante character(3) DEFAULT '006'::bpchar NOT NULL,
                                                cae bigint,
                                                fecha_vencimiento_cae date,
                                                importe_neto_no_gravado numeric(19,2) DEFAULT 0 NOT NULL,
                                                importe_exento numeric(19,2) DEFAULT 0 NOT NULL,
                                                importe_neto_gravado numeric(19,2) DEFAULT 0 NOT NULL,
                                                importe_tributos numeric(19,2) DEFAULT 0 NOT NULL,
                                                importe_iva numeric(19,2) DEFAULT 0 NOT NULL
);


--
-- Name: fiscal_libro_iva_ventas_id_factura_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.fiscal_libro_iva_ventas_id_factura_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: fiscal_libro_iva_ventas_id_factura_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.fiscal_libro_iva_ventas_id_factura_seq OWNED BY public.fiscal_libro_iva_ventas.id_registro;


--
-- Name: fiscal_libro_iva_ventas_lineas; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.fiscal_libro_iva_ventas_lineas (
                                                       id_linea_libro integer NOT NULL,
                                                       id_registro integer NOT NULL,
                                                       id_alicuota_iva integer NOT NULL,
                                                       neto_gravado numeric(19,2),
                                                       no_gravado numeric(19,2),
                                                       importe_iva numeric(19,2),
                                                       version integer DEFAULT 0 NOT NULL
);


--
-- Name: fiscal_libro_iva_ventas_lineas_id_linea_libro_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.fiscal_libro_iva_ventas_lineas_id_linea_libro_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: fiscal_libro_iva_ventas_lineas_id_linea_libro_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.fiscal_libro_iva_ventas_lineas_id_linea_libro_seq OWNED BY public.fiscal_libro_iva_ventas_lineas.id_linea_libro;


--
-- Name: fiscal_monedas; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.fiscal_monedas (
                                       codigo_moneda character(3) NOT NULL,
                                       nombre_moneda character varying(50) NOT NULL
);


--
-- Name: fiscal_periodos_fiscales; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.fiscal_periodos_fiscales (
                                                 id_periodo_fiscal integer NOT NULL,
                                                 nombre_periodo character varying(100) NOT NULL,
                                                 fecha_inicio_periodo timestamp without time zone NOT NULL,
                                                 fecha_fin_periodo timestamp without time zone NOT NULL,
                                                 version integer DEFAULT 0 NOT NULL,
                                                 periodo_cerrado boolean DEFAULT false NOT NULL
);


--
-- Name: fiscal_periodos_fiscales_id_periodo_fiscal_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.fiscal_periodos_fiscales_id_periodo_fiscal_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: fiscal_periodos_fiscales_id_periodo_fiscal_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.fiscal_periodos_fiscales_id_periodo_fiscal_seq OWNED BY public.fiscal_periodos_fiscales.id_periodo_fiscal;


--
-- Name: fiscal_puntos_venta; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.fiscal_puntos_venta (
                                            nro_punto_venta integer NOT NULL,
                                            activo boolean NOT NULL,
                                            tipo character varying(30) NOT NULL,
                                            descripcion character varying(100) NOT NULL,
                                            id_sucursal integer NOT NULL,
                                            version integer DEFAULT 0 NOT NULL
);


--
-- Name: TABLE fiscal_puntos_venta; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.fiscal_puntos_venta IS 'Puntos de venta y su tipo para realizar facturas';


--
-- Name: fiscal_responsabilidades_iva; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.fiscal_responsabilidades_iva (
                                                     id_resoponsabildiad_iva integer NOT NULL,
                                                     nombre_responsabildiad character varying(60) NOT NULL,
                                                     version integer DEFAULT 0 NOT NULL,
                                                     fiscal_codigo_responsable integer
);


--
-- Name: fiscal_responsabilidades_iva_id_resoponsabildiad_iva_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.fiscal_responsabilidades_iva_id_resoponsabildiad_iva_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: fiscal_responsabilidades_iva_id_resoponsabildiad_iva_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.fiscal_responsabilidades_iva_id_resoponsabildiad_iva_seq OWNED BY public.fiscal_responsabilidades_iva.id_resoponsabildiad_iva;


--
-- Name: fiscal_tipos_comprobante; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.fiscal_tipos_comprobante (
                                                 codigo_tipo_comprobante character(3) NOT NULL,
                                                 denominacion_comprobante character varying(100) NOT NULL,
                                                 version integer DEFAULT 0 NOT NULL,
                                                 letra character(1),
                                                 id_negocio_tipo_comprobante integer
);


--
-- Name: legal_generos; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.legal_generos (
                                      id_genero integer NOT NULL,
                                      nombre_genero character varying(30) NOT NULL,
                                      simbolo character varying(1) NOT NULL,
                                      id_tipo_personeria integer DEFAULT 1 NOT NULL,
                                      version integer DEFAULT 0 NOT NULL
);


--
-- Name: legal_generos_id_genero_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.legal_generos_id_genero_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: legal_generos_id_genero_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.legal_generos_id_genero_seq OWNED BY public.legal_generos.id_genero;


--
-- Name: legal_tipos_documento; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.legal_tipos_documento (
                                              id_tipo_documento integer NOT NULL,
                                              nombre_tipo_documento character varying(100) NOT NULL,
                                              cantidad_caracteres_minimo integer NOT NULL,
                                              cantidad_caracteres_maximo integer NOT NULL,
                                              id_tipo_personeria integer DEFAULT 1 NOT NULL,
                                              version integer DEFAULT 0 NOT NULL,
                                              fiscal_codigo_tipo_documento integer
);


--
-- Name: legal_tipos_documento_id_tipo_documento_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.legal_tipos_documento_id_tipo_documento_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: legal_tipos_documento_id_tipo_documento_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.legal_tipos_documento_id_tipo_documento_seq OWNED BY public.legal_tipos_documento.id_tipo_documento;


--
-- Name: legal_tipos_personeria; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.legal_tipos_personeria (
                                               id_tipo_personeria integer NOT NULL,
                                               nombre_tipo character varying(100) NOT NULL,
                                               version integer DEFAULT 0 NOT NULL
);


--
-- Name: legal_tipos_personeria_id_tipo_personeria_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.legal_tipos_personeria_id_tipo_personeria_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: legal_tipos_personeria_id_tipo_personeria_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.legal_tipos_personeria_id_tipo_personeria_seq OWNED BY public.legal_tipos_personeria.id_tipo_personeria;


--
-- Name: negocio_condiciones_operaciones; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.negocio_condiciones_operaciones (
                                                        id_condicion integer NOT NULL,
                                                        nombre_condicion character varying(60) NOT NULL,
                                                        activo boolean DEFAULT true NOT NULL,
                                                        venta boolean DEFAULT true NOT NULL,
                                                        compra boolean DEFAULT true NOT NULL,
                                                        pago_total boolean DEFAULT true NOT NULL,
                                                        version integer DEFAULT 0 NOT NULL
);


--
-- Name: negocio_formas_pago; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.negocio_formas_pago (
                                            id_forma_pago integer NOT NULL,
                                            nombre_forma_pago character varying(60) NOT NULL,
                                            nombre_corto character varying(10),
                                            venta boolean DEFAULT true NOT NULL,
                                            compra boolean DEFAULT true NOT NULL,
                                            version integer DEFAULT 0 NOT NULL,
                                            requiere_plan boolean DEFAULT false NOT NULL,
                                            requiere_valores boolean DEFAULT false NOT NULL
);


--
-- Name: COLUMN negocio_formas_pago.requiere_plan; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.negocio_formas_pago.requiere_plan IS 'Si requiere que tenga al menos un plan asociado para poder ser utilizado';


--
-- Name: COLUMN negocio_formas_pago.requiere_valores; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.negocio_formas_pago.requiere_valores IS 'Si la forma de pago requiere el ingreso de valores que la representen';


--
-- Name: negocio_planes_pago; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.negocio_planes_pago (
                                            id_plan integer NOT NULL,
                                            id_forma_pago integer NOT NULL,
                                            nombre character varying(60) NOT NULL,
                                            fecha_activo_desde timestamp without time zone NOT NULL,
                                            fecha_activo_hasta timestamp without time zone NOT NULL,
                                            version integer DEFAULT 0 NOT NULL
);


--
-- Name: TABLE negocio_planes_pago; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.negocio_planes_pago IS 'Los planes de pago para cada tipo de pago';


--
-- Name: negocio_planes_pago_detalle; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.negocio_planes_pago_detalle (
                                                    id_detalle_plan integer NOT NULL,
                                                    id_plan integer NOT NULL,
                                                    activo boolean NOT NULL,
                                                    cuotas integer NOT NULL,
                                                    coeficiente_interes numeric(19,4),
                                                    version integer DEFAULT 0 NOT NULL
);


--
-- Name: TABLE negocio_planes_pago_detalle; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.negocio_planes_pago_detalle IS 'Detalle del plan de pago';


--
-- Name: negocio_planes_pago_detalle_id_detalle_plan_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.negocio_planes_pago_detalle_id_detalle_plan_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: negocio_planes_pago_detalle_id_detalle_plan_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.negocio_planes_pago_detalle_id_detalle_plan_seq OWNED BY public.negocio_planes_pago_detalle.id_detalle_plan;


--
-- Name: negocio_planes_pago_id_plan_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.negocio_planes_pago_id_plan_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: negocio_planes_pago_id_plan_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.negocio_planes_pago_id_plan_seq OWNED BY public.negocio_planes_pago.id_plan;


--
-- Name: negocio_planes_pago_listas; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.negocio_planes_pago_listas (
                                                   id_plan integer NOT NULL,
                                                   id_lista_precio integer NOT NULL
);


--
-- Name: TABLE negocio_planes_pago_listas; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.negocio_planes_pago_listas IS 'Las listas de precio habilitadas para cada plan';


--
-- Name: negocio_tipos_comprobante; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.negocio_tipos_comprobante (
                                                  id_negocio_tipo_comprobante integer NOT NULL,
                                                  nombre_comprobante character varying(30) NOT NULL,
                                                  signo numeric(1,0) NOT NULL,
                                                  activo boolean NOT NULL,
                                                  version integer DEFAULT 0 NOT NULL,
                                                  CONSTRAINT negocio_tipos_comprobante_signo_check CHECK (((signo = (1)::numeric) OR (signo = ('-1'::integer)::numeric)))
);


--
-- Name: TABLE negocio_tipos_comprobante; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.negocio_tipos_comprobante IS 'Los tipos de comprobante soportados por el sistema (Facturas, Notas de Crédito, etc)';


--
-- Name: ofertas; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.ofertas (
                                id_oferta integer NOT NULL,
                                texto_oferta character varying(90) NOT NULL,
                                tipo_accion character varying(30) NOT NULL,
                                descuento numeric(19,4) NOT NULL,
                                vigencia_desde timestamp without time zone NOT NULL,
                                vigencia_hasta timestamp without time zone NOT NULL,
                                id_sucursal integer,
                                version integer DEFAULT 0 NOT NULL
);


--
-- Name: ofertas_condiciones; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.ofertas_condiciones (
                                            id_oferta_condicion integer NOT NULL,
                                            id_oferta integer NOT NULL,
                                            operacion character varying(30) NOT NULL,
                                            campo character varying(30) NOT NULL,
                                            valor character varying(30) NOT NULL,
                                            version integer DEFAULT 0 NOT NULL
);


--
-- Name: ofertas_condiciones_id_oferta_condicion_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.ofertas_condiciones_id_oferta_condicion_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: ofertas_condiciones_id_oferta_condicion_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.ofertas_condiciones_id_oferta_condicion_seq OWNED BY public.ofertas_condiciones.id_oferta_condicion;


--
-- Name: ofertas_id_oferta_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.ofertas_id_oferta_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: ofertas_id_oferta_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.ofertas_id_oferta_seq OWNED BY public.ofertas.id_oferta;


--
-- Name: parametros; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.parametros (
                                   nombre_parametro character varying(255) NOT NULL,
                                   valor_parametro character varying(255),
                                   descripcion_parametro character varying(1024) NOT NULL,
                                   version integer DEFAULT 0 NOT NULL
);


--
-- Name: personas; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.personas (
                                 id_persona integer NOT NULL,
                                 razon_social character varying(200) NOT NULL,
                                 apellidos character varying(60),
                                 nombres character varying(60),
                                 nombre_fantasia character varying(200),
                                 id_tipo_personeria integer NOT NULL,
                                 id_pais integer NOT NULL,
                                 id_provincia integer NOT NULL,
                                 id_localidad integer NOT NULL,
                                 calle character varying(100),
                                 altura character varying(50),
                                 piso character varying(3),
                                 depto character varying(5),
                                 id_tipo_documento integer NOT NULL,
                                 documento character varying(13) NOT NULL,
                                 id_responsabilidad_iva integer NOT NULL,
                                 fecha_alta timestamp without time zone NOT NULL,
                                 activo boolean DEFAULT true NOT NULL,
                                 cliente boolean DEFAULT true NOT NULL,
                                 proveedor boolean DEFAULT false NOT NULL,
                                 id_genero integer NOT NULL,
                                 version integer DEFAULT 0 NOT NULL,
                                 email character varying(100) DEFAULT 'a@a.com'::character varying,
                                 id_sucursal integer DEFAULT 1 NOT NULL
);


--
-- Name: personas_cuenta_corriente; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.personas_cuenta_corriente (
                                                  id_movimiento integer NOT NULL,
                                                  id_persona integer NOT NULL,
                                                  fecha_movimiento timestamp without time zone NOT NULL,
                                                  importe_movimiento numeric(19,2) NOT NULL,
                                                  descripcion_movimiento character varying(200) NOT NULL,
                                                  id_registro_contable integer,
                                                  version integer DEFAULT 0 NOT NULL
);


--
-- Name: personas_cuenta_corriente_id_movimiento_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.personas_cuenta_corriente_id_movimiento_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: personas_cuenta_corriente_id_movimiento_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.personas_cuenta_corriente_id_movimiento_seq OWNED BY public.personas_cuenta_corriente.id_movimiento;


--
-- Name: personas_id_persona_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.personas_id_persona_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: personas_id_persona_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.personas_id_persona_seq OWNED BY public.personas.id_persona;


--
-- Name: personas_imagenes; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.personas_imagenes (
                                          id_imagen integer NOT NULL,
                                          id_persona integer NOT NULL,
                                          fecha_alta timestamp without time zone NOT NULL,
                                          id_tipo_imagen integer NOT NULL,
                                          observaciones character varying(1024),
                                          version integer DEFAULT 0 NOT NULL
);


--
-- Name: personas_imagenes_id_imagen_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.personas_imagenes_id_imagen_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: personas_imagenes_id_imagen_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.personas_imagenes_id_imagen_seq OWNED BY public.personas_imagenes.id_imagen;


--
-- Name: personas_telefonos; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.personas_telefonos (
                                           id_telefono integer NOT NULL,
                                           id_persona integer NOT NULL,
                                           numero character varying(50) NOT NULL,
                                           referencia character varying(100),
                                           version integer DEFAULT 0 NOT NULL
);


--
-- Name: personas_telefonos_id_telefono_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.personas_telefonos_id_telefono_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: personas_telefonos_id_telefono_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.personas_telefonos_id_telefono_seq OWNED BY public.personas_telefonos.id_telefono;


--
-- Name: personas_tipos_imagenes; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.personas_tipos_imagenes (
                                                id_tipo_imagen integer NOT NULL,
                                                nombre_tipo character varying(60) NOT NULL,
                                                descripcion_tipo character varying(200),
                                                version integer DEFAULT 0 NOT NULL
);


--
-- Name: personas_tipos_imagenes_id_tipo_imagen_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.personas_tipos_imagenes_id_tipo_imagen_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: personas_tipos_imagenes_id_tipo_imagen_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.personas_tipos_imagenes_id_tipo_imagen_seq OWNED BY public.personas_tipos_imagenes.id_tipo_imagen;


--
-- Name: privilegios; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.privilegios (
                                    id_privilegio integer NOT NULL,
                                    nombre_privilegio character varying(100) NOT NULL,
                                    descripcion_privilegio character varying(1024),
                                    version integer DEFAULT 0 NOT NULL
);


--
-- Name: privilegios_gruposx; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.privilegios_gruposx (
                                            id_privilegio integer NOT NULL,
                                            id_grupo integer NOT NULL,
                                            version integer DEFAULT 0 NOT NULL
);


--
-- Name: privilegios_id_privilegio_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.privilegios_id_privilegio_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: privilegios_id_privilegio_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.privilegios_id_privilegio_seq OWNED BY public.privilegios.id_privilegio;


--
-- Name: productos; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.productos (
                                  id_producto integer NOT NULL,
                                  codigo_propio character varying(100),
                                  descripcion character varying(200),
                                  fecha_alta timestamp without time zone NOT NULL,
                                  activo boolean DEFAULT true NOT NULL,
                                  id_tipo_proveeduria integer NOT NULL,
                                  id_tipo_unidad_compra integer NOT NULL,
                                  id_tipo_unidad_venta integer NOT NULL,
                                  costo_adquisicion_neto numeric(19,4) NOT NULL,
                                  id_alicuota_iva integer NOT NULL,
                                  id_rubro integer NOT NULL,
                                  id_sub_rubro integer NOT NULL,
                                  annos_amortizacion integer DEFAULT 0 NOT NULL,
                                  id_proveedor_habitual integer,
                                  version integer DEFAULT 0 NOT NULL,
                                  id_marca integer DEFAULT 1 NOT NULL,
                                  fecha_ultima_modificacion timestamp without time zone DEFAULT now() NOT NULL,
                                  observaciones character varying(2048),
                                  costo_final numeric(19,4) DEFAULT 0 NOT NULL,
                                  unidades_compra_unidades_venta numeric(19,2) DEFAULT 1 NOT NULL,
                                  ubicacion character varying(60),
                                  stock_minimo numeric(19,2) DEFAULT 0 NOT NULL,
                                  codigo_fabricante character varying(60)
);


--
-- Name: productos_caracteristicas; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.productos_caracteristicas (
                                                  id_caracteristica integer NOT NULL,
                                                  nombre_caracteristica character varying(100) NOT NULL,
                                                  version integer DEFAULT 0 NOT NULL
);


--
-- Name: productos_caracteristicas_id_caracteristica_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.productos_caracteristicas_id_caracteristica_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: productos_caracteristicas_id_caracteristica_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.productos_caracteristicas_id_caracteristica_seq OWNED BY public.productos_caracteristicas.id_caracteristica;


--
-- Name: productos_id_producto_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.productos_id_producto_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: productos_id_producto_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.productos_id_producto_seq OWNED BY public.productos.id_producto;


--
-- Name: productos_imagenes; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.productos_imagenes (
                                           id_imagen integer NOT NULL,
                                           id_producto integer NOT NULL,
                                           id_usuario integer NOT NULL,
                                           fecha_alta timestamp without time zone NOT NULL,
                                           descripcion character varying(200),
                                           imagen bytea NOT NULL,
                                           version integer DEFAULT 0 NOT NULL
);


--
-- Name: productos_imagenes_id_imagen_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.productos_imagenes_id_imagen_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: productos_imagenes_id_imagen_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.productos_imagenes_id_imagen_seq OWNED BY public.productos_imagenes.id_imagen;


--
-- Name: productos_listas_precios; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.productos_listas_precios (
                                                 id_lista_precio integer NOT NULL,
                                                 version integer DEFAULT 0 NOT NULL,
                                                 nombre_lista character varying(50) NOT NULL,
                                                 activa boolean NOT NULL
);


--
-- Name: productos_listas_precios_id_lista_precio_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.productos_listas_precios_id_lista_precio_seq
    START WITH 2
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: productos_listas_precios_id_lista_precio_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.productos_listas_precios_id_lista_precio_seq OWNED BY public.productos_listas_precios.id_lista_precio;


--
-- Name: productos_marcas; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.productos_marcas (
                                         id_marca integer NOT NULL,
                                         version integer DEFAULT 0 NOT NULL,
                                         nombre_marca character varying(100) NOT NULL
);


--
-- Name: TABLE productos_marcas; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.productos_marcas IS 'Marcas de productos';


--
-- Name: productos_marcas_id_marca_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.productos_marcas_id_marca_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: productos_marcas_id_marca_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.productos_marcas_id_marca_seq OWNED BY public.productos_marcas.id_marca;


--
-- Name: productos_porcentajes; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.productos_porcentajes (
                                              id_producto_porcentaje integer NOT NULL,
                                              id_producto integer NOT NULL,
                                              id_tipo_porcentaje integer NOT NULL,
                                              valor numeric(19,4) NOT NULL,
                                              fecha_modificacion timestamp without time zone DEFAULT now() NOT NULL,
                                              version integer DEFAULT 0 NOT NULL
);


--
-- Name: TABLE productos_porcentajes; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.productos_porcentajes IS 'Valor de cada tipo de porcentaje por producto';


--
-- Name: productos_porcentajes_id_producto_porcentaje_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.productos_porcentajes_id_producto_porcentaje_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: productos_porcentajes_id_producto_porcentaje_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.productos_porcentajes_id_producto_porcentaje_seq OWNED BY public.productos_porcentajes.id_producto_porcentaje;


--
-- Name: productos_precios; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.productos_precios (
                                          id_producto integer NOT NULL,
                                          id_lista_precio integer NOT NULL,
                                          utilidad numeric(8,4) NOT NULL,
                                          neto numeric(19,4) NOT NULL,
                                          precio numeric(19,4) NOT NULL,
                                          version integer DEFAULT 0 NOT NULL,
                                          fecha_modificacion timestamp without time zone DEFAULT now() NOT NULL,
                                          productos_precios_id integer NOT NULL
);


--
-- Name: productos_precios_productos_precios_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.productos_precios_productos_precios_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: productos_precios_productos_precios_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.productos_precios_productos_precios_id_seq OWNED BY public.productos_precios.productos_precios_id;


--
-- Name: productos_rubros; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.productos_rubros (
                                         id_rubro integer NOT NULL,
                                         nombre_rubro character varying(100) NOT NULL,
                                         version integer DEFAULT 0 NOT NULL
);


--
-- Name: productos_rubros_id_rubro_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.productos_rubros_id_rubro_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: productos_rubros_id_rubro_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.productos_rubros_id_rubro_seq OWNED BY public.productos_rubros.id_rubro;


--
-- Name: productos_sub_rubros; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.productos_sub_rubros (
                                             id_sub_rubro integer NOT NULL,
                                             id_rubro integer NOT NULL,
                                             nombre_sub_rubro character varying(100) NOT NULL,
                                             version integer DEFAULT 0 NOT NULL
);


--
-- Name: productos_sub_rubros_id_sub_rubro_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.productos_sub_rubros_id_sub_rubro_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: productos_sub_rubros_id_sub_rubro_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.productos_sub_rubros_id_sub_rubro_seq OWNED BY public.productos_sub_rubros.id_sub_rubro;


--
-- Name: productos_tipos_porcentajes; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.productos_tipos_porcentajes (
                                                    id_tipo_porcentaje integer NOT NULL,
                                                    version integer DEFAULT 0 NOT NULL,
                                                    nombre_tipo character varying(50) NOT NULL,
                                                    porcentaje boolean DEFAULT true NOT NULL
);


--
-- Name: TABLE productos_tipos_porcentajes; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.productos_tipos_porcentajes IS 'Los tipos de porcentaje que se pueden aplicar';


--
-- Name: productos_tipos_porcentajes_id_tipo_porcentaje_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.productos_tipos_porcentajes_id_tipo_porcentaje_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: productos_tipos_porcentajes_id_tipo_porcentaje_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.productos_tipos_porcentajes_id_tipo_porcentaje_seq OWNED BY public.productos_tipos_porcentajes.id_tipo_porcentaje;


--
-- Name: productos_tipos_proveeduria; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.productos_tipos_proveeduria (
                                                    id_tipo_proveeduria integer NOT NULL,
                                                    nombre_tipo_proveeduria character varying(60),
                                                    puede_comprarse boolean NOT NULL,
                                                    puede_venderse boolean NOT NULL,
                                                    control_stock boolean NOT NULL,
                                                    cambiar_precio_venta boolean DEFAULT true NOT NULL,
                                                    version integer DEFAULT 0 NOT NULL
);


--
-- Name: productos_tipos_proveeduria_id_tipo_proveeduria_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.productos_tipos_proveeduria_id_tipo_proveeduria_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: productos_tipos_proveeduria_id_tipo_proveeduria_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.productos_tipos_proveeduria_id_tipo_proveeduria_seq OWNED BY public.productos_tipos_proveeduria.id_tipo_proveeduria;


--
-- Name: productos_tipos_unidades; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.productos_tipos_unidades (
                                                 id_tipo_unidad integer NOT NULL,
                                                 nombre_unidad character varying(60) NOT NULL,
                                                 cantidad_entera boolean NOT NULL,
                                                 version integer DEFAULT 0 NOT NULL
);


--
-- Name: productos_tipos_unidades_id_tipo_unidad_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.productos_tipos_unidades_id_tipo_unidad_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: productos_tipos_unidades_id_tipo_unidad_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.productos_tipos_unidades_id_tipo_unidad_seq OWNED BY public.productos_tipos_unidades.id_tipo_unidad;


--
-- Name: productos_x_caracteristicas; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.productos_x_caracteristicas (
                                                    id_caracteristica_x_producto integer NOT NULL,
                                                    id_caracteristica integer NOT NULL,
                                                    id_producto integer NOT NULL,
                                                    valor_caracteristica character varying(255) NOT NULL,
                                                    version integer DEFAULT 0 NOT NULL
);


--
-- Name: productos_x_caracteristicas_id_caracteristica_x_producto_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.productos_x_caracteristicas_id_caracteristica_x_producto_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: productos_x_caracteristicas_id_caracteristica_x_producto_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.productos_x_caracteristicas_id_caracteristica_x_producto_seq OWNED BY public.productos_x_caracteristicas.id_caracteristica_x_producto;


--
-- Name: productos_x_depositos; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.productos_x_depositos (
                                              id_producto_x_deposito integer NOT NULL,
                                              id_producto integer NOT NULL,
                                              id_deposito integer NOT NULL,
                                              stock numeric(19,2) NOT NULL,
                                              version integer DEFAULT 0 NOT NULL
);


--
-- Name: productos_x_depositos_id_producto_x_deposito_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.productos_x_depositos_id_producto_x_deposito_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: productos_x_depositos_id_producto_x_deposito_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.productos_x_depositos_id_producto_x_deposito_seq OWNED BY public.productos_x_depositos.id_producto_x_deposito;


--
-- Name: proveedores_comprobantes; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.proveedores_comprobantes (
                                                 id_comprobante integer NOT NULL,
                                                 fecha_comprobante timestamp without time zone NOT NULL,
                                                 id_usuario integer NOT NULL,
                                                 id_sucursal integer NOT NULL,
                                                 total numeric(19,2) NOT NULL,
                                                 id_registro_iva integer,
                                                 observaciones character varying(1024),
                                                 anulada boolean DEFAULT false NOT NULL,
                                                 id_proveedor integer NOT NULL,
                                                 version integer NOT NULL,
                                                 letra character(1),
                                                 id_negocio_tipo_comprobante integer DEFAULT 1 NOT NULL
);


--
-- Name: proveedores_comprobantes_id_comprobante_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.proveedores_comprobantes_id_comprobante_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: proveedores_comprobantes_id_comprobante_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.proveedores_comprobantes_id_comprobante_seq OWNED BY public.proveedores_comprobantes.id_comprobante;


--
-- Name: proveedores_ordenes_compra; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.proveedores_ordenes_compra (
                                                   id_orden_compra integer NOT NULL,
                                                   id_proveedor integer NOT NULL,
                                                   fecha_alta timestamp without time zone NOT NULL,
                                                   fecha_modificacion timestamp without time zone NOT NULL,
                                                   id_usuario integer NOT NULL,
                                                   id_transporte integer,
                                                   id_estado_orden_compra integer NOT NULL,
                                                   observaciones text,
                                                   fecha_estimada_recepcion date,
                                                   version integer DEFAULT 0 NOT NULL,
                                                   monto_iva_total numeric(19,2) NOT NULL,
                                                   total_orden_compra numeric(19,2) NOT NULL
);


--
-- Name: proveedores_ordenes_compra_estados; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.proveedores_ordenes_compra_estados (
                                                           id_estado_orden_compra integer NOT NULL,
                                                           nombre_estado_orden_compra character varying(50) NOT NULL,
                                                           version integer DEFAULT 0 NOT NULL
);


--
-- Name: TABLE proveedores_ordenes_compra_estados; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.proveedores_ordenes_compra_estados IS 'Los posibles estados de las órdenes de compra';


--
-- Name: proveedores_ordenes_compra_id_orden_compra_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.proveedores_ordenes_compra_id_orden_compra_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: proveedores_ordenes_compra_id_orden_compra_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.proveedores_ordenes_compra_id_orden_compra_seq OWNED BY public.proveedores_ordenes_compra.id_orden_compra;


--
-- Name: proveedores_ordenes_compra_lineas_porcentajes; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.proveedores_ordenes_compra_lineas_porcentajes (
                                                                      id_orden_compra_linea_porcentaje integer NOT NULL,
                                                                      id_orden_compra_linea integer NOT NULL,
                                                                      id_tipo_porcentaje integer NOT NULL,
                                                                      valor numeric(19,4) NOT NULL,
                                                                      version integer DEFAULT 0 NOT NULL
);


--
-- Name: TABLE proveedores_ordenes_compra_lineas_porcentajes; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.proveedores_ordenes_compra_lineas_porcentajes IS 'Los porcentajes que afectan al precio unitario de cada línea de la orden de compra';


--
-- Name: proveedores_ordenes_compra_li_id_orden_compra_linea_porcent_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.proveedores_ordenes_compra_li_id_orden_compra_linea_porcent_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: proveedores_ordenes_compra_li_id_orden_compra_linea_porcent_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.proveedores_ordenes_compra_li_id_orden_compra_linea_porcent_seq OWNED BY public.proveedores_ordenes_compra_lineas_porcentajes.id_orden_compra_linea_porcentaje;


--
-- Name: proveedores_ordenes_compra_lineas; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.proveedores_ordenes_compra_lineas (
                                                          id_orden_compra_linea integer NOT NULL,
                                                          id_orden_compra integer NOT NULL,
                                                          id_producto integer NOT NULL,
                                                          cantidad_pedida numeric(19,2) NOT NULL,
                                                          id_tipo_unidad integer NOT NULL,
                                                          cantidad_recibida numeric(19,2) NOT NULL,
                                                          precio_compra_unitario numeric(19,4) NOT NULL,
                                                          sub_total numeric(19,4) NOT NULL,
                                                          version integer DEFAULT 0 NOT NULL
);


--
-- Name: proveedores_ordenes_compra_lineas_id_orden_compra_linea_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.proveedores_ordenes_compra_lineas_id_orden_compra_linea_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: proveedores_ordenes_compra_lineas_id_orden_compra_linea_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.proveedores_ordenes_compra_lineas_id_orden_compra_linea_seq OWNED BY public.proveedores_ordenes_compra_lineas.id_orden_compra_linea;


--
-- Name: proveedores_ordenes_compra_porcentajes; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.proveedores_ordenes_compra_porcentajes (
                                                               id_orden_compra_porcentaje integer NOT NULL,
                                                               id_orden_compra integer NOT NULL,
                                                               id_tipo_porcentaje integer NOT NULL,
                                                               valor numeric(19,4) NOT NULL,
                                                               version integer DEFAULT 0 NOT NULL
);


--
-- Name: TABLE proveedores_ordenes_compra_porcentajes; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.proveedores_ordenes_compra_porcentajes IS 'Los porcentajes que afectan a la orden de compra en general';


--
-- Name: proveedores_ordenes_compra_porce_id_orden_compra_porcentaje_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.proveedores_ordenes_compra_porce_id_orden_compra_porcentaje_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: proveedores_ordenes_compra_porce_id_orden_compra_porcentaje_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.proveedores_ordenes_compra_porce_id_orden_compra_porcentaje_seq OWNED BY public.proveedores_ordenes_compra_porcentajes.id_orden_compra_porcentaje;


--
-- Name: recibos; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.recibos (
                                id_recibo integer NOT NULL,
                                fecha_recibo timestamp without time zone NOT NULL,
                                id_persona integer NOT NULL,
                                id_usuario integer NOT NULL,
                                monto_total numeric(19,2),
                                observaciones character varying(255),
                                id_caja integer NOT NULL,
                                version integer DEFAULT 0 NOT NULL
);


--
-- Name: TABLE recibos; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.recibos IS 'Recibos de pago de los clientes';


--
-- Name: recibos_detalle; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.recibos_detalle (
                                        id_detalle_recibo integer NOT NULL,
                                        id_recibo integer NOT NULL,
                                        id_comprobante_pago integer,
                                        monto_pagado numeric(19,2) NOT NULL,
                                        id_forma_pago integer NOT NULL,
                                        id_valor integer,
                                        version integer DEFAULT 0 NOT NULL,
                                        monto_pagado_con_signo numeric(19,2) NOT NULL,
                                        redondeo numeric(19,2) DEFAULT 0 NOT NULL
);


--
-- Name: TABLE recibos_detalle; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.recibos_detalle IS 'Detalle de los valores y comprobantes asociados al recibo';


--
-- Name: COLUMN recibos_detalle.redondeo; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.recibos_detalle.redondeo IS 'El monto que se suma o resta al valor correspondiente al pago en concepto de redondeo. Solo el tipo de pago EFECTIVO deberia completar este campo.';


--
-- Name: recibos_detalle_id_detalle_recibo_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.recibos_detalle_id_detalle_recibo_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: recibos_detalle_id_detalle_recibo_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.recibos_detalle_id_detalle_recibo_seq OWNED BY public.recibos_detalle.id_detalle_recibo;


--
-- Name: recibos_id_recibo_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.recibos_id_recibo_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: recibos_id_recibo_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.recibos_id_recibo_seq OWNED BY public.recibos.id_recibo;


--
-- Name: remitos; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.remitos (
                                id_remito integer NOT NULL,
                                id_origen_externo integer,
                                fecha_alta timestamp without time zone NOT NULL,
                                id_usuario integer NOT NULL,
                                observaciones text,
                                origen_is_interno boolean,
                                id_origen_interno integer,
                                destino_previsto_interno integer,
                                destino_previsto_externo integer,
                                destino_is_interno boolean,
                                fecha_cierre timestamp without time zone,
                                id_tipo_movimiento integer NOT NULL,
                                version integer DEFAULT 0 NOT NULL
);


--
-- Name: COLUMN remitos.id_origen_externo; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.remitos.id_origen_externo IS 'cuando es un origen externo se va a hacer referencia a una persona, osea a un proveedor.';


--
-- Name: COLUMN remitos.id_origen_interno; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.remitos.id_origen_interno IS 'cuando el origen es interno, se hace refenrencia a un deposito de la empresa';


--
-- Name: remitos_detalle; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.remitos_detalle (
                                        id_remito_detalle integer NOT NULL,
                                        id_remito integer NOT NULL,
                                        id_producto integer NOT NULL,
                                        cantidad numeric(19,2) DEFAULT 0,
                                        version integer DEFAULT 0 NOT NULL
);


--
-- Name: remitos_detalle_id_remito_detalle_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.remitos_detalle_id_remito_detalle_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: remitos_detalle_id_remito_detalle_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.remitos_detalle_id_remito_detalle_seq OWNED BY public.remitos_detalle.id_remito_detalle;


--
-- Name: remitos_id_remito_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.remitos_id_remito_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: remitos_id_remito_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.remitos_id_remito_seq OWNED BY public.remitos.id_remito;


--
-- Name: remitos_movimientos_tipos; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.remitos_movimientos_tipos (
                                                  id_tipo_movimiento integer NOT NULL,
                                                  nombre_tipo character varying(60) NOT NULL,
                                                  version integer DEFAULT 0 NOT NULL
);


--
-- Name: remitos_recepciones; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.remitos_recepciones (
                                            id_recepcion integer NOT NULL,
                                            id_remito integer NOT NULL,
                                            fecha timestamp without time zone DEFAULT now() NOT NULL,
                                            id_usuario integer NOT NULL,
                                            id_persona integer,
                                            id_deposito integer,
                                            version integer DEFAULT 0 NOT NULL
);


--
-- Name: COLUMN remitos_recepciones.id_persona; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.remitos_recepciones.id_persona IS 'si el remito tiene salida, este campo no debe ser nulo, ya que debe ser entregado a una persona';


--
-- Name: COLUMN remitos_recepciones.id_deposito; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.remitos_recepciones.id_deposito IS 'este campo no es nulo cuando el remito tiene como destino previsto un deposito.';


--
-- Name: remitos_recepciones_id_recepcion_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.remitos_recepciones_id_recepcion_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: remitos_recepciones_id_recepcion_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.remitos_recepciones_id_recepcion_seq OWNED BY public.remitos_recepciones.id_recepcion;


--
-- Name: schema_version; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.schema_version (
                                       version_rank integer NOT NULL,
                                       installed_rank integer NOT NULL,
                                       version character varying(50) NOT NULL,
                                       description character varying(200) NOT NULL,
                                       type character varying(20) NOT NULL,
                                       script character varying(1000) NOT NULL,
                                       checksum integer,
                                       installed_by character varying(100) NOT NULL,
                                       installed_on timestamp without time zone DEFAULT now() NOT NULL,
                                       execution_time integer NOT NULL,
                                       success boolean NOT NULL
);


--
-- Name: stock_movimientos; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.stock_movimientos (
                                          id_movimiento_stock integer NOT NULL,
                                          fecha_movimiento timestamp without time zone NOT NULL,
                                          id_producto integer NOT NULL,
                                          cantidad_anterior numeric(19,2) NOT NULL,
                                          cantidad_movimiento numeric(19,2) NOT NULL,
                                          cantidad_actual numeric(19,2) NOT NULL,
                                          id_tipo_movimiento integer NOT NULL,
                                          observaciones_movimiento character varying(255),
                                          id_usuario integer NOT NULL,
                                          costo_total_movimiento numeric(19,2) NOT NULL,
                                          id_deposito_movimiento integer NOT NULL,
                                          version integer DEFAULT 0 NOT NULL
);


--
-- Name: stock_movimientos_id_movimiento_stock_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.stock_movimientos_id_movimiento_stock_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: stock_movimientos_id_movimiento_stock_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.stock_movimientos_id_movimiento_stock_seq OWNED BY public.stock_movimientos.id_movimiento_stock;


--
-- Name: stock_movimientos_tipos; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.stock_movimientos_tipos (
                                                id_tipo_movimiento integer NOT NULL,
                                                nombre_tipo character varying(60) NOT NULL,
                                                version integer DEFAULT 0 NOT NULL
);


--
-- Name: stock_movimientos_tipos_id_tipo_movimiento_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.stock_movimientos_tipos_id_tipo_movimiento_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: stock_movimientos_tipos_id_tipo_movimiento_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.stock_movimientos_tipos_id_tipo_movimiento_seq OWNED BY public.stock_movimientos_tipos.id_tipo_movimiento;


--
-- Name: sucursales; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.sucursales (
                                   id_sucursal integer NOT NULL,
                                   nombre_sucursal character varying(100) NOT NULL,
                                   id_pais integer NOT NULL,
                                   id_provincia integer NOT NULL,
                                   id_localidad integer NOT NULL,
                                   direccion character varying(500),
                                   telefono_fijo character varying(20),
                                   fecha_alta timestamp without time zone NOT NULL,
                                   activo boolean DEFAULT true NOT NULL,
                                   version integer DEFAULT 0 NOT NULL
);


--
-- Name: sucursales_id_sucursal_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.sucursales_id_sucursal_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: sucursales_id_sucursal_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.sucursales_id_sucursal_seq OWNED BY public.sucursales.id_sucursal;


--
-- Name: ubicacion_localidades; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.ubicacion_localidades (
                                              id_localidad integer NOT NULL,
                                              id_provincia integer NOT NULL,
                                              nombre_localidad character varying(100) NOT NULL,
                                              codigo_postal character varying(20) DEFAULT '0'::character varying NOT NULL,
                                              version integer DEFAULT 0 NOT NULL
);


--
-- Name: ubicacion_localidades_id_localidad_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.ubicacion_localidades_id_localidad_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: ubicacion_localidades_id_localidad_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.ubicacion_localidades_id_localidad_seq OWNED BY public.ubicacion_localidades.id_localidad;


--
-- Name: ubicacion_paises; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.ubicacion_paises (
                                         id_pais integer NOT NULL,
                                         nombre_pais character varying(60) NOT NULL,
                                         version integer DEFAULT 0 NOT NULL
);


--
-- Name: ubicacion_paises_id_pais_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.ubicacion_paises_id_pais_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: ubicacion_paises_id_pais_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.ubicacion_paises_id_pais_seq OWNED BY public.ubicacion_paises.id_pais;


--
-- Name: ubicacion_provincias; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.ubicacion_provincias (
                                             id_provincia integer NOT NULL,
                                             id_pais integer NOT NULL,
                                             nombre_provincia character varying(100) NOT NULL,
                                             version integer DEFAULT 0 NOT NULL
);


--
-- Name: ubicacion_provincias_id_provincia_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.ubicacion_provincias_id_provincia_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: ubicacion_provincias_id_provincia_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.ubicacion_provincias_id_provincia_seq OWNED BY public.ubicacion_provincias.id_provincia;


--
-- Name: usuarios; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.usuarios (
                                 id_usuario integer NOT NULL,
                                 nombre_usuario character varying(100) NOT NULL,
                                 login character varying(60) NOT NULL,
                                 password character varying(64) NOT NULL,
                                 fecha_alta timestamp without time zone NOT NULL,
                                 id_sucursal integer,
                                 version integer DEFAULT 0 NOT NULL
);


--
-- Name: usuarios_grupos; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.usuarios_grupos (
                                        id_grupo integer NOT NULL,
                                        nombre_grupo character varying(100) NOT NULL,
                                        version integer DEFAULT 0 NOT NULL
);


--
-- Name: usuarios_grupos_id_grupo_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.usuarios_grupos_id_grupo_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: usuarios_grupos_id_grupo_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.usuarios_grupos_id_grupo_seq OWNED BY public.usuarios_grupos.id_grupo;


--
-- Name: usuarios_gruposx; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.usuarios_gruposx (
                                         id_usuario integer NOT NULL,
                                         id_grupo integer NOT NULL
);


--
-- Name: usuarios_id_usuario_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.usuarios_id_usuario_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: usuarios_id_usuario_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.usuarios_id_usuario_seq OWNED BY public.usuarios.id_usuario;


--
-- Name: v_roles_usuarios; Type: VIEW; Schema: public; Owner: -
--

CREATE VIEW public.v_roles_usuarios AS
SELECT us.login,
       us.password,
       gru.nombre_grupo
FROM ((public.usuarios us
    JOIN public.usuarios_gruposx uxg ON ((uxg.id_usuario = us.id_usuario)))
         JOIN public.usuarios_grupos gru ON ((uxg.id_grupo = gru.id_grupo)));


--
-- Name: valores; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.valores (
                                id_valor integer NOT NULL,
                                version integer DEFAULT 0 NOT NULL,
                                monto numeric(19,2) NOT NULL,
                                tipo_valor character varying(45) NOT NULL
);


--
-- Name: TABLE valores; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.valores IS 'Son los documentos que representan cupones o cheques.';


--
-- Name: valores_id_valor_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.valores_id_valor_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: valores_id_valor_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.valores_id_valor_seq OWNED BY public.valores.id_valor;


--
-- Name: ventas_condiciones_id_condicion_venta_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.ventas_condiciones_id_condicion_venta_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: ventas_condiciones_id_condicion_venta_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.ventas_condiciones_id_condicion_venta_seq OWNED BY public.negocio_condiciones_operaciones.id_condicion;


--
-- Name: ventas_formas_pago_id_forma_pago_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.ventas_formas_pago_id_forma_pago_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: ventas_formas_pago_id_forma_pago_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.ventas_formas_pago_id_forma_pago_seq OWNED BY public.negocio_formas_pago.id_forma_pago;


--
-- Name: ventas_id_venta_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.ventas_id_venta_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: ventas_id_venta_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.ventas_id_venta_seq OWNED BY public.comprobantes.id_comprobante;


--
-- Name: ventas_lineas_id_linea_venta_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.ventas_lineas_id_linea_venta_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: ventas_lineas_id_linea_venta_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.ventas_lineas_id_linea_venta_seq OWNED BY public.comprobantes_lineas.id_linea_comprobante;


--
-- Name: bancos id_banco; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.bancos ALTER COLUMN id_banco SET DEFAULT nextval('public.bancos_id_banco_seq'::regclass);


--
-- Name: bancos_cuenta_corriente id_movimiento; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.bancos_cuenta_corriente ALTER COLUMN id_movimiento SET DEFAULT nextval('public.bancos_cuenta_corriente_id_movimiento_seq'::regclass);


--
-- Name: bancos_cuentas id_cuenta_banco; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.bancos_cuentas ALTER COLUMN id_cuenta_banco SET DEFAULT nextval('public.bancos_cuentas_id_cuenta_banco_seq'::regclass);


--
-- Name: bancos_tipos_cuenta id_tipo_cuenta_banco; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.bancos_tipos_cuenta ALTER COLUMN id_tipo_cuenta_banco SET DEFAULT nextval('public.bancos_tipos_cuenta_id_tipo_cuenta_banco_seq'::regclass);


--
-- Name: caja_arqueos id_arqueo; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.caja_arqueos ALTER COLUMN id_arqueo SET DEFAULT nextval('public.caja_arqueos_id_arqueo_seq'::regclass);


--
-- Name: caja_arqueos_detalle id_arqueo_detalle; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.caja_arqueos_detalle ALTER COLUMN id_arqueo_detalle SET DEFAULT nextval('public.caja_arqueos_detalle_id_arqueo_detalle_seq'::regclass);


--
-- Name: cajas id_caja; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.cajas ALTER COLUMN id_caja SET DEFAULT nextval('public.cajas_id_caja_seq'::regclass);


--
-- Name: cajas_movimientos id_movimiento_caja; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.cajas_movimientos ALTER COLUMN id_movimiento_caja SET DEFAULT nextval('public.cajas_movimientos_id_movimiento_caja_seq'::regclass);


--
-- Name: cajas_transferencias id_transferencia; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.cajas_transferencias ALTER COLUMN id_transferencia SET DEFAULT nextval('public.cajas_transferencias_id_transferencia_seq'::regclass);


--
-- Name: comprobantes id_comprobante; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.comprobantes ALTER COLUMN id_comprobante SET DEFAULT nextval('public.ventas_id_venta_seq'::regclass);


--
-- Name: comprobantes_lineas id_linea_comprobante; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.comprobantes_lineas ALTER COLUMN id_linea_comprobante SET DEFAULT nextval('public.ventas_lineas_id_linea_venta_seq'::regclass);


--
-- Name: comprobantes_pagos id_pago; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.comprobantes_pagos ALTER COLUMN id_pago SET DEFAULT nextval('public.comprobantes_pagos_id_pago_seq'::regclass);


--
-- Name: contabilidad_libros id_libro; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.contabilidad_libros ALTER COLUMN id_libro SET DEFAULT nextval('public.contabilidad_libros_id_libro_seq'::regclass);


--
-- Name: contabilidad_monedas id_moneda; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.contabilidad_monedas ALTER COLUMN id_moneda SET DEFAULT nextval('public.contabilidad_monedas_id_moneda_seq'::regclass);


--
-- Name: contabilidad_periodos_contables id_periodo_contable; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.contabilidad_periodos_contables ALTER COLUMN id_periodo_contable SET DEFAULT nextval('public.contabilidad_periodos_contables_id_periodo_contable_seq'::regclass);


--
-- Name: contabilidad_plan_cuentas id_cuenta; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.contabilidad_plan_cuentas ALTER COLUMN id_cuenta SET DEFAULT nextval('public.contabilidad_plan_cuentas_id_cuenta_seq'::regclass);


--
-- Name: contabilidad_registro_contable id_registro; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.contabilidad_registro_contable ALTER COLUMN id_registro SET DEFAULT nextval('public.contabilidad_registro_contable_id_registro_seq'::regclass);


--
-- Name: contabilidad_registro_contable_lineas id_linea_registro; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.contabilidad_registro_contable_lineas ALTER COLUMN id_linea_registro SET DEFAULT nextval('public.contabilidad_registro_contable_lineas_id_linea_registro_seq'::regclass);


--
-- Name: contabilidad_tipos_comprobantes id_tipo_comprobante; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.contabilidad_tipos_comprobantes ALTER COLUMN id_tipo_comprobante SET DEFAULT nextval('public.contabilidad_tipos_comprobantes_id_tipo_comprobante_seq'::regclass);


--
-- Name: contabilidad_tipos_cuenta id_tipo_cuenta; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.contabilidad_tipos_cuenta ALTER COLUMN id_tipo_cuenta SET DEFAULT nextval('public.contabilidad_tipos_cuenta_id_tipo_cuenta_seq'::regclass);


--
-- Name: contabilidad_tipos_operacion id_tipo_operacion; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.contabilidad_tipos_operacion ALTER COLUMN id_tipo_operacion SET DEFAULT nextval('public.contabilidad_tipos_operacion_id_tipo_operacion_seq'::regclass);


--
-- Name: depositos id_deposito; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.depositos ALTER COLUMN id_deposito SET DEFAULT nextval('public.depositos_id_deposito_seq'::regclass);


--
-- Name: fiscal_alicuotas_iva id_alicuota_iva; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.fiscal_alicuotas_iva ALTER COLUMN id_alicuota_iva SET DEFAULT nextval('public.fiscal_alicuotas_iva_id_alicuota_iva_seq'::regclass);


--
-- Name: fiscal_libro_iva_compras id_registro; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.fiscal_libro_iva_compras ALTER COLUMN id_registro SET DEFAULT nextval('public.fiscal_libro_iva_compras_id_registro_seq'::regclass);


--
-- Name: fiscal_libro_iva_compras_lineas id_linea_libro; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.fiscal_libro_iva_compras_lineas ALTER COLUMN id_linea_libro SET DEFAULT nextval('public.fiscal_libro_iva_compras_lineas_id_linea_libro_seq'::regclass);


--
-- Name: fiscal_libro_iva_ventas id_registro; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.fiscal_libro_iva_ventas ALTER COLUMN id_registro SET DEFAULT nextval('public.fiscal_libro_iva_ventas_id_factura_seq'::regclass);


--
-- Name: fiscal_libro_iva_ventas_lineas id_linea_libro; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.fiscal_libro_iva_ventas_lineas ALTER COLUMN id_linea_libro SET DEFAULT nextval('public.fiscal_libro_iva_ventas_lineas_id_linea_libro_seq'::regclass);


--
-- Name: fiscal_periodos_fiscales id_periodo_fiscal; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.fiscal_periodos_fiscales ALTER COLUMN id_periodo_fiscal SET DEFAULT nextval('public.fiscal_periodos_fiscales_id_periodo_fiscal_seq'::regclass);


--
-- Name: fiscal_responsabilidades_iva id_resoponsabildiad_iva; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.fiscal_responsabilidades_iva ALTER COLUMN id_resoponsabildiad_iva SET DEFAULT nextval('public.fiscal_responsabilidades_iva_id_resoponsabildiad_iva_seq'::regclass);


--
-- Name: legal_generos id_genero; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.legal_generos ALTER COLUMN id_genero SET DEFAULT nextval('public.legal_generos_id_genero_seq'::regclass);


--
-- Name: legal_tipos_documento id_tipo_documento; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.legal_tipos_documento ALTER COLUMN id_tipo_documento SET DEFAULT nextval('public.legal_tipos_documento_id_tipo_documento_seq'::regclass);


--
-- Name: legal_tipos_personeria id_tipo_personeria; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.legal_tipos_personeria ALTER COLUMN id_tipo_personeria SET DEFAULT nextval('public.legal_tipos_personeria_id_tipo_personeria_seq'::regclass);


--
-- Name: negocio_condiciones_operaciones id_condicion; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.negocio_condiciones_operaciones ALTER COLUMN id_condicion SET DEFAULT nextval('public.ventas_condiciones_id_condicion_venta_seq'::regclass);


--
-- Name: negocio_formas_pago id_forma_pago; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.negocio_formas_pago ALTER COLUMN id_forma_pago SET DEFAULT nextval('public.ventas_formas_pago_id_forma_pago_seq'::regclass);


--
-- Name: negocio_planes_pago id_plan; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.negocio_planes_pago ALTER COLUMN id_plan SET DEFAULT nextval('public.negocio_planes_pago_id_plan_seq'::regclass);


--
-- Name: negocio_planes_pago_detalle id_detalle_plan; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.negocio_planes_pago_detalle ALTER COLUMN id_detalle_plan SET DEFAULT nextval('public.negocio_planes_pago_detalle_id_detalle_plan_seq'::regclass);


--
-- Name: ofertas id_oferta; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.ofertas ALTER COLUMN id_oferta SET DEFAULT nextval('public.ofertas_id_oferta_seq'::regclass);


--
-- Name: ofertas_condiciones id_oferta_condicion; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.ofertas_condiciones ALTER COLUMN id_oferta_condicion SET DEFAULT nextval('public.ofertas_condiciones_id_oferta_condicion_seq'::regclass);


--
-- Name: personas id_persona; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.personas ALTER COLUMN id_persona SET DEFAULT nextval('public.personas_id_persona_seq'::regclass);


--
-- Name: personas_cuenta_corriente id_movimiento; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.personas_cuenta_corriente ALTER COLUMN id_movimiento SET DEFAULT nextval('public.personas_cuenta_corriente_id_movimiento_seq'::regclass);


--
-- Name: personas_imagenes id_imagen; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.personas_imagenes ALTER COLUMN id_imagen SET DEFAULT nextval('public.personas_imagenes_id_imagen_seq'::regclass);


--
-- Name: personas_telefonos id_telefono; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.personas_telefonos ALTER COLUMN id_telefono SET DEFAULT nextval('public.personas_telefonos_id_telefono_seq'::regclass);


--
-- Name: personas_tipos_imagenes id_tipo_imagen; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.personas_tipos_imagenes ALTER COLUMN id_tipo_imagen SET DEFAULT nextval('public.personas_tipos_imagenes_id_tipo_imagen_seq'::regclass);


--
-- Name: privilegios id_privilegio; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.privilegios ALTER COLUMN id_privilegio SET DEFAULT nextval('public.privilegios_id_privilegio_seq'::regclass);


--
-- Name: productos id_producto; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.productos ALTER COLUMN id_producto SET DEFAULT nextval('public.productos_id_producto_seq'::regclass);


--
-- Name: productos_caracteristicas id_caracteristica; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.productos_caracteristicas ALTER COLUMN id_caracteristica SET DEFAULT nextval('public.productos_caracteristicas_id_caracteristica_seq'::regclass);


--
-- Name: productos_imagenes id_imagen; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.productos_imagenes ALTER COLUMN id_imagen SET DEFAULT nextval('public.productos_imagenes_id_imagen_seq'::regclass);


--
-- Name: productos_marcas id_marca; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.productos_marcas ALTER COLUMN id_marca SET DEFAULT nextval('public.productos_marcas_id_marca_seq'::regclass);


--
-- Name: productos_porcentajes id_producto_porcentaje; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.productos_porcentajes ALTER COLUMN id_producto_porcentaje SET DEFAULT nextval('public.productos_porcentajes_id_producto_porcentaje_seq'::regclass);


--
-- Name: productos_precios productos_precios_id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.productos_precios ALTER COLUMN productos_precios_id SET DEFAULT nextval('public.productos_precios_productos_precios_id_seq'::regclass);


--
-- Name: productos_rubros id_rubro; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.productos_rubros ALTER COLUMN id_rubro SET DEFAULT nextval('public.productos_rubros_id_rubro_seq'::regclass);


--
-- Name: productos_sub_rubros id_sub_rubro; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.productos_sub_rubros ALTER COLUMN id_sub_rubro SET DEFAULT nextval('public.productos_sub_rubros_id_sub_rubro_seq'::regclass);


--
-- Name: productos_tipos_porcentajes id_tipo_porcentaje; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.productos_tipos_porcentajes ALTER COLUMN id_tipo_porcentaje SET DEFAULT nextval('public.productos_tipos_porcentajes_id_tipo_porcentaje_seq'::regclass);


--
-- Name: productos_tipos_proveeduria id_tipo_proveeduria; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.productos_tipos_proveeduria ALTER COLUMN id_tipo_proveeduria SET DEFAULT nextval('public.productos_tipos_proveeduria_id_tipo_proveeduria_seq'::regclass);


--
-- Name: productos_tipos_unidades id_tipo_unidad; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.productos_tipos_unidades ALTER COLUMN id_tipo_unidad SET DEFAULT nextval('public.productos_tipos_unidades_id_tipo_unidad_seq'::regclass);


--
-- Name: productos_x_caracteristicas id_caracteristica_x_producto; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.productos_x_caracteristicas ALTER COLUMN id_caracteristica_x_producto SET DEFAULT nextval('public.productos_x_caracteristicas_id_caracteristica_x_producto_seq'::regclass);


--
-- Name: productos_x_depositos id_producto_x_deposito; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.productos_x_depositos ALTER COLUMN id_producto_x_deposito SET DEFAULT nextval('public.productos_x_depositos_id_producto_x_deposito_seq'::regclass);


--
-- Name: proveedores_comprobantes id_comprobante; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.proveedores_comprobantes ALTER COLUMN id_comprobante SET DEFAULT nextval('public.proveedores_comprobantes_id_comprobante_seq'::regclass);


--
-- Name: proveedores_ordenes_compra id_orden_compra; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.proveedores_ordenes_compra ALTER COLUMN id_orden_compra SET DEFAULT nextval('public.proveedores_ordenes_compra_id_orden_compra_seq'::regclass);


--
-- Name: proveedores_ordenes_compra_lineas id_orden_compra_linea; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.proveedores_ordenes_compra_lineas ALTER COLUMN id_orden_compra_linea SET DEFAULT nextval('public.proveedores_ordenes_compra_lineas_id_orden_compra_linea_seq'::regclass);


--
-- Name: proveedores_ordenes_compra_lineas_porcentajes id_orden_compra_linea_porcentaje; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.proveedores_ordenes_compra_lineas_porcentajes ALTER COLUMN id_orden_compra_linea_porcentaje SET DEFAULT nextval('public.proveedores_ordenes_compra_li_id_orden_compra_linea_porcent_seq'::regclass);


--
-- Name: proveedores_ordenes_compra_porcentajes id_orden_compra_porcentaje; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.proveedores_ordenes_compra_porcentajes ALTER COLUMN id_orden_compra_porcentaje SET DEFAULT nextval('public.proveedores_ordenes_compra_porce_id_orden_compra_porcentaje_seq'::regclass);


--
-- Name: recibos id_recibo; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.recibos ALTER COLUMN id_recibo SET DEFAULT nextval('public.recibos_id_recibo_seq'::regclass);


--
-- Name: recibos_detalle id_detalle_recibo; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.recibos_detalle ALTER COLUMN id_detalle_recibo SET DEFAULT nextval('public.recibos_detalle_id_detalle_recibo_seq'::regclass);


--
-- Name: remitos id_remito; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.remitos ALTER COLUMN id_remito SET DEFAULT nextval('public.remitos_id_remito_seq'::regclass);


--
-- Name: remitos_detalle id_remito_detalle; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.remitos_detalle ALTER COLUMN id_remito_detalle SET DEFAULT nextval('public.remitos_detalle_id_remito_detalle_seq'::regclass);


--
-- Name: remitos_recepciones id_recepcion; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.remitos_recepciones ALTER COLUMN id_recepcion SET DEFAULT nextval('public.remitos_recepciones_id_recepcion_seq'::regclass);


--
-- Name: stock_movimientos id_movimiento_stock; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.stock_movimientos ALTER COLUMN id_movimiento_stock SET DEFAULT nextval('public.stock_movimientos_id_movimiento_stock_seq'::regclass);


--
-- Name: stock_movimientos_tipos id_tipo_movimiento; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.stock_movimientos_tipos ALTER COLUMN id_tipo_movimiento SET DEFAULT nextval('public.stock_movimientos_tipos_id_tipo_movimiento_seq'::regclass);


--
-- Name: sucursales id_sucursal; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.sucursales ALTER COLUMN id_sucursal SET DEFAULT nextval('public.sucursales_id_sucursal_seq'::regclass);


--
-- Name: ubicacion_localidades id_localidad; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.ubicacion_localidades ALTER COLUMN id_localidad SET DEFAULT nextval('public.ubicacion_localidades_id_localidad_seq'::regclass);


--
-- Name: ubicacion_paises id_pais; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.ubicacion_paises ALTER COLUMN id_pais SET DEFAULT nextval('public.ubicacion_paises_id_pais_seq'::regclass);


--
-- Name: ubicacion_provincias id_provincia; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.ubicacion_provincias ALTER COLUMN id_provincia SET DEFAULT nextval('public.ubicacion_provincias_id_provincia_seq'::regclass);


--
-- Name: usuarios id_usuario; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.usuarios ALTER COLUMN id_usuario SET DEFAULT nextval('public.usuarios_id_usuario_seq'::regclass);


--
-- Name: usuarios_grupos id_grupo; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.usuarios_grupos ALTER COLUMN id_grupo SET DEFAULT nextval('public.usuarios_grupos_id_grupo_seq'::regclass);


--
-- Name: valores id_valor; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.valores ALTER COLUMN id_valor SET DEFAULT nextval('public.valores_id_valor_seq'::regclass);


--
-- Name: afip_auth_services afip_auth_services_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.afip_auth_services
    ADD CONSTRAINT afip_auth_services_pkey PRIMARY KEY (nombre_servicio);


--
-- Name: bancos_cuenta_corriente bancos_cuenta_corriente_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.bancos_cuenta_corriente
    ADD CONSTRAINT bancos_cuenta_corriente_pkey PRIMARY KEY (id_movimiento);


--
-- Name: bancos_cuentas bancos_cuentas_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.bancos_cuentas
    ADD CONSTRAINT bancos_cuentas_pkey PRIMARY KEY (id_cuenta_banco);


--
-- Name: bancos bancos_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.bancos
    ADD CONSTRAINT bancos_pkey PRIMARY KEY (id_banco);


--
-- Name: bancos_tipos_cuenta bancos_tipos_cuenta_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.bancos_tipos_cuenta
    ADD CONSTRAINT bancos_tipos_cuenta_pkey PRIMARY KEY (id_tipo_cuenta_banco);


--
-- Name: caja_arqueos_detalle caja_arqueos_detalle_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.caja_arqueos_detalle
    ADD CONSTRAINT caja_arqueos_detalle_pkey PRIMARY KEY (id_arqueo_detalle);


--
-- Name: caja_arqueos caja_arqueos_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.caja_arqueos
    ADD CONSTRAINT caja_arqueos_pkey PRIMARY KEY (id_arqueo);


--
-- Name: cajas_movimientos cajas_movimientos_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.cajas_movimientos
    ADD CONSTRAINT cajas_movimientos_pkey PRIMARY KEY (id_movimiento_caja);


--
-- Name: cajas cajas_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.cajas
    ADD CONSTRAINT cajas_pkey PRIMARY KEY (id_caja);


--
-- Name: cajas_transferencias cajas_transferencias_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.cajas_transferencias
    ADD CONSTRAINT cajas_transferencias_pkey PRIMARY KEY (id_transferencia);


--
-- Name: cheques_terceros cheques_terceros_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.cheques_terceros
    ADD CONSTRAINT cheques_terceros_pkey PRIMARY KEY (id_valor);


--
-- Name: comprobantes_pagos comprobantes_pagos_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.comprobantes_pagos
    ADD CONSTRAINT comprobantes_pagos_pkey PRIMARY KEY (id_pago);


--
-- Name: contabilidad_libros contabilidad_libros_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.contabilidad_libros
    ADD CONSTRAINT contabilidad_libros_pkey PRIMARY KEY (id_libro);


--
-- Name: contabilidad_monedas contabilidad_monedas_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.contabilidad_monedas
    ADD CONSTRAINT contabilidad_monedas_pkey PRIMARY KEY (id_moneda);


--
-- Name: contabilidad_periodos_contables contabilidad_periodos_contables_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.contabilidad_periodos_contables
    ADD CONSTRAINT contabilidad_periodos_contables_pkey PRIMARY KEY (id_periodo_contable);


--
-- Name: contabilidad_plan_cuentas contabilidad_plan_cuentas_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.contabilidad_plan_cuentas
    ADD CONSTRAINT contabilidad_plan_cuentas_pkey PRIMARY KEY (id_cuenta);


--
-- Name: contabilidad_registro_contable_lineas contabilidad_registro_contable_lineas_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.contabilidad_registro_contable_lineas
    ADD CONSTRAINT contabilidad_registro_contable_lineas_pkey PRIMARY KEY (id_linea_registro);


--
-- Name: contabilidad_registro_contable contabilidad_registro_contable_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.contabilidad_registro_contable
    ADD CONSTRAINT contabilidad_registro_contable_pkey PRIMARY KEY (id_registro);


--
-- Name: contabilidad_tipos_comprobantes contabilidad_tipos_comprobantes_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.contabilidad_tipos_comprobantes
    ADD CONSTRAINT contabilidad_tipos_comprobantes_pkey PRIMARY KEY (id_tipo_comprobante);


--
-- Name: contabilidad_tipos_cuenta contabilidad_tipos_cuenta_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.contabilidad_tipos_cuenta
    ADD CONSTRAINT contabilidad_tipos_cuenta_pkey PRIMARY KEY (id_tipo_cuenta);


--
-- Name: contabilidad_tipos_operacion contabilidad_tipos_operacion_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.contabilidad_tipos_operacion
    ADD CONSTRAINT contabilidad_tipos_operacion_pkey PRIMARY KEY (id_tipo_operacion);


--
-- Name: cupones cupones_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.cupones
    ADD CONSTRAINT cupones_pkey PRIMARY KEY (id_valor);


--
-- Name: depositos depositos_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.depositos
    ADD CONSTRAINT depositos_pkey PRIMARY KEY (id_deposito);


--
-- Name: fiscal_alicuotas_iva fiscal_alicuotas_iva_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.fiscal_alicuotas_iva
    ADD CONSTRAINT fiscal_alicuotas_iva_pkey PRIMARY KEY (id_alicuota_iva);


--
-- Name: fiscal_letras_comprobantes fiscal_letras_comprobantes_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.fiscal_letras_comprobantes
    ADD CONSTRAINT fiscal_letras_comprobantes_pkey PRIMARY KEY (id_resoponsabildiad_iva_emisor, id_resoponsabildiad_iva_receptor);


--
-- Name: fiscal_libro_iva_compras_lineas fiscal_libro_iva_compras_lineas_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.fiscal_libro_iva_compras_lineas
    ADD CONSTRAINT fiscal_libro_iva_compras_lineas_pkey PRIMARY KEY (id_linea_libro);


--
-- Name: fiscal_libro_iva_compras fiscal_libro_iva_compras_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.fiscal_libro_iva_compras
    ADD CONSTRAINT fiscal_libro_iva_compras_pkey PRIMARY KEY (id_registro);


--
-- Name: fiscal_libro_iva_ventas_lineas fiscal_libro_iva_ventas_lineas_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.fiscal_libro_iva_ventas_lineas
    ADD CONSTRAINT fiscal_libro_iva_ventas_lineas_pkey PRIMARY KEY (id_linea_libro);


--
-- Name: fiscal_libro_iva_ventas fiscal_libro_iva_ventas_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.fiscal_libro_iva_ventas
    ADD CONSTRAINT fiscal_libro_iva_ventas_pkey PRIMARY KEY (id_registro);


--
-- Name: fiscal_monedas fiscal_monedas_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.fiscal_monedas
    ADD CONSTRAINT fiscal_monedas_pkey PRIMARY KEY (codigo_moneda);


--
-- Name: fiscal_periodos_fiscales fiscal_periodos_fiscales_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.fiscal_periodos_fiscales
    ADD CONSTRAINT fiscal_periodos_fiscales_pkey PRIMARY KEY (id_periodo_fiscal);


--
-- Name: fiscal_puntos_venta fiscal_puntos_venta_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.fiscal_puntos_venta
    ADD CONSTRAINT fiscal_puntos_venta_pkey PRIMARY KEY (nro_punto_venta);


--
-- Name: fiscal_responsabilidades_iva fiscal_responsabilidades_iva_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.fiscal_responsabilidades_iva
    ADD CONSTRAINT fiscal_responsabilidades_iva_pkey PRIMARY KEY (id_resoponsabildiad_iva);


--
-- Name: fiscal_tipos_comprobante fiscal_tipos_comprobante_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.fiscal_tipos_comprobante
    ADD CONSTRAINT fiscal_tipos_comprobante_pkey PRIMARY KEY (codigo_tipo_comprobante);


--
-- Name: legal_generos legal_generos_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.legal_generos
    ADD CONSTRAINT legal_generos_pkey PRIMARY KEY (id_genero);


--
-- Name: legal_tipos_documento legal_tipos_documento_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.legal_tipos_documento
    ADD CONSTRAINT legal_tipos_documento_pkey PRIMARY KEY (id_tipo_documento);


--
-- Name: legal_tipos_personeria legal_tipos_personeria_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.legal_tipos_personeria
    ADD CONSTRAINT legal_tipos_personeria_pkey PRIMARY KEY (id_tipo_personeria);


--
-- Name: negocio_planes_pago_detalle negocio_planes_pago_detalle_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.negocio_planes_pago_detalle
    ADD CONSTRAINT negocio_planes_pago_detalle_pkey PRIMARY KEY (id_detalle_plan);


--
-- Name: negocio_planes_pago_listas negocio_planes_pago_listas_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.negocio_planes_pago_listas
    ADD CONSTRAINT negocio_planes_pago_listas_pkey PRIMARY KEY (id_plan, id_lista_precio);


--
-- Name: negocio_planes_pago negocio_planes_pago_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.negocio_planes_pago
    ADD CONSTRAINT negocio_planes_pago_pkey PRIMARY KEY (id_plan);


--
-- Name: negocio_tipos_comprobante negocio_tipos_comprobante_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.negocio_tipos_comprobante
    ADD CONSTRAINT negocio_tipos_comprobante_pkey PRIMARY KEY (id_negocio_tipo_comprobante);


--
-- Name: ofertas_condiciones ofertas_condiciones_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.ofertas_condiciones
    ADD CONSTRAINT ofertas_condiciones_pkey PRIMARY KEY (id_oferta_condicion);


--
-- Name: ofertas ofertas_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.ofertas
    ADD CONSTRAINT ofertas_pkey PRIMARY KEY (id_oferta);


--
-- Name: parametros parametros_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.parametros
    ADD CONSTRAINT parametros_pkey PRIMARY KEY (nombre_parametro);


--
-- Name: personas_cuenta_corriente personas_cuenta_corriente_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.personas_cuenta_corriente
    ADD CONSTRAINT personas_cuenta_corriente_pkey PRIMARY KEY (id_movimiento);


--
-- Name: personas_imagenes personas_imagenes_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.personas_imagenes
    ADD CONSTRAINT personas_imagenes_pkey PRIMARY KEY (id_imagen);


--
-- Name: personas personas_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.personas
    ADD CONSTRAINT personas_pkey PRIMARY KEY (id_persona);


--
-- Name: personas_telefonos personas_telefonos_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.personas_telefonos
    ADD CONSTRAINT personas_telefonos_pkey PRIMARY KEY (id_telefono);


--
-- Name: personas_tipos_imagenes personas_tipos_imagenes_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.personas_tipos_imagenes
    ADD CONSTRAINT personas_tipos_imagenes_pkey PRIMARY KEY (id_tipo_imagen);


--
-- Name: privilegios_gruposx privilegios_gruposx_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.privilegios_gruposx
    ADD CONSTRAINT privilegios_gruposx_pkey PRIMARY KEY (id_privilegio, id_grupo);


--
-- Name: privilegios privilegios_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.privilegios
    ADD CONSTRAINT privilegios_pkey PRIMARY KEY (id_privilegio);


--
-- Name: productos_caracteristicas productos_caracteristicas_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.productos_caracteristicas
    ADD CONSTRAINT productos_caracteristicas_pkey PRIMARY KEY (id_caracteristica);


--
-- Name: productos_imagenes productos_imagenes_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.productos_imagenes
    ADD CONSTRAINT productos_imagenes_pkey PRIMARY KEY (id_imagen);


--
-- Name: productos_listas_precios productos_listas_precios_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.productos_listas_precios
    ADD CONSTRAINT productos_listas_precios_pkey PRIMARY KEY (id_lista_precio);


--
-- Name: productos_marcas productos_marcas_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.productos_marcas
    ADD CONSTRAINT productos_marcas_pkey PRIMARY KEY (id_marca);


--
-- Name: productos productos_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.productos
    ADD CONSTRAINT productos_pkey PRIMARY KEY (id_producto);


--
-- Name: productos_porcentajes productos_porcentajes_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.productos_porcentajes
    ADD CONSTRAINT productos_porcentajes_pkey PRIMARY KEY (id_producto_porcentaje);


--
-- Name: productos_precios productos_precios_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.productos_precios
    ADD CONSTRAINT productos_precios_pkey PRIMARY KEY (productos_precios_id);


--
-- Name: productos_precios productos_precios_unique; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.productos_precios
    ADD CONSTRAINT productos_precios_unique UNIQUE (id_producto, id_lista_precio);


--
-- Name: productos_rubros productos_rubros_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.productos_rubros
    ADD CONSTRAINT productos_rubros_pkey PRIMARY KEY (id_rubro);


--
-- Name: productos_sub_rubros productos_sub_rubros_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.productos_sub_rubros
    ADD CONSTRAINT productos_sub_rubros_pkey PRIMARY KEY (id_sub_rubro);


--
-- Name: productos_tipos_porcentajes productos_tipos_porcentajes_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.productos_tipos_porcentajes
    ADD CONSTRAINT productos_tipos_porcentajes_pkey PRIMARY KEY (id_tipo_porcentaje);


--
-- Name: productos_tipos_proveeduria productos_tipos_proveeduria_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.productos_tipos_proveeduria
    ADD CONSTRAINT productos_tipos_proveeduria_pkey PRIMARY KEY (id_tipo_proveeduria);


--
-- Name: productos_tipos_unidades productos_tipos_unidades_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.productos_tipos_unidades
    ADD CONSTRAINT productos_tipos_unidades_pkey PRIMARY KEY (id_tipo_unidad);


--
-- Name: productos_x_caracteristicas productos_x_caracteristicas_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.productos_x_caracteristicas
    ADD CONSTRAINT productos_x_caracteristicas_pkey PRIMARY KEY (id_caracteristica_x_producto);


--
-- Name: productos_x_depositos productos_x_depositos_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.productos_x_depositos
    ADD CONSTRAINT productos_x_depositos_pkey PRIMARY KEY (id_producto_x_deposito);


--
-- Name: proveedores_comprobantes proveedores_comprobantes_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.proveedores_comprobantes
    ADD CONSTRAINT proveedores_comprobantes_pkey PRIMARY KEY (id_comprobante);


--
-- Name: proveedores_ordenes_compra_estados proveedores_ordenes_compra_estados_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.proveedores_ordenes_compra_estados
    ADD CONSTRAINT proveedores_ordenes_compra_estados_pkey PRIMARY KEY (id_estado_orden_compra);


--
-- Name: proveedores_ordenes_compra_lineas proveedores_ordenes_compra_lineas_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.proveedores_ordenes_compra_lineas
    ADD CONSTRAINT proveedores_ordenes_compra_lineas_pkey PRIMARY KEY (id_orden_compra_linea);


--
-- Name: proveedores_ordenes_compra_lineas_porcentajes proveedores_ordenes_compra_lineas_porcentajes_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.proveedores_ordenes_compra_lineas_porcentajes
    ADD CONSTRAINT proveedores_ordenes_compra_lineas_porcentajes_pkey PRIMARY KEY (id_orden_compra_linea_porcentaje);


--
-- Name: proveedores_ordenes_compra proveedores_ordenes_compra_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.proveedores_ordenes_compra
    ADD CONSTRAINT proveedores_ordenes_compra_pkey PRIMARY KEY (id_orden_compra);


--
-- Name: proveedores_ordenes_compra_porcentajes proveedores_ordenes_compra_porcentajes_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.proveedores_ordenes_compra_porcentajes
    ADD CONSTRAINT proveedores_ordenes_compra_porcentajes_pkey PRIMARY KEY (id_orden_compra_porcentaje);


--
-- Name: recibos_detalle recibos_detalle_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.recibos_detalle
    ADD CONSTRAINT recibos_detalle_pkey PRIMARY KEY (id_detalle_recibo);


--
-- Name: recibos recibos_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.recibos
    ADD CONSTRAINT recibos_pkey PRIMARY KEY (id_recibo);


--
-- Name: remitos_detalle remitos_detalle_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.remitos_detalle
    ADD CONSTRAINT remitos_detalle_pkey PRIMARY KEY (id_remito_detalle);


--
-- Name: remitos_movimientos_tipos remitos_movimientos_tipos_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.remitos_movimientos_tipos
    ADD CONSTRAINT remitos_movimientos_tipos_pkey PRIMARY KEY (id_tipo_movimiento);


--
-- Name: remitos remitos_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.remitos
    ADD CONSTRAINT remitos_pkey PRIMARY KEY (id_remito);


--
-- Name: remitos_recepciones remitos_recepciones_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.remitos_recepciones
    ADD CONSTRAINT remitos_recepciones_pkey PRIMARY KEY (id_recepcion);


--
-- Name: schema_version schema_version_pk; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.schema_version
    ADD CONSTRAINT schema_version_pk PRIMARY KEY (version);


--
-- Name: stock_movimientos stock_movimientos_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.stock_movimientos
    ADD CONSTRAINT stock_movimientos_pkey PRIMARY KEY (id_movimiento_stock);


--
-- Name: stock_movimientos_tipos stock_movimientos_tipos_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.stock_movimientos_tipos
    ADD CONSTRAINT stock_movimientos_tipos_pkey PRIMARY KEY (id_tipo_movimiento);


--
-- Name: sucursales sucursales_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.sucursales
    ADD CONSTRAINT sucursales_pkey PRIMARY KEY (id_sucursal);


--
-- Name: ubicacion_localidades ubicacion_localidades_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.ubicacion_localidades
    ADD CONSTRAINT ubicacion_localidades_pkey PRIMARY KEY (id_localidad);


--
-- Name: ubicacion_paises ubicacion_paises_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.ubicacion_paises
    ADD CONSTRAINT ubicacion_paises_pkey PRIMARY KEY (id_pais);


--
-- Name: ubicacion_provincias ubicacion_provincias_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.ubicacion_provincias
    ADD CONSTRAINT ubicacion_provincias_pkey PRIMARY KEY (id_provincia);


--
-- Name: productos_x_depositos unique; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.productos_x_depositos
    ADD CONSTRAINT "unique" UNIQUE (id_producto, id_deposito);


--
-- Name: productos unique_codigo_propio; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.productos
    ADD CONSTRAINT unique_codigo_propio UNIQUE (codigo_propio);


--
-- Name: productos_x_caracteristicas unique_productos_x_caracteristicas; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.productos_x_caracteristicas
    ADD CONSTRAINT unique_productos_x_caracteristicas UNIQUE (id_caracteristica, id_producto);


--
-- Name: usuarios_grupos usuarios_grupos_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.usuarios_grupos
    ADD CONSTRAINT usuarios_grupos_pkey PRIMARY KEY (id_grupo);


--
-- Name: usuarios_gruposx usuarios_gruposx_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.usuarios_gruposx
    ADD CONSTRAINT usuarios_gruposx_pkey PRIMARY KEY (id_usuario, id_grupo);


--
-- Name: usuarios usuarios_login_key; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.usuarios
    ADD CONSTRAINT usuarios_login_key UNIQUE (login);


--
-- Name: usuarios usuarios_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.usuarios
    ADD CONSTRAINT usuarios_pkey PRIMARY KEY (id_usuario);


--
-- Name: valores valores_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.valores
    ADD CONSTRAINT valores_pkey PRIMARY KEY (id_valor);


--
-- Name: negocio_condiciones_operaciones ventas_condiciones_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.negocio_condiciones_operaciones
    ADD CONSTRAINT ventas_condiciones_pkey PRIMARY KEY (id_condicion);


--
-- Name: comprobantes_estados ventas_estados_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.comprobantes_estados
    ADD CONSTRAINT ventas_estados_pkey PRIMARY KEY (id_estado);


--
-- Name: negocio_formas_pago ventas_formas_pago_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.negocio_formas_pago
    ADD CONSTRAINT ventas_formas_pago_pkey PRIMARY KEY (id_forma_pago);


--
-- Name: comprobantes_lineas ventas_lineas_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.comprobantes_lineas
    ADD CONSTRAINT ventas_lineas_pkey PRIMARY KEY (id_linea_comprobante);


--
-- Name: comprobantes ventas_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.comprobantes
    ADD CONSTRAINT ventas_pkey PRIMARY KEY (id_comprobante);


--
-- Name: fk_bancos_cuenta_corriente_id_cuenta_banco_fkey_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX fk_bancos_cuenta_corriente_id_cuenta_banco_fkey_idx ON public.bancos_cuenta_corriente USING btree (id_cuenta_banco);


--
-- Name: fk_bancos_cuentas_id_banco_fkey_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX fk_bancos_cuentas_id_banco_fkey_idx ON public.bancos_cuentas USING btree (id_banco);


--
-- Name: fk_bancos_cuentas_id_moneda_fkey_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX fk_bancos_cuentas_id_moneda_fkey_idx ON public.bancos_cuentas USING btree (id_moneda);


--
-- Name: fk_bancos_cuentas_id_tipo_cuenta_banco_fkey_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX fk_bancos_cuentas_id_tipo_cuenta_banco_fkey_idx ON public.bancos_cuentas USING btree (id_tipo_cuenta_banco);


--
-- Name: fk_caja_arqueos_detalle_id_arqueo_fkey_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX fk_caja_arqueos_detalle_id_arqueo_fkey_idx ON public.caja_arqueos_detalle USING btree (id_arqueo);


--
-- Name: fk_caja_arqueos_detalle_id_forma_pago_fkey_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX fk_caja_arqueos_detalle_id_forma_pago_fkey_idx ON public.caja_arqueos_detalle USING btree (id_forma_pago);


--
-- Name: fk_caja_arqueos_id_caja_fkey_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX fk_caja_arqueos_id_caja_fkey_idx ON public.caja_arqueos USING btree (id_caja);


--
-- Name: fk_caja_arqueos_id_sucursal_fkey_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX fk_caja_arqueos_id_sucursal_fkey_idx ON public.caja_arqueos USING btree (id_sucursal);


--
-- Name: fk_caja_arqueos_id_usuario_arqueo_fkey_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX fk_caja_arqueos_id_usuario_arqueo_fkey_idx ON public.caja_arqueos USING btree (id_usuario_arqueo);


--
-- Name: fk_caja_arqueos_id_usuario_control_fkey_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX fk_caja_arqueos_id_usuario_control_fkey_idx ON public.caja_arqueos USING btree (id_usuario_control);


--
-- Name: fk_cajas_id_sucursal_fkey_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX fk_cajas_id_sucursal_fkey_idx ON public.cajas USING btree (id_sucursal);


--
-- Name: fk_cajas_id_usuario_fkey_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX fk_cajas_id_usuario_fkey_idx ON public.cajas USING btree (id_usuario);


--
-- Name: fk_cajas_movimientos_id_caja_fkey_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX fk_cajas_movimientos_id_caja_fkey_idx ON public.cajas_movimientos USING btree (id_caja);


--
-- Name: fk_comprobantes_id_negocio_tipo_comprobante_fkey_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX fk_comprobantes_id_negocio_tipo_comprobante_fkey_idx ON public.comprobantes USING btree (id_negocio_tipo_comprobante);


--
-- Name: fk_comprobantes_pagos_id_comprobante_fkey_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX fk_comprobantes_pagos_id_comprobante_fkey_idx ON public.comprobantes_pagos USING btree (id_comprobante);


--
-- Name: fk_comprobantes_pagos_id_detalle_plan_fkey_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX fk_comprobantes_pagos_id_detalle_plan_fkey_idx ON public.comprobantes_pagos USING btree (id_detalle_plan);


--
-- Name: fk_comprobantes_pagos_id_forma_pago_fkey_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX fk_comprobantes_pagos_id_forma_pago_fkey_idx ON public.comprobantes_pagos USING btree (id_forma_pago);


--
-- Name: fk_comprobantes_pagos_id_plan_fkey_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX fk_comprobantes_pagos_id_plan_fkey_idx ON public.comprobantes_pagos USING btree (id_plan);


--
-- Name: fk_contabilidad_plan_cuentas_id_cuenta_padre_fkey_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX fk_contabilidad_plan_cuentas_id_cuenta_padre_fkey_idx ON public.contabilidad_plan_cuentas USING btree (id_cuenta_padre);


--
-- Name: fk_contabilidad_plan_cuentas_id_tipo_cuenta_fkey_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX fk_contabilidad_plan_cuentas_id_tipo_cuenta_fkey_idx ON public.contabilidad_plan_cuentas USING btree (id_tipo_cuenta);


--
-- Name: fk_contabilidad_registro_contable_id_libro_fkey_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX fk_contabilidad_registro_contable_id_libro_fkey_idx ON public.contabilidad_registro_contable USING btree (id_libro);


--
-- Name: fk_contabilidad_registro_contable_id_periodo_contable_fkey_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX fk_contabilidad_registro_contable_id_periodo_contable_fkey_idx ON public.contabilidad_registro_contable USING btree (id_periodo_contable);


--
-- Name: fk_contabilidad_registro_contable_id_periodo_fiscal_fkey_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX fk_contabilidad_registro_contable_id_periodo_fiscal_fkey_idx ON public.contabilidad_registro_contable USING btree (id_periodo_fiscal);


--
-- Name: fk_contabilidad_registro_contable_id_tipo_comprobante_fkey_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX fk_contabilidad_registro_contable_id_tipo_comprobante_fkey_idx ON public.contabilidad_registro_contable USING btree (id_tipo_comprobante);


--
-- Name: fk_contabilidad_registro_contable_id_tipo_operacion_fkey_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX fk_contabilidad_registro_contable_id_tipo_operacion_fkey_idx ON public.contabilidad_registro_contable USING btree (id_tipo_operacion);


--
-- Name: fk_contabilidad_registro_contable_lineas_id_cuenta_fkey_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX fk_contabilidad_registro_contable_lineas_id_cuenta_fkey_idx ON public.contabilidad_registro_contable_lineas USING btree (id_cuenta);


--
-- Name: fk_contabilidad_registro_contable_lineas_id_registro_contable_f; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX fk_contabilidad_registro_contable_lineas_id_registro_contable_f ON public.contabilidad_registro_contable_lineas USING btree (id_registro_contable);


--
-- Name: fk_cupones_id_valor_fkey_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX fk_cupones_id_valor_fkey_idx ON public.cupones USING btree (id_valor);


--
-- Name: fk_depositos_id_localidad_fkey_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX fk_depositos_id_localidad_fkey_idx ON public.depositos USING btree (id_localidad);


--
-- Name: fk_depositos_id_pais_fkey_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX fk_depositos_id_pais_fkey_idx ON public.depositos USING btree (id_pais);


--
-- Name: fk_depositos_id_provincia_fkey_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX fk_depositos_id_provincia_fkey_idx ON public.depositos USING btree (id_provincia);


--
-- Name: fk_depositos_id_sucursal_fkey_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX fk_depositos_id_sucursal_fkey_idx ON public.depositos USING btree (id_sucursal);


--
-- Name: fk_fiscal_letras_comprobantes_id_resoponsabildiad_iva_emisor_fk; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX fk_fiscal_letras_comprobantes_id_resoponsabildiad_iva_emisor_fk ON public.fiscal_letras_comprobantes USING btree (id_resoponsabildiad_iva_emisor);


--
-- Name: fk_fiscal_letras_comprobantes_id_resoponsabildiad_iva_recepto_f; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX fk_fiscal_letras_comprobantes_id_resoponsabildiad_iva_recepto_f ON public.fiscal_letras_comprobantes USING btree (id_resoponsabildiad_iva_receptor);


--
-- Name: fk_fiscal_libro_iva_compras_codigo_tipo_comprobante_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX fk_fiscal_libro_iva_compras_codigo_tipo_comprobante_idx ON public.fiscal_libro_iva_compras USING btree (codigo_tipo_comprobante);


--
-- Name: fk_fiscal_libro_iva_compras_id_periodo_fiscal_fkey_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX fk_fiscal_libro_iva_compras_id_periodo_fiscal_fkey_idx ON public.fiscal_libro_iva_compras USING btree (id_periodo_fiscal);


--
-- Name: fk_fiscal_libro_iva_compras_id_persona_fkey_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX fk_fiscal_libro_iva_compras_id_persona_fkey_idx ON public.fiscal_libro_iva_compras USING btree (id_persona);


--
-- Name: fk_fiscal_libro_iva_compras_id_registro_contable_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX fk_fiscal_libro_iva_compras_id_registro_contable_idx ON public.fiscal_libro_iva_compras USING btree (id_registro_contable);


--
-- Name: fk_fiscal_libro_iva_compras_id_responsabilidad_iva_fkey_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX fk_fiscal_libro_iva_compras_id_responsabilidad_iva_fkey_idx ON public.fiscal_libro_iva_compras USING btree (id_responsabilidad_iva);


--
-- Name: fk_fiscal_libro_iva_compras_lineas_id_alicuota_iva_fkey_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX fk_fiscal_libro_iva_compras_lineas_id_alicuota_iva_fkey_idx ON public.fiscal_libro_iva_compras_lineas USING btree (id_alicuota_iva);


--
-- Name: fk_fiscal_libro_iva_compras_lineas_id_factura_fkey_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX fk_fiscal_libro_iva_compras_lineas_id_factura_fkey_idx ON public.fiscal_libro_iva_compras_lineas USING btree (id_registro);


--
-- Name: fk_fiscal_libro_iva_ventas_id_periodo_fiscal_fkey_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX fk_fiscal_libro_iva_ventas_id_periodo_fiscal_fkey_idx ON public.fiscal_libro_iva_ventas USING btree (id_periodo_fiscal);


--
-- Name: fk_fiscal_libro_iva_ventas_id_persona_fkey_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX fk_fiscal_libro_iva_ventas_id_persona_fkey_idx ON public.fiscal_libro_iva_ventas USING btree (id_persona);


--
-- Name: fk_fiscal_libro_iva_ventas_id_responsabilidad_iva_fkey_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX fk_fiscal_libro_iva_ventas_id_responsabilidad_iva_fkey_idx ON public.fiscal_libro_iva_ventas USING btree (id_responsabilidad_iva);


--
-- Name: fk_fiscal_libro_iva_ventas_lineas_id_alicuota_iva_fkey_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX fk_fiscal_libro_iva_ventas_lineas_id_alicuota_iva_fkey_idx ON public.fiscal_libro_iva_ventas_lineas USING btree (id_alicuota_iva);


--
-- Name: fk_fiscal_libro_iva_ventas_lineas_id_factura_fkey_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX fk_fiscal_libro_iva_ventas_lineas_id_factura_fkey_idx ON public.fiscal_libro_iva_ventas_lineas USING btree (id_registro);


--
-- Name: fk_fiscal_puntos_venta_id_sucursal_fkey_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX fk_fiscal_puntos_venta_id_sucursal_fkey_idx ON public.fiscal_puntos_venta USING btree (id_sucursal);


--
-- Name: fk_fiscal_tipos_comprobante_id_negocio_tipo_comprobante_fkey_id; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX fk_fiscal_tipos_comprobante_id_negocio_tipo_comprobante_fkey_id ON public.fiscal_tipos_comprobante USING btree (id_negocio_tipo_comprobante);


--
-- Name: fk_fk_estado_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX fk_fk_estado_idx ON public.comprobantes USING btree (id_estado);


--
-- Name: fk_fk_fiscal_libro_iva_ventas_codigo_tipo_comprobante_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX fk_fk_fiscal_libro_iva_ventas_codigo_tipo_comprobante_idx ON public.fiscal_libro_iva_ventas USING btree (codigo_tipo_comprobante);


--
-- Name: fk_fk_id_registro_contable_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX fk_fk_id_registro_contable_idx ON public.fiscal_libro_iva_ventas USING btree (id_registro_contable);


--
-- Name: fk_fk_registro_contable_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX fk_fk_registro_contable_idx ON public.personas_cuenta_corriente USING btree (id_registro_contable);


--
-- Name: fk_fk_sucursal_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX fk_fk_sucursal_idx ON public.personas USING btree (id_sucursal);


--
-- Name: fk_legal_generos_id_tipo_personeria_fkey_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX fk_legal_generos_id_tipo_personeria_fkey_idx ON public.legal_generos USING btree (id_tipo_personeria);


--
-- Name: fk_legal_tipos_documento_id_tipo_personeria_fkey_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX fk_legal_tipos_documento_id_tipo_personeria_fkey_idx ON public.legal_tipos_documento USING btree (id_tipo_personeria);


--
-- Name: fk_negocio_planes_pago_detalle_id_plan_fkey_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX fk_negocio_planes_pago_detalle_id_plan_fkey_idx ON public.negocio_planes_pago_detalle USING btree (id_plan);


--
-- Name: fk_negocio_planes_pago_id_forma_pago_fkey_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX fk_negocio_planes_pago_id_forma_pago_fkey_idx ON public.negocio_planes_pago USING btree (id_forma_pago);


--
-- Name: fk_negocio_planes_pago_listas_id_lista_precio_fkey_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX fk_negocio_planes_pago_listas_id_lista_precio_fkey_idx ON public.negocio_planes_pago_listas USING btree (id_lista_precio);


--
-- Name: fk_negocio_planes_pago_listas_id_plan_fkey_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX fk_negocio_planes_pago_listas_id_plan_fkey_idx ON public.negocio_planes_pago_listas USING btree (id_plan);


--
-- Name: fk_personas_cuenta_corriente_id_persona_fkey_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX fk_personas_cuenta_corriente_id_persona_fkey_idx ON public.personas_cuenta_corriente USING btree (id_persona);


--
-- Name: fk_personas_id_genero_fkey_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX fk_personas_id_genero_fkey_idx ON public.personas USING btree (id_genero);


--
-- Name: fk_personas_id_localidad_fkey_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX fk_personas_id_localidad_fkey_idx ON public.personas USING btree (id_localidad);


--
-- Name: fk_personas_id_pais_fkey_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX fk_personas_id_pais_fkey_idx ON public.personas USING btree (id_pais);


--
-- Name: fk_personas_id_provincia_fkey_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX fk_personas_id_provincia_fkey_idx ON public.personas USING btree (id_provincia);


--
-- Name: fk_personas_id_responsabilidad_iva_fkey_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX fk_personas_id_responsabilidad_iva_fkey_idx ON public.personas USING btree (id_responsabilidad_iva);


--
-- Name: fk_personas_id_tipo_documento_fkey_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX fk_personas_id_tipo_documento_fkey_idx ON public.personas USING btree (id_tipo_documento);


--
-- Name: fk_personas_id_tipo_personeria_fkey_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX fk_personas_id_tipo_personeria_fkey_idx ON public.personas USING btree (id_tipo_personeria);


--
-- Name: fk_personas_imagenes_id_persona_fkey_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX fk_personas_imagenes_id_persona_fkey_idx ON public.personas_imagenes USING btree (id_persona);


--
-- Name: fk_personas_imagenes_id_tipo_imagen_fkey_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX fk_personas_imagenes_id_tipo_imagen_fkey_idx ON public.personas_imagenes USING btree (id_tipo_imagen);


--
-- Name: fk_personas_telefonos_id_persona_fkey_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX fk_personas_telefonos_id_persona_fkey_idx ON public.personas_telefonos USING btree (id_persona);


--
-- Name: fk_privilegios_gruposx_id_grupo_fkey_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX fk_privilegios_gruposx_id_grupo_fkey_idx ON public.privilegios_gruposx USING btree (id_grupo);


--
-- Name: fk_privilegios_gruposx_id_privilegio_fkey_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX fk_privilegios_gruposx_id_privilegio_fkey_idx ON public.privilegios_gruposx USING btree (id_privilegio);


--
-- Name: fk_productos_id_alicuota_iva_fkey_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX fk_productos_id_alicuota_iva_fkey_idx ON public.productos USING btree (id_alicuota_iva);


--
-- Name: fk_productos_id_proveedor_habitual_fkey_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX fk_productos_id_proveedor_habitual_fkey_idx ON public.productos USING btree (id_proveedor_habitual);


--
-- Name: fk_productos_id_rubro_fkey_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX fk_productos_id_rubro_fkey_idx ON public.productos USING btree (id_rubro);


--
-- Name: fk_productos_id_sub_rubro_fkey_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX fk_productos_id_sub_rubro_fkey_idx ON public.productos USING btree (id_sub_rubro);


--
-- Name: fk_productos_id_tipo_proveeduria_fkey_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX fk_productos_id_tipo_proveeduria_fkey_idx ON public.productos USING btree (id_tipo_proveeduria);


--
-- Name: fk_productos_id_tipo_unidad_compra_fkey_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX fk_productos_id_tipo_unidad_compra_fkey_idx ON public.productos USING btree (id_tipo_unidad_compra);


--
-- Name: fk_productos_id_tipo_unidad_venta_fkey_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX fk_productos_id_tipo_unidad_venta_fkey_idx ON public.productos USING btree (id_tipo_unidad_venta);


--
-- Name: fk_productos_imagenes_id_producto_fkey_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX fk_productos_imagenes_id_producto_fkey_idx ON public.productos_imagenes USING btree (id_producto);


--
-- Name: fk_productos_imagenes_id_usuario_fkey_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX fk_productos_imagenes_id_usuario_fkey_idx ON public.productos_imagenes USING btree (id_usuario);


--
-- Name: fk_productos_marcas_fkey_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX fk_productos_marcas_fkey_idx ON public.productos USING btree (id_marca);


--
-- Name: fk_productos_porcentajes_id_producto_fkey_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX fk_productos_porcentajes_id_producto_fkey_idx ON public.productos_porcentajes USING btree (id_producto);


--
-- Name: fk_productos_porcentajes_id_tipo_porcentaje_fkey_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX fk_productos_porcentajes_id_tipo_porcentaje_fkey_idx ON public.productos_porcentajes USING btree (id_tipo_porcentaje);


--
-- Name: fk_productos_precios_id_lista_precio_fkey_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX fk_productos_precios_id_lista_precio_fkey_idx ON public.productos_precios USING btree (id_lista_precio);


--
-- Name: fk_productos_precios_id_producto_fkey_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX fk_productos_precios_id_producto_fkey_idx ON public.productos_precios USING btree (id_producto);


--
-- Name: fk_productos_sub_rubros_id_rubro_fkey_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX fk_productos_sub_rubros_id_rubro_fkey_idx ON public.productos_sub_rubros USING btree (id_rubro);


--
-- Name: fk_productos_x_caracteristicas_id_caracteristica_fkey_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX fk_productos_x_caracteristicas_id_caracteristica_fkey_idx ON public.productos_x_caracteristicas USING btree (id_caracteristica);


--
-- Name: fk_productos_x_caracteristicas_id_producto_fkey_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX fk_productos_x_caracteristicas_id_producto_fkey_idx ON public.productos_x_caracteristicas USING btree (id_producto);


--
-- Name: fk_proveedores_comprobantes_id_negocio_tipo_comprobante_fkey_id; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX fk_proveedores_comprobantes_id_negocio_tipo_comprobante_fkey_id ON public.proveedores_comprobantes USING btree (id_negocio_tipo_comprobante);


--
-- Name: fk_proveedores_comprobantes_id_proveedor_fkey_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX fk_proveedores_comprobantes_id_proveedor_fkey_idx ON public.proveedores_comprobantes USING btree (id_proveedor);


--
-- Name: fk_proveedores_comprobantes_id_registro_iva_fkey_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX fk_proveedores_comprobantes_id_registro_iva_fkey_idx ON public.proveedores_comprobantes USING btree (id_registro_iva);


--
-- Name: fk_proveedores_comprobantes_id_sucursal_fkey_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX fk_proveedores_comprobantes_id_sucursal_fkey_idx ON public.proveedores_comprobantes USING btree (id_sucursal);


--
-- Name: fk_proveedores_comprobantes_id_usuario_fkey_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX fk_proveedores_comprobantes_id_usuario_fkey_idx ON public.proveedores_comprobantes USING btree (id_usuario);


--
-- Name: fk_recibos_detalle_id_comprobante_pago_fkey_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX fk_recibos_detalle_id_comprobante_pago_fkey_idx ON public.recibos_detalle USING btree (id_comprobante_pago);


--
-- Name: fk_recibos_detalle_id_forma_pago_fkey_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX fk_recibos_detalle_id_forma_pago_fkey_idx ON public.recibos_detalle USING btree (id_forma_pago);


--
-- Name: fk_recibos_detalle_id_recibo_fkey_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX fk_recibos_detalle_id_recibo_fkey_idx ON public.recibos_detalle USING btree (id_recibo);


--
-- Name: fk_recibos_detalle_id_valor_fkey_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX fk_recibos_detalle_id_valor_fkey_idx ON public.recibos_detalle USING btree (id_valor);


--
-- Name: fk_recibos_id_caja_fkey_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX fk_recibos_id_caja_fkey_idx ON public.recibos USING btree (id_caja);


--
-- Name: fk_recibos_id_persona_fkey_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX fk_recibos_id_persona_fkey_idx ON public.recibos USING btree (id_persona);


--
-- Name: fk_recibos_id_usuario_fkey_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX fk_recibos_id_usuario_fkey_idx ON public.recibos USING btree (id_usuario);


--
-- Name: fk_stock_movimientos_id_deposito_movimiento_fkey_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX fk_stock_movimientos_id_deposito_movimiento_fkey_idx ON public.stock_movimientos USING btree (id_deposito_movimiento);


--
-- Name: fk_stock_movimientos_id_producto_fkey_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX fk_stock_movimientos_id_producto_fkey_idx ON public.stock_movimientos USING btree (id_producto);


--
-- Name: fk_stock_movimientos_id_usuario_fkey_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX fk_stock_movimientos_id_usuario_fkey_idx ON public.stock_movimientos USING btree (id_usuario);


--
-- Name: fk_sucursales_id_localidad_fkey_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX fk_sucursales_id_localidad_fkey_idx ON public.sucursales USING btree (id_localidad);


--
-- Name: fk_sucursales_id_pais_fkey_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX fk_sucursales_id_pais_fkey_idx ON public.sucursales USING btree (id_pais);


--
-- Name: fk_sucursales_id_provincia_fkey_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX fk_sucursales_id_provincia_fkey_idx ON public.sucursales USING btree (id_provincia);


--
-- Name: fk_ubicacion_localidades_id_provincia_fkey_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX fk_ubicacion_localidades_id_provincia_fkey_idx ON public.ubicacion_localidades USING btree (id_provincia);


--
-- Name: fk_ubicacion_provincias_id_pais_fkey_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX fk_ubicacion_provincias_id_pais_fkey_idx ON public.ubicacion_provincias USING btree (id_pais);


--
-- Name: fk_usuarios_gruposx_id_grupo_fkey_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX fk_usuarios_gruposx_id_grupo_fkey_idx ON public.usuarios_gruposx USING btree (id_grupo);


--
-- Name: fk_usuarios_gruposx_id_usuario_fkey_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX fk_usuarios_gruposx_id_usuario_fkey_idx ON public.usuarios_gruposx USING btree (id_usuario);


--
-- Name: fk_usuarios_id_sucursal_fkey_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX fk_usuarios_id_sucursal_fkey_idx ON public.usuarios USING btree (id_sucursal);


--
-- Name: fk_ventas_id_condicion_venta_fkey_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX fk_ventas_id_condicion_venta_fkey_idx ON public.comprobantes USING btree (id_condicion_comprobante);


--
-- Name: fk_ventas_id_persona_fkey_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX fk_ventas_id_persona_fkey_idx ON public.comprobantes USING btree (id_persona);


--
-- Name: fk_ventas_id_registro_iva_fkey_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX fk_ventas_id_registro_iva_fkey_idx ON public.comprobantes USING btree (id_registro_iva);


--
-- Name: fk_ventas_id_sucursal_fkey_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX fk_ventas_id_sucursal_fkey_idx ON public.comprobantes USING btree (id_sucursal);


--
-- Name: fk_ventas_id_usuario_fkey_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX fk_ventas_id_usuario_fkey_idx ON public.comprobantes USING btree (id_usuario);


--
-- Name: fk_ventas_lineas_id_producto_fkey_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX fk_ventas_lineas_id_producto_fkey_idx ON public.comprobantes_lineas USING btree (id_producto);


--
-- Name: fk_ventas_lineas_id_venta_fkey_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX fk_ventas_lineas_id_venta_fkey_idx ON public.comprobantes_lineas USING btree (id_comprobante);


--
-- Name: schema_version_ir_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX schema_version_ir_idx ON public.schema_version USING btree (installed_rank);


--
-- Name: schema_version_s_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX schema_version_s_idx ON public.schema_version USING btree (success);


--
-- Name: schema_version_vr_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX schema_version_vr_idx ON public.schema_version USING btree (version_rank);


--
-- Name: remitos_recepciones t_remitos_recep_existencias_after; Type: TRIGGER; Schema: public; Owner: -
--

CREATE TRIGGER t_remitos_recep_existencias_after AFTER INSERT OR DELETE OR UPDATE ON public.remitos_recepciones FOR EACH ROW EXECUTE FUNCTION public.actualizar_existencias_remitos_recep();


--
-- Name: remitos_recepciones t_remitos_recep_existencias_before; Type: TRIGGER; Schema: public; Owner: -
--

CREATE TRIGGER t_remitos_recep_existencias_before BEFORE INSERT OR DELETE OR UPDATE ON public.remitos_recepciones FOR EACH ROW EXECUTE FUNCTION public.actualizar_existencias_remitos_recep();


--
-- Name: bancos_cuenta_corriente bancos_cuenta_corriente_id_cuenta_banco_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.bancos_cuenta_corriente
    ADD CONSTRAINT bancos_cuenta_corriente_id_cuenta_banco_fkey FOREIGN KEY (id_cuenta_banco) REFERENCES public.bancos_cuentas(id_cuenta_banco);


--
-- Name: bancos_cuentas bancos_cuentas_id_banco_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.bancos_cuentas
    ADD CONSTRAINT bancos_cuentas_id_banco_fkey FOREIGN KEY (id_banco) REFERENCES public.bancos(id_banco);


--
-- Name: bancos_cuentas bancos_cuentas_id_moneda_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.bancos_cuentas
    ADD CONSTRAINT bancos_cuentas_id_moneda_fkey FOREIGN KEY (id_moneda) REFERENCES public.contabilidad_monedas(id_moneda);


--
-- Name: bancos_cuentas bancos_cuentas_id_tipo_cuenta_banco_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.bancos_cuentas
    ADD CONSTRAINT bancos_cuentas_id_tipo_cuenta_banco_fkey FOREIGN KEY (id_tipo_cuenta_banco) REFERENCES public.bancos_tipos_cuenta(id_tipo_cuenta_banco);


--
-- Name: bancos bancos_id_persona_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.bancos
    ADD CONSTRAINT bancos_id_persona_fkey FOREIGN KEY (id_persona) REFERENCES public.personas(id_persona);


--
-- Name: caja_arqueos_detalle caja_arqueos_detalle_id_arqueo_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.caja_arqueos_detalle
    ADD CONSTRAINT caja_arqueos_detalle_id_arqueo_fkey FOREIGN KEY (id_arqueo) REFERENCES public.caja_arqueos(id_arqueo);


--
-- Name: caja_arqueos_detalle caja_arqueos_detalle_id_forma_pago_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.caja_arqueos_detalle
    ADD CONSTRAINT caja_arqueos_detalle_id_forma_pago_fkey FOREIGN KEY (id_forma_pago) REFERENCES public.negocio_formas_pago(id_forma_pago);


--
-- Name: caja_arqueos caja_arqueos_id_caja_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.caja_arqueos
    ADD CONSTRAINT caja_arqueos_id_caja_fkey FOREIGN KEY (id_caja) REFERENCES public.cajas(id_caja);


--
-- Name: caja_arqueos caja_arqueos_id_sucursal_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.caja_arqueos
    ADD CONSTRAINT caja_arqueos_id_sucursal_fkey FOREIGN KEY (id_sucursal) REFERENCES public.sucursales(id_sucursal);


--
-- Name: caja_arqueos caja_arqueos_id_usuario_arqueo_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.caja_arqueos
    ADD CONSTRAINT caja_arqueos_id_usuario_arqueo_fkey FOREIGN KEY (id_usuario_arqueo) REFERENCES public.usuarios(id_usuario);


--
-- Name: caja_arqueos caja_arqueos_id_usuario_control_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.caja_arqueos
    ADD CONSTRAINT caja_arqueos_id_usuario_control_fkey FOREIGN KEY (id_usuario_control) REFERENCES public.usuarios(id_usuario);


--
-- Name: cajas cajas_id_sucursal_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.cajas
    ADD CONSTRAINT cajas_id_sucursal_fkey FOREIGN KEY (id_sucursal) REFERENCES public.sucursales(id_sucursal);


--
-- Name: cajas cajas_id_usuario_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.cajas
    ADD CONSTRAINT cajas_id_usuario_fkey FOREIGN KEY (id_usuario) REFERENCES public.usuarios(id_usuario);


--
-- Name: cajas_movimientos cajas_movimientos_id_caja_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.cajas_movimientos
    ADD CONSTRAINT cajas_movimientos_id_caja_fkey FOREIGN KEY (id_caja) REFERENCES public.cajas(id_caja);


--
-- Name: cajas_transferencias cajas_transferencias_id_caja_destino_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.cajas_transferencias
    ADD CONSTRAINT cajas_transferencias_id_caja_destino_fkey FOREIGN KEY (id_caja_destino) REFERENCES public.cajas(id_caja);


--
-- Name: cajas_transferencias cajas_transferencias_id_caja_origen_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.cajas_transferencias
    ADD CONSTRAINT cajas_transferencias_id_caja_origen_fkey FOREIGN KEY (id_caja_origen) REFERENCES public.cajas(id_caja);


--
-- Name: cajas_transferencias cajas_transferencias_id_forma_pago_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.cajas_transferencias
    ADD CONSTRAINT cajas_transferencias_id_forma_pago_fkey FOREIGN KEY (id_forma_pago) REFERENCES public.negocio_formas_pago(id_forma_pago);


--
-- Name: cheques_terceros cheques_terceros_id_banco_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.cheques_terceros
    ADD CONSTRAINT cheques_terceros_id_banco_fkey FOREIGN KEY (id_banco) REFERENCES public.bancos(id_banco);


--
-- Name: cheques_terceros cheques_terceros_id_valor_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.cheques_terceros
    ADD CONSTRAINT cheques_terceros_id_valor_fkey FOREIGN KEY (id_valor) REFERENCES public.valores(id_valor);


--
-- Name: comprobantes comprobantes_id_negocio_tipo_comprobante_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.comprobantes
    ADD CONSTRAINT comprobantes_id_negocio_tipo_comprobante_fkey FOREIGN KEY (id_negocio_tipo_comprobante) REFERENCES public.negocio_tipos_comprobante(id_negocio_tipo_comprobante);


--
-- Name: comprobantes_pagos comprobantes_pagos_id_comprobante_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.comprobantes_pagos
    ADD CONSTRAINT comprobantes_pagos_id_comprobante_fkey FOREIGN KEY (id_comprobante) REFERENCES public.comprobantes(id_comprobante);


--
-- Name: comprobantes_pagos comprobantes_pagos_id_detalle_plan_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.comprobantes_pagos
    ADD CONSTRAINT comprobantes_pagos_id_detalle_plan_fkey FOREIGN KEY (id_detalle_plan) REFERENCES public.negocio_planes_pago_detalle(id_detalle_plan);


--
-- Name: comprobantes_pagos comprobantes_pagos_id_forma_pago_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.comprobantes_pagos
    ADD CONSTRAINT comprobantes_pagos_id_forma_pago_fkey FOREIGN KEY (id_forma_pago) REFERENCES public.negocio_formas_pago(id_forma_pago);


--
-- Name: comprobantes_pagos comprobantes_pagos_id_plan_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.comprobantes_pagos
    ADD CONSTRAINT comprobantes_pagos_id_plan_fkey FOREIGN KEY (id_plan) REFERENCES public.negocio_planes_pago(id_plan);


--
-- Name: contabilidad_plan_cuentas contabilidad_plan_cuentas_id_cuenta_padre_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.contabilidad_plan_cuentas
    ADD CONSTRAINT contabilidad_plan_cuentas_id_cuenta_padre_fkey FOREIGN KEY (id_cuenta_padre) REFERENCES public.contabilidad_plan_cuentas(id_cuenta);


--
-- Name: contabilidad_plan_cuentas contabilidad_plan_cuentas_id_tipo_cuenta_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.contabilidad_plan_cuentas
    ADD CONSTRAINT contabilidad_plan_cuentas_id_tipo_cuenta_fkey FOREIGN KEY (id_tipo_cuenta) REFERENCES public.contabilidad_tipos_cuenta(id_tipo_cuenta);


--
-- Name: contabilidad_registro_contable contabilidad_registro_contable_id_libro_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.contabilidad_registro_contable
    ADD CONSTRAINT contabilidad_registro_contable_id_libro_fkey FOREIGN KEY (id_libro) REFERENCES public.contabilidad_libros(id_libro);


--
-- Name: contabilidad_registro_contable contabilidad_registro_contable_id_periodo_contable_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.contabilidad_registro_contable
    ADD CONSTRAINT contabilidad_registro_contable_id_periodo_contable_fkey FOREIGN KEY (id_periodo_contable) REFERENCES public.contabilidad_periodos_contables(id_periodo_contable);


--
-- Name: contabilidad_registro_contable contabilidad_registro_contable_id_periodo_fiscal_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.contabilidad_registro_contable
    ADD CONSTRAINT contabilidad_registro_contable_id_periodo_fiscal_fkey FOREIGN KEY (id_periodo_fiscal) REFERENCES public.fiscal_periodos_fiscales(id_periodo_fiscal);


--
-- Name: contabilidad_registro_contable contabilidad_registro_contable_id_tipo_comprobante_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.contabilidad_registro_contable
    ADD CONSTRAINT contabilidad_registro_contable_id_tipo_comprobante_fkey FOREIGN KEY (id_tipo_comprobante) REFERENCES public.contabilidad_tipos_comprobantes(id_tipo_comprobante);


--
-- Name: contabilidad_registro_contable contabilidad_registro_contable_id_tipo_operacion_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.contabilidad_registro_contable
    ADD CONSTRAINT contabilidad_registro_contable_id_tipo_operacion_fkey FOREIGN KEY (id_tipo_operacion) REFERENCES public.contabilidad_tipos_operacion(id_tipo_operacion);


--
-- Name: contabilidad_registro_contable_lineas contabilidad_registro_contable_lineas_id_cuenta_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.contabilidad_registro_contable_lineas
    ADD CONSTRAINT contabilidad_registro_contable_lineas_id_cuenta_fkey FOREIGN KEY (id_cuenta) REFERENCES public.contabilidad_plan_cuentas(id_cuenta);


--
-- Name: contabilidad_registro_contable_lineas contabilidad_registro_contable_lineas_id_registro_contable_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.contabilidad_registro_contable_lineas
    ADD CONSTRAINT contabilidad_registro_contable_lineas_id_registro_contable_fkey FOREIGN KEY (id_registro_contable) REFERENCES public.contabilidad_registro_contable(id_registro) ON DELETE CASCADE;


--
-- Name: cupones cupones_id_valor_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.cupones
    ADD CONSTRAINT cupones_id_valor_fkey FOREIGN KEY (id_valor) REFERENCES public.valores(id_valor);


--
-- Name: depositos depositos_id_localidad_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.depositos
    ADD CONSTRAINT depositos_id_localidad_fkey FOREIGN KEY (id_localidad) REFERENCES public.ubicacion_localidades(id_localidad);


--
-- Name: depositos depositos_id_pais_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.depositos
    ADD CONSTRAINT depositos_id_pais_fkey FOREIGN KEY (id_pais) REFERENCES public.ubicacion_paises(id_pais);


--
-- Name: depositos depositos_id_provincia_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.depositos
    ADD CONSTRAINT depositos_id_provincia_fkey FOREIGN KEY (id_provincia) REFERENCES public.ubicacion_provincias(id_provincia);


--
-- Name: depositos depositos_id_sucursal_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.depositos
    ADD CONSTRAINT depositos_id_sucursal_fkey FOREIGN KEY (id_sucursal) REFERENCES public.sucursales(id_sucursal);


--
-- Name: fiscal_letras_comprobantes fiscal_letras_comprobantes_id_resoponsabildiad_iva_emisor_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.fiscal_letras_comprobantes
    ADD CONSTRAINT fiscal_letras_comprobantes_id_resoponsabildiad_iva_emisor_fkey FOREIGN KEY (id_resoponsabildiad_iva_emisor) REFERENCES public.fiscal_responsabilidades_iva(id_resoponsabildiad_iva);


--
-- Name: fiscal_letras_comprobantes fiscal_letras_comprobantes_id_resoponsabildiad_iva_recepto_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.fiscal_letras_comprobantes
    ADD CONSTRAINT fiscal_letras_comprobantes_id_resoponsabildiad_iva_recepto_fkey FOREIGN KEY (id_resoponsabildiad_iva_receptor) REFERENCES public.fiscal_responsabilidades_iva(id_resoponsabildiad_iva);


--
-- Name: fiscal_libro_iva_compras fiscal_libro_iva_compras_id_periodo_fiscal_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.fiscal_libro_iva_compras
    ADD CONSTRAINT fiscal_libro_iva_compras_id_periodo_fiscal_fkey FOREIGN KEY (id_periodo_fiscal) REFERENCES public.fiscal_periodos_fiscales(id_periodo_fiscal);


--
-- Name: fiscal_libro_iva_compras fiscal_libro_iva_compras_id_persona_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.fiscal_libro_iva_compras
    ADD CONSTRAINT fiscal_libro_iva_compras_id_persona_fkey FOREIGN KEY (id_persona) REFERENCES public.personas(id_persona);


--
-- Name: fiscal_libro_iva_compras fiscal_libro_iva_compras_id_responsabilidad_iva_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.fiscal_libro_iva_compras
    ADD CONSTRAINT fiscal_libro_iva_compras_id_responsabilidad_iva_fkey FOREIGN KEY (id_responsabilidad_iva) REFERENCES public.fiscal_responsabilidades_iva(id_resoponsabildiad_iva);


--
-- Name: fiscal_libro_iva_compras_lineas fiscal_libro_iva_compras_lineas_id_alicuota_iva_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.fiscal_libro_iva_compras_lineas
    ADD CONSTRAINT fiscal_libro_iva_compras_lineas_id_alicuota_iva_fkey FOREIGN KEY (id_alicuota_iva) REFERENCES public.fiscal_alicuotas_iva(id_alicuota_iva);


--
-- Name: fiscal_libro_iva_compras_lineas fiscal_libro_iva_compras_lineas_id_factura_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.fiscal_libro_iva_compras_lineas
    ADD CONSTRAINT fiscal_libro_iva_compras_lineas_id_factura_fkey FOREIGN KEY (id_registro) REFERENCES public.fiscal_libro_iva_compras(id_registro);


--
-- Name: fiscal_libro_iva_ventas fiscal_libro_iva_ventas_id_periodo_fiscal_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.fiscal_libro_iva_ventas
    ADD CONSTRAINT fiscal_libro_iva_ventas_id_periodo_fiscal_fkey FOREIGN KEY (id_periodo_fiscal) REFERENCES public.fiscal_periodos_fiscales(id_periodo_fiscal);


--
-- Name: fiscal_libro_iva_ventas fiscal_libro_iva_ventas_id_persona_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.fiscal_libro_iva_ventas
    ADD CONSTRAINT fiscal_libro_iva_ventas_id_persona_fkey FOREIGN KEY (id_persona) REFERENCES public.personas(id_persona);


--
-- Name: fiscal_libro_iva_ventas fiscal_libro_iva_ventas_id_responsabilidad_iva_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.fiscal_libro_iva_ventas
    ADD CONSTRAINT fiscal_libro_iva_ventas_id_responsabilidad_iva_fkey FOREIGN KEY (id_responsabilidad_iva) REFERENCES public.fiscal_responsabilidades_iva(id_resoponsabildiad_iva);


--
-- Name: fiscal_libro_iva_ventas_lineas fiscal_libro_iva_ventas_lineas_id_alicuota_iva_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.fiscal_libro_iva_ventas_lineas
    ADD CONSTRAINT fiscal_libro_iva_ventas_lineas_id_alicuota_iva_fkey FOREIGN KEY (id_alicuota_iva) REFERENCES public.fiscal_alicuotas_iva(id_alicuota_iva);


--
-- Name: fiscal_libro_iva_ventas_lineas fiscal_libro_iva_ventas_lineas_id_factura_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.fiscal_libro_iva_ventas_lineas
    ADD CONSTRAINT fiscal_libro_iva_ventas_lineas_id_factura_fkey FOREIGN KEY (id_registro) REFERENCES public.fiscal_libro_iva_ventas(id_registro);


--
-- Name: fiscal_puntos_venta fiscal_puntos_venta_id_sucursal_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.fiscal_puntos_venta
    ADD CONSTRAINT fiscal_puntos_venta_id_sucursal_fkey FOREIGN KEY (id_sucursal) REFERENCES public.sucursales(id_sucursal);


--
-- Name: fiscal_tipos_comprobante fiscal_tipos_comprobante_id_negocio_tipo_comprobante_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.fiscal_tipos_comprobante
    ADD CONSTRAINT fiscal_tipos_comprobante_id_negocio_tipo_comprobante_fkey FOREIGN KEY (id_negocio_tipo_comprobante) REFERENCES public.negocio_tipos_comprobante(id_negocio_tipo_comprobante);


--
-- Name: productos_x_depositos fk_deposito; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.productos_x_depositos
    ADD CONSTRAINT fk_deposito FOREIGN KEY (id_deposito) REFERENCES public.depositos(id_deposito);


--
-- Name: remitos fk_destino_persona; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.remitos
    ADD CONSTRAINT fk_destino_persona FOREIGN KEY (destino_previsto_externo) REFERENCES public.personas(id_persona);


--
-- Name: remitos fk_destino_previsto_interno; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.remitos
    ADD CONSTRAINT fk_destino_previsto_interno FOREIGN KEY (destino_previsto_interno) REFERENCES public.depositos(id_deposito);


--
-- Name: comprobantes fk_estado; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.comprobantes
    ADD CONSTRAINT fk_estado FOREIGN KEY (id_estado) REFERENCES public.comprobantes_estados(id_estado);


--
-- Name: fiscal_libro_iva_ventas fk_fiscal_libro_iva_ventas_codigo_tipo_comprobante; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.fiscal_libro_iva_ventas
    ADD CONSTRAINT fk_fiscal_libro_iva_ventas_codigo_tipo_comprobante FOREIGN KEY (codigo_tipo_comprobante) REFERENCES public.fiscal_tipos_comprobante(codigo_tipo_comprobante);


--
-- Name: fiscal_libro_iva_compras fk_fiscal_libro_iva_ventas_codigo_tipo_comprobante; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.fiscal_libro_iva_compras
    ADD CONSTRAINT fk_fiscal_libro_iva_ventas_codigo_tipo_comprobante FOREIGN KEY (codigo_tipo_comprobante) REFERENCES public.fiscal_tipos_comprobante(codigo_tipo_comprobante);


--
-- Name: fiscal_libro_iva_ventas fk_id_registro_contable; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.fiscal_libro_iva_ventas
    ADD CONSTRAINT fk_id_registro_contable FOREIGN KEY (id_registro_contable) REFERENCES public.contabilidad_registro_contable(id_registro);


--
-- Name: fiscal_libro_iva_compras fk_id_registro_contable; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.fiscal_libro_iva_compras
    ADD CONSTRAINT fk_id_registro_contable FOREIGN KEY (id_registro_contable) REFERENCES public.contabilidad_registro_contable(id_registro);


--
-- Name: remitos fk_mercaderia_deposito; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.remitos
    ADD CONSTRAINT fk_mercaderia_deposito FOREIGN KEY (id_origen_interno) REFERENCES public.depositos(id_deposito);


--
-- Name: remitos fk_mercaderia_persona; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.remitos
    ADD CONSTRAINT fk_mercaderia_persona FOREIGN KEY (id_origen_externo) REFERENCES public.personas(id_persona);


--
-- Name: remitos_recepciones fk_persona; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.remitos_recepciones
    ADD CONSTRAINT fk_persona FOREIGN KEY (id_persona) REFERENCES public.personas(id_persona);


--
-- Name: productos_x_depositos fk_productos; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.productos_x_depositos
    ADD CONSTRAINT fk_productos FOREIGN KEY (id_producto) REFERENCES public.productos(id_producto);


--
-- Name: personas_cuenta_corriente fk_registro_contable; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.personas_cuenta_corriente
    ADD CONSTRAINT fk_registro_contable FOREIGN KEY (id_registro_contable) REFERENCES public.contabilidad_registro_contable(id_registro);


--
-- Name: remitos_recepciones fk_remito; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.remitos_recepciones
    ADD CONSTRAINT fk_remito FOREIGN KEY (id_remito) REFERENCES public.remitos(id_remito);


--
-- Name: remitos fk_remito_cabecera_usuario; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.remitos
    ADD CONSTRAINT fk_remito_cabecera_usuario FOREIGN KEY (id_usuario) REFERENCES public.usuarios(id_usuario);


--
-- Name: remitos_detalle fk_remito_detalle_producto; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.remitos_detalle
    ADD CONSTRAINT fk_remito_detalle_producto FOREIGN KEY (id_producto) REFERENCES public.productos(id_producto);


--
-- Name: remitos_detalle fk_remito_detalle_remito_cabecera; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.remitos_detalle
    ADD CONSTRAINT fk_remito_detalle_remito_cabecera FOREIGN KEY (id_remito) REFERENCES public.remitos(id_remito);


--
-- Name: remitos fk_remitos_movimientos_tipos; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.remitos
    ADD CONSTRAINT fk_remitos_movimientos_tipos FOREIGN KEY (id_tipo_movimiento) REFERENCES public.remitos_movimientos_tipos(id_tipo_movimiento);


--
-- Name: personas fk_sucursal; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.personas
    ADD CONSTRAINT fk_sucursal FOREIGN KEY (id_sucursal) REFERENCES public.sucursales(id_sucursal);


--
-- Name: remitos_recepciones fk_usuario; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.remitos_recepciones
    ADD CONSTRAINT fk_usuario FOREIGN KEY (id_usuario) REFERENCES public.usuarios(id_usuario);


--
-- Name: remitos_recepciones id_deposito; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.remitos_recepciones
    ADD CONSTRAINT id_deposito FOREIGN KEY (id_deposito) REFERENCES public.depositos(id_deposito);


--
-- Name: legal_generos legal_generos_id_tipo_personeria_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.legal_generos
    ADD CONSTRAINT legal_generos_id_tipo_personeria_fkey FOREIGN KEY (id_tipo_personeria) REFERENCES public.legal_tipos_personeria(id_tipo_personeria);


--
-- Name: legal_tipos_documento legal_tipos_documento_id_tipo_personeria_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.legal_tipos_documento
    ADD CONSTRAINT legal_tipos_documento_id_tipo_personeria_fkey FOREIGN KEY (id_tipo_personeria) REFERENCES public.legal_tipos_personeria(id_tipo_personeria);


--
-- Name: negocio_planes_pago_detalle negocio_planes_pago_detalle_id_plan_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.negocio_planes_pago_detalle
    ADD CONSTRAINT negocio_planes_pago_detalle_id_plan_fkey FOREIGN KEY (id_plan) REFERENCES public.negocio_planes_pago(id_plan);


--
-- Name: negocio_planes_pago negocio_planes_pago_id_forma_pago_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.negocio_planes_pago
    ADD CONSTRAINT negocio_planes_pago_id_forma_pago_fkey FOREIGN KEY (id_forma_pago) REFERENCES public.negocio_formas_pago(id_forma_pago);


--
-- Name: negocio_planes_pago_listas negocio_planes_pago_listas_id_lista_precio_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.negocio_planes_pago_listas
    ADD CONSTRAINT negocio_planes_pago_listas_id_lista_precio_fkey FOREIGN KEY (id_lista_precio) REFERENCES public.productos_listas_precios(id_lista_precio);


--
-- Name: negocio_planes_pago_listas negocio_planes_pago_listas_id_plan_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.negocio_planes_pago_listas
    ADD CONSTRAINT negocio_planes_pago_listas_id_plan_fkey FOREIGN KEY (id_plan) REFERENCES public.negocio_planes_pago(id_plan);


--
-- Name: ofertas_condiciones ofertas_condiciones_id_oferta_fk; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.ofertas_condiciones
    ADD CONSTRAINT ofertas_condiciones_id_oferta_fk FOREIGN KEY (id_oferta) REFERENCES public.ofertas(id_oferta);


--
-- Name: ofertas ofertas_id_sucursal_fk; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.ofertas
    ADD CONSTRAINT ofertas_id_sucursal_fk FOREIGN KEY (id_sucursal) REFERENCES public.sucursales(id_sucursal);


--
-- Name: personas_cuenta_corriente personas_cuenta_corriente_id_persona_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.personas_cuenta_corriente
    ADD CONSTRAINT personas_cuenta_corriente_id_persona_fkey FOREIGN KEY (id_persona) REFERENCES public.personas(id_persona);


--
-- Name: personas personas_id_genero_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.personas
    ADD CONSTRAINT personas_id_genero_fkey FOREIGN KEY (id_genero) REFERENCES public.legal_generos(id_genero);


--
-- Name: personas personas_id_localidad_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.personas
    ADD CONSTRAINT personas_id_localidad_fkey FOREIGN KEY (id_localidad) REFERENCES public.ubicacion_localidades(id_localidad);


--
-- Name: personas personas_id_pais_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.personas
    ADD CONSTRAINT personas_id_pais_fkey FOREIGN KEY (id_pais) REFERENCES public.ubicacion_paises(id_pais);


--
-- Name: personas personas_id_provincia_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.personas
    ADD CONSTRAINT personas_id_provincia_fkey FOREIGN KEY (id_provincia) REFERENCES public.ubicacion_provincias(id_provincia);


--
-- Name: personas personas_id_responsabilidad_iva_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.personas
    ADD CONSTRAINT personas_id_responsabilidad_iva_fkey FOREIGN KEY (id_responsabilidad_iva) REFERENCES public.fiscal_responsabilidades_iva(id_resoponsabildiad_iva);


--
-- Name: personas personas_id_tipo_documento_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.personas
    ADD CONSTRAINT personas_id_tipo_documento_fkey FOREIGN KEY (id_tipo_documento) REFERENCES public.legal_tipos_documento(id_tipo_documento);


--
-- Name: personas personas_id_tipo_personeria_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.personas
    ADD CONSTRAINT personas_id_tipo_personeria_fkey FOREIGN KEY (id_tipo_personeria) REFERENCES public.legal_tipos_personeria(id_tipo_personeria);


--
-- Name: personas_imagenes personas_imagenes_id_persona_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.personas_imagenes
    ADD CONSTRAINT personas_imagenes_id_persona_fkey FOREIGN KEY (id_persona) REFERENCES public.personas(id_persona);


--
-- Name: personas_imagenes personas_imagenes_id_tipo_imagen_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.personas_imagenes
    ADD CONSTRAINT personas_imagenes_id_tipo_imagen_fkey FOREIGN KEY (id_tipo_imagen) REFERENCES public.personas_tipos_imagenes(id_tipo_imagen);


--
-- Name: personas_telefonos personas_telefonos_id_persona_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.personas_telefonos
    ADD CONSTRAINT personas_telefonos_id_persona_fkey FOREIGN KEY (id_persona) REFERENCES public.personas(id_persona) ON DELETE CASCADE;


--
-- Name: privilegios_gruposx privilegios_gruposx_id_grupo_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.privilegios_gruposx
    ADD CONSTRAINT privilegios_gruposx_id_grupo_fkey FOREIGN KEY (id_grupo) REFERENCES public.usuarios_grupos(id_grupo);


--
-- Name: privilegios_gruposx privilegios_gruposx_id_privilegio_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.privilegios_gruposx
    ADD CONSTRAINT privilegios_gruposx_id_privilegio_fkey FOREIGN KEY (id_privilegio) REFERENCES public.privilegios(id_privilegio);


--
-- Name: productos productos_id_alicuota_iva_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.productos
    ADD CONSTRAINT productos_id_alicuota_iva_fkey FOREIGN KEY (id_alicuota_iva) REFERENCES public.fiscal_alicuotas_iva(id_alicuota_iva);


--
-- Name: productos productos_id_proveedor_habitual_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.productos
    ADD CONSTRAINT productos_id_proveedor_habitual_fkey FOREIGN KEY (id_proveedor_habitual) REFERENCES public.personas(id_persona);


--
-- Name: productos productos_id_rubro_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.productos
    ADD CONSTRAINT productos_id_rubro_fkey FOREIGN KEY (id_rubro) REFERENCES public.productos_rubros(id_rubro);


--
-- Name: productos productos_id_sub_rubro_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.productos
    ADD CONSTRAINT productos_id_sub_rubro_fkey FOREIGN KEY (id_sub_rubro) REFERENCES public.productos_sub_rubros(id_sub_rubro);


--
-- Name: productos productos_id_tipo_proveeduria_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.productos
    ADD CONSTRAINT productos_id_tipo_proveeduria_fkey FOREIGN KEY (id_tipo_proveeduria) REFERENCES public.productos_tipos_proveeduria(id_tipo_proveeduria);


--
-- Name: productos productos_id_tipo_unidad_compra_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.productos
    ADD CONSTRAINT productos_id_tipo_unidad_compra_fkey FOREIGN KEY (id_tipo_unidad_compra) REFERENCES public.productos_tipos_unidades(id_tipo_unidad);


--
-- Name: productos productos_id_tipo_unidad_venta_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.productos
    ADD CONSTRAINT productos_id_tipo_unidad_venta_fkey FOREIGN KEY (id_tipo_unidad_venta) REFERENCES public.productos_tipos_unidades(id_tipo_unidad);


--
-- Name: productos_imagenes productos_imagenes_id_producto_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.productos_imagenes
    ADD CONSTRAINT productos_imagenes_id_producto_fkey FOREIGN KEY (id_producto) REFERENCES public.productos(id_producto);


--
-- Name: productos_imagenes productos_imagenes_id_usuario_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.productos_imagenes
    ADD CONSTRAINT productos_imagenes_id_usuario_fkey FOREIGN KEY (id_usuario) REFERENCES public.usuarios(id_usuario);


--
-- Name: productos productos_marcas_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.productos
    ADD CONSTRAINT productos_marcas_fkey FOREIGN KEY (id_marca) REFERENCES public.productos_marcas(id_marca);


--
-- Name: productos_porcentajes productos_porcentajes_id_producto_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.productos_porcentajes
    ADD CONSTRAINT productos_porcentajes_id_producto_fkey FOREIGN KEY (id_producto) REFERENCES public.productos(id_producto) ON DELETE CASCADE;


--
-- Name: productos_porcentajes productos_porcentajes_id_tipo_porcentaje_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.productos_porcentajes
    ADD CONSTRAINT productos_porcentajes_id_tipo_porcentaje_fkey FOREIGN KEY (id_tipo_porcentaje) REFERENCES public.productos_tipos_porcentajes(id_tipo_porcentaje);


--
-- Name: productos_precios productos_precios_id_lista_precio_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.productos_precios
    ADD CONSTRAINT productos_precios_id_lista_precio_fkey FOREIGN KEY (id_lista_precio) REFERENCES public.productos_listas_precios(id_lista_precio);


--
-- Name: productos_precios productos_precios_id_producto_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.productos_precios
    ADD CONSTRAINT productos_precios_id_producto_fkey FOREIGN KEY (id_producto) REFERENCES public.productos(id_producto);


--
-- Name: productos_sub_rubros productos_sub_rubros_id_rubro_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.productos_sub_rubros
    ADD CONSTRAINT productos_sub_rubros_id_rubro_fkey FOREIGN KEY (id_rubro) REFERENCES public.productos_rubros(id_rubro);


--
-- Name: productos_x_caracteristicas productos_x_caracteristicas_id_caracteristica_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.productos_x_caracteristicas
    ADD CONSTRAINT productos_x_caracteristicas_id_caracteristica_fkey FOREIGN KEY (id_caracteristica) REFERENCES public.productos_caracteristicas(id_caracteristica);


--
-- Name: productos_x_caracteristicas productos_x_caracteristicas_id_producto_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.productos_x_caracteristicas
    ADD CONSTRAINT productos_x_caracteristicas_id_producto_fkey FOREIGN KEY (id_producto) REFERENCES public.productos(id_producto);


--
-- Name: proveedores_comprobantes proveedores_comprobantes_id_negocio_tipo_comprobante_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.proveedores_comprobantes
    ADD CONSTRAINT proveedores_comprobantes_id_negocio_tipo_comprobante_fkey FOREIGN KEY (id_negocio_tipo_comprobante) REFERENCES public.negocio_tipos_comprobante(id_negocio_tipo_comprobante);


--
-- Name: proveedores_comprobantes proveedores_comprobantes_id_proveedor_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.proveedores_comprobantes
    ADD CONSTRAINT proveedores_comprobantes_id_proveedor_fkey FOREIGN KEY (id_proveedor) REFERENCES public.personas(id_persona);


--
-- Name: proveedores_comprobantes proveedores_comprobantes_id_registro_iva_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.proveedores_comprobantes
    ADD CONSTRAINT proveedores_comprobantes_id_registro_iva_fkey FOREIGN KEY (id_registro_iva) REFERENCES public.fiscal_libro_iva_compras(id_registro);


--
-- Name: proveedores_comprobantes proveedores_comprobantes_id_sucursal_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.proveedores_comprobantes
    ADD CONSTRAINT proveedores_comprobantes_id_sucursal_fkey FOREIGN KEY (id_sucursal) REFERENCES public.sucursales(id_sucursal);


--
-- Name: proveedores_comprobantes proveedores_comprobantes_id_usuario_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.proveedores_comprobantes
    ADD CONSTRAINT proveedores_comprobantes_id_usuario_fkey FOREIGN KEY (id_usuario) REFERENCES public.usuarios(id_usuario);


--
-- Name: proveedores_ordenes_compra proveedores_ordenes_compra_id_estado_orden_compra_fk; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.proveedores_ordenes_compra
    ADD CONSTRAINT proveedores_ordenes_compra_id_estado_orden_compra_fk FOREIGN KEY (id_estado_orden_compra) REFERENCES public.proveedores_ordenes_compra_estados(id_estado_orden_compra);


--
-- Name: proveedores_ordenes_compra proveedores_ordenes_compra_id_proveedor_fk; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.proveedores_ordenes_compra
    ADD CONSTRAINT proveedores_ordenes_compra_id_proveedor_fk FOREIGN KEY (id_proveedor) REFERENCES public.personas(id_persona);


--
-- Name: proveedores_ordenes_compra proveedores_ordenes_compra_id_transporte_fk; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.proveedores_ordenes_compra
    ADD CONSTRAINT proveedores_ordenes_compra_id_transporte_fk FOREIGN KEY (id_transporte) REFERENCES public.personas(id_persona);


--
-- Name: proveedores_ordenes_compra proveedores_ordenes_compra_id_usuario_fk; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.proveedores_ordenes_compra
    ADD CONSTRAINT proveedores_ordenes_compra_id_usuario_fk FOREIGN KEY (id_usuario) REFERENCES public.usuarios(id_usuario);


--
-- Name: proveedores_ordenes_compra_lineas proveedores_ordenes_compra_lineas_id_orden_compra_fk; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.proveedores_ordenes_compra_lineas
    ADD CONSTRAINT proveedores_ordenes_compra_lineas_id_orden_compra_fk FOREIGN KEY (id_orden_compra) REFERENCES public.proveedores_ordenes_compra(id_orden_compra);


--
-- Name: proveedores_ordenes_compra_lineas proveedores_ordenes_compra_lineas_id_producto_fk; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.proveedores_ordenes_compra_lineas
    ADD CONSTRAINT proveedores_ordenes_compra_lineas_id_producto_fk FOREIGN KEY (id_producto) REFERENCES public.productos(id_producto);


--
-- Name: proveedores_ordenes_compra_lineas proveedores_ordenes_compra_lineas_id_tipo_unidad_fk; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.proveedores_ordenes_compra_lineas
    ADD CONSTRAINT proveedores_ordenes_compra_lineas_id_tipo_unidad_fk FOREIGN KEY (id_tipo_unidad) REFERENCES public.productos_tipos_unidades(id_tipo_unidad);


--
-- Name: proveedores_ordenes_compra_lineas_porcentajes proveedores_ordenes_compra_lineas_porcentajes_id_orden_compra_l; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.proveedores_ordenes_compra_lineas_porcentajes
    ADD CONSTRAINT proveedores_ordenes_compra_lineas_porcentajes_id_orden_compra_l FOREIGN KEY (id_orden_compra_linea) REFERENCES public.proveedores_ordenes_compra_lineas(id_orden_compra_linea);


--
-- Name: proveedores_ordenes_compra_lineas_porcentajes proveedores_ordenes_compra_lineas_porcentajes_id_tipo_porcentaj; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.proveedores_ordenes_compra_lineas_porcentajes
    ADD CONSTRAINT proveedores_ordenes_compra_lineas_porcentajes_id_tipo_porcentaj FOREIGN KEY (id_tipo_porcentaje) REFERENCES public.productos_tipos_porcentajes(id_tipo_porcentaje);


--
-- Name: proveedores_ordenes_compra_porcentajes proveedores_ordenes_compra_porcentajes_id_orden_compra_fk; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.proveedores_ordenes_compra_porcentajes
    ADD CONSTRAINT proveedores_ordenes_compra_porcentajes_id_orden_compra_fk FOREIGN KEY (id_orden_compra) REFERENCES public.proveedores_ordenes_compra(id_orden_compra);


--
-- Name: proveedores_ordenes_compra_porcentajes proveedores_ordenes_compra_porcentajes_id_tipo_porcentaje_fk; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.proveedores_ordenes_compra_porcentajes
    ADD CONSTRAINT proveedores_ordenes_compra_porcentajes_id_tipo_porcentaje_fk FOREIGN KEY (id_tipo_porcentaje) REFERENCES public.productos_tipos_porcentajes(id_tipo_porcentaje);


--
-- Name: recibos_detalle recibos_detalle_id_comprobante_pago_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.recibos_detalle
    ADD CONSTRAINT recibos_detalle_id_comprobante_pago_fkey FOREIGN KEY (id_comprobante_pago) REFERENCES public.comprobantes_pagos(id_pago);


--
-- Name: recibos_detalle recibos_detalle_id_forma_pago_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.recibos_detalle
    ADD CONSTRAINT recibos_detalle_id_forma_pago_fkey FOREIGN KEY (id_forma_pago) REFERENCES public.negocio_formas_pago(id_forma_pago);


--
-- Name: recibos_detalle recibos_detalle_id_recibo_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.recibos_detalle
    ADD CONSTRAINT recibos_detalle_id_recibo_fkey FOREIGN KEY (id_recibo) REFERENCES public.recibos(id_recibo);


--
-- Name: recibos_detalle recibos_detalle_id_valor_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.recibos_detalle
    ADD CONSTRAINT recibos_detalle_id_valor_fkey FOREIGN KEY (id_valor) REFERENCES public.valores(id_valor);


--
-- Name: recibos recibos_id_caja_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.recibos
    ADD CONSTRAINT recibos_id_caja_fkey FOREIGN KEY (id_caja) REFERENCES public.cajas(id_caja);


--
-- Name: recibos recibos_id_persona_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.recibos
    ADD CONSTRAINT recibos_id_persona_fkey FOREIGN KEY (id_persona) REFERENCES public.personas(id_persona);


--
-- Name: recibos recibos_id_usuario_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.recibos
    ADD CONSTRAINT recibos_id_usuario_fkey FOREIGN KEY (id_usuario) REFERENCES public.usuarios(id_usuario);


--
-- Name: stock_movimientos stock_movimientos_id_deposito_movimiento_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.stock_movimientos
    ADD CONSTRAINT stock_movimientos_id_deposito_movimiento_fkey FOREIGN KEY (id_deposito_movimiento) REFERENCES public.depositos(id_deposito);


--
-- Name: stock_movimientos stock_movimientos_id_producto_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.stock_movimientos
    ADD CONSTRAINT stock_movimientos_id_producto_fkey FOREIGN KEY (id_producto) REFERENCES public.productos(id_producto);


--
-- Name: stock_movimientos stock_movimientos_id_usuario_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.stock_movimientos
    ADD CONSTRAINT stock_movimientos_id_usuario_fkey FOREIGN KEY (id_usuario) REFERENCES public.usuarios(id_usuario);


--
-- Name: sucursales sucursales_id_localidad_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.sucursales
    ADD CONSTRAINT sucursales_id_localidad_fkey FOREIGN KEY (id_localidad) REFERENCES public.ubicacion_localidades(id_localidad);


--
-- Name: sucursales sucursales_id_pais_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.sucursales
    ADD CONSTRAINT sucursales_id_pais_fkey FOREIGN KEY (id_pais) REFERENCES public.ubicacion_paises(id_pais);


--
-- Name: sucursales sucursales_id_provincia_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.sucursales
    ADD CONSTRAINT sucursales_id_provincia_fkey FOREIGN KEY (id_provincia) REFERENCES public.ubicacion_provincias(id_provincia);


--
-- Name: ubicacion_localidades ubicacion_localidades_id_provincia_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.ubicacion_localidades
    ADD CONSTRAINT ubicacion_localidades_id_provincia_fkey FOREIGN KEY (id_provincia) REFERENCES public.ubicacion_provincias(id_provincia);


--
-- Name: ubicacion_provincias ubicacion_provincias_id_pais_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.ubicacion_provincias
    ADD CONSTRAINT ubicacion_provincias_id_pais_fkey FOREIGN KEY (id_pais) REFERENCES public.ubicacion_paises(id_pais);


--
-- Name: usuarios_gruposx usuarios_gruposx_id_grupo_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.usuarios_gruposx
    ADD CONSTRAINT usuarios_gruposx_id_grupo_fkey FOREIGN KEY (id_grupo) REFERENCES public.usuarios_grupos(id_grupo);


--
-- Name: usuarios_gruposx usuarios_gruposx_id_usuario_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.usuarios_gruposx
    ADD CONSTRAINT usuarios_gruposx_id_usuario_fkey FOREIGN KEY (id_usuario) REFERENCES public.usuarios(id_usuario);


--
-- Name: usuarios usuarios_id_sucursal_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.usuarios
    ADD CONSTRAINT usuarios_id_sucursal_fkey FOREIGN KEY (id_sucursal) REFERENCES public.sucursales(id_sucursal);


--
-- Name: comprobantes ventas_id_condicion_venta_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.comprobantes
    ADD CONSTRAINT ventas_id_condicion_venta_fkey FOREIGN KEY (id_condicion_comprobante) REFERENCES public.negocio_condiciones_operaciones(id_condicion);


--
-- Name: comprobantes ventas_id_persona_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.comprobantes
    ADD CONSTRAINT ventas_id_persona_fkey FOREIGN KEY (id_persona) REFERENCES public.personas(id_persona);


--
-- Name: comprobantes ventas_id_registro_iva_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.comprobantes
    ADD CONSTRAINT ventas_id_registro_iva_fkey FOREIGN KEY (id_registro_iva) REFERENCES public.fiscal_libro_iva_ventas(id_registro);


--
-- Name: comprobantes ventas_id_sucursal_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.comprobantes
    ADD CONSTRAINT ventas_id_sucursal_fkey FOREIGN KEY (id_sucursal) REFERENCES public.sucursales(id_sucursal);


--
-- Name: comprobantes ventas_id_usuario_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.comprobantes
    ADD CONSTRAINT ventas_id_usuario_fkey FOREIGN KEY (id_usuario) REFERENCES public.usuarios(id_usuario);


--
-- Name: comprobantes_lineas ventas_lineas_id_producto_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.comprobantes_lineas
    ADD CONSTRAINT ventas_lineas_id_producto_fkey FOREIGN KEY (id_producto) REFERENCES public.productos(id_producto);


--
-- Name: comprobantes_lineas ventas_lineas_id_venta_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.comprobantes_lineas
    ADD CONSTRAINT ventas_lineas_id_venta_fkey FOREIGN KEY (id_comprobante) REFERENCES public.comprobantes(id_comprobante);


--
-- PostgreSQL database dump complete
--
