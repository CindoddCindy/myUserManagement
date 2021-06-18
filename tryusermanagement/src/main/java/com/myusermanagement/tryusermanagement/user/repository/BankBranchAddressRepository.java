package com.myusermanagement.tryusermanagement.user.repository;

import com.myusermanagement.tryusermanagement.user.entities.BankBranchAddress;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BankBranchAddressRepository extends CrudRepository<BankBranchAddress, Long> {
}
