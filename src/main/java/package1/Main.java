package package1;

import com.fasterxml.jackson.databind.ObjectMapper;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.security.SecureRandom;

public class Main {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    public static final Key SECRET_KEY = getSecureRandomKey( 128);
    public static final Key SECRET_KEY2 = SECRET_KEY;
    public static final String CIPHER = "AES";

    private static Key getSecureRandomKey( int keySize) {
        byte[] secureRandomKeyBytes = new byte[keySize / 8];
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(secureRandomKeyBytes);
        return new SecretKeySpec(secureRandomKeyBytes, CIPHER);
    }
}
