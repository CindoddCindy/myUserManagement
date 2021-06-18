package com.myusermanagement.tryusermanagement.user.entities;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="users")
@Data
public class User {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO, generator="native")
    @GenericGenerator(name = "native", strategy = "native")
    @Column(name="id")
    private Long id;

    @Column(name="username", nullable = false)
    private String username;

    @Column(name="password", nullable = false)
    private String password;

    @Column(name="nik", nullable = false)
    private String nik;

    @Enumerated
    @Column(columnDefinition = "tinyint")
    private AdminLevel adminLevel;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private BankBranchAddress bankBranchAddress;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private BankContact bankContact;

    @Column(name="secured")
    private boolean secured;


    @Basic
    private java.time.LocalDateTime creationDt;

    @Basic
    private java.time.LocalDateTime updatedDt;

    @Basic
    private java.time.LocalDateTime loginDt;

    @ManyToMany(fetch=FetchType.EAGER)
    @JoinTable(name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();


}
