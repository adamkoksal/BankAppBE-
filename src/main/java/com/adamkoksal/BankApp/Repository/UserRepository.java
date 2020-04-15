package com.adamkoksal.BankApp.Repository;

import com.adamkoksal.BankApp.Entity.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Integer> {

    List<User> getUserByUsername(String username);
}
