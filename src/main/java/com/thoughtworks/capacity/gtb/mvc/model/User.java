package com.thoughtworks.capacity.gtb.mvc.model;

import lombok.*;

@Data
@Builder
@EqualsAndHashCode
public class User {
    private String username;
    private String password;
    private String email;
}
