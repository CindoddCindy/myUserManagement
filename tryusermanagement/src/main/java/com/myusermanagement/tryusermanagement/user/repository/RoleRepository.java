package com.myusermanagement.tryusermanagement.user.repository;

import com.myusermanagement.tryusermanagement.user.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
}
