package springboot.api.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import springboot.api.constants.RolesConstants;
import springboot.api.entities.Role;
import springboot.api.entities.User;
import springboot.api.exceptions.ResourceAlreadyExistsException;
import springboot.api.exceptions.ResourceNotFoundException;
import springboot.api.repositories.RoleRepository;
import springboot.api.repositories.UserRepository;
import springboot.api.services.interfaces.UserServiceInterface;

@Service
public class UserService implements UserServiceInterface {
  @Autowired
  private UserRepository userRepository;

  @Autowired
  private RoleRepository roleRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Transactional
  @Override
  public List<User> findAll() {
    try {
      return (List<User>) userRepository.findAll();
    } catch (Exception e) {
      throw e;
    }
  }

  @Transactional
  @Override
  public User findById(Long id) {
    User user = userRepository.findById(id).orElse(null);

    if (user == null) {
      throw new ResourceNotFoundException("User not found");
    }

    return user;
  }

  @Transactional
  @Override
  public User save(User user) {
    User existingUser = userRepository.findByUsername(user.getUsername()).orElse(null);

    if (existingUser != null) {
      throw new ResourceAlreadyExistsException("User already exists");
    }

    List<Role> rolesToAdd = new ArrayList<>();

    Optional<Role> defaultRole = roleRepository.findByName(RolesConstants.ROLE_USER);
    if (defaultRole.isPresent()) {
      rolesToAdd.add(defaultRole.get());
    }
    user.setRoles(rolesToAdd);

    String passwordEncode = passwordEncoder.encode(user.getPassword());
    user.setPassword(passwordEncode);

    return userRepository.save(user);
  }
}
