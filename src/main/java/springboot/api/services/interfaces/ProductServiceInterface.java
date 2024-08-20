package springboot.api.services.interfaces;

import java.util.List;

import springboot.api.entities.Product;

public interface ProductServiceInterface {
  List<Product> findAll();
  Product findById(Long id);
  Product save(Product product);
  Product update(Long id, Product product);
  void deleteById(Long id);
}
