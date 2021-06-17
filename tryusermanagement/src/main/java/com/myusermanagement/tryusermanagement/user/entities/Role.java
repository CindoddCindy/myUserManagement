package com.myusermanagement.tryusermanagement.user.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.security.Permission;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="roles")
@Data
@NoArgsConstructor
public class Role {

    public static final long BRANCH_MANAGER = 1;
    public static final long FINANCIAL_PLANING = 2;
    public static final long REMARKETING = 3;
    public static final long CARD_OPERATION = 4;
    public static final long BUSINESS_DEVELOPMENT = 5;
    public static final long COMMERCIAL_LENDING = 6;



    @Id
    @GeneratedValue(strategy= GenerationType.AUTO, generator="native")
    @GenericGenerator(name = "native", strategy = "native")
    @Column(name="id")
    private Long id;

    @Column(name="role", nullable = false)
    private String role;

    public Role(Long id, String role) {
        this.id = id;
        this.role = role;
    }

    @ManyToMany(fetch=FetchType.EAGER)
    @JoinTable(name = "permissions_roles",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id"))
    private List<Permission> permissions= new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Role )) return false;
        return id != null && id.equals(((Role) o).getId());
    }

    @Override
    public int hashCode() {
        return 31;
    }

}
