package ar.com.gtsoftware.api.exception;

import ar.com.gtsoftware.exception.ExceptionErrorCode;

@ExceptionErrorCode(errorCode = "400007")
public class SupplierInvoiceCreateException extends RuntimeException {
  public SupplierInvoiceCreateException(String message) {
    super(message);
  }

  public SupplierInvoiceCreateException() {
    super("No se pudo guardar el comprobante del proveedor.");
  }
}
