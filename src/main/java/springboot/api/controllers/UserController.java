package springboot.api.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import springboot.api.controllers.dtos.UserBodyParamsDto;
import springboot.api.controllers.dtos.UserBodyResponseDto;
import springboot.api.entities.User;
import springboot.api.exceptions.RequestBodyException;
import springboot.api.exceptions.ResourceAlreadyExistsException;
import springboot.api.exceptions.ResourceNotFoundException;
import springboot.api.services.UserService;
import springboot.api.util.ResponseUtil;

@RestController
@RequestMapping("/users")
public class UserController {
  @Autowired
  private UserService userService;

  UserBodyResponseDto userBodyReponseDto = new UserBodyResponseDto();

  @GetMapping
  public ResponseEntity<Map<String, Object>> getAll() {
    try {
      List<User> users = userService.findAll();
      List<UserBodyResponseDto> usersResult = users.stream().map(user -> userBodyReponseDto.toDto(user)).toList();
      return ResponseUtil.response(HttpStatus.OK.value(), "Users found", usersResult);
    } catch (Exception e) {
      return ResponseUtil.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error trying get users");
    }
  }

  @GetMapping("/{id}")
  public ResponseEntity<Map<String, Object>> getById(@PathVariable Long id) {
    try {
      User user = userService.findById(id);
      UserBodyResponseDto userResult = userBodyReponseDto.toDto(user);
      return ResponseUtil.response(HttpStatus.OK.value(), "User found", userResult);
    } catch (ResourceNotFoundException e) {
      return ResponseUtil.error(HttpStatus.NOT_FOUND.value(), e.getMessage());
    } catch (Exception e) {
      return ResponseUtil.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error trying get user");
    }
  }

  @PostMapping
  public ResponseEntity<Map<String, Object>> create(@Valid @RequestBody UserBodyParamsDto userBodyParamsDto, BindingResult bindingResult) {
    try {
      if (bindingResult.hasErrors()) {
        throw new RequestBodyException(bindingResult.getAllErrors().get(0).getDefaultMessage());
      }

      User userCreated = userService.save(userBodyParamsDto.toEntity());
      UserBodyResponseDto userResult = userBodyReponseDto.toDto(userCreated);
      return ResponseUtil.response(HttpStatus.CREATED.value(), "User created", userResult);
    } catch (RequestBodyException e) {
      return ResponseUtil.error(HttpStatus.BAD_REQUEST.value(), e.getMessage());
    } catch (ResourceAlreadyExistsException e) {
      return ResponseUtil.error(HttpStatus.BAD_REQUEST.value(), e.getMessage());
    } catch (Exception e) {
      return ResponseUtil.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error trying create user");
    }
  }
}
