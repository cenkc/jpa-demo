package com.cenkc.jpademo.controller;

import com.cenkc.jpademo.model.Account;
import com.cenkc.jpademo.model.Credential;
import com.cenkc.jpademo.model.Registration;
import com.cenkc.jpademo.service.AccountService;
import com.cenkc.jpademo.util.DemoUtils;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * Account Controller
 *
 * created by cenkc on 12/31/2018
 */
@RestController
@Api(value = "/accounts", description = "Accounts API", produces = "application/json")
@RequestMapping(value = "/accounts")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @ApiOperation(value = "Logs in the user, if user exists and passwd matched. Updates the last login field.",
            response = Account.class,
            consumes = "application/json")
    @ApiResponses(value =  {
            @ApiResponse(code = 200, message = "The User logged in", response = Account.class),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @PostMapping(value = "/login")
    public ResponseEntity<Account> login (@RequestBody Credential credential) {
        Account account = accountService.login(credential.getUsername(), credential.getPassword());
        return new ResponseEntity(account, HttpStatus.OK);
    }

    @ApiOperation(value = "Registers a new Account, if the user does not exists yet and logs in the user",
            response = Account.class,
            consumes = "application/json")
    @ApiResponses(value =  {
            @ApiResponse(code = 200, message = "Registration completed", response = Account.class),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @PostMapping(value = "/register")
    public ResponseEntity<Account> register (@RequestBody Registration registration) {
        Account account = accountService.register(registration.getUsername(), registration.getEmail(), registration.getPassword());
        return new ResponseEntity<>(account, HttpStatus.OK);
    }

    @ApiOperation(value = "Checks if a user has logged in since a provided timestamp.",
            response = Boolean.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "User has logged in since a provided timestamp (TRUE|FALSE)", response = Boolean.class),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @PostMapping(value = "/loggeddate")
    public ResponseEntity<Boolean> loggedInSince(@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                                     @RequestParam("date")LocalDate localDate,
                                                 @RequestParam("username") String name) {

        final boolean hasLoggedInSince = accountService.hasLoggedInSince(name, DemoUtils.convertLocalDateToDate(localDate));
        return new ResponseEntity<>(hasLoggedInSince, HttpStatus.OK);
    }

    @ApiOperation(value = "Deletes an Account, if it exists")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Deletion completed"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @DeleteMapping(value = "/{name}")
    public ResponseEntity delete(@PathVariable String name) {
        accountService.deleteAccount(name);
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "Lists existing accounts")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Accounts listing completed"),
            @ApiResponse(code = 204, message = "Could not find any Accounts"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @GetMapping()
    public ResponseEntity<List<Account>> listAccounts() {
        final List<Account> accounts = accountService.getAllAccounts();
        if (accounts.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(accounts, HttpStatus.OK);
    }
}
