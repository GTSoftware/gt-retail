package ar.com.gtsoftware.auth;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashPasswordEncoder implements PasswordEncoder {

    private final Logger logger = LogManager.getLogger(HashPasswordEncoder.class);

    @Override
    public String encode(CharSequence rawPassword) {
        try {
            MessageDigest sha = MessageDigest.getInstance("SHA-256");
            sha.update(rawPassword.toString().getBytes(StandardCharsets.UTF_8));
            byte[] digest = sha.digest();
            return Base64.encodeBase64String(digest);

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
