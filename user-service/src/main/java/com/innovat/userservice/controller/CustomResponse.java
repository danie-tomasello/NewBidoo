package com.innovat.userservice.controller;

import java.util.Date;

import lombok.Data;

@Data
public class CustomResponse<k> {
	
	private Date data = new Date();
	private int cod;
	private String msg;
	private k object;

}
