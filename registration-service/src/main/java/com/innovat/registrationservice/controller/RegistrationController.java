package com.innovat.registrationservice.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.innovat.registrationservice.dto.DTOUser;
import com.innovat.registrationservice.exception.BindingException;
import com.innovat.registrationservice.exception.DuplicateException;
import com.innovat.registrationservice.exception.NotFoundException;
import com.innovat.registrationservice.service.RegistrationService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.java.Log;

@RestController
@RequestMapping(value = "${registration.uri}")
@Api(value="service", tags="Controller registrazione utenti")
@Log
public class RegistrationController {
	
	@Autowired
	private ResourceBundleMessageSource msg;
	
	@Autowired
    private RegistrationService service;
	
	@ApiOperation(
		      value = "Registrazione utente", 
		      notes = "Restituisce lo stato della registrazione",
		      response = MessageResponse.class, 
		      produces = "application/json")
	@ApiResponses(value =
	{   @ApiResponse(code = 201, message = "L'utente è stato creato con successo, è stata inviata un email per la conferma dell'account"),
	    @ApiResponse(code = 406, message = "Questo utente esiste già"),
	    @ApiResponse(code = 403, message = "Non sei AUTORIZZATO ad accedere alle informazioni"),
	    @ApiResponse(code = 401, message = "Non sei AUTENTICATO")
	})
	@RequestMapping(value = "${registration.send}", method = RequestMethod.POST)
  public ResponseEntity<?> createUser(@ApiParam("Dati registrazione utente") @Valid @RequestBody DTOUser dtouser,BindingResult bindingResult,HttpServletRequest request) throws UnsupportedEncodingException, MessagingException, BindingException, DuplicateException {
		log.info("===========================Start service/emailsend/=="+dtouser.toString()+"=============================");
		MessageResponse res = new MessageResponse();
		
		//Input validation
		if(bindingResult.hasErrors()) {
			String error = msg.getMessage(bindingResult.getFieldError(), LocaleContextHolder.getLocale());			
			log.warning(error);
			throw new BindingException(error);
		}
		log.info("start registrazione utente");
		
		service.send(dtouser); 
		res.setCod(HttpStatus.CREATED.value());
		res.setMsg(msg.getMessage("registration.success", null, LocaleContextHolder.getLocale()));
  	
		return ResponseEntity.ok(res);
  }
  
	
	
	
	@ApiOperation(
		      value = "Verifica email utente", 
		      notes = "Restituisce lo stato della validazione utente",
		      response = MessageResponse.class, 
		      produces = "application/json")
	@ApiResponses(value =
	{   @ApiResponse(code = 200, message = "L'utente è stato attivato con successo"),
	    @ApiResponse(code = 404, message = "Richiesta di verifica non valida o scaduta"),
	    @ApiResponse(code = 403, message = "Non sei AUTORIZZATO ad accedere alle informazioni"),
	    @ApiResponse(code = 401, message = "Non sei AUTENTICATO")
	})
  @RequestMapping(value="${registration.verify}",method=RequestMethod.GET)
	public ResponseEntity<?> verifyCode(@ApiParam("Codice attivazione utente")@RequestParam("username") String username, @RequestParam("code") String code,HttpServletRequest request,HttpServletResponse response) throws IOException, NotFoundException  {	
  	log.info("===========================Start service/verify/=="+code+"=============================");
  	MessageResponse res = new MessageResponse();
  	
		if (!service.verify(code,username)) {
			String errMsg = msg.getMessage("verify.error", null, LocaleContextHolder.getLocale());
	  		log.warning(errMsg);
	  		throw new NotFoundException(errMsg);
		}
		
		log.info("utente confermato con successo");
		res.setCod(HttpStatus.OK.value());
		res.setMsg(msg.getMessage("verify.success", null, LocaleContextHolder.getLocale()));
		return ResponseEntity.ok(res);
	}

}
