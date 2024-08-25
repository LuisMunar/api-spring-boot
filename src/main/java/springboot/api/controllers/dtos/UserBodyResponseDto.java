package springboot.api.controllers.dtos;

import springboot.api.entities.User;

public class UserBodyResponseDto {
  private Long id;
  private String username;

  public UserBodyResponseDto() {}

  public UserBodyResponseDto(Long id, String username) {
    this.id = id;
    this.username = username;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public UserBodyResponseDto toDto(User user) {
    Long id = user.getId();
    String username = user.getUsername();
    
    return new UserBodyResponseDto(id, username);
  }
}
