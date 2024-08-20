package springboot.api.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import springboot.api.entities.Product;
import springboot.api.exceptions.ResourceAlreadyExistsException;
import springboot.api.exceptions.ResourceNotFoundException;
import springboot.api.repositories.ProductRepository;
import springboot.api.services.interfaces.ProductServiceInterface;

@Service
public class ProductService implements ProductServiceInterface {
  @Autowired
  private ProductRepository productRepository;

  @Transactional
  @Override
  public List<Product> findAll() {
    try {
      return (List<Product>) productRepository.findAll();
    } catch (Exception e) {
      throw e;
    }
  }

  @Transactional
  @Override
  public Product findById(Long id) {
    Product product = productRepository.findById(id).orElse(null);

    if (product == null) {
      throw new ResourceNotFoundException("Product not found");
    }

    return product;
  }

  @Transactional
  @Override
  public Product save(Product product) {
    String productName = product.getName();
    Product existingProduct = productRepository.findByName(productName).orElse(null);

    if (existingProduct != null) {
      throw new ResourceAlreadyExistsException("Product already exists");
    }

    return productRepository.save(product);
  }

  @Transactional
  @Override
  public Product update(Long id, Product product) {
    Product existingProduct = productRepository.findById(id).orElse(null);

    if (existingProduct == null) {
      throw new ResourceNotFoundException("Product not found");
    }

    existingProduct.setName(product.getName());
    existingProduct.setDescription(product.getDescription());
    existingProduct.setPrice(product.getPrice());

    return productRepository.save(existingProduct);
  }

  @Transactional
  @Override
  public void deleteById(Long id) {
    Product product = productRepository.findById(id).orElse(null);

    if (product == null) {
      throw new ResourceNotFoundException("Product not exists");
    }

    productRepository.deleteById(id);
  }
}
