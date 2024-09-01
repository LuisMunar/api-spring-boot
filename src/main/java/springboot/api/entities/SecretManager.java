package springboot.api.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "secret_manager")
public class SecretManager {
  @Id
  @Column(nullable = false, unique = true, length = 512)
  String token;

  @Column(nullable = false, length = 512)
  String secretKey;

  @Column(nullable = false)
  Long userId;

  Boolean valid;

  public SecretManager(String token, String secretKey, Long userId, Boolean valid) {
    this.token = token;
    this.secretKey = secretKey;
    this.userId = userId;
    this.valid = valid;
  }

  public SecretManager() {}

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public String getSecretKey() {
    return secretKey;
  }

  public void setSecretKey(String secretKey) {
    this.secretKey = secretKey;
  }

  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

  public Boolean getValid() {
    return valid;
  }

  public void setValid(Boolean valid) {
    this.valid = valid;
  }
}
