package springboot.api.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import springboot.api.entities.Product;

public interface ProductRepository extends CrudRepository<Product, Long> {
  Optional<Product> findByName(String name);
}
