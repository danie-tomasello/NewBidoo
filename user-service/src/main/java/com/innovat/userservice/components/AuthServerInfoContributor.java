package com.innovat.userservice.components;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.info.Info.Builder;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.stereotype.Component;

import com.innovat.userservice.repository.UserRepository;

@Component
public class AuthServerInfoContributor implements InfoContributor{

	@Autowired
	UserRepository userRepo;	
	
	@Override
	public void contribute(Builder builder) {
		// TODO Auto-generated method stub
		long qtaUser = userRepo.findAll().size();
		
		Map<String,Object> userMap = new HashMap<String,Object>();
		userMap.put("Qta Utenti", qtaUser);
		builder.withDetail("user-info", userMap);
	}

}
