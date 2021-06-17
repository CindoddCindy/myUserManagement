package com.myusermanagement.tryusermanagement.user.repository;

import com.myusermanagement.tryusermanagement.user.entities.BankContact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BankContactRepository extends JpaRepository<BankContact,Long> {
}
