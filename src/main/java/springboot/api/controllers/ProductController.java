package springboot.api.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import springboot.api.controllers.dtos.ProductBodyParamsDto;
import springboot.api.entities.Product;
import springboot.api.exceptions.RequestBodyException;
import springboot.api.exceptions.ResourceAlreadyExistsException;
import springboot.api.exceptions.ResourceNotFoundException;
import springboot.api.services.interfaces.ProductServiceInterface;
import springboot.api.util.ResponseUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/products")
public class ProductController {
  
  @Autowired
  private ProductServiceInterface productService;

  @GetMapping
  public ResponseEntity<Map<String, Object>> getAll() {
    try {
      List<Product> products = productService.findAll();
      return ResponseUtil.response(HttpStatus.OK.value(), "Products found", products);
    } catch (Exception e) {
      return ResponseUtil.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error trying get products");
    }
  }

  @GetMapping("/{id}")
  public ResponseEntity<Map<String, Object>> get(@PathVariable Long id) {
    try {
      Product product = productService.findById(id);
      return ResponseUtil.response(HttpStatus.OK.value(), "Product found", product);
    } catch (ResourceNotFoundException e) {
      return ResponseUtil.error(HttpStatus.NOT_FOUND.value(), e.getMessage());
    } catch (Exception e) {
      return ResponseUtil.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error trying get product");
    }
  }

  @PostMapping()
  public ResponseEntity<Map<String, Object>> create(@Valid @RequestBody ProductBodyParamsDto productDto, BindingResult bindingResult) {
    try {
      if (bindingResult.hasErrors()) {
        throw new RequestBodyException(bindingResult.getAllErrors().get(0).getDefaultMessage());
      }

      Product productCreated = productService.save(productDto.toEntity());
      return ResponseUtil.response(HttpStatus.CREATED.value(), "Product created", productCreated);
    } catch (RequestBodyException e) {
      return ResponseUtil.error(HttpStatus.BAD_REQUEST.value(), e.getMessage());
    } catch (ResourceAlreadyExistsException e) {
      return ResponseUtil.error(HttpStatus.BAD_REQUEST.value(), e.getMessage());
    } catch (Exception e) {
      return ResponseUtil.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error trying create product");
    }
  }

  @PutMapping("/{id}")
  public ResponseEntity<Map<String, Object>> update(@PathVariable Long id, @Valid @RequestBody Product product, BindingResult bindingResult) {
    try {
      if (bindingResult.hasErrors()) {
        throw new RequestBodyException(bindingResult.getAllErrors().get(0).getDefaultMessage());
      }

      Product productUpdated = productService.update(id, product);
      return ResponseUtil.response(HttpStatus.OK.value(), "Product updated", productUpdated);
    } catch (RequestBodyException e) {
      return ResponseUtil.error(HttpStatus.BAD_REQUEST.value(), e.getMessage());
    } catch (ResourceNotFoundException e) {
      return ResponseUtil.error(HttpStatus.NOT_FOUND.value(), e.getMessage());
    } catch (Exception e) {
      return ResponseUtil.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error trying update product");
    }
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Map<String, Object>> delete(@PathVariable Long id) {
    try {
      productService.deleteById(id);
      return ResponseUtil.response(HttpStatus.OK.value(), "Product deleted", null);
    } catch (ResourceNotFoundException e) {
      return ResponseUtil.error(HttpStatus.NOT_FOUND.value(), e.getMessage());
    } catch (Exception e) {
      return ResponseUtil.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error trying delete product");
    }
  }
}
