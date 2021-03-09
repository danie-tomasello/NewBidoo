package com.innovat.eurekaserver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class BasicSecurityConfiguration  extends WebSecurityConfigurerAdapter {
	
	@Value("${sicurezza.adminpwd}")
	String admin_password;
	
	@Value("${sicurezza.userpwd}")
	String user_password;
	
	
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
		

	@Autowired
    public void configureGlobal(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder
                .inMemoryAuthentication()
                .withUser("user")
	            .password(passwordEncoder().encode(user_password))
	            .roles("USER")
            .and()
	            .withUser("admin")
	            .password(passwordEncoder().encode(admin_password))
	            .roles("USER","ACTUATOR");
	            
    }
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.csrf()
			.disable()
			.httpBasic()
		.and()
			.authorizeRequests()
			.antMatchers("/actuator/**").hasAuthority("ROLE_ACTUATOR")
			.antMatchers("*/**").hasAuthority("ROLE_USER")
			.anyRequest().authenticated();
	}
}
