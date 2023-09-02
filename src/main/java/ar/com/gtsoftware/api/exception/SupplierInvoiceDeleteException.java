package ar.com.gtsoftware.api.exception;

import ar.com.gtsoftware.exception.ExceptionErrorCode;

@ExceptionErrorCode(errorCode = "400006")
public class SupplierInvoiceDeleteException extends RuntimeException {
  public SupplierInvoiceDeleteException(String message) {
    super(message);
  }

  public SupplierInvoiceDeleteException() {
    super("El comprobante no pudo ser eliminado.");
  }
}
