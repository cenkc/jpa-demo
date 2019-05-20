package com.cenkc.jpademo.repository;

import com.cenkc.jpademo.model.Account;
import com.cenkc.jpademo.model.AccountBuilder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class AccountRepositoryTest {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TestEntityManager entityManager;

    private Account account;

    @Before
    public void setUp() {
        account = new AccountBuilder()
                .username("cenk")
                .email("cenk@cenkc.com")
                .deleted(false)
                .lastLogin(new Date())
                .build();
    }

    @Test
    public void whenFindByUsername_shouldReturnAccount() {
        accountRepository.save(account);
        Account found = accountRepository.findByUsername("cenk");
        assertThat(found).isNotNull();
        assertThat(found.getUsername()).isEqualTo(account.getUsername());
        assertThat(found).hasFieldOrPropertyWithValue("username", "cenk");
        assertThat(found).hasFieldOrPropertyWithValue("email", "cenk@cenkc.com");
        assertThat(found).hasFieldOrPropertyWithValue("deleted", false);
    }

    @Test
    public void whenFindByUsername_shouldReturnAccount_using_EntityManager() {
        Account cenkcan = new AccountBuilder()
                .username("cenkcan")
                .email("cenkcan@cenkc.com")
                .deleted(false)
                .lastLogin(new Date())
                .build();

        entityManager.persist(cenkcan);
        entityManager.flush();
        Account found = accountRepository.findByUsername("cenkcan");

        assertThat(found).isNotNull();
        assertThat(found.getUsername()).isEqualTo(cenkcan.getUsername());
        assertThat(found).hasFieldOrPropertyWithValue("username", cenkcan.getUsername());
        assertThat(found).hasFieldOrPropertyWithValue("email", cenkcan.getEmail());
        assertThat(found).hasFieldOrPropertyWithValue("deleted", cenkcan.isDeleted());
    }

    @Test
    @Transactional
    public void whenDeleteByAccountId_shouldChangeIsDeletedStatus() {
        entityManager.persist(account);
        entityManager.flush();
        Account found = accountRepository.findByUsername("cenk");

        int deleteStatus = accountRepository.deleteByAccountId(found.getId());
        assertThat(deleteStatus).isEqualTo(1);
    }
}