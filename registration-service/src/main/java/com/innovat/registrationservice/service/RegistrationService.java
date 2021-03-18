package com.innovat.registrationservice.service;

import java.io.UnsupportedEncodingException;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.validation.BindingResult;

import com.innovat.registrationservice.dto.DTOUser;
import com.innovat.registrationservice.exception.DuplicateException;

public interface RegistrationService {

	void send(DTOUser dtouser) throws DuplicateException, UnsupportedEncodingException, MessagingException;

	boolean verify(String code,String username);

}
