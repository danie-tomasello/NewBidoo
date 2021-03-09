package com.innovat.authJwt.security;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import lombok.extern.java.Log;

@Component
@Log
public class JwtAccessDeniedHandler implements AccessDeniedHandler,Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
			AccessDeniedException accessDeniedException) throws IOException, ServletException {
		// TODO Auto-generated method stub
		String ErrMsg = "Non sei autorizzato ad eseguire questa azione!";
		
		log.warning("Errore Sicurezza: " + accessDeniedException.getMessage());
		
		// Authentication failed, send error response.
		response.setContentType("application/json;charset=UTF-8");
		response.setStatus(HttpServletResponse.SC_FORBIDDEN);

		PrintWriter writer = response.getWriter();
		writer.println(ErrMsg);
    
	}

}
