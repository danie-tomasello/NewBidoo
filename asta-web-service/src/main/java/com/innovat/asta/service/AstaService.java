package com.innovat.asta.service;

import java.util.List;

import com.innovat.asta.dto.AstaDTO;


public interface AstaService {

	public List<AstaDTO> loadAllAsta();
	
	public List<AstaDTO> loadCategory(Long id);
	
	public boolean save(AstaDTO astaDto);
	
	public boolean update(AstaDTO astaDto);
	
	public boolean delete(Long id);

	public List<AstaDTO> searchForName(String name);
}
