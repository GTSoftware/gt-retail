//
// Este archivo ha sido generado por la arquitectura JavaTM para la implantación de la referencia de enlace (JAXB) XML v2.2.8-b130911.1802 
// Visite <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Todas las modificaciones realizadas en este archivo se perderán si se vuelve a compilar el esquema de origen. 
// Generado el: 2020.05.28 a las 09:17:46 PM ART 
//


package ar.com.gtsoftware.service.afip.client.login;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each
 * Java content interface and Java element interface
 * generated in the ar.com.gtsoftware.service.afip.client.login package.
 * <p>An ObjectFactory allows you to programatically
 * construct new instances of the Java representation
 * for XML content. The Java representation of XML
 * content can consist of schema derived interfaces
 * and classes representing the binding of schema
 * type definitions, element declarations and model
 * groups.  Factory methods for each of these are
 * provided in this class.
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _LoginTicketRequest_QNAME = new QName("", "loginTicketRequest");
    private final static QName _LoginTicketResponse_QNAME = new QName("", "loginTicketResponse");
    private final static QName _Fault_QNAME = new QName("https://wsaa.afip.gov.ar/ws/services/LoginCms", "fault");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: ar.com.gtsoftware.service.afip.client.login
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link LoginTicketRequest }
     */
    public LoginTicketRequest createLoginTicketRequest() {
        return new LoginTicketRequest();
    }

    /**
     * Create an instance of {@link LoginTicketResponse }
     */
    public LoginTicketResponse createLoginTicketResponse() {
        return new LoginTicketResponse();
    }

    /**
     * Create an instance of {@link HeaderType }
     */
    public HeaderType createHeaderType() {
        return new HeaderType();
    }

    /**
     * Create an instance of {@link CredentialsType }
     */
    public CredentialsType createCredentialsType() {
        return new CredentialsType();
    }

    /**
     * Create an instance of {@link LoginCms }
     */
    public LoginCms createLoginCms() {
        return new LoginCms();
    }

    /**
     * Create an instance of {@link LoginCmsResponse }
     */
    public LoginCmsResponse createLoginCmsResponse() {
        return new LoginCmsResponse();
    }

    /**
     * Create an instance of {@link LoginFault }
     */
    public LoginFault createLoginFault() {
        return new LoginFault();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link LoginTicketRequest }{@code >}}
     */
    @XmlElementDecl(namespace = "", name = "loginTicketRequest")
    public JAXBElement<LoginTicketRequest> createLoginTicketRequest(LoginTicketRequest value) {
        return new JAXBElement<LoginTicketRequest>(_LoginTicketRequest_QNAME, LoginTicketRequest.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link LoginTicketResponse }{@code >}}
     */
    @XmlElementDecl(namespace = "", name = "loginTicketResponse")
    public JAXBElement<LoginTicketResponse> createLoginTicketResponse(LoginTicketResponse value) {
        return new JAXBElement<LoginTicketResponse>(_LoginTicketResponse_QNAME, LoginTicketResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link LoginFault }{@code >}}
     */
    @XmlElementDecl(namespace = "https://wsaa.afip.gov.ar/ws/services/LoginCms", name = "fault")
    public JAXBElement<LoginFault> createFault(LoginFault value) {
        return new JAXBElement<LoginFault>(_Fault_QNAME, LoginFault.class, null, value);
    }

}
