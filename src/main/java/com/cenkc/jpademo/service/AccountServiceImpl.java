package com.cenkc.jpademo.service;

import com.cenkc.jpademo.exception.AccountServiceException;
import com.cenkc.jpademo.model.Account;
import com.cenkc.jpademo.model.AccountBuilder;
import com.cenkc.jpademo.repository.AccountPersistence;
import com.cenkc.jpademo.util.DemoUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.Date;

/**
 * Account Service Implementation
 *
 * created by cenkc on 12/31/2018
 */
@Service
@Transactional
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountPersistence accountPersistence;


    /**
     * logs in an Account with given username and password
     *
     * @param username the User's name
     * @param password the clear text passwd
     * @return Account the logged in Account
     * @throws AccountServiceException
     */
    @Override
    public Account login(String username, String password) {
        Account account = accountPersistence.findByName(username);
        if (account == null) {
            throw new AccountServiceException("Can't find user:" + username);
        }
        String storedPassword = DemoUtils.decrypt(new String(account.getEncryptedPassword()));
        if (!storedPassword.equals(password)) {
            throw new AccountServiceException("Password not matched for user:" + username);
        }
        if (account.isDeleted()) {
            throw new AccountServiceException("Account has been deleted. Can't login user:" + username);
        }
        account.setLastLogin(new Date());
        return account;
    }

    /**
     * Registers a new Account, if the user does not exists yet and logs in the user
     *
     * @param username the User's name
     * @param email    the User's email
     * @param password the User's clear text pass
     * @return the newly registered Account
     * @throws AccountServiceException
     */
    @Override
    public Account register(String username, String email, String password) {
        try {
            Account account = accountPersistence.findByName(username);
            if (account != null) {
                throw new AccountServiceException("An account with same name already exists! Please choose a different username.");
            }
            AccountBuilder accountBuilder = new AccountBuilder();
            account = accountBuilder.username(username).email(email).build();
            account.setEncryptedPassword(DemoUtils.encrypt(password));
            account.setSalt(new String(DemoUtils.getSalt()));
            accountPersistence.save(account);
            return login(account.getUsername(), password);
        } catch (IOException e) {
            throw new AccountServiceException("IOException occurred while encrypting password", e);
        }
    }

    /**
     * Deletes an Account, if it exists
     *
     * @param username the User's name
     * @throws AccountServiceException
     */
    @Override
    public void deleteAccount(String username) {
        Account account = accountPersistence.findByName(username);
        if (account == null) {
            throw new AccountServiceException("Account not found for Username:" + username);
        }
        accountPersistence.delete(account);
    }

    /**
     * Checks if a user has logged in since a provided timestamp.
     *
     * @param username the User's name
     * @param date     provided timestamp
     * @return true if the user has logged in since the provided timestamp, else false.
     * @throws AccountServiceException
     */
    @Override
    public boolean hasLoggedInSince(String username, Date date) {
        Account account = accountPersistence.findByName(username);
        if (account == null) {
            throw new AccountServiceException("Account not found for Username:" + username);
        }
        Date lastLoggedInDate = account.getLastLogin();
        return lastLoggedInDate.after(date);
    }
}
