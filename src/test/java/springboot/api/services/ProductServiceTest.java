package springboot.api.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import springboot.api.entities.Product;
import springboot.api.repositories.ProductRepository;

public class ProductServiceTest {
  @Mock
  private ProductRepository productRepository;

  @InjectMocks
  private ProductService productService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testFindAll() {
    Product product1 = new Product(1L, "Product 1", "Description 1", 100);
    Product product2 = new Product(2L, "Product 2", "Description 2", 200);
    List<Product> productList = Arrays.asList(product1, product2);
    when(productRepository.findAll()).thenReturn(productList);

    List<Product> result = productService.findAll();

    assertNotNull(result);
    assertEquals(2, result.size());
    assertEquals("Product 1", result.get(0).getName());
    assertEquals("Product 2", result.get(1).getName());
  }

  @Test
  void testFindAllThrowsException() {
    when(productRepository.findAll()).thenThrow(new RuntimeException("Database connection error"));

    assertThrows(RuntimeException.class, () -> {
      productService.findAll();
    });
  }

  @Test
  void testFindById() {
    Long id = 1L;
    Product product = new Product(id, "Product 1", "Description 1", 100);
    when(productRepository.findById(id)).thenReturn(java.util.Optional.of(product));

    Product result = productService.findById(id);

    assertNotNull(result);
    assertEquals("Product 1", result.getName());
  }

  @Test
  void testFindByIdThrowsException() {
    Long id = 1L;
    when(productRepository.findById(id)).thenReturn(java.util.Optional.empty());

    assertThrows(RuntimeException.class, () -> {
      productService.findById(id);
    });
  }

  @Test
  void testSave() {
    Product product = new Product(1L, "Product 1", "Description 1", 100);
    when(productRepository.findByName(product.getName())).thenReturn(java.util.Optional.empty());
    when(productRepository.save(product)).thenReturn(product);

    Product result = productService.save(product);

    assertNotNull(result);
    assertEquals("Product 1", result.getName());
  }

  @Test
  void testSaveThrowsException() {
    Product product = new Product(1L, "Product 1", "Description 1", 100);
    when(productRepository.findByName(product.getName())).thenReturn(java.util.Optional.of(product));

    assertThrows(RuntimeException.class, () -> {
      productService.save(product);
    });
  }

  @Test
  void testUpdate() {
    Long id = 1L;
    Product product = new Product(id, "Product 1", "Description 1", 100);
    Product existingProduct = new Product(id, "Product 1", "Description 1", 100);
    when(productRepository.findById(id)).thenReturn(java.util.Optional.of(existingProduct));
    when(productRepository.save(existingProduct)).thenReturn(existingProduct);

    Product result = productService.update(id, product);

    assertNotNull(result);
    assertEquals("Product 1", result.getName());
  }

  @Test
  void testUpdateThrowsException() {
    Long id = 1L;
    Product product = new Product(id, "Product 1", "Description 1", 100);
    when(productRepository.findById(id)).thenReturn(java.util.Optional.empty());

    assertThrows(RuntimeException.class, () -> {
      productService.update(id, product);
    });
  }

  @Test
  void testDeleteById() {
    Long id = 1L;
    Product product = new Product(id, "Product 1", "Description 1", 100);
    when(productRepository.findById(id)).thenReturn(java.util.Optional.of(product));

    productService.deleteById(id);
  }

  @Test
  void testDeleteByIdThrowsException() {
    Long id = 1L;
    when(productRepository.findById(id)).thenReturn(java.util.Optional.empty());

    assertThrows(RuntimeException.class, () -> {
      productService.deleteById(id);
    });
  }
}
