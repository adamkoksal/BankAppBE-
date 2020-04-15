package com.adamkoksal.BankApp.Service;

import com.adamkoksal.BankApp.Entity.Transaction;
import com.adamkoksal.BankApp.Repository.AccountRepository;
import com.adamkoksal.BankApp.Repository.TransactionRepository;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AccountRepository accountRepository;


    public String deposit(int receiverId, int amount) {
        if (!accountRepository.findById(receiverId).isPresent())  return "Account does not exist";

        Transaction n = new Transaction();
        n.setAmount(amount);
        n.setReceiver(accountRepository.findById(receiverId).get());
        n.setType("Deposit");
        n.setDate();

        accountRepository.findById(receiverId)
                .map(account -> {
                    account.setBalance(account.getBalance()+ amount);
                    return accountRepository.save(account);
                });
        transactionRepository.save(n);
        return "Deposit Successful";
    }

    public String withdrawal(int initiatorId, int amount) throws Exception {
        if (!accountRepository.findById(initiatorId).isPresent())  return "Account does not exist";

        if (accountRepository.findById(initiatorId).get().getBalance() < amount)
            throw new Exception("Insufficient Funds");
        else {
            Transaction n = new Transaction();
            n.setAmount(amount);
            n.setInitiator(accountRepository.findById(initiatorId).get());
            n.setType("Withdrawal");
            n.setDate();

            accountRepository.findById(initiatorId)
                    .map(account -> {
                        account.setBalance(account.getBalance() - amount);
                        return accountRepository.save(account);
                    });

            transactionRepository.save(n);
            return "Withdrawal Successful";
        }
    }

    public String transfer(int initiatorId, int receiverId, int amount) throws Exception {
        if (!accountRepository.findById(receiverId).isPresent())  return "Receiver account does not exist";
        if (!accountRepository.findById(initiatorId).isPresent())  return "Initiator account does not exist";

        if (accountRepository.findById(initiatorId).get().getBalance() < amount)
            throw new Exception("Insufficient Funds");

        else {
            Transaction n = new Transaction();
            n.setAmount(amount);
            n.setInitiator(accountRepository.findById(initiatorId).get());
            n.setReceiver(accountRepository.findById(receiverId).get());
            n.setType("Transfer");
            n.setDate();

            accountRepository.findById(initiatorId)
                    .map(account -> {
                        account.setBalance(account.getBalance() - amount);
                        return accountRepository.save(account);
                    });

            accountRepository.findById(receiverId)
                    .map(account -> {
                        account.setBalance(account.getBalance() + amount);
                        return accountRepository.save(account);
                    });
            transactionRepository.save(n);
            return "Transfer Successful";
        }
    }

    public Collection<Transaction> getTransactionsByAccount(int accountId) {
        Iterable<Transaction> combinedIterables = Iterables.concat(transactionRepository.getTransactionsByReceiverId(accountId),
                transactionRepository.getTransactionsByInitiatorId(accountId));

        return Lists.newArrayList(combinedIterables);
    }

    public String payBill(int initiatorId, int receiverId, int amount) throws Exception {
        if (!accountRepository.findById(receiverId).isPresent())  return "Receiver account does not exist";
        if (!accountRepository.findById(initiatorId).isPresent())  return "Initiator account does not exist";

        if (accountRepository.findById(initiatorId).get().getBalance() < amount)
            throw new Exception("Insufficient Funds");

        else {
            Transaction n = new Transaction();
            n.setAmount(amount);
            n.setInitiator(accountRepository.findById(initiatorId).get());
            n.setReceiver(accountRepository.findById(receiverId).get());
            n.setType("Bill Payment");
            n.setDate();

            accountRepository.findById(initiatorId)
                    .map(account -> {
                        account.setBalance(account.getBalance() - amount);
                        return accountRepository.save(account);
                    });

            accountRepository.findById(receiverId)
                    .map(account -> {
                        account.setBalance(account.getBalance() + amount);
                        return accountRepository.save(account);
                    });
            transactionRepository.save(n);
            return "Bill Payment Successful";
        }
    }
}
