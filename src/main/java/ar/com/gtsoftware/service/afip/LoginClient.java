package ar.com.gtsoftware.service.afip;

import ar.com.gtsoftware.dto.fiscal.AuthTicket;
import ar.com.gtsoftware.service.afip.client.LoginRequestSigner;
import ar.com.gtsoftware.service.afip.client.login.*;
import java.io.StringReader;
import java.time.LocalDateTime;
import javax.xml.bind.JAXB;
import javax.xml.datatype.XMLGregorianCalendar;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;

@RequiredArgsConstructor
public class LoginClient extends WebServiceGatewaySupport {

  private final Logger logger = LoggerFactory.getLogger(LoginClient.class);
  private final LoginTicketRequestBuilder loginTicketRequestBuilder;
  private final LoginRequestSigner loginRequestSigner;

  public AuthTicket login(String service) {

    final LoginTicketRequest loginTicketRequest =
        loginTicketRequestBuilder.buildLoginTicketRequest(service);

    logger.debug("Signing Login Request...");
    final String signedTicketRequest =
        loginRequestSigner.getSignedTicketRequest(loginTicketRequest);
    logger.debug("Login Request signed!");

    final LoginCms loginCms = new LoginCms();
    loginCms.setIn0(signedTicketRequest);

    final Object response = getWebServiceTemplate().marshalSendAndReceive(loginCms);

    LoginCmsResponse loginCmsResponse = (LoginCmsResponse) response;

    final LoginTicketResponse ticketResponse =
        JAXB.unmarshal(
            new StringReader(loginCmsResponse.getLoginCmsReturn()), LoginTicketResponse.class);
    final CredentialsType credentials = ticketResponse.getCredentials();

    return AuthTicket.builder()
        .sign(credentials.getSign())
        .token(credentials.getToken())
        .expirationDate(transformExpirationDate(ticketResponse.getHeader().getExpirationTime()))
        .build();
  }

  private LocalDateTime transformExpirationDate(XMLGregorianCalendar expirationTime) {
    return LocalDateTime.of(
        expirationTime.getYear(),
        expirationTime.getMonth(),
        expirationTime.getDay(),
        expirationTime.getHour(),
        expirationTime.getMinute(),
        expirationTime.getSecond());
  }

  protected LoginTicketRequestBuilder getLoginTicketRequestBuilder() {
    return loginTicketRequestBuilder;
  }

  protected LoginRequestSigner getLoginRequestSigner() {
    return loginRequestSigner;
  }
}
