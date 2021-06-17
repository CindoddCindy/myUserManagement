package com.myusermanagement.tryusermanagement.user.entities;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name="bank_contact")
@Data
public class BankContact {

    @Id
    @Column(name="user_id")
    private Long userId;

    @Column(name="phone")
    private String phone;

    @Column(name="email")
    private String email;

    @Column(name="official_website")
    private String official_website;



    @OneToOne
    @MapsId
    private User user;
}
