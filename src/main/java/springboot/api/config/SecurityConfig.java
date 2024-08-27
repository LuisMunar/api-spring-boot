package springboot.api.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import springboot.api.services.JWTAuthenticationService;
import springboot.api.services.JWTValidationService;

@Configuration
public class SecurityConfig {
  @Autowired
  private AuthenticationConfiguration authenticationConfiguration;

  @Bean
  AuthenticationManager authenticationManager() throws Exception {
    return authenticationConfiguration.getAuthenticationManager();
  }

  @Bean
  PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    return http.authorizeHttpRequests(
      (auth) -> auth
        .requestMatchers(HttpMethod.POST, "/users")
        .permitAll()
        .requestMatchers(HttpMethod.GET, "/products")
        .permitAll()
        .anyRequest()
        .authenticated()
    )
      .addFilter(new JWTAuthenticationService(authenticationManager()))
      .addFilter(new JWTValidationService(authenticationManager()))
      .csrf(config -> config.disable())
      .sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
      .build();
  }
}
