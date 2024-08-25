package springboot.api.services.interfaces;

import java.util.List;

import springboot.api.entities.User;

public interface UserServiceInterface {
  List<User> findAll();
  User findById(Long id);
  User save(User user);
}
