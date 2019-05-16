package com.cenkc.jpademo.controller;

import com.cenkc.jpademo.model.Account;
import com.cenkc.jpademo.model.Credential;
import com.cenkc.jpademo.model.Registration;
import io.swagger.annotations.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

public interface AccountControllerInterface {
    @ApiOperation(value = "Logs in the user, if user exists and passwd matched. Updates the last login field.",
            response = Account.class,
            consumes = "application/json")
    @ApiResponses(value =  {
            @ApiResponse(code = 200, message = "The User logged in", response = Account.class),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    ResponseEntity<Account> login (@RequestBody Credential credential);

    @ApiOperation(value = "Registers a new Account, if the user does not exists yet and logs in the user",
            response = Account.class,
            consumes = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "registration", value = "Registration object itself", required = true, dataType = "Registration", paramType = "body")
    })
    @ApiResponses(value =  {
            @ApiResponse(code = 200, message = "Registration completed", response = Account.class),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    ResponseEntity<Account> register (@RequestBody Registration registration);

    @ApiOperation(value = "Checks if a user has logged in since a provided timestamp.",
            response = Boolean.class,
            consumes = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "date", value = "User has logged in since", required = true, dataType = "string", paramType = "body"),
            @ApiImplicitParam(name = "username", value = "User's name", required = true, dataType = "string", paramType = "body")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "User has logged in since a provided timestamp (TRUE|FALSE)", response = Boolean.class),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    ResponseEntity<Boolean> loggedInSince(@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate localDate,
                                          @RequestParam("username") String name);

    @ApiOperation(value = "Deletes an Account, if it exists")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "User's name", required = true, dataType = "String", paramType = "query")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Deletion completed"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    ResponseEntity delete(@PathVariable String name);

    @ApiOperation(value = "Lists existing accounts")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Accounts listing completed"),
            @ApiResponse(code = 204, message = "Could not find any Accounts"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    ResponseEntity<List<Account>> listAccounts();
}
