package springboot.api.controllers.dtos;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import springboot.api.entities.Product;

public class ProductBodyParamsDtoTest {
  @Test
  void testConstructorAndGetters() {
    String name = "Test Product";
    String description = "Test Description";
    Integer price = 100;

    ProductBodyParamsDto dto = new ProductBodyParamsDto(name, description, price);

    assertEquals(name, dto.getName());
    assertEquals(description, dto.getDescription());
    assertEquals(price, dto.getPrice());
  }

  @Test
  void testSetters() {
    String name = "Test Product";
    String description = "Test Description";
    Integer price = 100;

    ProductBodyParamsDto dto = new ProductBodyParamsDto();

    dto.setName(name);
    dto.setDescription(description);
    dto.setPrice(price);

    assertEquals(name, dto.getName());
    assertEquals(description, dto.getDescription());
    assertEquals(price, dto.getPrice());
  }

  @Test
  void testToEntity() {
    ProductBodyParamsDto dto = new ProductBodyParamsDto("Test Product", "Test Description", 100);

    Product product = dto.toEntity();

    assertNotNull(product);
    assertEquals("Test Product", product.getName());
    assertEquals("Test Description", product.getDescription());
    assertEquals(100, product.getPrice());
    assertEquals(null, product.getId());
  }
}
