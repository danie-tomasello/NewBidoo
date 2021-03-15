package com.innovat.userservice.service;



import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.innovat.userservice.dto.DTOUser;
import com.innovat.userservice.dto.DTOUserFactory;
import com.innovat.userservice.model.User;
import com.innovat.userservice.repository.AuthorityRepository;
import com.innovat.userservice.repository.UserRepository;

import lombok.extern.java.Log;

@Log
@Service
@CacheConfig(cacheNames= {"user"})
public class UserServiceImpl implements UserService {	
     
     
	@Autowired
    private PasswordEncoder passwordEncoder;
	
	@Autowired
    private AuthorityRepository auth;
	
    @Autowired
    private JavaMailSender mailSender;
    
    
    @Autowired
    private UserRepository repo;
    
   @Cacheable(value="user", key = "#username", sync = true)
    public User loadUserByUsername(String username) {
	   System.out.println("metodo non cachato");
    	return repo.findByUsername(username);
    }
    
    public void register(DTOUser dtouser, String userLogged) throws UnsupportedEncodingException, MessagingException {
    	log.info("=====================start register================");
    	User user = DTOUserFactory.createUser(dtouser,userLogged,passwordEncoder,auth);
    	user.setEnabled(false);
        repo.save(user);
        sendVerificationEmail(user);
    }
    
    public boolean verify(String verificationCode) {
        User user = repo.findByVerification(verificationCode);
         
        if (user == null || user.getEnabled()) {
            return false;
        } else {
            user.setVerification(null);
            user.setEnabled(true);
            repo.save(user);
             
            return true;
        }
         
    }
     
    private void sendVerificationEmail(User user) throws UnsupportedEncodingException, MessagingException {
    	String toAddress = user.getEmail();
        String fromAddress = "danieletomasello.innovat@gmail.com";
        String senderName = "Innovat";
        String subject = "Please verify your registration";
        String content = "Dear [[name]],<br>"
                + "Please click the link below to verify your registration:<br>"
                + "<h3><a href=\"[[URL]]\" target=\"_self\">VERIFY</a></h3>"
                + "Thank you,<br>"
                + "Innovat.";
         
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
         
        helper.setFrom(fromAddress, senderName);
        helper.setTo(toAddress);
        helper.setSubject(subject);
         
        content = content.replace("[[name]]", user.getUsername());
        String verifyURL ="http://localhost:4200/verify/" + user.getVerification();
         
        content = content.replace("[[URL]]", verifyURL);
         
        helper.setText(content, true);
         
        mailSender.send(message);
        log.info("=====================fine register================");
    }

    @Cacheable
	public List<User> getAll() {
		// TODO Auto-generated method stub
    	System.out.println("metodo non cachato");
		return repo.findAll();
	}
	@Cacheable
	public boolean isExist(Long id) {
		// TODO Auto-generated method stub
		return repo.existsById(id);
	}

	@Caching(evict = {
			@CacheEvict(cacheNames = "user", allEntries = true),
			@CacheEvict(cacheNames = "user", key = "#user.username")
	})
	public boolean save(DTOUser dtouser,String userLogged) {
		// TODO Auto-generated method stub
		User user = DTOUserFactory.createUser(dtouser,userLogged,passwordEncoder,auth);
    	repo.save(user);
		return repo.findByUsername(user.getUsername())!=null;
	}

	@Caching(evict = {
			@CacheEvict(cacheNames = "user", allEntries = true),
			@CacheEvict(cacheNames = "user", key = "#user.username")
	})
	public boolean delete(Long id) {
		// TODO Auto-generated method stub
		repo.deleteById(id);
		return repo.existsById(id);
	}

        
}
