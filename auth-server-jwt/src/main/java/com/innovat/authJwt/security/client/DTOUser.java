package com.innovat.authJwt.security.client;

import java.util.List;



import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class DTOUser {

	private Long id;

	private String username;
    
    private String password;
	
	private String email;
    
	private String phoneNumber;
	
	private String verification;
	
	private Boolean enabled;
	
	private List<String> authorities;
    
    
    
    DTOUser(){}
}

