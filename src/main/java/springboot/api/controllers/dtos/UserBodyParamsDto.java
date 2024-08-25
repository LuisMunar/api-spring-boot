package springboot.api.controllers.dtos;

import jakarta.validation.constraints.NotBlank;
import springboot.api.entities.User;

public class UserBodyParamsDto {
  @NotBlank(message = "Username is required")
  private String username;

  @NotBlank(message = "Password is required")
  private String password;

  public UserBodyParamsDto() {}

  public UserBodyParamsDto(String username, String password) {
    this.username = username;
    this.password = password;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public User toEntity() {
    return new User(username, password, null);
  }
}
