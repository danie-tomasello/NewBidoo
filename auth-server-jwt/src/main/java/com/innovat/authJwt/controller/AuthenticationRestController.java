package com.innovat.authJwt.controller;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.hazelcast.core.HazelcastInstance;
import com.innovat.authJwt.exception.ExpiredSessionException;
import com.innovat.authJwt.security.service.JwtTokenUtil;

import io.jsonwebtoken.impl.DefaultClaims;
import lombok.extern.java.Log;

@Log
@RestController
@RequestMapping(value = "${sicurezza.uri}")
public class AuthenticationRestController {

    @Value("${jwt.header}")
    private String tokenHeader;
    
    @Value("${jwt.refreshHeader}")
    private String tokenRefreshHeader;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserDetailsService userDetailsService;
    
    @Autowired
    private HazelcastInstance hz;
    
    

    @RequestMapping(value = "${sicurezza.signin}", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@Valid @RequestBody JwtAuthenticationRequest authenticationRequest,BindingResult bindingResult, HttpServletResponse response) throws Exception {
    	
    	log.info("===========================Start auth/signin/=="+authenticationRequest.toString()+"=============================");
    	// Effettuo l autenticazione
    	try {
	        final Authentication authentication = authenticationManager.authenticate(
	                new UsernamePasswordAuthenticationToken(
	                        authenticationRequest.getUsername(),
	                        authenticationRequest.getPassword()
	                )
	        );
	        
	        SecurityContextHolder.getContext().setAuthentication(authentication);
	        
	    } catch (DisabledException e) {
			throw new Exception("USER_DISABLED", e);
		}
		catch (BadCredentialsException e) {
			throw new Exception("INVALID_CREDENTIALS", e);
		}
        
        

        // Genero Token
        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        final String token = jwtTokenUtil.generateToken(userDetails);
        final String refreshToken = jwtTokenUtil.createRefreshToken(userDetails.getUsername());
        
        Map<String, String> tokenSessionMap = hz.getMap("tokenMap");
        tokenSessionMap.put(userDetails.getUsername(), refreshToken);
		
		
        // Ritorno il token        
        response.addHeader(tokenHeader, token);
        response.addHeader(tokenRefreshHeader, refreshToken);
        return ResponseEntity.ok(new JwtAuthenticationResponse(userDetails.getUsername(),userDetails.getAuthorities()));
    }
    
    
    
    
    @RequestMapping(value="${sicurezza.logout}",method=RequestMethod.GET)
	public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
		log.info("===========================Start auth/logout/===============================");
		
		String refreshToken=request.getHeader(tokenRefreshHeader);
		String userId= jwtTokenUtil.getUsernameFromToken(refreshToken);
		
		Map<String, String> tokenSessionMap = hz.getMap("tokenMap");
		tokenSessionMap.remove(userId);
		
		HttpSession session= request.getSession(false);
	    SecurityContextHolder.clearContext();
	        if(session != null) {
	            session.invalidate();
	        }
	        
	       return ResponseEntity.ok().body(null);
			
	}
    
    @RequestMapping(value = "${sicurezza.refresh}", method = RequestMethod.GET)
    public ResponseEntity<?> refreshAndGetAuthenticationToken(HttpServletRequest request, HttpServletResponse response) throws ExpiredSessionException {
    	log.info("===========================Start auth/refresh/===============================");
    	
    	DefaultClaims claims = (io.jsonwebtoken.impl.DefaultClaims) request.getAttribute("claims");
    	String refreshToken=request.getHeader(tokenRefreshHeader);
    	Map<String, String> tokenSessionMap = hz.getMap("tokenMap");
    	 
    	if (claims!=null) {
	    	if(!tokenSessionMap.containsKey(claims.getSubject()) || !tokenSessionMap.containsValue(refreshToken)) {
	    		throw new ExpiredSessionException(); 
	    	}
    	
        
            String refreshedToken = jwtTokenUtil.generateToken(claims);
            log.info("claims ============================ "+claims.getSubject());
            
            response.setHeader(tokenRefreshHeader, jwtTokenUtil.createRefreshToken(claims.getSubject()));
            response.setHeader(tokenHeader,refreshedToken);
            
           
            tokenSessionMap.put(claims.getSubject(), refreshedToken);
            

            return ResponseEntity.ok().body(null);
        }
    	else {
        
    		return ResponseEntity.badRequest().body(null);
    	}
        
    }
    
    
     

}