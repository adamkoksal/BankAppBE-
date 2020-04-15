package com.adamkoksal.BankApp.Controller;

import com.adamkoksal.BankApp.Entity.*;
import com.adamkoksal.BankApp.Service.AccountService;
import com.adamkoksal.BankApp.Service.TransactionService;
import com.adamkoksal.BankApp.Service.UserService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:4200")
@Controller
@RequestMapping
public class MainController {

    @Autowired
    private UserService userService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private TransactionService transactionService;


    @GetMapping(path="/users")
    public @ResponseBody Iterable<User> getAllUsers() {
       return userService.getAllUsers();
    }

    @GetMapping(path="/users/{userId}")
    public @ResponseBody Optional<User> getUser(@PathVariable int userId) {
        return userService.getUser(userId);
    }

    @GetMapping(path="/users/username={username}")
    public @ResponseBody List<User> getUserByUsername(@PathVariable String username) {
        return userService.getUserByUsername(username);
    }

    @PutMapping(path = "/users/username/{userId}")
    public @ResponseBody String updateUserUsername(@PathVariable int userId, @RequestBody User user) {
        return userService.updateUserUsername(userId, user);
    }

    @PutMapping(path = "/users/password/{userId}")
    public @ResponseBody String updateUserPassword(@PathVariable int userId, @RequestBody User user) {
        return userService.updateUserPassword(userId, user);
    }

    @PutMapping(path = "/users/address/{userId}")
    public @ResponseBody String updateUserAddress(@PathVariable int userId, @RequestBody User user) {
        return userService.updateUserAddress(userId, user);
    }

    @PutMapping(path = "/users/security-question/{userId}")
    public @ResponseBody String updateUserSecurityQuestion(@PathVariable int userId, @RequestBody User user) {
        return userService.updateUserSecurityQuestion(userId, user);
    }

    @PostMapping(path="/account/{userId}")
    public @ResponseBody String createNewAccount(@RequestBody Account account, @PathVariable int userId) {
        return accountService.createNewAccount(account, userId);
    }

    @GetMapping(path="/account/{accountId}")
    public @ResponseBody Optional<Account> getAccount(@PathVariable int accountId) {
        return accountService.getAccount(accountId);
    }

    @PutMapping(path="/account/{accountId}")
    public @ResponseBody String updateAccount(@PathVariable int accountId, @RequestBody Account account) {
        return accountService.updateAccount(account, accountId);
    }

    @DeleteMapping(path = "/account/{accountId}")
    public @ResponseBody String deleteAccount(@PathVariable int accountId) {
        return accountService.deleteAccount(accountId);
    }

    @GetMapping(path="/accounts/{userId}")
    public @ResponseBody Iterable<Account> getAccountsByUser(@PathVariable int userId) {
        return accountService.getAccountsByUser(userId);
    }

    @PostMapping(path = "/deposit/{receiverId}/{amount}")
    public @ResponseBody String deposit(@PathVariable int receiverId, @PathVariable int amount) {
        return transactionService.deposit(receiverId, amount);
    }

    @PostMapping(path = "/withdrawal/{initiatorId}/{amount}")
    public @ResponseBody String withdrawal(@PathVariable int initiatorId, @PathVariable int amount) throws Exception {
        return transactionService.withdrawal(initiatorId, amount);
    }

    @PostMapping(path = "/transfer/{initiatorId}/{receiverId}/{amount}")
    public @ResponseBody String transfer(@PathVariable int initiatorId, @PathVariable int receiverId, @PathVariable int amount ) throws Exception {
        return transactionService.transfer(initiatorId, receiverId, amount);
    }

    @GetMapping(path = "/transactions/{accountId}")
    public @ResponseBody Collection<Transaction>  getTransactionsByAccount(@PathVariable int accountId) {
        return transactionService.getTransactionsByAccount(accountId);
    }

    //    LOGIN
    @PostMapping(path="/login")
    public @ResponseBody ResponseEntity<Object> login(@RequestBody User user) {
        return userService.login(user);
    }

    //    SIGNUP

    @PostMapping(path="/signup")
    public @ResponseBody String signup(@RequestBody User user) {
        return userService.addNewUser(user);
    }


    // Return UserId
    @PostMapping(path="/user/get-id")
    public @ResponseBody Integer getUserId(@RequestBody Token token) {
        return userService.getUserId(token);
    }

    @GetMapping(path="/password/{userId}")
    public @ResponseBody ResponseEntity<Object> getPassword(@PathVariable int userId) {
        return userService.getPassword(userId);
    }

    @GetMapping(path="/show-password/{userId}")
    public @ResponseBody String showPassword(@PathVariable int userId) {
        return userService.showPassword(userId);
    }

//    BILL PAY

    @PostMapping(path="pay-bill/{initiatorId}/{receiverId}/{amount}")
    public @ResponseBody String payBill(@PathVariable int initiatorId, @PathVariable int receiverId, @PathVariable int amount) throws Exception {
        return transactionService.payBill(initiatorId, receiverId, amount);
    }

//    @PostMapping(path="create/bill-company")
//    public @ResponseBody String createCompany(@RequestBody BillCompany company) {
//        return billCompanyService.createCompany(company);
//    }
}
