package springboot.api.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import springboot.api.controllers.dtos.LoginBodyParamsDto;
import springboot.api.exceptions.RequestBodyException;
import springboot.api.services.AuthService;
import springboot.api.util.ResponseUtil;

@RestController
@RequestMapping("/auth")
public class AuthController {
  @Autowired
  private AuthService authService;

  @PostMapping("/login")
  public ResponseEntity<Map<String, Object>> login(@Valid @RequestBody LoginBodyParamsDto loginBodyParamsDto, BindingResult bindingResult) {
    try {
      if (bindingResult.hasErrors()) {
        throw new RequestBodyException(bindingResult.getAllErrors().get(0).getDefaultMessage());
      }

      return ResponseUtil.response(HttpStatus.CREATED.value(), "Session created", "OK");
    } catch (Exception e) {
      return ResponseUtil.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error trying do login");
    }
  }
}
