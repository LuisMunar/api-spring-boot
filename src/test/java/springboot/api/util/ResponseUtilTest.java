package springboot.api.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@SpringBootTest
class ResponseUtilTest {

  @Test
  void testConstructor() {
    ResponseUtil responseUtil = new ResponseUtil();
    assertNotNull(responseUtil);
  }

  @Test
  void testResponseMethod() {
    int status = HttpStatus.OK.value();
    String message = "Success";
    String data = "This is the data";

    ResponseEntity<Map<String, Object>> response = ResponseUtil.response(status, message, data);

    assertEquals(status, response.getStatusCode().value());
    assertTrue(response.getBody().containsKey("data"));
    assertEquals(data, response.getBody().get("data"));
    assertEquals(message, response.getBody().get("message"));
  }

  @Test
  void testErrorMethod() {
    int status = HttpStatus.BAD_REQUEST.value();
    String message = "Error occurred";

    ResponseEntity<Map<String, Object>> response = ResponseUtil.error(status, message);

    assertEquals(status, response.getStatusCode().value());
    assertTrue(response.getBody().containsKey("message"));
    assertEquals(message, response.getBody().get("message"));
  }

  @Test
  void testResponseMethodWithNullData() {
    int status = HttpStatus.OK.value();
    String message = "Success";
    Object data = null;

    ResponseEntity<Map<String, Object>> response = ResponseUtil.response(status, message, data);

    assertEquals(status, response.getStatusCode().value());
    assertNotNull(response.getBody());
    assertTrue(response.getBody().containsKey("data"));
    assertEquals(data, response.getBody().get("data"));
    assertEquals(message, response.getBody().get("message"));
  }
}
