package springboot.api.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import springboot.api.entities.User;
import springboot.api.repositories.UserRepository;

@Service
public class AuthService implements UserDetailsService {
  private UserRepository userRepository;

  @Transactional(readOnly = true)
  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = userRepository.findByUsername(username).orElse(null);

    if (user == null) {
      throw new UsernameNotFoundException("User not found");
    }

    List<GrantedAuthority> authorities = user.getRoles().stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
    
    return new org.springframework.security.core.userdetails.User(
      user.getUsername(),
      user.getPassword(),
      user.isEnabled(),
      true,
      true,
      true,
      authorities
    );
  }
}
