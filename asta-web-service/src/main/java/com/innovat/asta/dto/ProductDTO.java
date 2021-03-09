package com.innovat.asta.dto;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.innovat.asta.model.Category;
import com.innovat.asta.model.ImageModel;

import lombok.Data;



@Data
public class ProductDTO {

    private Long id;
	
    private String name;
	
    private String brand;
	
    private double price;
	
    private List<AstaDTO> aste;
	
    private List<MultipartFile> imageInputList;

    private List<ImageModel> imageOutputList;
	
    private List<Category> categories;


    public ProductDTO() {}
	   
	public ProductDTO(Long id, String name, String brand, double price, List<AstaDTO> aste,
			List<MultipartFile> imageInputList, List<ImageModel> imageOutputList, List<Category> categories) {
		super();
		this.id = id;
		this.name = name;
		this.brand = brand;
		this.price = price;
		this.aste = aste;
		this.imageInputList = imageInputList;
		this.imageOutputList = imageOutputList;
		this.categories = categories;
	}

	

}
