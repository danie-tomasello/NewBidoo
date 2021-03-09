package com.innovat.asta.dto;

import lombok.Data;

@Data
public class AstaDTO {
	
    private Long id;
	
    private ProductDTO product;
    
    public AstaDTO() {}

	public AstaDTO(Long id, ProductDTO product) {
		super();
		this.id = id;
		this.product = product;
	}

	

}
