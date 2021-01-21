package ar.com.gtsoftware.service.afip;

import static org.mockito.Mockito.mock;

import ar.com.gtsoftware.service.ParametrosService;
import ar.com.gtsoftware.service.afip.client.LoginRequestSigner;
import org.springframework.context.annotation.Bean;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

public class AfipClientTestConfiguration {

  private final ParametrosService parametrosServiceMock = mock(ParametrosService.class);
  private LoginTicketRequestBuilder loginTicketRequestBuilderMock =
      mock(LoginTicketRequestBuilder.class);
  private LoginRequestSigner loginRequestSignerMock = mock(LoginRequestSigner.class);
  private String loginUrl = "https://wsaahomo.afip.gov.ar/ws/services/LoginCms";
  private String wsfeUrl = "https://wswhomo.afip.gov.ar/wsfev1/service.asmx";

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
    LoginClient client = new LoginClient(loginTicketRequestBuilderMock, loginRequestSignerMock);
    client.setDefaultUri(loginUrl);
    client.setMarshaller(loginMarshaller);
    client.setUnmarshaller(loginMarshaller);

    return client;
  }

  @Bean
  public ElectronicInvoiceClient electronicInvoiceClient(Jaxb2Marshaller feMarshaller) {
    ElectronicInvoiceClient invoiceClient = new ElectronicInvoiceClient(parametrosServiceMock);
    invoiceClient.setDefaultUri(wsfeUrl);
    invoiceClient.setMarshaller(feMarshaller);
    invoiceClient.setUnmarshaller(feMarshaller);

    return invoiceClient;
  }
}
