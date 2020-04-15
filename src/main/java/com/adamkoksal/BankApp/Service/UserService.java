package com.adamkoksal.BankApp.Service;

import com.adamkoksal.BankApp.Entity.Account;
import com.adamkoksal.BankApp.Entity.Token;
import com.adamkoksal.BankApp.Entity.User;
import com.adamkoksal.BankApp.Repository.AccountRepository;
import com.adamkoksal.BankApp.Repository.UserRepository;
import com.fasterxml.jackson.databind.util.JSONPObject;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;
import javax.swing.text.html.Option;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountRepository accountRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    private String SECRET_KEY = "oeRaYY7Wo24sDqKSX3IM9ASGmdGPmkTd9jo1QTy4b7P9Ze5_9hKolVX8xNrQDcNRfVEdTZNOuOyqEGhXEbdJI-ZQ19k_o9MI0y3eZN2lp9jow55FfXMiINEdt1XR85VipRLSOkT6kSpzs2x-jbLDiz9iFVzkd81YKxMgPA7VfZeQUm4n-mOmnWMaVX30zGFU4L3oPBctYKkl4dYfqYWqRNfrgPJVi5DGFjywgxx0ASEiJHtV72paI3fDR2XwlSkyhhmY-ICjCRmsJN4fX1pdoL8a18-aQrvyu4j0Os6dVPYIoPvvY0SAZtWYKHfM15g7A3HD4cVREf9cUsprCRK93w";




    public String addNewUser(User user) {
        User n = new User();
        n.setUsername(user.getUsername());
        n.setPassword(encryptPassword(user.getPassword()));
        n.setAddress(user.getAddress());
        n.setSecurityQuestion(user.getSecurityQuestion());
        n.setSecurityQuestionAnswer(user.getSecurityQuestionAnswer());
        userRepository.save(n);

        Account a1 = new Account();
        a1.setUser(n);
        a1.setName("Checking");
        a1.setBalance(0);
        accountRepository.save(a1);

        Account a2 = new Account();
        a2.setUser(n);
        a2.setName("Savings");
        a2.setBalance(0);
        accountRepository.save(a2);

        return "Saved and 2 new accounts created.";
    }

    public Iterable<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUser(int userId) {
        return userRepository.findById(userId);
    }

    public String updateUserUsername(int userId, User user) {
        userRepository.findById(userId)
                .map(u -> {
                    u.setUsername(user.getUsername());
                    return userRepository.save(u);
                });
        return "User Username Update Successful";
    }

    public String updateUserPassword(int userId, User user) {
        userRepository.findById(userId)
                .map(u -> {
                    u.setPassword(encryptPassword(user.getPassword()));
                    return userRepository.save(u);
                });
        return "User Password Update Successful";
    }

    public String updateUserAddress(int userId, User user) {
        userRepository.findById(userId)
                .map(u -> {
                    u.setAddress(user.getAddress());
                    return userRepository.save(u);
                });
        return "User Address Update Successful";
    }

    public String updateUserSecurityQuestion(int userId, User user) {
        userRepository.findById(userId)
                .map(u -> {
                    u.setSecurityQuestion(user.getSecurityQuestion());
                    u.setSecurityQuestionAnswer(user.getSecurityQuestionAnswer());
                    return userRepository.save(u);
                });
        return "User Security Question Update Successful";
    }

    public List<User> getUserByUsername(String username) {
        return userRepository.getUserByUsername(username);
    }

    public ResponseEntity<Object> login(User user) {
        if (isAuthorised(user)) {
            String token = buildJwt(user);
            int id = findId(user.getUsername());

            Map<Object, Object> model = new HashMap<>();
            model.put("token", token);
            model.put("id", id);
            return new ResponseEntity<Object>(model, HttpStatus.OK);
        } return new ResponseEntity<Object>(HttpStatus.UNAUTHORIZED);
    }

    private boolean isAuthorised(User user) {

        List<User> uL = userRepository.getUserByUsername(user.getUsername());

        if (!uL.isEmpty())
            return decryptPassword(uL.get(0).getPassword()).equals(user.getPassword());
        return false;

    }

    private String buildJwt(User user) {
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(SECRET_KEY);
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

        int id = findId(user.getUsername());

        JwtBuilder builder = Jwts.builder()
                .setId(Integer.toString(id)).signWith(signatureAlgorithm, signingKey);;

        return builder.compact();

    }

    private String encryptPassword(String password) {
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(SECRET_KEY);
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());



        JwtBuilder builder = Jwts.builder()
                .setSubject(password)
                .signWith(signatureAlgorithm, signingKey);

        return builder.compact();

    }

    private String decryptPassword(String enc) {
        Claims claims = Jwts.parser().setSigningKey(DatatypeConverter.parseBase64Binary(SECRET_KEY))
                .parseClaimsJws(enc).getBody();


        return claims.getSubject();

    }






    private Integer findId(String username) {
        List<User> list = userRepository.getUserByUsername(username);

        return list.get(0).getId();
    }

    public Integer getUserId(Token token) {
        Claims claims = Jwts.parser().setSigningKey(DatatypeConverter.parseBase64Binary(SECRET_KEY))
                .parseClaimsJws(token.toString()).getBody();

        return Integer.parseInt(claims.getId());
    }


    public ResponseEntity<Object> getPassword(int userId) {
        Optional<User> a = userRepository.findById(userId);
        if (a.isPresent()) {
            JSONObject password = new JSONObject();
            password.put("password", decryptPassword(a.get().getPassword()));
            return new ResponseEntity<Object>(password.toString(), HttpStatus.OK);
        }
        throw new IllegalArgumentException();

    }

    public String showPassword(int userId) {

        Optional<String> a = userRepository.findById(userId)
                .map(u -> {
                    return decryptPassword(u.getPassword());
                });

        return a.get();
    }

}


