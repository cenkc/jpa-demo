package com.cenkc.jpademo.repository;

import com.cenkc.jpademo.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * created by cenkc on 12/31/2018
 */
@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    Account findByUsername(String username);

    @Transactional
    @Modifying
    @Query("UPDATE Account SET deleted=true WHERE id = :accountId")
    int deleteByAccountId(@Param("accountId") Long id);

}
