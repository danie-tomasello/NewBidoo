package com.innovat.authJwt.exception;


import java.util.Collections;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;



@ControllerAdvice
@RestController
public class RestExceptionHandler extends ResponseEntityExceptionHandler {
	
	@ExceptionHandler(NotFoundException.class)
	public final ResponseEntity<ErrorResponse> exceptionNotFoundHandler(Exception ex){
		ErrorResponse error = new ErrorResponse();
		error.setCod(HttpStatus.NOT_FOUND.value());
		error.setMsg(ex.getMessage());
		
		return new ResponseEntity<ErrorResponse>(error,new HttpHeaders(),HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(ExpiredSessionException.class)
	public final ResponseEntity<Object> expiredSessionHandler(Exception ex) throws JsonProcessingException{
		HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.APPLICATION_JSON);
		byte[] body = new ObjectMapper().writeValueAsBytes(Collections.singletonMap("cause", ex.getMessage()));
		return new ResponseEntity<Object>(body,headers,HttpStatus.NOT_ACCEPTABLE);
	}

}
