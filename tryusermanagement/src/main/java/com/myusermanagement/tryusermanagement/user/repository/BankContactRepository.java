package com.myusermanagement.tryusermanagement.user.repository;

import com.myusermanagement.tryusermanagement.user.entities.BankContact;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BankContactRepository extends CrudRepository<BankContact,Long> {
}
