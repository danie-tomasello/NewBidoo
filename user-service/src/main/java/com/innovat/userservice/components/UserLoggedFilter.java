package com.innovat.userservice.components;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

import com.innovat.userservice.dto.CheckUser;
import com.innovat.userservice.utility.JwtTokenUtil;

@Component
public class UserLoggedFilter implements Filter {
    
	
	 @Override
	    public void doFilter(
	      ServletRequest request, 
	      ServletResponse response, 
	      FilterChain chain) throws IOException, ServletException {
	 
	        HttpServletRequest req = (HttpServletRequest) request;
	        String token = req.getHeader("X-Auth");
	        if(token!= null) {
	        	req.setAttribute("userLogged", JwtTokenUtil.getUserDetails(token));
	        }
	        else {
	        	req.setAttribute("userLogged", new CheckUser("anonymous"));
	        }
	        
	        chain.doFilter(request, response);
	    }  

    // other methods 
}
