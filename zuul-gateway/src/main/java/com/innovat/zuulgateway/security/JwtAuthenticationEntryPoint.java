package com.innovat.zuulgateway.security;

import java.io.IOException;
import java.io.Serializable;
import java.util.Collections;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.innovat.zuulgateway.security.exception.ExpiredSessionException;

import lombok.extern.java.Log;

@Component
@Log
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint, Serializable {

    private static final long serialVersionUID = -8970718410437077606L;

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
    	   	
    	response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);

		Exception exception = (Exception) request.getAttribute("exception");

		String message;

		if (exception != null) {

			if(exception instanceof ExpiredSessionException) {
				
				log.info("================= expired session exception ==================");
				response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
				response.setContentType(MediaType.APPLICATION_JSON_VALUE);
			}
			byte[] body = new ObjectMapper().writeValueAsBytes(Collections.singletonMap("cause", exception.toString()));

			response.getOutputStream().write(body);
			
			

		} else {

			if (authException.getCause() != null) {
				message = authException.getCause().toString() + " " + authException.getMessage();
			} else {
				message = authException.getMessage();
			}

			byte[] body = new ObjectMapper().writeValueAsBytes(Collections.singletonMap("error", message));

			response.getOutputStream().write(body);
		}
    }
    
}