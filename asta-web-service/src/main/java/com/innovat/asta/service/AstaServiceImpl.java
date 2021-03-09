package com.innovat.asta.service;


import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.innovat.asta.dto.AstaDTO;
import com.innovat.asta.dto.BeanFactory;
import com.innovat.asta.model.Asta;
import com.innovat.asta.repository.AstaRepository;

@Service
public class AstaServiceImpl implements AstaService {
	
	@Autowired
    private AstaRepository astaRepository;

	@Override
	@Cacheable(value = "aste")
	public List<AstaDTO> loadAllAsta() {
		// TODO Auto-generated method stub
		List<AstaDTO> res = new ArrayList<>();
		for(Asta asta : astaRepository.findAll()) {
			res.add(BeanFactory.createDtoAsta(asta));
		}
		return res; 
	}

	@Override
	public boolean save(AstaDTO astaDto) {
		// TODO Auto-generated method stub
		Asta asta = BeanFactory.createAsta(astaDto);		
		astaRepository.save(asta);
		return astaRepository.existsById(asta.getId());
	}

	@Override
	@Cacheable(value = "aste")
	public List<AstaDTO> loadCategory(Long id) {
		// TODO Auto-generated method stub
		List<AstaDTO> res = new ArrayList<>();
		List<Asta> astaList = astaRepository.findByProductCategoriesId(id);
		if(astaList!=null) {
			for(Asta asta : astaList) {
				res.add(BeanFactory.createDtoAsta(asta));
			}
		return res;	
		}
		return null;
	}

	@Override
	@Cacheable(value = "aste")
	public List<AstaDTO> searchForName(String name) {
		// TODO Auto-generated method stub
		List<AstaDTO> res = new ArrayList<>();
		List<Asta> astaList =astaRepository.findByProductNameLike("%"+name+"%");
		if(astaList!=null && !astaList.isEmpty()) {
			System.out.println("ricerca non nulla");
			for(Asta asta : astaList) {
				res.add(BeanFactory.createDtoAsta(asta));
			}
		return res;	
		}
		return null;
	}

	@Override
	public boolean update(AstaDTO astaDto) {
		// TODO Auto-generated method stub
		
		return false;
	}

	@Override
	public boolean delete(Long id) {
		// TODO Auto-generated method stub
		astaRepository.deleteById(id);
		return astaRepository.existsById(id)?false:true;
	}

}
