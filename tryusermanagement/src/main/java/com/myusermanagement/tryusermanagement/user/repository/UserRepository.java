package com.myusermanagement.tryusermanagement.user.repository;

import com.myusermanagement.tryusermanagement.user.entities.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    @Query(value = "SELECT u FROM User u WHERE u.bankContact.email = :email")
    User findByEmail(@Param("email") String email);



    User findByUsername(String username);
}
