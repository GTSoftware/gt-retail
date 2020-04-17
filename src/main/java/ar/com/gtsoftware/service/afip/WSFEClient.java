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


import ar.com.gtsoftware.domain.AFIPAuthServices;
import ar.com.gtsoftware.domain.FiscalLibroIvaVentas;
import ar.com.gtsoftware.domain.FiscalLibroIvaVentasLineas;
import ar.com.gtsoftware.dto.fiscal.CAEResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.xmlbeans.impl.soap.*;

import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import java.text.ParseException;
import java.text.SimpleDateFormat;


/**
 * Cliente para el servicio de factura electrónica
 *
 * @author Rodrigo M. Tato Rothamel mailto:rotatomel@gmail.com
 */
public class WSFEClient {

    private static final Logger LOG = LogManager.getLogger(WSFEClient.class);


    private static final String NAMESPACE = "http://ar.gov.afip.dif.FEV1/";
    private static final String FECompUltimoAutorizado_Action = "http://ar.gov.afip.dif.FEV1/FECompUltimoAutorizado";
    private static final SimpleDateFormat SDF_YMD = new SimpleDateFormat("yyyyMMdd");

    public static CAEResponse solicitarCAE(AFIPAuthServices loginTicket, String cuit, FiscalLibroIvaVentas comprobante,
                                           String endpoint) {

        try {
            SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
            SOAPConnection soapConnection = soapConnectionFactory.createConnection();

            SOAPMessage response = soapConnection.call(crearSoapReqSolicitudCAE(loginTicket, cuit, comprobante), endpoint);

            checkErrors(response);

            printMessage(response);

            String resultado = response.getSOAPBody().getElementsByTagName("Resultado").item(0).getTextContent();
            if (resultado.equals("A")) {

                String cae = response.getSOAPBody().getElementsByTagName("CAE").item(0).getTextContent();

                String fechaVencimiento = response.getSOAPBody().getElementsByTagName("CAEFchVto").item(0).getTextContent();

                CAEResponse result = new CAEResponse(Long.parseLong(cae),
                        SDF_YMD.parse(fechaVencimiento));

                return result;
            }

            throw new RuntimeException(response.getSOAPBody().getTextContent());

        } catch (SOAPException | ParseException ex) {
            LOG.error(ex);
            throw new RuntimeException("Error al realizar la solicitud", ex);
        }

    }

    private static SOAPMessage crearSoapReqSolicitudCAE(AFIPAuthServices loginTicket, String cuit,
                                                        FiscalLibroIvaVentas comprobante) throws SOAPException {
        MessageFactory messageFactory = MessageFactory.newInstance();
        SOAPMessage soapMessage = messageFactory.createMessage();
        soapMessage.getMimeHeaders().addHeader("SOAPAction", "http://ar.gov.afip.dif.FEV1/FECAESolicitar");

        SOAPPart soapPart = soapMessage.getSOAPPart();

        SOAPEnvelope envelope = soapPart.getEnvelope();

        // SOAP Body
        SOAPBody soapBody = envelope.getBody();

        //QName name = new QName(NAMESPACE, "FECAESolicitar");

        SOAPElement methodElement = soapBody.addChildElement(NAMESPACE, "FECAESolicitar");

        crearAuth(methodElement, loginTicket, cuit);

        SOAPElement elementFeCAEReq = methodElement.addChildElement("FeCAEReq");
        SOAPElement elementFeCabReq = elementFeCAEReq.addChildElement("FeCabReq");
        SOAPElement elementCantReg = elementFeCabReq.addChildElement("CantReg");
        elementCantReg.addTextNode("1");

        elementFeCabReq.addChildElement("PtoVta").addTextNode(comprobante.getPuntoVentaFactura());

        elementFeCabReq.addChildElement("CbteTipo").addTextNode(comprobante.getCodigoTipoComprobante().getCodigoTipoComprobante());

        SOAPElement elementFeDetReq = elementFeCAEReq.addChildElement("FeDetReq").addChildElement("FECAEDetRequest");
        SOAPElement elementConcepto = elementFeDetReq.addChildElement("Concepto");
        elementConcepto.addTextNode("3");//TODO ver como puede obtener esto de algún lado o cablear

        elementFeDetReq.addChildElement("DocTipo").addTextNode(comprobante.getIdPersona().getIdTipoDocumento().getFiscalCodigoTipoDocumento().toString());

        elementFeDetReq.addChildElement("DocNro").addTextNode(comprobante.getDocumento());

        elementFeDetReq.addChildElement("CbteDesde").addTextNode(comprobante.getNumeroFactura());
        elementFeDetReq.addChildElement("CbteHasta").addTextNode(comprobante.getNumeroFactura());

        elementFeDetReq.addChildElement("CbteFch").addTextNode(SDF_YMD.format(comprobante.getFechaFactura()));
        elementFeDetReq.addChildElement("ImpTotal").addTextNode(comprobante.getTotalFactura().abs().toPlainString());

        SOAPElement ivaArray = elementFeDetReq.addChildElement("Iva");

        for (FiscalLibroIvaVentasLineas lin : comprobante.getFiscalLibroIvaVentasLineasList()) {
            SOAPElement alic = ivaArray.addChildElement("AlicIva");
            alic.addChildElement("Id").addTextNode(lin.getIdAlicuotaIva().getFiscalCodigoAlicuota().toString());
            alic.addChildElement("BaseImp").addTextNode(lin.getNetoGravado().abs().toPlainString());
            alic.addChildElement("Importe").addTextNode(lin.getImporteIva().abs().toPlainString());

        }

        elementFeDetReq.addChildElement("ImpTotConc").addTextNode("0");// TODO calcular neto gravado y no gravado y exento
//TODO arreglar esto en el modelo
        elementFeDetReq.addChildElement("ImpNeto").addTextNode(comprobante.getImporteNetoGravado().abs().toPlainString());
        elementFeDetReq.addChildElement("ImpOpEx").addTextNode(comprobante.getImporteExento().abs().toPlainString());
        elementFeDetReq.addChildElement("ImpIVA").addTextNode(comprobante.getImporteIva().abs().toPlainString());
        elementFeDetReq.addChildElement("ImpTrib").addTextNode(comprobante.getImporteTributos().abs().toPlainString());

        elementFeDetReq.addChildElement("FchServDesde").addTextNode(SDF_YMD.format(comprobante.getFechaFactura()));
        elementFeDetReq.addChildElement("FchServHasta").addTextNode(SDF_YMD.format(comprobante.getFechaFactura()));
        elementFeDetReq.addChildElement("FchVtoPago").addTextNode(SDF_YMD.format(comprobante.getFechaFactura()));

        elementFeDetReq.addChildElement("MonId").addTextNode("PES");
        elementFeDetReq.addChildElement("MonCotiz").addTextNode("1");

        soapMessage.saveChanges();

        /* Print the request message */
//        System.out.print("Request SOAP Message = ");
//        try {
//            soapMessage.writeTo(System.out);
//        } catch (IOException ex) {
//            Logger.getLogger(WSAAClient.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        System.out.println();
        return soapMessage;
    }

    private static void printMessage(SOAPMessage response) {
        try {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            Source sourceContent = response.getSOAPPart().getContent();

            System.out.print("\nResponse SOAP Message = ");

            StreamResult result = new StreamResult(System.out);
            transformer.transform(sourceContent, result);

        } catch (TransformerException | SOAPException ex) {
            LOG.error(ex);
        }
    }

    //    private static String getXMLString(SOAPMessage response) {
//        try {
//            TransformerFactory transformerFactory = TransformerFactory.newInstance();
//            Transformer transformer = transformerFactory.newTransformer();
//            Source sourceContent = response.getSOAPPart().getContent();
//
//            StringWriter sw = new StringWriter();
//            StreamResult result = new StreamResult(sw);
//            transformer.transform(sourceContent, result);
//            return sw.getBuffer().toString();
//        } catch (TransformerConfigurationException ex) {
//            Logger.getLogger(WSFEClient.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (TransformerException | SOAPException ex) {
//            Logger.getLogger(WSFEClient.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return null;
//    }
    private static void crearAuth(SOAPElement methodElement, AFIPAuthServices loginTicket, String cuit)
            throws SOAPException {

        SOAPElement authElement = methodElement.addChildElement("Auth");

        authElement.addChildElement("Token").addTextNode(loginTicket.getToken());
        authElement.addChildElement("Sign").addTextNode(loginTicket.getSign());
        authElement.addChildElement("Cuit").addTextNode(cuit);
    }

    public static int obtenerUltimoComprobanteAutorizado(AFIPAuthServices loginTicket, String cuit, String endpoint, int puntoVenta, int tipoComprobante) {

        try {
            SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
            SOAPConnection soapConnection = soapConnectionFactory.createConnection();

            MessageFactory messageFactory = MessageFactory.newInstance();
            SOAPMessage soapMessage = messageFactory.createMessage();

            soapMessage.getMimeHeaders().addHeader("SOAPAction", FECompUltimoAutorizado_Action);
            SOAPPart soapPart = soapMessage.getSOAPPart();

            SOAPEnvelope envelope = soapPart.getEnvelope();

            SOAPBody soapBody = envelope.getBody();
            //QName name = new QName(NAMESPACE, "FECompUltimoAutorizado");

            SOAPElement methodElement = soapBody.addChildElement(NAMESPACE, "FECompUltimoAutorizado");

            crearAuth(methodElement, loginTicket, cuit);

            methodElement.addChildElement("PtoVta").addTextNode(String.valueOf(puntoVenta));
            methodElement.addChildElement("CbteTipo").addTextNode(String.valueOf(tipoComprobante));

            soapMessage.saveChanges();

            /* Print the request message */
//            System.out.print("Request SOAP Message = ");
//            try {
//                soapMessage.writeTo(System.out);
//            } catch (IOException ex) {
//                Logger.getLogger(WSAAClient.class.getName()).log(Level.SEVERE, null, ex);
//            }
//            System.out.println();
            SOAPMessage response = soapConnection.call(soapMessage, endpoint);

            checkErrors(response);

            String utlComp = response.getSOAPBody().getElementsByTagName("CbteNro").item(0).getTextContent();

            return Integer.parseInt(utlComp);
        } catch (SOAPException ex) {
            LOG.error(ex);
            throw new RuntimeException("Error al realizar la solicitud", ex);

        }
    }

    private static void checkErrors(SOAPMessage response) throws SOAPException {
        if (response.getSOAPBody().getElementsByTagName("Errors").getLength() > 0) {
            throw new RuntimeException(response.getSOAPBody().getTextContent());
        }
    }

}
