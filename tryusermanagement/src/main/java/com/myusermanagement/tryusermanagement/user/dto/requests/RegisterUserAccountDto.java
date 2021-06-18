package com.myusermanagement.tryusermanagement.user.dto.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterUserAccountDto implements Serializable {
    private String username;

    private String password;

    private String nik;

    private String adminLevel;

    //bank  branch address

    private String province;
    private String city;
    private String street;
    private String postal_code;

    //bank contact

    private String phone;

    private String email;

    private String official_website;




}
