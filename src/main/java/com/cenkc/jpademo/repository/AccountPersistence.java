package com.cenkc.jpademo.repository;

import com.cenkc.jpademo.model.Account;

import java.util.List;

/**
 * created by cenkc on 12/31/2018
 */
public interface AccountPersistence {

    void save(Account account);

    Account findById (long id);

    Account findByName (String name);

    int delete(Account account);

    List<Account> findAll();
}
