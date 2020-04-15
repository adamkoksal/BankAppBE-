package com.adamkoksal.BankApp.Repository;

import com.adamkoksal.BankApp.Entity.Account;
import org.springframework.data.repository.CrudRepository;

public interface AccountRepository extends CrudRepository<Account, Integer> {

    Iterable<Account> getAccountsByUserId(int id);
}
