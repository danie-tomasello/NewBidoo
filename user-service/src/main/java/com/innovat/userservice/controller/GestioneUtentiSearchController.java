package com.innovat.userservice.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.innovat.userservice.Exception.NotFoundException;
import com.innovat.userservice.dto.DTOUser;
import com.innovat.userservice.dto.DTOUserFactory;
import com.innovat.userservice.model.User;
import com.innovat.userservice.service.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.java.Log;

@RestController
@RequestMapping(value = "${gestioneUtenti.search}")
@Api(value="search", tags="Controller operazioni di ricerca dati utenti")
@Log
public class GestioneUtentiSearchController {

	@Autowired
    private UserService service;
	
	
	@ApiOperation(
		      value = "Cerca tutti gli utenti", 
		      notes = "Restituisce tutti gli utenti",
		      response = DTOUser.class, 
		      produces = "application/json")
	@ApiResponses(value =
	{   @ApiResponse(code = 200, message = "ricerca effettuata con successo"),
	    @ApiResponse(code = 404, message = "Nessun utente trovato"),
	    @ApiResponse(code = 403, message = "Non sei AUTORIZZATO ad accedere alle informazioni"),
	    @ApiResponse(code = 401, message = "Non sei AUTENTICATO")
	})
	@RequestMapping(value = "${gestioneUtenti.getAll}", method = RequestMethod.GET)
  public ResponseEntity<?> getAllUser(HttpServletRequest request, HttpServletResponse response){
  	log.info("===========================Start search/'all'===============================");
  	
  	List<DTOUser> resList = new ArrayList<>();
  	
  	List<User> userList = service.getAll();
  	
  	
  	for(User user : userList) {
  		resList.add(DTOUserFactory.createDTOUser(user));
  	}
  	
  	return ResponseEntity.ok(resList);
  }
  
	
	@ApiOperation(
		      value = "Ricerca utente per nome", 
		      notes = "Restituisce l'utente trovato",
		      response = DTOUser.class, 
		      produces = "application/json")
	@ApiResponses(value =
	{   @ApiResponse(code = 200, message = "L'utente è stato trovato"),
	    @ApiResponse(code = 404, message = "Nessun utente trovato"),
	    @ApiResponse(code = 403, message = "Non sei AUTORIZZATO ad accedere alle informazioni"),
	    @ApiResponse(code = 401, message = "Non sei AUTENTICATO")
	})
  @RequestMapping(value = "${gestioneUtenti.getByUsername}", method = RequestMethod.GET)
  public ResponseEntity<?> getByUsername(@ApiParam("Nome utente") @PathVariable (value="username") String username) throws NotFoundException {
  	log.info("===========================Start search/=="+username+"=============================");
  	User user = service.loadUserByUsername(username);   
  	
  	if(user==null) {
  		String errMsg = "Nessun utente trovato";
  		log.warning(errMsg);
  		throw new NotFoundException(errMsg);
  	}
  	
  	return ResponseEntity.ok(DTOUserFactory.createDTOUser(user));
  }
	
}
