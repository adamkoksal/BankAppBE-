package com.adamkoksal.BankApp.Repository;

import com.adamkoksal.BankApp.Entity.Account;
import com.adamkoksal.BankApp.Entity.Transaction;
import org.springframework.data.repository.CrudRepository;

public interface TransactionRepository extends CrudRepository<Transaction, Integer> {

    Iterable<Transaction> getTransactionsByReceiverId(int receiverId);

    Iterable<Transaction> getTransactionsByInitiatorId(int initiatorId);

}
