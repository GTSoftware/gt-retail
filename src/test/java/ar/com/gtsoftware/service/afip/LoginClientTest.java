package ar.com.gtsoftware.service.afip;

import ar.com.gtsoftware.dto.fiscal.AuthTicket;
import ar.com.gtsoftware.service.afip.client.login.HeaderType;
import ar.com.gtsoftware.service.afip.client.login.LoginTicketRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.ws.test.client.MockWebServiceServer;
import org.springframework.xml.transform.StringSource;

import javax.xml.transform.Source;
import java.time.LocalDateTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.ws.test.client.RequestMatchers.payload;
import static org.springframework.ws.test.client.ResponseCreators.withPayload;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = AfipClientTestConfiguration.class)
class LoginClientTest {

    private static final LocalDateTime EXPIRATION_DATE = LocalDateTime.of(
            2019, 9, 27, 1, 56, 14
    );

    @Autowired
    private LoginClient client;

    private MockWebServiceServer mockServer;

    @BeforeEach
    void setUp() {
        mockServer = MockWebServiceServer.createServer(client);
    }

    @Test
    void shouldLogin() {

        final LoginTicketRequest loginTicketRequest = new LoginTicketRequest();
        loginTicketRequest.setHeader(new HeaderType());
        loginTicketRequest.setService("mock");

        when(client.getLoginTicketRequestBuilder().buildLoginTicketRequest("test")).thenReturn(loginTicketRequest);
        when(client.getLoginRequestSigner().getSignedTicketRequest(loginTicketRequest)).thenReturn("signedRequest");

        Source requestPayload = new StringSource(
                "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
                        "<loginCms xmlns=\"http://wsaa.view.sua.dvadac.desein.afip.gov\">\n" +
                        "    <in0>signedRequest</in0>\n" +
                        "</loginCms>");

        Source responsePayload = new StringSource(
                "<loginCmsResponse xmlns=\"http://wsaa.view.sua.dvadac.desein.afip.gov\">\n" +
                        "    <loginCmsReturn><![CDATA[<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
                        "<loginTicketResponse version=\"1.0\">\n" +
                        "    <header>\n" +
                        "        <source>CN=wsaahomo, O=AFIP, C=AR,SERIALNUMBER=CUIT 33693450239</source>\n" +
                        "        <destination>SERIALNUMBER=CUIT 20190178154,CN=glarriera20190903</destination>\n" +
                        "        <uniqueId>3866895167</uniqueId>\n" +
                        "        <generationTime>2019-09-26T13:56:14.467-03:00</generationTime>\n" +
                        "        <expirationTime>2019-09-27T01:56:14.467-03:00</expirationTime>\n" +
                        "    </header>\n" +
                        "    <credentials>\n" +
                        "        <token>PD94bWwgdmVyc2lvgo8L3Nzbz4K</token>\n" +
                        "        <sign>Urp5dbarIb8m5ySEzSeon1W7ys=</sign>\n" +
                        "    </credentials>\n" +
                        "</loginTicketResponse>]]>\n" +
                        "    </loginCmsReturn>\n" +
                        "</loginCmsResponse>");

        mockServer.expect(payload(requestPayload))
                .andRespond(withPayload(responsePayload));

        final AuthTicket authTicket = client.login("test");

        assertThat(authTicket.getExpirationDate(), is(EXPIRATION_DATE));
        assertThat(authTicket.getSign(), is("Urp5dbarIb8m5ySEzSeon1W7ys="));
        assertThat(authTicket.getToken(), is("PD94bWwgdmVyc2lvgo8L3Nzbz4K"));

    }
}