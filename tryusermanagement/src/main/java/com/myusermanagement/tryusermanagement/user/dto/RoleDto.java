package com.myusermanagement.tryusermanagement.user.dto;

import com.myusermanagement.tryusermanagement.user.entities.Role;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Data
public class RoleDto implements Serializable {

    private Long id;
    private String role;

    private List<PermissionDto> permissionDtos = new ArrayList<>();

    public RoleDto(Role role) {
        this.id = role.getId();
        this.role = role.getRole();

        // permissions
        role.getPermissions().stream().forEach(permission -> permissionDtos.add(new PermissionDto(permission)));
    }

    public RoleDto(Long id, String role) {
        this.id = id;
        this.role = role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RoleDto)) return false;
        return id != null && id.equals(((RoleDto) o).getId());
    }

    @Override
    public int hashCode() {
        return 31;
    }
}
