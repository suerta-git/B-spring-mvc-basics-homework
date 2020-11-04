package com.thoughtworks.capacity.gtb.mvc.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.capacity.gtb.mvc.model.User;
import com.thoughtworks.capacity.gtb.mvc.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserService userService;

    private final User defaultUser = User.builder()
                .username("default")
                .password("password")
                .email("default@thoughtworks.com").build();

    @BeforeEach
    private void init() {
        userService.init(Collections.singletonList(defaultUser));
    }

    @Test
    public void should_register_user_given_all_fields() throws Exception {
        final User newUser = User.builder()
                .username("new")
                .password("abc123")
                .email("abc@xyz.com").build();
        final String json = objectMapper.writeValueAsString(newUser);

        mockMvc.perform(post("/register").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isCreated());

        assertEquals(newUser, userService.findByName(newUser.getUsername()));
    }

    @Test
    public void should_register_user_given_user_without_email() throws Exception {
        final User newUser = User.builder()
                .username("new")
                .password("abc123").build();
        final String json = objectMapper.writeValueAsString(newUser);

        mockMvc.perform(post("/register").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isCreated());

        assertEquals(newUser, userService.findByName(newUser.getUsername()));
    }

    @Test
    public void should_reject_registering_given_null_empty_or_blank_username() throws Exception {
        final User nullUsernameUser = User.builder().password("abc123").build();
        final User emptyUsernameUser = User.builder().username("").password("abc123").build();
        final User blankUsernameUser = User.builder().username("      ").password("abc123").build();

        final String nullUsernameJson = objectMapper.writeValueAsString(nullUsernameUser);
        final String emptyUsernameJson = objectMapper.writeValueAsString(emptyUsernameUser);
        final String blankUsernameJson = objectMapper.writeValueAsString(blankUsernameUser);

        mockMvc.perform(post("/register").contentType(MediaType.APPLICATION_JSON).content(nullUsernameJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Username could not be null, empty or blank."));

        mockMvc.perform(post("/register").contentType(MediaType.APPLICATION_JSON).content(emptyUsernameJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Username could not be null, empty or blank."));

        mockMvc.perform(post("/register").contentType(MediaType.APPLICATION_JSON).content(blankUsernameJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Username could not be null, empty or blank."));
    }

    @Test
    public void should_reject_registering_given_username_too_short_or_long() throws Exception {
        final User shortUsernameUser = User.builder().username("12").password("abc123").build();
        final User longUsernameUser = User.builder().username("1234567890abc").password("abc123").build();

        final String shortUsernameJson = objectMapper.writeValueAsString(shortUsernameUser);
        final String longUsernameJson = objectMapper.writeValueAsString(longUsernameUser);

        mockMvc.perform(post("/register").contentType(MediaType.APPLICATION_JSON).content(shortUsernameJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Username length must >= 3 and <= 10."));

        mockMvc.perform(post("/register").contentType(MediaType.APPLICATION_JSON).content(longUsernameJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Username length must >= 3 and <= 10."));
    }

}