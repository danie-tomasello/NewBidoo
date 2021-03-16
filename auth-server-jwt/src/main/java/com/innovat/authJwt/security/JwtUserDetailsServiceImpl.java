package com.innovat.authJwt.security;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.innovat.authJwt.security.client.DTOUser;
import com.innovat.authJwt.security.client.UserClient;

import lombok.extern.java.Log;

@Log
@Service
public class JwtUserDetailsServiceImpl implements UserDetailsService {
 
	//private static final Logger logger = LoggerFactory.getLogger(CustomerUserDetailsService.class);
	
	@Autowired
	private UserClient userClient;
	
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    	return getHttpValue(username);
    }
    
    private JwtUser getHttpValue(String userId){
    	
    	DTOUser user=null;
    	try {
    		
    		user=userClient.getByUsername(userId);    	
    		log.info(this.getClass().getSimpleName()+" "+user.toString());
    	}catch(Exception e) {
    		e.printStackTrace();
    	}    	    	
    	
    	if (user == null) {
            throw new UsernameNotFoundException(String.format("No user found with username '%s'.", userId));            
        } else {        	
            return JwtUserFactory.create(user);
        }
    }
}