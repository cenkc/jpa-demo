package com.cenkc.jpademo.repository;

import com.cenkc.jpademo.model.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Account persistence implementation
 *
 * created by cenkc on 12/31/2018
 */
@Service
public class AccountPersistenceImpl implements AccountPersistence {

    @Autowired
    private AccountRepository accountRepository;

    /**
     * Persist account
     *
     * @param account the Account to be persisted
     */
    @Override
    public void save(Account account) {
        accountRepository.save(account);
    }

    /**
     * finds Account by it's id
     *
     * @param id the User's id
     * @return the Account
     */
    @Override
    public Account findById(long id) {
        return accountRepository.getOne(id);
    }

    /**
     * finds Account by it's username
     *
     * @param name the User's username
     * @return the Account
     */
    @Override
    public Account findByName(String name) {
        return accountRepository.findByUsername(name);
    }

    /**
     * deletes an Account
     *
     * @param account Account to be deleted
     */
    @Override
    public int delete(Account account) {
        return accountRepository.deleteByAccountId(account.getId());
    }

    /**
     * @return list of account
     */
    @Override
    public List<Account> findAll() {
        return accountRepository.findAll();
    }
}
