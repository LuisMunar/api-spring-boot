package springboot.api.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import springboot.api.entities.SecretManager;

public interface SecretManagerRepository extends CrudRepository<SecretManager, String> {
  List<SecretManager> findByUserIdAndValidTrue(Long userId);
}
