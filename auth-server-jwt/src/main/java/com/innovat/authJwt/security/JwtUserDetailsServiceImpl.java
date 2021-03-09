package com.innovat.authJwt.security;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.innovat.authJwt.security.client.User;
import com.innovat.authJwt.security.client.UserClient;

@Service
public class JwtUserDetailsServiceImpl implements UserDetailsService {
 
	@Autowired
	private UserClient userClient;
	
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    	return getHttpValue(username);
    }
    
    private JwtUser getHttpValue(String userId){
    	
    	User user=null;
    	
    		
    		user=userClient.getByUsername(userId);    		
    	   	    	
    	
    	if (user == null) {
            throw new UsernameNotFoundException(String.format("No user found with username '%s'.", userId));            
        } else {        	
            return JwtUserFactory.create(user);
        }
    }
}