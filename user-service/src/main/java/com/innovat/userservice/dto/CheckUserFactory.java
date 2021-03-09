package com.innovat.userservice.dto;
import java.util.List;
import java.util.stream.Collectors;

import com.innovat.userservice.model.Authority;
import com.innovat.userservice.model.User;

public final class CheckUserFactory {

    private CheckUserFactory() {
    }

    
    public static CheckUser create(User user) {
        return new CheckUser(
                user.getUsername(),
                user.getPassword(),
                mapToGrantedAuthorities(user.getAuthorities()),
                user.getEnabled()
        );
    }

    
    private static List<String> mapToGrantedAuthorities(List<Authority> authorities) {
        return authorities.stream()
                .map(authority -> authority.getName())
                .collect(Collectors.toList());
    }
}