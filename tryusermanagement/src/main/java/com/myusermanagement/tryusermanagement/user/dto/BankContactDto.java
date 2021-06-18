package com.myusermanagement.tryusermanagement.user.dto;

import com.myusermanagement.tryusermanagement.user.entities.BankContact;
import lombok.Data;

import javax.persistence.Column;
import java.io.Serializable;

@Data
public class BankContactDto implements java.io.Serializable  {


    private String phone;

    private String email;

    private String official_website;


    public BankContactDto() {
        // empty constructor
    }

    public BankContactDto(BankContact bankContact){
        if (bankContact != null) {
            this.phone = bankContact.getPhone();
            this.email = bankContact.getEmail();
            this.official_website = bankContact.getOfficial_website();

        }
    }


}
