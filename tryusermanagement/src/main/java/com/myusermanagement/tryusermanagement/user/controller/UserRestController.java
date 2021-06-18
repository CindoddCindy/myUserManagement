package com.myusermanagement.tryusermanagement.user.controller;

import com.myusermanagement.tryusermanagement.user.dto.UserDto;
import com.myusermanagement.tryusermanagement.user.dto.UserListDto;
import com.myusermanagement.tryusermanagement.user.dto.requests.CreateOrUpdateUserDto;
import com.myusermanagement.tryusermanagement.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/users")
public class UserRestController {

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<UserListDto> getUserPresentationList() {
        List<UserDto> list = userService.getUserPresentationList();
        UserListDto userListDTO = new UserListDto();
        list.stream().forEach(e -> userListDTO.getUserList().add(e));
        return ResponseEntity.ok(userListDTO);
    }

    @PostMapping
    public ResponseEntity<UserDto> createUser(@RequestBody CreateOrUpdateUserDto createOrUpdateUserDTO) {
        return new ResponseEntity(new UserDto(userService.createUser(createOrUpdateUserDTO)), null, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public UserDto getUserById(@PathVariable("id") Long id) {
        return new UserDto(userService.getUserById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable("id") Long id, @RequestBody CreateOrUpdateUserDto updateUserDTO) {
        return new ResponseEntity(new UserDto(userService.updateUser(id, updateUserDTO)), null, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable("id") Long id) {
        userService.deleteUserById(id);
        return ResponseEntity.noContent().build();
    }

    // add or remove a Role on a user
    @PostMapping("/{id}/roles/{roleId}")
    public ResponseEntity<UserDto> addRole(@PathVariable("id") Long id, @PathVariable("roleId") Long roleId) {
        return new ResponseEntity(new UserDto(userService.addRole(id, roleId)), null, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}/roles/{roleId}")
    public ResponseEntity<UserDto> removeRole(@PathVariable("id") Long id, @PathVariable("roleId") Long roleId) {
        return new ResponseEntity(new UserDto(userService.removeRole(id, roleId)), null, HttpStatus.OK);
    }
}
