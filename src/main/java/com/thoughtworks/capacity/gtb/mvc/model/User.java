package com.thoughtworks.capacity.gtb.mvc.model;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

import javax.validation.GroupSequence;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@Builder
@EqualsAndHashCode
@GroupSequence({First.class, User.class})
public class User {
    @NotBlank(message = "Username could not be null, empty or blank.", groups = First.class)
    @Length(min = 3, max = 10, message = "Username length must >= 3 and <= 10.")
    @Pattern(regexp = "^[A-Za-z0-9_]+$", message = "Username could only be composed by letters, numbers and underscores.")
    private String username;

    @NotBlank(message = "Password could not be empty.", groups = First.class)
    @Length(min = 5, max = 12, message = "Password length must >= 5 and <= 12.")
    private String password;

    @Email(message = "Email address must conform to the email format.")
    private String email;
}

interface First {}