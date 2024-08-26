package springboot.api.controllers.dtos;

import jakarta.validation.constraints.NotBlank;

public class LoginBodyParamsDto {
  @NotBlank(message = "Email is required")
  private String username;

  @NotBlank(message = "Password is required")
  private String password;

  public LoginBodyParamsDto() {}

  public LoginBodyParamsDto(String username, String password) {
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
}
