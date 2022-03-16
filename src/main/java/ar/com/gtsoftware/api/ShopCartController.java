package ar.com.gtsoftware.api;

import ar.com.gtsoftware.api.request.AddCartItemRequest;
import ar.com.gtsoftware.api.request.SaleRequest;
import ar.com.gtsoftware.api.response.CartItemResponse;
import ar.com.gtsoftware.api.response.CreatedSaleResponse;
import ar.com.gtsoftware.api.response.Customer;
import ar.com.gtsoftware.dto.domain.NegocioCondicionesOperacionesDto;
import ar.com.gtsoftware.dto.domain.NegocioFormasPagoDto;
import ar.com.gtsoftware.dto.domain.NegocioPlanesPagoDto;
import ar.com.gtsoftware.dto.domain.NegocioTiposComprobanteDto;
import java.util.List;
import javax.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

public interface ShopCartController {
  @PostMapping(path = "/shop-cart/add-product")
  CartItemResponse addProduct(@RequestBody @Valid AddCartItemRequest addCartItemRequest);

  @GetMapping(path = "/shop-cart/default-customer")
  Customer getDefaultCustomer();

  @GetMapping(path = "/shop-cart/customers-search")
  List<Customer> searchCustomers(@RequestParam String query);

  @GetMapping(path = "/shop-cart/sale-types")
  List<NegocioTiposComprobanteDto> getSaleTypes();

  @GetMapping(path = "/shop-cart/sale-conditions")
  List<NegocioCondicionesOperacionesDto> getSaleConditions();

  @GetMapping(path = "/shop-cart/payment-methods")
  List<NegocioFormasPagoDto> getPaymentMethods();

  @GetMapping(path = "/shop-cart/payment-plans")
  List<NegocioPlanesPagoDto> getPaymentPlans(@RequestParam Long paymentPlanId);

  @PostMapping(path = "/shop-cart/sale")
  CreatedSaleResponse saveSale(@RequestBody SaleRequest saleRequest);
}
