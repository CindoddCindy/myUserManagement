package com.myusermanagement.tryusermanagement.user.controller;

import com.myusermanagement.tryusermanagement.user.dto.UserDto;
import com.myusermanagement.tryusermanagement.user.dto.requests.RegisterUserAccountDto;
import com.myusermanagement.tryusermanagement.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/users")
public class RegisterRestController {

    @Autowired
    private UserService userService;

    // register a new user's account: no all the user information are required
    @PostMapping("/register")
    public ResponseEntity<UserDto> registerNewUserAccount(@RequestBody RegisterUserAccountDto registerUserAccountDTO) {
        return new ResponseEntity(new UserDto(userService.registerUserAccount(registerUserAccountDTO)), null, HttpStatus.CREATED);
    }
}
