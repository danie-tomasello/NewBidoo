package com.innovat.asta.model;

import java.util.Base64;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "image_table")
public class ImageModel {

  public ImageModel() {

        super();

    }

   public ImageModel(String name, String type, byte[] picByte, Product product) {

        this.name = name;

        this.type = type;

        this.picByte = picByte;

        this.product = product;
    }

   public ImageModel(String name, String type, byte[] picByte) {

       this.name = name;

       this.type = type;

       this.picByte = picByte;

   }
   
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "name")
    private String name;
    
    @Column(name = "type")
    private String type;
    //image bytes can have large lengths so we specify a value
    //which is more than the default length for picByte column
    @Column(name = "picByte", length = 1000000000)
    private byte[] picByte;
    
    @ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="PRODUCT_ID",nullable=true)
    private Product product;
    
   
    
    public String getImgData() {
        return Base64.getMimeEncoder().encodeToString(picByte);
    }
}
