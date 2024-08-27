package springboot.api.constants;

import javax.crypto.SecretKey;

import io.jsonwebtoken.Jwts;

public class TokenConstants {
  public static final SecretKey SECRET_KEY = Jwts.SIG.HS256.key().build();
  public static final String CONTENT_TYPE = "application/json";
  public static final String HEADER_AUTHORIZATION = "Authorization";
  public static final String PREFIX_TOKEN = "Bearer ";
  public static final long EXPIRATION_TIME = 3600000; // 1 hour
}
