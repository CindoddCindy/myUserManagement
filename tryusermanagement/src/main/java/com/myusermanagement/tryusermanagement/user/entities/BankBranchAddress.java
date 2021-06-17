package com.myusermanagement.tryusermanagement.user.entities;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name="branch_addresses")
@Data
public class BankBranchAddress {

    @Id
    @Column(name="user_id")
    private Long userId;

    @Column(name="province")
    private String province;

    @Column(name="city")
    private String city;

    @Column(name="street")
    private String street;

    @Column(name="postal_code")
    private String postal_code;


    @OneToOne
    @MapsId
    private User user;
}
