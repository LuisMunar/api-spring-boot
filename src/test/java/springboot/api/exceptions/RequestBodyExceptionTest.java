package springboot.api.exceptions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class RequestBodyExceptionTest {
  @Test
  void testConstructor() {
    String expectedMessage = "Invalid request body";

    RequestBodyException exception = assertThrows(RequestBodyException.class, () -> {
      throw new RequestBodyException(expectedMessage);
    });

    assertEquals(expectedMessage, exception.getMessage());
  }
}
