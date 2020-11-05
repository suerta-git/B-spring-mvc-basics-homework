package com.thoughtworks.capacity.gtb.mvc.controller;

import com.thoughtworks.capacity.gtb.mvc.model.User;
import com.thoughtworks.capacity.gtb.mvc.service.UserService;
import org.hibernate.validator.constraints.Length;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;

@RestController
@Validated
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void addUser(@RequestBody @Valid User user) {
        userService.addUser(user);
    }

    @GetMapping("/login")
    public User login(
            @RequestParam
            @Pattern(regexp = "^[A-Za-z0-9_]+$", message = "Username could only be composed by letters, numbers and underscores.")
            @Length(min = 3, max = 10, message = "Username length must >= 3 and <= 10.")
                    String username,
            @RequestParam
                    String password) {
        return userService.login(username, password);
    }
}
