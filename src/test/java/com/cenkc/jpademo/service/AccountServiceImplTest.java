package com.cenkc.jpademo.service;

import com.cenkc.jpademo.exception.AccountServiceException;
import com.cenkc.jpademo.model.Account;
import com.cenkc.jpademo.model.AccountBuilder;
import com.cenkc.jpademo.repository.AccountPersistence;
import com.cenkc.jpademo.util.DemoUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AccountServiceImplTest {

    @Mock
    private AccountPersistence accountPersistence;

    @InjectMocks
    private AccountServiceImpl accountService;

    private Account account;
    private List<Account> accountList;

    @Before
    public void setUp() throws IOException {
        account = generateTestAccount();
        when(accountPersistence.findByName(anyString())).thenReturn(account);
        generateMultipleTestAccounts();
    }

    private void generateMultipleTestAccounts() {
        accountList = new ArrayList<>();
        accountList.add(account);
        AccountBuilder ab = new AccountBuilder();
        accountList.add(ab.username("cenkcan").email("cenkcan@mail.net").deleted(false).build());
    }

    private Account generateTestAccount() throws IOException {
        Calendar cal = Calendar.getInstance();
        cal.set(2019, 0, 1);
        Date lastLoginTime = cal.getTime();

        String pass = "01234567890123";
        byte[] encryptedPass = DemoUtils.encrypt(pass);

        Account acc = new Account();
        acc.setId(1L);
        acc.setUsername("cenkc");
        acc.setEmail("cenk.canarslan@gmail.com");
        acc.setDeleted(false);
        acc.setLastLogin(lastLoginTime);
        acc.setEncryptedPassword(encryptedPass);
        return acc;
    }

    @Test(expected = AccountServiceException.class)
    public void shouldThrowExceptionInLogin_whenUserIsNull() {
        when(accountPersistence.findByName(anyString())).thenReturn(null);
        Account login = accountService.login("hede", "hodo");
    }

    @Test(expected = AccountServiceException.class)
    public void shouldThrowExceptionInLogin_whenPasswordsNotMatched() {
        Account login = accountService.login("hede", "98765432109876");
    }

    @Test(expected = AccountServiceException.class)
    public void shouldThrowExceptionInLogin_whenAccountDeletedIsTrue() {
        account.setDeleted(true);
        Account login = accountService.login("hede", "01234567890123");
    }

    @Test
    public void shouldLastLoginDateSet_whenAccountLoggedIn() {
        Date storedLastLogin = account.getLastLogin();
        Account loggedIn = accountService.login("hede", "01234567890123");
        Assert.assertNotEquals(storedLastLogin, loggedIn.getLastLogin());
    }

    @Test
    public void login() {
        Account loggedIn = accountService.login("hede", "01234567890123");
        Assert.assertNotNull(loggedIn);
    }

    @Test(expected = AccountServiceException.class)
    public void shouldThrowExceptionInRegister_whenAccountWithSameUserFound() throws IOException {
        Account otherAcc = generateTestAccount();
        Account accountToBeRegistered = accountService.register(otherAcc.getUsername(), "hede@hodo.net", "somePassPhrase");
    }

    @Test
    public void register() throws IOException {
        AccountBuilder ab = new AccountBuilder();
        Account cenkcan = ab.username("cenkcan").email("cenkcan@mail.net").deleted(false).build();
        cenkcan.setEncryptedPassword(DemoUtils.encrypt("somePassPhrase"));

        when(accountPersistence.findByName(anyString())).thenReturn(null).thenReturn(cenkcan);

        accountService.register("cenkcan", "cenkcan@mail.net", "somePassPhrase");

        verify(accountPersistence, times(1)).save(any());
    }

    @Test(expected = AccountServiceException.class)
    public void shouldThrowExceptionInDeleteAccount_whenUserIsNull() {
        when(accountPersistence.findByName(anyString())).thenReturn(null);
        accountService.deleteAccount("cenkcan");
    }

    @Test
    public void deleteAccount() {
        accountService.deleteAccount("cenkc");
        verify(accountPersistence, times(1)).delete(any());
    }

    @Test(expected = AccountServiceException.class)
    public void shouldThrowExceptionInHasLoggedInSince_whenUserIsNull() {
        when(accountPersistence.findByName(anyString())).thenReturn(null);
        accountService.hasLoggedInSince("cenkcan", new Date());
    }

    @Test
    public void hasLoggedInSince() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2018, 0, 1);
        Assert.assertTrue(accountService.hasLoggedInSince("cenkc", calendar.getTime()));
    }

    @Test(expected = AccountServiceException.class)
    public void shouldThrowExceptionInGetAllAccounts_whenThereAreNoUsers() {
        when(accountPersistence.findAll()).thenReturn(null);
        accountService.getAllAccounts();
    }

    @Test
    public void getAllAccounts() {
        when(accountPersistence.findAll()).thenReturn(accountList);
        List<Account> accounts = accountService.getAllAccounts();
        verify(accountPersistence, times(1)).findAll();
        Assert.assertNotNull(accounts);
        Assert.assertEquals(2, accounts.size());
    }
}