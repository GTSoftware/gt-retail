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
    AFIP_WSFE_ENDPOINT("afip.wsfe.endpoint", String.class);

    private String nombreParametro;
    private Class tipoParametro;

}
