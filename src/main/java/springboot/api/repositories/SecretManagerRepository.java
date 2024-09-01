package springboot.api.repositories;

import org.springframework.data.repository.CrudRepository;

import springboot.api.entities.SecretManager;

public interface SecretManagerRepository extends CrudRepository<SecretManager, String> {}
