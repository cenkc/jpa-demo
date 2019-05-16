package com.cenkc.jpademo.controller;

import com.cenkc.jpademo.model.Account;
import com.cenkc.jpademo.model.AccountBuilder;
import com.cenkc.jpademo.model.Credential;
import com.cenkc.jpademo.model.Registration;
import com.cenkc.jpademo.service.AccountService;
import com.cenkc.jpademo.util.DemoUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = AccountController.class)
@AutoConfigureMockMvc
public class AccountControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    WebApplicationContext webApplicationContext;

    @MockBean
    private AccountService accountService;

    private ObjectMapper objectMapper = new ObjectMapper();
    private Account cenkc;
    private String loggedDateJsonString;
    private List<Account> accounts;

    @Before
    public void setUp() throws IOException {
        MockitoAnnotations.initMocks(this);
        this.mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).dispatchOptions(true).build();

        cenkc = new AccountBuilder().username("cenkc").email("cenkcan@cenkc.com").build();
        cenkc.setLastLogin(new Date());
        cenkc.setEncryptedPassword(DemoUtils.encrypt("12345"));
        cenkc.setSalt(new String(DemoUtils.getSalt()));

        ObjectNode loggedDateNode = objectMapper.createObjectNode();
        loggedDateNode.put("date", LocalDate.now().toString());
        loggedDateNode.put("username", "cenkc");
        loggedDateJsonString = objectMapper.writeValueAsString(loggedDateNode);

        accounts = new ArrayList<>();
        accounts.add(cenkc);
        accounts.add(new AccountBuilder().username("admin").email("admin@cenkc.com").deleted(false).build());
    }

    @Test
    public void register() throws Exception {
        String uri = "/accounts/register";

        when(accountService.register(anyString(), anyString(), anyString())).thenReturn(cenkc);

        Registration registration = new Registration();
        registration.setUsername("cenkc");
        registration.setEmail("cenkc@cenkc.com");
        registration.setPassword("somepass");
        String registerRequestBody = objectMapper.writeValueAsString(registration);


        MvcResult mvcResult = mvc.perform(
                post(uri)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(registerRequestBody))
                .andReturn();
        verify(accountService, times(1)).register(anyString(), anyString(), anyString());
        int status = mvcResult.getResponse().getStatus();
        assertEquals(HttpStatus.OK.value(), status);
    }

    @Test
    public void login() throws Exception {
        String uri = "/accounts/login";

        when(accountService.login(anyString(), anyString())).thenReturn(cenkc);

        Credential credential = new Credential("cenkc", "12345");
        String loginRequestBody = objectMapper.writeValueAsString(credential);

        MvcResult mvcResult = mvc.perform(
                post(uri)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginRequestBody)
        ).andReturn();
        verify(accountService, times(1)).login(anyString(), anyString());
        int status = mvcResult.getResponse().getStatus();
        assertEquals(HttpStatus.OK.value(), status);
    }

    @Test
    public void loggedInSince() throws Exception {
/*
        String uri = "/accounts/loggeddate";
        when(accountService.hasLoggedInSince(anyString(), any())).thenReturn(true);

        MvcResult mvcResult = mvc.perform(
                post(uri)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loggedDateJsonString)
        ).andReturn();
//        System.out.println("---->" + mvcResult.getResponse());
//        verify(accountService, times(1)).hasLoggedInSince(anyString(), any());
        int status = mvcResult.getResponse().getStatus();
        assertEquals(HttpStatus.OK.value(), status);
*/
    }

    @Test
    public void listAccounts() throws Exception {
        String uri = "/accounts";
        when(accountService.getAllAccounts()).thenReturn(accounts);

        MvcResult mvcResult = mvc.perform(
                get(uri)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn();
        verify(accountService, times(1)).getAllAccounts();
        int status = mvcResult.getResponse().getStatus();
        assertEquals(HttpStatus.OK.value(), status);
    }

    @Test
    public void delete() throws Exception {
        String urlTemplate = "/accounts/{name}";
        doNothing().when(accountService).deleteAccount(anyString());
        MvcResult mvcResult = mvc.perform(
                MockMvcRequestBuilders.delete(urlTemplate, "cenkc")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn();
        verify(accountService, times(1)).deleteAccount(anyString());
        int status = mvcResult.getResponse().getStatus();
        assertEquals(HttpStatus.OK.value(), status);
    }
}
