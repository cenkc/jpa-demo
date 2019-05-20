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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = AccountController.class)
//@AutoConfigureMockMvc
public class AccountControllerTest {

    @Autowired
    private MockMvc mvc;

//    @Autowired
//    WebApplicationContext webApplicationContext;

    @MockBean
    private AccountService accountService;

    private ObjectMapper objectMapper = new ObjectMapper();
    private Account cenkc;
    private String loggedDateJsonString;
    private List<Account> accounts;

    @Before
    public void setUp() throws IOException {
        //MockitoAnnotations.initMocks(this);
        //this.mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).dispatchOptions(true).build();

        cenkc = new AccountBuilder().username("cenkc").email("cenkcan@cenkc.com").build();
        cenkc.setLastLogin(new Date());
        cenkc.setEncryptedPassword(DemoUtils.encrypt("12345"));
        cenkc.setSalt(new String(DemoUtils.getSalt()));
        cenkc.setId(1L);

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

        String username = "cenkc";
        String email = "cenkc@cenkc.com";
        String password = "somepass";

        Registration registration = new Registration();
        registration.setUsername(username);
        registration.setEmail(email);
        registration.setPassword(password);
        String registerRequestBody = objectMapper.writeValueAsString(registration);

        mvc.perform(
                post(uri)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(registerRequestBody))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.username").exists())
                .andExpect(jsonPath("$.username").isString())
                .andExpect(jsonPath("$.username").isNotEmpty())
                .andExpect(jsonPath("$.username").value(cenkc.getUsername()))
                .andExpect(jsonPath("$.email").exists())
                .andExpect(jsonPath("$.email").isString())
                .andExpect(jsonPath("$.email").isNotEmpty())
                .andExpect(jsonPath("$.email").value(cenkc.getEmail()))
                .andExpect(jsonPath("$.encryptedPassword").exists())
                .andExpect(jsonPath("$.encryptedPassword").isString())
                .andExpect(jsonPath("$.encryptedPassword").isNotEmpty())
                .andExpect(jsonPath("$.salt").exists())
                .andExpect(jsonPath("$.salt").isString())
                .andExpect(jsonPath("$.salt").isNotEmpty())
                .andExpect(jsonPath("$.deleted").exists())
                .andExpect(jsonPath("$.deleted").isBoolean())
                .andExpect(jsonPath("$.deleted").value(false))
                .andExpect(jsonPath("$.lastLogin").exists())
                .andExpect(jsonPath("$.lastLogin").isString())
                .andExpect(jsonPath("$.lastLogin").isNotEmpty())
        ;

        verify(accountService, times(1)).register(anyString(), anyString(), anyString());
    }

    @Test
    public void login() throws Exception {
        String uri = "/accounts/login";

        when(accountService.login(anyString(), anyString())).thenReturn(cenkc);

        Credential credential = new Credential("cenkc", "12345");
        String loginRequestBody = objectMapper.writeValueAsString(credential);

        mvc.perform(
                post(uri)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginRequestBody))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.username").exists())
                .andExpect(jsonPath("$.username").isString())
                .andExpect(jsonPath("$.username").isNotEmpty())
                .andExpect(jsonPath("$.username").value(cenkc.getUsername()))
                .andExpect(jsonPath("$.email").exists())
                .andExpect(jsonPath("$.email").isString())
                .andExpect(jsonPath("$.email").isNotEmpty())
                .andExpect(jsonPath("$.email").value(cenkc.getEmail()))
                .andExpect(jsonPath("$.encryptedPassword").exists())
                .andExpect(jsonPath("$.encryptedPassword").isString())
                .andExpect(jsonPath("$.encryptedPassword").isNotEmpty())
                .andExpect(jsonPath("$.salt").exists())
                .andExpect(jsonPath("$.salt").isString())
                .andExpect(jsonPath("$.salt").isNotEmpty())
                .andExpect(jsonPath("$.deleted").exists())
                .andExpect(jsonPath("$.deleted").isBoolean())
                .andExpect(jsonPath("$.deleted").value(false))
                .andExpect(jsonPath("$.lastLogin").exists())
                .andExpect(jsonPath("$.lastLogin").isString())
                .andExpect(jsonPath("$.lastLogin").isNotEmpty())
        ;


        verify(accountService, times(1)).login(anyString(), anyString());
    }

    @Test
    public void loggedInSince() throws Exception {
        String uri = "/accounts/loggeddate";
        mvc.perform(
                get(uri)
                        .param("date", anyString())
                        .param("username", anyString())
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$").value(false))
        ;
        //verify(accountService, times(1)).hasLoggedInSince(anyString(), any(Date.class));
    }

    @Test
    public void listAccounts() throws Exception {
        String uri = "/accounts";
        when(accountService.getAllAccounts()).thenReturn(accounts);

        mvc.perform(
                get(uri)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$.[0]").exists())
                .andExpect(jsonPath("$.[0]").isNotEmpty())
                .andExpect(jsonPath("$.[0].id").exists())
                .andExpect(jsonPath("$.[0].id").isNumber())
                .andExpect(jsonPath("$.[0].id").isNotEmpty())
                .andExpect(jsonPath("$.[0].id").value(1L))
                .andExpect(jsonPath("$.[0].username").exists())
                .andExpect(jsonPath("$.[0].username").isString())
                .andExpect(jsonPath("$.[0].username").isNotEmpty())
                .andExpect(jsonPath("$.[0].username").value(cenkc.getUsername()))
                .andExpect(jsonPath("$.[0].email").exists())
                .andExpect(jsonPath("$.[0].email").isString())
                .andExpect(jsonPath("$.[0].email").isNotEmpty())
                .andExpect(jsonPath("$.[0].email").value(cenkc.getEmail()))
                .andExpect(jsonPath("$.[0].encryptedPassword").exists())
                .andExpect(jsonPath("$.[0].encryptedPassword").isString())
                .andExpect(jsonPath("$.[0].encryptedPassword").isNotEmpty())
                .andExpect(jsonPath("$.[0].salt").exists())
                .andExpect(jsonPath("$.[0].salt").isString())
                .andExpect(jsonPath("$.[0].salt").isNotEmpty())
                .andExpect(jsonPath("$.[0].deleted").exists())
                .andExpect(jsonPath("$.[0].deleted").isBoolean())
                .andExpect(jsonPath("$.[0].deleted").value(false))
                .andExpect(jsonPath("$.[0].lastLogin").exists())
                .andExpect(jsonPath("$.[0].lastLogin").isString())
                .andExpect(jsonPath("$.[0].lastLogin").isNotEmpty())
                .andExpect(jsonPath("$.[1]").exists())
                .andExpect(jsonPath("$.[1]").isNotEmpty())
        ;

        verify(accountService, times(1)).getAllAccounts();
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
