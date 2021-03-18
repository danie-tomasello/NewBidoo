package com.innovat.registrationservice.service;

import java.io.UnsupportedEncodingException;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import com.innovat.registrationservice.controller.UserClient;
import com.innovat.registrationservice.dto.DTOUser;
import com.innovat.registrationservice.exception.DuplicateException;

import lombok.extern.java.Log;
import net.bytebuddy.utility.RandomString;

@Log
@Service
public class RegistrationServiceImpl implements RegistrationService {
	
    @Autowired
    private JavaMailSender mailSender;
    
    @Autowired
	private UserClient userClient;
	
	private void sendVerificationEmail(DTOUser user) throws UnsupportedEncodingException, MessagingException {
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
        String verifyURL ="http://localhost:4200/verify?username="+user.getUsername()+"&code=" + user.getVerification();
         
        content = content.replace("[[URL]]", verifyURL);
         
        helper.setText(content, true);
         
        mailSender.send(message);
        log.info("=====================fine register================");
    }

	@Override
	public void send(DTOUser dtouser) throws DuplicateException, UnsupportedEncodingException, MessagingException {
		
		dtouser.setVerification(RandomString.make(64));
		dtouser.setEnabled(false);
		ResponseEntity<?> res = userClient.save(dtouser);
		if(res.getStatusCode().value()==HttpStatus.NOT_ACCEPTABLE.value()) {
	  		String errMsg = "Questo utente esiste già";
	  		log.warning(errMsg);
	  		throw new DuplicateException(errMsg);	    	 
		    }
		
		sendVerificationEmail(dtouser);
		
	}


	@Override
	public boolean verify(String code,String username) {
		
		DTOUser user = userClient.getByUsername(username);
		if(user==null||user.getVerification()==null||!user.getVerification().equalsIgnoreCase(code)) {
			return false;
		}
		user.setVerification(null);
		user.setEnabled(true);
		userClient.update(user);
		return true;
	}
}
