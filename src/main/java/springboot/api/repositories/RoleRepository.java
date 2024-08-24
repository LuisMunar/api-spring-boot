package springboot.api.repositories;

import org.springframework.data.repository.CrudRepository;

import springboot.api.entities.Role;

public interface RoleRepository extends CrudRepository<Role, Long>{}
