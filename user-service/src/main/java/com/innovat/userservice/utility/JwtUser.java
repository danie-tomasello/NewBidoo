package com.innovat.userservice.utility;

import java.util.List;

import lombok.Data;

/**
 * Created by stephan on 20.03.16.
 */
@Data
public class JwtUser {

    private String username;
    private String password;
    private List<String> authorities;
    private boolean enabled;

    public JwtUser(
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
    
    public JwtUser(String username) {this.username=username;}
}

