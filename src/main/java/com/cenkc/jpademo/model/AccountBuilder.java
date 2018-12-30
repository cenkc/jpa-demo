package com.cenkc.jpademo.model;

import java.util.Date;

/**
 * AccountBuilder - Builder pattern implementation
 *
 * created by cenkc on 12/31/2018
 */
public class AccountBuilder {

    /**
     * Account to be created
     */
    private Account account = new Account();

    /**
     * @param username
     * @return AccountBuilder Account with username info
     */
    public AccountBuilder username(String username) {
        this.account.setUsername(username);
        return this;
    }

    /**
     * @param email
     * @return AccountBuilder Account with email info
     */
    public AccountBuilder email(String email) {
        this.account.setEmail(email);
        return this;
    }

    /**
     * @param deleted
     * @return AccountBuilder Account with deleted info
     */
    public AccountBuilder deleted(boolean deleted) {
        this.account.setDeleted(deleted);
        return this;
    }

    /**
     * @param lastLogin
     * @return AccountBuilder Account with lastLogin info
     */
    public AccountBuilder lastLogin(Date lastLogin) {
        this.account.setLastLogin(lastLogin);
        return this;
    }

    /**
     * @return Account builded Account
     */
    public Account build() {
        return this.account;
    }
}
