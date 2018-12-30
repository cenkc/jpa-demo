package com.cenkc.jpademo.controller;

import com.cenkc.jpademo.model.Account;
import com.cenkc.jpademo.model.Credential;
import com.cenkc.jpademo.model.Registration;
import com.cenkc.jpademo.service.AccountService;
import com.cenkc.jpademo.util.DemoUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

/**
 * Account Controller
 *
 * created by cenkc on 12/31/2018
 */
@RestController
@RequestMapping(value = "/accounts")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @PostMapping(value = "/login")
    public ResponseEntity<Account> login (@RequestBody Credential credential) {
        Account account = accountService.login(credential.getUsername(), credential.getPassword());
        return new ResponseEntity(account, HttpStatus.OK);
    }

    @PostMapping(value = "/register")
    public ResponseEntity<Account> register (@RequestBody Registration registration) {
        Account account = accountService.register(registration.getUsername(), registration.getEmail(), registration.getPassword());
        return new ResponseEntity<>(account, HttpStatus.OK);
    }

    @PostMapping(value = "/loggeddate")
    public ResponseEntity<Boolean> loggedInSince(@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                                     @RequestParam("date")LocalDate localDate,
                                                 @RequestParam("username") String name) {

        final boolean hasLoggedInSince = accountService.hasLoggedInSince(name, DemoUtils.convertLocalDateToDate(localDate));
        return new ResponseEntity<>(hasLoggedInSince, HttpStatus.OK);
    }

    @DeleteMapping(value = "/{name}")
    public ResponseEntity delete(@PathVariable String name) {
        accountService.deleteAccount(name);
        return ResponseEntity.ok().build();
    }
}
