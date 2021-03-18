package com.innovat.zuulgateway.security;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.innovat.zuulgateway.security.client.DTOUser;
import com.innovat.zuulgateway.security.client.UserClient;

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
