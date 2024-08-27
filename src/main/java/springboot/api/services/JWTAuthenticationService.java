package springboot.api.services;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import springboot.api.constants.TokenConstants;
import springboot.api.entities.User;

public class JWTAuthenticationService extends UsernamePasswordAuthenticationFilter {
  private AuthenticationManager authenticationManager;

  public JWTAuthenticationService(AuthenticationManager authenticationManager) {
    this.authenticationManager = authenticationManager;
  }

  @Override
  public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
    try {
      User user = null;
      String username = null;
      String password = null;

      user = new ObjectMapper().readValue(request.getInputStream(), User.class);
      username = user.getUsername();
      password = user.getPassword();

      UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);

      return authenticationManager.authenticate(authenticationToken);
    } catch (StreamReadException e) {
      throw new RuntimeException(e);
    } catch (DatabindException e) {
      throw new RuntimeException(e);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
    org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) authResult.getPrincipal();
    String username = user.getUsername();

    Collection<? extends GrantedAuthority> roles = authResult.getAuthorities();
    Claims claims = Jwts
      .claims()
      .add("username", username)
      .add("roles", new ObjectMapper().writeValueAsString(roles))
      .build();

    String token = Jwts.builder()
      .subject(username)
      .claims(claims)
      .expiration(new Date(System.currentTimeMillis() + TokenConstants.EXPIRATION_TIME))
      .issuedAt(new Date())
      .signWith(TokenConstants.SECRET_KEY)
      .compact();

    response.addHeader(TokenConstants.HEADER_AUTHORIZATION, TokenConstants.PREFIX_TOKEN + token);
    Map<String, String> body = new HashMap<>();
    body.put("token", token);
    body.put("username", username);
    body.put("message", "User authenticated");

    response.getWriter().write(new ObjectMapper().writeValueAsString(body));
    response.setContentType(TokenConstants.CONTENT_TYPE);
    response.setStatus(HttpStatus.OK.value());
  }

  @Override
  protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
    Map<String, String> body = new HashMap<>();
    body.put("message", "Authentication failed");
    body.put("error", failed.getMessage());

    response.getWriter().write(new ObjectMapper().writeValueAsString(body));
    response.setContentType(TokenConstants.CONTENT_TYPE);
    response.setStatus(HttpStatus.UNAUTHORIZED.value());
  }
}
