package ar.com.gtsoftware.auth;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import org.apache.commons.codec.digest.MessageDigestAlgorithms;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.crypto.password.PasswordEncoder;

public class HashPasswordEncoder implements PasswordEncoder {

  private final Logger logger = LogManager.getLogger(HashPasswordEncoder.class);

  @Override
  public String encode(CharSequence rawPassword) {
    try {

      MessageDigest sha = MessageDigest.getInstance(MessageDigestAlgorithms.SHA_256);
      sha.update(rawPassword.toString().getBytes(StandardCharsets.UTF_8));
      byte[] digest = sha.digest();
      java.util.Base64.Encoder encoder = Base64.getEncoder();
      return encoder.encodeToString(digest);

    } catch (NoSuchAlgorithmException ex) {
      logger.error("Algorithm not found", ex);
    }
    return null;
  }

  @Override
  public boolean matches(CharSequence rawPassword, String encodedPassword) {
    return encode(rawPassword).equalsIgnoreCase(encodedPassword);
  }
}
