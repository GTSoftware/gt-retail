package ar.com.gtsoftware.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Parametros {

    ID_CLIENTE_DEFECTO("venta.pos.id_cliente.defecto", Long.class),
    ID_PRODUCTO_REDONDEO("venta.pos.redondeo.id_producto", Long.class),
    ID_LISTA_VENTA("venta.pos.id_lista", Long.class),
    ID_CONDICION_VENTA_DEFECTO("venta.pos.id_condicion.defecto", Long.class),
    ID_FORMA_PAGO_DEFECTO("venta.pos.id_forma_pago.defecto", Long.class),
    CUIT_EMPRESA("empresa.cuit", String.class),

    AFIP_WSFE_ENDPOINT("afip.wsfe.endpoint", String.class),
    AFIP_CERT_PATH("afip.wsaa.cert.path", String.class),
    AFIP_CERT_PASSWORD("afip.wsaa.cert.password", String.class),
    AFIP_DN_PARAM("afip.wsaa.dn", String.class),

    PRESUPUESTO_MOSTRAR_DETALLE_PRECIOS("presupuesto.impresion.mostrar_detalle_precios", Boolean.class),

    EMPRESA_CUIT("empresa.cuit", String.class),
    EMPRESA_DIRECCION("empresa.direccion", String.class),
    EMPRESA_EMAIL("empresa.email", String.class),
    EMPRESA_FECHA_INICIO("empresa.fechainicio", String.class),
    EMPRESA_LOCALIDAD("empresa.localidad", String.class),
    EMPRESA_NOMBRE("empresa.nombre", String.class),
    EMPRESA_NOMBRE_FANTASIA("empresa.nombre_fantasia", String.class),
    EMPRESA_PROVINCIA("empresa.provincia", String.class),
    EMPRESA_RAZON_SOCIAL("empresa.razon_social", String.class),
    EMPRESA_TELEFONO("empresa.telefono", String.class);


    private String nombreParametro;
    private Class tipoParametro;

}
