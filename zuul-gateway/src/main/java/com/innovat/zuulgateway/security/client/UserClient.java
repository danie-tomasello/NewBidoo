package com.innovat.zuulgateway.security.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name="user-service")
public interface UserClient {
	
	@RequestMapping(value = "/search/{username}", method = RequestMethod.GET)
    public DTOUser getByUsername(@PathVariable (value="username") String username);

}
