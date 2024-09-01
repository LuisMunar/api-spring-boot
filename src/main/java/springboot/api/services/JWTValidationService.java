package springboot.api.services;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import javax.crypto.SecretKey;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import springboot.api.constants.TokenConstants;
import springboot.api.entities.SecretManager;
import springboot.api.repositories.SecretManagerRepository;
import springboot.api.util.SecretKeyUtil;
import springboot.api.util.SimpleGrantedAuthorityJsonCreator;

public class JWTValidationService extends BasicAuthenticationFilter {
  SecretManagerRepository secretManagerRepository;

  public JWTValidationService(
    AuthenticationManager authenticationManager,
    SecretManagerRepository secretManagerRepository
  ) {
    super(authenticationManager);
    this.secretManagerRepository = secretManagerRepository;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
    try {
      String headerToken = request.getHeader(TokenConstants.HEADER_AUTHORIZATION);

      if (headerToken == null || !headerToken.startsWith(TokenConstants.PREFIX_TOKEN)) {
        chain.doFilter(request, response);
        return;
      }

      String token = headerToken.replace(TokenConstants.PREFIX_TOKEN, "");

      SecretManager secretManager = secretManagerRepository.findById(token).orElse(null);
      if (secretManager == null || !secretManager.getValid()) {
        throw new Exception("Invalid session");
      }

      String secretKeyEncode = secretManager.getSecretKey();
      SecretKey secretKey = SecretKeyUtil.decode(secretKeyEncode);

      Claims claims = Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload();
      String username = claims.getSubject();
      Object rolesClaims = claims.get("roles");

      Collection<? extends GrantedAuthority> authorities = Arrays.asList(
        new ObjectMapper()
        .addMixIn(SimpleGrantedAuthority.class, SimpleGrantedAuthorityJsonCreator.class)
        .readValue(rolesClaims.toString().getBytes(), SimpleGrantedAuthority[].class)
      );

      UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, null, authorities);
      SecurityContextHolder.getContext().setAuthentication(authenticationToken);
      chain.doFilter(request, response);
    } catch (Exception e) {
      Map<String, String> body = new HashMap<>();
      body.put("message", "Invalid session");

      response.getWriter().write(new ObjectMapper().writeValueAsString(body));
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      response.setContentType(TokenConstants.CONTENT_TYPE);
    }
  }
}
