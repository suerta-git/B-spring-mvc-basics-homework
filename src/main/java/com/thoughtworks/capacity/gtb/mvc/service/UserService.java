package com.thoughtworks.capacity.gtb.mvc.service;

import com.thoughtworks.capacity.gtb.mvc.model.User;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserService {
    private final Map<String, User> users = new HashMap<>();

    public User findByName(String username) {
        return users.get(username);
    }

    public void addUser(User user) {
        if (users.containsKey(user.getUsername())) {
            throw new IllegalArgumentException("Username already exists.");
        }
        users.put(user.getUsername(), user);
    }

    public void init(List<User> users) {
        this.users.clear();
        users.forEach(this::addUser);
    }
}
