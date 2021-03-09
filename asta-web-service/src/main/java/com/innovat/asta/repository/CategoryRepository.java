package com.innovat.asta.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.innovat.asta.model.Category;

public interface CategoryRepository  extends JpaRepository<Category, Long>{
	
	List<Category> findAll();
	
	Category findByName(String name);

}
