package com.myusermanagement.tryusermanagement.user.controller;

import com.myusermanagement.tryusermanagement.user.dto.PermissionDto;
import com.myusermanagement.tryusermanagement.user.dto.RoleDto;
import com.myusermanagement.tryusermanagement.user.entities.Permission;
import com.myusermanagement.tryusermanagement.user.entities.Role;
import com.myusermanagement.tryusermanagement.user.service.EncryptionService;
import com.myusermanagement.tryusermanagement.user.service.PermissionService;
import com.myusermanagement.tryusermanagement.user.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/users/rbac")
public class RBACRestController {

    @Autowired
    private RoleService roleService;

    @Autowired
    private PermissionService permissionService;

    // roles
    @GetMapping("/roles")
    public ResponseEntity<List<RoleDto>> getRolePresentationList() {
        Iterable<Role> roleList = roleService.getRoleList();
        ArrayList<RoleDto> list = new ArrayList<>();
        roleList.forEach(e -> list.add(new RoleDto(e)));
        return ResponseEntity.ok(list);
    }

    @PostMapping("/roles")
    public ResponseEntity<RoleDto> createRole(@RequestBody String role) {
        return new ResponseEntity(new RoleDto(roleService.createRole(role)), null, HttpStatus.CREATED);
    }

    @GetMapping("/roles/{roleId}")
    public RoleDto getRoleById(@PathVariable("roleId") Long roleId) {
        return new RoleDto(roleService.getRoleById(roleId));
    }

    @DeleteMapping("/roles/{roleId}")
    public ResponseEntity<?> deleteRoleById(@PathVariable("roleId") Long roleId) {
        roleService.deleteRole(roleId);
        return ResponseEntity.noContent().build();
    }

    // retrieve the permission's list
    @GetMapping("/permissions")
    public ResponseEntity<List<PermissionDto>> getPermissionPresentationList() {
        Iterable<Permission> permissionList = permissionService.getPermissionList();
        ArrayList<PermissionDto> list = new ArrayList<>();
        permissionList.forEach(e -> list.add(new PermissionDto(e)));
        return ResponseEntity.ok(list);
    }

    // permissions

    @GetMapping("/permissions/{permissionKey}")
    public ResponseEntity<PermissionDto> getPermissionByKey(@PathVariable("permissionKey") String permissionKey) {
        PermissionDto permissionDTO = new PermissionDto(permissionService.getPermissionByKey(permissionKey));
        return ResponseEntity.ok(permissionDTO);
    }

    @PostMapping("/permissions")
    public ResponseEntity<PermissionDto> createPermission(@RequestBody PermissionDto permissionDTO) {
        return new ResponseEntity(new PermissionDto(permissionService.createPermission(permissionDTO)), HttpStatus.CREATED);
    }

    @PutMapping("/permissions")
    public ResponseEntity<PermissionDto> updatePermission(@RequestBody PermissionDto permissionDTO) {
        return new ResponseEntity(new PermissionDto(permissionService.updatePermission(permissionDTO)), HttpStatus.CREATED);
    }

    @DeleteMapping("/permissions/{permissionKey}")
    public ResponseEntity<?> deletePermissionByKey(@PathVariable("permissionKey") String permissionKey) {
        permissionService.deletePermissionByKey(permissionKey);
        return ResponseEntity.noContent().build();
    }

    // add or remove a Permission on a Role

    @PostMapping("/roles/{roleId}/permissions/{permissionKey}")
    public ResponseEntity<RoleDto> addPermissionOnRole(@PathVariable("roleId") Long roleId, @PathVariable("permissionKey") String permissionKey) {
        return new ResponseEntity(new RoleDto(roleService.addPermissionOnRole(roleId, permissionKey)), null, HttpStatus.CREATED);
    }

    @DeleteMapping("/roles/{roleId}/permissions/{permissionKey}")
    public ResponseEntity<RoleDto> removePermissionOnRole(@PathVariable("roleId") Long roleId, @PathVariable("permissionKey") String permissionKey) {
        return new ResponseEntity(new RoleDto(roleService.removePermissionOnRole(roleId, permissionKey)), null, HttpStatus.OK);
    }

    // salt generation
    @GetMapping("/salt")
    public ResponseEntity<String> generateSalt() {
        return new ResponseEntity<String>(EncryptionService.generateSalt(32), HttpStatus.CREATED);
    }

}
