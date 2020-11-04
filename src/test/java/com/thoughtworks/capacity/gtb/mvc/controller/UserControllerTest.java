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

        assertEquals(newUser.getUsername(), userService.findByName(newUser.getUsername()).getUsername());
        assertEquals(newUser.getPassword(), userService.findByName(newUser.getUsername()).getPassword());
        assertEquals(newUser.getEmail(), userService.findByName(newUser.getUsername()).getEmail());
    }

    @Test
    public void should_register_user_given_user_without_email() throws Exception {
        final User newUser = User.builder()
                .username("new")
                .password("abc123").build();
        final String json = objectMapper.writeValueAsString(newUser);

        mockMvc.perform(post("/register").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isCreated());

        assertEquals(newUser.getUsername(), userService.findByName(newUser.getUsername()).getUsername());
        assertEquals(newUser.getPassword(), userService.findByName(newUser.getUsername()).getPassword());
        assertEquals(newUser.getEmail(), userService.findByName(newUser.getUsername()).getEmail());
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

    @Test
    public void should_reject_registering_given_username_with_unacceptable_characters() throws Exception {
        final User invalidUsernameUser = User.builder().username("123-@无效").password("abc123").build();

        final String invalidUsernameJson = objectMapper.writeValueAsString(invalidUsernameUser);

        mockMvc.perform(post("/register").contentType(MediaType.APPLICATION_JSON).content(invalidUsernameJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Username could only be composed by letters, numbers and underscores."));
    }

    @Test
    public void should_reject_registering_given_existing_username() throws Exception {
        final User existingUsernameUser = User.builder().username("default").password("abc123").build();

        final String existingUsernameJson = objectMapper.writeValueAsString(existingUsernameUser);

        mockMvc.perform(post("/register").contentType(MediaType.APPLICATION_JSON).content(existingUsernameJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Username already exists."));
    }

    @Test
    public void should_reject_registering_given_null_empty_or_blank_password() throws Exception {
        final User nullPasswordUser = User.builder().username("new").build();
        final User emptyPasswordUser = User.builder().username("new").password("").build();
        final User blankPasswordUser = User.builder().username("new").password("      ").build();

        final String nullPasswordJson = objectMapper.writeValueAsString(nullPasswordUser);
        final String emptyPasswordJson = objectMapper.writeValueAsString(emptyPasswordUser);
        final String blankPasswordJson = objectMapper.writeValueAsString(blankPasswordUser);

        mockMvc.perform(post("/register").contentType(MediaType.APPLICATION_JSON).content(nullPasswordJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Password could not be null, empty or blank."));

        mockMvc.perform(post("/register").contentType(MediaType.APPLICATION_JSON).content(emptyPasswordJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Password could not be null, empty or blank."));

        mockMvc.perform(post("/register").contentType(MediaType.APPLICATION_JSON).content(blankPasswordJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Password could not be null, empty or blank."));
    }

    @Test
    public void should_reject_registering_given_password_too_short_or_long() throws Exception {
        final User shortPasswordUser = User.builder().username("new1").password("123").build();
        final User longPasswordUser = User.builder().username("new2").password("1234567890abc--").build();

        final String shortPasswordJson = objectMapper.writeValueAsString(shortPasswordUser);
        final String longPasswordJson = objectMapper.writeValueAsString(longPasswordUser);

        mockMvc.perform(post("/register").contentType(MediaType.APPLICATION_JSON).content(shortPasswordJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Password length must >= 5 and <= 12."));

        mockMvc.perform(post("/register").contentType(MediaType.APPLICATION_JSON).content(longPasswordJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Password length must >= 5 and <= 12."));
    }

    @Test
    public void should_reject_registering_given_invalid_email() throws Exception {
        final User invalidEmailUser = User.builder().username("new1").password("123456").email("invalid_email").build();

        final String invalidEmailJson = objectMapper.writeValueAsString(invalidEmailUser);

        mockMvc.perform(post("/register").contentType(MediaType.APPLICATION_JSON).content(invalidEmailJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Email address must conform to the email format."));
    }
}