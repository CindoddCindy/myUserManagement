package com.myusermanagement.tryusermanagement.user.dto.requests;

import com.myusermanagement.tryusermanagement.user.entities.BankBranchAddress;
import com.myusermanagement.tryusermanagement.user.entities.BankContact;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateOrUpdateUserDto implements Serializable {

    //user

    private String username;

    private String password;

    private String nik;

    private String adminLevel;

    private BankBranchAddress bankBranchAddress;

    private BankContact bankContact;

    private boolean secured;

    private java.time.LocalDateTime creationDt;

    private java.time.LocalDateTime updatedDt;

    private java.time.LocalDateTime loginDt;

    //bank branch address


    private String province;

    private String city;

    private String street;

    private String postal_code;



    //bank contact


    private String phone;

    private String email;

    private String official_website;
}
