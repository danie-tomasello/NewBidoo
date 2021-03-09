package com.innovat.asta.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.innovat.asta.model.Asta;

public interface AstaRepository extends JpaRepository<Asta, Long>{
	List<Asta> findAll();
	List<Asta> findByProductCategoriesId(Long id);
	List<Asta> findByProductNameLike(String name);

}
