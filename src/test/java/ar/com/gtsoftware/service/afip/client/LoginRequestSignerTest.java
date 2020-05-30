package ar.com.gtsoftware.service.afip.client;

import ar.com.gtsoftware.service.ParametrosService;
import ar.com.gtsoftware.service.afip.client.login.HeaderType;
import ar.com.gtsoftware.service.afip.client.login.LoginTicketRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static ar.com.gtsoftware.enums.Parametros.AFIP_CERT_PASSWORD;
import static ar.com.gtsoftware.enums.Parametros.AFIP_CERT_PATH;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

class LoginRequestSignerTest {

    private LoginRequestSigner signer;

    @Mock
    private ParametrosService parametrosServiceMock;

    @BeforeEach
    void setUp() {
        initMocks(this);
        when(parametrosServiceMock.getStringParam(AFIP_CERT_PATH)).thenReturn(
                Thread.currentThread().getContextClassLoader().getResource("alias.p12").getPath()
        );
        when(parametrosServiceMock.getStringParam(AFIP_CERT_PASSWORD)).thenReturn("soloio");

        signer = new LoginRequestSigner(parametrosServiceMock);
    }

    @Test
    void getSignedTicketRequest() {
        LoginTicketRequest loginTicketRequest = new LoginTicketRequest();
        loginTicketRequest.setHeader(new HeaderType());

        final String signedTicketRequest = signer.getSignedTicketRequest(loginTicketRequest);

        assertThat(signedTicketRequest, is(notNullValue()));
    }
}
