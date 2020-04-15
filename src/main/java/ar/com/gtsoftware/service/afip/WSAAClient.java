/*
 * Copyright 2016 GT Software.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ar.com.gtsoftware.service.afip;

import ar.com.gtsoftware.dto.fiscal.AuthTicket;
import org.apache.commons.codec.binary.Base64;
import org.apache.xmlbeans.impl.soap.*;
import org.bouncycastle.cert.jcajce.JcaCertStore;
import org.bouncycastle.cms.*;
import org.bouncycastle.cms.jcajce.JcaSignerInfoGeneratorBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.operator.jcajce.JcaDigestCalculatorProviderBuilder;
import org.bouncycastle.util.Store;
import org.xml.sax.InputSource;

import javax.xml.bind.DatatypeConverter;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.security.*;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Clase para obtener acceso al servicio de autorización y autenticación de AFIP
 *
 * @author Rodrigo M. Tato Rothamel mailto:rotatomel@gmail.com
 */
public class WSAAClient {

    private static final Logger LOG = Logger.getLogger(WSAAClient.class.getName());

    // private static final String URL_PROD =
    // "https://wsaa.afip.gov.ar/ws/services/LoginCms";
    // private static final String URL_TESTING =
    // "https://wsaahomo.afip.gov.ar/ws/services/LoginCms";
    private static final long TICKET_TIME = 3600000L;

    public static AuthTicket performAuthentication(String endpoint, String certPath, String password, String dstDN,
                                                   String service) {

        try {
            // Create SOAP Connection
            SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
            SOAPConnection soapConnection = soapConnectionFactory.createConnection();

            // Send SOAP Message to SOAP Server
            SOAPMessage soapResponse = soapConnection.call(createSOAPRequest(certPath, password, dstDN, service),
                    endpoint);
            if (soapResponse.getSOAPBody().hasFault()) {
                throw new RuntimeException(soapResponse.getSOAPBody().getFault().getTextContent());
            }
            AuthTicket ticket = parseSOAPResponse(soapResponse);
            soapConnection.close();
            return ticket;

        } catch (SOAPException | XPathExpressionException ex) {
            LOG.log(Level.SEVERE, null, ex);
            throw new RuntimeException("Error al realizar la autorización", ex);

        }

    }

    // public static void main(String args[]) {
    // try {
    //
    // // Create SOAP Connection
    // SOAPConnectionFactory soapConnectionFactory =
    // SOAPConnectionFactory.newInstance();
    // SOAPConnection soapConnection = soapConnectionFactory.createConnection();
    //
    // // Send SOAP Message to SOAP Server
    // SOAPMessage soapResponse = soapConnection.call(createSOAPRequest(),
    // URL_TESTING);
    //
    // // Process the SOAP Response
    // printSOAPResponse(soapResponse);
    //
    // soapConnection.close();
    // } catch (Exception e) {
    // System.err.println("Error occurred while sending SOAP Request to
    // Server");
    //
    // }
    // }
    private static SOAPMessage createSOAPRequest(String certPath, String password, String dstDN, String service)
            throws SOAPException {
        MessageFactory messageFactory = MessageFactory.newInstance();
        SOAPMessage soapMessage = messageFactory.createMessage();
        soapMessage.getMimeHeaders().addHeader("SOAPAction", "\"\"");

        SOAPPart soapPart = soapMessage.getSOAPPart();

        SOAPEnvelope envelope = soapPart.getEnvelope();

        // SOAP Body
        SOAPBody soapBody = envelope.getBody();

        SOAPElement loginElement = soapBody.addChildElement("loginCms");
        SOAPElement request = loginElement.addChildElement("in0");

        request.addTextNode(new String(Base64.encodeBase64(create_cms(certPath, password, dstDN, service))));

        soapMessage.saveChanges();

        // /* Print the request message */
        // System.out.print("Request SOAP Message = ");
        // try {
        // soapMessage.writeTo(System.out);
        // } catch (IOException ex) {
        // Logger.getLogger(WSAAClient.class.getName()).log(Level.SEVERE, null,
        // ex);
        // }
        // System.out.println();
        return soapMessage;
    }

    /**
     * Method used to print the SOAP Response
     */
    private static AuthTicket parseSOAPResponse(SOAPMessage soapResponse)
            throws SOAPException, XPathExpressionException {
        // TransformerFactory transformerFactory =
        // TransformerFactory.newInstance();
        // Transformer transformer = transformerFactory.newTransformer();
        // Source sourceContent = soapResponse.getSOAPPart().getContent();
        //
        // System.out.print("\nResponse SOAP Message = ");
        //
        // StreamResult result = new StreamResult(System.out);
        // transformer.transform(sourceContent, result);

        SOAPBody body = soapResponse.getSOAPBody();

        XPathFactory factory = XPathFactory.newInstance();
        XPath xpath = factory.newXPath();

        String bodyXMLString = body.getTextContent();
        String token = xpath.evaluate("//token/text()", new InputSource(new StringReader(bodyXMLString)));
        String sign = xpath.evaluate("//sign/text()", new InputSource(new StringReader(bodyXMLString)));
        String expTime = xpath.evaluate("//expirationTime/text()", new InputSource(new StringReader(bodyXMLString)));

        // System.out.printf("token=%s\n", token);
        // System.out.printf("sign=%s\n", sign);
        Calendar parseDateTime = DatatypeConverter.parseDateTime(expTime);

        // System.out.printf("expDate=%s\n", parseDateTime.getTime());
        AuthTicket ticket = new AuthTicket(token, sign, parseDateTime.getTime());
        return ticket;
    }

    //
    // Create the CMS Message
    //
    private static byte[] create_cms(String p12file, String p12pass, String dstDN, String service) {

        PrivateKey pKey = null;
        X509Certificate pCertificate = null;
        byte[] asn1_cms = null;
        Store certs = null;
        String loginTicketRequest_xml;
        String signerDN = null;

        //
        // Manage Keys & Certificates
        //
        try {
            // Create a keystore using keys from the pkcs#12 p12file
            KeyStore ks = KeyStore.getInstance("pkcs12");
            FileInputStream p12stream = new FileInputStream(p12file);
            ks.load(p12stream, p12pass.toCharArray());

            // Get Certificate & Private key from KeyStore
            pKey = (PrivateKey) ks.getKey("1", p12pass.toCharArray());
            pCertificate = (X509Certificate) ks.getCertificate("1");
            signerDN = pCertificate.getSubjectDN().toString();

            // Create a list of Certificates to include in the final CMS
            ArrayList<X509Certificate> certList = new ArrayList<>();
            certList.add(pCertificate);

            if (Security.getProvider("BC") == null) {
                Security.addProvider(new BouncyCastleProvider());
            }
            certs = new JcaCertStore(certList);

        } catch (KeyStoreException | IOException | NoSuchAlgorithmException | CertificateException
                | UnrecoverableKeyException e) {
            LOG.log(Level.SEVERE, null, e);
            throw new RuntimeException("Error durante el tratamiento del certificado", e);
        }

        try {
            //
            // Create XML Message
            //
            loginTicketRequest_xml = create_LoginTicketRequest(signerDN, dstDN, service);
        } catch (DatatypeConfigurationException ex) {
            LOG.log(Level.SEVERE, null, ex);
            throw new RuntimeException("Error durante el armado del XML", ex);
        }

        //
        // Create CMS Message
        //
        try {
            // Create a new empty CMS Message
            CMSSignedDataGenerator gen = new CMSSignedDataGenerator();

            ContentSigner sha1Signer = new JcaContentSignerBuilder("SHA1withRSA").setProvider("BC").build(pKey);
            gen.addSignerInfoGenerator(new JcaSignerInfoGeneratorBuilder(
                    new JcaDigestCalculatorProviderBuilder().setProvider("BC").build()).build(sha1Signer,
                    pCertificate));
            gen.addCertificates(certs);

            CMSTypedData data = new CMSProcessableByteArray(loginTicketRequest_xml.getBytes());

            // Add a Sign of the Data to the Message
            CMSSignedData signed = gen.generate(data, true);
            // CMSSignedData signed = gen.generate(data, true, "BC");

            //
            asn1_cms = signed.getEncoded();
        } catch (OperatorCreationException | CertificateEncodingException | CMSException | IOException e) {
            LOG.log(Level.SEVERE, null, e);
            throw new RuntimeException("Error durante el tratamiento del certificado", e);
        }

        return asn1_cms;
    }

    //
    // Create XML Message for AFIP wsaa
    //
    private static String create_LoginTicketRequest(String SignerDN, String dstDN, String service)
            throws DatatypeConfigurationException {

        String loginTicketRequestXML;

        Date genTime = new Date();
        GregorianCalendar gentime = new GregorianCalendar();
        GregorianCalendar exptime = new GregorianCalendar();
        String uniqueId = Long.toString(genTime.getTime() / 1000);

        exptime.setTime(new Date(genTime.getTime() + TICKET_TIME));
        XMLGregorianCalendar xmlGenTime = DatatypeFactory.newInstance().newXMLGregorianCalendar(gentime);
        XMLGregorianCalendar xmlExpTime = DatatypeFactory.newInstance().newXMLGregorianCalendar(exptime);

        loginTicketRequestXML = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
                + "<loginTicketRequest version=\"1.0\">" + "<header>" + "<source>" + SignerDN + "</source>"
                + "<destination>" + dstDN + "</destination>" + "<uniqueId>" + uniqueId + "</uniqueId>"
                + "<generationTime>" + xmlGenTime + "</generationTime>" + "<expirationTime>" + xmlExpTime
                + "</expirationTime>" + "</header>" + "<service>" + service + "</service>" + "</loginTicketRequest>";

        return (loginTicketRequestXML);
    }
}
