package com.innovat.asta.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.innovat.asta.dto.AstaDTO;
import com.innovat.asta.service.AstaService;	



@RestController
@RequestMapping(value = "asta-service")
public class AstaController {
	
	@Autowired
	AstaService astaService;
	
	@RequestMapping(value = "${asta.getAll}", method = RequestMethod.GET)
    public ResponseEntity<?> getAll() {
        
		List<AstaDTO> allAste = astaService.loadAllAsta();
		
		if(!allAste.isEmpty()&&allAste!=null) {
            return ResponseEntity.ok(allAste);
        } else {
            return ResponseEntity.badRequest().body(null);
        }
    }
	
	@RequestMapping(value = "${asta.save}", method = RequestMethod.POST)
    public ResponseEntity<?> save(@ModelAttribute AstaDTO asta,HttpServletRequest request, HttpServletResponse response) {
        
		boolean success = astaService.save(asta);
		
		return success? ResponseEntity.ok().body(null): ResponseEntity.badRequest().body(null);
    }
	
	@RequestMapping(value = "${asta.update}", method = RequestMethod.POST)
    public ResponseEntity<?> update(@ModelAttribute AstaDTO asta,HttpServletRequest request, HttpServletResponse response) {
        
		boolean success = astaService.update(asta);
		
		return success? ResponseEntity.ok().body(null): ResponseEntity.badRequest().body(null);
    }
	
	@RequestMapping(value = "${asta.delete}", method = RequestMethod.POST)
    public ResponseEntity<?> delete(@PathVariable Long id,HttpServletRequest request, HttpServletResponse response) {
        
		boolean success = astaService.delete(id);
		
		return success? ResponseEntity.ok().body(null): ResponseEntity.badRequest().body(null);
    }

}
