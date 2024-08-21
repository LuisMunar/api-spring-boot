package springboot.api.exceptions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ResourceAlreadyExistsExceptionTest {
  @Test
  void testConstructor() {
    String expectedMessage = "Resource already exists";

    ResourceAlreadyExistsException exception = assertThrows(ResourceAlreadyExistsException.class, () -> {
      throw new ResourceAlreadyExistsException(expectedMessage);
    });

    assertEquals(expectedMessage, exception.getMessage());
  }
}
