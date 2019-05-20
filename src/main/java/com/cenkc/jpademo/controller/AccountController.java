package com.cenkc.jpademo.controller;

import com.cenkc.jpademo.model.Account;
import com.cenkc.jpademo.model.Credential;
import com.cenkc.jpademo.model.Registration;
import com.cenkc.jpademo.service.AccountService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * Account Controller
 *
 * created by cenkc on 12/31/2018
 */
@RestController
@Api(value = "/accounts", produces = "application/json")
@RequestMapping(value = "/accounts")
public class AccountController implements AccountControllerInterface {

    @Autowired
    private AccountService accountService;

    @Override
    @PostMapping(value = "/login")
    public ResponseEntity<Account> login(@RequestBody Credential credential) {
        Account account = accountService.login(credential.getUsername(), credential.getPassword());
        return new ResponseEntity(account, HttpStatus.OK);
    }

    @Override
    @PostMapping(value = "/register")
    public ResponseEntity<Account> register(@RequestBody Registration registration) {
        Account account = accountService.register(registration.getUsername(), registration.getEmail(), registration.getPassword());
        return new ResponseEntity<>(account, HttpStatus.OK);
    }

    @Override
    @GetMapping(value = "/loggeddate")
    public ResponseEntity<Boolean> loggedInSince(@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date date,
                                                 @RequestParam("username") String name) {
        final boolean hasLoggedInSince = accountService.hasLoggedInSince(name, date);
        return new ResponseEntity<>(hasLoggedInSince, HttpStatus.OK);
    }

    @Override
    @DeleteMapping(value = "/{name}")
    public ResponseEntity delete(@PathVariable String name) {
        accountService.deleteAccount(name);
        return ResponseEntity.ok().build();
    }

    @Override
    @GetMapping()
    public ResponseEntity<List<Account>> listAccounts() {
        final List<Account> accounts = accountService.getAllAccounts();
        if (accounts.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(accounts, HttpStatus.OK);
    }
}
