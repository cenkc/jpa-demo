package com.cenkc.jpademo.repository;

import com.cenkc.jpademo.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * created by cenkc on 12/31/2018
 */
@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    Account findByUsername(String username);

    @Modifying
    @Query("UPDATE Account SET deleted=true WHERE id = :accountId")
    void deleteById(@Param("accountId") Long id);

}
