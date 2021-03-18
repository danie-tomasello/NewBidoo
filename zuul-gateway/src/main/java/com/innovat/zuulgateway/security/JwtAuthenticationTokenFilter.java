package com.innovat.zuulgateway.security;


import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import com.innovat.zuulgateway.security.exception.ExpiredSessionException;

import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.java.Log;

@Log
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {



    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Value("${jwt.header}")
    private String tokenHeader;
    
    @Value("${jwt.refreshHeader}")
    private String tokenRefreshHeader;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        String authToken = request.getHeader(tokenHeader);
        
        
        UserDetails userDetails = null;
        try {
	        if(authToken != null && jwtTokenUtil.validateToken(authToken)){
	            userDetails = jwtTokenUtil.getUserDetails(authToken);
	        }
	
	        if (userDetails != null && SecurityContextHolder.getContext().getAuthentication() == null) {
	
	            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
	            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
	            SecurityContextHolder.getContext().setAuthentication(authentication);
	            
	        }
	    }
        catch(ExpiredJwtException ex)
		{
        	log.warning("============================ token scaduto =========================");
			String refreshToken = request.getHeader(tokenRefreshHeader);
			String requestURL = request.getRequestURL().toString();
			// allow for Refresh Token creation if following conditions are true.
			try {
				if (refreshToken != null && jwtTokenUtil.validateToken(refreshToken) && requestURL.contains("refresh")) {
					log.info("==================il tokenRefresh è valido==============");
					allowForRefreshToken(ex, request);
				}
				else {
					request.setAttribute("exception", ex);
				}
			}
			catch(ExpiredJwtException exc){
				log.info("==================== sessione scaduta =======================");
				request.setAttribute("exception", new ExpiredSessionException());
			}
		}
		 catch(BadCredentialsException ex)
		 {
			 request.setAttribute("exception", ex);
		 }

        chain.doFilter(request, response);
    }
    
    private void allowForRefreshToken(ExpiredJwtException ex, HttpServletRequest request) {

		// create a UsernamePasswordAuthenticationToken with null values.
		UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
				null, null, null);
		// After setting the Authentication in the context, we specify
		// that the current user is authenticated. So it passes the
		// Spring Security Configurations successfully.
		SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
		// Set the claims so that in controller we will be using it to create
		// new JWT
		request.setAttribute("claims", ex.getClaims());

	}
}