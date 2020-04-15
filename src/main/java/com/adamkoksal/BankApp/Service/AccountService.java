package com.adamkoksal.BankApp.Service;

import com.adamkoksal.BankApp.Entity.Account;
import com.adamkoksal.BankApp.Repository.AccountRepository;
import com.adamkoksal.BankApp.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UserRepository userRepository;


    public String createNewAccount(Account account, int userId) {
        if (!userRepository.findById(userId).isPresent())  return "User does not exist";

        Account n = new Account();
        n.setUser(userRepository.findById(userId).get());
        n.setBalance(0);
        n.setName(account.getName());
        accountRepository.save(n);
        return "Account Created";
    }

    public String updateAccount(Account account, int accountId) {
        accountRepository.findById(accountId)
                .map(a -> {
                    a.setName(account.getName());
                    return accountRepository.save(a);
                });
        return "Account Update Successful";
    }

    public String deleteAccount(int accountId) {
        accountRepository.deleteById(accountId);
        return "Account Deleted";
    }

    public Iterable<Account> getAccountsByUser(int userId) {
        return accountRepository.getAccountsByUserId(userId);
    }

    public Optional<Account> getAccount(int accountId) {
        return accountRepository.findById(accountId);
    }
}
