package com.cenkc.jpademo.service;

import com.cenkc.jpademo.model.Account;

import java.util.Date;
import java.util.List;

/**
 * Service Interface for Account Management
 *
 * created by cenkc on 12/31/2018
 */
public interface AccountService {

    /**
     * Logs in the user, if user exists and passwd matched.
     * Updates the last login field.
     *
     * @param username the User's name
     * @param password the clear text passwd
     * @return Account that logged in
     * @throws com.cenkc.jpademo.exception.AccountServiceException if any errors occur
     */
    Account login(String username, String password);

    /**
     * Registers a new Account, if the user does not exists yet and logs in the user
     *
     * @param username the User's name
     * @param email the User's email
     * @param password the User's clear text pass
     * @return the newly registered Account
     * @throws com.cenkc.jpademo.exception.AccountServiceException if any errors occur
     */
    Account register(String username, String email, String password);

    /**
     * Deletes an Account, if it exists
     *
     * @param username the User's name
     * @throws com.cenkc.jpademo.exception.AccountServiceException if any errors occur
     */
    int deleteAccount(String username);

    /**
     * Checks if a user has logged in since a provided timestamp.
     *
     * @param username the User's name
     * @param date provided timestamp
     * @return true if the user has logged in since the provided timestamp, else false.
     * @throws com.cenkc.jpademo.exception.AccountServiceException if any errors occur
     */
    boolean hasLoggedInSince(String username, Date date);

    /**
     * @return list of accounts
     */
    List<Account> getAllAccounts();
}
