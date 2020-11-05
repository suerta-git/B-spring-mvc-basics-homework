package com.thoughtworks.capacity.gtb.mvc.service;

import com.thoughtworks.capacity.gtb.mvc.exception.PasswordNotCorrectException;
import com.thoughtworks.capacity.gtb.mvc.exception.UserNotExistsException;
import com.thoughtworks.capacity.gtb.mvc.model.User;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserService {
    private final Map<String, User> users = new HashMap<>();
    private int nextId = 1;

    public User findByName(String username) {
        return users.get(username);
    }

    public void addUser(User user) {
        if (users.containsKey(user.getUsername())) {
            throw new IllegalArgumentException("Username already exists.");
        }
        user.setId(nextId++);
        users.put(user.getUsername(), user);
    }

    public void init(List<User> users) {
        this.users.clear();
        users.forEach(this::addUser);
    }

    public User login(String username, String password) {
        if (!users.containsKey(username)) {
            throw new UserNotExistsException();
        }
        final User user = users.get(username);
        if (!user.getPassword().equals(password)) {
            throw new PasswordNotCorrectException();
        }
        return user;
    }
}
