package springboot.api.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import springboot.api.entities.User;

public interface UserRepository extends CrudRepository<User, Long> {
  Optional<User> findByUsername(String username);
}
