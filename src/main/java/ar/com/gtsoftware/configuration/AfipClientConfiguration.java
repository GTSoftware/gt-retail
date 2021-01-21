package ar.com.gtsoftware.configuration;

import ar.com.gtsoftware.service.ParametrosService;
import ar.com.gtsoftware.service.afip.ElectronicInvoiceClient;
import ar.com.gtsoftware.service.afip.LoginClient;
import ar.com.gtsoftware.service.afip.LoginTicketRequestBuilder;
import ar.com.gtsoftware.service.afip.client.LoginRequestSigner;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

@Configuration
@RequiredArgsConstructor
public class AfipClientConfiguration {

  private final LoginTicketRequestBuilder loginTicketRequestBuilder;
  private final LoginRequestSigner loginRequestSigner;
  private final ParametrosService parametrosService;

  @Value("${gtretail.afip.login.url:https://wsaahomo.afip.gov.ar/ws/services/LoginCms}")
  private String loginUrl;

  @Value("${gtretail.afip.wsfe.url:https://wswhomo.afip.gov.ar/wsfev1/service.asmx}")
  private String wsfeUrl;

  @Bean
  public Jaxb2Marshaller loginMarshaller() {
    Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
    marshaller.setContextPath("ar.com.gtsoftware.service.afip.client.login");

    return marshaller;
  }

  @Bean
  public Jaxb2Marshaller feMarshaller() {
    Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
    marshaller.setContextPath("ar.com.gtsoftware.service.afip.client.fe");

    return marshaller;
  }

  @Bean
  public LoginClient loginClient(Jaxb2Marshaller loginMarshaller) {
    LoginClient loginClient = new LoginClient(loginTicketRequestBuilder, loginRequestSigner);
    loginClient.setDefaultUri(loginUrl);
    loginClient.setMarshaller(loginMarshaller);
    loginClient.setUnmarshaller(loginMarshaller);

    return loginClient;
  }

  @Bean
  public ElectronicInvoiceClient electronicInvoiceClient(Jaxb2Marshaller feMarshaller) {
    ElectronicInvoiceClient invoiceClient = new ElectronicInvoiceClient(parametrosService);
    invoiceClient.setDefaultUri(wsfeUrl);
    invoiceClient.setMarshaller(feMarshaller);
    invoiceClient.setUnmarshaller(feMarshaller);

    return invoiceClient;
  }
}
