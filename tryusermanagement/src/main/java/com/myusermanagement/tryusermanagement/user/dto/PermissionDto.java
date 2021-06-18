package com.myusermanagement.tryusermanagement.user.dto;

import com.myusermanagement.tryusermanagement.user.entities.Permission;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@Data
public class PermissionDto implements java.io.Serializable {

    private Long id;
    private String permission;
    private boolean enabled;


    public PermissionDto(Permission permission) {
        this.id = permission.getId();
        this.permission = permission.getPermission();
        this.enabled = permission.isEnabled();

    }


}
