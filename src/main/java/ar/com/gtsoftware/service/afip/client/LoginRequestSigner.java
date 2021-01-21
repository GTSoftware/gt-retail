package ar.com.gtsoftware.service.afip.client;

import ar.com.gtsoftware.enums.Parametros;
import ar.com.gtsoftware.service.ParametrosService;
import ar.com.gtsoftware.service.afip.client.login.LoginTicketRequest;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.security.*;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Collections;
import java.util.List;
import javax.xml.bind.JAXB;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.cert.jcajce.JcaCertStore;
import org.bouncycastle.cms.*;
import org.bouncycastle.cms.jcajce.JcaSignerInfoGeneratorBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.operator.jcajce.JcaDigestCalculatorProviderBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LoginRequestSigner {

  private static final String KEY_STORE_TYPE = "pkcs12";
  private static final String CERTIFICATE_ALIAS = "1";
  private static final String SECURITY_PROVIDER = "BC";
  private static final String SIGNATURE_ALGORITHM = "SHA1withRSA";

  private final Logger logger = LoggerFactory.getLogger(LoginRequestSigner.class);
  private final ParametrosService parametrosService;

  public String getSignedTicketRequest(LoginTicketRequest loginTicketRequest) {

    String p12FilePath = parametrosService.getStringParam(Parametros.AFIP_CERT_PATH);
    char[] certPassword =
        parametrosService.getStringParam(Parametros.AFIP_CERT_PASSWORD).toCharArray();

    try {
      // Create a keystore using keys from the pkcs#12 p12file
      KeyStore ks = KeyStore.getInstance(KEY_STORE_TYPE);
      FileInputStream p12stream = new FileInputStream(p12FilePath);
      ks.load(p12stream, certPassword);

      // Get Certificate & Private key from KeyStore
      final PrivateKey privateKey = (PrivateKey) ks.getKey(CERTIFICATE_ALIAS, certPassword);
      final X509Certificate x509Certificate =
          (X509Certificate) ks.getCertificate(CERTIFICATE_ALIAS);
      loginTicketRequest.getHeader().setSource(x509Certificate.getSubjectDN().toString());

      // Create a list of Certificates to include in the final CMS
      List<X509Certificate> certList = Collections.singletonList(x509Certificate);

      if (Security.getProvider(SECURITY_PROVIDER) == null) {
        Security.addProvider(new BouncyCastleProvider());
      }
      final JcaCertStore jcaCertStore = new JcaCertStore(certList);

      final byte[] encodedAsn1EncodedCms =
          getEncodedAsn1EncodedCms(
              marshalLoginTicketRequest(loginTicketRequest),
              privateKey,
              x509Certificate,
              jcaCertStore);

      return new String(Base64.encodeBase64(encodedAsn1EncodedCms));

    } catch (KeyStoreException
        | IOException
        | NoSuchAlgorithmException
        | CertificateException
        | UnrecoverableKeyException e) {
      logger.error(e.getMessage());
      throw new RuntimeException("Error durante el tratamiento del certificado", e);
    }
  }

  private String marshalLoginTicketRequest(LoginTicketRequest loginTicketRequest) {

    StringWriter stringWriter = new StringWriter();
    JAXB.marshal(loginTicketRequest, stringWriter);

    return stringWriter.toString();
  }

  private byte[] getEncodedAsn1EncodedCms(
      String xmlLoginRequest,
      PrivateKey privateKey,
      X509Certificate x509Certificate,
      JcaCertStore certStore) {
    try {
      // Create a new empty CMS Message
      CMSSignedDataGenerator gen = new CMSSignedDataGenerator();

      ContentSigner sha1Signer =
          new JcaContentSignerBuilder(SIGNATURE_ALGORITHM)
              .setProvider(SECURITY_PROVIDER)
              .build(privateKey);

      gen.addSignerInfoGenerator(
          new JcaSignerInfoGeneratorBuilder(
                  new JcaDigestCalculatorProviderBuilder().setProvider(SECURITY_PROVIDER).build())
              .build(sha1Signer, x509Certificate));
      gen.addCertificates(certStore);

      CMSTypedData data = new CMSProcessableByteArray(xmlLoginRequest.getBytes());

      // Add a Sign of the Data to the Message
      CMSSignedData signed = gen.generate(data, true);

      return signed.getEncoded();

    } catch (OperatorCreationException
        | CertificateEncodingException
        | CMSException
        | IOException e) {
      logger.error(e.getMessage());
      throw new RuntimeException("Error durante el tratamiento del certificado", e);
    }
  }
}
