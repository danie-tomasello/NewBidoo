package com.innovat.asta.service;

import java.util.List;

import com.innovat.asta.model.Category;

public interface CategoryService {
	
	public List<Category> findAll();
	
	public Category findByName(String name);

}
