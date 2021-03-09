package com.innovat.userservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.innovat.userservice.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
	User findByUsername(String username);

	User findByVerification(String verificationCode);

}