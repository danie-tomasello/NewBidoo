
package com.innovat.asta.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.innovat.asta.model.Category;
import com.innovat.asta.repository.CategoryRepository;

@Service
public class CategoryServiceImpl implements CategoryService {
	
	@Autowired
	CategoryRepository categoryRepository;
	
	@Override
	public List<Category> findAll(){
		return categoryRepository.findAll();
	}
	@Override
	public Category findByName(String name) {
		return categoryRepository.findByName(name);
	}

}
