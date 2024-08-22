package springboot.api.controllers;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.mockito.ArgumentMatchers.any;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import springboot.api.controllers.dtos.ProductBodyParamsDto;
import springboot.api.entities.Product;
import springboot.api.exceptions.ResourceAlreadyExistsException;
import springboot.api.exceptions.ResourceNotFoundException;
import springboot.api.services.interfaces.ProductServiceInterface;

public class ProductControllerTest {
  @Autowired
  private MockMvc mockMvc;

  @Mock
  private ProductServiceInterface productService;

  @InjectMocks
  private ProductController productController;

  private ObjectMapper objectMapper = new ObjectMapper();

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    mockMvc = MockMvcBuilders.standaloneSetup(productController).build();
  }

  @Test
  void testGetAllSuccess() throws Exception {
    Product product1 = new Product(1L, "Product 1", "Description 1", 100);
    Product product2 = new Product(2L, "Product 2", "Description 2", 200);
    List<Product> productList = Arrays.asList(product1, product2);

    when(productService.findAll()).thenReturn(productList);

    mockMvc.perform(get("/products").contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.message").value("Products found"))
      .andExpect(jsonPath("$.data[0].name").value("Product 1"))
      .andExpect(jsonPath("$.data[1].name").value("Product 2"));

    verify(productService).findAll();
  }

  @Test
  void testGetAllException() throws Exception {
    doThrow(new RuntimeException("Database error")).when(productService).findAll();

    mockMvc.perform(get("/products").contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isInternalServerError())
      .andExpect(jsonPath("$.message").value("Error trying get products"));

    verify(productService).findAll();
  }

  @Test
  void testGetSuccess() throws Exception {
    Long productId = 1L;
    Product product = new Product(productId, "Product 1", "Description 1", 100);
    when(productService.findById(productId)).thenReturn(product);

    mockMvc.perform(get("/products/{id}", productId).contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.message").value("Product found"))
      .andExpect(jsonPath("$.data.name").value("Product 1"))
      .andExpect(jsonPath("$.data.description").value("Description 1"))
      .andExpect(jsonPath("$.data.price").value(100));

    verify(productService).findById(productId);
  }

  @Test
  void testGetNotFound() throws Exception {
    Long productId = 999L;
    when(productService.findById(productId)).thenThrow(new ResourceNotFoundException("Product not found"));

    mockMvc.perform(get("/products/{id}", productId).contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isNotFound())
      .andExpect(jsonPath("$.message").value("Product not found"));

    verify(productService).findById(productId);
  }

  @Test
  void testGetInternalServerError() throws Exception {
    Long productId = 1L;
    when(productService.findById(productId)).thenThrow(new RuntimeException("Internal server error"));

    mockMvc.perform(get("/products/{id}", productId).contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isInternalServerError())
      .andExpect(jsonPath("$.message").value("Error trying get product"));

    verify(productService).findById(productId);
  }

  @Test
  void testCreateSuccess() throws Exception {
    ProductBodyParamsDto productDto = new ProductBodyParamsDto("Product 1", "Description 1", 100);
    Product product = productDto.toEntity();
    Product createdProduct = new Product(1L, "Product 1", "Description 1", 100);

    when(productService.save(product)).thenReturn(createdProduct);

    mockMvc.perform(
      post("/products")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(productDto))
    )
      .andExpect(status().isCreated())
      .andExpect(jsonPath("$.message").value("Product created"));

    verify(productService).save(any(Product.class));
  }

  @Test
  void testCreateValidationError() throws Exception {
    ProductBodyParamsDto productDto = new ProductBodyParamsDto("", "Description 1", 100);

    mockMvc.perform(
      post("/products")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(productDto))
    )
      .andExpect(status().isBadRequest())
      .andExpect(jsonPath("$.message").exists());
  }

  @Test
  void testCreateResourceAlreadyExists() throws Exception {
    ProductBodyParamsDto productDto = new ProductBodyParamsDto("Product 1", "Description 1", 100);

    doThrow(new ResourceAlreadyExistsException("Product already exists")).when(productService).save(any(Product.class));

    mockMvc.perform(
      post("/products")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(productDto))
    )
      .andExpect(status().isBadRequest())
      .andExpect(jsonPath("$.message").value("Product already exists"));

    verify(productService).save(any(Product.class));
  }

  @Test
  void testCreateInternalServerError() throws Exception {
    ProductBodyParamsDto productDto = new ProductBodyParamsDto("Product 1", "Description 1", 100);

    doThrow(new RuntimeException("Internal server error")).when(productService).save(any(Product.class));

    mockMvc.perform(
      post("/products")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(productDto))
    )
      .andExpect(status().isInternalServerError())
      .andExpect(jsonPath("$.message").value("Error trying create product"));

    verify(productService).save(any(Product.class));
  }

  @Test
  void testUpdateSuccess() throws Exception {
    ProductBodyParamsDto productDto = new ProductBodyParamsDto("Updated Product", "Updated Description", 150);
    Product updatedProduct = new Product(1L, "Updated Product", "Updated Description", 150);

    when(productService.update(any(Long.class), any(Product.class))).thenReturn(updatedProduct);

    mockMvc.perform(
      put("/products/1")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(productDto))
    )
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.message").value("Product updated"))
      .andExpect(jsonPath("$.data.name").value("Updated Product"));

    verify(productService).update(any(Long.class), any(Product.class));
  }

  @Test
  void testUpdateValidationError() throws Exception {
    ProductBodyParamsDto productDto = new ProductBodyParamsDto(null, "Updated Description", 150);

    mockMvc.perform(
      put("/products/1")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(productDto))
    )
      .andExpect(status().isBadRequest())
      .andExpect(jsonPath("$.message").exists());
  }

  @Test
  void testUpdateResourceNotFound() throws Exception {
    ProductBodyParamsDto productDto = new ProductBodyParamsDto("Updated Product", "Updated Description", 150);

    doThrow(new ResourceNotFoundException("Product not found")).when(productService).update(any(Long.class), any(Product.class));

    mockMvc.perform(
      put("/products/1")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(productDto))
    )
      .andExpect(status().isNotFound())
      .andExpect(jsonPath("$.message").value("Product not found"));

    verify(productService).update(any(Long.class), any(Product.class));
  }

  @Test
  void testUpdateInternalServerError() throws Exception {
    ProductBodyParamsDto productDto = new ProductBodyParamsDto("Updated Product", "Updated Description", 150);

    doThrow(new RuntimeException("Internal server error")).when(productService).update(any(Long.class), any(Product.class));

    mockMvc.perform(
      put("/products/1")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(productDto))
    )
      .andExpect(status().isInternalServerError())
      .andExpect(jsonPath("$.message").value("Error trying update product"));

    verify(productService).update(any(Long.class), any(Product.class));
  }

  @Test
  void testDeleteSuccess() throws Exception {
    Long productId = 1L;
    doNothing().when(productService).deleteById(productId);

    mockMvc.perform(
      delete("/products/{id}", productId)
        .contentType(MediaType.APPLICATION_JSON)
    )
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.message").value("Product deleted"));

    verify(productService, times(1)).deleteById(productId);
  }

  @Test
  void testDeleteNotFound() throws Exception {
    Long productId = 999L;
    doThrow(new ResourceNotFoundException("Product not found")).when(productService).deleteById(productId);

    mockMvc.perform(
      delete("/products/{id}", productId)
        .contentType(MediaType.APPLICATION_JSON)
    )
      .andExpect(status().isNotFound())
      .andExpect(jsonPath("$.message").value("Product not found"));

    verify(productService, times(1)).deleteById(productId);
  }

  @Test
  void testDeleteInternalServerError() throws Exception {
    Long productId = 1L;
    doThrow(new RuntimeException("Internal server error")).when(productService).deleteById(productId);

    mockMvc.perform(
      delete("/products/{id}", productId)
        .contentType(MediaType.APPLICATION_JSON)
    )
      .andExpect(status().isInternalServerError())
      .andExpect(jsonPath("$.message").value("Error trying delete product"));

    verify(productService, times(1)).deleteById(productId);
  }
}
