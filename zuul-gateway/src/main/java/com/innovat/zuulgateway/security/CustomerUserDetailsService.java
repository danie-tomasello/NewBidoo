package com.innovat.zuulgateway.security;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.innovat.zuulgateway.security.client.DTOUser;
import com.innovat.zuulgateway.security.client.UserClient;

import lombok.extern.java.Log;

@Log
@Service
public class CustomerUserDetailsService implements UserDetailsService{
	
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
    		throw new UsernameNotFoundException(String.format("No user found with username '%s'.", userId));
    	}    	    	
    	return JwtUserFactory.create(user);
    }

}
