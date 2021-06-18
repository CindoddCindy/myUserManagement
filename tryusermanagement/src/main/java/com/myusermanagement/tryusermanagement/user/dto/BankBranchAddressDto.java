package com.myusermanagement.tryusermanagement.user.dto;

import com.myusermanagement.tryusermanagement.user.entities.BankBranchAddress;
import lombok.Data;
import org.apache.tomcat.jni.Address;

import javax.persistence.Column;
import java.io.Serializable;

@Data
public class BankBranchAddressDto implements Serializable {

    public BankBranchAddressDto() {
        // empty constructor
    }

    public BankBranchAddressDto(BankBranchAddress bankBranchAddress) {
        if (bankBranchAddress != null) {
            this.province = bankBranchAddress.getProvince();
            this.city = bankBranchAddress.getCity();
            this.street = bankBranchAddress.getStreet();
            this.postal_code = bankBranchAddress.getPostal_code();

        }
    }


    private String province;
    private String city;
    private String street;
    private String postal_code;
}
