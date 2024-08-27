package springboot.api.services;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

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
import springboot.api.util.SimpleGrantedAuthorityJsonCreator;

public class JWTValidationService extends BasicAuthenticationFilter {
  public JWTValidationService(AuthenticationManager authenticationManager) {
    super(authenticationManager);
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

      Claims claims = Jwts.parser().verifyWith(TokenConstants.SECRET_KEY).build().parseSignedClaims(token).getPayload();
      System.out.println("CLAIMS => " + claims);
      String usernameOne = claims.getSubject();
      System.out.println("USERNAME_ONE => " + usernameOne);
      String usernameTwo = claims.get("username", String.class);
      System.out.println("USERNAME_TWO => " + usernameTwo);
      Object rolesClaims = claims.get("roles");
      System.out.println("ROLES_CLAIMS => " + rolesClaims);

      Collection<? extends GrantedAuthority> authorities = Arrays.asList(
        new ObjectMapper()
        .addMixIn(SimpleGrantedAuthority.class, SimpleGrantedAuthorityJsonCreator.class)
        .readValue(rolesClaims.toString().getBytes(), SimpleGrantedAuthority[].class)
      );

      UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(usernameOne, null, authorities);
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
