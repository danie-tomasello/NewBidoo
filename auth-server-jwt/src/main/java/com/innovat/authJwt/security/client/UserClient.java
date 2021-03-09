package com.innovat.authJwt.security.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name="user-service")
public interface UserClient {
	
	@RequestMapping(value = "/userservice/checkAuth/{username}", method = RequestMethod.GET)
    public User getByUsername(@PathVariable (value="username") String username);

}
