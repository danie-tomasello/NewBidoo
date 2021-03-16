package com.innovat.userservice.service;

import java.util.List;

import com.innovat.userservice.dto.DTOUser;
import com.innovat.userservice.model.User;


public interface UserService {
	
	public List<User> getAll();
	
	public User loadUserByUsername(String username);
	
	public void save(DTOUser user, String userLogged);
	
	public void update(DTOUser user, String userLogged);
	
	public void delete(Long id);
	
	public boolean isExist(Long id);

}
