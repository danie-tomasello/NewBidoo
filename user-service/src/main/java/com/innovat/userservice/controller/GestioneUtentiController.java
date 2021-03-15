package com.innovat.userservice.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.innovat.userservice.Exception.BindingException;
import com.innovat.userservice.Exception.DuplicateException;
import com.innovat.userservice.Exception.NotFoundException;
import com.innovat.userservice.dto.CheckUser;
import com.innovat.userservice.dto.CheckUserFactory;
import com.innovat.userservice.dto.DTOUser;
import com.innovat.userservice.model.User;
import com.innovat.userservice.service.UserService;
import com.innovat.userservice.utility.JwtTokenUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.java.Log;

@RestController
@RequestMapping(value = "${gestioneUtenti.uri}")
@Api(value="userservice", tags="Controller operazioni di gestione dati utenti")
@Log
public class GestioneUtentiController {
	
	@Autowired
    private UserService service;
	
	@Autowired
	private ResourceBundleMessageSource msg;
	
	@Value("${jwt.header}")
    private String tokenHeader;
	
	
	
	@ApiOperation(
		      value = "Cerca tutti gli utenti", 
		      notes = "Restituisce tutti gli utenti",
		      response = CustomResponse.class, 
		      produces = "application/json")
	@ApiResponses(value =
	{   @ApiResponse(code = 200, message = "ricerca effettuata con successo"),
	    @ApiResponse(code = 404, message = "Nessun utente trovato"),
	    @ApiResponse(code = 403, message = "Non sei AUTORIZZATO ad accedere alle informazioni"),
	    @ApiResponse(code = 401, message = "Non sei AUTENTICATO")
	})
	@RequestMapping(value = "${gestioneUtenti.getAll}", method = RequestMethod.GET)
    public ResponseEntity<?> getAllUser(HttpServletRequest request, HttpServletResponse response){
    	log.info("===========================Start userservice/search/'all'===============================");
    	CustomResponse<List<User>> res = new CustomResponse<>();
    	
    	List<User> userList = service.getAll();
    	res.setCod(HttpStatus.OK.value());
    	res.setMsg("ricerca effettuata con successo");
    	res.setObject(userList);
    	return ResponseEntity.ok(res);
    }
    
	
	@ApiOperation(
		      value = "Ricerca utente per nome", 
		      notes = "Restituisce l'utente trovato",
		      response = CustomResponse.class, 
		      produces = "application/json")
	@ApiResponses(value =
	{   @ApiResponse(code = 200, message = "L'utente è stato trovato"),
	    @ApiResponse(code = 404, message = "Nessun utente trovato"),
	    @ApiResponse(code = 403, message = "Non sei AUTORIZZATO ad accedere alle informazioni"),
	    @ApiResponse(code = 401, message = "Non sei AUTENTICATO")
	})
    @RequestMapping(value = "${gestioneUtenti.getByUsername}", method = RequestMethod.GET)
    public ResponseEntity<?> getByUsername(@ApiParam("Nome utente") @PathVariable (value="username") String username) throws NotFoundException {
    	log.info("===========================Start userservice/search/=="+username+"=============================");
    	CustomResponse<User> res = new CustomResponse<>();
    	User user = service.loadUserByUsername(username);   
    	
    	if(user==null) {
    		String errMsg = "Nessun utente trovato";
    		log.warning(errMsg);
    		throw new NotFoundException(errMsg);
    	}
    	
    	res.setCod(HttpStatus.OK.value());
    	res.setMsg("L'utente è stato trovato");
    	res.setObject(user);
    	return ResponseEntity.ok(res);
    }
	
	
	
	@ApiOperation(
		      value = "Registrazione utente", 
		      notes = "Restituisce lo stato della registrazione",
		      response = CustomResponse.class, 
		      produces = "application/json")
	@ApiResponses(value =
	{   @ApiResponse(code = 201, message = "L'utente è stato creato con successo, è stata inviata un email per la conferma dell'account"),
	    @ApiResponse(code = 406, message = "Questo utente esiste già"),
	    @ApiResponse(code = 403, message = "Non sei AUTORIZZATO ad accedere alle informazioni"),
	    @ApiResponse(code = 401, message = "Non sei AUTENTICATO")
	})
	@RequestMapping(value = "${gestioneUtenti.registration}", method = RequestMethod.POST)
    public ResponseEntity<?> createUser(@ApiParam("Dati registrazione utente") @Valid @RequestBody DTOUser dtouser,BindingResult bindingResult,HttpServletRequest request) throws UnsupportedEncodingException, MessagingException, BindingException, DuplicateException {
		log.info("===========================Start userservice/registration/=="+dtouser.toString()+"=============================");
		CustomResponse<?> res = new CustomResponse<>();
		CheckUser userlogged = JwtTokenUtil.getUserDetails(request.getHeader(tokenHeader));
		//Input validation
		if(bindingResult.hasErrors()) {
			String error = msg.getMessage(bindingResult.getFieldError(), LocaleContextHolder.getLocale());			
			log.warning(error);
			throw new BindingException(error);
		}
		if(service.loadUserByUsername(dtouser.getUsername())!=null) {
    		String errMsg = "Questo utente esiste già";
    		log.warning(errMsg);
    		throw new DuplicateException(errMsg);	    	 
	    }
		
		log.info("start registrazione utente");
		
    	service.register(dtouser,userlogged.getUsername()); 
		res.setCod(HttpStatus.CREATED.value());
		res.setMsg(msg.getMessage("registration.success", null, LocaleContextHolder.getLocale()));
    	
		return ResponseEntity.ok(res);
    }
    
	
	
	
	@ApiOperation(
		      value = "Verifica email utente", 
		      notes = "Restituisce lo stato della validazione utente",
		      response = CustomResponse.class, 
		      produces = "application/json")
	@ApiResponses(value =
	{   @ApiResponse(code = 200, message = "L'utente è stato attivato con successo"),
	    @ApiResponse(code = 404, message = "Richiesta di verifica non valida o scaduta"),
	    @ApiResponse(code = 403, message = "Non sei AUTORIZZATO ad accedere alle informazioni"),
	    @ApiResponse(code = 401, message = "Non sei AUTENTICATO")
	})
    @RequestMapping(value="${gestioneUtenti.verify}",method=RequestMethod.GET)
	public ResponseEntity<?> verifyCode(@ApiParam("Codice attivazione utente") @PathVariable(value="code") String code,HttpServletRequest request,HttpServletResponse response) throws IOException, NotFoundException  {	
    	log.info("===========================Start userservice/verify/=="+code+"=============================");
    	CustomResponse<?> res = new CustomResponse<>();
    	
		if (!service.verify(code)) {
			String errMsg = msg.getMessage("verify.error", null, LocaleContextHolder.getLocale());
    		log.warning(errMsg);
    		throw new NotFoundException(errMsg);
		}
		
		log.info("utente confermato con successo");
		res.setCod(HttpStatus.OK.value());
		res.setMsg(msg.getMessage("verify.success", null, LocaleContextHolder.getLocale()));
    	return ResponseEntity.ok(res);
	}
	
	
    
    
	@ApiOperation(
		      value = "Salvataggio utente", 
		      notes = "Salvataggio un utente attivo nel db",
		      response = CustomResponse.class, 
		      produces = "application/json")
	@ApiResponses(value =
	{   @ApiResponse(code = 201, message = "L'utente è stato creato con successo"),
	    @ApiResponse(code = 406, message = "Questo utente esiste già"),
	    @ApiResponse(code = 403, message = "Non sei AUTORIZZATO ad accedere alle informazioni"),
	    @ApiResponse(code = 401, message = "Non sei AUTENTICATO")
	})
	@RequestMapping(value = "${gestioneUtenti.save}", method = RequestMethod.POST)
	public ResponseEntity<?> save(@ApiParam("Dati registrazione utente") @Valid @RequestBody DTOUser dtouser,BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response) throws  BindingException, DuplicateException {
		log.info("===========================Start userservice/save/=="+dtouser.toString()+"=============================");
		
		CheckUser userlogged = JwtTokenUtil.getUserDetails(request.getHeader(tokenHeader));		
		CustomResponse<?> res = new CustomResponse<>();
		//Input validation
		if(bindingResult.hasErrors()) {
			String error = msg.getMessage(bindingResult.getFieldError(), LocaleContextHolder.getLocale());			
			log.warning(error);
			throw new BindingException(error);
		}
		if(service.loadUserByUsername(dtouser.getUsername())!=null) {
    		String errMsg = "Questo utente esiste già";
    		log.warning(errMsg);
    		throw new DuplicateException(errMsg);	    	 
	    }
		
		log.info("start registrazione utente");
		
    	service.save(dtouser, userlogged.getUsername()); 
		res.setCod(HttpStatus.CREATED.value());
		res.setMsg(msg.getMessage("save.success", null, LocaleContextHolder.getLocale()));
    	
		return ResponseEntity.ok(res);
	}
	
	
    
	
	@ApiOperation(
		      value = "Modifica utente", 
		      notes = "Modifica le informazioni di un utente",
		      response = CustomResponse.class, 
		      produces = "application/json")
	@ApiResponses(value =
	{   @ApiResponse(code = 201, message = "L'utente è stato modificato con successo"),
	    @ApiResponse(code = 406, message = "Questo utente esiste già"),
	    @ApiResponse(code = 403, message = "Non sei AUTORIZZATO ad accedere alle informazioni"),
	    @ApiResponse(code = 401, message = "Non sei AUTENTICATO")
	})
    @RequestMapping(value = "${gestioneUtenti.update}", method = RequestMethod.POST)
    public ResponseEntity<?> update(@ApiParam("Dati utente") @Valid @RequestBody DTOUser dtouser,BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response) throws NotFoundException, BindingException {
    	log.info("===========================Start userservice/update/=="+dtouser.toString()+"=============================");
    	CheckUser userlogged = JwtTokenUtil.getUserDetails(request.getHeader(tokenHeader));	
    	CustomResponse<?> res = new CustomResponse<>();
		//Input validation
		if(bindingResult.hasErrors()) {
			String error = msg.getMessage(bindingResult.getFieldError(), LocaleContextHolder.getLocale());			
			log.warning(error);
			throw new BindingException(error);
		}
    	
    	if(!service.isExist(dtouser.getId())) {
    		String errMsg = "Utente inesistente";
    		log.warning(errMsg);
    		throw new NotFoundException(errMsg);
    	}
    	
    	service.save(dtouser,userlogged.getUsername()); 
    	res.setCod(HttpStatus.CREATED.value());
    	res.setMsg("Utente modificato con successo");
		return ResponseEntity.ok(res);
    	
    }
	
	
    
	
	
	@ApiOperation(
		      value = "Elimina utente", 
		      notes = "Elimina le informazioni di un utente",
		      response = CustomResponse.class, 
		      produces = "application/json")
	@ApiResponses(value =
	{   @ApiResponse(code = 201, message = "L'utente è stato eliminato con successo"),
	    @ApiResponse(code = 406, message = "Questo utente non esiste"),
	    @ApiResponse(code = 403, message = "Non sei AUTORIZZATO ad accedere alle informazioni"),
	    @ApiResponse(code = 401, message = "Non sei AUTENTICATO")
	})
    @RequestMapping(value = "${gestioneUtenti.delete}", method = RequestMethod.DELETE)
    public ResponseEntity<?> delete(@ApiParam("Id utente univoco") @PathVariable(value="userId") Long userId, HttpServletRequest request, HttpServletResponse response) throws NotFoundException {
    	
    	log.info("===========================Start userservice/delete/=="+userId+"=============================");
    	CustomResponse<?> res = new CustomResponse<>();
    	if(!service.isExist(userId)) {
    		String errMsg = "Utente inesistente";
    		log.warning(errMsg);
    		throw new NotFoundException(errMsg);
    	}
    	
    	service.delete(userId);  
    	res.setCod(HttpStatus.OK.value());
    	res.setMsg("Utente eliminato con successo");
    	return ResponseEntity.ok(res);
    }
	
	
	
    
    @RequestMapping(value = "/checkAuth/{userId}", method = RequestMethod.GET)
    public ResponseEntity<?> helpAuth(@PathVariable (value="userId") String userId) throws NotFoundException {
    	log.info("===========================Start userservice/checkAuth/=="+userId+"=============================");
    	
    	User user = service.loadUserByUsername(userId);  
    	if(user==null) {
    		String errMsg = "User not found=="+userId;
    		log.warning(errMsg);
    		throw new NotFoundException(errMsg);
    	} 	
    	
    	return ResponseEntity.ok(CheckUserFactory.create(user));
    }
    
}
