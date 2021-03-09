package com.innovat.asta.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.innovat.asta.model.ImageModel;

public interface ImageRepository extends JpaRepository<ImageModel, Long> {
	    Optional<ImageModel> findByName(String name);
	}

