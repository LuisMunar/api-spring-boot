package springboot.api.util;

import java.util.Base64;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class SecretKeyUtil {
  public static String encode(SecretKey secretKey) {
    return Base64.getEncoder().encodeToString(secretKey.getEncoded());
  }

  public static SecretKey decode(String encodeSecretKey) {
    byte[] decodeBase64 = Base64.getDecoder().decode(encodeSecretKey);
    return new SecretKeySpec(decodeBase64, 0, decodeBase64.length, "HmacSHA256");
  }
}
