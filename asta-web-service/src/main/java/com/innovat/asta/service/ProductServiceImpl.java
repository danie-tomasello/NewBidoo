package com.innovat.asta.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.innovat.asta.model.Product;
import com.innovat.asta.repository.ProductRepository;

@Service
public class ProductServiceImpl implements ProductService {

	@Autowired
    private ProductRepository productRepository;

	@Override
	public List<Product> loadAllProduct() {
		// TODO Auto-generated method stub
		return productRepository.findAll();
	}
	
	
}
