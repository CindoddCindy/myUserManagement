package com.myusermanagement.tryusermanagement.user.controller;

import com.myusermanagement.tryusermanagement.user.dto.UserDto;
import com.myusermanagement.tryusermanagement.user.dto.requests.LoginRequestDto;
import com.myusermanagement.tryusermanagement.user.entities.User;
import com.myusermanagement.tryusermanagement.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/login")
@Slf4j
public class LoginRestController {

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<UserDto> login(@RequestBody LoginRequestDto loginRequestDTO) {
        User user = userService.login(loginRequestDTO.getUsername(), loginRequestDTO.getPassword());
        return ResponseEntity.ok(new UserDto(user));
    }
}
