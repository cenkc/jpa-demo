package com.cenkc.jpademo.repository;

import com.cenkc.jpademo.model.Account;

/**
 * created by cenkc on 12/31/2018
 */
public interface AccountPersistence {

    void save(Account account);

    Account findById (long id);

    Account findByName (String name);

    void delete(Account account);
}
