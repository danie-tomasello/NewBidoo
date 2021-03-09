package com.innovat.asta.dto;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.multipart.MultipartFile;

import com.innovat.asta.model.Asta;
import com.innovat.asta.model.ImageModel;
import com.innovat.asta.model.Product;

public final class BeanFactory {
	
	private BeanFactory() {}
	
	public static AstaDTO createDtoAsta(Asta asta) {
        return new AstaDTO(
                asta.getId(),
                createDtoProduct(asta.getProduct())
        );
    }
	
	public static ProductDTO createDtoProduct(Product product) {
		return new ProductDTO(
				product.getId(),
				product.getName(),
				product.getBrand(),
				product.getPrice(),
				null,
				null,
				decompressImage(product.getImageList()),
				product.getCategories()
		);
	}
	
	public static Asta createAsta(AstaDTO astaDto) {
        return new Asta(
                astaDto.getId(),
                createProduct(astaDto.getProduct())
        );
    }
	
	public static Product createProduct(ProductDTO productDto) {
		Product p = new Product(
				productDto.getId(),
				productDto.getName(),
				productDto.getBrand(),
				productDto.getPrice(),
				null,
				null,
				productDto.getCategories()
		);
		
		p.setImageList(compressImage(productDto.getImageInputList(),p));
		
		return p;
		
		
	}

 static List<ImageModel> compressImage(List<MultipartFile> imageInputList,Product p) {
        return imageInputList.stream()
                .map(img -> {
					try {
						return new ImageModel(img.getOriginalFilename(),img.getContentType(),ImageCompres.compressBytes(img.getBytes()), p);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						return new ImageModel();
					}
				})
                .collect(Collectors.toList());
    }
	
	private static List<ImageModel> decompressImage(List<ImageModel> imageList) {
        return imageList.stream()
                .map(img -> new ImageModel(img.getName(),img.getType(),ImageCompres.decompressBytes(img.getPicByte())))
                .collect(Collectors.toList());
    }

}
