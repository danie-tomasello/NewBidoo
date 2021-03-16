package com.innovat.registrationservice.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.innovat.registrationservice.dto.DTOUser;


@FeignClient(name="user-service")
public interface UserClient {
	
	@RequestMapping(value = "/search/{username}", method = RequestMethod.GET)
    public DTOUser getByUsername(@PathVariable (value="username") String username);
	
	@RequestMapping(value = "/service/save", method = RequestMethod.POST)
	public ResponseEntity<?> save(@RequestBody DTOUser dtouser);
	
	@RequestMapping(value = "/service/update", method = RequestMethod.POST)
	public ResponseEntity<?> update(@RequestBody DTOUser dtouser);

}
