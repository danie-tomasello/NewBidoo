package com.innovat.userservice.Exception;

import java.util.Date;

import lombok.Data;

@Data
public class ErrorResponse {
	
	private Date data = new Date();
	private int cod;
	private String msg;

}
