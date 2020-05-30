package ar.com.gtsoftware.service.afip;

import ar.com.gtsoftware.service.ParametrosService;
import ar.com.gtsoftware.service.afip.client.login.HeaderType;
import ar.com.gtsoftware.service.afip.client.login.LoginTicketRequest;
import ar.com.gtsoftware.utils.BusinessDateUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import static ar.com.gtsoftware.enums.Parametros.AFIP_DN_PARAM;

@RequiredArgsConstructor
@Component
public class LoginTicketRequestBuilder {

    private static final int TICKET_DURATION_HS = 24;
    private static final DateTimeFormatter dtf = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

    private final Logger logger = LoggerFactory.getLogger(LoginTicketRequestBuilder.class);
    private final BusinessDateUtils dateUtils;
    private final ParametrosService parametrosService;


    public LoginTicketRequest buildLoginTicketRequest(String service) {

        HeaderType headerType = new HeaderType();

        headerType.setDestination(parametrosService.getStringParam(AFIP_DN_PARAM));
        headerType.setUniqueId(RandomUtils.nextInt());

        ZonedDateTime genTime = dateUtils.getCurrentZonedDateTime();
        ZonedDateTime expTime = genTime.plusHours(TICKET_DURATION_HS);

        try {
            XMLGregorianCalendar xmlGenTime = DatatypeFactory.newInstance().newXMLGregorianCalendar(dtf.format(genTime));
            XMLGregorianCalendar xmlExpTime = DatatypeFactory.newInstance().newXMLGregorianCalendar(dtf.format(expTime));

            headerType.setGenerationTime(xmlGenTime);
            headerType.setExpirationTime(xmlExpTime);
        } catch (DatatypeConfigurationException e) {
            logger.error(e.getMessage());
            throw new RuntimeException("Error while setting generation and expiration dates");
        }

        LoginTicketRequest loginTicketRequest = new LoginTicketRequest();

        loginTicketRequest.setHeader(headerType);
        loginTicketRequest.setService(service);

        return loginTicketRequest;
    }
}
