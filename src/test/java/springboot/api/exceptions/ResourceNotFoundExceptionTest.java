package springboot.api.exceptions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ResourceNotFoundExceptionTest {
  @Test
  void testConstructor() {
    String expectedMessage = "Resource not found";

    ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
      throw new ResourceNotFoundException(expectedMessage);
    });

    assertEquals(expectedMessage, exception.getMessage());
  }
}
