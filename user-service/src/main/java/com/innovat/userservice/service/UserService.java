package com.innovat.userservice.service;

import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.mail.MessagingException;

import com.innovat.userservice.dto.DTOUser;
import com.innovat.userservice.model.User;


public interface UserService {
	
	public List<User> getAll();
	
	public User loadUserByUsername(String username);
	
	public void register(DTOUser user, String userLogged) throws UnsupportedEncodingException, MessagingException;
	
	public boolean verify(String verificationCode);
	
	public boolean save(DTOUser user, String userLogged);
	
	public boolean delete(Long id);
	
	public boolean isExist(Long id);

}
