package com.innovat.zuulgateway.security.client;

import java.util.List;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class User {

    private String username;
    private String password;
    private List<String> authorities;
    private boolean enabled;

    public User(
            String username,
            String password, 
            List<String> authorities,
            boolean enabled
    ) {
        this.username = username;
        this.password = password;
        this.authorities = authorities;
        this.enabled = enabled;
    }
}

