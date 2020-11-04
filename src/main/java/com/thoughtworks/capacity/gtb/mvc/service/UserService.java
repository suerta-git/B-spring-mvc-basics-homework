package com.thoughtworks.capacity.gtb.mvc.service;

import com.thoughtworks.capacity.gtb.mvc.model.User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserService {
    private final Map<String, User> users = new HashMap<>();

    public UserService() {
        final User defaultUser = User.builder()
                .username("default")
                .password("password")
                .email("default@thoughtworks.com").build();
        init(Collections.singletonList(defaultUser));
    }

    public User findByName(String username) {
        return users.get(username);
    }

    public void addUser(User user) {
        users.put(user.getUsername(), user);
    }

    private void init(List<User> users) {
        this.users.clear();
        users.forEach(this::addUser);
    }
}
