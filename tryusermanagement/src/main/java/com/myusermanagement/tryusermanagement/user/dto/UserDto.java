package com.myusermanagement.tryusermanagement.user.dto;

import com.myusermanagement.tryusermanagement.user.entities.*;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class UserDto implements Serializable {

    public UserDto() {
        // empty constructor
        roles = new ArrayList<>();
        permissions = new ArrayList<>();
    }

    public UserDto(User user) {
        if (user != null) {
            this.id = user.getId();
            this.username = user.getUsername();
            this.password = user.getPassword();
            this.nik = user.getNik();
            this.adminLevel = user.getAdminLevel().name();


            this.creationDt = user.getCreationDt();
            this.updatedDt = user.getUpdatedDt();
            this.loginDt = user.getLoginDt();

            this.secured = user.isSecured();

            // contact, if set
            if (user.getBankContact() != null) {
                this.bankContactDto = new BankContactDto(user.getBankContact());
            }

            // address, if set
            if (user.getBankBranchAddress() != null) {
                this.bankBranchAddressDto = new BankBranchAddressDto(user.getBankBranchAddress());
            }

            // Because the permissions can be associated to more than one roles i'm creating two String arrays
            // with the distinct keys of roles and permissions.
            roles = new ArrayList<>();
            permissions = new ArrayList<>();

            for (Role role : user.getRoles()) {
                roles.add(role.getRole());
                for (Permission p : role.getPermissions()) {
                    String key = p.getPermission();
                    if ((!permissions.contains(key)) && (p.isEnabled())) {
                        // add the permission only if enabled
                        permissions.add(key);
                    }
                }
            }



        }
    }

    private Long id;

    private String username;

    private String password;

    private String nik;

    private String adminLevel;

    private BankContactDto bankContactDto;

    private BankBranchAddressDto bankBranchAddressDto;

    private boolean secured;

    private java.time.LocalDateTime creationDt;

    private java.time.LocalDateTime updatedDt;

    private java.time.LocalDateTime loginDt;


    // permissions and roles list
    private List<String> roles;
    private List<String> permissions;
}
