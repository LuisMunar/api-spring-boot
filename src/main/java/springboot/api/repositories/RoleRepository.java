package springboot.api.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import springboot.api.entities.Role;

public interface RoleRepository extends CrudRepository<Role, Long>{
  Optional<Role> findByName(String name);
}
