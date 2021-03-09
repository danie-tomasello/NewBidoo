package com.innovat.userservice.dto;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;
import lombok.ToString;
@Data
@ToString
public class DTOUser {

	@NotNull(message="{NotNull.User.username.Validation}")
    @Size(min = 4, max = 50, message="{Size.User.username.Validation}")
	private String username;
    
	@NotNull(message="{NotNull.User.password.Validation}")
    @Size(min = 4, max = 100, message="{Size.User.password.Validation}")
    private String password;
	
	@Column(name = "EMAIL", length = 50, unique = true)
    @NotNull(message="{NotNull.User.email.Validation}")
    @Size(min = 4, max = 50 ,message="{Size.User.email.Validation}")
	private String email;
    
	@NotNull(message="{NotNull.User.phoneNumber.Validation}")
    @Size(min = 4, max = 15, message="{Size.User.phoneNumber.Validation}")
	private String phoneNumber;
    
    
    
    DTOUser(){}
}
