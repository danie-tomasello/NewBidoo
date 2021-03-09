package com.innovat.userservice.dto;

import java.util.Arrays;
import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.innovat.userservice.model.Authority;
import com.innovat.userservice.model.User;
import com.innovat.userservice.repository.AuthorityRepository;

import lombok.extern.java.Log;
import net.bytebuddy.utility.RandomString;

@Log
public class DTOUserFactory {
	
	
	
	public static User createUser(DTOUser dtouser,PasswordEncoder passwordEncoder,AuthorityRepository auth) {
		User user = new User();
		log.info("Creazione oggetto User");
	    user.setUsername(dtouser.getUsername());
	    String encodedPassword = passwordEncoder.encode(dtouser.getPassword());
	    user.setPassword(encodedPassword);
	     
	    user.setEnabled(true);
	    
	    user.setEmail(dtouser.getEmail());
	    
	    user.setPhoneNumber(dtouser.getPhoneNumber());
	    
	    String randomCode = RandomString.make(64);
	    user.setVerification(randomCode);
	    
	    Authority authorityUser = auth.findByName("ROLE_USER");
	    List<Authority> authorities = Arrays.asList(new Authority[] {authorityUser});
	    user.setAuthorities(authorities); 
	    log.info("Oggetto User creato con successo");
	    return user;
	 
	}
	
	public static DTOUser createDTOUser(User user) {
		DTOUser dtouser = new DTOUser();
		
		dtouser.setUsername(user.getUsername());
		dtouser.setPhoneNumber(user.getPhoneNumber());
		dtouser.setEmail(user.getEmail());
		
		return dtouser;
	}
}
