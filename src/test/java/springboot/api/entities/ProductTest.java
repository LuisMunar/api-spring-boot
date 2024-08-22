package springboot.api.entities;

import org.junit.jupiter.api.Test;

public class ProductTest {
  @Test
  void testSetId() {
    Long id = 1L;
    Product product = new Product();
    product.setId(id);
    assert product.getId() == id;
  }

  @Test
  void testSetName() {
    String name = "Product Name";
    Product product = new Product();
    product.setName(name);
    assert product.getName().equals(name);
  }

  @Test
  void testSetDescription() {
    String description = "Product Description";
    Product product = new Product();
    product.setDescription(description);
    assert product.getDescription().equals(description);
  }

  @Test
  void testSetPrice() {
    Integer price = 100;
    Product product = new Product();
    product.setPrice(price);
    assert product.getPrice() == price;
  }
}
