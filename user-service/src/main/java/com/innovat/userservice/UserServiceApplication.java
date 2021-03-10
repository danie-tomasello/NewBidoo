package com.innovat.userservice;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.innovat.userservice.model.Authority;
import com.innovat.userservice.model.User;
import com.innovat.userservice.repository.AuthorityRepository;
import com.innovat.userservice.repository.UserRepository;

@EnableJpaRepositories
@SpringBootApplication
@EnableDiscoveryClient
@EnableJpaAuditing
public class UserServiceApplication {
	

	@Autowired
	PasswordEncoder passwordEncoder;

	@Bean
	public CommandLineRunner loadData (UserRepository userRepository, AuthorityRepository authorityRepository) {
		return (args) -> {

			
			
			User user=userRepository.findByUsername("admin");
			User user2=userRepository.findByUsername("dani");
			
			
			User dani=userRepository.findByUsername("daniele");
			if(dani!=null) {
				userRepository.delete(dani);
			}
			
			if(user == null){

				/**
				 * Inizializzo i dati del mio test
				 */

				Authority authorityAdmin=new Authority();
				authorityAdmin.setName("ROLE_ADMIN");
				authorityAdmin=authorityRepository.save(authorityAdmin);

				Authority authorityUser=new Authority();
				authorityUser.setName("ROLE_USER");
				authorityUser=authorityRepository.save(authorityUser);
				


				List<Authority> authorities = Arrays.asList(new Authority[] {authorityAdmin,authorityUser});


				user = new User();
				user.setAuthorities(authorities);
				user.setEnabled(true);
				user.setUsername("admin");
				user.setEmail("email@example.it");
				user.setPhoneNumber("123456789");
				user.setPassword(passwordEncoder.encode("admin"));

				user = userRepository.save(user);

			}
			if(user2 == null){

				/**
				 * Inizializzo i dati del mio test
				 */



				
				Authority authorityUser= authorityRepository.findByName("ROLE_USER");


				List<Authority> authorities = Arrays.asList(new Authority[] {authorityUser});


				user = new User();
				user.setAuthorities(authorities);
				user.setEnabled(true);
				user.setUsername("dani");
				user.setEmail("email2@example.it");
				user.setPhoneNumber("987654321");
				user.setPassword(passwordEncoder.encode("dani"));

				user = userRepository.save(user);

			}
		};
	}

	public static void main(String[] args) {
		SpringApplication.run(UserServiceApplication.class, args);
	}

}
