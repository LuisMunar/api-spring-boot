package springboot.api.repositories;

import org.springframework.data.repository.CrudRepository;

import springboot.api.entities.User;

public interface UserRepository extends CrudRepository<User, Long> {}
