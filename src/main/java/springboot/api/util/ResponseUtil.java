package springboot.api.util;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;

public class ResponseUtil {
  public static <T> ResponseEntity<Map<String, Object>> response(int status, String message, T data) {
    Map<String, Object> body = new HashMap<>();
    body.put("data", data);
    body.put("message", message);
    return ResponseEntity.status(status).body(body);
  }

  public static ResponseEntity<Map<String, Object>> error(int status, String message) {
    return ResponseEntity.status(status).body(Map.of("message", message));
  }
}
