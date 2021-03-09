package com.innovat.authJwt.exception;

import org.apache.http.HttpStatus;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;



@ControllerAdvice
@RestController
public class RestExceptionHandler extends ResponseEntityExceptionHandler {
	
	@ExceptionHandler(NotFoundException.class)
	public final ResponseEntity<ErrorResponse> exceptionNotFoundHandler(Exception ex){
		ErrorResponse error = new ErrorResponse();
		error.setCod(HttpStatus.SC_NOT_FOUND);
		error.setMsg(ex.getMessage());
		
		return new ResponseEntity<ErrorResponse>(error,new HttpHeaders(),HttpStatus.SC_NOT_FOUND);
	}

}