package ar.com.gtsoftware.service.afip;

import ar.com.gtsoftware.service.ParametrosService;
import ar.com.gtsoftware.service.afip.client.login.HeaderType;
import ar.com.gtsoftware.service.afip.client.login.LoginTicketRequest;
import ar.com.gtsoftware.utils.BusinessDateUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import static ar.com.gtsoftware.enums.Parametros.AFIP_DN_PARAM;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

class LoginTicketRequestBuilderTest {

    private LoginTicketRequestBuilder builder;
    @Mock
    private BusinessDateUtils dateUtilsMock;
    @Mock
    private ParametrosService parametrosServiceMock;

    @BeforeEach
    void setUp() {
        initMocks(this);
        when(parametrosServiceMock.getStringParam(AFIP_DN_PARAM)).thenReturn("testDstn");
        when(dateUtilsMock.getCurrentZonedDateTime()).thenReturn(ZonedDateTime.of(2020, 1, 1, 0, 0, 0, 0, ZoneId.of("GMT-3")));

        builder = new LoginTicketRequestBuilder(dateUtilsMock, parametrosServiceMock);
    }

    @Test
    void shouldBuildLoginTicketRequest() {
        final LoginTicketRequest loginTicketRequest = builder.buildLoginTicketRequest("wsfe");

        assertThat(loginTicketRequest, is(notNullValue()));

        assertThat(loginTicketRequest.getService(), is("wsfe"));
        assertThat(loginTicketRequest.getHeader(), is(notNullValue()));

        final HeaderType header = loginTicketRequest.getHeader();

        assertThat(header.getDestination(), is("testDstn"));
        assertThat(header.getSource(), is(nullValue()));
        assertThat(header.getUniqueId(), is(greaterThan(0L)));
        assertThat(header.getExpirationTime().toString(), is("2020-01-02T00:00:00-03:00"));
        assertThat(header.getGenerationTime().toString(), is("2020-01-01T00:00:00-03:00"));
    }
}